<template>
      <div>
          <div>
              <div class="xt-left" >
                  <div class="xt-left-head">
                      计量类型
                  </div>
                  <div class="xt-left-body">
                      <div v-for="item in measureTypeList" class="xt-left-list bounceInDown animated" @click="measureTypeClick(item)" :class="measureTypeSelected.measureTypeId==item.id?'xt-left-list-active':''">
                          <div class="xt-measure-type">{{item.name}}</div>
                          <div class="xt-measure-type" style="float: right;margin-right: 0.2rem" @click="deleteMeasureType(item)"><i class="el-icon-delete"></i></div>
                          <div class="xt-measure-type" style="float: right;margin-right: 0.2rem" @click="editMeasureType(item)"><i class="el-icon-edit"></i></div>
                      </div>
                      <div class="bounceInDown animated" style="height: 2.5rem;line-height: 2.5rem;text-align: center;border-bottom:1px solid #748FAA">
                          <div> <el-button type="primary" size="small" @click="dialog.dialogAddMeasureTypeFormVisible = true" icon="plus"></el-button></div>
                      </div>
                  </div>
              </div>
              <div class="xt-body">
                  <div class="xt-body-head">
                      计量换算查看
                  </div>
                  <div class="xt-measure-list" v-show="addMeasure.typeId==0?false:true">
                      <el-button type="success" size="small" icon="plus" @click="dialog.dialogAddMeasureFormVisible = true">新增</el-button>
                      <div class="xt-measure-title" >{{addMeasure.typeName}}设置</div>
                      <el-table
                              :data="measureList"
                              style="width: 100%;border-radius: 0rem 0.5rem 0.5rem 0.5rem" height="300">
                          <el-table-column
                                  label="名称">
                              <template scope="scope">
                                  <span v-show="!scope.row.isEdit">{{ scope.row.name }}</span>
                                  <div v-show="scope.row.isEdit">
                                      <el-input v-model="scope.row.name" size="small" auto-complete="off"></el-input>
                                  </div>
                              </template>
                          </el-table-column>
                          <el-table-column
                                  label="值">
                              <template scope="scope">
                                  <span v-show="!scope.row.isEdit">{{ scope.row.value }}</span>
                                  <div v-show="scope.row.isEdit">
                                      <el-input-number v-model="scope.row.value" size="small" auto-complete="off" :controls="false" ></el-input-number>
                                  </div>
                              </template>
                          </el-table-column>
                          <el-table-column
                                  label="代码">
                              <template scope="scope">
                                  <span v-show="!scope.row.isEdit">{{ scope.row.code }}</span>
                                  <div v-show="scope.row.isEdit">
                                      <el-input v-model="scope.row.code" size="small" auto-complete="off"></el-input>
                                  </div>
                              </template>
                          </el-table-column>
                          <el-table-column
                                  label="操作"
                                  width="180">
                              <template scope="scope">
                                  <el-button v-show="scope.row.isEdit" type="success" icon="check" size="small" @click="sureMeasure(scope.row)"></el-button>
                                  <el-button v-show="scope.row.isEdit" type="danger" icon="close" size="small" @click="scope.row.isEdit=false"></el-button>
                                  <el-button v-show="!scope.row.isEdit" type="primary" icon="edit" size="small" @click="editMeasure(scope.row)"></el-button>
                                  <el-button v-show="!scope.row.isEdit" type="danger" icon="delete" size="small" @click="deleteMeasure(scope.row)"></el-button>
                              </template>
                          </el-table-column>
                      </el-table>
                  </div>
                  <div class="xt-measure-change-body" v-show="addMeasure.typeId==0?false:true">
                      <div class="xt-measure-title">
                          {{addMeasure.typeName}}换算器
                      </div>
                      <el-row >
                          <el-col :span="12" v-for="item in changeMeasureList" class="xt-measure-table" >
                              <el-col :span="6" class="xt-measure-change-list">
                                  {{item.name}}({{item.code}})
                              </el-col>
                              <el-col :span="18" class="xt-measure-change-list">
                                  <el-col :xs="16" :sm="17" :md="18" :lg="19" style="margin-left: 0.2rem">
                                      <el-input-number  placeholder="请输入内容"  size="small" style="margin-top: 0.2rem;margin-bottom: 0;width: 100%" :controls="false" v-model="item.value"></el-input-number>
                                  </el-col>
                                  <el-col  :xs="6" :sm="5" :md="4" :lg="3" style="margin-left: 0.2rem">
                                      <el-button type="info" size="small" @click="measureChange(item)">转换</el-button>
                                  </el-col>
                              </el-col>
                          </el-col>
                      </el-row>
                  </div>
              </div>
          </div>

          <!--计量类型新增-->
          <el-dialog title="新增计量类型" :visible.sync="dialog.dialogAddMeasureTypeFormVisible">
              <el-form :model="addMeasureType">
                  <el-form-item label="计量类型名称" label-width="120px">
                      <el-input v-model="addMeasureType.name" auto-complete="off"></el-input>
                  </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="dialog.dialogAddMeasureTypeFormVisible = false">取 消</el-button>
                  <el-button type="primary" @click="submitMeasureType">确 定</el-button>
              </div>
          </el-dialog>

          <!--计量类型新增-->
          <el-dialog title="新增计量单位" :visible.sync="dialog.dialogAddMeasureFormVisible" :close-on-click-modal="false">
              <el-form :model="addMeasure" label-width="100px">
                  <el-form-item label="计量类型">
                      <el-tag type="gray">{{addMeasure.typeName}}</el-tag>
                  </el-form-item>
                  <el-form-item label="计量单位名称">
                      <el-input v-model="addMeasure.name" auto-complete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="计量值" >
                      <el-input-number v-model="addMeasure.value" :controls="false" auto-complete="off" style="width: 100%"></el-input-number>
                  </el-form-item>
                  <el-form-item label="计量代码">
                      <el-input v-model="addMeasure.code" auto-complete="off"></el-input>
                  </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="dialog.dialogAddMeasureFormVisible = false">取 消</el-button>
                  <el-button type="primary" @click="submitMeasure">确 定</el-button>
              </div>
          </el-dialog>
      </div>
