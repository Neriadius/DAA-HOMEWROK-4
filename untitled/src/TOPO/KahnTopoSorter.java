package TOPO;

import SCC.Graph;
import SCC.SCCResult;

import java.util.*;

/**
 * Performs topological sorting of the condensation DAG (result of SCC).
 * Uses Kahn's algorithm (BFS-based).
 */
public class KahnTopoSorter {

    public static TopoResult sort(SCCResult sccResult) {
        Graph dag = sccResult.condensation;

        // Compute indegree for all vertices in DAG
        Map<String, Integer> indegree = new HashMap<>();
        for (String v : dag.vertices) indegree.put(v, 0);
        for (String v : dag.vertices) {
            for (String u : dag.neighbors(v)) {
                indegree.put(u, indegree.get(u) + 1);
            }
        }

        // Initialize queue with all 0-indegree vertices
        Queue<String> q = new ArrayDeque<>();
        for (String v : dag.vertices)
            if (indegree.get(v) == 0)
                q.add(v);

        List<String> topoOrder = new ArrayList<>();
        while (!q.isEmpty()) {
            String v = q.poll();
            topoOrder.add(v);
            for (String u : dag.neighbors(v)) {
                indegree.put(u, indegree.get(u) - 1);
                if (indegree.get(u) == 0) q.add(u);
            }
        }

        // map component IDs to their member vertices
        Map<String, List<String>> compToVertices = new HashMap<>();
        for (int i = 0; i < sccResult.components.size(); i++) {
            compToVertices.put(String.valueOf(i), sccResult.components.get(i));
        }

        // Construct final result
        List<List<String>> componentOrder = new ArrayList<>();
        List<String> vertexOrder = new ArrayList<>();

        for (String compId : topoOrder) {
            List<String> comp = compToVertices.getOrDefault(compId, List.of());
            componentOrder.add(comp);
            vertexOrder.addAll(comp);
        }

        return new TopoResult(componentOrder, vertexOrder);
    }
}
