package mine;

import java.util.*;

public class fees {

    private static final double TOTAL_FEES = 70000;
    private students studentModule;

    //  BST Node Definition 
    private static class FeeNode {
        String id;
        String name;
        double amountPaid;
        FeeNode left;
        FeeNode right;

        FeeNode(String id, String name, double amountPaid) {
            this.id = id;
            this.name = name;
            this.amountPaid = amountPaid;
            this.left = null;
            this.right = null;
        }
    }

    private FeeNode feeTreeRoot; // The root of the Binary Search Tree



    public fees(students studentModule) {
        this.studentModule = studentModule;
        // The tree is built when a report or status check is requested.
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n FEES ");
            System.out.println("1. Display Fee Status for All Students (Sorted)");
            System.out.println("2. Generate Fee Report");
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
                    displayFeeStatus();
                    break;
                case 2:
                    generateFeeReport();
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 3);
    }

    //  BST Helper Method: Insert
    private FeeNode insert(FeeNode root, String id, String name, double paid) {
        if (root == null) {
            return new FeeNode(id, name, paid);
        }

        // We use the 'amountPaid' as the sorting key for the BST
        if (paid < root.amountPaid) {
            root.left = insert(root.left, id, name, paid);
        } else {
            // Students with the same amount paid go to the right (or could be ID based)
            root.right = insert(root.right, id, name, paid);
        }
        return root;
    }

    // BST Helper Method: In-Order Traversal (Used for Display) 
    private void inOrderDisplay(FeeNode node) {
        if (node != null) {
            inOrderDisplay(node.left);

            String status = node.amountPaid >= TOTAL_FEES ? "Finished" : "Has Arrears (" + (TOTAL_FEES - node.amountPaid) + ")";
            System.out.println(node.id + " | " + node.name + " | Paid: " + node.amountPaid + " | " + status);

            inOrderDisplay(node.right);
        }
    }

    // BST Helper Method: Traversal for Report Generation 
    private void inOrderReport(FeeNode node, ReportData report) {
        if (node != null) {
            inOrderReport(node.left, report);

            report.totalCollected += node.amountPaid;
            if (node.amountPaid >= TOTAL_FEES) report.cleared++;
            else report.arrears++;
            report.totalStudents++;

            inOrderReport(node.right, report);
        }
    }

    // Helper class for report data accumulation
    private static class ReportData {
        double totalCollected = 0;
        int cleared = 0;
        int arrears = 0;
        int totalStudents = 0;
    }

    
    //  Public Methods (Modified to use BST) 

    private void buildFeeTree() {
        if (studentModule == null) {
            System.out.println("Student data not linked.");
            return;
        }

        List<Map<String, Object>> data = studentModule.getFeeData();
        feeTreeRoot = null; // Reset the tree

        if (data.isEmpty()) {
            return;
        }

        // Populate the BST
        for (Map<String, Object> entry : data) {
            String id = (String) entry.get("id");
            String name = (String) entry.get("name");
            double paid = (double) entry.get("amountPaid");
            feeTreeRoot = insert(feeTreeRoot, id, name, paid);
        }
    }

    public void displayFeeStatus() {
        buildFeeTree(); // Build the tree from scratch (to get up-to-date data)

        if (feeTreeRoot == null) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\nStudent Fee Status, Sorted by Amount Paid:");
        inOrderDisplay(feeTreeRoot);
    }

    public void generateFeeReport() {
        buildFeeTree();

        if (feeTreeRoot == null) {
            System.out.println("No student data to report.");
            return;
        }

        ReportData report = new ReportData();
        inOrderReport(feeTreeRoot, report);

        System.out.println("\nFEE REPORT");
        System.out.println("Total Students: " + report.totalStudents);
        System.out.println("Students Cleared: " + report.cleared);
        System.out.println("Students with Arrears: " + report.arrears);
        System.out.println("Total Collected: " + report.totalCollected);
        System.out.println("Outstanding: " + ((TOTAL_FEES * report.totalStudents) - report.totalCollected));
    }
}