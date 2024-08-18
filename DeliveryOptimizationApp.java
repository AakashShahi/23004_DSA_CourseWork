import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DeliveryOptimizationApp extends JFrame {

    private JTextArea deliveryListArea;
    private JTextField vehicleCapacityField;
    private JTextField distanceConstraintField;
    private JComboBox<String> algorithmComboBox;
    private JButton optimizeButton;
    private JTextArea resultArea;
    private JPanel mapPanel;

    public DeliveryOptimizationApp() {
        setTitle("Delivery Route Optimization");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        deliveryListArea = new JTextArea(10, 40);
        vehicleCapacityField = new JTextField(10);
        distanceConstraintField = new JTextField(10);
        algorithmComboBox = new JComboBox<>(new String[]{"Dijkstra", "TSP"});
        optimizeButton = new JButton("Optimize");
        resultArea = new JTextArea(10, 40);
        mapPanel = new JPanel();
        mapPanel.setBackground(Color.LIGHT_GRAY);
        mapPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Ensure JTextArea scrolls when content is too long
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Setup Layout
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(new JLabel("Delivery Points (Address;Priority):"), gbc);
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(new JScrollPane(deliveryListArea), gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Vehicle Capacity:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(vehicleCapacityField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Distance Constraint:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(distanceConstraintField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Algorithm:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(algorithmComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        inputPanel.add(optimizeButton, gbc);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(mapPanel, BorderLayout.SOUTH);

        // Add Action Listener for Optimize Button
        optimizeButton.addActionListener(new OptimizeAction());
    }

    private class OptimizeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Retrieve input data
                String deliveryListText = deliveryListArea.getText();
                String vehicleCapacityText = vehicleCapacityField.getText();
                String distanceConstraintText = distanceConstraintField.getText();
                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();

                // Parse inputs
                List<DeliveryPoint> deliveryPoints = parseDeliveryPoints(deliveryListText);
                int vehicleCapacity = Integer.parseInt(vehicleCapacityText);
                int distanceConstraint = Integer.parseInt(distanceConstraintText);

                // Start Optimization in a SwingWorker
                SwingWorker<RouteOptimizationResult, Void> worker = new SwingWorker<>() {
                    @Override
                    protected RouteOptimizationResult doInBackground() {
                        // Perform optimization
                        return optimizeRoute(deliveryPoints, vehicleCapacity, distanceConstraint, selectedAlgorithm);
                    }

                    @Override
                    protected void done() {
                        try {
                            RouteOptimizationResult result = get();
                            resultArea.setText(result.toString());
                            visualizeRoute(result);
                        } catch (InterruptedException | ExecutionException ex) {
                            JOptionPane.showMessageDialog(DeliveryOptimizationApp.this, "Error during optimization: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                worker.execute();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(DeliveryOptimizationApp.this, "Invalid number format. Please check your input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<DeliveryPoint> parseDeliveryPoints(String text) {
        List<DeliveryPoint> points = new ArrayList<>();
        String[] lines = text.split("\n");
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length == 2) {
                String address = parts[0].trim();
                try {
                    int priority = Integer.parseInt(parts[1].trim());
                    points.add(new DeliveryPoint(address, priority));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid priority value for address: " + address, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return points;
    }

    private RouteOptimizationResult optimizeRoute(List<DeliveryPoint> deliveryPoints, int vehicleCapacity, int distanceConstraint, String algorithm) {
        if (algorithm.equals("Dijkstra")) {
            return optimizeWithDijkstra(deliveryPoints, vehicleCapacity, distanceConstraint);
        } else {
            return optimizeWithTSP(deliveryPoints, vehicleCapacity, distanceConstraint);
        }
    }

    private RouteOptimizationResult optimizeWithDijkstra(List<DeliveryPoint> deliveryPoints, int vehicleCapacity, int distanceConstraint) {
        // Simulate Dijkstra's algorithm: sort by priority
        deliveryPoints.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
        return new RouteOptimizationResult("Dijkstra Result", deliveryPoints);
    }

    private RouteOptimizationResult optimizeWithTSP(List<DeliveryPoint> deliveryPoints, int vehicleCapacity, int distanceConstraint) {
        // Simulate TSP algorithm: sort by priority
        deliveryPoints.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
        return new RouteOptimizationResult("TSP Result", deliveryPoints);
    }

    private void visualizeRoute(RouteOptimizationResult result) {
        // Clear previous visualization
        mapPanel.removeAll();

        // Create a grid layout for mapPanel
        mapPanel.setLayout(new GridLayout(result.getRoute().size(), 1));

        // Colorful markers for each delivery point
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CYAN, Color.MAGENTA};
        int colorIndex = 0;

        for (DeliveryPoint point : result.getRoute()) {
            JPanel pointPanel = new JPanel();
            pointPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            pointPanel.setBackground(colors[colorIndex % colors.length]);
            pointPanel.add(new JLabel(point.toString()));
            mapPanel.add(pointPanel);
            colorIndex++;
        }

        mapPanel.revalidate();
        mapPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DeliveryOptimizationApp app = new DeliveryOptimizationApp();
            app.setVisible(true);
        });
    }
}

// Define DeliveryPoint class
class DeliveryPoint {
    private String address;
    private int priority;

    public DeliveryPoint(String address, int priority) {
        this.address = address;
        this.priority = priority;
    }

    public String getAddress() {
        return address;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return address + " (Priority: " + priority + ")";
    }
}

// Define RouteOptimizationResult class
class RouteOptimizationResult {
    private String description;
    private List<DeliveryPoint> route;

    public RouteOptimizationResult(String description, List<DeliveryPoint> route) {
        this.description = description;
        this.route = route;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(description).append("\n");
        for (DeliveryPoint point : route) {
            sb.append(point.toString()).append("\n");
        }
        return sb.toString();
    }

    public String getDescription() {
        return description;
    }

    public List<DeliveryPoint> getRoute() {
        return route;
    }

    public String getRouteAsHtml() {
        StringBuilder sb = new StringBuilder();
        for (DeliveryPoint point : route) {
            sb.append(point.toString()).append("<br>");
        }
        return sb.toString();
    }
}

// 123 Elm Street; 3
// 456 Oak Avenue; 1
// 789 Pine Road; 2
// 101 Maple Street; 4
// 202 Birch Boulevard; 2
// 303 Cedar Lane; 3


// 10

// 50