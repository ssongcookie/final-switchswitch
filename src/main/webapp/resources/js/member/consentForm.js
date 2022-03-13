$(".next_btn").on("click",function(){
	//필수 동의만 필요한 경우
	if($("input:checked").val() == "consent"){
		$("form").attr(csrfHeader, csrfToken);
		$('<input type="hidden" name="consent" value="consent" />').appendTo("form");
		$("form").submit();
	} else {
		alert("개인정보 제공 동의가 필요합니다.");
		return;
	}
})