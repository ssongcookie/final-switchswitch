/**
 * 
 */
 (()=>{
	  let cofirmPw = "";
	  let cofirmNick = document.querySelector('#memberNick').value;

	  document.querySelector('#btnPwCheck').addEventListener('click', function(){
		  
		   let password = document.querySelector('#memberPass').value;
		   
		   if(!password){
			   document.querySelector('#passwordCheck').innerHTML = '비밀번호를 입력하지 않았습니다';
			   return;
		   }
		   
		   fetch("/mypage/pw-check?password="+encodeURIComponent(password))
		   .then(response =>{
				if(response.ok){
					return response.text()
				}else{
					throw new Error(response.status);
				}
			})
		   .then(text => {
			   if(text == 'available'){
				   cofirmPw = password;
				   document.querySelector('#passwordCheck').innerHTML = '비밀번호가 일치합니다';
			   }else{
				   document.querySelector('#passwordCheck').innerHTML = '비밀번호가 일치하지 않습니다';
			   }
		   })
			.catch(error=>{
				 document.querySelector('#passwordCheck').innerHTML ='응답에 실패했습니다  상태코드 : '+error;
			})
	   });

	document.querySelector('#btnNickCheck').addEventListener('click', function(){
		  
		   let nickName = document.querySelector('#memberNick').value;

		   if(!nickName){
			   document.querySelector('#nickNameCheck').innerHTML = '닉네임을 입력하지 않았습니다';
			   return;
		   }
		   
		   if(nickName == cofirmNick){
			   document.querySelector('#nickNameCheck').innerHTML = '원래 닉네임 입니다';
			   return;
		   }
		 

		   fetch("/mypage/nick-check?nickName="+nickName)
		   .then(response =>{
				if(response.ok){
					return response.text()
				}else{
					throw new Error(response.status);
				}
			})
		   .then(text => {
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
	   });
	   
	   document.querySelector('#frm_modify').addEventListener('submit',e=>{
		   let password = document.querySelector('#memberPass').value;
		   let nickName = document.querySelector('#memberNick').value;
			
		   
			if(cofirmNick != nickName){
			   alert('닉네임 중복 검사를 하지 않았습니다');
			   document.querySelector('#memberNick').focus();
			   e.preventDefault();
		   }

		   if(cofirmPw != password){
			   alert('비밀번호 일치 검사를 하지 않습니다');
			   document.querySelector('#memberPass').focus();
			   e.preventDefault();
		   }
		   
		   if(!password){
			   alert('현재비밀번호를 입력하지 않았습니다.');
			   document.querySelector('#memberPass').focus();
			   e.preventDefault();
		   }

	   })
  })();