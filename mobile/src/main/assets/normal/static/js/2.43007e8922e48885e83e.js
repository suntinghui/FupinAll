webpackJsonp([2],{"3fs2":function(t,e,n){var r=n("RY/4"),o=n("dSzd")("iterator"),a=n("/bQp");t.exports=n("FeBl").getIteratorMethod=function(t){if(void 0!=t)return t[o]||t["@@iterator"]||a[r(t)]}},BO1k:function(t,e,n){t.exports={default:n("fxRn"),__esModule:!0}},"RY/4":function(t,e,n){var r=n("R9M2"),o=n("dSzd")("toStringTag"),a="Arguments"==r(function(){return arguments}());t.exports=function(t){var e,n,i;return void 0===t?"Undefined":null===t?"Null":"string"==typeof(n=function(t,e){try{return t[e]}catch(t){}}(e=Object(t),o))?n:a?r(e):"Object"==(i=r(e))&&"function"==typeof e.callee?"Arguments":i}},WMoA:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=n("mvHQ"),o=n.n(r),a=n("BO1k"),i=n.n(a),c={data:function(){return{tabList:[],color:["#fda9a9","#e9bc93","#13c6c2","#e8cd5c","#c79cd7","#f1a631","#8ad87a","#cba67e","#6dcbed","#e9bc93","#c8da76"]}},created:function(){var t=this;this.$axios.post(this.$config.videoTypeList).then(function(e){console.log(e),t.tabList=e.data.data;for(var n=0;n<t.tabList.length;n++){var r=n;r>9&&(r-=10),t.tabList[n].color=t.color[r]}})},activated:function(){},methods:{showVideo:function(t){var e=!0,n=!1,r=void 0;try{for(var a,c=i()(this.list);!(e=(a=c.next()).done);e=!0){var s=a.value;s.id==t&&(localStorage.setItem("video",o()(s)),this.$router.push({path:"/video"}))}}catch(t){n=!0,r=t}finally{try{!e&&c.return&&c.return()}finally{if(n)throw r}}}}},s={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("header",{staticClass:"header"},[n("a",{staticClass:"back",on:{click:function(e){t.$router.back(-1)}}}),t._v(" "),n("p",[t._v("全部频道")])]),t._v(" "),n("ul",{staticClass:"more-link clearfix"},t._l(t.tabList,function(e,r){return n("li",{key:r},[n("a",{style:{backgroundColor:e.color},attrs:{href:""}},[n("p",{staticClass:"title"},[t._v(t._s(e.name))])])])}))])},staticRenderFns:[]};var u=n("VU/8")(c,s,!1,function(t){n("nmWH")},"data-v-15bb8db9",null);e.default=u.exports},fxRn:function(t,e,n){n("+tPU"),n("zQR9"),t.exports=n("g8Ux")},g8Ux:function(t,e,n){var r=n("77Pl"),o=n("3fs2");t.exports=n("FeBl").getIterator=function(t){var e=o(t);if("function"!=typeof e)throw TypeError(t+" is not iterable!");return r(e.call(t))}},nmWH:function(t,e){}});