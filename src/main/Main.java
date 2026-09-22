package main;

import dao.BookDAO;
import dao.MemberDAO;
import model.Book;
import model.Member;
import model.Searchable;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAO();
        MemberDAO memberDAO = new MemberDAO();

        try {
            bookDAO.createTable();
            memberDAO.createTable();

            String keyword = "hobbit";

            System.out.println("Searching books for: " + keyword);
            List<Book> books = bookDAO.getAllBooks();
            for (Book b : books) {
                if (b.matches(keyword)) {
                    System.out.println("Match: " + b.getTitle());
                }
            }

            String keyword2 = "bikash";
            System.out.println("\nSearching members for: " + keyword2);
            List<Member> members = memberDAO.getAllMembers();
            for (Member m : members) {
                if (m.matches(keyword2)) {
                    System.out.println("Match: " + m.getName());
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}