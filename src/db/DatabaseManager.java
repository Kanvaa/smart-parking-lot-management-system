package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:parking_lot.db";
    private Connection connection;

    public DatabaseManager() {
        try {
        	Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTableIfNotExists();
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }catch (ClassNotFoundException e) {
        	System.out.println("SQLitedriver not found" + e.getMessage());
        }
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "vehicle_number TEXT NOT NULL," +
                "vehicle_type TEXT NOT NULL," +
                "entry_time TEXT NOT NULL," +
                "exit_time TEXT NOT NULL," +
                "amount REAL NOT NULL" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void saveTransaction(String vehicleNumber, String vehicleType,
                                 LocalDateTime entryTime, LocalDateTime exitTime, double amount) {
        String sql = "INSERT INTO transactions (vehicle_number, vehicle_type, entry_time, exit_time, amount) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, vehicleNumber);
            ps.setString(2, vehicleType);
            ps.setString(3, entryTime.toString());
            ps.setString(4, exitTime.toString());
            ps.setDouble(5, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to save transaction: " + e.getMessage());
        }
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(amount) AS total FROM transactions";
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch revenue: " + e.getMessage());
        }
        return 0.0;
    }

    public void printAllTransactions() {
        String sql = "SELECT * FROM transactions ORDER BY id DESC";
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery(sql);
            System.out.println("---- TRANSACTION HISTORY ----");
            while (rs.next()) {
                System.out.printf("#%d | %s (%s) | Entry: %s | Exit: %s | Rs.%.2f%n",
                        rs.getInt("id"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_type"),
                        rs.getString("entry_time"),
                        rs.getString("exit_time"),
                        rs.getDouble("amount"));
            }
            System.out.println("------------------------------");
        } catch (SQLException e) {
            System.out.println("Failed to fetch transactions: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing DB connection: " + e.getMessage());
        }
    }
}
