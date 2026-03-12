package specialspade.utilitybot.Application.Message.Processing.ProcessingTypes;

import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.Application.AppInterface;

import java.util.Map;

public interface ProcessorInterface {
    /**
     * Returns {@code true} if Processor can process update and {@code false} otherwise.
     * @param task Task description
     * @return Ability to process the update.
     */
    boolean canProcess(String task);

    /**
     * Processes the update with appropriate processor.
     * @param update        Update received from user.
     * @param app           AppInterface to process the update with.
     * @param userOptions   Options that the users chose before.
     * @return              An Object that has been the result of processing the update.
     */
    Object process(Update update, AppInterface app, Map<Long, String> userOptions);

}
