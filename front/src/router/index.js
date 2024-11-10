import { createRouter, createWebHistory } from 'vue-router'

//导入组件
import LoginVue from '@/views/Login.vue'
import LayoutVue from '@/views/Layout.vue'

import UserAvatarVue from '@/views/user/UserAvatar.vue'
import UserInfoVue from '@/views/user/UserInfo.vue'
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue'
import UserManageVue from '@/views/user/UserManage.vue'
import WelcomeVue from '@/components/Welcome.vue'
import RoomInfoVue from '@/views/room/RoomInfo.vue'
import RoomRecordVue from '@/views/room/RoomRecord.vue'
import RoomBookApproveVue from '@/views/room/RoomBookApprove.vue'
import RoomBookVue from '@/views/room/RoomBook.vue'

//定义路由关系
const routes = [
    { path: '/login', component: LoginVue },
    {
        path: '/', 
        component: LayoutVue,
        redirect:'/welcome',
        children: [
            { path: '/welcome', component: WelcomeVue },
            { path: '/user/info', component: UserInfoVue },
            { path: '/user/avatar', component: UserAvatarVue },
            { path: '/user/resetPassword', component: UserResetPasswordVue },
            { path: '/user/manage', component: UserManageVue },
            { path: '/room/info', component: RoomInfoVue },
            { path: '/room/book', component: RoomBookVue },
            { path: '/room/bookApprove', component: RoomBookApproveVue },
            { path: '/room/bookRecord', component: RoomRecordVue }
        ]
    }
]

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

//导出路由
export default router
