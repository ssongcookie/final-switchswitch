/**
 * 
 */
 
 const profileImg = document.querySelector('#memberImg');
 (()=>{
	profileImg.addEventListener('click',()=>{
		const flIdx = document.querySelector('#fileNo').value;
		if(flIdx){
				fetch("/admin/profile-img-delete?flIdx="+flIdx)
				.then(response =>{
				if(response.ok){
					return response.text()
				}else{
					throw new Error(response.status);
				}
			})
			.then(text =>{
				if(text == 'success'){
					alert("삭제되었습니다.");
				}else{
					alert("삭제할 이미지가 없습니다.");
				}
			})
			.catch(error=>{
				document.querySelector('#imgConfirm').innerHTML = '응답에 실패했습니다 상태코드 : '+error;
			})
		}
		 
	})
})