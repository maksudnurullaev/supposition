if (MessageHandler == null) var MessageHandler = {};

MessageHandler.applyIncomingMessages = function(messages){
	$each(messages, function(elem, elemIndex){
		if(elem.type.test("message.error","i")){
			alert("ERROR: " + elem.data);			
		}else{
			alert("Unknown type: " + elem.type);
		}
	});
};
