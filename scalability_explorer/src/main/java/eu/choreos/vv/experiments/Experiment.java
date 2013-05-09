package eu.choreos.vv.experiments;

import java.util.ArrayList;
import java.util.List;

import eu.choreos.vv.Scalable;
import eu.choreos.vv.ScaleCaster;
import eu.choreos.vv.analysis.Analyzer;
import eu.choreos.vv.data.ScalabilityReport;
import eu.choreos.vv.deployment.Deployer;
import eu.choreos.vv.increasefunctions.LinearIncrease;
import eu.choreos.vv.increasefunctions.ScalabilityFunction;
import eu.choreos.vv.loadgenerator.DegeneratedLoadGenerator;
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
public abstract class Experiment implements Scalable {

	private int numberOfRequestsPerStep;
	private int numberOfRequestsPerMinute;
	private int scaleSize;
	private Integer numberOfSteps;
	private Double measurementLimit;

	private LoadGenerator loadGen;
	private ScalabilityFunction scalabilityFunction;
	private Deployer deployer;
	private Analyzer analyzer;

	private List<ScalabilityReport> reports;

	/**
	 * This method can be overridden to be executed before the experiment begin.
	 * 
	 * @throws Exception
	 */
	public void beforeExperiment() throws Exception {
	}

	/**
	 * This method can be overridden to execute before each step
	 * 
	 * @param resourceQuantity
	 *            current resource quantity
	 * @throws Exception
	 */
	public void beforeIteration() throws Exception {
	}

	/**
	 * This method can be overridden to execute before each request
	 * 
	 * @throws Exception
	 */
	public void beforeRequest() throws Exception {
	}

	/**
	 * This method must be overridden in order to execute the proper request
	 * 
	 * @throws Exception
	 */
	public void request() throws Exception {
	}

	/**
	 * This method can be overriden to execute after each request
	 * 
	 * @throws Exception
	 */
	public void afterRequest() throws Exception {
	}

	/**
	 * This method can be overriden to execute after each step
	 * 
	 * @throws Expeption
	 */
	public void afterIteration() throws Exception {

	}

	/**
	 * This method can be overriden to execute after the experiment
	 * 
	 * @throws Exception
	 */
	public void afterExperiment() throws Exception {
	}

	/**
	 * Creates a new ScalabilityTester that uses UniformLoadGenarator, Mean and
	 * LinearIncrease
	 */
	public Experiment() {
		this(new DegeneratedLoadGenerator(), new LinearIncrease(1)); 
	}

	/**
	 * Creates a new ScalabilityTester
	 * 
	 * @param loadGenerator
	 *            load generator to run the tests
	 * @param aggregator
	 *            aggregation function
	 * @param function
	 *            scalability function
	 */
	public Experiment(LoadGenerator loadGenerator, ScalabilityFunction function) {
		this.loadGen = loadGenerator;
		this.scalabilityFunction = function;
		this.numberOfSteps = 1;
		this.numberOfRequestsPerStep = 1;
		this.measurementLimit = Double.MAX_VALUE;
		reports = new ArrayList<ScalabilityReport>();
	}

	public LoadGenerator getLoadGenerator() {
		return loadGen;
	}

	public void setLoadGenerator(LoadGenerator loadGen) {
		this.loadGen = loadGen;
	}

	public ScalabilityFunction getScalabilityFunction() {
		return scalabilityFunction;
	}

	public void setScalabilityFunction(ScalabilityFunction function) {
		this.scalabilityFunction = function;
	}

	public Deployer getDeployer() {
		return deployer;
	}

	public void setDeployer(Deployer enacter) {
		this.deployer = enacter;
	}

	public int getNumberOfRequestsPerStep() {
		return numberOfRequestsPerStep;
	}

	public void setNumberOfRequestsPerStep(int number) {
		this.numberOfRequestsPerStep = number;
	}

	public int getNumberOfRequestsPerMinute() {
		return numberOfRequestsPerMinute;
	}

