webpackJsonp([7],{LVoD:function(t,s){},Qt9A:function(t,s,i){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var e=i("mvHQ"),a=i.n(e),o={data:function(){return{selected:0,tabList:[],list:[],keywords:"",total:"",searchParams:{cp:1,ps:10,district:0},isShowLoading:0,pullupDefaultConfig:{content:"",pullUpHeight:60,height:40,autoRefresh:!1,downContent:"",upContent:"",loadingContent:"",clsPrefix:"xs-plugin-pullup-"}}},created:function(){var t=this;this.getHeatVideo(),this.$axios.post(this.$config.videoTypeList).then(function(s){console.log(s),t.tabList=s.data.data,t.tabList.unshift({name:"精选"})})},activated:function(){this.selected=0,this.searchParams.district=0,this.getHeatVideo()},watch:{selected:function(t,s){this.clearKeywords(),0==t?(delete this.searchParams.vodClassifyId,this.getHeatVideo()):(this.searchParams.vodClassifyId=this.tabList[t].id,this.clearList(),this.getVideo())}},methods:{showVideo:function(t){localStorage.setItem("video",a()(this.list[t])),this.$router.push({path:"/video"})},getHeatVideo:function(){var t=this;this.$axios.post(this.$config.videoList).then(function(s){console.log(s),t.list=s.data.data.heatVideoList})},getVideo:function(){var t=this;this.$axios.post(this.$config.videoSearchList,this.searchParams).then(function(s){console.log(s),t.list=t.list.concat(s.data.data.vodList),t.total=s.data.data.totalNum,t.isShowLoading=0})},search:function(){0==this.selected&&(this.selected=-1),""!=this.keywords&&(this.searchParams.keywords=this.keywords,this.clearList(),this.getVideo())},clearKeywords:function(){this.searchParams.district=0,this.keywords="",delete this.searchParams.keywords},clearList:function(){this.list=[],this.searchParams.cp=1,this.isShowLoading=0},loadmore:function(){this.isShowLoading=1,this.total/10>this.searchParams.cp?(this.searchParams.cp++,this.getVideo(),this.$refs.scroller.enablePullup()):(this.isShowLoading=2,1==this.searchParams.cp&&this.$refs.scroller.reset({top:0}))},listTab:function(t){this.searchParams.district=t,this.clearList(),this.getVideo()}}},l={render:function(){var t=this,s=t.$createElement,i=t._self._c||s;return i("div",[i("header",[i("div",{staticClass:"right"},[i("ly-tab",{attrs:{activeColor:"#fff"},model:{value:t.selected,callback:function(s){t.selected=s},expression:"selected"}},t._l(t.tabList,function(s,e){return i("ly-tab-item",{key:e},[t._v("\n                    "+t._s(s.name)+"\n                ")])})),t._v(" "),i("input",{directives:[{name:"model",rawName:"v-model",value:t.keywords,expression:"keywords"}],staticClass:"search",attrs:{type:"text",placeholder:"刘老根大舞台"},domProps:{value:t.keywords},on:{input:[function(s){s.target.composing||(t.keywords=s.target.value)},t.search]}})],1),t._v(" "),t._m(0)]),t._v(" "),0==t.selected?i("div",{staticClass:"scroll"},[t._m(1),t._v(" "),i("ul",{staticClass:"list"},t._l(t.list,function(s,e){return i("li",{key:e},[i("a",{on:{click:function(s){t.showVideo(e)}}},[i("div",[i("img",{attrs:{src:s.imgPath}}),t._v(" "),i("span",[t._v(t._s(s.totaltime))])]),t._v(" "),i("p",[t._v(t._s(s.title))])])])}))]):t._e(),t._v(" "),0!=t.selected?i("div",{staticClass:"tab"},[i("a",{class:{active:0==t.searchParams.district},on:{click:function(s){t.listTab(0)}}},[t._v("省内")]),t._v(" "),i("a",{class:{active:1==t.searchParams.district},on:{click:function(s){t.listTab(1)}}},[t._v("省外")])]):t._e(),t._v(" "),0!=t.selected&&t.list.length>0?i("div",{staticClass:"l-scroll"},[i("scroller",{ref:"scroller",attrs:{"lock-x":"","scrollbar-y":"","use-pullup":"",height:"100%","pullup-config":t.pullupDefaultConfig},on:{"on-pullup-loading":t.loadmore}},[i("div",[i("ul",{staticClass:"list"},t._l(t.list,function(s,e){return i("li",{key:e},[i("a",{on:{click:function(s){t.showVideo(e)}}},[i("div",[i("img",{attrs:{src:s.imgPath}}),t._v(" "),i("span",[t._v(t._s(s.totaltime))])]),t._v(" "),i("p",[t._v(t._s(s.title))])])])}))]),t._v(" "),1==t.isShowLoading?i("load-more",{attrs:{tip:"正在加载"}}):t._e(),t._v(" "),2==t.isShowLoading?i("load-more",{attrs:{"show-loading":!1,tip:"已经到底啦","background-color":"#fbf9fe"}}):t._e()],1)],1):t._e(),t._v(" "),0==t.list.length?i("div",{staticClass:"nodata"},[t._v("\n        没有相关视频哦！\n    ")]):t._e()])},staticRenderFns:[function(){var t=this.$createElement,s=this._self._c||t;return s("div",{staticClass:"left"},[s("a",{staticClass:"menu",attrs:{href:"#/video-classify"}}),this._v(" "),s("a",{staticClass:"clock",attrs:{href:"#/video-history"}})])},function(){var t=this.$createElement,s=this._self._c||t;return s("div",[s("img",{staticClass:"banner",attrs:{src:i("hKcQ"),alt:""}}),this._v(" "),s("p",{staticClass:"label"},[this._v("正在热播")])])}]};var c=i("VU/8")(o,l,!1,function(t){i("LVoD")},"data-v-6128b773",null);s.default=c.exports},hKcQ:function(t,s,i){t.exports=i.p+"static/img/banner.f304218.jpg"}});