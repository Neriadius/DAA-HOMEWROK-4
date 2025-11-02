package DAG;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight parser for assignment JSON formats.
 * Supports:
 * 1) numeric nodes: { "n": 8, "edges":[ {"u":0,"v":1,"w":3}, ... ], "source": 4 }
 * 2) named vertices: { "vertices": ["A","B"], "edges":[ {"from":"A","to":"B"} ] }
 *
 * Not a full JSON parser but works for controlled dataset format.
 */
public class GraphIO {

    private static final Pattern N_PATTERN = Pattern.compile("\"n\"\\s*:\\s*(\\d+)");
    private static final Pattern EDGE_OBJ_PATTERN = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
    private static final Pattern EDGE_U_V_W_PATTERN = Pattern.compile(
            "\"u\"\\s*:\\s*(\\d+)\\s*,\\s*\"v\"\\s*:\\s*(\\d+)(?:\\s*,\\s*\"w\"\\s*:\\s*([-+]?[0-9]*\\.?[0-9]+))?",
            Pattern.DOTALL);
    private static final Pattern SOURCE_PATTERN = Pattern.compile("\"source\"\\s*:\\s*(\\d+)");
    private static final Pattern VERTICES_PATTERN = Pattern.compile("\"vertices\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
    private static final Pattern FROM_TO_PATTERN = Pattern.compile("\"from\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"to\"\\s*:\\s*\"(.*?)\"");

    public static Graph loadGraph(String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) throw new IOException("File not found: " + path);
        String content = Files.readString(f.toPath(), StandardCharsets.UTF_8);

        Graph g = new Graph();

        // Try numeric format with "n"
        Matcher nm = N_PATTERN.matcher(content);
        if (nm.find()) {
            int n = Integer.parseInt(nm.group(1));
            for (int i = 0; i < n; i++) g.addVertex(String.valueOf(i));

            int edgesIndex = content.indexOf("\"edges\"");
            if (edgesIndex >= 0) {
                int start = content.indexOf('[', edgesIndex);
                int end = findMatchingBracket(content, start);
                if (start >= 0 && end > start) {
                    String arr = content.substring(start + 1, end);
                    Matcher objM = EDGE_OBJ_PATTERN.matcher(arr);
                    while (objM.find()) {
                        String obj = objM.group(1);
                        Matcher e = EDGE_U_V_W_PATTERN.matcher(obj);
                        if (e.find()) {
                            String u = e.group(1);
                            String v = e.group(2);
                            String wstr = e.group(3);
                            double w = (wstr != null) ? Double.parseDouble(wstr) : 1.0;
                            g.addEdge(u, v, w);
                        }
                    }
                }
            }
            return g;
        }

        // Try named vertices format
        Matcher vm = VERTICES_PATTERN.matcher(content);
        if (vm.find()) {
            String inside = vm.group(1);
            String[] parts = inside.split(",");
            for (String p : parts) {
                String s = p.trim();
                if (s.length() >= 2 && (s.startsWith("\"") || s.startsWith("'"))) s = s.substring(1, s.length() - 1);
                if (!s.isEmpty()) g.addVertex(s);
            }

            int edgesIndex = content.indexOf("\"edges\"");
            if (edgesIndex >= 0) {
                int start = content.indexOf('[', edgesIndex);
                int end = findMatchingBracket(content, start);
                if (start >= 0 && end > start) {
                    String arr = content.substring(start + 1, end);
                    Matcher objM = EDGE_OBJ_PATTERN.matcher(arr);
                    while (objM.find()) {
                        String obj = objM.group(1);
                        Matcher e = FROM_TO_PATTERN.matcher(obj);
                        if (e.find()) {
                            String from = e.group(1);
                            String to = e.group(2);
                            g.addEdge(from, to, 1.0);
                        }
                    }
                }
            }
            return g;
        }

        // fallback: empty
        return g;
    }

    public static int readSource(String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) throw new IOException("File not found: " + path);
        String content = Files.readString(f.toPath(), StandardCharsets.UTF_8);
        Matcher m = SOURCE_PATTERN.matcher(content);
        if (m.find()) return Integer.parseInt(m.group(1));
        return -1;
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
