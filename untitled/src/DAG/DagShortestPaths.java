package DAG;

import java.util.*;

/**
 * Shortest-paths on DAG using relaxation in topological order.
 */
public class DagShortestPaths {

    /** Kahn topological sort */
    public static List<String> topoSort(DAG.Graph g) {
        Map<String, Integer> indeg = new LinkedHashMap<>();
        for (String v : g.vertexSet()) indeg.put(v, 0);
        for (String u : g.vertexSet()) {
            for (String v : g.neighbors(u)) {
                indeg.put(v, indeg.getOrDefault(v, 0) + 1);
            }
        }

        Deque<String> q = new ArrayDeque<>();
        for (var e : indeg.entrySet()) if (e.getValue() == 0) q.addLast(e.getKey());

        List<String> order = new ArrayList<>();
        while (!q.isEmpty()) {
            String u = q.removeFirst();
            order.add(u);
            for (String v : g.neighbors(u)) {
                indeg.put(v, indeg.get(v) - 1);
                if (indeg.get(v) == 0) q.addLast(v);
            }
        }

        if (order.size() != g.vertexCount()) {
            throw new IllegalArgumentException("Graph is not a DAG (topo order incomplete)");
        }
        return order;
    }

    public static PathResult shortestPaths(DAG.Graph g, String source, List<String> topoOrder) {
        Map<String, Double> dist = new LinkedHashMap<>();
        Map<String, String> pred = new HashMap<>();
        for (String v : g.vertexSet()) dist.put(v, Double.POSITIVE_INFINITY);
        if (!dist.containsKey(source)) throw new IllegalArgumentException("Source not in graph: " + source);
        dist.put(source, 0.0);

        for (String u : topoOrder) {
            double du = dist.get(u);
            if (Double.isInfinite(du)) continue;
            for (var e : g.edgesFrom(u).entrySet()) {
                String v = e.getKey();
                double w = e.getValue();
                if (du + w < dist.get(v)) {
                    dist.put(v, du + w);
                    pred.put(v, u);
                }
            }
        }

        return new PathResult(dist, pred, Collections.emptyList(), Double.POSITIVE_INFINITY);
    }

    public static List<String> reconstructPath(String source, String target, Map<String, String> pred) {
        List<String> path = new ArrayList<>();
        String cur = target;
        while (cur != null) {
            path.add(cur);
            if (cur.equals(source)) break;
            cur = pred.get(cur);
        }
        Collections.reverse(path);
        if (path.isEmpty() || !path.get(0).equals(source)) return Collections.emptyList();
        return path;
    }
}
