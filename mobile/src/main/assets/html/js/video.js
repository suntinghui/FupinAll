$(function () {

    var movie = {
        title: '这里是视频标题这里是视频标题',
        src: 'http://www.runoob.com/try/demo_source/movie.mp4',
        img: 'img/img.jpg',
        content: '这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊这里是视频的简介啊没错啊简介接啊'
    };
    
    var player = new Aliplayer({
        id: 'player',
        width: '880px',
        height: '560px',
        autoplay: false,
        preload: false,
        rePlay: false,
        cover: movie.img,
        source: movie.src
    }, function (player) {
        console.log('播放器创建好了。');
    });

    player.on('ready', function (e) {

    });

    player.on('startSeek', function (e) {
        player.pause();

    });
    player.on('completeSeek', function (e) {
        player.seek(e.paramData);
        player.play();
    });
    
    $('.title').text(movie.title);
    $('#content').text(movie.content);

    initData();
});

function initData() {


    var movielist = {
        list: [{
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }, {
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }, {
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }, {
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }, {
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }, {
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }, {
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }, {
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }, {
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }, {
            src: 'https://www.baidu.com',
            title: '宋小宝得意小品青春的校园搞笑，回忆校园的那些年',
            img: 'img/img.jpg',
            time: "2018-07-09",
            column: '百姓大舞台',
            play: '293'
        }]
    };

    var html = template('movieHtml', movielist);
    $('#list').html(html);
}
