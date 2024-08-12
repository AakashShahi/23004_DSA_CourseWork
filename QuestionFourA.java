// Imagine you're a city planner tasked with improving the road network between key locations (nodes) represented by numbers 0 to n-1. Some roads (edges) have known travel times (positive weights), while others are under construction (weight -1). Your goal is to modify the construction times on these unbuilt roads to achieve a specific travel time (target) between two important locations (from source to destination).
// Constraints:
// You can only modify the travel times of roads under construction (-1 weight).
// The modified times must be positive integers within a specific range. [1, 2 * 10^9] You need to find any valid modification that achieves the target travel time. Examples:
// Input:
// City: 5 locations
// Roads: [[4, 1,-1], [2, 0,-1],[0,3,-1],[4,3,-1]]
// Source: 0, Destination: 1, Target time: 5 minutes
// Output: [[4,1,1],[2,0,1],[0,3,3],[4,3,1]]
// Solution after possible modification

// ????????????
import java.util.*;

public class QuestionFourA {

    static class Edge {
        int to, weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public static List<int[]> findModifiedRoads(int n, int[][] roads, int source, int destination, int target) {
        // Create adjacency list
        Map<Integer, List<Edge>> graph = new HashMap<>();
        List<int[]> underConstruction = new ArrayList<>();
        Map<int[], Integer> roadIndexMap = new HashMap<>();
        
        for (int i = 0; i < roads.length; i++) {
            int[] road = roads[i];
            int u = road[0];
            int v = road[1];
            int weight = road[2];
            
            if (!graph.containsKey(u)) graph.put(u, new ArrayList<>());
            if (!graph.containsKey(v)) graph.put(v, new ArrayList<>());
            
            if (weight == -1) {
                underConstruction.add(new int[]{u, v});
                roadIndexMap.put(new int[]{u, v}, i);
                graph.get(u).add(new Edge(v, Integer.MAX_VALUE)); // placeholder
                graph.get(v).add(new Edge(u, Integer.MAX_VALUE)); // placeholder
            } else {
                graph.get(u).add(new Edge(v, weight));
                graph.get(v).add(new Edge(u, weight));
            }
        }
        
        // Calculate initial shortest path using Dijkstra
        int[] dist = dijkstra(n, graph, source);
        int initialDistance = dist[destination];
        int requiredChange = target - initialDistance;

        if (requiredChange < 0) {
            System.out.println("Target travel time is less than the current shortest path.");
            return Collections.emptyList();
        }

        // Distribute the required change across roads under construction
        int numUnderConstruction = underConstruction.size();
        if (numUnderConstruction == 0) {
            return Collections.emptyList(); // No roads to modify
        }

        // Set up the result roads array
        List<int[]> result = new ArrayList<>(Arrays.asList(roads));
        
        for (int i = 0; i < numUnderConstruction; i++) {
            int[] road = underConstruction.get(i);
            int currentRoadIndex = roadIndexMap.get(road);
            int newWeight = 1; // Minimum positive weight
            
            if (i == numUnderConstruction - 1) {
                newWeight += requiredChange;
            }
            result.set(currentRoadIndex, new int[]{road[0], road[1], newWeight});
        }
        
        return result;
    }

    private static int[] dijkstra(int n, Map<Integer, List<Edge>> graph, int source) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{source, 0});
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int currentDist = current[1];
            
            if (currentDist > dist[u]) continue;
            
            for (Edge edge : graph.getOrDefault(u, Collections.emptyList())) {
                int v = edge.to;
                int weight = edge.weight;
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(new int[]{v, dist[v]});
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] roads = {{4, 1, -1}, {2, 0, -1}, {0, 3, -1}, {4, 3, -1}};
        int source = 0;
        int destination = 1;
        int target = 5;
        
        List<int[]> result = findModifiedRoads(n, roads, source, destination, target);
        if (result.isEmpty()) {
            System.out.println("No valid modification found.");
        } else {
            for (int[] road : result) {
                System.out.println(Arrays.toString(road));
            }
        }
    }
}
