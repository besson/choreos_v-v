package eu.choreos.vv.loadgenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

public class PoissonLoadGenerator extends FastestLoadGenerator {

	private RandomData random;
	private int delay;

	public PoissonLoadGenerator() {
		super();
		random = new RandomDataImpl();
	}

	@Override
	protected void performRequest(final ExecutorService executor,
			final List<Future<Double>> futureResults) throws Exception {
		long delay = nextPoisson();
		long start = System.currentTimeMillis();
		super.performRequest(executor, futureResults);
		long end = System.currentTimeMillis();
		sleep(delay - end + start);
	}

	@Override
	public void setDelay(int delay) {
		this.delay = delay;
	}

	private long nextPoisson() {
		return random.nextPoisson(delay);
	}

}
