package main;

import dao.BookDAO;
import model.Book;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAO();

        try {
            bookDAO.createTable();


            Book found = bookDAO.getBookById(1);
            if (found != null) {
                System.out.println("Found book: " + found.getTitle() + " (available: " + found.getAvailableCopies() + ")");


                found.setAvailableCopies(found.getAvailableCopies() - 1);
                bookDAO.updateBook(found);
                System.out.println("Updated available copies to: " + found.getAvailableCopies());
            } else {
                System.out.println("No book with id 1 found.");
            }


            System.out.println("\nAll books now:");
            List<Book> books = bookDAO.getAllBooks();
            for (Book b : books) {
                System.out.println(b.getId() + " - " + b.getTitle() + " (available: " + b.getAvailableCopies() + ")");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}