


function export2excel(){
	  var exceldownloadUrl =  "/downloadExcel";
	  $.ajax({
    			url:exceldownloadUrl,
		   		success:function(){
		   			console.log("Download successful");
		   		}
		   	});
	  	
}