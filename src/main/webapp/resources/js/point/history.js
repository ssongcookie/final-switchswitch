var token = "bd5667d9-4a6f-41c9-b7ba-67ad50ff8479"
var header = "X-CSRF-TOKEN"


function createPointHistory(value,index) {
    var result = '';
	
    result +=  '<tr><td>'+(index+1)+'</td>';
    result +=  '<td>'+value.regDate+'</td>';
    result +=  '<td> 포인트'+value.type+'</td>';
    result +=  '<td>'+value.points+'</td>';
    if(value.type == '충전'){
		var resultPoint = value.resultPoint+value.points;
		 result +=  '<td>'+ resultPoint +'</td>';
	}
	if(value.type == '사용'){
		var resultPoint = value.resultPoint - value.points;
		 result +=  '<td>'+ resultPoint +'</td>';
	}
    return result;
}



$(() =>{
   pointHistory()
})

function pointHistory(){
	 $.ajax({
	        type: 'get',
	        url: "/point/total-point-history",
	        dataType: "json",
	        async: false,
	
	        success: (data) => {
	
	            var result = '';
	
	            $.each(data, (index, value) => {
	                result += createPointHistory(value,index)
	            })
	            $('.poinHistoryList').append(result);
	        },
	        error: (err) => {
	            console.log(err)
	        }
	
	    })
}

$("#pointCharge").on("click",function search() {
    
     $('.poinHistoryList *').remove();
	
   $.ajax({
	        type: 'get',
	        url: "/point/charge-point-history",
	        dataType: "json",
	        async: false,
	
	        success: (data) => {
	
	            var result = '';
	
	            $.each(data, (index, value) => {
	                result += createPointHistory(value,index)
	            })
	
	            $('.poinHistoryList').append(result);
	        },
	
	        error: (err) => {
	            console.log(err)
	        }
	
	    })
})


$("#pointUse").on("click",function search() {
    
     $('.poinHistoryList *').remove();
	
   $.ajax({
	        type: 'get',
	        url: "/point/use-point-history",
	        dataType: "json",
	        async: false,
	
	        success: (data) => {
	
	            var result = '';
	
	            $.each(data, (index, value) => {
	                result += createPointHistory(value,index)
	            })
	
	            $('.poinHistoryList').append(result);
	        },
	
	        error: (err) => {
	            console.log(err)
	        }
	
	    })
})


$("#pointRefund").on("click",function search() {
    
     $('.poinHistoryList *').remove();
	
   $.ajax({
	        type: 'get',
	        url: "/point/refund-point-history",
	        dataType: "json",
	        async: false,
	
	        success: (data) => {
	
	            var result = '';
	
	            $.each(data, (index, value) => {
	                result += createPointHistory(value,index)
	            })
	
	            $('.poinHistoryList').append(result);
	        },
	
	        error: (err) => {
	            console.log(err)
	        }
	
	    })
})






