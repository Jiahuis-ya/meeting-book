<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'
import { userListService, userInfoUpdateService, deleteUserService, adminAddUserService } from '@/api/user.js'
import { ElMessage, ElMessageBox } from 'element-plus'

//用户列表数据模型
const userInfo = ref([])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(10)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    getList()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    getList()
}

const searchData = ref({});
const searchFormRef = ref();
const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
}

//获取列表数据
const getList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        userId: searchData.value.userId || null,
        username: (searchData.value.username || '').trim() || null,
        phone: (searchData.value.phone || '').trim() || null
    }
    let result = await userListService(params);

    //渲染视图
    total.value = result.data.total;

    userInfo.value = (result.data.items || []).map((i, index)=> {
      return {
        ...i,
        userRoleName: i.userRole === 'ADMIN' ? '超级管理员' : '普通用户'
      }
    })
  
}

getList();

import { createUserTool } from '@/utils/common.js'
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const userModel = ref(createUserTool())

//清空模型的数据
const clearData = () => {
  userModel.value = createUserTool();
}

//控制添加用户弹窗
const dialogVisible = ref(false);
//定义变量,控制标题的展示
const title = ref('');
// 定义操作状态
const isAdd = ref(true);
const ruleFormRef = ref();
// 自定义校验邮箱
const checkEmailPass = (rule, value, callback) => {
  let pattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
  const pass = pattern.test(value);
  if (value && !pass) {
    callback(new Error("邮箱格式错误"));
  } else {
    callback();
  }
}

// 自定义校验手机号
const checkPhonePass = (rule, value, callback) => {
  let pattern = /(^1\d{10}$)|(^[0-9]\d{7}$)/;
  const pass = pattern.test(value);
  if (value && !pass) {
    callback(new Error("邮箱格式错误"));
  } else if(!value) {
    callback(new Error("请输入手机号"));
  } else {
    callback();
  }
}


//添加用户表单校验
const rules = {
    username: [
      { required: true, message: '请输入' + '用户名', trigger: 'blur' },
    ],
    email: [
      { required: true, message: '请输入' + '邮箱', trigger: 'blur' },
      { validator: checkEmailPass, trigger: 'blur' }
    ],
    phone: [
      { required: true, message: '请输入' + '手机号', trigger: 'blur' },
      { validator: checkPhonePass, trigger: 'blur' }
    ],
    nickname: [
      { required: true, message: '请输入' + '昵称', trigger: 'blur' },
    ]
}

// 删除用户
const handleDelete = async (row) => {
  ElMessageBox.confirm(
        '你确认要删除该用户吗?',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            //调用接口
            let result = await deleteUserService(row.id);
            if(result?.code === 0) {
              ElMessage.success('删除成功');
              //刷新列表
              getList();
            } else {
              ElMessage.error('删除失败')
            }
        }).catch(() => {})
}

//展示编辑弹窗
const showDialog = (row) => {
    dialogVisible.value = true;
    title.value = '编辑用户'
    //数据拷贝
    userModel.value = {...row};
}

const handleUpdate = async() => {
  let result = await userInfoUpdateService(userModel.value);

  if(result?.code === 0) {
    ElMessage.success('操作成功')
      dialogVisible.value = false;
      getList();
  } else {
    ElMessage.error('操作失败')
  }
   
}


const handleAdd = async() => {
  let result = await adminAddUserService(userModel.value);

  if(result?.code === 0) {
    ElMessage.success('操作成功')
      dialogVisible.value = false;
      getList();
  } else {
    ElMessage.error('操作失败')
  }
}

