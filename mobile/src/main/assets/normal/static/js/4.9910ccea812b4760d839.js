webpackJsonp([4],{ono8:function(A,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=s("mvHQ"),e=s.n(i),a=s("gsqX"),n=s.n(a),l=(s("mgS3"),{data:function(){return{list:[]}},mounted:function(){new n.a(".swiper-container",{loop:!0,autoplay:3e3,width:window.innerWidth,pagination:".swiper-pagination"})},created:function(){var A=this;this.$axios.post(this.$config.news,{}).then(function(t){console.log(t),A.list=t.data.data})},activated:function(){},methods:{showDetail:function(A,t){localStorage.setItem("news",e()(this.list[A].noticesList[t])),this.$router.push({path:"/news-details"})}}}),c={render:function(){var A=this,t=A.$createElement,i=A._self._c||t;return i("div",[A._m(0),A._v(" "),i("div",{staticClass:"scroll"},[A._m(1),A._v(" "),A._l(A.list,function(t,e){return i("div",{key:e,staticClass:"box"},[i("div",{staticClass:"label"},[i("p",[A._v(A._s(t.typeName))]),A._v(" "),i("a",{attrs:{href:"#/news-list?id="+t.typeId}},[A._v("更多\n                    "),i("i",[A._v(">")])])]),A._v(" "),i("ul",{staticClass:"list"},A._l(t.noticesList,function(t,a){return i("li",{key:a},[i("a",{on:{click:function(t){A.showDetail(e,a)}}},[i("div",{staticClass:"img-box"},[i("img",{attrs:{src:t.noticeFiles[0]?t.noticeFiles[0].osskey:s("u1NA")}})]),A._v(" "),i("p",{staticClass:"title"},[A._v(A._s(t.title))]),A._v(" "),i("p",{staticClass:"time"},[A._v(A._s(t.createtime))])])])}))])})],2)])},staticRenderFns:[function(){var A=this.$createElement,t=this._self._c||A;return t("header",{staticClass:"header"},[t("p",[this._v("资讯")])])},function(){var A=this.$createElement,t=this._self._c||A;return t("div",{staticClass:"swiper-container"},[t("div",{staticClass:"swiper-wrapper"},[t("div",{staticClass:"swiper-slide"},[t("img",{staticClass:"banner",attrs:{src:"https://sxfp-bucket.oss-cn-zhangjiakou.aliyuncs.com/banner/banner1.png",alt:""}})]),this._v(" "),t("div",{staticClass:"swiper-slide"},[t("img",{staticClass:"banner",attrs:{src:"https://sxfp-bucket.oss-cn-zhangjiakou.aliyuncs.com/banner/banner2.png",alt:""}})]),this._v(" "),t("div",{staticClass:"swiper-slide"},[t("img",{staticClass:"banner",attrs:{src:"https://sxfp-bucket.oss-cn-zhangjiakou.aliyuncs.com/banner/banner3.png",alt:""}})])]),this._v(" "),t("div",{staticClass:"swiper-pagination"})])}]};var r=s("VU/8")(l,c,!1,function(A){s("tBxH")},"data-v-7b95731e",null);t.default=r.exports},tBxH:function(A,t){},u1NA:function(A,t){A.exports="data:image/jpeg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAA8AAD/4QMvaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjYtYzEzOCA3OS4xNTk4MjQsIDIwMTYvMDkvMTQtMDE6MDk6MDEgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE3IChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo1Q0M2MDdDRDk2REYxMUU4QURBMkU0NzJGRTgyOTlFQyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo1Q0M2MDdDRTk2REYxMUU4QURBMkU0NzJGRTgyOTlFQyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjVDQzYwN0NCOTZERjExRThBREEyRTQ3MkZFODI5OUVDIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjVDQzYwN0NDOTZERjExRThBREEyRTQ3MkZFODI5OUVDIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/+4ADkFkb2JlAGTAAAAAAf/bAIQABgQEBAUEBgUFBgkGBQYJCwgGBggLDAoKCwoKDBAMDAwMDAwQDA4PEA8ODBMTFBQTExwbGxscHx8fHx8fHx8fHwEHBwcNDA0YEBAYGhURFRofHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8f/8AAEQgAagC8AwERAAIRAQMRAf/EAHIAAQADAQEBAQAAAAAAAAAAAAADBAUCAQYIAQEBAQAAAAAAAAAAAAAAAAAAAQIQAAICAgEDAwMCAwkAAAAAAAABAgMRBBIhEwUxQVFxIhSRMmGBoeFSkiNTY6QVBhEBAQEAAAAAAAAAAAAAAAAAAAER/9oADAMBAAIRAxEAPwD9QGkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAzNnzmvTfGmEZWS5ONnSS4qLxn9r5fyJpiSfmdKMOSc5fcoKKhLLlLOEk0vgGJNTet2LZRlq201pZjOxYy/jHsB7tb617Iw7F1zks5qhyS9uvUCrDz1NkpRhq7M5QeJqNeXF/Dw+g0xc1NtbMJSVVlXF442x4v0z0QEO/wCVo1K3JONlikoyqU0pLP6gWqr6bU3VZGzHrxalj9AINLf/ACbtmvt8Px59vOc8sNrPosegHH/a18pR/H2PteMqqTX9AY5j5rWlZKuNVzsj+6Crk5L6oGJafIwtujUqboOWfunW4xWFnq2BbKAAAAAAAAAAAAAfMeYVne3LV30pJVtuqPbxFrp3M+mVnoiLEkq9u2Wnq3d1OdkJxbcVxhXF5ceH7fX6gWvE1Ts2tlzvukta6Ua4ysk00srqn6gq1ZueTjZONehzhFtRn3YRyvnDQRkeO8lfVfbOGr3Jbt0u2uaXWPVxzj25eoVta21v2XKF+n2IYb7ncjPr8YQRl+YqslROy3Tqp+9N7HJSk+uFnEc9QsTyrrThG+EvGqclGr8aeOcpf3uMVjH8QIvDaae7ty71v+Rc1jl0n1azPp1YKl29ryD8htVU7Harop7qi4RlnCXTqBm07V0HDYp3Yva3JwjdX203HOVnr8EGx4vY257e5RsW93sOEYtRUfXOei+hUrTKAAAAAAAAAAAAAY3n7ra79JQslCMpTU8WOpNfb6y9iVYrd7/e/wCf/YBd/wDO22WaM5WTc5d2SzJuTxhe7ESvfKeUjBPU1X3dyz7Yxh1459W8e4Ip369ehb4muclFQlY7Jvosvi28sK0a/LUW+RWpTi2LhydsGnFNZ6MJjJ8zf4+VV0a9q2d7nl0tz4J56ri0l0Cxc16/HbldsZTvlTCUJuy+TSyspcW+oHXhGnteRcXlO9tNdfdgqrvbNFHld7uy49zW4Q6N5k0sLoBS/Lq/E0oPbc+1bCU6HDCglnL5JZePqQa3hbq7t/yNtT5VzlW4y6rK+75KVsFQAAAAAAAAAAAACC/T1r51zuhzlU268t4WcZ6ej9Pcg6/G1v8ASh/hQCjXoog4UwUIt8ml8sDyvV1q7Z3V1xjbZ++aXVgdW69FySurjYl6KcVLH6geVa2tS26aoVt+vCKjn9AOoU0wlKUIRjKTzJpJNt+7wAupruqlVbHlXNYlH+H8gI9XT1tWDhr1qEW8vGXl/VgdR16Y3yvUErprjKfu0gJSiOFFMLJ2QhGM7P3ySw3j5IJCgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH/2Q=="}});