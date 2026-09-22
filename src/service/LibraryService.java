package service;

import dao.BookDAO;
import dao.BorrowDAO;
import model.Book;
import model.BookNotAvailableException;
import model.BorrowRecord;

import java.sql.SQLException;
import java.time.LocalDate;

public class LibraryService {
    private BookDAO bookDAO = new BookDAO();
    private BorrowDAO borrowDAO = new BorrowDAO();

    public void issueBook(int bookId, int memberId) throws SQLException, BookNotAvailableException {
        Book book = bookDAO.getBookById(bookId);

        if (book == null) {
            throw new BookNotAvailableException("No book found with id " + bookId);
        }

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("'" + book.getTitle() + "' has no available copies right now.");
        }


        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDAO.updateBook(book);


        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(14);

        BorrowRecord record = new BorrowRecord(0, bookId, memberId, today.toString(), dueDate.toString(), null);
        borrowDAO.addBorrowRecord(record);
    }
}

