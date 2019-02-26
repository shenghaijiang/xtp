<template>
  <section class="app-main" >
    <!--<transition name="fade" mode="out-in">-->
      <keep-alive>
        <router-view v-if="isKeepAlive"></router-view>
      </keep-alive>
      <router-view v-if="!isKeepAlive"></router-view>
    <!--</transition>-->
  </section>
</template>

<script>
export default {
  name: "AppMain",
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews;
    },
    isKeepAlive() {
      const route = this.$store.state.tagsView.cachedViews.find((cachedItem) => cachedItem === this.$route.name);
      if (route) {return true;}
      return false;
    }
    // key() {
    //   return this.$route.name !== undefined ? this.$route.name + +new Date() : this.$route + +new Date()
    // }
  }
};
</script>

<style lang="scss">
</style>
