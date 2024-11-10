package com.itheima.controller;

import com.itheima.pojo.Category;
import com.itheima.pojo.Result;
import com.itheima.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @PostMapping // url 是/category  post请求
  public Result add(@RequestBody @Validated(Category.Add.class) Category category){
    categoryService.add(category);
    return Result.success();
  }

  @GetMapping // url 是/category  get请求
//  页面接收到的是数组
  public Result<List<Category>> list(){
    List<Category> cs = categoryService.list();
    return Result.success(cs);
  }

  @GetMapping("/detail")
  public Result<Category> detail(Integer id){
    Category c = categoryService.findById(id);
    return Result.success(c);
  }

  @PutMapping // url 是/category  put请求
  public Result update(@RequestBody @Validated(Category.Update.class) Category category){
    categoryService.update(category);
    return Result.success();
  }

  @DeleteMapping
  public Result delete(Integer id){
    categoryService.deleteById(id);
    return Result.success();
  }
}
