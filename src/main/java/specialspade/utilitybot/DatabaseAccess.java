package specialspade.utilitybot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DatabaseAccess {


    public void addToDb(long id, String task) {
        if (task == null) {
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:./tasks.db");
             Statement statement = connection.createStatement()) {
            ResultSet rs = connection.getMetaData().getTables(null, null, "tasks", null);
            if (rs.next()) {
                String sql = "INSERT INTO tasks (userid, task) VALUES (" + id + ", \"" + task + "\");";
                statement.executeUpdate(sql);
            } else {
                statement.executeUpdate("CREATE TABLE tasks (userid INTEGER NOT NULL, "
                        + "task STRING NOT NULL, taskID INTEGER PRIMARY KEY AUTOINCREMENT)");
                statement.execute("INSERT INTO tasks (userid, task) VALUES (" + id + ", " + task);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

    }

    public HashMap<Long, String> listTasks(long id) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tasks.db");
             Statement statement = connection.createStatement()) {
            ResultSet rs = connection.getMetaData().getTables(null, null, "tasks", null);
            if (rs.next()) {
                statement.execute("SELECT * FROM tasks WHERE userid = " + id);
                ResultSet result = statement.executeQuery("SELECT * FROM tasks WHERE userid = " + id);
                HashMap<Long, String> resultMap = new HashMap<>();
                while (result.next()) {
                    resultMap.put(result.getLong("taskID"), result.getString("task"));
                }
                return resultMap;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public void deleteTask(long id, int taskId) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tasks.db");
             Statement statement = connection.createStatement()) {
            ResultSet rs = connection.getMetaData().getTables(null, null, "tasks", null);
            if (rs.next()) {
                String sql = "DELETE FROM tasks WHERE userid = " + id + " AND taskID = " + taskId + ";";
                statement.executeUpdate(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void deleteTask(long id, String taskName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tasks.db");
             Statement statement = connection.createStatement()) {
            ResultSet rs = connection.getMetaData().getTables(null, null, "tasks", null);
            if (rs.next()) {
                String sql = "DELETE FROM tasks WHERE userid = " + id + " AND task = \"" + taskName + "\";";
                statement.executeUpdate(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
}