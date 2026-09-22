package main;

import dao.BookDAO;
import dao.MemberDAO;
import dao.BorrowDAO;
import model.Book;
import model.Member;
import model.BorrowRecord;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAO();
        MemberDAO memberDAO = new MemberDAO();
        BorrowDAO borrowDAO = new BorrowDAO();

        try {
            bookDAO.createTable();
            memberDAO.createTable();
            borrowDAO.createTable();


            BorrowRecord record = new BorrowRecord(0, 1, 1, "2026-09-22", "2026-10-06", null);
            borrowDAO.addBorrowRecord(record);
            System.out.println("Borrow record added!");


            System.out.println("\nCurrently borrowed (not yet returned):");
            List<BorrowRecord> overdue = borrowDAO.getOverdueBooks();
            for (BorrowRecord r : overdue) {
                System.out.println("Record " + r.getId() + " - Book ID " + r.getBookId() +
                        ", Member ID " + r.getMemberId() + ", Due: " + r.getDueDate());
            }


            System.out.println("\nAll borrow records:");
            List<BorrowRecord> all = borrowDAO.getAllBorrowRecords();
            for (BorrowRecord r : all) {
                System.out.println(r.getId() + " - Book " + r.getBookId() + ", Member " + r.getMemberId() +
                        ", Returned: " + r.getReturnDate());
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}