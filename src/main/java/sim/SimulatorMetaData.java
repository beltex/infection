package sim;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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


    public void setDuration(long start) {
        long end = new Date().getTime();
        long diff = end - start;

        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

        duration = String.format("%d hour, %d min, %d sec",
                                 hours,

                                 minutes -
                                 TimeUnit.HOURS.toMinutes(hours),

                                 seconds -
                                 (minutes -
                                  TimeUnit.HOURS.toMinutes(hours)));
    }
}
