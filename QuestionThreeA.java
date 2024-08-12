import java.util.*;

public class QuestionThreeA {

    static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }

    public static List<String> canAcceptRequests(int numHouses, int[][] restrictions, int[][] requests) {
        UnionFind uf = new UnionFind(numHouses);
        Map<Integer, Set<Integer>> restrictionMap = new HashMap<>();

        // Process restrictions
        for (int[] restriction : restrictions) {
            int a = restriction[0];
            int b = restriction[1];
            int rootA = uf.find(a);
            int rootB = uf.find(b);
            if (rootA == rootB) {
                return Collections.nCopies(requests.length, "denied");
            }
            restrictionMap.computeIfAbsent(rootA, k -> new HashSet<>()).add(rootB);
            restrictionMap.computeIfAbsent(rootB, k -> new HashSet<>()).add(rootA);
        }

        List<String> result = new ArrayList<>();

        // Process requests
        for (int[] request : requests) {
            int house1 = request[0];
            int house2 = request[1];
            if (uf.find(house1) == uf.find(house2)) {
                result.add("denied");
                continue;
            }

            // Temporarily add the edge
            uf.union(house1, house2);
            int root1 = uf.find(house1);
            int root2 = uf.find(house2);

            boolean conflict = false;
            Set<Integer> restrictedSet = restrictionMap.get(root1);
            if (restrictedSet != null) {
                for (int restricted : restrictedSet) {
                    if (uf.find(restricted) == root2) {
                        conflict = true;
                        break;
                    }
                }
            }
            
            if (conflict) {
                result.add("denied");
                // Undo the union
                uf.parent[house2] = house2;
            } else {
                result.add("approved");
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int numHouses = 5;
        int[][] restrictions = {{0, 1}, {1, 2}, {2, 3}};
        int[][] requests = {{0, 4}, {1, 2}, {3, 1}, {3, 4}};
        
        List<String> result = canAcceptRequests(numHouses, restrictions, requests);
        System.out.println(result); // Output: [approved, denied, approved, denied]
    }
}
