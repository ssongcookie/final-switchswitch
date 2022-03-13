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
	var answerIdx  = $(".answerIdx").val();
	console.log(answerIdx)
	console.log(typeof answerIdx)
	console.log(userId)
	$.ajax({
		url:"http://localhost:9090/inquiry/delete-answer?answerIdx="+answerIdx,
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
