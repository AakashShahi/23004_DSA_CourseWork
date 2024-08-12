// a)
// Imagine you're a scheduling officer at a university with n classrooms
// numbered 0 to n-1. Several different courses require classrooms throughout
// the day, represented by an array of classes classes[i] = [starti, endi],
// where starti is the start time of the class and endi is the end time (both in
// whole hours). Your goal is to assign each course to a classroom while
// minimizing disruption and maximizing classroom utilization. Here are the
// rules:
// • Priority Scheduling: Classes with earlier start times have priority when
// assigning classrooms. If multiple classes start at the same time, prioritize
// the larger class (more students).
// • Dynamic Allocation: If no classroom is available at a class's start time,
// delay the class until a room becomes free. The delayed class retains its
// original duration.
// • Room Release: When a class finishes in a room, that room becomes available
// for the next class with the highest priority (considering start time and
// size).
// Your task is to determine which classroom held the most classes throughout
// the day. If multiple classrooms are tied, return the one with the lowest
// number.
// Example:
// Example 1:
// Input: n = 2, classes = [[0, 10], [1, 5], [2, 7], [3, 4]]
// Output: 0
// Explanation:
// - At time 0, both classes are not being used. The first class starts in room
// 0.
// - At time 1, only room 1 is not being used. The second class starts in room
// 1.
// - At time 2, both rooms are being used. The third class is delayed.
// - At time 3, both rooms are being used. The fourth class is delayed.
// - At time 5, the class in room 1 finishes. The third class starts in room 1
// for the time period [5, 10).
// - At time 10, the classes in both rooms finish. The fourth class starts in
// room 0 for the time period [10,11). Both rooms 0 and 1 held 2 classes, so we
// return 0.
// Example 2:
// Input: n = 3, meetings = [[1, 20],[2,10],[3,5],[4,9],[6,8]]
// Output: 1
// Explanation:
// - At time 1, all three rooms are not being used. The first class starts in
// room 0.
// - At time 2, rooms 1 and 2 are not being used. The second class starts in
// room 1. - At time 3, only room 2 is not being used. The third class starts in
// room 2.
// - At time 4, all three rooms are being used. The fourth class is delayed.
// - At time 5, the class in room 2 finishes. The fourth class starts in room 2
// for the time period [5, 10).
// - At time 6, all three rooms are being used. The fifth class is delayed.
// - At time 10, the class in rooms 1 and 2 finish. The fifth class starts in
// room 1 for the time period [10, 12). Room 0 held 1 class while rooms 1 and 2
// each held 2 classes, so we return 1.

import java.util.*;

public class QuestionOneA {
    public static class Class {
        int start;
        int end;
        int size;

        public Class(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static int mostClassesRoom(int n, int[][] classes) {
        // Convert input array to list of Class objects
        List<Class> classList = new ArrayList<>();
        for (int[] c : classes) {
            classList.add(new Class(c[0], c[1]));
        }

        // Sort classes by start time, and by end time if start times are the same
        Collections.sort(classList, (a, b) -> a.start != b.start ? Integer.compare(a.start, b.start) : Integer.compare(a.end, b.end));

        // Min-heap to track room availability (end time, room index)
        PriorityQueue<int[]> roomHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int[] classCount = new int[n];

        // Initialize the heap with available rooms
        for (int i = 0; i < n; i++) {
            roomHeap.offer(new int[]{0, i}); // {end time, room index}
        }

        for (Class cls : classList) {
            int start = cls.start;
            int end = cls.end;

            // Get the earliest available room
            int[] earliestRoom = roomHeap.poll();
            int availableTime = earliestRoom[0];
            int roomIndex = earliestRoom[1];

            // If the room is available before the class start time, use it
            if (availableTime <= start) {
                earliestRoom[0] = end;
            } else { // Otherwise, delay the class until the room is free
                earliestRoom[0] = availableTime + (end - start);
            }

            // Increment the class count for the assigned room
            classCount[roomIndex]++;

            // Put the room back into the heap with the new end time
            roomHeap.offer(earliestRoom);
        }

        // Find the room with the maximum classes
        int maxClasses = 0;
        int roomIndex = 0;
        for (int i = 0; i < n; i++) {
            if (classCount[i] > maxClasses) {
                maxClasses = classCount[i];
                roomIndex = i;
            } else if (classCount[i] == maxClasses && i < roomIndex) {
                roomIndex = i;
            }
        }

        return roomIndex;
    }

    public static void main(String[] args) {
        int[][] classes1 = {{0, 10}, {1, 5}, {2, 7}, {3, 4}};
        int n1 = 2;
        System.out.println(mostClassesRoom(n1, classes1)); // Output: 0

        int[][] classes2 = {{1, 20}, {2, 10}, {3, 5}, {4, 9}, {6, 8}};
        int n2 = 3;
        System.out.println(mostClassesRoom(n2, classes2)); // Output: 1
    }
}

