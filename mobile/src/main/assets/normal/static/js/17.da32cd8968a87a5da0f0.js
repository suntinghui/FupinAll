webpackJsonp([17],{"2NXm":function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={data:function(){return{tab:0,tabList:[{name:"首页",src:"/index/index",class:"home"},{name:"资讯",src:"/index/news",class:"news"},{name:"视频",src:"",class:"video"},{name:"我的",src:"/index/personal",class:"personal"}]}},watch:{$route:function(e,t){"/index/index"==e.fullPath?this.tab=0:"/index/news"==e.fullPath?this.tab=1:"/index/personal"==e.fullPath?this.tab=3:this.tab=0}},methods:{changeTab:function(e,t){this.tab=e,2==e?this.goUpload():(console.log(e,t),this.$router.push({path:t}))},goUpload:function(){var e=this;if(!JSON.parse(localStorage.getItem("login")))return this.$vux.alert.show({content:"请先登录",onHide:function(){e.$router.push({path:"/login"})}}),!1;this.$axios.post(this.$config.getOss,{type:3}).then(function(t){console.log(t);var n=t.data.data,a={id:JSON.parse(localStorage.getItem("login")).id,accessKeyId:n.accessid,accessKeySecret:n.signature,secretToken:n.policy,expireTime:n.expire},i=navigator.userAgent,s=(navigator.appVersion,i.indexOf("Android")>-1||i.indexOf("Linux")>-1),o=!!i.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);s&&window.WebViewJavascriptBridge.callHandler("uploadFile",a,function(e){alert("success")}),o&&e.setupWebViewJavascriptBridge(function(e){e.callHandler("uploadFile",a,function(e){})})})},setupWebViewJavascriptBridge:function(e){if(window.WebViewJavascriptBridge)return e(WebViewJavascriptBridge);if(window.WVJBCallbacks)return window.WVJBCallbacks.push(e);window.WVJBCallbacks=[e];var t=document.createElement("iframe");t.style.display="none",t.src="https://__bridge_loaded__",document.documentElement.appendChild(t),setTimeout(function(){document.documentElement.removeChild(t)},0)}}},i={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",[n("div",{staticClass:"content"},[n("router-view")],1),e._v(" "),n("footer",[n("ul",e._l(e.tabList,function(t,a){return n("li",{key:a},[n("a",{class:[t.class,{active:e.tab==a}],on:{click:function(n){e.changeTab(a,t.src)}}},[e._v("\n                    "+e._s(t.name)+"\n                ")])])}))])])},staticRenderFns:[]};var s=n("VU/8")(a,i,!1,function(e){n("6xaX")},"data-v-2d119316",null);t.default=s.exports},"6xaX":function(e,t){}});