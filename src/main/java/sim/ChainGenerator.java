/*
 * ChainGenerator.java
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

import org.graphstream.algorithm.generator.BaseGenerator;
import org.pmw.tinylog.Logger;


/**
 * Generator for a chain graph of any size. NOTE: This class was merged into
 * GraphStream core 1.3.
 *
 * <p>
 * This generator creates a chain graph (much like a linked-list). By default,
 * the graph is directed and doubly linked (much like a doubly linked-list).
 * That is, each node has an edge to the next node in the chain, and one going
 * back to the previous (except for the first and last nodes). A loop back edge
 * can be added such that the last node in the chain has an edge directly back
 * to the starting node.
 * </p>
 *
 * <h2>Usage</h2>
 *
 * <p>
 * Calling {@link #begin()} will add an initial node with no edges. Each call
 * to {@link #nextEvents()} will add a new node, connected to the previous node,
 * in a chain. If the graph is directed and doubly linked, each call will result
 * in the addition of two new edges, otherwise just a single edge is added. Thus,
 * if the graph is undirected, then the doubly linked parameter is ignored, and
 * only single edges are created between nodes.
 * </p>
 *
 * <p>
 * If you are displaying the graph, directed and doubly-linked chain graphs are
 * best viewed with the {@code cubic-curve} edge shape CSS property, though it
 * will require the use of the <b>gs-ui</b> renderer to work.
 * </p>
 */
public class ChainGenerator extends BaseGenerator {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Used to generate node names
     */
    private int nodeNames = 0;


    /**
     * Create doubly linked nodes. That is, each node will have an extra edge
     * directed towards the previous node (except for the first and last node).
     * This does not apply if the graph is undirected.
     */
    private boolean doublyLinked;


    /**
     * Create a loop back edge. That is, the last node has an edge looping back
     * to the starting node.
     */
    private boolean loopBack = false;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    /**
     * New chain generator. By default edges are directed, nodes doubly
     * linked, and there is no loop back edge.
     */
    public ChainGenerator() {
        this(true, true, false);
    }


    /**
     * New chain generator.
     *
     * @param directed Should the edges be directed?
     * @param doublyLinked Should the nodes be doubly linked?
     * @param loopBack Should the graph have a loop back edge?
     */
    public ChainGenerator(boolean directed, boolean doublyLinked, boolean loopBack) {
        // Direction of edges should not be random, hence false by default
        super(directed, false);

        this.doublyLinked = doublyLinked;
        this.loopBack = loopBack;
    }


    /**
     * Add an initial node.
     *
     * @see org.graphstream.algorithm.generator.Generator#begin()
     */
    public void begin() {
        String id = Integer.toString(nodeNames);

        addNode(id);

        Logger.trace("Node added - {0}", nodeNames);
    }


    /**
     * Add a new node (link) in the chain.
     *
     * @see org.graphstream.algorithm.generator.Generator#nextEvents()
     */
    @Override
    public boolean nextEvents() {
        String id_previous = Integer.toString(nodeNames);

        // Prefix increment - http://stackoverflow.com/a/5413593
        String id_next = Integer.toString(++nodeNames);

        addNode(id_next);
        addEdge(null, id_previous, id_next);
        Logger.trace("EDGE ADDED: {0}_{1}", id_previous, id_next);

        // No point in adding double edge if undirected
        if (doublyLinked && directed) {
            addEdge(null, id_next, id_previous);
            Logger.trace("EDGE ADDED: {0}_{1}", id_next, id_previous);
        }

        return true;
    }


    /**
     * If the graph is set to have a loop back edge, this method will add it.
     *
     * @see org.graphstream.algorithm.generator.Generator#end()
     */
    @Override
    public void end() {
        if (loopBack) {
            String id_last = Integer.toString(nodeNames);
            addEdge(null, id_last, "0");
        }

        super.end();
    }
}