function transfer(e) {
	let appendArea;
	if(e.parentElement.className == 'mycard-list'){
		if(document.querySelector("#appendArea").childElementCount > 4){
			return;
		}
		appendArea = document.querySelector("#appendArea");
		appendArea.appendChild(e);
	} else {
		appendArea = document.querySelector(".mycard-list");
		appendArea.appendChild(e);
	}
}

