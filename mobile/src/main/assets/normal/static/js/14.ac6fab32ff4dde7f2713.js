webpackJsonp([14],{CmUZ:function(e,t){},gS72:function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=i("mvHQ"),a=i.n(s),n={data:function(){return{sex:0,discribe:"",chineseName:"",num:""}},created:function(){this.num=JSON.parse(localStorage.getItem("login")).phone,this.sex=JSON.parse(localStorage.getItem("login")).sex?JSON.parse(localStorage.getItem("login")).sex:0,this.discribe=JSON.parse(localStorage.getItem("login")).discribe,this.chineseName=JSON.parse(localStorage.getItem("login")).chinesename},methods:{submit:function(){var e=this;this.$axios.post(this.$config.saveUserMsg,{userid:JSON.parse(localStorage.getItem("login")).id,chineseName:this.chineseName,sex:this.sex,discribe:this.discribe}).then(function(t){if("0000"==t.data.code){var i=localStorage.getItem("login");i&&((i=JSON.parse(i)).chinesename=e.chineseName,i.sex=e.sex,i.discribe=e.discribe),localStorage.setItem("login",a()(i));var s=e;e.$vux.alert.show({title:"",content:"保存成功",onHide:function(){s.$router.back(-1)}})}})},clearData:function(){this.sex=0,this.discribe="",this.chineseName=""}}},c={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("header",{staticClass:"header"},[i("a",{staticClass:"back",on:{click:function(t){e.$router.back(-1)}}}),e._v(" "),i("p",[e._v("个人信息")])]),e._v(" "),i("section",{staticClass:"gray-wrap"},[i("ul",{staticClass:"list"},[i("li",[i("label",[e._v("昵称：")]),e._v(" "),i("input",{directives:[{name:"model",rawName:"v-model",value:e.chineseName,expression:"chineseName"}],attrs:{placeholder:"请输入昵称",type:"text"},domProps:{value:e.chineseName},on:{input:function(t){t.target.composing||(e.chineseName=t.target.value)}}})]),e._v(" "),i("li",[i("label",[e._v("性别：")]),e._v(" "),i("a",{class:{active:0==e.sex},on:{click:function(t){e.sex=0}}},[e._m(0),e._v("\n                    男\n                ")]),e._v(" "),i("a",{class:{active:1==e.sex},on:{click:function(t){e.sex=1}}},[e._m(1),e._v("\n                    女\n                ")])]),e._v(" "),i("li",[i("label",[e._v("昵绑定手机号：")]),e._v(" "),i("span",[e._v(e._s(e.num))])]),e._v(" "),i("li",[i("label",[e._v("文字介绍：")]),e._v(" "),i("textarea",{directives:[{name:"model",rawName:"v-model",value:e.discribe,expression:"discribe"}],attrs:{placeholder:"请输入文字介绍",rows:"5"},domProps:{value:e.discribe},on:{input:function(t){t.target.composing||(e.discribe=t.target.value)}}})])]),e._v(" "),i("a",{staticClass:"btn",on:{click:e.submit}},[e._v("保存")])])])},staticRenderFns:[function(){var e=this.$createElement,t=this._self._c||e;return t("i",[t("span")])},function(){var e=this.$createElement,t=this._self._c||e;return t("i",[t("span")])}]};var r=i("VU/8")(n,c,!1,function(e){i("CmUZ")},"data-v-2d3e4c57",null);t.default=r.exports}});