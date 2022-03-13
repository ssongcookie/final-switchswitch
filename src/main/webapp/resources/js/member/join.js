const myInit = {
	method: "GET",
	headers: getCsrfHeader()
};
let confirmEmail = '';
$('#btnEmailCheck').click(()=>{
	   
	let email = $('#email').val();
	   
	if(!email){
		$('#emailCheck').html('이메일을 입력하지 않았습니다.');
		$('#emailCheck').css('color','red');
		return;
	}
	   
	fetch("/member/email-check?memberEmail=" + email, myInit)
	.then(response =>{
		if(response.ok){
			return response.text()
		}else{
			throw new Error(response.status);
		}
	})
	.then(text => {
		if(text == 'available'){
			confirmEmail = email;
			$('#emailCheck').html('사용 가능한 이메일 입니다.');
			$('#emailCheck').css('color','blue');
		}else{
			$('#emailCheck').html('사용 불가능한 이메일 입니다.');
			$('#emailCheck').css('color','red');
		}
	})
	.catch(error => {
		$('#emailCheck').html('응답에 실패했습니다. 상태코드 : ' + error);
		$('#nickCheck').css('color','red');
	})
});
   
let confirmNickname = '';
$('#btnNickCheck').click(()=>{
	   
	let nickname = $('#nickname').val();
	   
	if(!nickname){
		$('#nickCheck').html('닉네임을 입력하지 않았습니다.');
		$('#nickCheck').css('color','red');
		return;
	}
	   
	fetch("/member/nickname-check?memberNick=" + nickname, myInit)
	.then(response =>{
		if(response.ok){
			return response.text()
		}else{
			throw new Error(response.status);
		}
	})
	.then(text => {
		if(text == 'available'){
			confirmNickname = nickname;
			$('#nickCheck').html('사용 가능한 닉네임 입니다.');
			$('#nickCheck').css('color','blue');
		}else{
			$('#nickCheck').html('사용 불가능한 닉네임 입니다.');
			$('#nickCheck').css('color','red');
		}
	})
	.catch(error => {
		$('#nickCheck').html('응답에 실패했습니다. 상태코드 : ' + error);
		$('#nickCheck').css('color','red');
	})
});
 
//조소api
var pop;
	
$("#searchAddr").click(function(){
	//경로는 시스템에 맞게 수정하여 사용
	//호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
	pop = window.open("/member/addrPopup","pop","width=570,height=420, scrollbars=yes, resizable=yes"); 
})
var jusoCallBack = function(zipNo, roadFullAddr){
	$('#zipNo').val(zipNo);
	$('#address').val(roadFullAddr);
} 

$('#frm_join').submit(e=>{
	let email = $('#email').val();
	let nickname = $('#nickname').val();
	let password = $('#password').val();
	let passwordCheck = $('#passwordCheck').val();
	
	if(confirmEmail != email){
		$('#emailCheck').html('이메일 중복 검사를 하지 않았습니다.');
		$('#emailCheck').css('color','red');
		$('#email').focus();
		e.preventDefault();
	}
		
	if(password != passwordCheck){
		$('#passwordNotMatched').html('입력하신 비밀번호와 일치하지 않습니다.');
		$('#passwordNotMatched').css('color','red');
		$('#passwordNotMatched').focus();
		e.preventDefault();
	}
		
	if(confirmNickname != nickname){
		$('#nickCheck').html('닉네임 중복 검사를 하지 않았습니다.');
		$('#nickCheck').css('color','red');
		$('#nickname').focus();
		e.preventDefault();
	}
})
   
$('#btn_cancel').click(()=>{
   	history.back();
})