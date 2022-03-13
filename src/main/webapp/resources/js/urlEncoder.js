/**
 * 
 */
 
 let urlEncoder = paramObj =>{
	let arr = [];
	
	for(key in paramObj){
		let param = key + '=' + encodeURIComponent(paramObj[key]);
		arr.push(param);
	}
	
	//[a=b, 가=나,t=q]
	return arr.join('&'); //a=b&가=나&t=q
}