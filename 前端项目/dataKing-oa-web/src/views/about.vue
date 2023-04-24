<template>
  <div class="container">
    <van-nav-bar
      title="关于我"
    />
    <div class="detail-wrap">
      <div class="item">
        一名刚刚上岸的准研究生，为了找到一个实习工作，花费一个多月完成的OA项目...
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "process",

  data() {
    return {
    };
  },

  created(){
  },

  methods: {

  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 20px;
  .detail-wrap {
    .item {
      color: #838485;
      
    }
  }
}
</style>
