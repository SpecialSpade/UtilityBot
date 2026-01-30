package specialspade.utilitybot;

import java.util.HashMap;

public interface DatabankAccessInterface {
    void addToDb(long id, String task);
    void deleteTask(long id, int taskId);
    void deleteTask(long id, String taskName);
    HashMap<Long, String> listTasks(long id);
}
