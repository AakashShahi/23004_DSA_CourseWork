import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class QuestionTwoA extends JFrame {
    private JTextField inputField;
    private JButton calculateButton;
    private JLabel resultLabel;

    public QuestionTwoA() {
        // Set up the frame
        setTitle("Basic Calculator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create components
        inputField = new JTextField();
        calculateButton = new JButton("Calculate");
        resultLabel = new JLabel("Result: ");

        // Set up panel for input and button
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(inputField);
        panel.add(calculateButton);

        // Add components to frame
        add(panel, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        // Add action listener to button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expression = inputField.getText();
                try {
                    int result = evaluate(expression);
                    resultLabel.setText("Result: " + result);
                } catch (Exception ex) {
                    resultLabel.setText("Error: Invalid expression");
                }
            }
        });
    }

    private int evaluate(String expression) throws Exception {
        Stack<Integer> nums = new Stack<>();
        Stack<Character> ops = new Stack<>();
        int num = 0;
        int len = expression.length();
        boolean hasNum = false; // To handle multiple digit numbers

        for (int i = 0; i < len; i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) {
                num = num * 10 + (ch - '0');
                hasNum = true;
            } else {
                if (hasNum) {
                    nums.push(num);
                    num = 0;
                    hasNum = false;
                }
                if (ch == ' ') {
                    continue;
                } else if (ch == '(') {
                    ops.push(ch);
                } else if (ch == ')') {
                    while (ops.peek() != '(') {
                        nums.push(applyOp(ops.pop(), nums.pop(), nums.pop()));
                    }
                    ops.pop();
                } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                    while (!ops.isEmpty() && hasPrecedence(ch, ops.peek())) {
                        nums.push(applyOp(ops.pop(), nums.pop(), nums.pop()));
                    }
                    ops.push(ch);
                }
            }
        }

        if (hasNum) {
            nums.push(num);
        }

        while (!ops.isEmpty()) {
            nums.push(applyOp(ops.pop(), nums.pop(), nums.pop()));
        }

        return nums.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    private int applyOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return a / b;
        }
        return 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuestionTwoA().setVisible(true);
            }
        });
    }
}
