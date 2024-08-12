// Implement travelling a salesman problem using hill climbing algorithm.
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionFiveA {
    
    // Function to calculate the total distance of a given tour
    public static double calculateTotalDistance(int[][] distanceMatrix, List<Integer> tour) {
        double totalDistance = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            totalDistance += distanceMatrix[tour.get(i)][tour.get(i + 1)];
        }
        // Add the distance from the last city to the first city to complete the tour
        totalDistance += distanceMatrix[tour.get(tour.size() - 1)][tour.get(0)];
        return totalDistance;
    }

    // Function to generate a random tour
    public static List<Integer> generateRandomTour(int numCities) {
        List<Integer> tour = new ArrayList<>();
        for (int i = 0; i < numCities; i++) {
            tour.add(i);
        }
        Collections.shuffle(tour);
        return tour;
    }

    // Function to perform hill climbing to solve the TSP
    public static List<Integer> hillClimbing(int[][] distanceMatrix, List<Integer> currentTour) {
        boolean improvement = true;
        List<Integer> bestTour = new ArrayList<>(currentTour);
        double bestDistance = calculateTotalDistance(distanceMatrix, bestTour);

        while (improvement) {
            improvement = false;

            for (int i = 0; i < currentTour.size() - 1; i++) {
                for (int j = i + 1; j < currentTour.size(); j++) {
                    List<Integer> newTour = new ArrayList<>(currentTour);
                    // Swap two cities to create a new neighbor tour
                    Collections.swap(newTour, i, j);

                    double newDistance = calculateTotalDistance(distanceMatrix, newTour);

                    if (newDistance < bestDistance) {
                        bestTour = new ArrayList<>(newTour);
                        bestDistance = newDistance;
                        improvement = true;
                    }
                }
            }
            currentTour = new ArrayList<>(bestTour);
        }
        return bestTour;
    }

    public static void main(String[] args) {
        // Example distance matrix (symmetric TSP)
        int[][] distanceMatrix = {
            {0, 29, 20, 21},
            {29, 0, 15, 17},
            {20, 15, 0, 28},
            {21, 17, 28, 0}
        };

        int numCities = distanceMatrix.length;
        List<Integer> initialTour = generateRandomTour(numCities);
        System.out.println("Initial Tour: " + initialTour);
        System.out.println("Initial Distance: " + calculateTotalDistance(distanceMatrix, initialTour));

        List<Integer> bestTour = hillClimbing(distanceMatrix, initialTour);
        System.out.println("Best Tour: " + bestTour);
        System.out.println("Best Distance: " + calculateTotalDistance(distanceMatrix, bestTour));
    }
}
