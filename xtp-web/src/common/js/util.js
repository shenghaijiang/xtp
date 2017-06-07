import { Message  } from 'element-ui';
import {SystemConfigAPI} from '../../api/api'

var SIGN_REGEXP = /([yMdhsm])(\1*)/g;
var DEFAULT_PATTERN = 'yyyy-MM-dd';
function padding(s, len) {
    var len = len - (s + '').length;
    for (var i = 0; i < len; i++) { s = '0' + s; }
    return s;
};

export default {
    getQueryStringByName: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        var context = "";
        if (r != null)
            context = r[2];
        reg = null;
        r = null;
        return context == null || context == "" || context == "undefined" ? "" : context;
    },
    formatDate: {
        format: function (date, pattern) {
            pattern = pattern || DEFAULT_PATTERN;
            return pattern.replace(SIGN_REGEXP, function ($0) {
                switch ($0.charAt(0)) {
                    case 'y': return padding(date.getFullYear(), $0.length);
                    case 'M': return padding(date.getMonth() + 1, $0.length);
                    case 'd': return padding(date.getDate(), $0.length);
                    case 'w': return date.getDay() + 1;
                    case 'h': return padding(date.getHours(), $0.length);
                    case 'm': return padding(date.getMinutes(), $0.length);
                    case 's': return padding(date.getSeconds(), $0.length);
                }
            });
        },
        parse: function (dateString, pattern) {
            var matchs1 = pattern.match(SIGN_REGEXP);
            var matchs2 = dateString.match(/(\d)+/g);
            if (matchs1.length == matchs2.length) {
                var _date = new Date(1970, 0, 1);
                for (var i = 0; i < matchs1.length; i++) {
                    var _int = parseInt(matchs2[i]);
                    var sign = matchs1[i];
                    switch (sign.charAt(0)) {
                        case 'y': _date.setFullYear(_int); break;
                        case 'M': _date.setMonth(_int - 1); break;
                        case 'd': _date.setDate(_int); break;
                        case 'h': _date.setHours(_int); break;
                        case 'm': _date.setMinutes(_int); break;
                        case 's': _date.setSeconds(_int); break;
                    }
                }
                return _date;
            }
            return null;
        }

    }

};

export const ArrayFun=Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
}

export const DateFun=Date.prototype.Format=function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
export const DateCal=function(date){
    if(this instanceof DateCal){
        let now = new Date(date); //当前日期
        let nowDayOfWeek = now.getDay(); //今天本周的第几天
        let nowDay = now.getDate(); //当前日
        let nowMonth = now.getMonth(); //当前月
        let nowYear = now.getYear(); //当前年
        nowYear += (nowYear < 2000) ? 1900 : 0;
        this.beforeSevenDay=function(){
            var weekStartDate = new Date(nowYear, nowMonth, nowDay-6);
            return weekStartDate.Format('yyyy-MM-dd')
        };
        this.afterSevenDay=function(){
            var weekStartDate = new Date(nowYear, nowMonth, nowDay+6);
            return weekStartDate.Format('yyyy-MM-dd')
        };
    }else{
        return new DateCal(date);
    }
}


export const compareFun= {
    compareSign(standardValue,sign,value){
        let signFlag=true;
        switch(sign){
            case 0:signFlag=standardValue==value;break;
            case 1:signFlag=standardValue>value;break;
            case 2:signFlag=standardValue>=value;break;
            case 3:signFlag=standardValue<value;break;
            case 4:signFlag=standardValue<=value;break;
            default:break;
        }
        return signFlag
    }
}
/*物料、半成品检验值判断*/
export const GetValue={
    getCompareSignText(value){
        /*  {key:'等于',val:'0'},{key:'大于',val:'1'},{key:'大于等于',val:'2'},{key:'小于',val:'3'},{key:'小于等于',val:'4'}*/
        let compareSignText=""
        switch(value){
            case 0:compareSignText="等于";break;
            case 1:compareSignText="大于";break;
            case 2:compareSignText="大于等于";break;
            case 3:compareSignText="小于";break;
            case 4:compareSignText="小于等于";break;
            default:break;
        }
        return compareSignText
    }
}

export const CheckExp={
    isTel(tel)
    {
        if (!(/^1[34578]\d{9}$/.test(tel))) return false
        return true
    },
    isQQ(qq){
        var re=/[1-9][0-9]{4,}/;
        if(!re.test(qq)) return false;
        return true
    }

}

export const CodeChange={
    specialCharacter(code){
        code=code.replace(/\amp;/g,'');
        code=code.replace(/\&lt;/g,'<');
        code=code.replace(/\&gt;/g,'>');
        code=code.replace(/\&quot;/g,'"');
        return code
    },
    substringCharacter(remark){
        if(remark!=''&&(typeof remark!="undefined")){
            let text=remark.substr(7,100)!=""?remark.substr(0,7)+'...':remark
            return text
        }else{
            return ''
        }
    }
}

export const MessageBox={
    codeMessage(code,message,fn){
        return new Promise((resolve, reject) => {
        let errorCode=[{code:1,msg:'成功'},{code:0,msg:'逻辑错误'}]
        SystemConfigAPI.getSysConfigList({}).then((res) => {
            if(res.data.code==1) Object.assign(errorCode,res.data.data)
                let message='';
                message=errorCode.find(element => element.code==code).msg;
                resolve({message});
            })
        })
    }
}

export const Compares={
    compareAscending:function(sortname){
        return function (object1,object2) {
            var value1=object1[sortname];
            var value2=object2[sortname];
            if(value1<value2){
                return -1;
            }else if(value1>value2){
                return 1;
            }else{
                return 0;
            }
        }
    }

};