package eu.choreos.vv.experiments;

import java.util.ArrayList;
import java.util.List;

import eu.choreos.vv.increasefunctions.ScalabilityFunction;
import eu.choreos.vv.loadgenerator.LoadGenerator;

/**
 * This class implements a skeleton of a scalability experiment consisted on
 * many steps. In each test battery, the frequency of requests and the quantity
 * of resources will be increased according to a ScalabilityFunction. A request
 * is executed a number of times, in each step, and some metrics are collected
 * for analysis. The steps will be executed up to a determined number of
 * executions (or until one's aggregated return value surpasses a defined
 * limit).
 * 
 */
public abstract class ScalabilityExperiment <K, T> extends Experiment <K, T> {

	private Number initialRequestsPerMinute;
	private Number inititalResoucesQuantity;


	/**
	 * Creates a new ScalabilityTester that uses UniformLoadGenarator, Mean and
	 * LinearIncrease
	 */
	public ScalabilityExperiment() {
		super();
		init();
	}

	private void init() {
		this.initialRequestsPerMinute = 60;
		this.inititalResoucesQuantity = 1;
		List<String> labels = new ArrayList<String>();
		labels.add("load");
		labels.add("size");
		super.setParameterLabels(labels);
	}

	public Number getInitialRequestsPerMinute() {
		return initialRequestsPerMinute;
	}

	public void setInitialRequestsPerMinute(Number initialRequestsPerMinute) {
		this.initialRequestsPerMinute = initialRequestsPerMinute;
	}

	public Number getInititalResoucesQuantity() {
		return inititalResoucesQuantity;
	}

	public void setInititalResoucesQuantity(Number inititalResoucesQuantity) {
		this.inititalResoucesQuantity = inititalResoucesQuantity;
	}

	@Override
	protected Number[] getInitialParameterValues() {
		Number[] values = new Number[2];
		values[0] = initialRequestsPerMinute;
		values[1] = inititalResoucesQuantity;
		return values;
	}
	
	@Override
	protected void updateParameterValues(Number... values) {
		super.setNumberOfRequestsPerMinute(values[0].intValue());
		super.setScaleSize(values[1].intValue());
	}
	
}
