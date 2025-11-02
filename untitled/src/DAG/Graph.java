package DAG;

import java.util.*;

/**
 * Weighted directed graph (String ids).
 * adj: Map<u, Map<v, weight>>
 */
public class Graph {
    private final LinkedHashSet<String> vertices = new LinkedHashSet<>();
    private final Map<String, LinkedHashMap<String, Double>> adj = new LinkedHashMap<>();

    public Graph() {}

    public void addVertex(String v) {
        if (!vertices.contains(v)) {
            vertices.add(v);
            adj.put(v, new LinkedHashMap<>());
        }
    }

    /** add edge with weight (keeps minimum if parallel edges) */
    public void addEdge(String from, String to, double weight) {
        addVertex(from);
        addVertex(to);
        LinkedHashMap<String, Double> m = adj.get(from);
        double prev = m.getOrDefault(to, Double.POSITIVE_INFINITY);
        m.put(to, Math.min(prev, weight));
    }

    /** convenience: unit weight */
    public void addEdge(String from, String to) {
        addEdge(from, to, 1.0);
    }

    public List<String> neighbors(String u) {
        LinkedHashMap<String, Double> map = adj.get(u);
        if (map == null) return Collections.emptyList();
        return new ArrayList<>(map.keySet());
    }

    public Map<String, Double> edgesFrom(String u) {
        LinkedHashMap<String, Double> map = adj.get(u);
        if (map == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(map);
    }

    public Map<String, Map<String, Double>> getAdj() {
        Map<String, Map<String, Double>> out = new LinkedHashMap<>();
        for (String u : adj.keySet()) out.put(u, Collections.unmodifiableMap(adj.get(u)));
        return Collections.unmodifiableMap(out);
    }

    public Set<String> vertexSet() {
        return Collections.unmodifiableSet(vertices);
    }

    public int vertexCount() {
        return vertices.size();
    }

    public int edgeCount() {
        int s = 0;
        for (var m : adj.values()) s += m.size();
        return s;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph |V|=").append(vertexCount()).append(" |E|=").append(edgeCount()).append("\n");
        for (String u : vertices) sb.append(u).append(" -> ").append(adj.get(u)).append("\n");
        return sb.toString();
    }
}
