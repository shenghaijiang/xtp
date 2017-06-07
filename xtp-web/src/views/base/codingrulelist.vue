<template>
    <div>
        <div class="xt-body">
            <div class="xt-left">
                <div class="xt-left-head">
                    编码类型
                </div>
                <div class="xt-left-list">
                    <div class="xt-left-list-body bounceInDown animated" v-for="item in ruleTypeList" @click="codingTypeClick(item)" :class="addRuleElementForm.ruleTypeId==item.id?'xt-left-list-body-active':''">
                        <div class="xt-rule-type">{{item.name}}({{item.code}})</div>
                        <div class="xt-rule-type" style="float: right;margin-right: 0.2rem" @click="deleteRuleType(item)"><i class="el-icon-delete"></i></div>
                        <div class="xt-rule-type" style="float: right;margin-right: 0.2rem" @click="editRuleType(item)"><i class="el-icon-edit"></i></div>
                    </div>
                    <div class="xt-left-list-add bounceInDown animated">
                        <div><el-button type="primary" size="small" icon="plus" @click="addRuleType"></el-button></div>
                    </div>
                </div>
            </div>
            <div class="xt-right">
                <div class="xt-right-head">编码规则</div>
                <div class="xt-right-body" v-show="addRuleElementForm.ruleTypeId==0?false:true">
                    <el-button type="success" size="small" icon="plus" @click="addRuleBtn">新增</el-button>
                    <el-table
                            :data="ruleElementList"
                            style="width: 100%">
                        <el-table-column
                                prop="eleRule"
                                label="元素类别"
                                minWidth="120">
                        </el-table-column>
                        <el-table-column
                                prop="startValue"
                                label="初始值"
                                minWidth="100">
                        </el-table-column>
                        <el-table-column
                                prop="step"
                                label="长度">
                        </el-table-column>
                        <el-table-column
                                prop="format"
                                label="格式">
                        </el-table-column>
                        <el-table-column
                                prop="sort"
                                label="序号">
                        </el-table-column>
                        <el-table-column
                                prop="enumerate"
                                label="枚举库" minWidth="200">
                        </el-table-column>
                        <el-table-column
                                fixed="right"
                                label="操作"
                                width="220">
                            <template scope="scope">
                                <el-button type="primary" icon="edit" size="small" @click="editRule(scope.row)"></el-button>
                                <el-button type="danger" icon="delete" size="small" @click="deleteRule(scope.row)"></el-button>
                                <el-button :disabled="scope.$index==0?true:false" type="success" size="small" @click="upRule(scope.row)"><i class="fa fa-arrow-up" aria-hidden="true"></i></el-button>
                                <el-button :disabled="scope.$index==(ruleElementList.length-1)?true:false" type="warning" size="small" @click="downRule(scope.row)" ><i class="fa fa-arrow-down" aria-hidden="true"></i></el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </div>


        <!--编码类型新增-->
        <el-dialog title="新增编码类型" :visible.sync="dialog.dialogRuleTypeFormVisible">
            <el-form :model="addRuleTypeForm">
                <el-form-item label="类型名称" label-width="120px">
                    <el-input v-model="addRuleTypeForm.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="类型代码" label-width="120px">
                    <el-input v-model="addRuleTypeForm.code" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog.dialogRuleTypeFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitRuleType">确 定</el-button>
            </div>
        </el-dialog>

        <!--编码规则添加-->
        <el-dialog title="编码规则添加" :visible.sync="dialog.addRuleDialogVisible">
            <el-form :model="addRuleElementForm" label-width="80px">
                <el-form-item label="编码类型" >
                    <el-tag type="gray">{{addRuleElementForm.ruleTypeName}}</el-tag>
                </el-form-item>
                <el-form-item label="元素类别" >
                    <el-select v-model="addRuleElementForm.eleRule" placeholder="请选择" style="width: 100%">
                        <el-option  label="固定字串" value="固定字串"></el-option>
                        <el-option  label="自增（减）数字字串" value="自增（减）数字字串"></el-option>
                        <el-option  label="枚举字串" value="枚举字串"></el-option>
                        <el-option  label="时间字串" value="时间字串"></el-option>
                        <el-option  label="当前用户字串" value="当前用户字串"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="初始值" >
                    <el-input v-model="addRuleElementForm.startValue" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="长度" >
                    <el-input-number v-model="addRuleElementForm.step" :min="0" :max="9999" :controls="false" style="width: 100%"></el-input-number>
                </el-form-item>
                <el-form-item label="格式" >
                    <el-input v-model="addRuleElementForm.format" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="序号" >
                    <el-input-number v-model="addRuleElementForm.sort" :min="1" :max="9999" :controls="false" style="width: 100%"></el-input-number>
                </el-form-item>
                <el-form-item label="枚举库" >
                    <el-input v-model="addRuleElementForm.enumerate" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog.addRuleDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addRuleSubmit">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script type="text/ecmascript-6">
    import {Compares} from '../../common/js/util'

    export default{
        data(){
            return{
                ruleTypeList:[{id:1,name:'物料编码',code:'WL'},{id:2,name:'工序编码',code:'GX'}],
                ruleElementList:[{id:1,typeId:1,eleRule:'固定字段',startValue:'[物料管理]',step:0,format:'',sort:1,enumerate:''},{id:2,typeId:1,eleRule:'自增（减）数字字串',startValue:'1234567',step:2,format:'##，#_##',sort:2,enumerate:''},{id:3,typeId:1,eleRule:'时间字串',startValue:'2017-05-24',step:0,format:'MMdd',sort:3,enumerate:''},{id:4,typeId:1,eleRule:'当前用户字串',startValue:'realName',step:0,format:'',sort:4,enumerate:''},{id:5,typeId:1,eleRule:'枚举字串',startValue:'b',step:0,format:'',sort:5,enumerate:'a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s'}],
                addRuleElementForm:{id:0,ruleTypeId:0,eleRule:'',startValue:'',step:0,format:'',sort:1,enumerate:'',ruleTypeName:'',ruleTypeCode:''},
                addRuleTypeForm:{id:0,name:'',code:''},
                dialog:{addRuleDialogVisible:false,dialogRuleTypeFormVisible:false} //弹框页面显示
            }
        },
        methods:{
            /*编码类型点击事件*/
            codingTypeClick(item){
                let newAddRuleElementForm=this.addRuleElementForm;
                newAddRuleElementForm.ruleTypeId=item.id;
                newAddRuleElementForm.ruleTypeName=item.name;
                newAddRuleElementForm.ruleTypeCode=item.code;
            },
            /*新增规则确定事件*/
            addRuleSubmit(){
                let newAddRuleElementForm=JSON.parse(JSON.stringify(this.addRuleElementForm));
                newAddRuleElementForm.id=this.ruleElementList.length+1;
                this.ruleElementList.push(newAddRuleElementForm);
                this.dialog.addRuleDialogVisible = false;
                let oldAddRuleElementForm=this.addRuleElementForm;
                oldAddRuleElementForm.eleRule='';oldAddRuleElementForm.sort=this.ruleElementList.length+1;oldAddRuleElementForm.enumerate='';
                oldAddRuleElementForm.startValue='';oldAddRuleElementForm.step=0;oldAddRuleElementForm.format='';
            },
            /*新增编码类型确定事件*/
            submitRuleType(){
                let newAddRuleForm=JSON.parse(JSON.stringify(this.addRuleTypeForm));
                if(newAddRuleForm.id==0){
                    newAddRuleForm.id=this.ruleTypeList.length+1;
                    this.ruleTypeList.push(newAddRuleForm);
                }
                else this.ruleTypeList.map(function (element) {
                    if(element.id==newAddRuleForm.id){
                        element.name= newAddRuleForm.name
                        element.code= newAddRuleForm.code
                    }
                })
                this.dialog.dialogRuleTypeFormVisible = false;
                let oldAddRuleForm=this.addRuleTypeForm;
                oldAddRuleForm.name='';oldAddRuleForm.code='';
            },
            /*删除编码类型事件*/
            deleteRuleType(item){
                this.ruleTypeList.remove(item);
            },
            /*编辑编码类型事件*/
            editRuleType(item){
                let oldAddRuleForm=this.addRuleTypeForm;
                oldAddRuleForm.id=item.id;
                oldAddRuleForm.name=item.name;oldAddRuleForm.code=item.code;
                this.dialog.dialogRuleTypeFormVisible = true;
            },
            /*打开添加编码类型页面*/
            addRuleType(){
                let oldAddRuleForm=this.addRuleTypeForm;
                oldAddRuleForm.id=0;
                oldAddRuleForm.name='';oldAddRuleForm.code='';
                this.dialog.dialogRuleTypeFormVisible = true;
            },
            /*编码规则按钮点击事件*/
            addRuleBtn(){
                let oldAddRuleElementForm=this.addRuleElementForm;
                oldAddRuleElementForm.id=0;oldAddRuleElementForm.eleRule='';
                oldAddRuleElementForm.sort=this.ruleElementList.length+1;oldAddRuleElementForm.enumerate='';
                oldAddRuleElementForm.startValue='';oldAddRuleElementForm.step=0;oldAddRuleElementForm.format='';
                this.dialog.addRuleDialogVisible = true;
            },
            /*编辑编码规则*/
            editRule(item){
                let oldAddRuleElementForm=this.addRuleElementForm;
                oldAddRuleElementForm.eleRule=item.eleRule;oldAddRuleElementForm.sort=item.sort;oldAddRuleElementForm.enumerate=item.enumerate;
                oldAddRuleElementForm.startValue=item.startValue;oldAddRuleElementForm.step=item.step;oldAddRuleElementForm.format=item.format;
                this.dialog.addRuleDialogVisible = true;
            },
            /*删除编码规则*/
            deleteRule(item){
                this.ruleElementList.remove(item);
            },
            /*上移编码规则（排序）*/
            upRule(item){
                if(item.sort>0)
                item.sort=item.sort-1
                this.ruleElementList.sort(Compares.compareAscending('sort'))
            },
            /*下移编码规则（排序）*/
            downRule(item){
                if(item.sort<this.ruleElementList.length)
                    item.sort=item.sort+1
                this.ruleElementList.sort(Compares.compareAscending('sort'))
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .xt-left{
        display: inline-block;
        float: left;
        width: 20%;
        height: 700px;
        background-color: #EAECF0;
        border: 1px solid darkgray;
    }
    .xt-left-head{
        height: 3rem;
        font-weight: bolder;
        font-size: 16px;
        line-height: 3rem;
        color:#ffffff;
        text-align: center;
        background-color: #3375CE;
    }
    .xt-left-list{
        height: 697px;
        overflow: auto;
    }
    .xt-rule-type{
        display: inline-block;
    }
    .xt-left-list-body{
        height: 3rem;
        line-height: 3rem;
        /*margin-bottom: 0.2rem;*/
        text-align: center;
        /*background-color: #f292b2;*/
        border-bottom: 1px solid #A9A9A9;
    }
    .xt-left-list-body:hover{
        background-color: #2B95FF;
        color:#ffffff
    }
    .xt-left-list-add{
        height: 3rem;
        line-height: 3rem;
        text-align: center;
        border-bottom: 1px solid #A9A9A9;
    }
    .xt-left-list-body-active{
        background-color: #2B95FF;
        color:#ffffff
    }
    .xt-right{
        display: inline-block;
        width:78%;
        height: 700px;
        /*background-color: orange;*/
        border: 1px solid darkgray;
    }
    .xt-right-head{
        height: 3rem;
        font-weight: bolder;
        font-size: 16px;
        line-height: 3rem;
        color:#ffffff;
        text-align: center;
        background-color: #3375CE;
    }
    .xt-right-body{
        padding: 0.2rem 0.3rem;
    }
</style>