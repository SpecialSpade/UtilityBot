package specialspade.utilitybot.Application.Message.Processing.Preprocessing;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessagePreprocessorInterface {
        /**
         * Processes the update received.
         * @param update Update from the user.
         */
        void process(Update update);

}
