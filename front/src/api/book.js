//导入request.js请求工具
import request from '@/utils/request.js'


// 预约审核
export const roomBookApproveListService = (params)=>{
    return  request.get('/roomBook/listApprove',{params})
 }

//  预约记录
export const roomBookRecordListService = (params)=>{
    return  request.get('/roomBook/listRecord',{params})
 }

// 新增会议室预约
 export const addRoomBookService = (roomData)=>{
    return  request.post('/roomBook/add', roomData)
 }


 // 删除&取消会议室预约
export const deleteRoomBookService = (id)=>{
    return request.delete('/roomBook/delete?id='+id)
}

 // 取消预约
export const cancelRoomBookService = (params)=>{
    return request.put('/roomBook/cancel', params)
}

 // 审核通过&&不通过
export const approveRoomBookService = (params)=>{
    return request.put('/roomBook/approve', params)
}