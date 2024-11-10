//导入request.js请求工具
import request from '@/utils/request.js'

// //获取会议室详细信息
// export const roomInfoService = ()=>{
//     return request.get('/room/roomInfo')
// }

// 修改会议室信息
export const roomInfoUpdateService = (roomInfoData)=>{
   return request.put('/room/update', roomInfoData)
}

// 会议室列表查询
export const roomListService = (params)=>{
    return  request.get('/room/list',{params})
 }

// 新增会议室
 export const addRoomService = (roomData)=>{
    return  request.post('/room/add', roomData)
 }


 // 删除会议室
export const deleteRoomService = (id)=>{
    return request.delete('/room/delete?id='+id)
}