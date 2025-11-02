package SCC;

import java.util.*;

/**
 * Minimal directed graph representation with String vertex IDs.
 */
public class Graph {
    public final Set<String> vertices = new LinkedHashSet<>();
    public final Map<String, List<String>> adj = new HashMap<>();

    public Graph() {}

    public void addVertex(String v) {
        if (!vertices.contains(v)) {
            vertices.add(v);
            adj.put(v, new ArrayList<>());
        }
    }

    public void addEdge(String from, String to) {
        addVertex(from);
        addVertex(to);
        adj.get(from).add(to);
    }

    public List<String> neighbors(String v) {
        return adj.getOrDefault(v, Collections.emptyList());
    }

    public int vertexCount() {
        return vertices.size();
    }

    public int edgeCount() {
        int sum = 0;
        for (List<String> lst : adj.values()) sum += lst.size();
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph: |V|=").append(vertexCount()).append(", |E|=").append(edgeCount()).append("\n");
        for (String v : vertices) {
            sb.append(v).append(" -> ").append(adj.getOrDefault(v, List.of())).append("\n");
        }
        return sb.toString();
    }
}