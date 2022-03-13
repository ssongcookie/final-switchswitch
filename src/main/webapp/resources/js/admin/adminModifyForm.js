(()=>{
	  let cofirmPw = "";
	  let cofirmNick = document.querySelector('#memberNick').value;
	  
	document.querySelector('#btnNickCheck').addEventListener('click', ()=>{
		  
		   let nickName = document.querySelector('#memberNick').value;
		   if(nickName){
				   fetch("/admin/nick-check?nickName="+nickName)
				   .then(response =>{
						if(response.ok){
							return response.text()
						}else{
							throw new Error(response.status);
						}
					})
				   .then(text => {
					console.dir(text);
					   if(text == 'available'){
						   cofirmNick = nickName;
						   document.querySelector('#nickNameCheck').innerHTML = '사용 가능한 닉네임 입니다';
					   }else{
						   document.querySelector('#nickNameCheck').innerHTML = '사용 불가능한 닉네임 입니다';
					   }
				   })
					.catch(error=>{
						 document.querySelector('#nickNameCheck').innerHTML ='응답에 실패했습니다  상태코드 : '+error;
					})
				}
	   });
	   
	   document.querySelector('#frm_modify').addEventListener('submit',e=>{
		   let nickName = document.querySelector('#memberNick').value;
		   
			if(cofirmNick != nickName && nickName != null){
			   alert('닉네임 중복 검사를 하지 않았습니다');
			   document.querySelector('#memberNick').focus();
			   e.preventDefault();
		   }
	   })
  })();