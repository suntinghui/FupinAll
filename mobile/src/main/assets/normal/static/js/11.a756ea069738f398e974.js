webpackJsonp([11],{RQXq:function(e,t){},gS72:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i={data:function(){return{sex:0,discribe:"",chineseName:"",num:""}},created:function(){this.num=JSON.parse(localStorage.getItem("login")).phone},methods:{submit:function(){var e=this;this.$axios.post(this.$config.saveUserMsg,{userid:JSON.parse(localStorage.getItem("login")).id,chineseName:this.chineseName,sex:this.sex,discribe:this.discribe}).then(function(t){console.log(t);var s=e;"0000"==t.data.code&&e.$vux.alert.show({title:"",content:"上传成功",onHide:function(){s.clearData(),s.$router.push({path:"/index/personal"})}})})},clearData:function(){this.sex=0,this.discribe="",this.chineseName=""}}},a={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",[s("header",{staticClass:"header"},[s("a",{staticClass:"back",on:{click:function(t){e.$router.back(-1)}}}),e._v(" "),s("p",[e._v("个人信息")])]),e._v(" "),s("section",{staticClass:"gray-wrap"},[s("ul",{staticClass:"list"},[s("li",[s("label",[e._v("昵称：")]),e._v(" "),s("input",{directives:[{name:"model",rawName:"v-model",value:e.chineseName,expression:"chineseName"}],attrs:{placeholder:"请输入昵称",type:"text"},domProps:{value:e.chineseName},on:{input:function(t){t.target.composing||(e.chineseName=t.target.value)}}})]),e._v(" "),s("li",[s("label",[e._v("性别：")]),e._v(" "),s("a",{class:{active:0==e.sex},on:{click:function(t){e.sex=0}}},[e._m(0),e._v("\n                    男\n                ")]),e._v(" "),s("a",{class:{active:1==e.sex},on:{click:function(t){e.sex=1}}},[e._m(1),e._v("\n                    女\n                ")])]),e._v(" "),s("li",[s("label",[e._v("昵绑定手机号：")]),e._v(" "),s("span",[e._v(e._s(e.num))])]),e._v(" "),s("li",[s("label",[e._v("文字介绍：")]),e._v(" "),s("textarea",{directives:[{name:"model",rawName:"v-model",value:e.discribe,expression:"discribe"}],attrs:{placeholder:"请输入文字介绍",rows:"5"},domProps:{value:e.discribe},on:{input:function(t){t.target.composing||(e.discribe=t.target.value)}}})])]),e._v(" "),s("a",{staticClass:"btn",on:{click:e.submit}},[e._v("保存")])])])},staticRenderFns:[function(){var e=this.$createElement,t=this._self._c||e;return t("i",[t("span")])},function(){var e=this.$createElement,t=this._self._c||e;return t("i",[t("span")])}]};var n=s("VU/8")(i,a,!1,function(e){s("RQXq")},"data-v-7c4080a4",null);t.default=n.exports}});