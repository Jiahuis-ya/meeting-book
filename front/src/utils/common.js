export const createUserTool = () => {
  return {
    username: '', // 用户名
    userRole: 'USER', // 用户角色(ADMIN:管理员，USER:普通用户)
    phone: '', // 手机号
    department: '', // 部门
    position: '', // 职位
    // password: '', // 密码
    nickname: '', // 昵称
    email: '', // 邮箱
    userPic: '' // 头像
  }
}


export const createRoomTool = () => {
  return {
    roomname: '', // 会议室名称
    capacity: null, // 容纳人数
    location: '', // 地点
    username: '', // 负责人
    phone: '', // 负责人电话
  }
}