
function replyList(){
		$.ajax({
		url:"/board/reply-list",
		type:"post",
		 dataType: "json",
		contentType : "application/json",
		data: JSON.stringify(reply),
		 beforeSend: function (xhr) {
            xhr.setRequestHeader(header,token);
        },
		success:(data) => {
			console.dir(data);
	$('#commentList').append(result);
		},
		error:(error) => {
			 console.log(error)
		}
	});
	return false;
}

function uploadConfirm(cmIdx){
	if(confirm("댓글을 입력하시겠습니까?")){
		commentCreate(cmIdx);
	}else{
		return;
	}
}
//댓글 입력
function commentCreate(data){
	var content = $("#commentContent").val();
	
	if(content==""){
		alert("댓글을 입력해주세요.");
		$("#commentContent").val("");
		$("#commentContent").focus();
		return false;
	}
	
	var reply = {
			"bdIdx" :$(".bdIdx").val(),
			"userId" :userId,
			"content" : $("#commentContent").val()
	}
	console.log($(".bdIdx").val())
	$.ajax({
		url:"/board/upload-reply",
		type:"post",
		 dataType: "json",
		contentType : "application/json",
		data: JSON.stringify(reply),
		success:(text) => {
			alert("댓글이 입력되었습니다.");
			console.dir(text);
			console.dir(document.getElementById(cmIdx));
			document.getElementById(cmIdx).style.visibility = 'visible';
			

		},
		
		error:(error) => {
			 console.log(error)
			 location.reload();

		}
	});
 refreshReply();
}

function refreshReply(){
		location.reload();
	}

//댓글 수정
function updateComment(){
	var content = $("#commentContent").val();
	
	var reply = {
			"bdIdx" :$(".bdIdx").val(),
			"userId" :userId,
			"content" : $("#commentContent").val()
	}

	$.ajax({
		url:"/board/modify-reply",
		type:"post",
		 dataType: "json",
		contentType : "application/json",
		data: JSON.stringify(reply),
		 beforeSend: function (xhr) {
            xhr.setRequestHeader(header,token);
        },
		success:(data) => {
			console.dir(data);

		},
		error:(error) => {
			 console.log(error)
		}
	});
	return false;
}


function deleteConfirm(idx){
	if(confirm("댓글을 삭제하시겠습니까?")){
		commentDelete(idx);
	}else{
		return;
	}
}

// 댓글 삭제 
function commentDelete(idx){
	console.log(idx)
	var cmIdx  = $(".cmIdx").val();
	console.log(cmIdx)
	console.log(typeof cmIdx)
	console.log(userId)
	$.ajax({
		url:"/board/delete-reply?cmIdx="+cmIdx,
		type:"post",
		contentType : "application/json",
		 beforeSend: function (xhr) {
            xhr.setRequestHeader(header,token);
        },
		success:(data) => {
			console.dir(document.getElementById(idx));
			document.getElementById(idx).style.display = 'none';
			alert("댓글이 삭제되었습니다.");
			
		},
		error:(error) => {
			 console.log(error)
		}
	});
	return false;
}

function login(){
	if(confirm("로그인 하시겠습니까?")){
		location.href="/member/login";	
	}else{
		return;
	}
	return false;
	
}

// 댓글 업데이트 폼 요청
function replyUpdateForm(id){
	if("${principal.id}" == ""){
		login();
		return;
	}
	var dropdownForm = $("#dropdownForm-"+id);
	var replyForm = $("#replyForm-"+id);
	var updateForm = $("#updateForm-"+id);
	var replyToReplyForm = $("#replyToReplyForm-"+id);
	
	$("#replyContent-"+id).val($("#reply-"+id).text());
	replyForm.hide();
	dropdownForm.hide();
	replyToReplyForm.hide();
	updateForm.show();
	$("#replyContent-"+id).focus();
}
