var token = "bd5667d9-4a6f-41c9-b7ba-67ad50ff8479"
var header = "X-CSRF-TOKEN"


function createExchangeHistory(value) {
    var result = '';
   	var memberIdx= $('#memberIdx').val();
	
    result +=  '<tr><td>'+value.eh.exchangeDate+'</td>'
    if(memberIdx == value.eh.requestedMemIdx){
		 result += '<td>'+value.requestedCardName+'</td>'
		 result += '<td>'+value.requestCardName+'</td>'
	}
	if(memberIdx == value.eh.requestMemIdx){
		 result += '<td>'+value.requestCardName+'</td>'
		 result += '<td>'+value.requestedCardName+'</td>'
	}
    result += '<td>'+value.crl.propBalance+'</td>'
    result += '<td>'+value.opponentNickList+'</td>'
    if(value.isRate == 0){
		result += ' <td><i class="far fa-star" style="color:red;"></i></td></tr>'
	}else{
		result += ' <td><i class="far fa-star" style="color:green;"></i></td></tr>'
	}
    return result
}

function createFreeHistory(value) {
    var result = '';
    var memberIdx= $('#memberIdx').val();

    result +=  '<tr><td>'+value.eh.exchangeDate+'</td>'
    if(memberIdx == value.eh.requestedMemIdx){
		 result += '<td>'+value.requestedCardName+'</td>'
		  result += '<td> X </td>'
	}
   if(memberIdx == value.eh.requestMemIdx){
		 result += '<td> X </td>'
		 result += '<td>'+value.requestedCardName+'</td>'
	}

    result += '<td> 0 </td>'
    result += '<td>'+value.opponentNickList+'</td>'
    if(value.isRate == 0){
		result += '<td><a href="#" class="btn btn-danger btn-circle btn-sm"> <i class="far fa-star"></i> </a></td>/tr>'
	}else{
		result += '<td><a href="#" class="btn btn-success btn-circle btn-sm"> <i class="far fa-star"></i> </a></td></tr>'
	}
    return result
}

$(() =>{
   exchangeHistory()
})

$("#exchange").on('click',function search() {
                
     $('.historyList *').remove();
	 exchangeHistory();
    
})

$("#freeSharing").on('click',function search() {
                
     $('.historyList *').remove();
	
    $.ajax({
        type: 'get',
        url: "/mypage/free-history",
        dataType: "json",
        async: false,

        success: (data) => {

            var result = '';

            $.each(data, (index, value) => {
                result += createFreeHistory(value)
            })

            $('.historyList').append(result);
        },

        error: (err) => {
            console.log(err)
        }

    })

    
})


function exchangeHistory(){
	 $.ajax({
	        type: 'get',
	        url: "/mypage/exchange-history",
	        dataType: "json",
	        async: false,
	
	        success: (data) => {
	
	            var result = '';
	
	            $.each(data, (index, value) => {
	                result += createExchangeHistory(value)
	            })
	            $('.historyList').append(result);
	        },
	        error: (err) => {
	            console.log(err)
	        }
	
	    })
}

 document.querySelectorAll(".card-box").forEach(function(e){
    	switch(e.previousElementSibling.value){
    	case '1' : e.setAttribute("src","../resources/img/ddongCard.png"); break;
    	case '2' : e.setAttribute("src","../resources/img/BrownCard.png"); break;
    	case '3' : e.setAttribute("src","../resources/img/silverCard.png"); break;
    	case '4' : e.setAttribute("src","../resources/img/goldCard.png"); break;
    	case '5' : e.setAttribute("src","../resources/img/diaCard.png"); break;
    	default : console.dir("error!!!");
    	};
    })

    document.querySelectorAll("#icon-val").forEach(function(e){
    	switch(e.previousElementSibling.value){
    	case '전자기기/생활가전' : e.setAttribute("class","fas fa-bolt"); break;
    	case '가구/인테리어' : e.setAttribute("class","fas fa-couch"); break;
    	case '유아동' : e.setAttribute("class","fal fa-baby"); break;
    	case '생활/가공식품' : e.setAttribute("class","fas fa-utensils"); break;
    	case '스포츠/레저' : e.setAttribute("class","fas fa-basketball-ball"); break;
    	case '패션/잡화' : e.setAttribute("class","fas fa-tshirt"); break;
    	case '게임/만화' : e.setAttribute("class","fas fa-gamepad"); break;
    	case '뷰티/미용' : e.setAttribute("class","fas fa-air-freshener"); break;
    	case '반려 동물 용품' : e.setAttribute("class","fas fa-paw"); break;
    	case '도서/티켓/음반' : e.setAttribute("class","fas fa-compact-disc"); break;
    	case '기타' : e.setAttribute("class","fas fa-cogs"); break;
    	default : console.dir("error!!!");
    	};
    })



