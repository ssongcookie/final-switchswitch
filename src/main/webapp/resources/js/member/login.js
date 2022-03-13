function kakaoLogin(){
	location.href = "https://kauth.kakao.com/oauth/authorize?"
					+ "client_id=a66e69c27f0b16ebbfae461ad8678ee0"
					+ "&redirect_uri=" + encodeURIComponent('http://toy-khj1220.ga/member/kakaoLogin')
					+ "&response_type=code";
}


if(expired == 'expired') alert("다른 브라우저에서 로그인 중 있습니다.");

if($("#kakao").val() == 'valid') {
	$(".memberEmail").val(email);
	$(".password").val(pw);
	$(".login").submit();
};
