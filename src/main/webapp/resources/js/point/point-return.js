let validUserName = ""; //이름
let account = ""; // 계좌
let bankName = ""; // 은행이름
let bankCode = ""; //은행코드
function checkAccount(){
	let inputAccount = $("#account").val();
	let inputBankCode = $("select[name=bankName]").val();
	let birth = $("#birth").val();
	
	fetch("/point/checkAccount?accountNum="+ inputAccount + "&bankCodeStd=" + inputBankCode + "&accountHolderInfo=" + birth)
	.then(response =>{
		console.dir(response);
		if(response.ok){
			return response.text();
		}else{
			throw new Error(response.status);
		}
	})
	.then(text => {
		console.dir(text);
		if(text == '실패'){
			console.dir(text);
		}else{
			let texts = text.split(",");
			console.dir(texts[1]);
			console.dir(memberName);
			if(texts[1] == memberName ){
				alert("계좌실명인증이 완료되었습니다.");
				validUserName = texts[1];
				account = texts[0];
				bankName = texts[2];
				bankCode = texts[3];
			} else {
				alert("회원가입 시 입력한 이름과 계좌 소유주 이름이 다른 경우 어떻게 할까요?????");
			}
			
		}
	})
	.catch(error => {
		console.dir(error);
	});
}

document.querySelector(".request").addEventListener("click", function(e){
	if(validUserName != memberName){
		alert("계좌조회 후 신청가능합니다./1");
		e.preventDefault();
		return;
	}
	if(account != $("#account").val()){
		alert("계좌조회 후 신청가능합니다./2");
		e.preventDefault();
		return;
	}
	if(bankCode != $("select[name=bankName]").val()){
		alert("계좌조회 후 신청가능합니다./3");
		e.preventDefault();
		return;
	}
	
	if(availableBal < 0 || availableBal < $("#refundPoint").val()){
		alert("가용포인트보다 큰 금액은 입력하실 수 없습니다./4");
		e.preventDefault();
		return;
	}
	
	if($("#refundPoint").val() == null || $("#refundPoint").val() == "" || $("#refundPoint").val() == 0){
		alert("금액을 입력해 주세요./5");
		e.preventDefault();
		return;
	}
	
	$("#refundInfo").submit();
});