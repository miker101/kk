package mine;

import java.util.*;

public class students {

    private static class Node {
        String id;
        String name;
        double amountPaid;
        Node next;

        Node(String id, String name, double amountPaid) {
            this.id = id;
            this.name = name;
            this.amountPaid = amountPaid;
            this.next = null;
        }
    }

    private Node head;

    public void run() {
        Scanner input = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("\nSTUDENT MANAGEMENT");
            System.out.println("1. Add Student");
            System.out.println("2. Search Student by ID");
            System.out.println("3. Delete Student by ID");
            System.out.println("4. Display All Students");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");

            if (!input.hasNextInt()) {
                System.out.println("Please enter valid  number.");
                input.nextLine();
                continue;
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    String id = input.nextLine().trim();
                    System.out.print("Enter Name: ");
                    String name = input.nextLine().trim();
                    System.out.print("Enter Amount Paid: ");
                    double amount = 0;
                    if (input.hasNextDouble()) {
                        amount = input.nextDouble();
                        input.nextLine();
                    } else {
                        System.out.println("Invalid amount.");//we default to 0
                        input.nextLine();
                    }
                    addStudent(id, name, amount);
                    break;
                case 2:
                    System.out.print("Enter Student ID to search: ");
                    searchStudent(input.nextLine().trim());
                    break;
                case 3:
                    System.out.print("Enter Student ID to delete: ");
                    deleteStudent(input.nextLine().trim());
                    break;
                case 4:
                    displayAllStudents();
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 5);
    }

    public void addStudent(String id, String name, double amount) {
        if (id == null || id.isEmpty()) {
            System.out.println("Invalid ID. Student not added.");
            return;
        }

        Node newNode = new Node(id, name, amount);
        if (head == null) {
            head = newNode;
        } else {
            Node cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = newNode;
        }
        System.out.println("Student added: " + id + " | " + name + " | Paid: " + amount);
    }

    public void searchStudent(String id) {
        Node cur = head;
        while (cur != null) {
            if (cur.id.equals(id)) {
                System.out.println("Found -> ID: " + cur.id + " | Name: " + cur.name + " | Paid: " + cur.amountPaid);
                return;
            }
            cur = cur.next;
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    public void deleteStudent(String id) {
        if (head == null) {
            System.out.println("No students to delete.");
            return;
        }

        if (head.id.equals(id)) {
            head = head.next;
            System.out.println("Student " + id + " deleted.");
            return;
        }

        Node cur = head;
        while (cur.next != null && !cur.next.id.equals(id)) {
            cur = cur.next;
        }

        if (cur.next == null) {
            System.out.println("Student with ID " + id + " not found.");
        } else {
            cur.next = cur.next.next;
            System.out.println("Student " + id + " deleted.");
        }
    }

    public void displayAllStudents() {
        if (head == null) {
            System.out.println(" No student data available.");
            return;
        }

        System.out.println("\nRegistered Students:");
        Node cur = head;
        int idx = 1;
        while (cur != null) {
            System.out.println(idx + ". ID: " + cur.id + " | Name: " + cur.name + " | Paid: " + cur.amountPaid);
            idx++;
            cur = cur.next;
        }
    }

    // === This gives fee info to Fee module ===
    public List<Map<String, Object>> getFeeData() {
        List<Map<String, Object>> data = new ArrayList<>();
        Node cur = head;
        while (cur != null) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", cur.id);
            entry.put("name", cur.name);
            entry.put("amountPaid", cur.amountPaid);
            data.add(entry);
            cur = cur.next;
        }
        return data;
    }

    public List<String> getAllStudentIDs() {
        List<String> ids = new ArrayList<>();
        Node cur = head;
        
        // Traverse the Linked List from the head (O(N) time)
        while (cur != null) {
            ids.add(cur.id);
            cur = cur.next;
        }
        
        return ids;
    }
}
