package specialspade.utilitybot.Application.Database;

import java.sql.*;
import java.util.HashMap;

public class DatabaseAccess {


    public void addToDb(long id, String task) {
        if (task == null) {
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:./tasks.db")) {
            ResultSet rs = connection.getMetaData().getTables(null, null, "tasks", null);
            String sql = "INSERT INTO tasks (userid, task) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Long.toString(id));
            preparedStatement.setString(2, task);
            if (rs.next()) {
                preparedStatement.executeUpdate();
            } else {
                String newDb = "CREATE TABLE tasks (userid INTEGER NOT NULL, "
                        + "task STRING NOT NULL, taskID INTEGER PRIMARY KEY AUTOINCREMENT)";
                preparedStatement = connection.prepareStatement(newDb);
                preparedStatement.execute();
                String insertNewTask = "INSERT INTO tasks (userid, task) VALUES (?,?)";
                preparedStatement = connection.prepareStatement(insertNewTask);
                preparedStatement.setString(1, Long.toString(id));
                preparedStatement.setString(2, task);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

    }

    public HashMap<Long, String> listTasks(long id) throws SQLException{
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tasks.db")){
            String sqlStatement = "SELECT * FROM tasks WHERE userid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, Long.toString(id));
            ResultSet rs = connection.getMetaData().getTables(null, null, "tasks", null);
            if (rs.next()) {
                ResultSet result = preparedStatement.executeQuery();
                HashMap<Long, String> resultMap = new HashMap<>();
                while (result.next()) {
                    resultMap.put(result.getLong("taskID"), result.getString("task"));
                }
                return resultMap;
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return null;
    }


    public int deleteTask(long id, int taskId) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tasks.db")){
            String sqlCommand = "DELETE FROM tasks WHERE userid = ? AND taskID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
            preparedStatement.setString(1, Long.toString(id));
            preparedStatement.setString(2, Integer.toString(taskId));
            ResultSet rs = connection.getMetaData().getTables(null, null, "tasks", null);
            if (rs.next()) {
                return preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return 0;
    }

    public int deleteTask(long id, String taskName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tasks.db")) {
            ResultSet rs = connection.getMetaData().getTables(null, null, "tasks", null);
            String sqlCommand = "DELETE FROM tasks WHERE userid = ? AND task = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
            preparedStatement.setString(1, Long.toString(id));
            preparedStatement.setString(2, taskName);
            if (rs.next()) {
                return preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return 0;
    }
}