	public void setNumberOfRequestsPerMinute(int number) {
		this.numberOfRequestsPerMinute = number;
	}

	public int getScaleSize() {
		return scaleSize;
	}

	public void setScaleSize(int scaleSize) {
		this.scaleSize = scaleSize;
	}

	public Integer getNumberOfSteps() {
		return numberOfSteps;
	}

	public void setNumberOfSteps(Integer numberOfTestsToRun) {
		this.numberOfSteps = numberOfTestsToRun;
	}

	public Double getMeasurementLimit() {
		return measurementLimit;
	}

	public void setMeasurementLimit(Double measurementLimit) {
		this.measurementLimit = measurementLimit;
	}

	public Analyzer getAnalyser() {
		return analyzer;
	}

	public void setAnalyser(Analyzer analyser) {
		this.analyzer = analyser;
	}

	protected abstract Number[] setInitialParameterValues();

	protected abstract void getParameterValues(Number... values);

	protected abstract List<String> getParameterLabels();

	@Override
	public List<Number> execute(Number... params) throws Exception {
		getParameterValues(params);

		List<Number> results = new ArrayList<Number>();

		if (deployer != null)
			deployer.scale(getScaleSize());
		beforeIteration();

		loadGen.setDelay(60000000 / numberOfRequestsPerMinute);
		results = loadGen.execute(numberOfRequestsPerStep, this);

		afterIteration();

		return results;
	}

	/**
	 * runs a test battery according to the parameters
	 * 
	 * @param name
	 *            name to identify the battery - used by ScalabilityReportChart
	 * @param timesToRun
	 *            maximum number of test batteries to be executed
	 * @param latencyLimit
	 *            maximum return value allowed for a test battery
	 * @param numberOfExecutionsPerTest
	 *            number of executions in each test battery
	 * @param initialRequestsPerMinute
	 *            initial frequency of requests
	 * @param inititalResoucesQuantity
	 *            initial amount of resources
	 */
	// public void run(String name, int timesToRun, double latencyLimit,
	// int numberOfExecutionsPerTest, int initialRequestsPerMinute,
	// int inititalResoucesQuantity) throws Exception {
	// this.numberOfSteps = timesToRun;
	// this.measurementLimit = latencyLimit;
	// this.initialRequestsPerMinute = initialRequestsPerMinute;
	// this.inititalResoucesQuantity = inititalResoucesQuantity;
	//
	// run(name);
	// }
	//
	// public void run(String name, int timesToRun, int
	// numberOfExecutionsPerTest,
	// int initialRequestsPerMinute, int inititalResoucesQuantity)
	// throws Exception {
	// run(name, timesToRun, Double.MAX_VALUE, numberOfExecutionsPerTest,
	// initialRequestsPerMinute, inititalResoucesQuantity);
	// }

	public void run(String name) throws Exception {
		run(name, true, true);
	}
	
	public void run(String name, boolean analyse) throws Exception {
		run(name, analyse, true);
	}

	/**
	 * Runs a test battery according to current attributes of the
	 * ScalabilityTester
	 * 
	 * @param name
	 *            label to be used in a ScalabilityReportChart
	 * @param analyse
	 * 			  true to perform analysis at the and of the experiment
	 * @param store
	 *            false discards the experiment report
	 */
	public void run(String name, boolean analyse, boolean store) throws Exception {
		ScaleCaster scalingCaster = new ScaleCaster(this, name, numberOfSteps,
				measurementLimit, scalabilityFunction);

		scalingCaster.setInitialParametersValues(setInitialParameterValues());

		ScalabilityReport report;
		if (deployer != null)
			deployer.enact();
		beforeExperiment();
		report = scalingCaster.executeIncreasingParams();
		report.setParameterLabels(getParameterLabels());
		report.setMeasurementUnit(loadGen.getLabel());
		afterExperiment();
		if (store)
			reports.add(report);
		if (analyse)
			analyzer.analyse(reports);
	}

}
