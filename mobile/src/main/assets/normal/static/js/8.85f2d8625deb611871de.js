webpackJsonp([8],{"1hYS":function(t,s,i){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var e=i("3cXf"),o=i.n(e),l={data:function(){return{list:[]}},created:function(){var t=this;this.$axios.post(this.$config.collectVideo,{createuser:JSON.parse(localStorage.getItem("login")).id}).then(function(s){console.log(s),t.list=s.data.data.collectionsModel})},methods:{showVideo:function(t){console.log(this.list[t].vodModel);var s=this.list[t].vodModel;s.osskey=this.list[t].vodModel.osspath,s.imgPath=this.list[t].vodModel.imgurl,s.classifyName=this.list[t].vodModel.classify_name,localStorage.setItem("video",o()(s)),this.$router.push({path:"/video"})}}},a={render:function(){var t=this,s=t.$createElement,i=t._self._c||s;return i("div",[i("header",{staticClass:"header"},[i("a",{staticClass:"back",on:{click:function(s){t.$router.back(-1)}}}),t._v(" "),i("p",[t._v("我的收藏")])]),t._v(" "),i("section",[i("div",{staticClass:"main-list"},[i("ul",{staticClass:"video-list"},t._l(t.list,function(s,e){return i("li",{key:e},[i("a",{on:{click:function(s){t.showVideo(e)}}},[i("div",{staticClass:"v-img"},[i("img",{attrs:{src:s.vodModel.imgurl}})]),t._v(" "),i("div",{staticClass:"info"},[t._v(t._s(s.vodModel.title))])])])}))])])])},staticRenderFns:[]};var c=i("vSla")(l,a,!1,function(t){i("tm3R")},"data-v-f590c328",null);s.default=c.exports},tm3R:function(t,s){}});