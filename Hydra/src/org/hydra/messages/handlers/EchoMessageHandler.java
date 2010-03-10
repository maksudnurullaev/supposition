package org.hydra.messages.handlers;

import org.hydra.messages.handlers.abstracts.AMessageHandler;
import org.hydra.messages.interfaces.IMessage;
import org.hydra.processors.interfaces.IProcessor;

public class EchoMessageHandler extends AMessageHandler{

	@Override
	public void applyMessage(IMessage inMessage, IProcessor inProcessor) {
		String label = "Message handled with EchoMessageHandler";
		inMessage.setObject(label);
		inProcessor.sendToOutPipe(inMessage);
	}

}
