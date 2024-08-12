public class QuestionTwoB {
    public static boolean canSitTogether(int[] nums, int indexDiff, int valueDiff) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n && j <= i + indexDiff; j++) {
                if (Math.abs(nums[i] - nums[j]) <= valueDiff) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] nums = {2, 3, 5, 4, 9};
        int indexDiff = 2;
        int valueDiff = 1;
        boolean result = canSitTogether(nums, indexDiff, valueDiff);
        System.out.println(result);  // Output: true
    }
}