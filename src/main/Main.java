package main;

import dao.BookDAO;
import dao.MemberDAO;
import dao.BorrowDAO;
import model.Book;
import model.Member;
import model.BorrowRecord;
import model.BookNotAvailableException;
import service.LibraryService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static BookDAO bookDAO = new BookDAO();
    static MemberDAO memberDAO = new MemberDAO();
    static BorrowDAO borrowDAO = new BorrowDAO();
    static LibraryService libraryService = new LibraryService();

    public static void main(String[] args) {
        try {
            bookDAO.createTable();
            memberDAO.createTable();
            borrowDAO.createTable();
        } catch (SQLException e) {
            System.out.println("Failed to set up database: " + e.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getValidIntInput();

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewAllBooks();
                case 3 -> addMember();
                case 4 -> viewAllMembers();
                case 5 -> issueBook();
                case 6 -> returnBook();
                case 7 -> searchBooks();
                case 8 -> viewOverdueBooks();
                case 9 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    static void printMenu() {
        System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
        System.out.println("1. Add Book");
        System.out.println("2. View All Books");
        System.out.println("3. Add Member");
        System.out.println("4. View All Members");
        System.out.println("5. Issue Book");
        System.out.println("6. Return Book");
        System.out.println("7. Search Books");
        System.out.println("8. View Overdue/Borrowed Books");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    static int getValidIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    static void addBook() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter total copies: ");
        int totalCopies = getValidIntInput();

        Book book = new Book(0, title, author, isbn, category, totalCopies, totalCopies);

        try {
            bookDAO.addBook(book);
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to add book: " + e.getMessage());
        }
    }

    static void viewAllBooks() {
        try {
            List<Book> books = bookDAO.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("No books in the library yet.");
                return;
            }
            System.out.println("\n--- All Books ---");
            for (Book b : books) {
                System.out.println(b.getId() + ". " + b.getTitle() + " by " + b.getAuthor() +
                        " | ISBN: " + b.getIsbn() + " | Available: " + b.getAvailableCopies() + "/" + b.getTotalCopies());
            }
        } catch (SQLException e) {
            System.out.println("Failed to load books: " + e.getMessage());
        }
    }

    static void addMember() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        Member member = new Member(0, name, email, phone);

        try {
            memberDAO.addMember(member);
            System.out.println("Member added successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to add member: " + e.getMessage());
        }
    }

    static void viewAllMembers() {
        try {
            List<Member> members = memberDAO.getAllMembers();
            if (members.isEmpty()) {
                System.out.println("No members registered yet.");
                return;
            }
            System.out.println("\n--- All Members ---");
            for (Member m : members) {
                System.out.println(m.getId() + ". " + m.getName() + " | " + m.getEmail() + " | " + m.getPhone());
            }
        } catch (SQLException e) {
            System.out.println("Failed to load members: " + e.getMessage());
        }
    }

    static void issueBook() {
        System.out.print("Enter Book ID: ");
        int bookId = getValidIntInput();

        System.out.print("Enter Member ID: ");
        int memberId = getValidIntInput();

        try {
            libraryService.issueBook(bookId, memberId);
            System.out.println("Book issued successfully! Due in 14 days.");
        } catch (BookNotAvailableException e) {
            System.out.println("Cannot issue book: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static void returnBook() {
        System.out.print("Enter Borrow Record ID: ");
        int recordId = getValidIntInput();

        try {
            String today = LocalDate.now().toString();
            borrowDAO.markAsReturned(recordId, today);
            System.out.println("Book marked as returned.");
        } catch (SQLException e) {
            System.out.println("Failed to return book: " + e.getMessage());
        }
    }

    static void searchBooks() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();

        try {
            List<Book> books = bookDAO.getAllBooks();
            System.out.println("\n--- Search Results ---");
            boolean found = false;
            for (Book b : books) {
                if (b.matches(keyword)) {
                    System.out.println(b.getId() + ". " + b.getTitle() + " by " + b.getAuthor());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No books matched your search.");
            }
        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        }
    }

    static void viewOverdueBooks() {
        try {
            List<BorrowRecord> records = borrowDAO.getOverdueBooks();
            if (records.isEmpty()) {
                System.out.println("No books are currently borrowed.");
                return;
            }
            System.out.println("\n--- Currently Borrowed / Overdue Books ---");
            for (BorrowRecord r : records) {
                System.out.println("Record " + r.getId() + " | Book ID: " + r.getBookId() +
                        " | Member ID: " + r.getMemberId() + " | Due: " + r.getDueDate());
            }
        } catch (SQLException e) {
            System.out.println("Failed to load records: " + e.getMessage());
        }
    }
}