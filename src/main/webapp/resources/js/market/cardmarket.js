var token = "bd5667d9-4a6f-41c9-b7ba-67ad50ff8479"
var header = "X-CSRF-TOKEN"
let userId = $('.id-Check').val()

function slickControll() {
    $('.detail-img').slick({
        autoplay: true,
        infinite: true,
        dots: false,
        arrows: false,
    });
}

function myCard(value) {
		
		if(value.memberNick == userId) {
			alert("내카드입니다.")
			stopPropagation()
			return false;
		}
}

function createCard(value,productStar,cardShape ,icon ,createMemberStar) {
    var result = '';
	
	result += '<div class="card-container" id='+value.cardIdx+' onclick="updateView(this,'+value.cardIdx+','+value.memberIdx+','+value.views+')">'
    result += '<input type="hidden" id="cardIdx" name="cardIdx" value="' +
        value.cardIdx + '">'
    result += '<div class="card-name" id='+value.cardIdx+'>'
    result += '<p id='+value.cardIdx+'>' + value.name + '</p>'
    result += '</div>'
    result += '<div class="grade-container" id='+value.cardIdx+'>'
    result += '<div class="icon" id='+value.cardIdx+'>'
    result += '<i class="'+icon+'" id='+value.cardIdx+'></i>'
    result += '</div>'
    result += '<div class="user-grade" id='+value.cardIdx+'>'
    result += '<p id='+value.cardIdx+'>사용자 평점</p>'
    result += '<div class="star" id='+value.cardIdx+'>'
    result += createMemberStar
    result += '</div>'
    result += '</div>'
    result += '<div class="item-grade" id='+value.cardIdx+'>'
    result += '<p id='+value.cardIdx+'>물건 평점</p>'
    result += '<div class="star" id='+value.cardIdx+'>'
    result += productStar
    result += '</div>'
    result += '</div>'
    result += '</div>'
    result += '<div class="address" id='+value.cardIdx+'>'
    result += '<p id='+value.cardIdx+'>지역 <span id='+value.cardIdx+'>' + value.region + '</span><span id='+value.cardIdx+'> ' + value
        .regionDetail + '</span></p>'
    result += '</div>'
    result += '<div class="card" id='+value.cardIdx+'>'
    result += '<img src="'+cardShape+'" alt="" id='+value.cardIdx+'>'
    result += '<div class="card-img" id='+value.cardIdx+'>'
    result += '<img src="'+value.imgUrl[0]+'" alt="" id='+value.cardIdx+'>'
    result += '</div>'
    result += '</div>'
    result += '</div>'

    return result
}

function createPopup(value,productStar,memberStar) {
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

$(() =>{
    $.ajax({

        type: 'get',
        url: "/market/getcard",
        dataType: "json",
        async: false,

        success: (data) => {

            var result = '';
            var lastindex = '';

            $.each(data, (index, value) => {
                
                let productStar = createStar(value);

                let cardShape = createShape(value);

                let icon = createIcon(value);
				
                let memberStar = createMemberStar(value);
                
                result += createCard(value,productStar,cardShape,icon,memberStar)
		
                lastindex = index + 1;

            })

            $('.card-list').append(result);
            $('.last-card').text(lastindex);

        },

        error: (err) => {
            console.log(err)
        }

    })

    cardClick()
    cardClose()

})

$('.searchbox').change(function search() {
    let category={"category" : $('select[name="category"]').val()
                ,"region" : $('select[name="location"]').val()
                ,"content" : $('input[name="content"]').val()}
    console.log(category);

    if($('select[name="category"]').val() != '') {
                    $('.sc-category').text($('select[name="category"]').val())
                } else {
                    $('.sc-category').text('전체검색')
                }

    if($('select[name="location"]').val() != '') {
        $('.sc-location').removeClass('hide')
        $('.sc-location').text($('select[name="location"]').val())
    } else {
        $('.sc-location').addClass('hide')
    }

    if($('input[name="content"]').val() != '') {
        $('.sc-input').removeClass('hide')
        $('.sc-input').text($('input[name="content"]').val())
        $('input[name="content"]').val('')
    } else {
        $('.sc-input').addClass('hide')
    }

    $.ajax({

        type: 'post',
        url: "/market/category",
        dataType: "json",
        async: false,
        data: JSON.stringify(category),
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header,token);
        },

        success: (data) => {
            $(".card-list").empty();

            var result = '';
            var lastindex = '';

            $.each(data, (index, value) => {

                let productStar = createStar(value);

                let cardShape = createShape(value);

                let icon = createIcon(value);

                let memberStar = createMemberStar(value);

                result += createCard(value,productStar,cardShape,icon,memberStar)

                lastindex = index + 1

            })

            $('.card-list').append(result);
            console.log(data.length)
            if(data.length == 0) {
                $('.last-card').text('0');
            } else {
                $('.last-card').text(lastindex);
            }

        },

        error: (err) => {
            console.log(err)
        }

    })

    cardClick()
    cardClose()

})


function search() {
    let category={"category" : $('select[name="category"]').val()
                ,"region" : $('select[name="location"]').val()
                ,"content" : $('input[name="content"]').val()}
    console.log(category);

    if($('input[name="content"]').val() != '') {
                    $('.sc-input').removeClass('hide')
                    $('.sc-input').text($('input[name="content"]').val())
                    $('input[name="content"]').val('')
                } else {
                    $('.sc-input').addClass('hide')
                }

    $.ajax({

        type: 'post',
        url: "/market/category",
        dataType: "json",
        async: false,
        data: JSON.stringify(category),
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header,token);
        },

        success: (data) => {
            $(".card-list").empty();

            var result = '';
            var lastindex = '';

            $.each(data, (index, value) => {

                let productStar = createStar(value);

                let cardShape = createShape(value);

                let icon = createIcon(value);

                let memberStar = createMemberStar(value);

                result += createCard(value,productStar,cardShape,icon,memberStar)

                lastindex = index + 1;

            })

            $('.card-list').append(result);
            console.log(data.length)
            if(data.length == 0) {
                $('.last-card').text('0');
            } else {
                $('.last-card').text(lastindex);
            }
           

        },

        error: (err) => {
            console.log(err)
        }

    })

    cardClick()
    cardClose() 
    
}

function cardClose() {
    $('.close-button').click(() => {
        $('.popup-detail').addClass('hide')
    })
}

// popupAjax
function cardClick() {
	
        $('.card-container').on("click",(e) => {
            
            let cardIdx = e.target.id
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
                    xhr.setRequestHeader(header,token);
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
            slickControll()
        })
}
