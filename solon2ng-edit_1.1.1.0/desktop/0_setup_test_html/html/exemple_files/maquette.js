function activateInput(inputName){
	$('#'+inputName).prev('p').hide();
	$('#'+inputName).show();
	$('#'+inputName).focus();
}

function desactivateInput(inputName){
	
	var filename = $('#'+inputName).val();
	var splitStr = filename.split('.');
	var name = "";
	for(var i=0 ; i < splitStr.length-1 ; i++){
		name+= splitStr[i]+'.';
	}
	name=name.substring(0,name.length-1);
	var extension = "."+splitStr[splitStr.length-1];
	
	var $paragraphe = $('#'+inputName).prev('p');
	var $span = $paragraphe.find('.file-name__text');
	var $innerSpan = $span.find('.file-name__extension');
	$innerSpan.text(extension)
	$span.text(name);
	$span.append($innerSpan);
	$('#'+inputName+'Link').attr("href","solonng:1232344:"+name+extension);
	$('#'+inputName).hide();
	
	
	$paragraphe.show();
}