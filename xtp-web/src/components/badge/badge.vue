<template>
  <div class="el-badge">
    <slot></slot>
    <transition name="el-zoom-in-center">
      <sup
        v-show="!hidden && ( content || isDot )"
        v-text="content"
        class="xt-content"
        :class="{ 'is-fixed': $slots.default, 'is-dot': isDot}+' xt-'+type">
      </sup>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'XTBadge',

  props: {
    value: {},
    max: Number,
    isDot: Boolean,
    hidden: Boolean,
    type:{
      type:String,
      default:"success"
    }
  },

  computed: {
    content() {
      if (this.isDot) return;

      const value = this.value;
      const max = this.max;

      if (typeof value === 'number' && typeof max === 'number') {
        return max < value ? `${max}+` : value;
      }

      return value;
    }
  }
};
</script>

<style scoped>
  .xt-content{
    /*background-color:#ff4949;*/
    border-radius:20px;
    color:#fff;
    display:inline-block;
    font-size:12px;
    height:18px;
    line-height:18px;
    padding:0 6px;
    text-align:center;
    white-space:nowrap;
    border:1px solid #fff;
  /*  margin-top: 10px;
    margin-right: 40px;*/
  }
  .xt-success{
    background-color: #00b900;
  }
  .xt-error{
    background-color: red;
  }
</style>
