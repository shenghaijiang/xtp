<template>
    <div :class="['el-form-item','is-required',isError?'is-error':'']" style="margin-bottom: 0">
        <div class="el-form-item__content">
            <el-tooltip :disabled="!isError" placement="right" effect="light">
                <div slot="content" style="color: #ff4949">{{message}}</div>
                <div :style="contentStyle">
                    <slot></slot>
                </div>
            </el-tooltip>
        </div>
    </div>
</template>
<script>
    import AsyncValidator from "async-validator";
    export default{
        name: "xt-row-edit-item",
        data() {
            return {
                isError: false,
                message: ""
            };
        },
        methods: {
            validate() {
                const _props = this.prop;
                const descriptor = {};
                descriptor[this.prop] = this.rules[this.prop];
                const validator = new AsyncValidator(descriptor);
                const model = {};
                model[this.prop] = this.itemVal;
                validator.validate(model, { firstFields: true }, (errors, fields) => {
                    this.isError = !!errors;
                    this.message = errors ? errors[0].message : "";
                });
            }
        },
        props: {
            rules: Object,
            prop: String,
            itemVal: null,
            vueHub: Object,
            uid: Number,
            contentStyle: Object
        },
        watch: {
            "itemVal": function (newVal, oldVal) {
                this.validate();
            }
        }, mounted() {
            this.vueHub.$on("xt-row-edit-validate", (arr) => {
                console.log(this.prop, arr);
                this.validate();
                if (this.isError) {
                    arr.push(false);
                }
            });
            this.vueHub.$on("xt-row-edit-validate-" + this.uid, (arr) => {
                console.log(this.prop, arr);
                this.validate();
                if (this.isError) {
                    arr.push(false);
                }
            });
        },
        created() {
        },
        beforeDestroy() {
        },
        destroyed() {
        }
    };
</script>
