<template>
  <div class="full-calendar-header">
    <div class="header-left">
      <slot name="header-left">
      </slot>
    </div>
    <div class="header-center">
      <span class="prev-month" @click.stop="goPrev">{{leftArrow}}</span>
      <span class="title">
        <select placeholder="请选择" v-model="currentYear" size="small" style="width:4rem" @change="yearHandleChange">
          <option v-for="item in 2050" v-if="item>=1900" :label="item" :value="item"></option>
        </select>年
        <select placeholder="请选择" v-model="currentMonth" size="small" style="width:4rem" @change="monthHandleChange">
        <option v-for="item in 12" v-if="item>=1" :label="item" :value="item"></option>
        </select>月
      </span>
      <span class="next-month" @click.stop="goNext">{{rightArrow}}</span>
    </div>
    <div class="header-right">
      <slot name="header-right">
      </slot>
    </div>
  </div>
</template>
<script type="text/babel">
  import dateFunc from './dateFunc'

  export default {
    created () {
      this.dispatchEvent()
    },
    props : {
      currentDate : {}, //当前日期
      titleFormat : {},
      firstDay    : {}, //
      monthNames  : {}
    },
    data () {
      return {
        title      : '',
        currentYear:1900,//控件默认年
        currentMonth:1, //控件默认月
        leftArrow  : '<<',
        rightArrow : '>>',
        headDate : new Date()
      }
    },
    watch : {
      currentDate (val) {
        if (!val) return
        this.headDate = val
        // this.headDate = JSON.parse(JSON.stringify(val))
      }
    },
    methods : {
      yearHandleChange(){
        this.headDate=new Date(new Date(this.headDate).setFullYear(this.currentYear))
        this.dispatchEvent()
      },
      monthHandleChange(){
        this.headDate=new Date(new Date(this.headDate).setMonth(this.currentMonth-1))
        this.dispatchEvent()
      },
      goPrev () {
        console.log(this.headDate,1111)
        this.headDate = this.changeMonth(this.headDate, -1)
        this.dispatchEvent()
      },
      goNext () {
        console.log(this.headDate)
        this.headDate = this.changeMonth(this.headDate, +1)
        this.dispatchEvent()
      },
      changeMonth (date, num) {
        let dt = new Date(date)
        return new Date(dt.setMonth(dt.getMonth() + num))
      },
      dispatchEvent() {
        //this.title = dateFunc.format(this.headDate, this.titleFormat, this.monthNames)
        this.currentYear=parseInt(new Date(this.headDate).Format('yyyy'))
        this.currentMonth=parseInt(new Date(this.headDate).Format('MM'))
       // console.log(this.headDate)
        let startDate = dateFunc.getStartDate(this.headDate)
        let curWeekDay = startDate.getDay()

        // 1st day of this monthView
        let diff = parseInt(this.firstDay) - curWeekDay
        if (diff) diff -= 7
        startDate.setDate(startDate.getDate() + diff) 

        // the month view is 6*7
        let endDate = dateFunc.changeDay(startDate, 41)

        // 1st day of current month
        let currentDate = dateFunc.getStartDate(this.headDate)

        this.$emit('change', 
          dateFunc.format(startDate, 'yyyy-MM-dd'),
          dateFunc.format(endDate, 'yyyy-MM-dd'),
          dateFunc.format(currentDate,'yyyy-MM-dd'),
          this.headDate
        )
      }
    }
  }
</script>
<style lang="scss">
.full-calendar-header{
  display: flex;
  align-items: center;
  .header-left,.header-right{
    flex:1;
  }
  .header-center{
    flex:3;
    text-align:center;
    .title{
      margin: 0 10px;
    }
    .prev-month,.next-month{
      -moz-user-select:none;/*火狐*/
      -webkit-user-select:none;/*webkit浏览器*/
      -ms-user-select:none;/*IE10*/
      -khtml-user-select:none;/*早期浏览器*/
      user-select:none;
      cursor: pointer;
    }
  }
}
</style>
