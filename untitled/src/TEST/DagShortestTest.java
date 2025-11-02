package TEST;

import DAG.DagShortestPaths;
import DAG.PathResult;
import DAG.Graph;
import DAG.GraphIO;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DagShortestTest {
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
            System.out.println("Running shortest paths on: " + file.getName());

            try {
                Graph g = GraphIO.loadGraph(file.getPath());
                int s = GraphIO.readSource(file.getPath());
                String source = (s >= 0) ? String.valueOf(s) : "0";

                System.out.println("Graph loaded: " + g.vertexCount() + " nodes, " + g.edgeCount() + " edges");
                List<String> topo = DagShortestPaths.topoSort(g);
                System.out.println("Topo order: " + topo);

                PathResult pr = DagShortestPaths.shortestPaths(g, source, topo);
                System.out.println("Shortest distances from " + source + ":");
                pr.distance.forEach((k, v) ->
                        System.out.printf("  %s -> %s%n", k, (Double.isInfinite(v) ? "INF" : String.format("%.2f", v)))
                );

                String target = String.valueOf(g.vertexCount() - 1);
                var pathNodes = DagShortestPaths.reconstructPath(source, target, pr.predecessor);
                System.out.println("Path " + source + " -> " + target + ": " + pathNodes);
                System.out.println();
            } catch (Exception e) {
                System.err.println("Error processing " + file.getName() + ": " + e.getMessage());
            }
        }
    }
}
