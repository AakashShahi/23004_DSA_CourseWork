// ??????????
// Imagine you're on a challenging hiking trail represented by an array nums, where each element represents the altitude at a specific point on the trail. You want to find the longest consecutive stretch of the trail you can hike while staying within a certain elevation gain limit (at most k).
// Constraints:
// You can only go uphill (increasing altitude).
// The maximum allowed elevation gain between two consecutive points is k.
// Goal: Find the longest continuous hike you can take while respecting the elevation gain limit.
// Examples:
// Input:
// Trail: [4, 2, 1, 4, 3, 4, 5, 8, and 15], Elevation gain limit (K): 3
// Output: 5
// Explanation
// Longest hike: [1, 3, 4, 5, 8] (length 5) - You can climb from 1 to 3 (gain 2), then to 4 (gain 1), and so on, all within the limit.
// Invalid hike: [1, 3, 4, 5, 8, 15] - The gain from 8 to 15 (7) exceeds the limit.

public class QuestionFiveB {

    public static int[] longestHike(int[] nums, int k) {
        int maxLength = 0;
        int start = 0;
        int maxStart = 0; // To track the starting index of the longest segment
        int maxEnd = 0; // To track the ending index of the longest segment

        for (int end = 1; end < nums.length; end++) {
            // Check if the current elevation gain exceeds k or if the current point is not
            // higher than the previous point
            if (nums[end] - nums[end - 1] > k || nums[end] <= nums[end - 1]) {
                start = end;
            }

            // Update maxLength and the segment if the current length is greater
            if (end - start + 1 > maxLength) {
                maxLength = end - start + 1;
                maxStart = start;
                maxEnd = end;
            }
        }

        // Create and return the longest segment
        int[] longestSegment = new int[maxLength];
        for (int i = 0; i < maxLength; i++) {
            longestSegment[i] = nums[maxStart + i];
        }

        System.out.println("Longest hike segment: " + java.util.Arrays.toString(longestSegment));
        return longestSegment;
    }

    public static void main(String[] args) {
        int[] trail = { 4, 2, 1, 4, 3, 4, 5, 8, 15 };
        int k = 3;

        int[] result = longestHike(trail, k);
        System.out.println("Longest hike length: " + result.length);
    }
}
