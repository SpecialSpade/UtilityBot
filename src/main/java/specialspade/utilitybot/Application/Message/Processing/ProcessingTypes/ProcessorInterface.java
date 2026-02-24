package specialspade.utilitybot.Application.Message.Processing.ProcessingTypes;

import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.Application.AppInterface;

import java.util.Map;

public interface ProcessorInterface {
    boolean canProcess(String task);
    Object process(Update update, AppInterface app, Map<Long, String> userOptions);

}
