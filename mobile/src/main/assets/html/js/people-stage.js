var data = {
    list: [{
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }, {
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:36",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年",
        count: '12'
    }]
};
var data2 = {
    list: [{
        href: "baidu.com",
        img: 'img/img.jpg',
        time: "07:02",
        title: "宋小宝得意小品青春的校园搞笑，回忆校园的那些年2",
        count: '12'
    }]
};
var html = template('inner', data);
$('.inner').html(html);
$('.inner li').each(function (v, k) {
    if ((v + 1) % 4 == 0) {
        $(this).css('margin-right', 0);
    }
})
var page = (data.list.length) % 16 == 0 ? (data.list.length) / 16 : ((data.list.length) / 16) + 1;
$('.inner-box').pagination({
    pageCount: page,
    coping: true,
    homePage: '首页',
    endPage: '末页',
    prevContent: '上一页',
    nextContent: '下一页',
    callback: function (api) {
        console.log(api.getCurrent())
    }
});


var html2 = template('outer', data2);
$('.outer').html(html2);
$('.outer li').each(function (v, k) {
    if ((v + 1) % 4 == 0) {
        $(this).css('margin-right', 0);
    }
});
var page2 = (data2.list.length) % 16 == 0 ? (data2.list.length) / 16 : ((data2.list.length) / 16) + 1;
$('.outer-box').pagination({
    pageCount: page2,
    coping: true,
    homePage: '首页',
    endPage: '末页',
    prevContent: '上一页',
    nextContent: '下一页',
    callback: function (api) {
        console.log(api.getCurrent())
    }
});


$('.screen a').click(function () {
    var index = $(this).index();
    $('.screen a').removeClass('active');
    $(this).addClass('active');
    $('.lists').hide();
    $('.lists').eq(index).show();
})