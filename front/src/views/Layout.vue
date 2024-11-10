<script setup>

import { ref, onMounted, watch } from 'vue'

onMounted(() => {
  console.log(`the component is now mounted.`)
})
import {
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom,
    Location,
    Menu as IconMenu,
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'

import {userInfoService} from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import {useTokenStore} from '@/stores/token.js'
const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
//调用函数,获取用户详细信息
const getUserInfo = async()=>{
    //调用接口
    let result = await userInfoService();
    //数据存储到pinia中
    userInfoStore.setInfo(result.data);
}

getUserInfo();
//条目被点击后,调用的函数
import {useRouter, useRoute} from 'vue-router'
const router = useRouter();
import {ElMessage,ElMessageBox} from 'element-plus'
const handleCommand = (command)=>{
    //判断指令
    if(command === 'logout'){
        //退出登录
        ElMessageBox.confirm(
        '您确认要退出吗?',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            //退出登录
            //1.清空pinia中存储的token以及个人信息
            tokenStore.removeToken()
            userInfoStore.removeInfo()

            //2.跳转到登录页面
            router.push('/login')
            ElMessage({
                type: 'success',
                message: '退出登录成功',
            })
            
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '用户取消了退出登录',
            })
        })
    }else{
        //路由
        router.push('/user/'+command)
    }
}

const activeIndex = ref('/welcome')
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

const route = useRoute();  
watch(() => route.path, (newPath, oldPath) => {  
//   console.log(`路由路径从 ${oldPath} 变为 ${newPath}`);  
  activeIndex.value = newPath;
}, {  
  immediate: true // 组件挂载时立即触发一次回调  
});  

</script>

<template>
    <!-- element-plus中的容器 -->
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <!-- element-plus的菜单标签 -->
            <el-menu
                :default-active="activeIndex"
                active-text-color="#409eff" 
                background-color="#fff"  
                text-color="#333"
                router
                @select="handleSelect"
            >
                <el-menu-item index="/welcome">
                    <el-icon>
                        <Location />
                    </el-icon>
                    <span>首页</span>
                </el-menu-item>
               
                <el-sub-menu index="1-3">
                    <template #title>
                        <el-icon><icon-menu /></el-icon>
                        <span>会议室管理</span>
                    </template>
                    <el-menu-item index="/room/info">
                        <span>会议室信息管理</span>
                    </el-menu-item>
                    <el-menu-item index="/room/book">
                        <span>预约申请</span>
                    </el-menu-item>
                    <el-menu-item index="/room/bookApprove">
                        <span>预约审核</span>
                    </el-menu-item>
                    <el-menu-item index="/room/bookRecord">
                        <span>预约记录</span>
                    </el-menu-item>
                </el-sub-menu>
                <el-sub-menu index="1-4">
                    <template #title>
                        <el-icon><icon-menu /></el-icon>
                        <span>用户管理</span>
                    </template>
                    <el-menu-item index="/user/manage">
                        <span>用户信息管理</span>
                    </el-menu-item>
                    
                </el-sub-menu>
                <el-sub-menu index="1-5">
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item index="/user/info">
                        <span>我的账号</span>
                    </el-menu-item>
                    <el-menu-item index="/user/avatar">
                        <span>头像设置</span>
                    </el-menu-item>
                    <el-menu-item index="/user/resetPassword">
                        <span>修改密码</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header class="top_header">
                <div class="system_name">会议室预约管理系统</div>
                <div class="header_right">
                    <span class="nickname">您好，{{ userInfoStore.info.nickname }}</span>
                <!-- 下拉菜单 -->
                <!-- command: 条目被点击后会触发,在事件函数上可以声明一个参数,接收条目对应的指令 -->
                <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.info.userPic? userInfoStore.info.userPic:avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="info" :icon="User">我的账号</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">头像设置</el-dropdown-item>
                            <el-dropdown-item command="resetPassword" :icon="EditPen">修改密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
                </div>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <!-- <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    内容展示区
                </div> -->
                <router-view></router-view>
            </el-main>
            <!-- 底部区域 -->
            <!-- <el-footer>会议室预约管理系统 ©2024 Created by 吴家惠</el-footer> -->
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
::v-deep(.el-menu-item.is-active)  {
    background-color: #8cc5ff38 !important;
    color: #409eff !important;
    border-right: 3px solid #409eff;
}


::v-deep(.el-menu-item:hover) {
    background-color: #8cc5ff38 !important;
}

::v-deep(.el-sub-menu__title:hover) {
    background-color: #8cc5ff38 !important;
}

.el-footer {
    line-height: 60px;
}

.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #fff;

        &__logo {
            height: 60px;
            background: #fff;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    // .el-footer {
    //     display: flex;
    //     align-items: center;
    //     justify-content: center;
    //     font-size: 14px;
    //     color: #666;
    // }

    .top_header {
        position: relative;
        width: 100vw;
        left: -200px;
        box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
        display: flex;
        justify-content: space-between;

        .system_name {
            font-weight: bold;
            font-size: 24px;
        }

        .header_right {
            display: flex;
            flex-direction: row;
            align-items: center;

            .nickname {
                margin-right: 20px;
            }
        }

    }

    
   
}
</style>