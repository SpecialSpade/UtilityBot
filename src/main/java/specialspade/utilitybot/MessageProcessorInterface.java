package specialspade.utilitybot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface MessageProcessorInterface {
    void process(Update update, AppInterface app);
}
