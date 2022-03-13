let userId = $('.id-Check').val()

function slickControl() {
    $('.detail-img').slick({
        autoplay: true,
        infinite: true,
        dots: false,
        arrows: false,
    });
}

function createPopup(value,productStar,memberStar) {

    let icon = createIcon(value);
    
	let imgUrls = ''
	
	myCard(value);
	
    
    for (let index = 0; index < value.imgUrl.length; index++) {
       imgUrls += '<div class="imgs"><img src="'+value.imgUrl[index]+'" alt=""></div>'
    } 
	
    let result = ''

    result += '<div class="detail-img">'
    result += imgUrls
    result += '</div>'
    result += '<div class="detail-info">'
    result += '<div class="profile">'
    result += '<div class="profile-img">'
    result += '<div class="inner-img">'
    result += '<img src="../resources/img/default-user.png" alt="">'
    result += '</div>'
    result += '</div>'
    result += '<div class="profile-name">'
    result += '<p>' + value.memberNick + '</p>'
    result += '</div>'
    result += '</div>'
    result += '<div class="detail-content">'
    result += '<div class="detail-title">'
    result += '<h1>' + value.name + '</h1>'
    result += '</div>'
    result += '<div class="detail-time">'
    result += '<p>' + value.dateParse + '</p>'
    result += '</div>'
    result += '<div class="post">'
    result += '<p>' + value.content + '</p>'
    result += '</div>'
    result += '<div class="detail-list">'
    result += '<ul>'
    result += '<li><span class="list-title">지역:</span> <strong>' + value.region + '</strong> <strong>' + value.regionDetail + '</strong></li>'
    result += '<li><span class="list-title">배송:</span> ' + value.method + '</li>'
    result += '<li><span class="list-title">거래방식:</span> ' + value.method + '</li>'
    result += '<li><span class="list-title">희망물품카테고리:</span> ' + value.hopeKind + '</li>'
    result += '<li><span class="list-title">물건별점:</span> ' + productStar + '</li>'
    result += '<li><span class="list-title">사용자별점:</span> ' + memberStar + '</li>'
    result += '</ul>'
    result += '</div>'
    result += '<div class="deail-buttons">'
    result += '<form action="/exchange/exchangeForm/'+value.cardIdx+'">'
    result += '<button class="exchange-button">교환신청</button>'
    result += '<button type="button" class="close-button">닫기</button>'
    result += '</form>'
    result += '</div>'
    result += '</div>'
    result += '</div>'

    return result
}

function createStar(value) {

    let productStar = '';

    if(value.condition >= 1 ){
        for (let index = 0; index < value.condition; index++) {
            productStar += '<i class="fas fa-star full-star" id='+value.cardIdx+'></i>'
        }
    }
    
    if (5-value.condition > 0) {
        for (let index = 0; index < (5-value.condition); index++) {
            productStar += '<i class="far fa-star" id='+value.cardIdx+'></i>'
        }
    }

    return productStar
}

function createShape(value) {
    
    let cardShape = '';
    switch(value.condition) {
        case 1: cardShape += '../resources/img/ddongCard.png'
        break;
        case 2: cardShape += '../resources/img/BrownCard.png'
        break;
        case 3: cardShape += '../resources/img/silverCard.png'
        break;
        case 4: cardShape += '../resources/img/goldCard.png'
        break;
        case 5: cardShape += '../resources/img/diaCard.png'
        break;
    }

    return cardShape
}

function createIcon(value) {
    let icon = ''
    switch(value.category) {
        case '전자기기/생활가전': icon += 'fas fa-bolt'
        break;
        case '가구/인테리어': icon += 'fas fa-couch'
        break;
        case '유아동': icon += 'fal fa-baby'
        break;
        case '생활/가공식품': icon += 'fas fa-utensils'
        break;
        case '스포츠/레저': icon += 'fas fa-basketball-ball'
        break;
        case '패션/잡화': icon += 'fas fa-tshirt'
        break;
        case '게임/만화': icon += 'fas fa-gamepad'
        break;
        case '뷰티/미용': icon += 'fas fa-air-freshener'
        break;
        case '반려 동물 용품': icon += 'fas fa-paw'
        break;
        case '도서/티켓/음반': icon += 'fas fa-compact-disc'
        break;
        case '기타': icon += 'fas fa-cogs'
        break;
    }

    return icon
} 

function createMemberStar(value) {

      let memberStar = '';
    if(value.memberRate >= 1){
        for (let index = 0; index < value.memberRate; index++) {
            memberStar += '<i class="fas fa-star full-star" id='+value.cardIdx+'></i>'
        }
    } 
    
    if (value.memberRate%1 != 0) {
            memberStar += '<i class="fas fa-star-half-alt" id='+value.cardIdx+'></i>'
    } 
    
    if (5-value.memberRate > 0) {
        for (let index = 0; index < (5-value.memberRate); index++) {
            memberStar += '<i class="far fa-star" id='+value.cardIdx+'></i>'
        }
    }
    return memberStar
}

function cardClose() {
    $('.close-button').click(() => {
        $('.popup-detail').addClass('hide')
    })
}

// popupAjax
function cardClick(e) {
	
	let cardIdx = e
    console.log(cardIdx);

    let card={"cardIdx" : cardIdx}
    
    $.ajax({

        type: 'post',
        url: "/market/card",
        dataType: "json",
        async: false,
        data: JSON.stringify(card),
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader,csrfToken);
        },

        success: (data) => {
			
			if(data.requestedCardIdx != null) {
					if(data.requestedCardIdx == data.cardIdx) {
						alert("거래중인카드입니다.")
						location.href = "/exchange/detail/"+data.reqIdx
						stopPropagation()
						return false;
					} 
			}

			myCard(data)
			$('.popup-detail').removeClass('hide')
            $(".popup-detail").empty();

            var result = '';
            var lastindex = '';

            let productStar = createStar(data);

            let memberStar = createMemberStar(data);
                
            result = createPopup(data,productStar,memberStar)
            console.log(data)
                
            $('.popup-detail').append(result);  

        },

        error: (err) => {
            console.log(err)
        }

    })

    cardClose() 
    slickControl()
}

function myCard(value) {
		
		if(value.memberNick == userId) {
			alert("내카드입니다.")
			stopPropagation()
			return false;
		}
}