webpackJsonp([11],{"2NXm":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n={data:function(){return{tab:1}},methods:{changeTab:function(e){this.tab=e},goUpload:function(){console.log("11"),this.$axios.post(this.$config.getOss,{type:3}).then(function(e){console.log(e);var t=e.data.data,a={accessKeyId:t.accessid,accessKeySecret:t.signature,secretToken:t.policy,expireTime:t.expire};window.WebViewJavascriptBridge.callHandler("uploadFile",a,function(e){alert("success")})})}}},s={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticClass:"content"},[a("router-view")],1),e._v(" "),a("footer",[a("ul",[a("li",[a("a",{staticClass:"home",class:{active:1==e.tab},attrs:{href:"#/index/index"},on:{click:function(t){e.changeTab(1)}}},[e._v("\n                    首页\n                ")])]),e._v(" "),a("li",[a("a",{staticClass:"news",class:{active:2==e.tab},attrs:{href:"#/index/news"},on:{click:function(t){e.changeTab(2)}}},[e._v("\n                    资讯\n                ")])]),e._v(" "),a("li",[a("a",{staticClass:"video",class:{active:3==e.tab},attrs:{href:"#/index/news"},on:{click:e.goUpload}},[e._v("\n                    视频\n                ")])]),e._v(" "),a("li",[a("a",{staticClass:"personal",class:{active:4==e.tab},attrs:{href:"#/index/personal"},on:{click:function(t){e.changeTab(4)}}},[e._v("\n                    我的\n                ")])])])])])},staticRenderFns:[]};var c=a("VU/8")(n,s,!1,function(e){a("2W6O")},"data-v-7977c4e3",null);t.default=c.exports},"2W6O":function(e,t){}});