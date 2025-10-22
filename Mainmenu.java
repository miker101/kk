package mine;

import java.util.Scanner;

public class Mainmenu {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice = 0;

        // ✅ Create ONE shared instance of each module
        students studentModule = new students();
		Courses courseModule = new Courses(studentModule); // share student data
        fees feeModule = new fees(studentModule);           // now linked to student data
        Library libraryModule = new Library(studentModule);
        Performance performanceModule = new Performance(studentModule);

        do {
            System.out.println("\n");
            System.out.println("     SCHOOL MANAGEMENT SYSTEM");
            System.out.println("");
            System.out.println("1. Student Management");
            System.out.println("2. Courses");
            System.out.println("3. Fee Tracking");
            System.out.println("4. Library System");
            System.out.println("5. Performance Analytics");
            System.out.println("6. Exit");
            System.out.println("");
            System.out.print("Enter your choice: ");
            
            if (!input.hasNextInt()) {
                System.out.println(" Please enter a valid number.");
                input.nextLine();
                continue;
            }

            choice = input.nextInt();
            input.nextLine(); // clear the buffer

            switch (choice) {
                case 1:
                    studentModule.run();
                    break;

                case 2:
                    courseModule.run();
                    break;

                case 3:
                    feeModule.run();
                    break;

                case 4:
                    libraryModule.run();
                    break;

                case 5:
                    performanceModule.run();
                    break;

                case 6:
                    System.out.println(" Exiting System... Goodbye!");
                    break;

                default:
                    System.out.println(" Invalid choice! Try again.");
            }

        } while (choice != 6);

        input.close();
    }
}
