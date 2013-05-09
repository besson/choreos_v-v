package eu.choreos.vv.loadgenerator;

import java.util.List;

import eu.choreos.vv.chart.Labeled;
import eu.choreos.vv.experiments.Experiment;

/**
 *  A load generator is a component capable to repeatedly run an Executable according to the defined execution pattern.
 *
 */

public interface LoadGenerator extends Labeled {
	/**
	 * Calls the Executable a number of times in a certain frequency
	 * @param times number of times to execute
	 * @param callsPerMin execution frequency
	 * @param exec object to be executed
	 * @return a list of the values returned by the executions
	 * @throws Exception
	 */
	public List<Number> execute(int times, Experiment exec) throws Exception;
	
	public void setDelay(int delay);
	
}
