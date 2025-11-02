package TEST;

import SCC.Graph;
import SCC.GraphIO;
import SCC.SCCResult;
import SCC.TarjanSCC;
import TOPO.KahnTopoSorter;
import TOPO.TopoResult;

import java.io.IOException;
import java.util.*;

public class TopoSmallGraphTest {

    private static void runTopoTest(String path) throws IOException {
        System.out.println("=== Running topological sort for " + path + " ===");
        Graph g = GraphIO.loadGraph(path);
        TarjanSCC scc = new TarjanSCC(g);
        SCCResult sccResult = scc.run();

        TopoResult topo = KahnTopoSorter.sort(sccResult);

        System.out.println("Component order:");
        for (int i = 0; i < topo.componentOrder.size(); i++) {
            System.out.printf("  C%d: %s%n", i, topo.componentOrder.get(i));
        }

        System.out.println("Derived vertex order:");
        System.out.println("  " + topo.vertexOrder);
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
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

        long start = System.nanoTime();
        for (String f : files) {
            runTopoTest(f);
        }
        long end = System.nanoTime();
        System.out.printf("=== Finished all topo tests in %.3f ms ===%n", (end - start) / 1_000_000.0);
    }
}
