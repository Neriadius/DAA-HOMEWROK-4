package SCC;

import java.util.*;

/**
 * Tarjan's algorithm for Strongly Connected Components.
 * Produces a list of components (each is list of vertex IDs) and builds the condensation DAG.
 */
public class TarjanSCC {

    private final Graph graph;
    private final Metrics metrics = new Metrics();

    // Tarjan state
    private final Map<String, Integer> disc = new HashMap<>();
    private final Map<String, Integer> low = new HashMap<>();
    private final Deque<String> stack = new ArrayDeque<>();
    private final Set<String> onStack = new HashSet<>();
    private int time = 0;
    private final List<List<String>> components = new ArrayList<>();

    public TarjanSCC(Graph g) {
        this.graph = g;
    }

    /**
     * Run Tarjan and return SCCResult containing components and condensation graph.
     */
    public SCCResult run() {
        metrics.start();
        disc.clear();
        low.clear();
        stack.clear();
        onStack.clear();
        components.clear();
        time = 0;
        // iterate through all vertices (preserve insertion order of Graph.vertices)
        for (String v : graph.vertices) {
            if (!disc.containsKey(v)) {
                dfs(v);
            }
        }
        metrics.stop();

        // Build condensation graph
        Graph condensation = buildCondensation();

        return new SCCResult(components, condensation, metrics);
    }

    private void dfs(String u) {
        metrics.dfsVisits++;
        disc.put(u, time);
        low.put(u, time);
        time++;
        stack.push(u);
        onStack.add(u);

        for (String v : graph.neighbors(u)) {
            metrics.dfsEdges++;
            if (!disc.containsKey(v)) {
                dfs(v);
                low.put(u, Math.min(low.get(u), low.get(v)));
            } else if (onStack.contains(v)) {
                low.put(u, Math.min(low.get(u), disc.get(v)));
            }
        }

        if (low.get(u).equals(disc.get(u))) {
            // root of SCC
            List<String> comp = new ArrayList<>();
            while (true) {
                String w = stack.pop();
                onStack.remove(w);
                comp.add(w);
                if (w.equals(u)) break;
            }
            // keep components with vertices in natural discovered order (optional)
            components.add(comp);
        }
    }

    /**
     * Build condensation: each SCC -> component vertex "C{i}",
     * and edges between components if any original edge crosses components.
     */
    private Graph buildCondensation() {
        Graph cond = new Graph();
        // map vertex -> compId index
        Map<String, Integer> compOf = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            String compId = "C" + i;
            cond.addVertex(compId);
            for (String v : components.get(i)) compOf.put(v, i);
        }

        // add edges between components (avoid duplicates)
        for (int i = 0; i < components.size(); i++) {
            String compId = "C" + i;
            Set<String> seen = new HashSet<>();
            for (String v : components.get(i)) {
                for (String to : graph.neighbors(v)) {
                    Integer j = compOf.get(to);
                    if (j == null) continue; // should not happen
                    if (j != i) {
                        String targetComp = "C" + j;
                        if (!seen.contains(targetComp)) {
                            cond.addEdge(compId, targetComp);
                            seen.add(targetComp);
                        }
                    }
                }
            }
        }

        return cond;
    }
}