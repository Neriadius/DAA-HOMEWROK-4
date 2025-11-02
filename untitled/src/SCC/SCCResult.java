package SCC;

import java.util.List;

/**
 * Result container for TarjanSCC.run(...)
 */
public class SCCResult {
    /** List of components; each component is a list of vertex IDs (Strings). */
    public final List<List<String>> components;

    /** Condensation graph: each vertex represents a component (IDs "C0","C1",...). */
    public final Graph condensation;

    /** Metrics captured during SCC run. */
    public final Metrics metrics;

    /** elapsed time in nanoseconds (copied from Metrics but kept for convenience) */
    public final long elapsedTimeNs;

    public SCCResult(List<List<String>> components, Graph condensation, Metrics metrics) {
        this.components = components;
        this.condensation = condensation;
        this.metrics = metrics;
        this.elapsedTimeNs = (metrics.endTime - metrics.startTime);
    }
}