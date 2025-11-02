package TEST;

import DAG.DagLongestPath;
import DAG.PathResult;
import DAG.Graph;
import DAG.GraphIO;
import DAG.DagShortestPaths;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DagLongestTest {
    public static void main(String[] args) throws IOException {
        String folderPath = "data"; // папка с JSON-файлами
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Folder not found: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.err.println("No JSON files found in " + folderPath);
            return;
        }

        for (File file : files) {
            System.out.println("===============================================");
            System.out.println("Running longest path on: " + file.getName());

            try {
                Graph g = GraphIO.loadGraph(file.getPath());
                int s = GraphIO.readSource(file.getPath());
                String source = (s >= 0) ? String.valueOf(s) : "0";

                List<String> topo = DagShortestPaths.topoSort(g);
                PathResult res = DagLongestPath.longestPath(g, source, topo);

                System.out.println("Longest distances from " + source + ":");
                res.distance.forEach((k, v) ->
                        System.out.printf("  %s -> %s%n", k, (Double.isInfinite(v) ? "NINF" : String.format("%.2f", v)))
                );

                System.out.println("Critical path: " + res.path);
                System.out.println("Critical path length: " + res.pathLength);
                System.out.println();
            } catch (Exception e) {
                System.err.println("Error processing " + file.getName() + ": " + e.getMessage());
            }
        }
    }
}
