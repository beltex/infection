package sim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.graphstream.graph.Edge;
import org.pmw.tinylog.Logger;


/**
 * Handles everything related to graph visualization.
 *
 * Note, not using GraphStream event based updates. This is a TODO list item.
 *
 */
public class GraphVis {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Singleton instance of this class
     */
    private static final GraphVis INSTANCE = new GraphVis();


    /**
     * Reference to the graph
     */
    private ExtendedGraph g;


    /*
     * Attribute keys defined by GraphStream. See link below for reference.
     *
     * http://graphstream-project.org/doc/FAQ/Attributes/Is-there-a-list-of-attributes-with-a-predefined-meaning-for-the-graph-viewer_1.0/
     */
    private static final String UI_SIZE = "ui.size";
    private static final String UI_LABEL = "ui.label";
    private static final String UI_COLOR = "ui.color";
    private static final String UI_QUALITY = "ui.quality";
    private static final String UI_ANTIALIAS = "ui.antialias";
    private static final String UI_STYLESHEET = "ui.stylesheet";


    /**
     * CSS file path to be applied to the graph
     */
    private static final String cssPath = System.getProperty("user.dir") +
                                          File.separator + "src" +
                                          File.separator + "main" +
                                          File.separator + "resources" +
                                          File.separator + "style.css";


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    private GraphVis() {
        /*
         * Use renderer from gs-ui package to enable all GraphStream CSS
         * properties. For example, cubic-curve for edge shape.
         */
        System.setProperty("org.graphstream.ui.renderer",
                           "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Get the singleton instance of GraphVis
     *
     * @return GraphVis object
     */
    public static GraphVis getInstance() {
        return INSTANCE;
    }


    /**
     * Initialize the GraphVis singleton class. This is done be providing a
     * reference to the graph that is desired to be visualized.
     *
     * @param g Graph to be displayed
     */
    public void init(ExtendedGraph g) {
        this.g = g;
    }


    /**
     * Turn on visualization. Opens up window to actually see the graph.
     */
    public void display() {
        g.addAttribute(UI_QUALITY);
        g.addAttribute(UI_ANTIALIAS);

        applyCSS();
        g.display();
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - VISULAZATION
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Turn on curved edges (cubic-curve). Currently used for directed, doubly
     * linked chain graphs.
     */
    public void curvedEdges() {
        g.addAttribute(UI_STYLESHEET, "edge { shape: cubic-curve; }");
    }


    /**
     * Update the node (re-draw)
     *
     * @param id The ID of the node to be updated
     */
    public void updateNode(String id) {
        ExtendedNode n = g.getNode(id);
        int n_numAgents = n.getAgentCount();
        String label = Integer.toString(n_numAgents);

        // If the node contains the leader, add an asterisk to the label
        if (n.containsLeader()) {
            label = label.concat("*");
        }

        // Update the label, size, and colour of the node
        n.addAttribute(UI_LABEL, label);
        n.addAttribute(UI_SIZE, ((double)n_numAgents/(double)g.getNumAgents()) * 100.0);
        n.addAttribute(UI_COLOR, (double)n.infectionCount()/(double)n.getAgentCount());

        stepSleep();
    }


    /**
     * Update the edge (re-draw). If a traversal is happening by an agent, edge
     * colour is set to blue.
     *
     *
     * @param e Edge to be updated
     * @param traverse Is this update to denote a edge traversal?
     */
    public void updateEdge(Edge e,  boolean traverse) {
        // Default setting for an edge
        int size = 1;
        int color = 0;

        if (traverse) {
            size = 1000;
            color = 1;	// Changes colour to blue to denote a transition
        }

        e.addAttribute(UI_SIZE, size);
        e.addAttribute(UI_COLOR, color);

        stepSleep();
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Step the graph visualization event and sleep to let user see the changes
     * more clearly. This is used by the update methods at the end of a call
     */
    private void stepSleep() {
        g.stepBegins(g.getStep() + 1);
        Sleep.sleep();
    }


    /**
     * Read in the CSS file and apply it to the graph
     */
    private void applyCSS() {
        Scanner s = null;
        String css = "";

        try {
            s = new Scanner(new File(cssPath));

            // Read in the CSS
            while (s.hasNext()) {
                css = css.concat(s.nextLine());
            }
            g.addAttribute(UI_STYLESHEET, css);
        } catch (FileNotFoundException e) {
            Logger.error(e, "CSS FILE NOT FOUND - {0}", cssPath);
        } finally {
            s.close();
        }
    }
}