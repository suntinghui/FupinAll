webpackJsonp([16],{Jw4o:function(t,i){},XUVx:function(t,i,e){"use strict";Object.defineProperty(i,"__esModule",{value:!0});var o={data:function(){return{movie:{},list:[],player:{},collected:!1}},activated:function(){this.movie=JSON.parse(localStorage.getItem("video")),this.collected=!1,this.loadVideo(),this.getNewVideos()},methods:{getNewVideos:function(){var t=this;this.$axios.post(this.$config.videoList).then(function(i){console.log(i),t.list=i.data.data.latestVideoList})},chooseVideo:function(t){this.saveHistory(),this.collected=!1,this.movie=this.list[t],this.loadVideo()},loadVideo:function(){var t=this;this.player.dispose&&this.player.dispose(),this.player=new Aliplayer({id:"player",width:"100%",height:"4.28rem",autoplay:!1,preload:!1,rePlay:!1,cover:this.movie.imgPath,source:this.movie.osskey,controlBarVisibility:"always"},function(i){i.on("play",function(){localStorage.getItem("login")||(i.pause(),t.$tip("请登录后观看！"))})}),this.movie.watchtime&&this.player.seek(this.movie.watchtime)},collect:function(){var t=this;0==this.collected?(this.collected=!0,this.$axios.post(this.$config.videoCollect,{vid:this.movie.id,createuser:JSON.parse(localStorage.getItem("login")).id}).then(function(i){"0000"==i.data.code&&t.$tip("收藏成功！")})):(this.collected=!1,this.$axios.post(this.$config.uncollectVideo,{vid:this.movie.id,createuser:JSON.parse(localStorage.getItem("login")).id}).then(function(i){console.log(i),"0000"==i.data.code&&t.$tip("取消收藏成功！")}))},back:function(){this.saveHistory(),this.$router.back(-1)},saveHistory:function(){var t=this.player.getCurrentTime();t>0&&localStorage.getItem("login")&&this.$axios.post(this.$config.addHistory,{vid:this.movie.id,createuser:JSON.parse(localStorage.getItem("login")).id,watchtime:t}).then(function(t){console.log(t)})},download:function(t){console.log(t),window.WebViewJavascriptBridge.callHandler("downloadFile",{url:t},function(t){alert("success")})}}},s={render:function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",[e("a",{staticClass:"back",on:{click:t.back}}),t._v(" "),e("div",{staticClass:"prism-player",attrs:{id:"player"}}),t._v(" "),e("div",{attrs:{id:"qw"}}),t._v(" "),e("div",{staticClass:"msg"},[e("a",{staticClass:"download",on:{click:function(i){t.download(t.movie.osskey)}}}),t._v(" "),e("a",{staticClass:"collect",class:{active:t.collected},on:{click:t.collect}}),t._v(" "),e("h1",{staticClass:"text-ellipsis"},[t._v(t._s(t.movie.title))]),t._v(" "),e("p",[t._v(t._s(t.movie.classifyName))])]),t._v(" "),e("div",{staticClass:"scroll"},[e("ul",{staticClass:"list"},t._l(t.list,function(i,o){return e("li",{key:o,on:{click:function(i){t.chooseVideo(o)}}},[e("a",[e("div",{staticClass:"img-box"},[e("img",{attrs:{src:i.imgPath}}),t._v(" "),e("span",[t._v(t._s(i.totaltime))])]),t._v(" "),e("p",[t._v(t._s(i.title))])])])}))])])},staticRenderFns:[]};var a=e("VU/8")(o,s,!1,function(t){e("Jw4o")},"data-v-01a1aebc",null);i.default=a.exports}});