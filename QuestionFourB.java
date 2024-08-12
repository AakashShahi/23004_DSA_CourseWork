// Imagine you're on a treasure hunt in an enchanted forest represented by a binary tree root. Each node in the tree has a value representing a magical coin. Your goal is to find the largest collection of coins that forms a magical grove.
// A magical grove is defined as a subtree where:
// Every coin's value on left subtree less than to the value of the coin directly above it (parent node).
// Every coin's value on right subtree is greater than to the value of the coin directly above it (parent node). Every coin in the grove needs to be binary search tree.
// Your task is to find the magical grove with the highest total value of coins.
// Examples:
// Forest: [1,4,3,2,4,2,5,null,null,null,null,null,null,4,6]
// Output: 20
// Largest Magical Grove: The subtree rooted at node 3, with a total value of 20 (3+2+5+4+6).

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class QuestionFourB {
    class Result {
        int maxSum;
        int sum;
        int min;
        int max;
        boolean isBST;

        Result(int maxSum, int sum, int min, int max, boolean isBST) {
            this.maxSum = maxSum;
            this.sum = sum;
            this.min = min;
            this.max = max;
            this.isBST = isBST;
        }
    }

    private int maxSum = 0;

    public int maxSumBST(TreeNode root) {
        traverse(root);
        return maxSum;
    }

    private Result traverse(TreeNode node) {
        if (node == null) {
            return new Result(0, 0, Integer.MAX_VALUE, Integer.MIN_VALUE, true);
        }

        Result left = traverse(node.left);
        Result right = traverse(node.right);

        if (left.isBST && right.isBST && node.val > left.max && node.val < right.min) {
            int sum = node.val + left.sum + right.sum;
            maxSum = Math.max(maxSum, sum);
            int min = Math.min(node.val, left.min);
            int max = Math.max(node.val, right.max);
            return new Result(maxSum, sum, min, max, true);
        }

        return new Result(maxSum, 0, 0, 0, false);
    }

    public static void main(String[] args) {
        QuestionFourB solution = new QuestionFourB();

        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(4);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(2);
        root.right.right = new TreeNode(5);
        root.right.right.left = new TreeNode(4);
        root.right.right.right = new TreeNode(6);

        int result = solution.maxSumBST(root);
        System.out.println(result); // Output: 20
    }
}
