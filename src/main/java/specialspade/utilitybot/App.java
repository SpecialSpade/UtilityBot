package specialspade.utilitybot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class App extends TelegramLongPollingBot implements AppInterface{

    private static String api_key = System.getenv("TELEGRAM_API_KEY");
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private String currentText = "";
    private String notFound = "Town not found";
    private boolean isActive = true;
    private HashMap<Long, String> userOptions = new HashMap<>();
    private DatabaseAccess db;
    private WeatherServiceInterface weatherService;
    private MessageProcessorInterface messageProcessor;

    public App(WeatherServiceInterface weatherService, MessageProcessorInterface messageProcessor) {
        super(api_key);
        db = new DatabaseAccess();
        this.weatherService = weatherService;
        this.messageProcessor = messageProcessor;

    }

    public static void main(String[] args) {
        System.out.println("Bot has started");
        try {
            App app = new App(new WeatherService(), new MessageProcessor());
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(app);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!isActive) {
            System.out.println("Bot is: " + isActive);
            return;
        }
        messageProcessor.process(update, this);

    }

    public void sendWeatherUpdate(TownTemperatureData weatherData, Message message) {
        if (weatherData != null) {
            SendMessage sm_2 = createMessage(message, "Town: " + weatherData.getTown() + " Temperature: "
                    + Double.toString(weatherData.getTemperature().getTemperature()));
            sendMessage(sm_2);
        } else {
            sendNotFound(message);
        }
    }

    @Override
    public String getBotUsername() {
        return "UtilityBot";
    }


    public void writeToFile(String content) {
        FileWriter fw;
        try {
            fw = new FileWriter("./log.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SendMessage createMessage(Message message, String text) {
        //SendMessage sm = new SendMessage().builder().chatId(message.getFrom().getId()).text(text).build();
        return SendMessage.builder().chatId(message.getFrom().getId()).text(text).build();
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotFound(Message message) {
        try {
            execute(createMessage(message, notFound));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Only for testing
    public void writeToFilePublic(String content){
        this.writeToFile(content);
    }



    public void sendOwner(Message message) {
        SendMessage sm = SendMessage.builder().chatId(message.getFrom().getId()).text("Martin D.").build();
        try {
            execute(sm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProperty(String text) {
        String oldValue = this.currentText;
        this.currentText = text;
        changes.firePropertyChange("New Text", oldValue, text);
    }

    public void addToDb(long id, String task){
        db.addToDb(id, task);
    }
    public void deleteTask(long id, int taskId) {
        db.deleteTask(id, taskId);
    }
    public void deleteTask(long id, String taskName){
        db.deleteTask(id, taskName);
    }
    public HashMap<Long, String> listTasks(long id){
        return db.listTasks(id);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    public void changeActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public TownTemperatureData getWeatherData(Update update) {
        return weatherService.getWeatherData(update);
    }
}