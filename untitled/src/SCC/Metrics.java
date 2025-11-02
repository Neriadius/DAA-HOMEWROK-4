package SCC;

public class Metrics {
    public long startTime;
    public long endTime;
    public int dfsVisits;
    public int dfsEdges;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public long getElapsedMillis() {
        return (endTime - startTime) / 1_000_000;
    }

    @Override
    public String toString() {
        return String.format("time(ms)=%d, dfsVisits=%d, dfsEdges=%d",
                getElapsedMillis(), dfsVisits, dfsEdges);
    }
}