
<template>
    <div>
        <el-col :span="24" class="toolbar xt-search search-box" >
            <div class="xt-search-items">
                <slot></slot>
            </div>
            <div class="xt-search-button">
                <el-button @click="searchClick" type="info" size="small">查询</el-button>
                <el-button type="primary" v-show="addButton" @click="addClick" size="small">新增</el-button>
                <slot name="actions"></slot>
            </div>
        </el-col>
    </div>
</template>

<script type="text/ecmascript-6">
    export default{
        data() {
            return {

            };
        },
        props: {
            addButton: {
                type: Boolean,
                default: false
            }
        },
        methods: {
            searchClick(event) {
                this.$emit("click", event);
            },
            addClick(event) {
                this.$emit("addClick", event);
            }
        }
    };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .xt-search{
        display: inline;
        flex-flow: row wrap;
    }
    .xt-search-items{
        float: left;
    }
    .xt-search-button{
        display: inline-block;
        width:10rem;
        float: left;
        margin-top: 4px;
    }
</style>
