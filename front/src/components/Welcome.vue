<template>
  
  <div class="welcome">
      <div class="title">欢迎使用会议室预约管理系统</div>
  </div>
</template>

<style lang="scss" scoped>
.welcome {
  position: relative;
  display: flex;
  height: 100%;
  background: url('@/assets/welcome_bg.jpg') no-repeat center / cover;
  background-size: 40% 50%;
  background-position: 85%;

  .title {
    color: #666;
    position: absolute;
    top: 20%;
    left: 10%;
    font-size: 30px;

  }
}
</style>