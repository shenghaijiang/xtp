<template>
  <section>
      <div :class="[
            inputSize ? 'el-input--' + inputSize : '',
            {
            'is-disabled': disabled
            },
            'vue-line-height__0'
        ]">
        <tree-select
            v-model="currentValue"
            :multiple="multiple"
            :options="options"
            :placeholder="placeholder"
            :noChildrenText="noChildrenText"
            @input="handleInput"
            class="vue-select-box el-input__inner"
            :disabled="disabled"
            style="padding: 0px 0px 0px 0px;margin-top: 3px;"
        />
      </div>

  </section>
</template>

<script>
import TreeSelect from "@riophae/vue-treeselect";

export default {
    name: "vue-tree-select-validate",
    components: {
        TreeSelect
    },
    props: {
        value: [String, Number],
        validateEvent: {
            type: Boolean,
            default: true
        },
        placeholder: String,
        options: Array,
        multiple: Boolean,
        noChildrenText: String,
        size: String,
        disabled: {
            type: Boolean,
            default: () => false
      }
    },
    data() {
      return {
          currentValue: this.value
      };
    },
    watch: {
        "value"(val, oldValue) {
            this.setCurrentValue(val);
        }
    },
    computed: {
        inputSize() {
        return this.size;
        }
    },
    methods: {
        handleInput(value, id) {
            this.$emit("input", value, id);
            this.setCurrentValue(value);
        },
        setCurrentValue(value) {
            if (value === this.currentValue) {return;}
            this.currentValue = value;
            if (this.validateEvent) {
            // this.dispatch('ElFormItem', 'el.form.change', [value]);
            }
        }
    }
};
</script>

<style scoped>
.vue-select-box{
    border-color: white
}
.vue-treeselect__placeholder,.vue-treeselect__single-value {
    line-height: 28px;
}
.vue-line-height__0{
    line-height: 0px
}
</style>

