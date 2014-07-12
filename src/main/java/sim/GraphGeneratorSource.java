package sim;

import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.FullGenerator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.pmw.tinylog.Logger;


/**
 * Handles everything related to automatic graph generation. Wrapper for
 * Generator classes.
 *
 */
public class GraphGeneratorSource {


    ///////////////////////////////////////////////////////////////////////////
    // PROTECTED ENUM
    ///////////////////////////////////////////////////////////////////////////

    protected enum GraphType {
        GRID,
        CHAIN,
        CUSTOM,
        FULLY_CONNECTED
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private BaseGenerator generator;


    /**
     * How many nextEvents() should be called? This basically means how many
     * nodes the graph should have, but ultimately depends on the generator in
     * questions
     */
    private int events;


    /**
     * Should the graph have curved edges?
     */
    private boolean curvedEdges;


    private static final GraphGeneratorSource INSTANCE = new GraphGeneratorSource();


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////


    private GraphGeneratorSource() {
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void graphGrid(int events, boolean directed, boolean crossLinks) {
        this.events = events;
        generator = new GridGenerator(crossLinks, false, false, directed);
    }


    public void graphFullyConnected(int events, boolean directed, boolean randomlyDirectedEdges) {
        this.events = events;
        generator = new FullGenerator(directed, randomlyDirectedEdges);
    }


    public void graphChain(int events, boolean directed, boolean doublyLinked, boolean loopBack) {
        this.events = events;

        // In this case the graph should have curved edges, looks much nicer
        if (directed && doublyLinked) {
            curvedEdges = true;
        }

        generator = new ChainGenerator(directed, doublyLinked, loopBack);
    }


    public void generateGraph(ExtendedGraph g) {
        Logger.info("Graph generation - BEGIN");

        // Must be called here otherwise graph colors go B&W for some reason
        if (curvedEdges) {
            GraphVis.getInstance().curvedEdges();
        }

        generator.addSink(g);
        generator.begin();

        // Because call to begin() is basically like a call to nextEvent()
        events--;

        // What an event means depends on the generator in question
        for (int i = 0; i < events; i++) {
            generator.nextEvents();
            Sleep.sleep();   // To help the user see the graph being made
        }

        generator.end();
        generator.removeSink(g);

        Logger.info("Graph generation - COMPLETE");
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    public static GraphGeneratorSource getInstance() {
        return INSTANCE;
    }
}
