package mine;

import java.util.*;

public class Performance {

    private students studentModule; // shared student data
    
    // Core storage: Hash Map for quick lookup of a student's marks by ID
    private Map<String, double[]> performanceData = new LinkedHashMap<>();
    
    private final String[] subjects = {"Java", "Data Structures", "Databases", "Networks", "Web Development"};

    //  Data Structure for Ranking (Max Heap) 
    // A Min-Heap (default in Java) is reversed to act as a Max-Heap by comparing averages.
    private PriorityQueue<StudentAvg> rankingHeap = new PriorityQueue<>(
        (s1, s2) -> Double.compare(s2.average, s1.average) // Comparator for Max Heap (Higher average = Higher priority)
    );
   

    // Constructor
    public Performance(students studentModule) {
        this.studentModule = studentModule;
    }

    // Default constructor (for testing)
    public Performance() {}

    public void run() {
        Scanner input = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\nPERFORMANCE ANALYTICS");//using Max Heap
            System.out.println("1. Record Student Marks");
            System.out.println("2. View Individual Performance");
            System.out.println("3. View All Student Performance");
            System.out.println("4. Generate Performance Report");//Find top Student in 0(1)
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");

            if (!input.hasNextInt()) {
                System.out.println(" Please enter a number.");
                input.nextLine();
                continue;
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    recordMarks(input);
                    // Rebuild the ranking structure after new data is added
                    rebuildRankingHeap();
                    break;
                case 2:
                    viewStudentPerformance(input);
                    break;
                case 3:
                    displayAllPerformance();
                    break;
                case 4:
                    generateReport();
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println(" Invalid choice!");
            }

        } while (choice != 5);
    }

    // Record marks for a student
    private void recordMarks(Scanner input) {
        System.out.print("Enter Student ID: ");
        String studentId = input.nextLine().trim();

        if (!isStudentRegistered(studentId)) {
            System.out.println("Student not found. Register first in Student Module.");
            return;
        }

        double[] marks = new double[subjects.length];

        for (int i = 0; i < subjects.length; i++) {
            System.out.print("Enter marks for " + subjects[i] + ": ");
            while (!input.hasNextDouble()) {
                System.out.println("Invalid input. Enter numeric marks.");
                input.nextLine();
            }
            marks[i] = input.nextDouble();
            input.nextLine();
        }

        performanceData.put(studentId, marks);
        System.out.println("Marks recorded successfully for " + studentId);
    }

    // Helper method to rebuild the Max Heap (runs after new data is recorded)
    private void rebuildRankingHeap() {
        // Clear old rankings
        rankingHeap.clear();

        // Re-calculate and insert all current students into the Max Heap
        for (Map.Entry<String, double[]> entry : performanceData.entrySet()) {
            String id = entry.getKey();
            double avg = Arrays.stream(entry.getValue()).average().orElse(0);
            rankingHeap.offer(new StudentAvg(id, avg)); // offer = insert/push
        }
    }

    // View one student's marks
    private void viewStudentPerformance(Scanner input) {
        System.out.print("Enter Student ID: ");
        String id = input.nextLine().trim();

        if (!performanceData.containsKey(id)) {
            System.out.println("No performance data found for this student.");
            return;
        }

        double[] marks = performanceData.get(id);
        System.out.println("\n Performance for " + id + ":");
        double total = 0;
        for (int i = 0; i < subjects.length; i++) {
            System.out.println(subjects[i] + ": " + marks[i]);
            total += marks[i];
        }
        double avg = total / subjects.length;
        System.out.println("Average: " + String.format("%.2f", avg));
        System.out.println("Grade: " + getGrade(avg));
        System.out.println("Status: " + (avg >= 40 ? "Pass" : "Fail"));
    }

    // Display all student performances
    private void displayAllPerformance() {
        if (performanceData.isEmpty()) {
            System.out.println(" No performance data recorded yet.");
            return;
        }

        System.out.println("\nALL STUDENT PERFORMANCE (Unsorted):");
        for (Map.Entry<String, double[]> entry : performanceData.entrySet()) {
            String id = entry.getKey();
            double[] marks = entry.getValue();
            double avg = Arrays.stream(marks).average().orElse(0);
            System.out.println(id + " | Avg: " + String.format("%.2f", avg) + " | Grade: " + getGrade(avg));
        }
    }

    // Generate performance report - Now uses the Max Heap
    private void generateReport() {
        if (performanceData.isEmpty()) {
            System.out.println("No data to generate report.");
            return;
        }
        
        // Ensure the heap is up to date before querying the top student
        // This is necessary if report is called before marks are recorded in the current session
        if (rankingHeap.isEmpty() && !performanceData.isEmpty()) {
            rebuildRankingHeap();
        }

        double classTotal = 0;
        
        // Calculate class total by iterating the underlying data (O(N))
        for (double[] marks : performanceData.values()) {
            classTotal += Arrays.stream(marks).average().orElse(0);
        }

        double classAverage = classTotal / performanceData.size();

        // Retrieve top student in O(1) time using the Max Heap (peek)
        StudentAvg topStudent = rankingHeap.peek();
        String topStudentId = (topStudent != null) ? topStudent.studentId : "N/A";
        double topAvg = (topStudent != null) ? topStudent.average : 0;


        System.out.println("\nPERFORMANCE REPORT");
        System.out.println("Total Students with Records: " + performanceData.size());
        System.out.println("Class Average: " + String.format("%.2f", classAverage));
        // This is the O(1) lookup part:
        System.out.println("Top Student (via Max Heap): " + topStudentId + " (Avg: " + String.format("%.2f", topAvg) + ")");
        System.out.println("\nMax Heap used for O(1) top student lookup.");
    }

    // Check if a student is registered
    private boolean isStudentRegistered(String id) {
        if (studentModule == null) return false;
        List<Map<String, Object>> data = studentModule.getFeeData();
        for (Map<String, Object> entry : data) {
            if (entry.get("id").equals(id)) return true;
        }
        return false;
    }

    // Determine grade
    private String getGrade(double avg) {
        if (avg >= 70) return "A";
        else if (avg >= 60) return "B";
        else if (avg >= 50) return "C";
        else if (avg >= 40) return "D";
        else return "E";
    }

    //  Helper class to store student ID and average score for the Heap ---
    private static class StudentAvg {
        String studentId;
        double average;

        StudentAvg(String studentId, double average) {
            this.studentId = studentId;
            this.average = average;
        }
    }
}