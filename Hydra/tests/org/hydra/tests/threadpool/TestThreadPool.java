package org.hydra.tests.threadpool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hydra.collectors.StatisticsCollector;
import org.hydra.executors.Executor;
import org.hydra.messages.EchoMessage;
import org.hydra.pipes.Pipe;
import org.hydra.pipes.exceptions.RichedMaxCapacityException;
import org.hydra.processors.Processor;
import org.hydra.processors.exceptions.NullProcessorException;
import org.hydra.processors.interfaces.IProcessor.ProcessorStatus;
import org.junit.Assert;
import org.junit.Test;

public class TestThreadPool {
	Log _log = LogFactory.getLog(this.getClass());

	Processor _processor11, _processor12, _processor13, _processor21, _processor22;

	Pipe _main_inPipe;
	Pipe _outPipe;

	EchoMessage _message1, _message2;

	StatisticsCollector _statisticsCollector;	
	
	@Test
	public void test_processors_and_messages() {
		_statisticsCollector = new StatisticsCollector();

		_processor11 = new Processor();
		_processor11.setName("Processor#1.1");
		_processor11.setStatisticsCollector(_statisticsCollector);

		_processor12 = new Processor();
		_processor12.setName("Processor#1.2");
		_processor12.setStatisticsCollector(_statisticsCollector);
		
		_processor13 = new Processor();
		_processor13.setName("Processor#1.3");
		_processor13.setStatisticsCollector(_statisticsCollector);		

		_processor21 = new Processor();
		_processor21.setName("Processor#2.1");
		_processor21.setStatisticsCollector(_statisticsCollector);

		_processor22 = new Processor();
		_processor22.setName("Processor#2.2");
		_processor22.setStatisticsCollector(_statisticsCollector);		
		
		_main_inPipe = new Pipe();
		_main_inPipe.setName("Pipe1");
		_main_inPipe.setStatisticsCollector(_statisticsCollector);

		_outPipe = new Pipe();
		_outPipe.setName("Pipe2");
		_outPipe.setStatisticsCollector(_statisticsCollector);
		
		_processor11.setExecutor(Executor.getInstance());
		_processor12.setExecutor(Executor.getInstance());
		_processor13.setExecutor(Executor.getInstance());
		_processor21.setExecutor(Executor.getInstance());
		_processor22.setExecutor(Executor.getInstance());
		
		try {
			_main_inPipe.setProcessor(_processor11);
			_main_inPipe.setProcessor(_processor12);
			_main_inPipe.setProcessor(_processor13);
			
			_outPipe.setProcessor(_processor21);
			_outPipe.setProcessor(_processor22);
			
		} catch (NullProcessorException e1) {
			e1.printStackTrace();
		}

		_processor11.setOutPipe(_outPipe);
		_processor12.setOutPipe(_outPipe);		
		_processor13.setOutPipe(_outPipe);		

		int messageCount = 10000;

		try {
			EchoMessage message = null;
			for (int i = 0; i < messageCount; i++) {
				message = new EchoMessage();
				message.setGroupID(String.format("Test Message #%d", i));
				_main_inPipe.setMessage(message);
			}
		} catch (RichedMaxCapacityException e) {
			e.printStackTrace();
		}
		
		while(_processor11.getState() != ProcessorStatus.WAITING
				|| _processor12.getState() != ProcessorStatus.WAITING
				|| _processor13.getState() != ProcessorStatus.WAITING
				|| _processor21.getState() != ProcessorStatus.WAITING
				|| _processor22.getState() != ProcessorStatus.WAITING){
			// System.out.println("WAITING...");
			Thread.yield();
			
		}
		
		Assert.assertTrue(messageCount == (
				_statisticsCollector.getMessagesTotal4(_processor11.getName())
				+ _statisticsCollector.getMessagesTotal4(_processor12.getName())
				+ _statisticsCollector.getMessagesTotal4(_processor13.getName())));

		Assert.assertTrue(messageCount == (
						_statisticsCollector.getMessagesTotal4(_processor21.getName())
						+ _statisticsCollector.getMessagesTotal4(_processor22.getName())));
		
		System.out.println(_statisticsCollector.getReport());
	}
}
