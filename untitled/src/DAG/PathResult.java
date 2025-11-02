package DAG;

import java.util.List;
import java.util.Map;

/** Result holder for DAG path algorithms */
public class PathResult {
    public final Map<String, Double> distance;
    public final Map<String, String> predecessor;
    public final List<String> path;
    public final double pathLength;

    public PathResult(Map<String, Double> distance,
                      Map<String, String> predecessor,
                      List<String> path,
                      double pathLength) {
        this.distance = distance;
        this.predecessor = predecessor;
        this.path = path;
        this.pathLength = pathLength;
    }

    @Override
    public String toString() {
        return "PathResult{" +
                "distance=" + distance +
                ", path=" + path +
                ", pathLength=" + pathLength +
                '}';
    }
}

