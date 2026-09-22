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
}