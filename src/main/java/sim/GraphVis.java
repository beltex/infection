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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.BorderFactory;
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
 * NOTE: not using GraphStream event based updates. This is a TODO list item.
 */
public class GraphVis {


    //--------------------------------------------------------------------------
    // PRIVATE ATTRIBUTES
    //--------------------------------------------------------------------------


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
     * http://graphstream-project.org/doc/FAQ/Attributes/
     */
    private static final String UI_SIZE       = "ui.size";
    private static final String UI_LABEL      = "ui.label";
    private static final String UI_COLOR      = "ui.color";
    private static final String UI_QUALITY    = "ui.quality";
    private static final String UI_ANTIALIAS  = "ui.antialias";
    private static final String UI_STYLESHEET = "ui.stylesheet";


    /**
     * CSS file path
     */
    private static final String CSSPath = System.getProperty("user.dir") +
                                          File.separator + "src" +
                                          File.separator + "main" +
                                          File.separator + "resources" +
                                          File.separator + "style.css";

    /*
     * Labels for stats panel
     */
    private JLabel infectionCounter;
    private JLabel leaderElectionComplete;
    private JLabel allElectionComplete;


    private static final Font font = new Font("Courier New", Font.BOLD, 20);


    //--------------------------------------------------------------------------
    // CONSTRUCTOR
    //--------------------------------------------------------------------------


    private GraphVis() {
        /*
         * Use renderer from gs-ui package to enable all GraphStream CSS
         * properties. For example, cubic-curve for edge shape.
         */
        System.setProperty("org.graphstream.ui.renderer",
                           "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
    }


    //--------------------------------------------------------------------------
    // PROTECTED METHODS
    //--------------------------------------------------------------------------


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
        // Get display size - handles multi-monitor
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                            .getDefaultScreenDevice()
                                            .getDisplayMode();

        // JFrame measurements
        Double width = Math.ceil(dm.getWidth() * 0.85);
        Double height = Math.ceil(dm.getHeight() * 0.70);
        Double vis_width = width * 0.8;
        Double stat_width = width * 0.2;


        // Infection label
        infectionCounter = new JLabel("infectionCounter");
        infectionCounter.setFont(font);
        infectionCounter.setForeground(Color.WHITE);
        infectionCounter.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        infectionCounter.setToolTipText("How many agents are infected by the " +
                                                               "acutal leader?");
        updateInfectionLabel();

        // Leader believes election comp label
        leaderElectionComplete = new JLabel("leaderElectionComplete");
        leaderElectionComplete.setFont(font);
        leaderElectionComplete.setForeground(Color.WHITE);
        leaderElectionComplete.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        leaderElectionComplete.setToolTipText("Does the acutal leader believe "
                                              + "election is complete?");
        updateLeaderElectionCompleteLabel(false);


        // All believe infec comp
        allElectionComplete = new JLabel("allElectionComplete");
        allElectionComplete.setFont(font);
        allElectionComplete.setForeground(Color.WHITE);
        allElectionComplete.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        allElectionComplete.setToolTipText("How many agents believe infection "
                                           + "election is complete?");
        updateAllElectionCompleteLabel();


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
        view.setPreferredSize(new Dimension(vis_width.intValue(),
                                            width.intValue()));


        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(Color.DARK_GRAY);
        statsPanel.setLayout(new GridLayout(3, 1, 0, 0));

        //statsPanel.setSize(200, 720);
        //statsPanel.setMinimumSize(new Dimension(200, 720));
        statsPanel.setPreferredSize(new Dimension(stat_width.intValue(),
                                                  height.intValue()));
        statsPanel.add(infectionCounter);
        statsPanel.add(leaderElectionComplete);
        statsPanel.add(allElectionComplete);


        // Main window
        JFrame jframe = new JFrame("infection");
        jframe.setSize(width.intValue(), height.intValue());
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(new BorderLayout(0, 0));

        jframe.add(view, BorderLayout.CENTER);
        jframe.add(statsPanel, BorderLayout.LINE_START);

        // Should be done right before display
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
    }


    //--------------------------------------------------------------------------
    // PROTECTED METHODS - VISULAZATION
    //--------------------------------------------------------------------------


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
        n.addAttribute(UI_SIZE, ((double) n_numAgents /
                                 (double) g.getNumAgents()) * 100.0);
        n.addAttribute(UI_COLOR, (double) n.infectionCount() /
                                 (double) n.getAgentCount());

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
    protected void updateInfectionLabel() {
        infectionCounter.setText("Infections: " + g.infectionCount() + "/"
                                                + g.getNumAgents());
    }


    /**
     * Update the leader election complete label.
     */
    protected void updateLeaderElectionCompleteLabel(boolean flag) {
        // TODO: Should call graph to get this
        leaderElectionComplete.setText("Leader Believes: " + flag);
    }


    /**
     * Update the all election complete label.
     */
    protected void updateAllElectionCompleteLabel() {
        allElectionComplete.setText("Believers: " +
                                    g.electionCompleteCount() + "/" +
                                    g.getNumAgents());
    }


    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------


    /**
     * Step the graph visualization event and sleep to let user see the changes
     * more clearly. This is used by the update methods at the end of a call
     */
    private void stepSleep() {
        g.stepBegins(g.getStep() + 1);
        Sleep.sleep();
    }


    /**
     * Read in the CSS file and apply it to the graph.
     */
    private void applyCSS() {
        Scanner s = null;
        String css = "";

        try {
            s = new Scanner(new File(CSSPath));

            // Read in the CSS
            while (s.hasNext()) {
                css = css.concat(s.nextLine());
            }

            g.addAttribute(UI_STYLESHEET, css);
        } catch (FileNotFoundException e) {
            Logger.error(e, "CSS FILE NOT FOUND - {0}", CSSPath);
        } finally {
            s.close();
        }
    }
}