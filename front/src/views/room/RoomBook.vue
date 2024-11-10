<script setup>

  import { ref } from 'vue'
  import { roomListService } from '@/api/room.js'
  import { addRoomBookService } from '@/api/book.js'
  
  import { ElMessage } from 'element-plus'
  import useUserInfoStore from '@/stores/userInfo.js'
  import {useRouter} from 'vue-router'
  const router = useRouter()
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
          roomname: (searchData.value.roomname || '').trim() || null,
          username: (searchData.value.username || '').trim() || null
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


  //控制添加会议室弹窗
  const dialogVisible = ref(false);
  //定义变量,控制标题的展示
  const title = ref('');
  // 定义操作状态
  const isOpenBookWindow = ref(true);
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
    timeslot: [
        { required: true, message: '请输入' + '预约时间', trigger: 'blur' },
      ]
  }

  //展示编辑弹窗
  const showDialog = (row) => {
      dialogVisible.value = true;
      title.value = '信息'
      //数据拷贝
      addOrEditModel.value = {...row};
  }

  const handleBook = async() => {
    const params = {
        roomid: addOrEditModel.value.id,
        bookstate: '已预约',
        state: '待审批',
        bookerid: userInfo.value.id,
        startTime: addOrEditModel.value.timeslot[0],
        endTime: addOrEditModel.value.timeslot[1],
    };

    let result = await addRoomBookService(params);
      if(result?.code === 0) {
        ElMessage.success('预约成功')
        dialogVisible.value = false;
        router.push('/room/bookApprove')
      } else {
        ElMessage.error('预约失败')
      }
  }

  // 提交表单
  const submitForm = (formEl) => {
    if (!formEl) return
    formEl.validate((valid) => {
      if (valid) {
        handleBook();
      } else {
        
      }
    })
  }

</script>
<template>
    <el-card class="page-container">
      <template #header>
            <div class="header">
                <span>预约申请</span>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form 
          inline 
          ref="searchFormRef" 
          :model="searchData" 
        >
            <el-form-item label="创建人：" prop="username">
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
            <el-table-column label="容纳人数" prop="capacity" width="80px"></el-table-column>
            <el-table-column label="地点" prop="location"></el-table-column>
            <el-table-column label="负责人" prop="username" width="100px"></el-table-column>
            <el-table-column label="手机号" prop="phone"></el-table-column>
            <el-table-column label="修改时间" prop="updateTime"></el-table-column>
            <el-table-column label="操作" width="80">
                <template #default="{ row }">
                    <el-button 
                      type="primary"
                      size="small"
                      @click="isOpenBookWindow = true; showDialog(row)"
                    >预约</el-button>
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
            ref="ruleFormRef" 
            label-width="100px" 
            style="padding-right: 30px"
            class="book_form"
          >
              <el-form-item label="预约时间：" prop="timeslot">
                <el-date-picker
                  v-model="addOrEditModel.timeslot"
                  type="datetimerange"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  format="YYYY-MM-DD hh:mm:ss"
                  value-format="YYYY-MM-DD hh:mm:ss"
                />
              </el-form-item>
              <div style="display: flex;">
                <el-form-item label="会议室名称：" style="width: 50%;">
                  {{addOrEditModel.roomname}}
                </el-form-item>
                <el-form-item label="可容纳人数：" style="width: 50%;">
                  {{addOrEditModel.capacity}}
                </el-form-item>
              </div>
              <div style="display: flex;">
                <el-form-item label="地点：" style="width: 50%;">
                  {{addOrEditModel.location}}
                </el-form-item>
                <el-form-item label="负责人：" style="width: 50%;">
                  {{addOrEditModel.username}}
                </el-form-item>
              </div>
              <el-form-item label="负责人电话："  style="width: 50%;">
                {{addOrEditModel.phone}}
              </el-form-item>
              
             
          </el-form>
          <template #footer>
              <span class="dialog-footer">
                  <el-button @click="dialogVisible = false">取消</el-button>
                  <el-button 
                  type="primary"
                  @click="submitForm(ruleFormRef)"
                  > 确认预约 </el-button>
              </span>
          </template>
        </el-dialog>
    </el-card>
</template>
<style lang="scss" scoped>


  .book_form {
    ::v-deep(.el-input.el-input--suffix)  {
        width: 190px;
    }

    ::v-deep(.el-form-item__label) {
      width: 200px!important;
    }
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