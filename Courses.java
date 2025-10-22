package mine;

import java.util.*;

public class Courses {

    private String[] availableCourses = {
        "Mathematics", "Java Programming", "Python", "Networks", "Artificial Intelligence"
    };

    // The maximum capacity constraint is conceptually part of the circular array/queue limits.
    private final int MAX_CAPACITY = 3; 

    // Data Structure: Course Name (Key) -> List of Assigned Student IDs (Value)
    // A LinkedHashMap is still required to manage allocations for multiple courses.
    private Map<String, List<String>> courseAllocations = new LinkedHashMap<>();

    private students studentModule; // shared reference

    public Courses(students studentModule) {
        this.studentModule = studentModule;
        // Initialization remains the same
        for (String course : availableCourses) {
            courseAllocations.put(course, new ArrayList<>());
        }
    }

    // --- (run() method is UNCHANGED as requested) ---
    public void run() {
        Scanner input = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n COURSE SCHEDULING ");
            System.out.println("1. Auto Assign Courses to Students");
            System.out.println("2. Display Course Allocations");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter choice: ");

            if (!input.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                input.nextLine();
                continue;
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    autoAssignCourses();
                    break;

                case 2:
                    displayAllocations();
                    break;

                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 3);
    }

    // Auto assign courses considering max capacity
    public void autoAssignCourses() {
        List<String> allStudents = (studentModule).getAllStudentIDs();

        if (allStudents == null || allStudents.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        // Reset old allocations
        for (String course : availableCourses) {
            courseAllocations.get(course).clear();
        }

        int assignedCount = 0;
        // Start index (conceptually the 'front' of the circular course array)
        int courseIndex = 0; 

        // Loop through all students who need assigning
        for (String id : allStudents) {
            boolean assigned = false;
            
            // This starting position is where the circular search begins for the student
            int startIndex = courseIndex; 

            // Inner loop searches circularly until a free slot is found or all courses are checked
            do {
                String currentCourse = availableCourses[courseIndex];
                List<String> currentList = courseAllocations.get(currentCourse);

                if (currentList.size() < MAX_CAPACITY) {
                    // Assignment SUCCESS!
                    currentList.add(id);
                    assigned = true;
                    assignedCount++;
                    
                    // Move the starting point for the next student assignment
                    // This implements the 'dequeue/move front' step of a queue/circular array
                    courseIndex = (courseIndex + 1) % availableCourses.length;
                    
                    break; // Exit the inner 'do-while' loop
                } else {
                    // Course full → move to the next course in the circle to check capacity
                    // This implements the circular traversal check
                    courseIndex = (courseIndex + 1) % availableCourses.length;
                }
                
            // The loop continues as long as we haven't found a spot AND we haven't looped back 
            // to the starting course index.
            } while (courseIndex != startIndex);

            if (!assigned) {
                System.out.println("Could not assign course for student " + id + " (all courses full)");
            }
            // If assigned, courseIndex is already incremented and ready for the next student.
        }

        System.out.println("Auto assignment complete. " + assignedCount + " students assigned to courses.");
    }

    // --- (displayAllocations() method is UNCHANGED) ---
    public void displayAllocations() {
        System.out.println("\nCOURSE ALLOCATIONS (Max per course: " + MAX_CAPACITY + ")");
        for (String course : availableCourses) {
            List<String> students = courseAllocations.get(course);
            System.out.println("\n" + course + " (" + students.size() + "/" + MAX_CAPACITY + "):");
            if (students.isEmpty()) {
                System.out.println("  - No students assigned");
            } else {
                for (String s : students) {
                    System.out.println("  • Student ID: " + s);
                }
            }
        }
    }
}