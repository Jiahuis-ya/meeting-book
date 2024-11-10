<script setup>

  import { ref, computed } from 'vue'
  import {
    roomBookApproveListService,
    cancelRoomBookService,
    approveRoomBookService
   } from '@/api/book.js'
  
  import { ElMessage } from 'element-plus'
  import useUserInfoStore from '@/stores/userInfo.js'
  import {useRouter} from 'vue-router'
  const router = useRouter()
  const userInfoStore = useUserInfoStore();
  const userInfo = ref({...userInfoStore.info})

  const isAdmin = computed(() => {  
      return userInfo.value.userRole === 'ADMIN';  
    });  

  //会议室搜索时选中的发布状态
  const state = ref('')

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

  //获取列表数据
  const getList = async () => {
      let commonParams = {
          pageNum: pageNum.value,
          pageSize: pageSize.value,
          roomname: (searchData.value.roomname || '').trim() || null,
      }
      // 管理员参数
      const params1 = {
        ...commonParams,
          booker: (searchData.value.booker || '').trim() || null,
          bookphone: (searchData.value.bookphone || '').trim() || null,
      }
      // 当前预约人参数
      const params2 = {
        ...commonParams,
        bookerid: userInfo.value.id
      }

      let result = await roomBookApproveListService(isAdmin.value ? params1 : params2);

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
      ],
      booker: [
        { required: true, message: '请输入' + '预约人', trigger: 'blur' },
      ],
      bookphone: [
        { required: true, message: '请输入' + '预约电话', trigger: 'blur' },
        { validator: checkPhonePass, trigger: 'blur' }
      ]
  }

  //展示编辑弹窗
  const showDialog = (row) => {
      dialogVisible.value = true;
      title.value = '信息'
      //数据拷贝
      addOrEditModel.value = {...row};
      
      
  }





 
// 取消预约
  const handleCancel = async(row) => {
    let result = await cancelRoomBookService({
      ...row,
      bookstate: '已取消'
    });
      if(result?.code === 0) {
        ElMessage.success('取消成功');
        //刷新列表
        getList();
      } else {
        ElMessage.error('取消失败');
      }
  }

  
  const handleApprove = async(row, state) => {
    let result = await approveRoomBookService({
      ...row,
      state
    });
      if(result?.code === 0) {
        ElMessage.success('操作成功');
        //刷新列表
        getList();
      } else {
        ElMessage.error('操作失败');
      }
  }

</script>
<template>
    <el-card class="page-container">
      <template #header>
            <div class="header">
                <span>预约记录</span>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form 
          inline 
          ref="searchFormRef" 
          :model="searchData" 
        >
            <el-form-item label="会议室：" prop="roomname">
              <el-input v-model="searchData.roomname" placeholder="请输入" clearable></el-input>
            </el-form-item>
            <template v-if="!!isAdmin">
                <el-form-item label="预约人：" prop="booker">
                <el-input v-model="searchData.booker" placeholder='请输入' clearable></el-input>
                </el-form-item>
                <el-form-item label="预约人电话：" prop="bookphone">
                    <el-input v-model="searchData.bookphone" placeholder='请输入' clearable></el-input>
                </el-form-item>
            </template>
            
            
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
            <el-table-column label="预约人" prop="booker"></el-table-column>
            <el-table-column label="预约电话" prop="bookphone"></el-table-column>
            <el-table-column label="审核状态" prop="state"></el-table-column>
            <el-table-column label="开始时间" prop="startTime"></el-table-column>
            <el-table-column label="结束时间" prop="endTime"></el-table-column>
            <el-table-column label="修改时间" prop="updateTime"></el-table-column>

            <el-table-column label="操作" width="150">
                <template #default="{ row }">
                  <el-button
                    v-if=" !!isAdmin && ['待审批'].includes(row.state) && ['已预约'].includes(row.bookstate)"
                    type="success"
                    size="small"
                    @click="handleApprove(row, '已批准')"
                  >
                    通过
                  </el-button>
                  
                  <el-button 
                    v-if="!!isAdmin && ['待审批'].includes(row.state) && ['已预约'].includes(row.bookstate)"
                    type="danger"
                    size="small"
                    @click="handleApprove(row, '不批准')"
                  >
                    不通过
                  </el-button>

                  <el-popconfirm
                    width="200"
                    v-if="(!isAdmin || userInfo.id === row.bookerid) && ['待审批'].includes(row.state) && ['已预约'].includes(row.bookstate)"
                    title="确认取消预约吗？"
                    @confirm="handleCancel(row)"
                  >
                    <template #reference>
                      <el-button
                        type="primary"
                        size="small"
                        style="margin-top: 8px;"
                      >取消预约</el-button>
                    </template>
                  </el-popconfirm>
                    
                
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