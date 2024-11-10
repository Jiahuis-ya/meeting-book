<script setup>
    import { ref } from 'vue'
    import useUserInfoStore from '@/stores/userInfo.js'
    const userInfoStore = useUserInfoStore();

    const userInfo = ref({...userInfoStore.info})
    const ruleFormRef = ref();

    const formData = ref({
        old_pwd: '',
        new_pwd: '',
        re_pwd: ''
    });

    //校验密码的函数
    const checkRePassword = (rule, value, callback) => {
        if (value === '') {
            callback(new Error('请再次确认密码'))
        } else if (value !== formData.value.new_pwd) {
            callback(new Error('请确保两次输入的密码一样'))
        } else {
            callback()
        }
    }

    const rules = {
        old_pwd: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
        ],
        new_pwd: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
        ],
        re_pwd: [
            { required: true, message: '请再次确认密码', trigger: 'blur' },
            { validator: checkRePassword, trigger: 'blur' }
        ]
    }

    //修改个人信息
    import {userPasswordUpdateService} from '@/api/user.js'
    import {ElMessage} from 'element-plus'
    import {useTokenStore} from '@/stores/token.js'
    const tokenStore = useTokenStore();
    import {useRouter} from 'vue-router'
    const router = useRouter()
    const onSubmit = (formEl)=>{
        console.log(formData.value);
        if (!formEl) return
        formEl.validate((valid) => {
            if (valid) {
                resetPassword();
            }
        })
    }

    const resetForm = (formEl) => {
        if (!formEl) return
        formEl.resetFields()
    }

    const resetPassword = async() => {
        const params = {
            old_pwd: formData.value.old_pwd,
            new_pwd: formData.value.new_pwd,
            re_pwd: formData.value.re_pwd
        }

        let result = await userPasswordUpdateService(params);

        //退出登录
        //1.清空pinia中存储的token以及个人信息
        tokenStore.removeToken()
        userInfoStore.removeInfo()

        //2.跳转到登录页面
        router.push('/login')
        ElMessage({
            type: 'success',
            message: '修改成功，请重新登录系统',
        })
    }
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>修改密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form 
                    :model="formData" 
                    :rules="rules" 
                    label-width="100px" 
                    size="large"
                    ref="ruleFormRef"
                >
                    <el-form-item label="用户名">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item 
                        label="旧密码" 
                        prop="old_pwd"
                    >
                        <el-input 
                            v-model="formData.old_pwd" 
                            show-password 
                            type="password"
                            autocomplete="off"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd">
                        <el-input 
                            v-model="formData.new_pwd"
                            show-password 
                            type="password"
                            autocomplete="off"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="确认新密码" prop="re_pwd">
                        <el-input
                            v-model="formData.re_pwd"
                            show-password 
                            type="password"
                            autocomplete="off"
                            clearable
                        ></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="onSubmit(ruleFormRef)">确定</el-button>
                        <el-button @click="resetForm(ruleFormRef)">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>