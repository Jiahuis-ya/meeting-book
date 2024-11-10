package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {
    @Value("${reggie.path}")
    private String basePath;
   @PostMapping("/upload")
   public Result<String> upload(MultipartFile file) throws Exception {
       //把文件的内容存储到本地磁盘上
       String originalFilename = file.getOriginalFilename();
       //保证文件的名字是唯一的,从而防止文件覆盖
       String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
    //   file.transferTo(new File("C:\\Users\\28892\\Desktop\\files\\"+filename));
       String url = AliOssUtil.uploadFile(filename,file.getInputStream());
       return Result.success(url);

   }


    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 图片上传
     */
    @PostMapping("/upload2")
    public Result<String> upload2(MultipartFile file){
        //获取上传的原始图片名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成图片的名称，防止文件名称重复造成图片覆盖
        String fileName = UUID.randomUUID().toString() + suffix;
        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，则创建
            dir.mkdirs();
        }
        try {
            //将上传的图片转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(fileName);
    }
}
