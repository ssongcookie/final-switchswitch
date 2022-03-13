<!-- 요청취소 -->
function requestCancel(){
	$("#sendResponse").attr("action", "/exchange/request-cancel/"+reqIdx);
	$("#sendResponse").submit();
}
<!-- 요청수정 -->
function requestRevise(){
	$("#sendResponse").attr("action", "/exchange/revise/"+reqIdx);
	$("#sendResponse").submit();
}
<!-- 요청수락 -->
function requestAccept(){
	$("#sendResponse").attr("action", "/exchange/accept/"+reqIdx);
	console.dir("요청수락 보내기 전");
	$("#sendResponse").submit();
}
<!-- 요청거절 -->
function requestReject(){
	$("#sendResponse").attr("action", "/exchange/reject/"+reqIdx);
	$("#sendResponse").submit();
}
<!-- 교환취소요청 -->
function exchangeCancelRequest(){
	$("#sendResponse")
	.attr("action", 
			"/exchange/cancel-request/"+reqIdx
			+"/"+updateStatus);
	console.dir("요청수락 보내기 후");
	$("#sendResponse").submit();
}
<!-- 교환취소요청수락 -->
function exchangeRequestCancel(){
	$("#sendResponse").attr("action", "/exchange/exchange-cancel/"+reqIdx);
	$("#sendResponse").submit();
}
<!-- 교환취소요청거절 -->
function exchangeRequestCancelReject(){
	$("#sendResponse").attr("action", "/exchange/cancel-request-reject/" +reqIdx+ "/" +previousStatus);
	$("#sendResponse").submit();
}
<!-- 교환취소요청취소 -->
function cancelExchangeRequestCancel(){
	$("#sendResponse").attr("action", "/exchange/cancel-request-cancel/" +reqIdx+ "/" +previousStatus);
	$("#sendResponse").submit();
}

<!-- 교환완료 -->
<!-- 평점요청창 생성 -->
function exchangeComplete(){
	<!-- 사용자평가 모달창 생성 -->
	document.querySelector(".noticePopUp").style.setProperty("visibility","visible");
}

document.querySelector("#notice_close").addEventListener("click", (e)=> {
	noticeInitialize();
});

function noticeInitialize(){
	document.querySelector(".noticePopUp").style.setProperty("visibility","hidden");
}


$("#submit_btn").on("click", function(){
	$(".notice").appendTo("#sendResponse");
	<!-- 제출 -->
	doSubmit();
})

function doSubmit(){
	$("#sendResponse").attr("action", "/exchange/complete/"+reqIdx);
	$("#sendResponse").submit();
}


let loofCnt = 4-document.querySelector(".exc-whish-card-table").childElementCount;
for(let i = 0; i < loofCnt; i++){
	let cardContainer = document.createElement("div");
	let card = document.createElement("div");
	let img = document.createElement("img");
	img.setAttribute("src", "/resources/img/defaultCard.png");
	cardContainer.setAttribute("class", "card-contatiner");
	cardContainer.setAttribute("style", "width:230px;");
	cardContainer.setAttribute("id", "default-card")
	card.setAttribute("class", "card");
	cardContainer.appendChild(card);
	card.appendChild(img);
	document.querySelector("#appendArea").appendChild(cardContainer);
}