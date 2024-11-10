<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import {ref} from 'vue'
import avatar from '@/assets/default.png'
const uploadRef = ref()
import {useTokenStore} from '@/stores/token.js'
const tokenStore = useTokenStore();

import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStore = useUserInfoStore();

//用户头像地址
const imgUrl= ref(userInfoStore.info.userPic)

//图片上传成功的回调函数
const uploadSuccess = (result)=>{
    imgUrl.value = result.data;
}

const beforeAvatarUpload = (file) => {
    const suffix = file.name.split('.')[1]
    const size = file.size / 1024 / 1024 < 2
    if(['png','jpeg','jpg'].indexOf(suffix) < 0){
        ElMessage.error('上传图片只支持 png、jpeg、jpg 格式！');
        return false
    }
    if(!size){
        ElMessage.error('上传文件大小不能超过 2MB!');
        return false
    }
    return true
}

import {userAvatarUpdateService} from '@/api/user.js'
import {ElMessage} from 'element-plus'
//头像修改
const updateAvatar = async ()=>{

    if(!imgUrl.value) {
        ElMessage.warning("请上传头像");
        return
    }
    
    //调用接口
    let result = await userAvatarUpdateService(imgUrl.value);

    ElMessage.success(result.msg? result.msg:'修改成功')

    //修改pinia中的数据
    userInfoStore.info.userPic = imgUrl.value
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>头像设置</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <div class="tip">请选择本地图片，上传编辑自己的头像</div>

                <el-row>
                    <el-button 
                    :icon="Plus" 
                    size="large" 
                     @click="uploadRef.$el.querySelector('input').click()"
                     class="choose_btn"
                     >
                        选择图片
                    </el-button>
                    <div class="choose_text">支持png、jpeg、jpg格式的图片</div>
                </el-row>
                <el-row style="margin-top: 200px;">
                    <el-button type="primary" :icon="Upload" size="large" @click="updateAvatar">
                    保存头像
                </el-button>
                </el-row>
            </el-col>
            
            <el-col :span="12" class="right_row">
                <el-row>
                    <div class="preview">头像预览</div>
                </el-row>
                <el-row>

                     <el-upload 
                        ref="uploadRef"
                        class="avatar-uploader" 
                        :show-file-list="false"
                        :auto-upload="true"
                        action="/api/upload"
                        name="file"
                        :headers="{'Authorization':tokenStore.token}"
                        :on-success="uploadSuccess"
                        :before-upload="beforeAvatarUpload"
                        >
                        <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                        <img v-else :src="avatar" width="278" />
                    </el-upload>
                </el-row>
               
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.tip {
    margin-bottom: 24px;
    color: #999;
}

.choose_text {
    color: #999;
    line-height: 40px;
}

.choose_btn {
    margin-right: 20px;
}
.preview {
    margin-bottom: 24px;
    color: #333;
    font-size: 24px;
}

.right_row {
    padding-left: 20px;
    border-left: 1px solid var(--el-card-border-color);
}
.avatar-uploader {
    :deep() {
        .avatar {
            width: 278px;
            height: 278px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 278px;
            height: 278px;
            text-align: center;
        }
    }
}
</style>