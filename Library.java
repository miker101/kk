package mine;

import java.util.*;

public class Library {

    // HashMap: ISBN → Book Object
    private Map<String, Book> books = new HashMap<>();

    // Stack: track borrow and return history
    private Stack<String> history = new Stack<>();

    private students studentModule; // shared student data

    // Constructor to connect student module
    public Library(students studentModule) {
        this.studentModule = studentModule;
        preloadBooks(); // Add some books initially
    }

    // Default constructor (for testing, can be removed if not needed)
    public Library() {
        preloadBooks();
    }

    // books in the system
    private void preloadBooks() {
        books.put("9780134685991", new Book("Effective Java", "Joshua Bloch"));
        books.put("9780596009205", new Book("Head First Java", "Kathy Sierra"));
        books.put("9780132350884", new Book("Clean Code", "Robert C. Martin"));
        books.put("9780134494166", new Book("Core Java Volume I", "Cay Horstmann"));
        books.put("9780596007126", new Book("Java in a Nutshell", "David Flanagan"));
        books.put("9780131872486", new Book("Java How to Program", "Paul Deitel"));
        books.put("9781617294945", new Book("Spring in Action", "Craig Walls"));
        books.put("9780136019701", new Book("Introduction to Algorithms", "Thomas H. Cormen"));
        books.put("9781492056270", new Book("Learning Java", "Patrick Niemeyer"));
        books.put("9780596007737", new Book("Java Cookbook", "Ian F. Darwin"));
        books.put("9780134177304", new Book("Android Programming: The Big Nerd Ranch Guide", "Bill Phillips"));
        books.put("9781491900864", new Book("Head First Design Patterns", "Eric Freeman"));
        books.put("9780134694726", new Book("Core Java Volume II - Advanced Features", "Cay Horstmann"));
        books.put("9780134757599", new Book("Effective Modern Java", "Joshua Bloch"));
        books.put("9780321356680", new Book("Refactoring: Improving the Design of Existing Code", "Martin Fowler"));
        books.put("9781492072508", new Book("Programming Kotlin", "Venkat Subramaniam"));
        books.put("9781617294944", new Book("Spring Boot in Action", "Craig Walls"));
        books.put("9780133069983", new Book("Data Structures and Algorithms in Java", "Michael T. Goodrich"));
        books.put("9780134802213", new Book("Java Concurrency in Practice", "Brian Goetz"));
        books.put("9780137081073", new Book("Test-Driven Development by Example", "Kent Beck"));
    }


    public void run() {
        Scanner input = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n--- LIBRARY MANAGEMENT ---");
            System.out.println("1. Borrow a Book");
            System.out.println("2. Return a Book");
            System.out.println("3. Show Available Books");
            System.out.println("4. Show Borrowed Books");
            System.out.println("5. Show Borrow/Return History");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            if (!input.hasNextInt()) {
                System.out.println("⚠️ Please enter a number.");
                input.nextLine();
                continue;
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    borrowBook(input);
                    break;
                case 2:
                    returnBook(input);
                    break;
                case 3:
                    showAvailableBooks();
                    break;
                case 4:
                    showBorrowedBooks();
                    break;
                case 5:
                    showHistory();
                    break;
                case 6:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 6);
    }

    // Borrow book
    private void borrowBook(Scanner input) {
        System.out.print("Enter Student ID: ");
        String studentId = input.nextLine().trim();

        if (!isStudentRegistered(studentId)) {
            System.out.println("Student not found. Register first in Student Module.");
            return;
        }

        System.out.print("Enter Book ISBN to Borrow: ");
        String isbn = input.nextLine().trim();

        if (!books.containsKey(isbn)) {
            System.out.println("Book not found in library.");
            return;
        }

        Book book = books.get(isbn);
        if (book.isBorrowed) {
            System.out.println("Book already borrowed by " + book.borrowedBy);
        } else {
            book.isBorrowed = true;
            book.borrowedBy = studentId;
            history.push("Borrowed: " + book.title + " by " + studentId);
            System.out.println("Book '" + book.title + "' successfully borrowed by " + studentId);
        }
    }

    // Return book
    private void returnBook(Scanner input) {
        System.out.print("Enter Book ISBN to Return: ");
        String isbn = input.nextLine().trim();

        if (!books.containsKey(isbn)) {
            System.out.println(" Book not found in library.");
            return;
        }

        Book book = books.get(isbn);
        if (!book.isBorrowed) {
            System.out.println("Book is already available in the library.");
        } else {
            history.push("Returned: " + book.title + " by " + book.borrowedBy);
            System.out.println(" Book '" + book.title + "' returned by " + book.borrowedBy);
            book.isBorrowed = false;
            book.borrowedBy = null;
        }
    }

    // Show all available books
    private void showAvailableBooks() {
        System.out.println("\n AVAILABLE BOOKS:");
        for (Book book : books.values()) {
            if (!book.isBorrowed) {
                System.out.println(book);
            }
        }
    }

    // 📕 Show borrowed books and who borrowed them
    private void showBorrowedBooks() {
        System.out.println("\n BORROWED BOOKS:");
        boolean found = false;
        for (Book book : books.values()) {
            if (book.isBorrowed) {
                System.out.println(book + " → Borrowed by: " + book.borrowedBy);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No borrowed books right now.");
        }
    }

    // Show recent borrow/return history (Stack)
    private void showHistory() {
        System.out.println("\n📜 BORROW/RETURN HISTORY:");
        if (history.isEmpty()) {
            System.out.println("No activity yet.");
        } else {
            Stack<String> temp = (Stack<String>) history.clone();
            while (!temp.isEmpty()) {
                System.out.println(temp.pop());
            }
        }
    }

    // Check if a student exists in the student module
    private boolean isStudentRegistered(String id) {
        if (studentModule == null) return false;
        List<Map<String, Object>> data = studentModule.getFeeData(); // reuse to get IDs
        for (Map<String, Object> entry : data) {
            if (entry.get("id").equals(id)) return true;
        }
        return false;
    }

    // Inner Book class 
    private static class Book {
        String isbn;
        String title;
        String author;
        boolean isBorrowed;
        String borrowedBy;

        Book(String title, String author) {
            this.title = title;
            this.author = author;
            this.isBorrowed = false;
        }

        @Override
        public String toString() {
            return title + " by " + author;
        }
    }
}
