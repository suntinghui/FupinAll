webpackJsonp([15],{QlWu:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=s("mvHQ"),a=s.n(i),n={data:function(){return{tel:"",password:""}},methods:{login:function(){var t=this;if(1==this.verTel()){if(!this.password)return this.$tip("请输入密码"),!1;this.$axios.post(this.$config.login,{username:this.tel,password:this.password}).then(function(e){console.log(e.data.data),"0000"==e.data.code?(localStorage.setItem("login",a()(e.data.data)),t.$router.push({name:"index"})):t.$tip(e.data.msg)})}},showPlugin:function(t){this.$vux.alert.show({content:t,onShow:function(){console.log("Plugin: I'm showing")},onHide:function(){console.log("Plugin: I'm hiding now")}})},showPluginAuto:function(t){var e=this;this.showPlugin(t),setTimeout(function(){e.$vux.alert.hide()},3e3)},verTel:function(){return this.tel?!!/^[1][3,4,5,7,8][0-9]{9}$/.test(this.tel)||(this.$tip("手机号格式不正确"),!1):(this.$tip("请输入手机号"),!1)}}},o={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",[t._m(0),t._v(" "),s("div",{staticClass:"container"},[t._m(1),t._v(" "),s("div",{staticClass:"input",attrs:{id:"step2"}},[s("div",{staticClass:"input-item"},[s("input",{directives:[{name:"model",rawName:"v-model",value:t.tel,expression:"tel"}],attrs:{type:"text",autocomplete:"off",value:"",placeholder:"手机号"},domProps:{value:t.tel},on:{input:function(e){e.target.composing||(t.tel=e.target.value)}}})]),t._v(" "),s("div",{staticClass:"input-item"},[s("input",{directives:[{name:"model",rawName:"v-model",value:t.password,expression:"password"}],attrs:{type:"password",autocomplete:"off",value:"",placeholder:"密码"},domProps:{value:t.password},on:{input:function(e){e.target.composing||(t.password=e.target.value)}}})]),t._v(" "),s("a",{staticClass:"tips",attrs:{href:"#/forget"}},[t._v("忘记密码？")]),t._v(" "),s("a",{staticClass:"next-step",attrs:{href:"javascript:void(0)",id:"register-btn"},on:{click:function(e){t.login()}}},[t._v("登录")])])])])},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("header",{staticClass:"header"},[e("p",[this._v("登录/注册")])])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"nav"},[e("a",{staticClass:"active",attrs:{href:"#/login"}},[e("span",[this._v("登录")])]),this._v(" "),e("a",{attrs:{href:"#/register"}},[e("span",[this._v("注册")])])])}]};var r=s("VU/8")(n,o,!1,function(t){s("cZba")},"data-v-62a0f9d4",null);e.default=r.exports},cZba:function(t,e){}});