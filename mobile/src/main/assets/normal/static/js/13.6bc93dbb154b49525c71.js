webpackJsonp([13],{Jyv5:function(t,i){},W8H5:function(t,i,s){"use strict";Object.defineProperty(i,"__esModule",{value:!0});var e=s("mvHQ"),a=s.n(e),o={data:function(){return{history:[]}},created:function(){var t=this;this.$axios.post(this.$config.historyList,{userid:JSON.parse(localStorage.getItem("login")).id}).then(function(i){console.log(i),t.history=i.data.data})},methods:{showVideo:function(t){console.log(this.history[t]);var i={id:this.history[t].vid,osskey:this.history[t].vossPath,imgPath:this.history[t].vimgPath,title:this.history[t].vtitle,watchtime:this.history[t].watchtime,classifyName:this.history[t].classifyName};localStorage.setItem("video",a()(i)),this.$router.push({path:"/video"})}}},c={render:function(){var t=this,i=t.$createElement,s=t._self._c||i;return s("div",[s("header",{staticClass:"header"},[s("a",{staticClass:"back",on:{click:function(i){t.$router.back(-1)}}}),t._v(" "),s("p",[t._v("观看历史")])]),t._v(" "),s("section",[s("div",{staticClass:"main-list"},[s("ul",{staticClass:"video-list"},t._l(t.history,function(i,e){return s("li",{key:e},[s("a",{on:{click:function(i){t.showVideo(e)}}},[s("div",{staticClass:"v-img"},[s("img",{attrs:{src:i.vimgPath}})]),t._v(" "),s("div",{staticClass:"info"},[t._v(t._s(i.vtitle))]),t._v(" "),s("div",{staticClass:"time"},[t._v(t._s(i.createtime))])])])}))])])])},staticRenderFns:[]};var r=s("VU/8")(o,c,!1,function(t){s("Jyv5")},"data-v-48c2449a",null);i.default=r.exports}});