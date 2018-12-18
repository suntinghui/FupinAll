$(function () {
    initData();
    initEvent();
});

function initData(){
    var headline = {
        img: 'img/img.jpg',
        title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
        details: '定档，由国内最顶尖的喜剧人岳云鹏、郭德纲、张小斐、艾伦、潘斌龙、孙越、张泰维等联袂主演，“憨豆先生”罗温·艾金森特别出演的春节档喜剧电影《欢乐喜剧人》自宣布定档鸡年大年初一上映后一直备受关注。今日，片方首度曝光了一支名为“猪一样的队友”特辑，小岳岳领衔的喜剧天团在片中技能全开、各展神通...',
        src: 'https://www.baidu.com'
    };

    var news = {
        title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
        list: [{
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }],
        link: [{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }]
    };

    var inform = {
        list: [{
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        },{
            src: 'https://www.baidu.com',
            title: '致富新技植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }, {
            src: 'https://www.baidu.com',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技'
        }]
    };

    var movieNew = {
        list: [{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        }]
    };

    var movieHot = {
        list: [{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            time:'01:45',
            play:'2009'
        }]
    };

    var live = {
        list: [{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            name: '中央电视台',
            headImg:'img/img.jpg',
            personNum:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            name: '中央电视台',
            headImg:'img/img.jpg',
            personNum:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            name: '中央电视台',
            headImg:'img/img.jpg',
            personNum:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            name: '中央电视台',
            headImg:'img/img.jpg',
            personNum:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            name: '中央电视台',
            headImg:'img/img.jpg',
            personNum:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            name: '中央电视台',
            headImg:'img/img.jpg',
            personNum:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            name: '中央电视台',
            headImg:'img/img.jpg',
            personNum:'2009'
        },{
            src: 'https://www.baidu.com',
            img: 'img/img.jpg',
            title: '致富新技术-大葱种植技术大葱种植技术大葱种植技术大葱种植技',
            name: '中央电视台',
            headImg:'img/img.jpg',
            personNum:'2009'
        }]
    };
    
    var ad={
        img: 'img/img.jpg'
    };

    var headlineHtml = template('headlineHtml', headline);
    $('#headline').html(headlineHtml);

    var newsHtml = template('newsHtml', news);
    $('#news').html(newsHtml);

    var informHtml = template('informHtml', inform);
    $('#inform').html(informHtml);

    var movieNewHtml = template('movieNewHtml', movieNew);
    $('#movieNew').html(movieNewHtml);

    var movieHotHtml = template('movieHotHtml', movieHot);
    $('#movieHot').html(movieHotHtml);

    var liveHtml = template('liveHtml', live);
    $('#live').html(liveHtml);
    
    var adHtml = template('adHtml', ad);
    $('#ad').html(adHtml);
}

function initEvent(){
    $('.movie').eq(0).show();
    $('.tab a').click(function(){
        $(this).addClass('active').siblings().removeClass('active');
        $('.movie').eq($(this).index()).show().siblings().hide();
    })
}






