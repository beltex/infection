package sim;

import java.util.Date;

import com.google.common.collect.Range;


/**
 * Holds meta data about the overall simualtion run. This class is seriablized
 * into JSON. This is the file you look at to get a view of how the simulation
 * turned out.
 *
 */
public class SimulatorMetaData {


    /**
     * When was the simulation performed?
     */
    private Date date;


    /**
     * How long did the whole simulation take?
     */
    private String duration;


    private String graphType;
    private int numNodes;

    private double interactProbability;
    private double traversalProbability;

    private double accuracy;

    private Range<Integer> numAgents;
    private int termA;
    private int termB;
    private int maxTimeSteps;
    private int runs;
}
