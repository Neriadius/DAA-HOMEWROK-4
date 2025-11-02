package TEST;

import SCC.Graph;
import SCC.GraphIO;
import SCC.SCCResult;
import SCC.TarjanSCC;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manual runnable tests for SCC algorithm on all data files in /DATA/.
 * Each filename contains the expected number of nodes (e.g. medium_dense_16.json -> 16 nodes).
 */
public class SCCSmallGraphTest {

    private static boolean isDag(Graph g) {
        Map<String, Integer> color = new HashMap<>();
        for (String v : g.vertices) color.put(v, 0);
        for (String v : g.vertices) {
            if (color.get(v) == 0 && !dfs(v, g, color)) return false;
        }
        return true;
    }

    private static boolean dfs(String u, Graph g, Map<String, Integer> color) {
        color.put(u, 1);
        for (String w : g.neighbors(u)) {
            if (color.get(w) == 1) return false; // found back edge -> cycle
            if (color.get(w) == 0 && !dfs(w, g, color)) return false;
        }
        color.put(u, 2);
        return true;
    }

    // Extract number from filename (e.g. "..._12.json" -> 12)
    private static int expectedNodeCount(String filename) {
        Pattern p = Pattern.compile("(\\d+)(?=\\.json$)");
        Matcher m = p.matcher(filename);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        throw new IllegalArgumentException("Cannot extract expected node count from filename: " + filename);
    }

    private void runSccTest(String filePath) throws IOException {
        int expected = expectedNodeCount(filePath);
        Graph g = GraphIO.loadGraph(filePath);
        if (g == null) throw new IOException("Graph is null for file: " + filePath);

        if (g.vertexCount() != expected) {
            throw new AssertionError("Vertex count mismatch: expected " + expected +
                    ", got " + g.vertexCount() + " (" + filePath + ")");
        }

        TarjanSCC scc = new TarjanSCC(g);
        SCCResult res = scc.run();

        int sum = res.components.stream().mapToInt(c -> c.size()).sum();
        if (sum != g.vertexCount()) {
            throw new AssertionError("Sum of SCC sizes (" + sum + ") != vertex count (" + g.vertexCount() + ")");
        }

        for (var c : res.components) {
            if (c.isEmpty()) throw new AssertionError("Empty SCC component in " + filePath);
        }

        if (!isDag(res.condensation)) {
            throw new AssertionError("Condensation is not a DAG in file: " + filePath);
        }

        System.out.printf("%s passed all checks (%d nodes, %d components)%n",
                filePath, g.vertexCount(), res.components.size());
    }

    // ---------------------------
    // Main method for manual running
    // ---------------------------
    public static void main(String[] args) {
        SCCSmallGraphTest tester = new SCCSmallGraphTest();
        String[] files = {
                "DATA/small_dag_6.json",
                "DATA/small_cycle_8.json",
                "DATA/small_mixed_10.json",
                "DATA/medium_multiscc_12.json",
                "DATA/medium_dense_16.json",
                "DATA/medium_sparse_20.json",
                "DATA/large_sparse_30.json",
                "DATA/large_mixed_40.json",
                "DATA/large_dense_50.json"
        };

        System.out.println("=== Running SCC tests manually ===");
        long startAll = System.nanoTime();
        for (String path : files) {
            try {
                tester.runSccTest(path);
            } catch (Exception e) {
                System.err.println(path + " failed: " + e.getMessage());
            }
        }
        long endAll = System.nanoTime();
        System.out.printf("=== Finished all tests in %.3f ms ===%n", (endAll - startAll) / 1_000_000.0);
    }
}