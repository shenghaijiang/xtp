<template>
    <div>
        <el-button type="primary" size="small" icon="plus" @click="addNewFormClick()">新增集团</el-button>
        <div class="xt-body">
            <div class="xt-left">
                <div class="xt-left-head">
                  组织结构
                </div>
                <div class="xt-left-list">
                    <el-tree :data="data" :props="defaultProps" :expand-on-click-node="false" style="height: 650px;overflow: auto;" @node-click="handleNodeClick"></el-tree>
                </div>
            </div>
            <div class="xt-right">
                <div class="xt-right-head">
                   结构详情
                </div>
                <!--已选择的数据展示-->
                <div v-show="(formData.id==0&&formData.parentId==0)?false:true">
                    <div class="xt-right-body" >
                        <div class="xt-form-head">
                            修改{{formData.name}}
                        </div>
                        <div class="xt-form">
                            <el-form ref="formData" :model="formData" label-width="80px">
                                <el-row>
                                    <el-col :span="12">
                                        <el-form-item label="编码">
                                            <el-input size="small" v-model="formData.code" :disabled="!formData.isEdit"></el-input>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-form-item label="名称">
                                            <el-input size="small" v-model="formData.name" :disabled="!formData.isEdit"></el-input>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <el-col :span="12">
                                        <el-form-item label="上级组织代码">
                                            <el-input size="small" v-model="formData.parentCode" :disabled="!formData.isEdit"></el-input>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-form-item label="组织级别">
                                            <el-select v-model="formData.structureRange" size="small" placeholder="请选择" :disabled="!formData.isEdit">
                                                <el-option label="集团" value="1"></el-option>
                                                <el-option label="公司" value="2"></el-option>
                                                <el-option label="工厂" value="3"></el-option>
                                                <el-option label="部门" value="4"></el-option>
                                                <el-option label="岗位" value="5"></el-option>
                                            </el-select>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <el-col :span="12">
                                        <el-form-item label="账套" >
                                            <el-input size="small" v-model="formData.books" :disabled="!formData.isEdit"></el-input>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-form-item label="地址" >
                                            <el-input size="small" v-model="formData.address" :disabled="!formData.isEdit"></el-input>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <el-col :span="4" :offset="10">
                                        <el-button type="primary" @click="submitForm" v-show="formData.isEdit">保存</el-button>
                                        <el-button type="info" v-show="!formData.isEdit" @click="formData.isEdit=true">修改</el-button>
                                    </el-col>
                                    <el-col :span="8">
                                        <el-button type="default" v-show="formData.isEdit" @click="formData.isEdit=false">取消</el-button>
                                        <el-button type="danger" v-show="!formData.isEdit" @click="deleteForm(formData)">删除</el-button>
                                    </el-col>
                                </el-row>
                            </el-form>
                        </div>
                    </div>
                    <div class="xt-right-list" v-if="formData.structureRange!=5">
                        <el-button type="primary" size="small" icon="plus" @click="addFormChildrenClick(formData)">新增</el-button>
                        <div class="xt-list-head">
                            结构子列表
                        </div>
                        <div class="xt-list-body">
                            <el-table
                                    :data="formData.children"
                                    style="width: 100%">
                                <el-table-column
                                        prop="name"
                                        label="岗位名称" minWidth="120">
                                </el-table-column>
                                <el-table-column
                                        prop="code"
                                        label="岗位代码" minWidth="120">
                                </el-table-column>
                                <el-table-column
                                        prop="address"
                                        label="地址">
                                </el-table-column>
                                <el-table-column
                                        prop="address"
                                        label="账套">
                                </el-table-column>
                                <el-table-column label="操作" width="150" fixed="right">
                                    <template scope="scope">
                                        <el-button size="small" type="warning" @click="editFormChildren(scope.row)">编辑</el-button>
                                        <el-button type="danger" size="small" @click="deleteFormChildren(scope.row)">删除</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--新增父级信息-->
        <el-dialog title="新增集团" :visible.sync="dialog.dialogFormVisible">
            <el-form ref="formData" :model="formData" label-width="80px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="编码">
                            <el-input size="small" v-model="formData.code"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="名称">
                            <el-input size="small" v-model="formData.name"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="上级组织代码">
                            <el-input size="small" v-model="formData.parentCode"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="组织级别">
                            <el-select v-model="formData.structureRange" size="small" placeholder="请选择">
                                <el-option label="集团" value="1"></el-option>
                                <el-option label="公司" value="2"></el-option>
                                <el-option label="工厂" value="3"></el-option>
                                <el-option label="部门" value="4"></el-option>
                                <el-option label="岗位" value="5"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="账套" >
                            <el-input size="small" v-model="formData.books"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="地址" >
                            <el-input size="small" v-model="formData.address"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog.dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>

        <!--新增子列表信息-->
        <el-dialog :title="pageShow.addTitle" :visible.sync="dialog.dialogFormChildrenVisible" >
            <el-form ref="formChildrenData" :model="formChildrenData" label-width="80px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="编码">
                            <el-input size="small" v-model="formChildrenData.code"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="名称">
                            <el-input size="small" v-model="formChildrenData.name"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="上级组织代码">
                            <el-input size="small" v-model="formChildrenData.parentCode"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="组织级别">
                            <el-select v-model="formChildrenData.structureRange" size="small" placeholder="请选择">
                                <el-option label="集团" value="1"></el-option>
                                <el-option label="公司" value="2"></el-option>
                                <el-option label="工厂" value="3"></el-option>
                                <el-option label="部门" value="4"></el-option>
                                <el-option label="岗位" value="5"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="账套" >
                            <el-input size="small" v-model="formChildrenData.books"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="地址" >
                            <el-input size="small" v-model="formChildrenData.address"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog.dialogFormChildrenVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitFormChildren">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script type="text/ecmascript-6">

    export default {
        components:{},
        data() {
            return {
                data: [{
                    id:1,
                    name: '集团',
                    code:'JT',
                    parentId:0,
                    parentCode:'',
                    structureRange:'1',
                    address:'杭州',
                    books:'集团账套',
                    children: [{
                        id:2,
                        name: '公司1',
                        code:'GS1',
                        parentId:1,
                        parentCode:'JT',
                        structureRange:'2',
                        address:'宁波',
                        books:'公司1账套',
                        children: [{
                            id:3,
                            name: '工厂1',
                            code:'GC1',
                            parentId:2,
                            parentCode:'GS1',
                            structureRange:'3',
                            address:'宁波',
                            books:'工厂1账套',
                            children: [{
                                name: '部门1',
                                id:4,
                                code:'BM1',
                                parentId:3,
                                parentCode:'GC1',
                                structureRange:'4',
                                address:'宁波',
                                books:'部门1账套',
                                children: [{
                                    name: '岗位1',
                                    id:5,
                                    code:'GW1',
                                    parentId:4,
                                    parentCode:'BM1',
                                    structureRange:'5',
                                    address:'宁波',
                                    books:'部门1账套'
                                }]
                            }]
                        }]
                    }, {
                        id: 10,
                        name: '公司2',
                        code: 'GS2',
                        parentId: 1,
                        parentCode: 'JT',
                        structureRange: '2',
                        address: '泰国',
                        books: '公司2账套',
                        children: []
                    }]
                }],//菜单获取的数据
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },//属性控件标题以及子列表数据绑定
                formData:{id:0,name:'',code:'',parentId:0,parentCode:'',structureRange:'1',address:'',books:'',children:[],isEdit:false},//选择的组织结构数据
                formChildrenData:{id:0,name:'',code:'',parentId:0,parentCode:'',structureRange:'1',address:'',books:'',children:[],isEdit:false},//选择的结构数据的子列表
                pageShow:{title:'',addTitle:'新增',addButtonShow:true}, //子列表修改的标题设置
                dialog:{dialogFormVisible: false,dialogFormChildrenVisible:false}, //模态框控制
            };
        },
        methods: {
            ///树形控件点击事件
            handleNodeClick(data) {
                let newFormData=this.formData;
                newFormData.id=data.id;newFormData.name=data.name;
                newFormData.code=data.code;newFormData.parentId=data.parentId;
                newFormData.parentCode=data.parentCode;newFormData.structureRange=data.structureRange;
                newFormData.address=data.address;newFormData.books=data.books;
                newFormData.children=data.children;
            },
            /*新增集团事件*/
            addNewFormClick(){
                let newForm=this.formData;
                newForm.parentId=0;
                newForm.parentCode='';
                newForm.id=0;
                newForm.name='';
                newForm.code='';
                newForm.structureRange='1';
                newForm.address='';
                newForm.books='';
                newForm.children=[]
                this.dialog.dialogFormVisible = true
            },

            /*新增子列表事件*/
            addFormChildrenClick(item){
                let newForm=this.formChildrenData;
                let pageShow=this.pageShow;
                    newForm.parentId=item.id;
                    newForm.parentCode=item.code;
                    newForm.id=0;
                    newForm.name='';
                    newForm.code='';
                    newForm.structureRange=parseInt(item.structureRange)+1+'';
                    newForm.address='';
                    newForm.books='';
                switch (newForm.structureRange){
                    case '1':pageShow.addTitle="新增集团";break;
                    case '2':pageShow.addTitle="新增公司";break;
                    case '3':pageShow.addTitle="新增工厂";break;
                    case '4':pageShow.addTitle="新增部门";break;
                    case '5':pageShow.addTitle="新增岗位";break;
                    default:break;
                }
                this.dialog.dialogFormChildrenVisible = true
            },

            /*编辑子列表事件*/
            editFormChildren(item){
                let newForm=this.formChildrenData;
                let pageShow=this.pageShow;
                newForm.parentId=item.id;
                newForm.parentCode=item.code;
                newForm.id=item.id;
                newForm.name=item.name;
                newForm.code=item.code;
                newForm.structureRange=item.structureRange;
                newForm.address=item.address;
                newForm.books=item.books;
                switch (newForm.structureRange){
                    case '1':pageShow.addTitle="修改集团";break;
                    case '2':pageShow.addTitle="修改公司";break;
                    case '3':pageShow.addTitle="修改工厂";break;
                    case '4':pageShow.addTitle="修改部门";break;
                    case '5':pageShow.addTitle="修改岗位";break;
                    default:break;
                }
                this.dialog.dialogFormChildrenVisible = true
            },

            /*删除子列表数据事件*/
            deleteFormChildren(item){
                alert('删除'+item.name)
            },

            /*删除选择的组织结构内容事件*/
            deleteForm(item){
                alert('删除'+item.name)
                this.formData={id:0,name:'',code:'',parentId:0,parentCode:'',structureRange:'1',address:'',books:'',children:[],isEdit:false};

            },

            /*新增/修改已选择的组织事件*/
            submitForm() {
                let newForm=JSON.parse(JSON.stringify(this.formData));
                if(newForm.id==0&&newForm.parentId==0){
                    newForm.id=this.data.length+1;
                    this.data.push(newForm)
                }else{
                    this.updateDatas(newForm);
                }
                this.dialog.dialogFormVisible = false
                console.log(this.data)
            },

            /*新增/修改子列表中的数据*/
            submitFormChildren(){
                let newForm=JSON.parse(JSON.stringify(this.formChildrenData));
                this.updateDatas(newForm);
                this.dialog.dialogFormChildrenVisible = false
            },

            /*模拟提交数据（修改数据）*/
            updateDatas(newForm){
                let newData=this.data;
                let _this=this;
                if(newForm.parentId==0){
                    newForm.children=[]
                    this.data.push(newForm)
                }else{
                    newData.map(function (element) {
                        if(newForm.id>0){
                            if(element.id==newForm.id){
                                _this.updateData(element,newForm)
                            }else{
                                element.children.map(function (children1) {
                                    if(children1.id==newForm.id){
                                        _this.updateData(children1,newForm)
                                    }else{
                                        children1.children.map(function (children2) {
                                            if(children2.id==newForm.id){
                                                _this.updateData(children2,newForm)
                                            }else{
                                                children2.children.map(function (children3) {
                                                    if (children3.id == newForm.id) {
                                                        _this.updateData(children3,newForm)
                                                    } else {
                                                        children3.children.map(function (children4) {
                                                            if (children4.id == newForm.id) {
                                                                _this.updateData(children4,newForm)
                                                            } else {
                                                            }
                                                        })
                                                    }
                                                })
                                            }
                                        })
                                    }
                                })
                            }
                        }else{
                            newForm.children=[]
                            if(element.id==newForm.parentId){
                                element.children.push(JSON.parse(JSON.stringify(newForm)))
                            }else{
                                element.children.map(function (children1) {
                                    if(children1.id==newForm.parentId){
                                        children1.children.push(JSON.parse(JSON.stringify(newForm)))
                                    }else{
                                        children1.children.map(function (children2) {
                                            if(children2.id==newForm.parentId){
                                                children2.children.push(JSON.parse(JSON.stringify(newForm)))
                                            }else{
                                                children2.children.map(function (children3) {
                                                    if(children3.id==newForm.parentId){
                                                        children3.children.push(JSON.parse(JSON.stringify(newForm)))
                                                    }else{
                                                        children3.children.map(function (children4) {
                                                            if(children4.id==newForm.parentId){
                                                                children4.children.push(JSON.parse(JSON.stringify(newForm)))
                                                            }else{
                                                            }
                                                        })
                                                    }
                                                })
                                            }
                                        })
                                    }
                                })
                            }
                        }
                    })
                }
                this.data=newData
            },

           /*模拟修改具体数据*/
            updateData(form,item){
                form.id=item.id;
                form.name=item.name;
                form.code=item.code;
                form.parentId=item.parentId;
                form.parentCode=item.parentCode;
                form.structureRange=item.structureRange;
                form.address=item.address;
                form.books=item.books;
            }
        }
    };
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
    .xt-right{
        display: inline-block;
        width:78%;
        height: 700px;
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
        height: 285px;
        /*background-color: pink;*/
        padding: 1rem 0.2rem;
    }
    .xt-form-head{
        height: 2rem;
        line-height: 2rem;
        text-align: center;
        font-size: 14px;
        font-weight: bolder;
        border: 1px solid #A9A9A9;
        background-color: #2C66B6;
        color: #ffffff;
    }
    .xt-form{
        padding: 0.3rem 0.4rem;
        border: 1px solid #A9A9A9;
    }
    .xt-right-list{
        padding: 1rem 0.2rem;
        height: 300px;
        /*background-color: yellow;*/
    }
    .xt-list-head{
        height: 2rem;
        line-height: 2rem;
        text-align: center;
        font-size: 14px;
        font-weight: bolder;
        border: 1px solid #A9A9A9;
        background-color: #2C66B6;
        color: #ffffff;
    }
</style>