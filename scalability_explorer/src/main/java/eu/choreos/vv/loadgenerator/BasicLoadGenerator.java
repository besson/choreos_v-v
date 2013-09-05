package eu.choreos.vv.loadgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import eu.choreos.vv.experiments.Experiment;
import eu.choreos.vv.loadgenerator.strategy.LoadGenerationStrategy;

/**
 * Load generator that trigger the requests as fast as possible
 * 
 */
public class BasicLoadGenerator <K, T> implements LoadGenerator<K, T>, Callable<Double> {

	static final String LABEL = "response time (msec)";

	private int poolSize;
	private int timeout;
	private Experiment<K, T> experiment;
	private LoadGenerationStrategy strategy;

	protected long delay;

	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public List<Number> execute(int numberOfCalls, Experiment<K, T> experiment)
			throws Exception {
		final ExecutorService executorService = Executors
				.newFixedThreadPool(poolSize);
		final List<Future<Double>> futureResults = new ArrayList<Future<Double>>();
		final List<Number> results = new ArrayList<Number>();
		this.experiment = experiment;
		strategy.setDelay(delay);
		strategy.setup();
		try {
			for (int i = 0; i < numberOfCalls; i++) {
				strategy.beforeRequest();
				performRequest(executorService, futureResults);
				strategy.afterRequest();
			}
			executorService.shutdown();
			while (!executorService.awaitTermination(timeout, TimeUnit.SECONDS))
				;
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			throw e;
		}

		for (Future<Double> future : futureResults)
			results.add(future.get());

		return results;
	}

	protected void performRequest(final ExecutorService executorService,
			final List<Future<Double>> futureResults) throws Exception {
		Future<Double> result = executorService.submit(this);
		futureResults.add(result);
	}
	
	public LoadGenerationStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(LoadGenerationStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

//	public static void sleep(long delay) throws InterruptedException {
//		long millis = delay / 1000000;
//		int nanos = (int) (delay % 1000000);
//		Thread.sleep(millis, nanos);
//
//	}

	/**
	 * Calls the other methods in a proper sequence. It can be used by an
	 * ExecutorService.
	 * 
	 * @return finalMeasurement() - initialMeasurement()
	 */
	@Override
	public Double call() throws Exception {
		K valueBefore = experiment.beforeRequest();
		double start = System.currentTimeMillis();
		T valueRequest = experiment.request(valueBefore);
		double end = System.currentTimeMillis();
		experiment.afterRequest(valueRequest);
		return (end - start);
	}

}