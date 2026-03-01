package specialspade.utilitybot.Application.Database;

public interface DatabaseDeleteTaskInterface {
    int deleteTaskFromDb(long id, int taskId);
    int deleteTaskFromDb(long id, String taskName);
}
