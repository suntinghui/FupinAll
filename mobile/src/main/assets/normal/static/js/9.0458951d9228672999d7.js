webpackJsonp([9],{RrAR:function(t,i){},WMoA:function(t,i,a){"use strict";Object.defineProperty(i,"__esModule",{value:!0});var e=a("mvHQ"),o=a.n(e),s=a("BO1k"),r=a.n(s),c={data:function(){return{videoList:[],audioList:[],color:["#fda9a9","#e9bc93","#13c6c2","#e8cd5c","#c79cd7","#f1a631","#8ad87a","#cba67e","#6dcbed","#e9bc93","#c8da76"]}},created:function(){var t=this;this.$axios.post(this.$config.videoTypeList).then(function(i){console.log(i),t.videoList=i.data.data;for(var a=0;a<t.videoList.length;a++){var e=a;e>9&&(e-=10),t.videoList[a].color=t.color[e]}}),this.$axios.post(this.$config.audioTypeList).then(function(i){console.log(i),t.audioList=i.data.data;for(var a=0;a<t.audioList.length;a++){var e=a;e>9&&(e-=10),t.audioList[a].color=t.color[e]}})},methods:{showVideo:function(t){var i=!0,a=!1,e=void 0;try{for(var s,c=r()(this.list);!(i=(s=c.next()).done);i=!0){var n=s.value;n.id==t&&(localStorage.setItem("video",o()(n)),this.$router.push({path:"/video"}))}}catch(t){a=!0,e=t}finally{try{!i&&c.return&&c.return()}finally{if(a)throw e}}}}},n={render:function(){var t=this,i=t.$createElement,a=t._self._c||i;return a("div",[a("header",{staticClass:"header"},[a("a",{staticClass:"back",on:{click:function(i){t.$router.back(-1)}}}),t._v(" "),a("p",[t._v("全部频道")])]),t._v(" "),a("section",[a("p",{staticClass:"divider"},[t._v("视频")]),t._v(" "),a("ul",{staticClass:"video-link clearfix"},t._l(t.videoList,function(i,e){return a("li",{key:e},[a("a",{style:{backgroundColor:i.color},attrs:{href:"#/video-timelist?mark=0&id="+i.id}},[a("p",{staticClass:"title"},[t._v(t._s(i.name))])])])})),t._v(" "),a("p",{staticClass:"divider"},[t._v("音频")]),t._v(" "),a("ul",{staticClass:"video-link clearfix"},t._l(t.audioList,function(i,e){return a("li",{key:e},[a("a",{style:{backgroundColor:i.color},attrs:{href:"#/video-timelist?mark=1&id="+i.id}},[a("p",{staticClass:"title"},[t._v(t._s(i.name))])])])}))])])},staticRenderFns:[]};var d=a("VU/8")(c,n,!1,function(t){a("RrAR")},"data-v-bfb5c646",null);i.default=d.exports}});