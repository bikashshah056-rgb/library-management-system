package dao;

import model.BorrowRecord;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS borrow_records (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "book_id INTEGER," +
                "member_id INTEGER," +
                "borrow_date TEXT," +
                "due_date TEXT," +
                "return_date TEXT," +
                "FOREIGN KEY (book_id) REFERENCES books(id)," +
                "FOREIGN KEY (member_id) REFERENCES members(id))";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addBorrowRecord(BorrowRecord record) throws SQLException {
        String sql = "INSERT INTO borrow_records (book_id, member_id, borrow_date, due_date, return_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, record.getBookId());
            ps.setInt(2, record.getMemberId());
            ps.setString(3, record.getBorrowDate());
            ps.setString(4, record.getDueDate());
            ps.setString(5, record.getReturnDate());

            ps.executeUpdate();
        }
    }

    public List<BorrowRecord> getAllBorrowRecords() throws SQLException {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(mapRowToRecord(rs));
            }
        }
        return records;
    }

    public List<BorrowRecord> getOverdueBooks() throws SQLException {
        List<BorrowRecord> overdue = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records WHERE return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                overdue.add(mapRowToRecord(rs));
            }
        }
        return overdue;
    }

    public BorrowRecord getBorrowRecordById(int recordId) throws SQLException {
        String sql = "SELECT * FROM borrow_records WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToRecord(rs);
                }
            }
        }
        return null;
    }

    public void markAsReturned(int recordId, String returnDate) throws SQLException {
        String sql = "UPDATE borrow_records SET return_date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, returnDate);
            ps.setInt(2, recordId);

            ps.executeUpdate();
        }
    }

    private BorrowRecord mapRowToRecord(ResultSet rs) throws SQLException {
        return new BorrowRecord(
                rs.getInt("id"),
                rs.getInt("book_id"),
                rs.getInt("member_id"),
                rs.getString("borrow_date"),
                rs.getString("due_date"),
                rs.getString("return_date")
        );
    }
}