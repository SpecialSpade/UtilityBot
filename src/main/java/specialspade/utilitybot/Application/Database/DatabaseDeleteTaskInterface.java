package specialspade.utilitybot.Application.Database;

public interface DatabaseDeleteTaskInterface {
    void deleteTaskFromDb(long id, int taskId);
    void deleteTaskFromDb(long id, String taskName);
}
