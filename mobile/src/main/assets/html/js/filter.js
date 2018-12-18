var search='宋小宝';
var data = {
   list: [{
        href: 'https://www.baidu.com',
        title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
        img: 'img/img.jpg',
        time: "2018-07-09",
        type: '新闻资讯'
    }, {
        href: 'https://www.baidu.com',
        title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
        img: 'img/img.jpg',
        time: "2018-07-09",
        type: '新闻资讯'
    }, {
        href: 'https://www.baidu.com',
        title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
        img: 'img/img.jpg',
        actor: '张三、王五、李四',
        director: '慈萨斯',
        intro: '这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介',
        num: [{
            num: '1',
            href: 'https://www.baidu.com'
        }, {
            num: '2',
            href: 'https://www.baidu.com'
        }, {
            num: '3',
            href: 'https://www.baidu.com'
        }, {
            num: '4',
            href: 'https://www.baidu.com'
        }, {
            num: '5',
            href: 'https://www.baidu.com'
        }, {
            num: '6',
            href: 'https://www.baidu.com'
        }, {
            num: '7',
            href: 'https://www.baidu.com'
        }, {
            num: '8',
            href: 'https://www.baidu.com'
        }, {
            num: '9',
            href: 'https://www.baidu.com'
        }, {
            num: '10',
            href: 'https://www.baidu.com'
        }, {
            num: '11',
            href: 'https://www.baidu.com'
        }, {
            num: '12',
            href: 'https://www.baidu.com'
        }, {
            num: '13',
            href: 'https://www.baidu.com'
        }, {
            num: '14',
            href: 'https://www.baidu.com'
        }, {
            num: '15',
            href: 'https://www.baidu.com'
        }, {
            num: '16',
            href: 'https://www.baidu.com'
        }, {
            num: '17',
            href: 'https://www.baidu.com'
        }, {
            num: '18',
            href: 'https://www.baidu.com'
        }, {
            num: '19',
            href: 'https://www.baidu.com'
        }, {
            num: '20',
            href: 'https://www.baidu.com'
        }, {
            num: '21',
            href: 'https://www.baidu.com'
        }, {
            num: '22',
            href: 'https://www.baidu.com'
        }, {
            num: '23',
            href: 'https://www.baidu.com'
        }, {
            num: '24',
            href: 'https://www.baidu.com'
        }]
    }, {
        href: 'https://www.baidu.com',
        title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
        img: 'img/img.jpg',
        actor: '张三、王五、李四',
        director: '慈萨斯',
        intro: '这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介这里是简介',
        num: [{
            num: '1',
            href: 'https://www.baidu.com'
        }, {
            num: '2',
            href: 'https://www.baidu.com'
        }, {
            num: '3',
            href: 'https://www.baidu.com'
        }, {
            num: '4',
            href: 'https://www.baidu.com'
        }, {
            num: '5',
            href: 'https://www.baidu.com'
        }, {
            num: '6',
            href: 'https://www.baidu.com'
        }, {
            num: '7',
            href: 'https://www.baidu.com'
        }, {
            num: '8',
            href: 'https://www.baidu.com'
        }, {
            num: '9',
            href: 'https://www.baidu.com'
        }, {
            num: '10',
            href: 'https://www.baidu.com'
        }, {
            num: '11',
            href: 'https://www.baidu.com'
        }, {
            num: '12',
            href: 'https://www.baidu.com'
        }, {
            num: '13',
            href: 'https://www.baidu.com'
        }, {
            num: '14',
            href: 'https://www.baidu.com'
        }, {
            num: '15',
            href: 'https://www.baidu.com'
        }, {
            num: '16',
            href: 'https://www.baidu.com'
        }]
    }]
};

for (var i = 0; i < data.list.length; i++) {
    var arr = data.list[i].num;
    if (arr) {
        var n = arr.length;
        if (n > 18) {
            data.list[i].omit1 = arr.slice(0, 12);
            data.list[i].omit2 = arr.slice(n - 5, n);
            data.list[i].omit = arr.slice(12, n - 5);
        }
    }
}

var html = template('list', data);
var rex = new RegExp(search, 'g');
html = html.replace(rex, '<em>'+search+'</em>');
$('.list').html(html);


$('.num-more').click(function () {
    $(this).hide().siblings('.num-hide').show();
});

// var page = (data.list.length) % 16 == 0 ? (data.list.length) / 16 : ((data.list.length) / 16) + 1;
$('.m-style').pagination({
    pageCount: 8,
    coping: true,
    homePage: '首页',
    endPage: '末页',
    prevContent: '上一页',
    nextContent: '下一页',
    callback: function (api) {
        console.log(api.getCurrent())
    }
});