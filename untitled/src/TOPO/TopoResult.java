package TOPO;

import java.util.List;

/**
 * Container for results of topological sorting.
 * Contains:
 *  - order of components in the condensation DAG
 *  - derived order of original vertices
 */
public class TopoResult {
    public final List<List<String>> componentOrder; // SCC order
    public final List<String> vertexOrder;          // flattened order of original vertices

    public TopoResult(List<List<String>> componentOrder, List<String> vertexOrder) {
        this.componentOrder = componentOrder;
        this.vertexOrder = vertexOrder;
    }

    @Override
    public String toString() {
        return "TopoResult{" +
                "componentOrder=" + componentOrder +
                ", vertexOrder=" + vertexOrder +
                '}';
    }
}
