webpackJsonp([15],{J57a:function(t,s,a){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var i=a("mvHQ"),e=a.n(i),o={data:function(){return{list:[],searchParams:{cp:1,ps:10,timeType:null},total:0,isShowLoading:0,pullupDefaultConfig:{content:"",pullUpHeight:60,height:40,autoRefresh:!1,downContent:"",upContent:"",loadingContent:"",clsPrefix:"xs-plugin-pullup-"}}},created:function(){this.searchParams.mark=this.$route.query.mark,this.searchParams.vodClassifyId=this.$route.query.id,this.getVideo()},methods:{getVideo:function(){var t=this;this.$axios.post(this.$config.videoSearchList,this.searchParams).then(function(s){console.log(s),t.list=t.list.concat(s.data.data.vodList),t.total=s.data.data.totalNum,t.isShowLoading=0})},showVideo:function(t){localStorage.setItem("video",e()(this.list[t])),this.$router.push({path:"/video"})},loadmore:function(){this.isShowLoading=1,this.total/10>this.searchParams.cp?(this.searchParams.cp++,this.getVideo(),this.$refs.scroller.enablePullup()):(this.isShowLoading=2,1==this.searchParams.cp&&this.$refs.scroller.reset({top:0}))},tab:function(t){this.clearList(),this.searchParams.timeType=t||null,console.log(this.searchParams.timeType),this.getVideo()},clearList:function(){this.list=[],this.total=0}}},l={render:function(){var t=this,s=t.$createElement,a=t._self._c||s;return a("div",[a("header",{staticClass:"header"},[a("a",{staticClass:"back",on:{click:function(s){t.$router.back(-1)}}})]),t._v(" "),a("section",[a("p",{staticClass:"divider"},[t._v("发布时间")]),t._v(" "),a("div",{staticClass:"tab"},[a("a",{class:{active:null==t.searchParams.timeType},on:{click:function(s){t.tab()}}},[a("span",[t._v("全部")])]),t._v(" "),a("a",{class:{active:1==t.searchParams.timeType},on:{click:function(s){t.tab(1)}}},[a("span",[t._v("最近一周")])]),t._v(" "),a("a",{class:{active:2==t.searchParams.timeType},on:{click:function(s){t.tab(2)}}},[a("span",[t._v("最近一月")])])]),t._v(" "),t.list.length>0?a("div",{staticClass:"l-scroll"},[a("scroller",{ref:"scroller",attrs:{"lock-x":"","scrollbar-y":"","use-pullup":"",height:"100%","pullup-config":t.pullupDefaultConfig},on:{"on-pullup-loading":t.loadmore}},[a("div",[a("ul",{staticClass:"list"},t._l(t.list,function(s,i){return a("li",{key:i},[a("a",{on:{click:function(s){t.showVideo(i)}}},[a("div",[a("img",{attrs:{src:s.imgPath}}),t._v(" "),a("span",[t._v(t._s(s.totaltime))])]),t._v(" "),a("p",[t._v(t._s(s.title))])])])}))]),t._v(" "),1==t.isShowLoading?a("load-more",{attrs:{tip:"正在加载"}}):t._e(),t._v(" "),2==t.isShowLoading?a("load-more",{attrs:{"show-loading":!1,tip:"已经到底啦","background-color":"#fbf9fe"}}):t._e()],1)],1):t._e(),t._v(" "),0==t.list.length?a("div",{staticClass:"nodata"},[t._v("\n            没有相关视频哦！\n        ")]):t._e()])])},staticRenderFns:[]};var n=a("VU/8")(o,l,!1,function(t){a("W4hF")},"data-v-3b769df8",null);s.default=n.exports},W4hF:function(t,s){}});