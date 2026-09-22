package main;

import dao.BookDAO;
import dao.MemberDAO;
import model.Book;
import model.Member;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAO();
        MemberDAO memberDAO = new MemberDAO();

        try {
            bookDAO.createTable();
            memberDAO.createTable();


            Member member1 = new Member(0, "Bikash Shah", "bikash@example.com", "9800000000");
            memberDAO.addMember(member1);
            System.out.println("Member added successfully!");


            System.out.println("\nAll members:");
            List<Member> members = memberDAO.getAllMembers();
            for (Member m : members) {
                System.out.println(m.getId() + " - " + m.getName() + " (" + m.getEmail() + ")");
            }


            System.out.println("\nAll books:");
            List<Book> books = bookDAO.getAllBooks();
            for (Book b : books) {
                System.out.println(b.getId() + " - " + b.getTitle());
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}