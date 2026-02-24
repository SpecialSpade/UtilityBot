package specialspade.utilitybot.Application.Message.Processing.ProcessingTypes;

import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.Application.AppInterface;

import java.util.Map;

public class ListTasksProcessor implements ProcessorInterface {

    private final String processable = "tasklist";

    @Override
    public Object process(Update update, AppInterface app, Map<Long, String> userOptions) {
        Map<Long, String> result = app.listAllTasksUser(update.getMessage().getFrom().getId());
        return result;
    }

    @Override
    public boolean canProcess(String task) {
        return processable.equals(task);
    }

}
