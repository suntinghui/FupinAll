webpackJsonp([19],{"/TR5":function(t,e){},"8jPt":function(t,e){},IQ6V:function(t,e){},J3r1:function(t,e){},LKVA:function(t,e){!function(){for(var t=0,e=["webkit","moz"],n=0;n<e.length&&!window.requestAnimationFrame;++n)window.requestAnimationFrame=window[e[n]+"RequestAnimationFrame"],window.cancelAnimationFrame=window[e[n]+"CancelAnimationFrame"]||window[e[n]+"CancelRequestAnimationFrame"];window.requestAnimationFrame||(window.requestAnimationFrame=function(e,n){var i=(new Date).getTime();console.log(i,t);var o=Math.max(0,16.7-(i-t)),a=i-t,s=window.setTimeout(function(){e(a)},o);return t=i+o,s}),window.cancelAnimationFrame||(window.cancelAnimationFrame=function(t){clearTimeout(t)})}()},NHnr:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=n("7+uW"),o={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",{attrs:{id:"app"}},[e("keep-alive",[this.$route.meta.keepAlive?e("router-view"):this._e()],1),this._v(" "),this.$route.meta.keepAlive?this._e():e("router-view")],1)},staticRenderFns:[]};var a=n("VU/8")({name:"App"},o,!1,function(t){n("J3r1")},null,null).exports,s=n("/ocq");i.a.use(s.a);var l=new s.a({routes:[{path:"/",redirect:"/index/index"},{path:"/index",name:"enter",component:function(){return n.e(17).then(n.bind(null,"2NXm"))},meta:{keepAlive:!0},children:[{path:"index",name:"index",component:function(){return n.e(7).then(n.bind(null,"Qt9A"))},meta:{keepAlive:!0}},{path:"personal",name:"personal",component:function(){return n.e(3).then(n.bind(null,"cIuR"))},meta:{keepAlive:!0}},{path:"news",name:"news",component:function(){return Promise.all([n.e(0),n.e(5)]).then(n.bind(null,"ono8"))},meta:{keepAlive:!0}}]},{path:"/video-classify",name:"video-classify",component:function(){return n.e(1).then(n.bind(null,"WMoA"))}},{path:"/video-timelist",name:"video-timelist",component:function(){return n.e(14).then(n.bind(null,"J57a"))}},{path:"/video-history",name:"video-history",component:function(){return n.e(15).then(n.bind(null,"W8H5"))}},{path:"/video-collect",name:"video-collect",component:function(){return n.e(10).then(n.bind(null,"1hYS"))}},{path:"/video",name:"video",component:function(){return n.e(13).then(n.bind(null,"XUVx"))},meta:{keepAlive:!0}},{path:"/news-list",name:"news-list",component:function(){return Promise.all([n.e(0),n.e(6)]).then(n.bind(null,"e/yx"))}},{path:"/news-details",name:"news-details",component:function(){return Promise.all([n.e(0),n.e(11)]).then(n.bind(null,"Eoxv"))}},{path:"/login",name:"login",component:function(){return n.e(12).then(n.bind(null,"QlWu"))}},{path:"/register",name:"register",component:function(){return n.e(9).then(n.bind(null,"8zp9"))}},{path:"/forget",name:"forget",component:function(){return n.e(8).then(n.bind(null,"zHOx"))}},{path:"/help",name:"help",component:function(){return n.e(2).then(n.bind(null,"YQSR"))}},{path:"/feedback",name:"feedback",component:function(){return n.e(4).then(n.bind(null,"1oos"))}},{path:"/personal-msg",name:"personal-msg",component:function(){return n.e(16).then(n.bind(null,"gS72"))}}]}),r=(n("gQNZ"),n("mtWM")),u=n.n(r),c={baseUrl:"http://47.92.175.158:8080/",videoList:"api/getHomePageVideoList",videoTypeList:"api/getVodTypeList",audioTypeList:"api/getAudioTypeList",videoSearchList:"api/searchVodListByKeyword",videoCollect:"account/saveUserCollection",collectVideo:"account/getUserCollectionInfoList",uncollectVideo:"account/deleteUserCollection",addHistory:"account/addVodHistory",historyList:"account/getVodHistoryList",newsTypeList:"api/getNewsTypeList",newsList:"api/getMoreNewsInfoList",news:"api/getNewsInfoListForApp",getUserRegion:"account/getUserRegion",getMobileCaptcha:"account/getMobileCaptcha",mobileExistCheck:"account/mobileExistCheck",register:"account/register",login:"account/login",modifyPasswordByMobile:"account/modifyPasswordByMobile",getOss:"commons/getApiOssUrl",uploadPortrait:"account/uploadUserPortrait",saveUserMsg:"account/saveUserBaseInfo",feedBack:"account/addFeedBack"},h=(n("LKVA"),Number,String,Boolean,Number,Number,Number,Number,{name:"LyTab",props:{lineWidth:{type:Number,default:1},activeColor:{type:String,default:"red"},fixBottom:{type:Boolean,default:!1},value:{},additionalX:{type:Number,default:50},reBoundExponent:{type:Number,default:10},sensitivity:{type:Number,default:1e3},reBoundingDuration:{type:Number,default:360}},computed:{style:function(){return{transitionTimingFunction:this.transitionTimingFunction,transitionDuration:this.transitionDuration+"ms",transform:"translate3d("+this.translateX+"px, 0px, 0px)"}},transitionDuration:function(){return this.touching||!this.reBounding&&!this.touching?"0":this.reBounding&&!this.touching?this.reBoundingDuration:void 0},transitionTimingFunction:function(){return this.reBounding?"cubic-bezier(0.25, 0.46, 0.45, 0.94)":"cubic-bezier(0.1, 0.57, 0.1, 1)"},viewAreaWidth:function(){return this.$refs.viewArea.offsetWidth},listWidth:function(){return this.$refs.list.offsetWidth-this.viewAreaWidth},isMoveLeft:function(){return this.currentX<=this.startX},isMoveRight:function(){return this.currentX>=this.startX}},data:function(){return{speed:0,touching:!1,reBounding:!1,translateX:0,startX:0,lastX:0,currentX:0,frameTime:16.7,frameStartTime:0,frameEndTime:0,inertiaFrame:0,zeroSpeed:.001,acceleration:0}},mounted:function(){this.bindEvent()},methods:{handleTouchStart:function(t){cancelAnimationFrame(this.inertiaFrame),this.lastX=t.touches[0].clientX},handleTouchMove:function(t){this.listWidth<=0||(t.preventDefault(),t.stopPropagation(),this.touching=!0,this.startMoveTime=this.endMoveTime,this.startX=this.lastX,this.currentX=t.touches[0].clientX,this.moveFellowTouch(),this.endMoveTime=t.timeStamp)},handleTouchEnd:function(t){if(this.touching=!1,this.checkReboundX())cancelAnimationFrame(this.inertiaFrame);else{var e=t.timeStamp-this.endMoveTime,n=this.endMoveTime-this.startMoveTime;if(e>100)return;this.speed=(this.lastX-this.startX)/n,this.acceleration=this.speed/this.sensitivity,this.frameStartTime=(new Date).getTime(),this.inertiaFrame=requestAnimationFrame(this.moveByInertia)}},checkReboundX:function(){return this.reBounding=!1,this.translateX>0?(this.reBounding=!0,this.translateX=0):this.translateX<-this.listWidth&&(this.reBounding=!0,this.translateX=-this.listWidth),0===this.translateX||this.translateX===-this.listWidth},bindEvent:function(){this.$el.addEventListener("touchstart",this.handleTouchStart,!1),this.$el.addEventListener("touchmove",this.handleTouchMove,!1),this.$el.addEventListener("touchend",this.handleTouchEnd,!1)},moveFellowTouch:function(){this.isMoveLeft?this.translateX<=0&&this.translateX+this.listWidth>0||this.translateX>0?this.translateX+=this.currentX-this.lastX:this.translateX+this.listWidth<=0&&(this.translateX+=this.additionalX*(this.currentX-this.lastX)/(this.viewAreaWidth+Math.abs(this.translateX+this.listWidth))):this.translateX>=0?this.translateX+=this.additionalX*(this.currentX-this.lastX)/(this.viewAreaWidth+this.translateX):(this.translateX<=0&&this.translateX+this.listWidth>=0||this.translateX+this.listWidth<=0)&&(this.translateX+=this.currentX-this.lastX),this.lastX=this.currentX},moveByInertia:function(){this.frameEndTime=(new Date).getTime(),this.frameTime=this.frameEndTime-this.frameStartTime,this.isMoveLeft?this.translateX<=-this.listWidth?(this.acceleration*=(this.reBoundExponent+Math.abs(this.translateX+this.listWidth))/this.reBoundExponent,this.speed=Math.min(this.speed-this.acceleration,0)):this.speed=Math.min(this.speed-this.acceleration*this.frameTime,0):this.isMoveRight&&(this.translateX>=0?(this.acceleration*=(this.reBoundExponent+this.translateX)/this.reBoundExponent,this.speed=Math.max(this.speed-this.acceleration,0)):this.speed=Math.max(this.speed-this.acceleration*this.frameTime,0)),this.translateX+=this.speed*this.frameTime/2,Math.abs(this.speed)<=this.zeroSpeed?this.checkReboundX():(this.frameStartTime=this.frameEndTime,this.inertiaFrame=requestAnimationFrame(this.moveByInertia))}}}),d={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",{ref:"viewArea",staticClass:"ly-tab",class:{"ly-tab-fix-bottom":this.fixBottom}},[e("div",{ref:"list",staticClass:"ly-tab-list",style:this.style},[this._t("default")],2)])},staticRenderFns:[]};var p=n("VU/8")(h,d,!1,function(t){n("/TR5")},null,null).exports,m={render:function(){var t=this.$createElement,e=this._self._c||t;return e("a",{staticClass:"ly-tab-item",style:this.$parent.value===this.id?this.activeStyle:{},on:{click:this.onItemClicked}},[e("div",{staticClass:"ly-tab-item-icon"},[this._t("icon")],2),this._v(" "),e("div",{staticClass:"ly-tab-item-label"},[this._t("default")],2)])},staticRenderFns:[]};var f=n("VU/8")({name:"LyTabItem",computed:{activeStyle:function(){return{color:this.$parent.activeColor,borderColor:this.$parent.activeColor,borderWidth:this.$parent.lineWidth,borderBottomStyle:"solid",fontWeight:"bold"}}},data:function(){return{id:(this.$parent.$children.length||1)-1}},methods:{onItemClicked:function(){this.$parent.$emit("input",this.id)}}},m,!1,function(t){n("hLY5")},null,null).exports,g=function t(e){arguments.length>1&&void 0!==arguments[1]&&arguments[1];t.installed||(e.component(p.name,p),e.component(f.name,f))};"undefined"!=typeof window&&window.Vue&&g(window.Vue);var v={install:g,version:"1.0.1",LyTab:p,LyTabItem:f},w=n("Peep"),b=n("mvHQ"),y=n.n(b),x=n("BEQ0"),_=n.n(x),S=n("WTNC"),B=n.n(S),T=n("ypEt"),X=n.n(T),C=n("qbvd"),k=n.n(C);Object,String,Boolean,Boolean,Boolean,Boolean,Boolean,Boolean,Boolean,Boolean,Boolean,Boolean,Boolean,Boolean,Boolean,Object,Object,Boolean,Number;var $={name:"scroller",props:{value:{type:Object,default:function(){return{pulldownStatus:"",pullupStatus:""}}},height:String,lockX:Boolean,lockY:Boolean,scrollbarX:Boolean,scrollbarY:Boolean,bounce:{type:Boolean,default:!0},useOriginScroll:{type:Boolean,default:!1},useTransition:{type:Boolean,default:!0},preventDefault:{type:Boolean,default:!1},stopPropagation:Boolean,boundryCheck:{type:Boolean,default:!0},gpuAcceleration:{type:Boolean,default:!0},usePulldown:{type:Boolean,default:!1},usePullup:{type:Boolean,default:!1},pulldownConfig:{type:Object,default:function(){return{}}},pullupConfig:{type:Object,default:function(){return{}}},enableHorizontalSwiping:{type:Boolean,default:!1},scrollBottomOffset:{type:Number,default:0}},methods:{reset:function(t,e,n){t&&(void 0!==t.left&&this._xscroll.scrollLeft(t.left,e,n),void 0!==t.top&&this._xscroll.scrollTop(t.top,e,n)),this._xscroll&&this._xscroll.resetSize()},donePulldown:function(){var t=this;this.pulldown.reset(function(){t.reset()}),this.currentValue.pulldownStatus="default"},disablePullup:function(){this.pullup.stop(),this.currentValue.pullupStatus="disabled"},enablePullup:function(){this.currentValue.pullupStatus="default",this.pullup.restart()},donePullup:function(){this.pullup.complete(),this.reset(),this.currentValue.pullupStatus="default"},getStyles:function(){var t=this.height;!this.height&&this.$el&&!this.$el.style.height&&this.lockX&&(t=document.documentElement.clientHeight+"px",this.reset()),this.height&&0===this.height.indexOf("-")&&(t=document.documentElement.clientHeight+parseInt(this.height)+"px"),this.styles={height:""+t}}},created:function(){var t=this;this.value?this.currentValue=this.value:this.currentValue={pulldownStatus:"",pullupStatus:""},this.handleOrientationchange=function(){setTimeout(function(){t.reset()},100)}},data:function(){return{currentValue:{},styles:{}}},watch:{currentValue:{handler:function(t){var e;this.$emit("input",(e=t,JSON.parse(y()(e))))},deep:!0},height:function(){this.getStyles()},value:{handler:function(t){"default"===t.pullupStatus&&"default"!==this.currentValue.pullupStatus&&this.donePullup(),"default"===t.pulldownStatus&&"default"!==this.currentValue.pulldownStatus&&this.donePulldown(),"disabled"===t.pullupStatus&&"disabled"!==this.currentValue.pullupStatus&&this.disablePullup(),"enabled"===t.pullupStatus&&"disabled"===this.currentValue.pullupStatus&&this.enablePullup()},deep:!0}},mounted:function(){var t=this;this.uuid=Math.random().toString(36).substring(3,8),this.$nextTick(function(){t.$el.setAttribute("id","vux-scroller-"+t.uuid);var e=null;if(t.$slots.default&&(e=t.$slots.default[0].elm),!e)throw new Error("no content is found");if(t._xscroll=new B.a({renderTo:"#vux-scroller-"+t.uuid,lockX:t.lockX,lockY:t.lockY,scrollbarX:t.scrollbarX,scrollbarY:t.scrollbarY,content:e,bounce:t.bounce,useOriginScroll:t.useOriginScroll,useTransition:t.useTransition,preventDefault:t.preventDefault,boundryCheck:t.boundryCheck,gpuAcceleration:t.gpuAcceleration,stopPropagation:t.stopPropagation}),t._xscroll.on("scroll",function(){if(t._xscroll){var e=t._xscroll.getScrollTop();t.$emit("on-scroll",{top:e,left:t._xscroll.getScrollLeft()}),e>=t._xscroll.containerHeight-t._xscroll.height-t.scrollBottomOffset&&t.$emit("on-scroll-bottom")}}),t.usePulldown){var n=t.$slots.pulldown,i=_()({content:"Pull Down To Refresh",height:60,autoRefresh:!1,downContent:"Pull Down To Refresh",upContent:"Release To Refresh",loadingContent:"Loading...",clsPrefix:"xs-plugin-pulldown-"},t.pulldownConfig);n&&(i.container=n[0].elm),t.pulldown=new X.a(i),t._xscroll.plug(t.pulldown),t.pulldown.on("loading",function(e){t.$emit("on-pulldown-loading",t.uuid)}),t.pulldown.on("statuschange",function(e){t.currentValue.pulldownStatus=e.newVal})}if(t.usePullup){var o=t.$slots.pullup,a=_()({content:"Pull Up To Refresh",pullUpHeight:60,height:40,autoRefresh:!1,downContent:"Release To Refresh",upContent:"Pull Up To Refresh",loadingContent:"Loading...",clsPrefix:"xs-plugin-pullup-"},t.pullupConfig);o&&(a.container=o[0].elm),t.pullup=new k.a(a),t._xscroll.plug(t.pullup),t.pullup.on("loading",function(e){t.$emit("on-pullup-loading",t.uuid)}),t.pullup.on("statuschange",function(e){t.currentValue.pullupStatus=e.newVal})}t.enableHorizontalSwiping&&(t._xscroll.on("panstart",function(e){2!==e.direction&&4!==e.direction||(e.preventDefault(),t.scrollbarY&&(t._xscroll.userConfig.scrollbarY=!1),t._xscroll.userConfig.lockY=!0)}),t._xscroll.on("panend",function(){t.scrollbarY&&(t._xscroll.userConfig.scrollbarY=!0),t._xscroll.userConfig.lockY=!1})),t._xscroll.render(),window.addEventListener("orientationchange",t.handleOrientationchange,!1)}),this.getStyles()},updated:function(){this.reset()},beforeDestroy:function(){this.pullup&&(this._xscroll.unplug(this.pullup),this.pullup.pluginDestructor()),this.pulldown&&(this._xscroll.unplug(this.pulldown),this.pulldown.pluginDestructor()),window.removeEventListener("orientationchange",this.handleOrientationchange,!1),this._xscroll.destroy(),this._xscroll=null}};var V={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",{style:this.styles},[e("div",{staticClass:"xs-container"},[this._t("default"),this._v(" "),this._t("pulldown"),this._v(" "),this._t("pullup")],2)])},staticRenderFns:[]};var L=n("VU/8")($,V,!1,function(t){n("XHfh")},null,null).exports,A=(Boolean,String,{name:"load-more",props:{showLoading:{type:Boolean,default:!0},tip:String}}),P={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"vux-loadmore weui-loadmore",class:{"weui-loadmore_line":!t.showLoading,"weui-loadmore_dot":!t.showLoading&&!t.tip}},[t.showLoading?n("i",{staticClass:"weui-loading"}):t._e(),t._v(" "),n("span",{directives:[{name:"show",rawName:"v-show",value:t.tip||!t.showLoading,expression:"tip || !showLoading"}],staticClass:"weui-loadmore__tips"},[t._v(t._s(t.tip))])])},staticRenderFns:[]};var E=n("VU/8")(A,P,!1,function(t){n("IQ6V")},null,null).exports;var N=n("VU/8")({data:function(){return{}},methods:{}},null,!1,function(t){n("YNi/")},null,null).exports,F=i.a.extend(N),M=null;i.a.use(w.a),i.a.component("scroller",L),i.a.component("load-more",E),i.a.use(v),i.a.prototype.$tip=function(t){M||(M=new F({el:document.createElement("div")})),console.log(t),M.$el.setAttribute("id","tip"),M.$el.innerHTML="<div>"+t+"</div>",M.options=t,document.body.appendChild(M.$el),setTimeout(function(){document.body.removeChild(document.getElementById("tip"))},1e3)},i.a.config.productionTip=!1;var W=u.a.create({baseURL:c.baseUrl,headers:{"content-type":"application/json;charset=utf-8"}});i.a.prototype.$axios=W,i.a.prototype.$config=c,new i.a({el:"#app",router:l,components:{App:a},template:"<App/>"}),window.onload=function(){var t=this;t.width=750,t.fontSize=100,t.widthProportion=function(){console.log(document.getElementsByTagName("html"));var e=document.getElementsByTagName("html")[0].offsetWidth/t.width;return e>1?1:e<.32?.32:e},t.changePage=function(){document.getElementsByTagName("html")[0].setAttribute("style","font-size:"+t.widthProportion()*t.fontSize+"px !important")},t.changePage(),window.addEventListener("resize",function(){t.changePage()},!1)}},"TI/Z":function(t,e){},XHfh:function(t,e){},"YNi/":function(t,e){},gQNZ:function(t,e){},hLY5:function(t,e){},mzja:function(t,e,n){"use strict";var i=n("JkZY"),o=(i.a,Boolean,String,String,Number,String,String,Boolean,Object,Boolean,{mixins:[i.a],name:"x-dialog",model:{prop:"show",event:"change"},props:{show:{type:Boolean,default:!1},maskTransition:{type:String,default:"vux-mask"},maskZIndex:[String,Number],dialogTransition:{type:String,default:"vux-dialog"},dialogClass:{type:String,default:"weui-dialog"},hideOnBlur:Boolean,dialogStyle:Object,scroll:{type:Boolean,default:!0,validator:function(t){return!0}}},computed:{maskStyle:function(){if(void 0!==this.maskZIndex)return{zIndex:this.maskZIndex}}},mounted:function(){"undefined"!=typeof window&&window.VUX_CONFIG&&"VIEW_BOX"===window.VUX_CONFIG.$layout&&(this.layout="VIEW_BOX")},watch:{show:function(t){this.$emit("update:show",t),this.$emit(t?"on-show":"on-hide"),t?this.addModalClassName():this.removeModalClassName()}},methods:{shouldPreventScroll:function(){var t=/iPad|iPhone|iPod/i.test(window.navigator.userAgent),e=this.$el.querySelector("input")||this.$el.querySelector("textarea");if(t&&e)return!0},hide:function(){this.hideOnBlur&&(this.$emit("update:show",!1),this.$emit("change",!1),this.$emit("on-click-mask"))}},data:function(){return{layout:""}}}),a={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"vux-x-dialog",class:{"vux-x-dialog-absolute":"VIEW_BOX"===t.layout}},[n("transition",{attrs:{name:t.maskTransition}},[n("div",{directives:[{name:"show",rawName:"v-show",value:t.show,expression:"show"}],staticClass:"weui-mask",style:t.maskStyle,on:{click:t.hide}})]),t._v(" "),n("transition",{attrs:{name:t.dialogTransition}},[n("div",{directives:[{name:"show",rawName:"v-show",value:t.show,expression:"show"}],class:t.dialogClass,style:t.dialogStyle},[t._t("default")],2)])],1)},staticRenderFns:[]};var s=n("VU/8")(o,a,!1,function(t){n("TI/Z")},null,null).exports,l=(Boolean,String,String,String,Boolean,String,String,Number,String,{name:"alert",components:{XDialog:s},created:function(){void 0!==this.value&&(this.showValue=this.value)},props:{value:Boolean,title:String,content:String,buttonText:String,hideOnBlur:{type:Boolean,default:!1},maskTransition:{type:String,default:"vux-mask"},dialogTransition:{type:String,default:"vux-dialog"},maskZIndex:[Number,String]},data:function(){return{showValue:!1}},methods:{_onHide:function(){this.showValue=!1}},watch:{value:function(t){this.showValue=t},showValue:function(t){this.$emit("input",t)}}}),r={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"vux-alert"},[n("x-dialog",{attrs:{"mask-transition":t.maskTransition,"dialog-transition":t.dialogTransition,"hide-on-blur":t.hideOnBlur,"mask-z-index":t.maskZIndex},on:{"on-hide":function(e){t.$emit("on-hide")},"on-show":function(e){t.$emit("on-show")}},model:{value:t.showValue,callback:function(e){t.showValue=e},expression:"showValue"}},[n("div",{staticClass:"weui-dialog__hd"},[n("strong",{staticClass:"weui-dialog__title"},[t._v(t._s(t.title))])]),t._v(" "),n("div",{staticClass:"weui-dialog__bd"},[t._t("default",[n("div",{domProps:{innerHTML:t._s(t.content)}})])],2),t._v(" "),n("div",{staticClass:"weui-dialog__ft"},[n("a",{staticClass:"weui-dialog__btn weui-dialog__btn_primary",attrs:{href:"javascript:;"},on:{click:t._onHide}},[t._v(t._s(t.buttonText||"确定"))])])])],1)},staticRenderFns:[]};var u=n("VU/8")(l,r,!1,function(t){n("8jPt")},null,null);e.a=u.exports}},["NHnr"]);