<template>
  <div>
    <div>账号切换</div>
    <button @click="admin()" type="default" size="mini">admin</button>
    <button @click="lrsjl()" type="default" size="mini">李人事经理</button>
    <button @click="zhangsan()" type="default" size="mini">张三</button>
    <button @click="lisi()" type="default" size="mini">李四</button>
    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    admin() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwNjE3MjCy0FEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQCx1CYRQgAAAA.UydnSpc5wZI8BKxlVBT7qT3sjmNsTs-yOXVsA-_qulTGgo3H-fa_RrCUx9MugRkuYARO1GGKAubApxgGMOb5wg'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zhangsan() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwNjGzsDAw1FEqLU4t8kxRsjKHMPMSc1OBWqoyEvPSixPzlGoBJXJS9UUAAAA.9-e0vtHfrlDUae8oCJ54D7mRI12zYvcNQ_Xiu7qjs-Du3qkyRqyZuJYF5RA9JSNASZoZ8sK5ut4wPLoJ3t0IPQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zzjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDM3NjMwNLYw11EqLU4t8kxRsjKFMPMSc1OBWqqqsnKUagGHebUNQQAAAA.FcCkD6QfaP8bd_3MYsRD6Tp9wr9JSRFWIUuIdG5O7XxhIeyWfzGLp40_Lesx_kWMMaaskh_ba3NrTlj8-nP6OA'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    lisi() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwNjGzMDEx0VEqLU4t8kxRsoIy8xJzU4FacjKLM5VqAVp9M_dBAAAA.Ufy5JQR0UfrxWXWBqBikff0gigpaFivoo2DjCoAWDgJbRQJfRm1m9AetKKUfnscQ1eIGbdMrAlBNBv-XPpSeIw'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
