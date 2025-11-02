package SCC;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphIO {

    private static final Pattern N_PATTERN = Pattern.compile("\"n\"\\s*:\\s*(\\d+)");
    private static final Pattern EDGE_OBJ_PATTERN = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
    private static final Pattern EDGE_UV_PATTERN = Pattern.compile("\"u\"\\s*:\\s*(\\d+)\\s*,\\s*\"v\"\\s*:\\s*(\\d+)", Pattern.DOTALL);

    public static Graph loadGraph(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("File not found: " + path);
        }

        String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        Graph g = new Graph();

        // Parse number of vertices
        Matcher nm = N_PATTERN.matcher(content);
        int n = 0;
        if (nm.find()) {
            n = Integer.parseInt(nm.group(1));
            for (int i = 0; i < n; i++) {
                g.addVertex(String.valueOf(i));
            }
        }

        // Parse edges
        int edgesIndex = content.indexOf("\"edges\"");
        if (edgesIndex >= 0) {
            int startArray = content.indexOf('[', edgesIndex);
            int endArray = findMatchingBracket(content, startArray);
            if (startArray >= 0 && endArray > startArray) {
                String edgesArray = content.substring(startArray + 1, endArray);
                Matcher objMatcher = EDGE_OBJ_PATTERN.matcher(edgesArray);
                while (objMatcher.find()) {
                    String obj = objMatcher.group(1);
                    Matcher pair = EDGE_UV_PATTERN.matcher(obj);
                    if (pair.find()) {
                        String from = pair.group(1);
                        String to = pair.group(2);
                        g.addEdge(from, to);
                    }
                }
            }
        }

        return g;
    }

    private static int findMatchingBracket(String s, int openPos) {
        if (openPos < 0 || openPos >= s.length() || s.charAt(openPos) != '[') return -1;
        int depth = 0;
        for (int i = openPos; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') depth++;
            else if (c == ']') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }
}
