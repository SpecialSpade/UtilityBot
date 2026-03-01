package specialspade.utilitybot.Application.Message.Processing.ProcessingTypes;

import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.Application.AppInterface;

import java.util.Map;

public class DeleteTaskProcessor implements ProcessorInterface {
    private final String processable = "deletetask";

    @Override
    public Object process(Update update, AppInterface app, Map<Long, String> userOptions) {
        try{
            int toDelete = Integer.parseInt(update.getMessage().getText());
            int result = app.deleteTaskFromDb(update.getMessage().getChatId(), toDelete);
            return result == 0 ? "Task not deleted" :  "Task deleted";
        } catch (NumberFormatException e) {
            int result = app.deleteTaskFromDb(update.getMessage().getFrom().getId(), update.getMessage().getText());
            return result == 0 ? "Task not deleted" : "Task deleted";

        }

    }


    @Override
    public boolean canProcess(String task) {
        return processable.equals(task);
    }
}
