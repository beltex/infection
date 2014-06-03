package sim;

import org.graphstream.graph.Graph;
import org.graphstream.graph.NodeFactory;
import org.graphstream.graph.implementations.AbstractGraph;


public class ExtendedNodeFactory implements NodeFactory<ExtendedNode> {


    @Override
    public ExtendedNode newInstance(String id, Graph graph) {
        return new ExtendedNode((AbstractGraph)graph, id);
    }
}
