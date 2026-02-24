package specialspade.utilitybot.Application.Message.Processing.Preprocessing;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessagePreprocessorInterface {
        void process(Update update);

}
