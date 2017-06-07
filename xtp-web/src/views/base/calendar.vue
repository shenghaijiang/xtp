<template>
    <div>
        <div class="xt-body">
            <div class="xt-left">
                <div class="xt-left-head">
                    岗位
                </div>
                <div class="xt-left-list">
                    <div class="xt-left-list-body bounceInDown animated" v-for="item in postList" @click="postClick(item)" :class="eventForm.postId==item.id?'xt-left-list-body-active':''">
                        <div class="xt-rule-type">{{item.name}}</div>
                    </div>
                </div>
            </div>
            <div class="xt-right">
                <div class="xt-right-head">
                    日历
                </div>
                <div v-show="eventForm.postId==0?false:true">
                    <div class="xt-calendar">
                        <full-calendar :events="eventList" lang="zh" :restDay="systemForm.restDay" :holiday="systemForm.holiday"></full-calendar>
                    </div>
                    <div class="xt-calendar-event" v-show="!dialog.eventShow">
                        <div class="xt-calendar-event-button xt-green" @click="addEvent">新建事件</div>
                        <div class="xt-calendar-event-button xt-orange" @click="dialog.eventShow=true">事件列表</div>
                        <div class="xt-calendar-event-button xt-gray" @click="holidayEvent">假期设置</div>
                    </div>
                    <!--事件列表-->
                    <div class="xt-calendar-event-list" v-show="dialog.eventShow">
                        <div class="xt-calendar-event-head" ><i class="fa fa-angle-left fa-2x xt-event-item-back" aria-hidden="true" @click="dialog.eventShow=false"></i>事件列表<i class="fa fa-plus fa-1x" aria-hidden="true" @click="addEvent" style="float: right;margin-right: 0.3rem;line-height: inherit"></i></div>
                        <div class="xt-calendar-event-body  slideInRight animated">
                            <div v-for="item in eventList" class="xt-event-item" title="双击进行修改" @dblclick="editEvent(item)">
                                <div>
                                    <div class="xt-event-item-close"><i class="fa fa-close" aria-hidden="true" style="float: right" @click="deleteEvent(item)"></i></div>
                                    <div class="xt-event-item-title">{{item.title}}</div>
                                    <div class="xt-event-item-date">{{item.start}}至{{item.end}}</div>
                                </div>
                            </div>
                        </div>
                        <div class="xt-calendar-event-footer">
                            <!--<el-button type="info" style="float: right" size="small" @click="dialog.eventShow=false">关闭</el-button>-->
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--时间新增dialog-->
        <el-dialog title="待办事件" :visible.sync="dialog.dialogEventVisible">
            <el-form :model="eventForm" label-width="80px">
                <el-form-item label="内容" >
                    <el-input v-model="eventForm.title" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="时间选择" >
                    <el-date-picker
                            v-model="eventForm.dateCall"
                            type="daterange"
                            placeholder="选择日期范围" style="width: 100%">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog.dialogEventVisible = false">取 消</el-button>
                <el-button type="primary" @click="eventSubmit">确 定</el-button>
            </div>
        </el-dialog>

        <!--休息日期设置界面-->
        <el-dialog title="休息时间设置" :visible.sync="dialog.dialogSystemVisible">
            <el-form :model="holidayAddForm" label-width="100px">
                <el-form-item label="每周" >
                    <el-checkbox-group v-model="holidayAddForm.restDayLabel" @change="checkBoxHandleChange">
                        <el-checkbox label="星期一" ></el-checkbox>
                        <el-checkbox label="星期二"></el-checkbox>
                        <el-checkbox label="星期三" ></el-checkbox>
                        <el-checkbox label="星期四"></el-checkbox>
                        <el-checkbox label="星期五" ></el-checkbox>
                        <el-checkbox label="星期六" ></el-checkbox>
                        <el-checkbox label="星期日" ></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="节假日设置">
                    <el-card class="box-card">
                        <el-tag
                                :key="tag.value"
                                v-for="tag in holidayAddForm.holiday"
                                :closable="true"
                                :close-transition="false"
                                @close="handleClose(tag)"
                                style="margin-left:0.3rem"
                        >
                            {{tag.name}}({{tag.value}})
                        </el-tag>
                        <div  v-if="holidayAddForm.inputVisible" style="border: 1px solid #c5c5c5">
                            <el-form :inline="true" :model="holidayAddForm" class="demo-form-inline" label-position="right">
                                <el-form-item label="节日名称">
                                    <el-input v-model="holidayAddForm.name" placeholder="节日名称" size="small" ></el-input>
                                </el-form-item>
                                <el-form-item label="日期">
                                    <el-date-picker
                                            type="date"
                                            placeholder="选择日期"
                                            format="MMdd"
                                            v-model="holidayAddForm.date" size="small"
                                    >
                                    </el-date-picker>
                                </el-form-item>
                                <el-row>
                                    <el-col :span="12">

                                    </el-col>
                                    <el-col :span="12">
                                    </el-col>
                                </el-row>
                            </el-form>
                            <div style="height:1.7rem;">
                                <el-button class="" size="small" type="danger" style="margin-left:0.1rem;float: right" icon="close" @click="holidayAddForm.inputVisible = false"></el-button>
                                <el-button class="" size="small" type="success" style="float: right" icon="check" @click="addHolidayClick"></el-button>
                            </div>
                        </div>
                        <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 新增</el-button>
                    </el-card>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog.dialogSystemVisible = false">取 消</el-button>
                <el-button type="primary" @click="systemFormSure">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script type="text/ecmascript-6">
    import {sFtv} from '../../components/calendar/dataMap/holiday'

    export default{
        data () {
            return {
                eventList :[{id:1,postId:1,title : 'gotu',start : '2017-05-24',end : '2017-05-25',dateCall:[]}],
                postList:[{id:1,name:'岗位1',systemForm:{restDay:[0],holiday:sFtv},eventList :[{id:1,postId:1,title : 'gotu',start : '2017-05-24',end : '2017-05-25',dateCall:[]}]},{id:2,name:'岗位2',systemForm:{restDay:[6],holiday:sFtv},eventList :[{id:1,postId:1,title : '岗位2事件',start : '2017-05-24',end : '2017-05-26',dateCall:[]}]}],
                systemForm:{restDay:[],holiday:sFtv},
                eventForm:{id:0,postId:0,title:'',start:'',end:'',dateCall:[]},
                dialog:{eventShow:false,dialogEventVisible:false,dialogSystemVisible:false},
                holidayAddForm:{value:'',name:'',inputVisible:false,date:'',restDay:[],holiday:sFtv,restDayLabel:[]}
            }
        },
        watch:{
        },
        methods:{
            addHolidayClick(){
                let newHolidayForm=this.holidayAddForm;
                newHolidayForm.value=new Date(newHolidayForm.date).Format('MMdd');
                let newHolidayItem={value:newHolidayForm.value,name:newHolidayForm.name};
                this.holidayAddForm.holiday.push(JSON.parse(JSON.stringify(newHolidayItem)));
                this.holidayAddForm.inputVisible = false
            },
            handleClose(tag) {
                this.holidayAddForm.holiday.remove(tag)
            },
            holidayEvent(){
                let newRestLabel=[];
                this.systemForm.restDay.map(function (element) {
                    switch (element){
                        case 1:newRestLabel.push('星期一');break;
                        case 2:newRestLabel.push('星期二');break;
                        case 3:newRestLabel.push('星期三');break;
                        case 4:newRestLabel.push('星期四');break;
                        case 5:newRestLabel.push('星期五');break;
                        case 6:newRestLabel.push('星期六');break;
                        case 0:newRestLabel.push('星期日');break;
                        default:break;
                    }
                });
                this.holidayAddForm={value:'',name:'',inputVisible:false,date:'',restDay:JSON.parse(JSON.stringify(this.systemForm.restDay)),holiday:JSON.parse(JSON.stringify(this.systemForm.holiday)),restDayLabel:newRestLabel};
                this.dialog.dialogSystemVisible = true
            },
            showInput() {
                this.holidayAddForm.inputVisible = true;
            },
            systemFormSure(){
                let newSystemForm=this.systemForm;
                newSystemForm.restDay=JSON.parse(JSON.stringify(this.holidayAddForm.restDay));
                newSystemForm.holiday=JSON.parse(JSON.stringify(this.holidayAddForm.holiday));
                this.dialog.dialogSystemVisible = false
            },
            checkBoxHandleChange(item){
                let newRestDay=[];
                item.map(function (element) {
                    switch (element){
                        case '星期一':newRestDay.push(1);break;
                        case '星期二':newRestDay.push(2);break;
                        case '星期三':newRestDay.push(3);break;
                        case '星期四':newRestDay.push(4);break;
                        case '星期五':newRestDay.push(5);break;
                        case '星期六':newRestDay.push(6);break;
                        case '星期日':newRestDay.push(0);break;
                        default:break;
                    }
                });
                this.holidayAddForm.restDay=newRestDay;
            },
            postClick(item){
                this.eventForm.postId=item.id;
                this.systemForm=JSON.parse(JSON.stringify(item.systemForm));
                this.eventList=JSON.parse(JSON.stringify(item.eventList));
                if(item.id==1){
                }
            },
            eventSubmit(){
                let newEventForm=JSON.parse(JSON.stringify(this.eventForm));
                newEventForm.start=new Date(newEventForm.dateCall[0]).Format('yyyy-MM-dd');
                newEventForm.end=new Date(newEventForm.dateCall[1]).Format('yyyy-MM-dd');
                if(newEventForm.id==0){
                    newEventForm.id=this.eventList.length+1;
                    this.eventList.push(newEventForm);
                }else{
                    this.eventList.map(function (element) {
                        if(element.id==newEventForm.id){
                            element.title=newEventForm.title;
                            element.start=newEventForm.start;
                            element.end=newEventForm.end;
                        }
                    })
                }

                this.dialog.dialogEventVisible = false;
                console.log(this.eventForm)
            },
            addEvent(){
                let newEventForm=this.eventForm;
                newEventForm.id=0;
                newEventForm.title='';
                newEventForm.start='';newEventForm.end='';
                newEventForm.dateCall='';
                this.dialog.dialogEventVisible = true;
            },
            editEvent(item){
                console.log(item)
                let newEventForm=this.eventForm;
                newEventForm.id=item.id;newEventForm.end=item.end;
                newEventForm.title=item.title;newEventForm.postId=item.postId;
                newEventForm.start=item.start;
                newEventForm.dateCall=[];
                newEventForm.dateCall.push(new Date(item.start));
                newEventForm.dateCall.push(new Date(item.end));
                this.dialog.dialogEventVisible = true;
            },
            deleteEvent(item){
                this.eventList.remove(item)
            }
        },
        events: {
        },
        components : {
            'full-calendar': require('../../components/calendar/fullCalendar.vue'),
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
        text-align: center;
        border-bottom: 1px solid #A9A9A9;
        cursor:pointer
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
    .xt-calendar{
        display: inline-block;
        float: left;
        width: 70%;
        height: 600px;
        overflow: auto;
        border: 1px solid lightgray;
    }
    .xt-calendar-event{
        display: inline-block;
        width: 5rem;
        height: 600px;
        /*background-color: orange;*/
    }
    .xt-calendar-event-button{
        -moz-user-select:none;/*火狐*/
        -webkit-user-select:none;/*webkit浏览器*/
        -ms-user-select:none;/*IE10*/
        -khtml-user-select:none;/*早期浏览器*/
        user-select:none;
        height: 2.5rem;
        text-align: center;
        line-height: 2.5rem;
        border:1px solid lightgray;
    }
    .xt-green{
        background-color: #00a900;
    }
    .xt-green:hover{
             background-color: green;
    }
    .xt-orange{
        background-color: orange;
    }
    .xt-orange:hover{
        background-color: #d58e00;
    }
    .xt-gray{
        background-color: lightgray;
    }
    .xt-gray:hover{
        background-color: #b3b3b3;
    }
    .xt-calendar-event-list{
        display: inline-block;
        width: 29%;
        height: 600px;
        border: 1px solid lightgray;
    }
    .xt-calendar-event-head{
        line-height: 40px;
        text-align: center;
        font-size: 14px;
        font-weight: bolder;
        height: 40px;
        background-color: #275CA5;
        color:#ffffff
    }
    .xt-calendar-event-body{
        height: 533px;
        /*background-color: yellowgreen;*/
    }
    .xt-event-item{
        -moz-user-select:none;/*火狐*/
        -webkit-user-select:none;/*webkit浏览器*/
        -ms-user-select:none;/*IE10*/
        -khtml-user-select:none;/*早期浏览器*/
        user-select:none;
        height: 3rem;
        background-color: #F3F3F3;
        border-bottom: 1px solid #b3b3b3;
        color:#757575
    }
    .xt-event-item-close{
        height: 0.4rem;
        margin-right: 0.2rem;
    }
    .xt-event-item-title{
        -moz-user-select:none;
        height: 1.2rem;
        margin-left: 0.3rem;
        font-size: 15px;
        color:#454545
    }
    .xt-event-item-date{
        -moz-user-select:none;
        height: 1.2rem;
        font-size:small;
    }
    .xt-event-item-back{
        float: left;
        color:#ffffff;
        line-height: 40px;
        margin-left: 0.4rem;
        width:0.5rem
    }
</style>