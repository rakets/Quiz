import javax.swing.*;
import java.awt.*;
import java.sql.*;

import javax.swing.SwingUtilities;

public class History extends JFrame {

    Connection connection;
    JTextArea historyTextArea;

    public History() {

        setTitle("History of Quiz");
        setSize(650, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        historyTextArea = new JTextArea();
        historyTextArea.setBackground(new Color(50, 50, 50));
        historyTextArea.setFont(new Font("Ink Free", Font.BOLD, 30));
        historyTextArea.setForeground(new Color(255, 255, 0));

        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        add(scrollPane);

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:quiz_database.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        loadHistoryData();

        setVisible(true);
    }

    private void loadHistoryData() {
        try {
            String selectQuery = "SELECT * FROM quiz_results";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                StringBuilder historyData = new StringBuilder();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    int score = resultSet.getInt("score");

                    historyData.append(id).append(") ").append(username).append(" , Score: ").append(score).append('%').append("\n");
                }
                historyTextArea.setText(historyData.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new History());
    }
}