// 提交表单
const submitForm = (formEl) => {
  console.log('userModel.value', userModel.value)

  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      title.value == '添加用户' ? handleAdd() : handleUpdate()
    } else {
      
    }
  })
}

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>用户管理</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible = true; title = '添加用户'; isAdd = true; clearData()">添加用户</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form 
          inline 
          ref="searchFormRef" 
          :model="searchData" 
        >
            <el-form-item label="工号：" prop="userId">
                <el-input v-model="searchData.userId" placeholder="请输入" clearable></el-input>
            </el-form-item>

            <el-form-item label="用户名：" prop="username">
              <el-input v-model="searchData.username" placeholder="请输入" clearable></el-input>
            </el-form-item>
            <el-form-item label="手机号：" prop="phone">
              <el-input v-model="searchData.phone" placeholder="请输入" clearable></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="getList">搜索</el-button>
                <el-button @click="resetForm(searchFormRef)">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 用户列表 -->
        <el-table :data="userInfo" style="width: 100%">
            <el-table-column label="工号" prop="id" width="80px"></el-table-column>
            <el-table-column label="用户名" prop="username"></el-table-column>
            <el-table-column label="用户角色" prop="userRoleName"></el-table-column>
            <el-table-column label="手机号" prop="phone"  width="150px"></el-table-column>
            <el-table-column label="部门" prop="department"></el-table-column>
            <el-table-column label="职位" prop="position"></el-table-column>
            <el-table-column label="头像" prop="userPic">
              <template #default="{ row }">
                <el-image
                  style="width: auto; height: 40px; border:none;cursor: pointer;" 
                  v-if="row?.userPic"
                  :src="row?.userPic" 
                  :preview-src-list="[row?.userPic]"
                  preview-teleported
                ></el-image>
              </template>
            </el-table-column>
            <el-table-column label="邮箱" prop="email" width="150px"></el-table-column>
            <el-table-column label="注册时间" prop="createTime" width="200px"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button 
                    :icon="Edit" 
                    circle 
                    plain 
                    type="primary"
                    @click="isAdd = false; showDialog(row)"
                    ></el-button>
                    <el-button 
                    :icon="Delete" 
                    circle 
                    plain 
                    type="danger"
                    @click="handleDelete(row)"
                    ></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[10, 15, 20]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />

       <!-- 添加用户弹窗 -->
       <el-dialog 
       v-model="dialogVisible" 
       :title="title" 
       width="750px"
       destroy-on-close
       >
            <el-form 
              :model="userModel" 
              :rules="rules"
              inline
              ref="ruleFormRef" 
              label-width="100px" 
              style="padding-right: 30px"
            >
                <el-form-item label="用户名" prop="username">
                    <el-input
                      v-model="userModel.username"
                      placeholder="请输入"
                      minlength="2" 
                      maxlength="16"
                      :disabled="!isAdd"
                      clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="用户角色" prop="userRole">
                    <el-select 
                      placeholder="请选择"
                      v-model="userModel.userRole"
                    >
                        <el-option label="超级管理员" value="ADMIN"></el-option>
                        <el-option label="普通用户" value="USER"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input
                      v-model="userModel.phone"
                      placeholder="请输入"
                      :disabled="!isAdd"
                      clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="部门" prop="department">
                    <el-input
                      v-model="userModel.department"
                      placeholder="请输入"
                      clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="职位" prop="position">
                    <el-input
                      v-model="userModel.position"
                      placeholder="请输入"
                      clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="昵称" prop="nickname">
                    <el-input
                      v-model="userModel.nickname"
                      placeholder="请输入"
                      clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input
                      v-model="userModel.email"
                      placeholder="请输入"
                      clearable
                    ></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button 
                    type="primary"
                    @click="submitForm(ruleFormRef)"
                    > 确认 </el-button>
                </span>
            </template>
        </el-dialog>


    </el-card>
</template>

<style lang="scss" scoped>

  ::v-deep(.el-input.el-input--suffix)  {
      width: 190px;
  }

  ::v-deep(.el-image__inner.el-image__preview) {
    border-radius: 50%;
  }

  .page-container {
      min-height: 100%;
      box-sizing: border-box;

      .header {
          display: flex;
          align-items: center;
          justify-content: space-between;
      }
  }

  .editor {
      width: 100%;

      :deep(.ql-editor) {
          min-height: 200px;
      }
  }

  // 头像

  .demo-type {
    display: flex;
  }
  .demo-type > div {
    flex: 1;
    text-align: center;
  }

  .demo-type > div:not(:last-child) {
    border-right: 1px solid var(--el-border-color);
  }

</style>