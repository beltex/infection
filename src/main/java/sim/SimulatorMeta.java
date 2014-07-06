package sim;

import java.util.Date;

import com.google.common.collect.Range;


/**
 * Holds meta data about the overall simualtion run. This class is seriablized
 * into JSON. This is the file you look at to get a view of how the simulation
 * turned out.
 *
 */
public class SimulatorMeta {

    /**
     * When was the simulation performed?
     */
    private Date date;

    /**
    * How many agents
    */
    private Range<Integer> numAgents;
    private int termA;
    private int termB;
    private int maxTimeSteps;
    private int runs;

}
