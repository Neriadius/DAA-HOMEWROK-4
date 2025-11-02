package DAG;

import java.util.*;

/**
 * Longest path on a DAG using DP over topological order.
 */
public class DagLongestPath {

    public static PathResult longestPath(DAG.Graph g, String source, List<String> topoOrder) {
        Map<String, Double> dist = new LinkedHashMap<>();
        Map<String, String> pred = new HashMap<>();
        for (String v : g.vertexSet()) dist.put(v, Double.NEGATIVE_INFINITY);
        if (!dist.containsKey(source)) throw new IllegalArgumentException("Source not in graph: " + source);
        dist.put(source, 0.0);

        for (String u : topoOrder) {
            double du = dist.get(u);
            if (Double.isInfinite(du) && du < 0) continue; // negative infinity
            for (var e : g.edgesFrom(u).entrySet()) {
                String v = e.getKey();
                double w = e.getValue();
                if (du + w > dist.get(v)) {
                    dist.put(v, du + w);
                    pred.put(v, u);
                }
            }
        }

        // find best end
        String bestNode = null;
        double best = Double.NEGATIVE_INFINITY;
        for (var en : dist.entrySet()) {
            if (en.getValue() > best) {
                best = en.getValue();
                bestNode = en.getKey();
            }
        }

        List<String> path = Collections.emptyList();
        if (bestNode != null && best > Double.NEGATIVE_INFINITY) {
            path = DagShortestPaths.reconstructPath(source, bestNode, pred);
        }

        return new PathResult(dist, pred, path, best);
    }
}
