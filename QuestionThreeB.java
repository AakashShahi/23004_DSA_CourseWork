import java.util.*;

public class QuestionThreeB {

    public static List<Integer> optimizeBoarding(List<Integer> head, int k) {
        // Handle edge case where k is less than or equal to 1
        if (k <= 1 || head == null || head.size() < k) {
            return head;
        }

        List<Integer> result = new ArrayList<>();
        int n = head.size();
        
        // Process the list in chunks of size k
        for (int i = 0; i < n; i += k) {
            // Determine the end of the current chunk
            int end = Math.min(i + k, n);
            // Reverse the chunk if it's full-size
            if (end - i == k) {
                List<Integer> chunk = new ArrayList<>(head.subList(i, end));
                Collections.reverse(chunk);
                result.addAll(chunk);
            } else {
                // If the chunk is smaller than k, keep it as it is
                result.addAll(head.subList(i, end));
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        // Example 1
        List<Integer> head1 = Arrays.asList(1, 2, 3, 4, 5);
        int k1 = 2;
        System.out.println(optimizeBoarding(head1, k1)); // Output: [2, 1, 4, 3, 5]

        // Example 2
        List<Integer> head2 = Arrays.asList(1, 2, 3, 4, 5);
        int k2 = 3;
        System.out.println(optimizeBoarding(head2, k2)); // Output: [3, 2, 1, 4, 5]
    }
}