</template>

<script type="text/ecmascript-6">
    export default{
        data(){
            return{
                measureList:[{id:1,typeId:1,name:'米',value:1,code:'m',isEdit:false},{id:2,typeId:1,name:'千米',value:0.001,code:'km',isEdit:false}],
                measureTypeList:[{id:1,name:'长度单位'},{id:2,name:'重量单位'}],
                dialog:{dialogAddMeasureTypeFormVisible: false,dialogAddMeasureFormVisible:false},
                measureTypeSelected:{measureTypeId:0},
                addMeasureType:{id:0,name:''},
                addMeasure:{id:0,typeId:0,name:'',value:0,code:'',typeName:'',isEdit:false},
                changeMeasureList:[{id:1,typeId:1,name:'米',value:1,code:'m',isEdit:false},{id:2,typeId:1,name:'千米',value:0.001,code:'km',isEdit:false}]
            }
        },
        methods:{
            /*计量类型点击*/
            measureTypeClick(item){
                let newMeasureTypeSelected=this.measureTypeSelected;
                newMeasureTypeSelected.measureTypeId=item.id;
                let newAddMeasure=this.addMeasure;
                newAddMeasure.typeId=item.id;
                newAddMeasure.typeName=item.name
                if(item.id==2){
                    this.measureList=[{id:3,typeId:2,name:'克',value:1,code:'g',isEdit:false},{id:4,typeId:2,name:'千克',value:0.001,code:'kg',isEdit:false}]
                }else{
                    this.measureList=[{id:1,typeId:1,name:'米',value:1,code:'m',isEdit:false},{id:2,typeId:1,name:'千米',value:0.001,code:'km',isEdit:false}]
                }
                this.changeMeasureList=JSON.parse(JSON.stringify(this.measureList))
            },
            /*计量类型添加提交事件*/
            submitMeasureType(){
                let _this=this;
                let newAddMeasureType=JSON.parse(JSON.stringify(this.addMeasureType));
                if(newAddMeasureType.id==0){
                    newAddMeasureType.id=_this.measureTypeList.length+1;
                    _this.measureTypeList.push(newAddMeasureType)
                }else{
                    _this.measureTypeList.map(element => {
                        if(element.id==newAddMeasureType.id){
                            element.name=newAddMeasureType.name
                        }
                    })
                }
                _this.dialog.dialogAddMeasureTypeFormVisible = false;
                this.addMeasureType.name='';
                this.addMeasureType.id=0;
            },
            /*计量单位设置添加事件*/
            submitMeasure(){
                let _this=this;
                let newAddMeasure=JSON.parse(JSON.stringify(this.addMeasure));
                newAddMeasure.id=_this.measureList.length+1;
                _this.measureList.push(newAddMeasure)
                this.changeMeasureList=JSON.parse(JSON.stringify(this.measureList))
                _this.dialog.dialogAddMeasureFormVisible = false;
                this.addMeasure.name='';
                this.addMeasure.value=0;
                this.addMeasure.code='';
                this.addMeasure.id=0;
            },
            /*编辑计量类型*/
            editMeasureType(item){
                let newAddMeasureType=this.addMeasureType;
                newAddMeasureType.id=item.id;
                newAddMeasureType.name=item.name;
                this.dialog.dialogAddMeasureTypeFormVisible = true;
            },
            /*删除计量类型*/
            deleteMeasureType(item){
                this.measureTypeList.remove(item)
            },
            /*编辑计量单位*/
            editMeasure(item){
                item.isEdit=true;
            },
            sureMeasure(item){
                item.isEdit=false;
                this.changeMeasureList=JSON.parse(JSON.stringify(this.measureList))
            },
            deleteMeasure(item){
                this.measureList.remove(item)
                this.changeMeasureList=JSON.parse(JSON.stringify(this.measureList))
            },
            /*计量单位换算*/
            measureChange(item){
                let _this=this;
                let oldItem=this.measureList.find(element => { return element.id==item.id})
                let measureRate=item.value/oldItem.value;
                if(oldItem.value==0)
                    measureRate=0
                this.changeMeasureList.map(function (element) {
                    element.value=_this.measureList.find(element1 => {return element1.id==element.id}).value*measureRate;
                })
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
        border: 1px solid #A7A7A7;
    }
    .xt-left-head{
        height: 3rem;
        line-height: 3rem;
        font-size: 16px;
        font-weight: bolder;
        text-align: center;
        background-color: #3375CE;
        border: 1px solid #A7A7A7;
        color: #ffffff;
    }
    .xt-left-body{
        height: 670px;
        overflow: auto;
    }
    .xt-left-list{
        height: 2.5rem;
        line-height: 2.5rem;
        text-align: center;
        border-bottom:1px solid #748FAA
        /*background-color: #77BBFF;*/
    }
    .xt-measure-type{
        display: inline-block;
    }
    .xt-left-list-active{
        background-color: #2B95FF;
        color:#ffffff
    }
    .xt-left-list:hover{
        background-color: #2B95FF;
        color:#ffffff
    }
    .xt-body{
        display: inline-block;
        width: 79%;
        height: 700px;
        border: 1px solid #A7A7A7;
        /*background-color: #8B97B1;*/
    }
    .xt-body-head{
        height: 3rem;
        line-height: 3rem;
        font-size: 16px;
        font-weight: bolder;
        text-align: center;
        background-color: #3375CE;
        border: 1px solid #A7A7A7;
        color: #ffffff;
    }
    .xt-measure-list{
        padding: 0.2rem 1rem;
    }
    .xt-measure-title{
        text-align: center;
        height: 2.5rem;
        line-height: 2.5rem;
        font-size: 16px;
        font-weight: bolder;
        color:white;
        background-color: #3375CE;
        border: 1px solid #A7A7A7;
    }
    .xt-measure-table{
        height: 2.5rem;
        line-height: 2.5rem;
        text-align: right;
    }
    .xt-measure-change-body{
        padding: 1rem 1rem;
        height: 245px;
        /*background-color: pink;*/
        overflow: auto;
    }
    .xt-measure-change-list{
        border: 1px solid #B9C0D2;
        height: 2.5rem;
    }
    .xt-inline-block{
        display: inline-block;
    }
</style>