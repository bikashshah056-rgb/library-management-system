package main;

import dao.BookDAO;
import model.Book;
import model.BookNotAvailableException;
import service.LibraryService;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAO();
        LibraryService libraryService = new LibraryService();

        try {
            bookDAO.createTable();

            // Try issuing "The Hobbit" (id 1) to member id 1
            libraryService.issueBook(1, 1);
            System.out.println("Book issued successfully!");

            // Check available copies afterward
            Book book = bookDAO.getBookById(1);
            System.out.println("Available copies now: " + book.getAvailableCopies());

        } catch (BookNotAvailableException e) {
            System.out.println("Cannot issue book: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}