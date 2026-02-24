package specialspade.utilitybot.Application.Message.Processing;

import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.Application.AppInterface;
import specialspade.utilitybot.Application.Message.Processing.ProcessingTypes.ProcessorInterface;

import java.util.List;
import java.util.Map;

public class MessageProcessor{

    private final List<ProcessorInterface> processorTypes;

    public MessageProcessor(List<ProcessorInterface> processorTypes){
        this.processorTypes = processorTypes;
    }

    /***
     * Processes the update depending on the message type.
     * @param update Current update from the user
     * @param app Application context
     * @param userOptions The list of options of all users unless an immediate option was chosen.
     * @return Object
     */
    public Object processUpdate(Update update, AppInterface app, Map<Long, String> userOptions){
        ProcessorInterface type = findProcessorTypeImmediateResponse(update);
        if(type != null && userOptions.get(update.getMessage().getChatId()) == null){
            return type.process(update, app, userOptions);
        } else {
            type = findProcessorType(userOptions.get(update.getMessage().getChatId()));
            if(type != null)
                return type.process(update, app, userOptions);
        }
        return null;
    }

    private ProcessorInterface findProcessorTypeImmediateResponse(Update update){
        ProcessorInterface type = findProcessorType(update.getMessage().getText());
        return type;
    }

    private ProcessorInterface findProcessorType(String message){
        for(ProcessorInterface type: processorTypes){
            if(type.canProcess(MessageValidator.validateTypeAndNormalizeType(message))){
                return type;
            }
        }
        return null;
    }
}
