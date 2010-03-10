package org.hydra.messages.handlers.intefaces;

import org.hydra.messages.interfaces.IMessage;
import org.hydra.processors.interfaces.IProcessor;

public interface IMessageHandler {
	void applyMessage(IMessage inMessage, IProcessor inProcessor);
}
