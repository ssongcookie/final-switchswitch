 var input = document.querySelector('#profileImage');
    var preview = document.querySelector('.preview');

    input.addEventListener('change', updateImageDisplay);

    function updateImageDisplay() {
    	  while(preview.firstChild) {
    	    preview.removeChild(preview.firstChild);
    	  }

    	  const curFiles = input.files;
    	  if(curFiles.length === 0) {
    	    const para = document.createElement('p');
    	    para.textContent = 'No files currently selected for upload';
    	    preview.appendChild(para);
    	  } else {
    	    for(const file of curFiles) {
    	      const para = document.createElement('p');
    	      if(validFileType(file)) {
    	        const image = document.querySelector('.profile-image');
    	       
    	        image.src = URL.createObjectURL(file);

    	      } else {
    	        para.textContent = `File name ${file.name}: Not a valid file type. Update your selection.`;
    	        listItem.appendChild(para);
    	      }
    	    }
    	  }
    }
    	
    const fileTypes = [
    	  "image/gif",
    	  "image/jpeg",
    	  "image/png",
    	  "image/jpg"
    ];

    function validFileType(file) {
    	 return fileTypes.includes(file.type);
    }
    	
    function returnFileSize(number) {
    	if(number < 1024) {
    		return number + 'bytes';
    	} else if(number >= 1024 && number < 1048576) {
    		return (number/1024).toFixed(1) + 'KB';
    	} else if(number >= 1048576) {
    		return (number/1048576).toFixed(1) + 'MB';
    	}
    }
    
    let fileCheck = (e) => {
        
    	console.dir(e);
    	let file = e[0].name.split('.');
   		let filetype = file[file.length-1].toLowerCase();
   		if(filetype == 'jpg' || filetype == 'gif' || filetype == 'jpeg' || filetype=='png'){
   			document.querySelector(".rejectMsg").innerHTML = '';
   		} else{
   			document.querySelector(".rejectMsg").innerHTML = '<i class="fas fa-times-circle"></i>JPG/GIF/PNG 파일만 업로드 가능합니다.';
   			let files = document.getElementsByName('profileImage');
   			files[0].value='';
   			return false;
   		}
	
    }
    
   
    
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