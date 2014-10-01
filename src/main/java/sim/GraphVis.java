/*
 * GraphVis.java
 * infection
 *
 * Copyright (C) 2014  beltex <https://github.com/beltex>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package sim;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.graphstream.graph.Edge;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.Layouts;
import org.graphstream.ui.swingViewer.GraphRenderer;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
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


    private JLabel infectionCounter;


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
    // PROTECTED METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Get the singleton instance of GraphVis
     *
     * @return GraphVis object
     */
    protected static GraphVis getInstance() {
        return INSTANCE;
    }


    /**
     * Initialize the GraphVis singleton class. This is done be providing a
     * reference to the graph that is desired to be visualized.
     *
     * @param g Graph to be displayed
     */
    protected void init(ExtendedGraph g) {
        this.g = g;
    }


    /**
     * Turn on visualization. Opens up window to actually see the graph.
     */
    protected void display() {
        infectionCounter = new JLabel("infectionCounter");
        infectionCounter.setText("Infection Level: 1/" + g.getNumAgents());
        infectionCounter.setFont(new Font("Courier New", Font.PLAIN, 30));
        infectionCounter.setForeground(Color.WHITE);

        g.addAttribute(UI_QUALITY);
        g.addAttribute(UI_ANTIALIAS);

        applyCSS();


        // Instead of Viewer viewer = g.display(true);
        // Based off AbstractGraph.display()
        Viewer viewer = new Viewer(g,
                                  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        GraphRenderer renderer = Viewer.newGraphRenderer();
        viewer.addView(Viewer.DEFAULT_VIEW_ID, renderer, false);
        Layout layout = Layouts.newLayoutAlgorithm();
        viewer.enableAutoLayout(layout);


        View view = viewer.getDefaultView();
        view.resizeFrame(880, 720);
        view.setSize(880, 720);
        view.setPreferredSize(new Dimension(880, 720));


        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(Color.DARK_GRAY);
        //statsPanel.setSize(200, 720);
        //statsPanel.setMinimumSize(new Dimension(200, 720));
        statsPanel.setPreferredSize(new Dimension(200, 720));
        statsPanel.add(infectionCounter);



        JFrame jframe = new JFrame();
        jframe.setLocationRelativeTo(null);
        jframe.setTitle("FIXME");
        jframe.setSize(1080, 722);
        jframe.setLayout(new GridLayout(1,2));
        //FlowLayout flow = new FlowLayout(FlowLayout.LEFT, 0, 0);

        //jframe.setLayout(flow);

        jframe.add(view);
        jframe.add(statsPanel);
        jframe.setVisible(true);
        //jframe.setDefaultCloseOperation(operation);
    }


    ///////////////////////////////////////////////////////////////////////////
    // PROTECTED METHODS - VISULAZATION
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Turn on curved edges (cubic-curve). Currently used for directed, doubly
     * linked chain graphs.
     */
    protected void curvedEdges() {
        g.addAttribute(UI_STYLESHEET, "edge { shape: cubic-curve; }");
    }


    /**
     * Update the node (re-draw)
     *
     * @param id The ID of the node to be updated
     */
    protected void updateNode(String id) {
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
    protected void updateEdge(Edge e,  boolean traverse) {
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


    /**
     * Update the infection counter label.
     */
    protected void updateCounter() {
        infectionCounter.setText("Infection Level:" + g.infectionCount() +
                                 "/" + g.getNumAgents());
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