 // submenu jquery
 $('.myPage').hover(() => {
    $('.sub-menu').removeClass("hide");
})

$('.myPage').siblings().hover(() => {
    $('.sub-menu').addClass("hide");
})

setTimeout(() => {
    $('.sub-menu').mouseleave(()=> {
        $('.sub-menu').addClass("hide");
    })
}, 5000);