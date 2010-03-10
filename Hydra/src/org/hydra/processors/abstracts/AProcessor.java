package org.hydra.processors.abstracts;

import org.hydra.collectors.StatisticsCollector.StatisticsTypes;
import org.hydra.collectors.abstracts.AStatisticsApplyer;
import org.hydra.collectors.interfaces.ICollector;
import org.hydra.events.PipeEvent;
import org.hydra.executors.interfaces.IExecutor;
import org.hydra.messages.handlers.intefaces.IMessageHandler;
import org.hydra.messages.interfaces.IMessage;
import org.hydra.pipes.exceptions.RichedMaxCapacityException;
import org.hydra.pipes.interfaces.IPipe;
import org.hydra.processors.exceptions.NullPipeException;
import org.hydra.processors.interfaces.IProcessor;
import org.hydra.utils.Constants;

public abstract class AProcessor extends AStatisticsApplyer implements IProcessor {

	// Default values
	private IPipe<IMessage> _logPipe = null;
	private IPipe<IMessage> _inPipe = null;
	private IPipe<IMessage> _outPipe = null;
	private String _name = null;
	private ProcessorStatus _myState = ProcessorStatus.WAITING;
	private IExecutor _executor = null;

	private ICollector _messagesCollector = null;

	@Override
	public ICollector getMessageCollector() {
		return _messagesCollector;
	}

	@Override
	public void setMessageCollector(ICollector inCollector) {
		_messagesCollector = inCollector;
		getLog().debug(String.format("%s set message collector to %s", getName(), inCollector
				.getName()));

	}

	public boolean isClassExist(String inPathToPackageAndClass) {
		getLog().debug("Trying to find class: " + inPathToPackageAndClass);
		
		try {//TODO We should find more optimized method to check, without raising ANY exception
			Class.forName(inPathToPackageAndClass,false,this.getClass().getClassLoader());
			return true;
		} catch (Exception e) {
			getLog().error(e.toString());
			return false;
		}
	}
	
	public boolean isMessageHandlerExist(String inMesssgeClassName) {
		return isClassExist(Constants.getDefaultDispatcherName(inMesssgeClassName));
	}	

	public IMessageHandler getMessageDispatcher(IMessage inMessage) {
		
		String className = getMessageClassSimpleName(inMessage);

		Object o = null;

		try {
			getLog().debug(String.format(
					"Default handler for class(%s) is '%s'", className,
					Constants.getDefaultDispatcherName(className)));
				
			o = Class.forName(Constants.getDefaultDispatcherName(className)).newInstance();
			
		} catch (ClassNotFoundException e) {
			getLog().error(e.getMessage());
			return null;
		} catch (Exception e) {
			getLog().error(e.getMessage());
			return null;
		}

		if (o instanceof IMessageHandler)
			return (IMessageHandler) o;
		else {
			getLog().error(String.format(
					"Object(%s) should has IMessageHandler interface", o.toString()));
		}
		return null;
	}

	private String getMessageClassSimpleName(IMessage inMessage) {
		Class<?> c = inMessage.getClass();
		String className = c.getSimpleName();
		return className;
	}

	public void setInPipe(IPipe<IMessage> inPipe) {
		this._inPipe = inPipe;
		getLog().debug(String.format("InPipe(%s) defined for processor(%s)", inPipe.getName(), getName()));		
	}

	public IPipe<IMessage> getInPipe() {
		return _inPipe;
	}

	@Override
	public void setOutPipe(IPipe<IMessage> inPipe) {
		this._outPipe = inPipe;
	}

	public IPipe<IMessage> getOutpipe() {
		return _outPipe;
	}

	public boolean isValidDefaultInPipe() {
		return (_inPipe != null);
	}

	public boolean isValidDefaultOutPipe() {
		return (_outPipe != null);
	}

	public void setName(String inName) {
		this._name = inName;
		getLog().debug(String.format("Processor set name to '%s'", inName));
	}

	public String getName() {
		return _name;
	}

	public void setLogPipe(IPipe<IMessage> _logPipe) {
		this._logPipe = _logPipe;
	}

	public IPipe<IMessage> getLogPipe() {
		return _logPipe;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.supposition.events.IPipeEventListener#pipeHasMessage(org.supposition
	 * .events.PipeEvent)
	 */
	@Override
	public void eventHandleIncomingMessage(PipeEvent evt) {
		getLog().debug(String.format("Processor(%s) has new PipeEvent", getName()));
		if (getState() == ProcessorStatus.WAITING) {
			if (isValidExecutor()) {
				_executor.execute(this);
				getLog().debug(String.format(
						"Processor(%s) submited by handle new messages from Pipe's event",
						getName()));
			} else {
				getLog().fatal("Executor is NULL");
				System.exit(1);
			}
		}
	}

	private boolean isValidExecutor() {
		return (_executor != null);
	}

	public IExecutor getExecutor() {
		return _executor;
	}

	public void setExecutor(IExecutor inExecutor) {
		_executor = inExecutor;
	}

	/**
	 * @return the myState
	 */
	public synchronized ProcessorStatus getState() {
		return _myState;
	}

	/**
	 * @param myState
	 *            the myState to set
	 */
	public synchronized void setState(ProcessorStatus myState) {
		_myState = myState;
	}

	public boolean isProxyMessage(IMessage inMessage) {
		return inMessage.isProxy();
	}

	public void sendToOutPipe(IMessage message) {
		if (isValidDefaultOutPipe()) {
			try {
				getOutpipe().setMessage(message);
			} catch (RichedMaxCapacityException e) {
				getLog().error(String.format(
						"Pipe(%s) riched maximal capacity(%d)", getOutpipe()
								.getName(), getOutpipe().getMaxCapacity()));
			}
		}else{
			getLog().debug("Invalid OutPipe for: " + getName());
		}
	}

	@Override
	public void run() {
		// Start
		// **** Change thread state
		setState(ProcessorStatus.WORKING);
		// ****
		
		// 1. Main Validations
		// 1.1 Incoming Pipe
		if (getInPipe() == null)
			try {
				throw new NullPipeException();
			} catch (NullPipeException e) {
				e.printStackTrace();
			}

		IMessage message = null;
		getLog().debug(String.format("Start to handle pipe(%s) message(s)",
				getInPipe().getName()));
		while ((message = (IMessage) getInPipe().getMessage()) != null) {
			// Log message
			getLog().debug(String.format(
					"Processor(%s) handle message for group(%s)", getName(),
					message.getGroupID()));

			if (isExpectedMessage(message)) {
				applyMessage(message);
			} else
				sendToOutPipe(message);

		}
		// Stop
		// **** Change thread state
		setState(ProcessorStatus.WAITING);
		// ****
	}

	public void applyMessage(IMessage inMessage){
		getLog().debug(String.format("Handle new message for group(%s)...",inMessage.getGroupID()));

		IMessageHandler dispatcher = null;		
		
		if(isMessageHandlerExist(getMessageClassSimpleName(inMessage))){
			dispatcher = getMessageDispatcher(inMessage);
		}else{
			getLog().error(String.format("Could not find handler for message class(%s)", getMessageClassSimpleName(inMessage)));
			setStatistics(getName(), StatisticsTypes.WITH_ERRORS);
			return ;
		}

		if (dispatcher != null){
			dispatcher.applyMessage(inMessage, this);
			setStatistics(getName(), StatisticsTypes.ACCEPTED);
		}

		getLog().debug(String.format("Apply new message for groupID(%s)...DONE", inMessage.getGroupID()));
	};

	@Override
	public boolean isExpectedMessage(IMessage message) {
		return true;
	}
}
