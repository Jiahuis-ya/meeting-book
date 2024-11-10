<script setup>
  import {
      Edit,
      Delete
  } from '@element-plus/icons-vue'

  import { ref } from 'vue'
  import { roomListService, addRoomService, deleteRoomService, roomInfoUpdateService } from '@/api/room.js'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import useUserInfoStore from '@/stores/userInfo.js'

  const userInfoStore = useUserInfoStore();
  const userInfo = ref({...userInfoStore.info})

  //列表数据模型
  const dataInfo = ref([])

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

  //获取会议室列表数据
  const getList = async () => {
      let params = {
          pageNum: pageNum.value,
          pageSize: pageSize.value,
          roomname: searchData.value.roomname || null,
          username: searchData.value.username || null
      }
      let result = await roomListService(params);

      //渲染视图
      total.value = result.data.total;
      dataInfo.value = (result.data.items || [])
  }


  getList();

  import { createRoomTool } from '@/utils/common.js'

  //添加表单数据模型
  const addOrEditModel = ref(createRoomTool())

  //清空模型的数据
  const clearData = () => {
    addOrEditModel.value = createRoomTool();
  }

  //控制添加会议室弹窗
  const dialogVisible = ref(false);
  //定义变量,控制标题的展示
  const title = ref('');
  // 定义操作状态
  const isAdd = ref(true);
  const ruleFormRef = ref();


  // 自定义校验手机号
  const checkPhonePass = (rule, value, callback) => {
    let pattern = /(^1\d{10}$)|(^[0-9]\d{7}$)/;
    const pass = pattern.test(value);
    if (value && !pass) {
      callback(new Error("手机号格式错误"));
    } else {
      callback();
    }
  }

  //添加会议室表单校验
  const rules = {
      roomname: [
        { required: true, message: '请输入' + '会议室名称', trigger: 'blur' },
      ],
      location: [
        { required: true, message: '请输入' + '地点', trigger: 'blur' },
      ],
      capacity: [
        { required: true, message: '请输入' + '容纳人数', trigger: 'blur' },
      ],
      phone: [
        { validator: checkPhonePass, trigger: 'blur' }
      ]
  }

  // 删除会议室
  const handleDelete = async (row) => {
    ElMessageBox.confirm(
          '你确认要删除该会议室吗?',
          '温馨提示',
          {
              confirmButtonText: '确认',
              cancelButtonText: '取消',
              type: 'warning',
          }
      )
          .then(async () => {
              //调用接口
              let result = await deleteRoomService(row.id);
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
      title.value = '编辑会议室'
      //数据拷贝
      addOrEditModel.value = {...row};
      //扩展id属性,将来需要传递给后台,完成分类的修改
      // addOrEditModel.value.id = row.id
  }

  const handleUpdate = async() => {
    const params = {
      ...addOrEditModel.value,
      
    }

    let result = await roomInfoUpdateService(params);

    if(result?.code === 0) {
      ElMessage.success('操作成功')
        dialogVisible.value = false;
        getList();
    } else {
      ElMessage.error('操作失败')
    }
    
  }

  const handleAdd = async() => {
    const params = {
      ...(addOrEditModel.value),
      createrid: userInfo.value.id,
      creater: userInfo.value.username
    }
    let result = await addRoomService(params);

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
    if (!formEl) return
    formEl.validate((valid) => {
      if (valid) {
        title.value == '添加会议室' ? handleAdd() : handleUpdate()
      } else {
        
      }
    })
  }

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>会议室信息管理</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible = true; title = '添加会议室'; isAdd = true; clearData()">添加会议室</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form 
          inline 
          ref="searchFormRef" 
          :model="searchData" 
        >
            <el-form-item label="负责人：" prop="username">
                <el-input v-model="searchData.username" placeholder='请输入' clearable></el-input>
            </el-form-item>

            <el-form-item label="会议室：" prop="roomname">
              <el-input v-model="searchData.roomname" placeholder="请输入" clearable></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="getList">搜索</el-button>
                <el-button @click="resetForm(searchFormRef)">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 会议室列表 -->
        <el-table :data="dataInfo" style="width: 100%">
            <el-table-column label="会议室号" prop="id" width="80px"></el-table-column>
            <el-table-column label="会议室名称" prop="roomname"></el-table-column>
            <el-table-column label="容纳人数" prop="capacity"></el-table-column>
            <el-table-column label="地点" prop="location"></el-table-column>
            <el-table-column label="负责人" prop="username"></el-table-column>
            <el-table-column label="手机号" prop="phone"></el-table-column>
            <el-table-column label="修改时间" prop="updateTime"></el-table-column>
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

       <!-- 添加会议室弹窗 -->
      <el-dialog 
        v-model="dialogVisible" 
        :title="title" 
        width="750px"
        destroy-on-close
      >
            <el-form 
              :model="addOrEditModel" 
              :rules="rules"
              inline
              ref="ruleFormRef" 
              label-width="100px" 
              style="padding-right: 30px"
            >
                <el-form-item label="会议室名称" prop="roomname">
                    <el-input
                      v-model="addOrEditModel.roomname"
                      placeholder="请输入"
                    ></el-input>
                </el-form-item>
                <el-form-item label="容纳人数" prop="capacity">
                  <el-input
                      v-model="addOrEditModel.capacity"
                      placeholder="请输入"
                      type="number"
                    ></el-input>
                </el-form-item>
                
                <el-form-item label="地点" prop="location">
                    <el-input
                      v-model="addOrEditModel.location"
                      placeholder="请输入"
                    ></el-input>
                </el-form-item>
                <el-form-item label="负责人" prop="username">
                    <el-input
                      v-model="addOrEditModel.username"
                      placeholder="请输入"
                    ></el-input>
                </el-form-item>
                <el-form-item label="负责人电话" prop="phone">
                    <el-input
                      v-model="addOrEditModel.phone"
                      placeholder="请输入"
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

  .page-container {
      min-height: 100%;
      box-sizing: border-box;

      .header {
          display: flex;
          align-items: center;
          justify-content: space-between;
      }
  }
</style>