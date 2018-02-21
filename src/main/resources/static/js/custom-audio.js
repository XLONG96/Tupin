$(function(){
    var song = [

        {
            'cover' : 'music/cover1.jpg',
            'src' : 'music/劲舞团 - Heyoh.mp3',
            'title' : '劲舞团 - Heyoh'
        }

    ];

    var audioFn = audioPlay({
        song : song,
        autoPlay : false  //是否立即播放第一首，autoPlay为true且song为空，会alert文本提示并退出
    });

    /* 向歌单中添加新曲目，第二个参数true为新增后立即播放该曲目，false则不播放
    audioFn.newSong({
        'cover' : 'images/cover4.jpg',
        'src' : 'http://so1.111ttt.com:8282/2016/5/06m/06/199061931237.m4a?tflag=1494769315&pin=a0b26b2dddd976d74724841f6d2641d6&ip=114.233.172.33#.mp3',
        'title' : '极乐净土 - GARNiDELiA'
    },false);
    */

    /* 暂停播放 */
    //audioFn.stopAudio();

    /* 开启播放 */
    //audioFn.playAudio();

    /* 选择歌单中索引为3的曲目(索引是从0开始的)，第二个参数true立即播放该曲目，false则不播放 */
    //audioFn.selectMenu(3,true);

    /* 查看歌单中的曲目 */
    //console.log(audioFn.song);

    /* 当前播放曲目的对象 */
    //console.log(audioFn.audio);
});