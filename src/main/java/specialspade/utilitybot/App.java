package specialspade.utilitybot;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App extends TelegramLongPollingBot {

    private static String api_key = System.getenv("TELEGRAM_API_KEY");
    private String notFound = "Town not found";
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private String currentText = "";
    private boolean isActive = true;
    private HashMap<Long, String> userOptions = new HashMap<>();
    final String enterCity = "Input a city name!";
    final String enterNewTask = "Input a task to add!";
    final String enterTaskId = "Input ID of task for deletion!";
    private DatabaseAccess db;
    private WeatherServiceInterface weatherService;

    public App(WeatherServiceInterface weatherService) {
        super(api_key);
        db = new DatabaseAccess();
        this.weatherService = weatherService;

    }

    public static void main(String[] args) {
        System.out.println("Bot has started");
        try {
            App app = new App(new WeatherService());
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
        Message message = update.getMessage();
        long userId = message.getFrom().getId();
        writeToFile("From: " + message.getFrom().getUserName() + " at: " + new Date(message.getDate() * 1000L) + " Message: "
                + message.getText());

        if (!(message.getText().contains("Owner"))) {
            if (message.getText().contains("/wetter")) {
                userOptions.put(userId, "wetter");
                setProperty("Weather started");
                sendMessage(createMessage(message, enterCity));
                return;
            } else if (message.getText().contains("/task_new")) {
                userOptions.put(userId, "newTask");
                setProperty("New task started");
                sendMessage(createMessage(message, enterNewTask));
                return;
            } else if (message.getText().contains("/task_delete")) {
                userOptions.put(userId, "deleteTask");
                setProperty("Task deletion started");
                sendMessage(createMessage(message, enterTaskId));
                return;
            } else if (message.getText().contains("/tasks_list")) {
                HashMap<Long, String> map = db.listTasks(userId);
                setProperty("List tasks");
                if (map == null || map.isEmpty()) {
                    sendMessage(createMessage(message, "No tasks"));
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Long, String> entry : map.entrySet()) {
                    sb.append(entry.getKey().toString() + ": " + entry.getValue() + "\n");
                }
                sendMessage(createMessage(message, sb.toString()));
                return;
            }
            if (userOptions.get(userId) == "wetter") {
                TownTemperatureData weatherData = weatherService.getWeatherData(update);
                sendWeatherUpdate(weatherData, message);
                //getWeather(update);
                userOptions.remove(userId);
                return;
            } else if (userOptions.get(userId) == "newTask") {
                db.addToDb(userId, message.getText());
                userOptions.remove(userId);
                return;
            } else if (userOptions.get(userId) == "deleteTask") {
                try {
                    db.deleteTask(userId, Integer.parseInt(message.getText()));
                    return;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                db.deleteTask(userId, message.getText());
                userOptions.remove(userId);
                return;

            }
            setProperty(message.getText());

        } else if (update.getMessage().getText().contains("Owner")) {
            sendOwner(message);

        }

    }

    private void sendWeatherUpdate(TownTemperatureData weatherData, Message message) {
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


    private static void writeToFile(String content) {
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

    private SendMessage createMessage(Message message, String text) {
        //SendMessage sm = new SendMessage().builder().chatId(message.getFrom().getId()).text(text).build();
        return SendMessage.builder().chatId(message.getFrom().getId()).text(text).build();
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotFound(Message message) {
        try {
            execute(createMessage(message, notFound));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public boolean sendOwner(Message message) {
        SendMessage sm = SendMessage.builder().chatId(message.getFrom().getId()).text("Martin D.").build();
        try {
            execute(sm);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setProperty(String text) {
        String oldValue = this.currentText;
        this.currentText = text;
        changes.firePropertyChange("New Text", oldValue, text);
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
}