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

            List<Book> books = bookDAO.getAllBooks();
            System.out.println("Books currently in the database:");
            for (Book b : books) {
                System.out.println(b.getId() + " - " + b.getTitle() + " by " + b.getAuthor());
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}