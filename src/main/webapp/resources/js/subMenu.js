// submenu jquery
$('.myPage').hover(() => {
    $('#mypage-sub').removeClass("hide");
})

$('.myPage').siblings().hover(() => {
    $('#mypage-sub').addClass("hide");
})

$('.board').hover(() => {
    $('#board-sub').removeClass("hide");
})

$('.board').siblings().hover(() => {
    $('#board-sub').addClass("hide");
})

$('.myCard').hover(() => {
    $('#myCard-sub').removeClass("hide");
})

$('.myCard').siblings().hover(() => {
    $('#myCard-sub').addClass("hide");
})

setTimeout(() => {
    $('.sub-menu').mouseleave(()=> {
        $('.sub-menu').addClass("hide");
    })
}, 5000);
// submenu jquery End