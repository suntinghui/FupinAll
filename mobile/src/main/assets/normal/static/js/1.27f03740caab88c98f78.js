webpackJsonp([1],{"8G+t":function(t,s){},"9wG7":function(t,s,i){t.exports=i.p+"static/img/g3.48915f7.jpg"},FHvz:function(t,s,i){t.exports=i.p+"static/img/g4.15ac862.jpg"},P2Iv:function(t,s,i){t.exports=i.p+"static/img/g2.302d4a5.jpg"},Qt9A:function(t,s,i){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var e=i("3cXf"),a=i.n(e),o=i("oWSD"),n=(i("sNxO"),{data:function(){return{selected:0,tabList:[],list:[],keywords:"",total:"",searchParams:{cp:1,ps:10,district:0},isShowLoading:0,pullupDefaultConfig:{content:"",pullUpHeight:60,height:40,autoRefresh:!1,downContent:"",upContent:"",loadingContent:"",clsPrefix:"xs-plugin-pullup-"},liveList:[{src:"https://www.douyu.com/16228",img:i("fKot"),title:"广场舞-美丽中国",name:"广场舞频道",headImg:"../../assets/g1.jpg",personNum:"1009"},{src:"https://www.huya.com/766334",img:i("P2Iv"),title:"今夜起舞",name:"广场舞频道",headImg:"../../assets/g2.jpg",personNum:"203"},{src:"https://www.huya.com/425558",img:i("9wG7"),title:"暖暖的的幸福广场舞",name:"广场舞频道",headImg:"../../assets/g3.jpg",personNum:"1800"},{src:"https://www.huya.com/11738744",img:i("FHvz"),title:"广场舞现场教学",name:"广场舞频道",headImg:"../../assets/g4.jpg",personNum:"3212"}]}},created:function(){var t=this;this.getHeatVideo(),this.$axios.post(this.$config.videoTypeList).then(function(s){console.log(s),t.tabList=s.data.data,t.tabList.unshift({name:"精选"})})},mounted:function(){new o.a(".swiper-container",{autoplay:!0,width:window.innerWidth,pagination:{el:".swiper-pagination"}})},activated:function(){this.selected=0,this.searchParams.district=0,this.getHeatVideo()},watch:{selected:function(t,s){console.log(t,s),this.clearKeywords(),0==t?(delete this.searchParams.mark,delete this.searchParams.vodClassifyId,this.getHeatVideo()):t>0&&(this.searchParams.mark=0,this.searchParams.vodClassifyId=this.tabList[t].id,this.clearList(),this.getVideo())}},methods:{showVideo:function(t){localStorage.setItem("video",a()(this.list[t])),this.$router.push({path:"/video"})},getHeatVideo:function(){var t=this;this.$axios.post(this.$config.videoList,{}).then(function(s){console.log(s),t.list=s.data.data.heatVideoList})},getVideo:function(){var t=this;this.$axios.post(this.$config.videoSearchList,this.searchParams).then(function(s){console.log(s),t.list=t.list.concat(s.data.data.vodList),t.total=s.data.data.totalNum,t.isShowLoading=0})},search:function(){0==this.selected&&(this.selected=-1),""!=this.keywords&&(this.searchParams.keywords=this.keywords,this.clearList(),this.getVideo())},clearKeywords:function(){this.searchParams.district=0,this.keywords="",delete this.searchParams.keywords},clearList:function(){this.list=[],this.searchParams.cp=1,this.isShowLoading=0},loadmore:function(){this.isShowLoading=1,this.total/10>this.searchParams.cp?(this.searchParams.cp++,this.getVideo(),this.$refs.scroller.enablePullup()):(this.isShowLoading=2,1==this.searchParams.cp&&this.$refs.scroller.reset({top:0}))},listTab:function(t){this.searchParams.district=t,this.clearList(),this.getVideo()}}}),c={render:function(){var t=this,s=t.$createElement,i=t._self._c||s;return i("div",[i("header",[i("div",{staticClass:"right"},[i("ly-tab",{attrs:{activeColor:"#fff"},model:{value:t.selected,callback:function(s){t.selected=s},expression:"selected"}},t._l(t.tabList,function(s,e){return i("ly-tab-item",{key:e},[t._v("\n                    "+t._s(s.name)+"\n                ")])})),t._v(" "),i("input",{directives:[{name:"model",rawName:"v-model",value:t.keywords,expression:"keywords"}],staticClass:"search",attrs:{type:"text",placeholder:"刘老根大舞台"},domProps:{value:t.keywords},on:{input:[function(s){s.target.composing||(t.keywords=s.target.value)},t.search]}})],1),t._v(" "),t._m(0)]),t._v(" "),0==t.selected?i("div",{staticClass:"scroll"},[t._m(1),t._v(" "),i("p",{staticClass:"label"},[t._v("正在热播")]),t._v(" "),i("ul",{staticClass:"list"},t._l(t.list,function(s,e){return i("li",{key:e},[i("a",{on:{click:function(s){t.showVideo(e)}}},[i("div",[i("img",{attrs:{src:s.imgPath}}),t._v(" "),i("span",[t._v(t._s(s.totaltime))])]),t._v(" "),i("p",[t._v(t._s(s.title))])])])})),t._v(" "),i("p",{staticClass:"label bt"},[t._v("全民直播")]),t._v(" "),i("ul",{staticClass:"list"},t._l(t.liveList,function(s,e){return i("li",{key:e},[i("a",{attrs:{href:s.src}},[i("div",[i("img",{attrs:{src:s.img}}),t._v(" "),i("span",[t._v(t._s(s.totaltime))])]),t._v(" "),i("p",[t._v(t._s(s.title))])])])}))]):t._e(),t._v(" "),0!=t.selected?i("div",{staticClass:"tab"},[i("a",{class:{active:0==t.searchParams.district},on:{click:function(s){t.listTab(0)}}},[t._v("省内")]),t._v(" "),i("a",{class:{active:1==t.searchParams.district},on:{click:function(s){t.listTab(1)}}},[t._v("省外")])]):t._e(),t._v(" "),0!=t.selected&&t.list.length>0?i("div",{staticClass:"l-scroll"},[i("scroller",{ref:"scroller",attrs:{"lock-x":"","scrollbar-y":"","use-pullup":"",height:"100%","pullup-config":t.pullupDefaultConfig},on:{"on-pullup-loading":t.loadmore}},[i("div",[i("ul",{staticClass:"list"},t._l(t.list,function(s,e){return i("li",{key:e},[i("a",{on:{click:function(s){t.showVideo(e)}}},[i("div",[i("img",{attrs:{src:s.imgPath}})]),t._v(" "),i("p",[t._v(t._s(s.title))])])])}))]),t._v(" "),1==t.isShowLoading?i("load-more",{attrs:{tip:"正在加载"}}):t._e(),t._v(" "),2==t.isShowLoading?i("load-more",{attrs:{"show-loading":!1,tip:"已经到底啦","background-color":"#fbf9fe"}}):t._e()],1)],1):t._e(),t._v(" "),0==t.list.length?i("div",{staticClass:"nodata"},[t._v("\n        没有相关视频哦！\n    ")]):t._e()])},staticRenderFns:[function(){var t=this.$createElement,s=this._self._c||t;return s("div",{staticClass:"left"},[s("a",{staticClass:"menu",attrs:{href:"#/video-classify"}}),this._v(" "),s("a",{staticClass:"clock",attrs:{href:"#/video-history"}})])},function(){var t=this.$createElement,s=this._self._c||t;return s("div",{staticClass:"swiper-container"},[s("div",{staticClass:"swiper-wrapper"},[s("div",{staticClass:"swiper-slide"},[s("img",{staticClass:"banner",attrs:{src:"https://sxfp-bucket.oss-cn-zhangjiakou.aliyuncs.com/banner/banner1.png",alt:""}})]),this._v(" "),s("div",{staticClass:"swiper-slide"},[s("img",{staticClass:"banner",attrs:{src:"https://sxfp-bucket.oss-cn-zhangjiakou.aliyuncs.com/banner/banner2.png",alt:""}})]),this._v(" "),s("div",{staticClass:"swiper-slide"},[s("img",{staticClass:"banner",attrs:{src:"https://sxfp-bucket.oss-cn-zhangjiakou.aliyuncs.com/banner/banner3.png",alt:""}})])]),this._v(" "),s("div",{staticClass:"swiper-pagination"})])}]};var r=i("vSla")(n,c,!1,function(t){i("8G+t")},"data-v-c60a94cc",null);s.default=r.exports},fKot:function(t,s,i){t.exports=i.p+"static/img/g1.7a5cd51.jpg"}});