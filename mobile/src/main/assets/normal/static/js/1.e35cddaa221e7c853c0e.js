webpackJsonp([1],{"3fs2":function(t,e,r){var n=r("RY/4"),o=r("dSzd")("iterator"),a=r("/bQp");t.exports=r("FeBl").getIteratorMethod=function(t){if(void 0!=t)return t[o]||t["@@iterator"]||a[n(t)]}},BO1k:function(t,e,r){t.exports={default:r("fxRn"),__esModule:!0}},"RY/4":function(t,e,r){var n=r("R9M2"),o=r("dSzd")("toStringTag"),a="Arguments"==n(function(){return arguments}());t.exports=function(t){var e,r,i;return void 0===t?"Undefined":null===t?"Null":"string"==typeof(r=function(t,e){try{return t[e]}catch(t){}}(e=Object(t),o))?r:a?n(e):"Object"==(i=n(e))&&"function"==typeof e.callee?"Arguments":i}},WMoA:function(t,e,r){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=r("mvHQ"),o=r.n(n),a=r("BO1k"),i=r.n(a),c={data:function(){return{tabList:[],color:["#fda9a9","#e9bc93","#13c6c2","#e8cd5c","#c79cd7","#f1a631","#8ad87a","#cba67e","#6dcbed","#e9bc93","#c8da76"]}},created:function(){var t=this;this.$axios.post(this.$config.videoTypeList).then(function(e){console.log(e),t.tabList=e.data.data;for(var r=0;r<t.tabList.length;r++){var n=r;n>9&&(n-=10),t.tabList[r].color=t.color[n]}})},methods:{showVideo:function(t){var e=!0,r=!1,n=void 0;try{for(var a,c=i()(this.list);!(e=(a=c.next()).done);e=!0){var s=a.value;s.id==t&&(localStorage.setItem("video",o()(s)),this.$router.push({path:"/video"}))}}catch(t){r=!0,n=t}finally{try{!e&&c.return&&c.return()}finally{if(r)throw n}}}}},s={render:function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",[r("header",{staticClass:"header"},[r("a",{staticClass:"back",on:{click:function(e){t.$router.back(-1)}}}),t._v(" "),r("p",[t._v("全部频道")])]),t._v(" "),r("ul",{staticClass:"more-link clearfix"},t._l(t.tabList,function(e,n){return r("li",{key:n},[r("a",{style:{backgroundColor:e.color}},[r("p",{staticClass:"title"},[t._v(t._s(e.name))])])])}))])},staticRenderFns:[]};var l=r("VU/8")(c,s,!1,function(t){r("XJF7")},"data-v-8dde264c",null);e.default=l.exports},XJF7:function(t,e){},fxRn:function(t,e,r){r("+tPU"),r("zQR9"),t.exports=r("g8Ux")},g8Ux:function(t,e,r){var n=r("77Pl"),o=r("3fs2");t.exports=r("FeBl").getIterator=function(t){var e=o(t);if("function"!=typeof e)throw TypeError(t+" is not iterable!");return n(e.call(t))}}});