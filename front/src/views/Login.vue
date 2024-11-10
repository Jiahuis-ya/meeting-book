<script setup>
    import { User, Lock } from '@element-plus/icons-vue'
    import { ref } from 'vue'
    import { ElMessage } from 'element-plus'
    //控制注册与登录表单的显示， 默认显示注册
    const isRegister = ref(false)
    //定义数据模型
    const registerData = ref({
        username: '',
        password: '',
        rePassword: ''
    })

    //校验密码的函数
    const checkRePassword = (rule, value, callback) => {
        if (value === '') {
            callback(new Error('请再次确认密码'))
        } else if (value !== registerData.value.password) {
            callback(new Error('请确保两次输入的密码一样'))
        } else {
            callback()
        }
    }

    //定义表单校验规则
    const rules = {
        username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 2, max: 16, message: '长度为2~16位非空字符', trigger: 'blur' }
        ],
        password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
        ],
        rePassword: [
            { validator: checkRePassword, trigger: 'blur' }
        ]
    }

    //调用后台接口,完成注册
    import { userRegisterService, userLoginService} from '@/api/user.js'
    const handleRegister = async () => {
        let result = await userRegisterService(registerData.value);
        ElMessage.success(result.msg ? result.msg : '注册成功')
    }

    //绑定数据,复用注册表单的数据模型
    //表单数据校验
    //登录函数
    import {useTokenStore} from '@/stores/token.js'
    import {useRouter} from 'vue-router'
    const router = useRouter()
    const tokenStore = useTokenStore();
    const handleLogin = async ()=>{
        //调用接口,完成登录
        userLoginService(registerData.value).then(result => {
            ElMessage.success('登录成功');
            //把得到的token存储到pinia中
            tokenStore.setToken(result.data)
            //跳转到首页 路由完成跳转
            router.push('/')
        }).catch(err => {
            console.log(err, 'err')
            ElMessage.error(err?.message || '登录失败')
        })
    }

    //定义函数,清空数据模型的数据
    const clearRegisterData = ()=>{
        registerData.value={
            username:'',
            password:'',
            rePassword:''
        }
    }

    const ruleFormRefRegister = ref();
    const ruleFormRefLogin = ref();

    // 提交表单
    const submitForm = (formEl, type) => {
    console.log('registerData.value', registerData.value)

    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            type == 'login' ? handleLogin() : handleRegister()
        } else {
        
        }
    })
    }
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="ruleFormRefRegister" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码"
                        v-model="registerData.rePassword"></el-input>
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button 
                        class="button" 
                        type="primary" 
                        auto-insert-space 
                        @click="submitForm(ruleFormRefRegister, 'register')"
                    >
                        立即注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false;clearRegisterData()">
                        ← 已有账号点此登录
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登录表单 -->
            <el-form ref="ruleFormRefLogin" size="large" autocomplete="off" v-else :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <!-- <el-link type="primary" :underline="false">忘记密码？</el-link> -->
                    </div>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button 
                        class="button" 
                        type="primary" 
                        auto-insert-space 
                        @click="submitForm(ruleFormRefLogin, 'login')"
                    >登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true;clearRegisterData()">
                        先去注册 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>

::v-deep(.el-input__wrapper)  {
    border-bottom: 1px solid #999!important;
    border-radius: 0!important;
    box-shadow: none;
}

::v-deep(.el-input__prefix) {
    color: #409eff!important;
    font-weight: bold;
}

::v-deep(.el-button--large) {
    border-radius: 50px!important;
}

::v-deep(.el-button--primary) {
background: linear-gradient(to top right, #32a3ff, #32c9ff);

}

::v-deep(.el-form-item__content) {
    justify-content: center;
}


/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        background: url('@/assets/logo-bg.jpg') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>