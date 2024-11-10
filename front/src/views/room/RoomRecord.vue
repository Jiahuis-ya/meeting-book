<script setup>

  import { ref, computed } from 'vue'
  import {
    roomBookRecordListService,
   } from '@/api/book.js'
  
  import useUserInfoStore from '@/stores/userInfo.js'
  const userInfoStore = useUserInfoStore();
  const userInfo = ref({...userInfoStore.info})

  const isAdmin = computed(() => {  
      return userInfo.value.userRole === 'ADMIN';  
    });  

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

      let result = await roomBookRecordListService(isAdmin.value ? params1 : params2);

      //渲染视图
      total.value = result.data.total;
      dataInfo.value = (result.data.items || [])
  }


  getList();



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
            <el-table-column label="预约状态" prop="bookstate"></el-table-column>
            <el-table-column label="审核状态" prop="state"></el-table-column>
            <el-table-column label="开始时间" prop="startTime"></el-table-column>
            <el-table-column label="结束时间" prop="endTime"></el-table-column>
            <el-table-column label="修改时间" prop="updateTime"></el-table-column>
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