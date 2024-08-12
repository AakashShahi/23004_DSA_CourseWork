public class QuestionOneB {

    public static String decipherMessage(String s, int[][] shifts) {
        char[] message = s.toCharArray();

        for (int[] shift : shifts) {
            int start = shift[0];
            int end = shift[1];
            int direction = shift[2];

            for (int i = start; i <= end; i++) {
                if (direction == 1) {
                    message[i] = rotateClockwise(message[i]);
                } else {
                    message[i] = rotateCounterClockwise(message[i]);
                }
            }
        }

        return new String(message);
    }

    private static char rotateClockwise(char c) {
        return c == 'z' ? 'a' : (char) (c + 1);
    }

    private static char rotateCounterClockwise(char c) {
        return c == 'a' ? 'z' : (char) (c - 1);
    }

    public static void main(String[] args) {
        String s = "hello";
        int[][] shifts = {
            {0, 1, 1}, // Rotate discs 0 and 1 clockwise
            {2, 3, 0}, // Rotate discs 2 and 3 counter-clockwise
            {0, 2, 1}  // Rotate discs 0, 1, and 2 clockwise
        };

        String result = decipherMessage(s, shifts);
        System.out.println(result); // Output: jglko
    }
}

