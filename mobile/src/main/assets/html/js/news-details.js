$(function () {
    var search = '宋小宝';
    var content = {
        title: '农民工朋友，请带好这份“求职防骗指南”',
        time: '2018-03-03',
        img: 'img/img.jpg',
        details: '春节过后，农民工返城务工进入高峰期。针对农民工在找工作、订立劳动合同过程中容易遇到的侵权隐患，江苏省镇江市总工会发布今年第1期维权预警信息，希望农民工朋友们在返城路上带上这份“防骗指南”，避免求职路上遭受损失。'
    };

    var html = template('contentHtml', content);
    $('#content').html(html);


});