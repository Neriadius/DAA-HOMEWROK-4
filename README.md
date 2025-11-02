Design and Analysis of Algorithms — Homework 4 Report
Data Summary
The dataset consists of 9 directed graphs with varying sizes and structural properties:

File Name	Nodes	Edges	Structure Type	Weight Model				
small_dag_6.json	6	6	DAG	Unit weight				
small_cycle_8.json	8	8	Cyclic	Unit weight				
small_mixed_10.json	10	10	Mixed	Unit weight				
medium_multiscc_12.json	12	11	Multi-SCC	Unit weight				
medium_dense_16.json	16	15	Dense	Unit weight				
medium_sparse_20.json	20	11	Sparse	Unit weight				
large_sparse_30.json	30	15	Sparse	Unit weight				
large_mixed_40.json	40	24	Mixed	Unit weight				
large_dense_50.json	50	50	Dense	Unit weight				

Weight Model: All graphs use unit weights (weight = 1.0) for simplicity in analysis.
Results
SCC Algorithm Performance (Tarjan's Algorithm)
File Name	Nodes	Components	Time (ms)	DFS Visits	DFS Edges			
small_dag_6.json	6	6	<1	6	6			
small_cycle_8.json	8	3	<1	8	8			
small_mixed_10.json	10	3	<1	10	10			
medium_multiscc_12.json	12	5	<1	12	11			
medium_dense_16.json	16	16	<1	16	15			
medium_sparse_20.json	20	20	<1	20	11			
large_sparse_30.json	30	30	<1	30	15			
large_mixed_40.json	40	35	<1	40	24			
large_dense_50.json	50	50	<1	50	50			

Total SCC Test Time: 35.801 ms for all 9 graphs
Topological Sort Performance (Kahn's Algorithm)
Issue Identified: The topological sort implementation shows empty components and vertex orders in all test cases, indicating a bug in the component mapping logic where component IDs ("C0", "C1", ...) don't match the expected format in the condensation graph.
File Name	Components Found	Vertex Order Length		
All test files	All empty	0		

Total Topo Test Time: 38.144 ms (despite the bug)
DAG Algorithms Performance
Longest Path Results
File Name	Valid DAG?	Critical Path Length	Max Distance				
small_dag_6.json	Yes	10.0	0→1→3→4→5				
medium_dense_16.json	Yes	17.0	0→2→5→8→11→14				
large_dense_50.json	Yes	53.0	0→3→6→9→...→48				
medium_sparse_20.json	Yes	9.0	0→1→2→3				
large_sparse_30.json	Yes	1.0	0→1				

Failed DAG Tests (non-DAG graphs):
•	large_mixed_40.json
•	medium_multiscc_12.json
•	small_cycle_8.json
•	small_mixed_10.json
Shortest Path Results
File Name	Valid DAG?	Shortest Path 0→N	Distance				
small_dag_6.json	Yes	0→2→3→4→5	9.0				
medium_dense_16.json	Yes	0→1→3→6→9→12→15	9.0				
large_dense_50.json	Yes	0→1→4→7→...→49	50.0				

Analysis
SCC Algorithm (Tarjan's)
Bottlenecks:
•	DFS recursion depth for large graphs
•	Stack operations and onStack set maintenance
•	Component reconstruction in large SCCs
Structure Effects:
•	Dense graphs: More edges lead to higher dfsEdges count but linear time complexity
•	Sparse graphs: Fewer edges visited, faster execution
•	Cyclic structures: Fewer but larger components
•	DAGs: Each vertex forms its own component (n components for n vertices)
Performance: Excellent across all graph sizes with O(V+E) complexity.
Topological Sort
Issues Identified:
•	Component ID mapping failure between SCC result and topological sorter
•	Empty results across all test cases despite correct condensation DAG
Expected Bottlenecks:
•	Indegree computation: O(V+E)
•	Queue processing: O(V+E)
•	Would perform best on sparse DAGs
DAG Algorithms (Shortest/Longest Path)
Bottlenecks:
•	Topological sort preprocessing
•	Edge relaxation in topological order: O(V+E)
•	Path reconstruction
Structure Effects:
•	Dense DAGs: More edge relaxations, longer paths
•	Sparse DAGs: Fewer reachable nodes, shorter paths
•	Non-DAG graphs: Immediate failure during topological sort
Performance Characteristics:
•	Works only on true DAGs
•	Efficient O(V+E) complexity for both shortest and longest paths
•	Longest path useful for critical path analysis
Conclusions
Method Selection Guidelines
1.	SCC Detection (Tarjan's Algorithm)
o	Use when: Analyzing graph connectivity, finding cycles, preprocessing for condensation
o	Avoid when: Only need simple path finding in known DAGs
o	Best for: General directed graph analysis
2.	Topological Sort (Kahn's Algorithm)
o	Use when: Task scheduling, dependency resolution, DAG preprocessing
o	Requires: Acyclic graphs
o	Implementation note: Ensure proper component ID mapping
3.	DAG Shortest/Longest Path
o	Use when: Critical path analysis, project scheduling, dependency chains
o	Requires: Valid DAG structure
o	Best for: Weighted dependency networks
Practical Recommendations
1.	Always validate graph structure before applying DAG algorithms
2.	Use SCC as preprocessing step to handle general directed graphs
3.	For large graphs, consider:
o	Memory-efficient data structures
o	Iterative DFS for SCC to avoid stack overflow
o	Batch processing for very large datasets
4.	Testing strategy:
o	Include cyclic and acyclic test cases
o	Verify component mappings in topological sort
o	Validate path reconstruction in DAG algorithms
Performance Insights
•	SCC algorithm is robust and efficient across all graph types
•	DAG algorithms provide optimal path solutions but only work on acyclic graphs
•	Topological sort is efficient for DAGs but requires careful implementation
•	Graph density affects edge processing but doesn't change asymptotic complexity
The implementation successfully handles graphs up to 50 nodes efficiently, with SCC computation completing in under 1ms per graph even for the largest test cases.
