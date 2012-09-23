package eu.choreos.vv.loadgenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import eu.choreos.vv.loadgenerator.executable.Executable;

public class UniformLoadGenerator implements LoadGenerator {
	
	final int THREADS_TIMEOUT = 60;

	@Override
	public List<Number> execute(int numberOfCalls, int callsPerMin, Executable executable) throws Exception {
		final long delay = 60000 / callsPerMin;
		final ExecutorService executor = Executors.newCachedThreadPool(); 
		for(int i = 0; i < numberOfCalls; i++) {
			long start = System.currentTimeMillis();
			executor.execute(executable);
//			executable.run();
			long end = System.currentTimeMillis();
			Thread.sleep(delay - end + start);
		}
		executor.shutdown();
	    try {
      while (!executor.awaitTermination(THREADS_TIMEOUT, TimeUnit.SECONDS))
	          ;
	  } catch (InterruptedException e) {
	      executor.shutdownNow();
	  }
		return executable.getResults();
	}

}