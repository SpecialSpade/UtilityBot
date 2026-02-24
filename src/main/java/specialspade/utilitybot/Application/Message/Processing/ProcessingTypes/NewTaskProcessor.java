package specialspade.utilitybot.Application.Message.Processing.ProcessingTypes;

import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.Application.AppInterface;

import java.util.Map;

public class NewTaskProcessor implements ProcessorInterface {

    private final String processable = "newtask";

    @Override
    public Object process(Update update, AppInterface app, Map<Long, String> userOptions){
        Long userId = update.getMessage().getFrom().getId();
        String task = userOptions.get(userId);
        if(task.equals(processable))
            app.addToDb(userId, update.getMessage().getText());
        return "Task added";
    }

    @Override
    public boolean canProcess(String task) {
        return processable.equals(task);
    }


}
