package testgroupz.utilitybot;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
    private static String weather_api_key = System.getenv("WEATHER_API_KEY"); // Weatherapi.com
    private StringBuilder url = new StringBuilder("https://api.weatherapi.com/v1/current.json?q=");
    private String notFound = "Stadt nicht gefunden";
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private String currentText = "";
    private boolean isActive;
    private HashMap<Long, String> userOptions = new HashMap<>();
    final String enterCity = "Geben sie die Stadt ein!";
    final String enterNewTask = "Geben sie eine neue Task ein!";
    final String enterTaskId = "Geben sie die TaskID zum Löschen ein!";
    private DatabaseAccess db;

    public App(String token) {
        super(token);
        db = new DatabaseAccess();

    }

    public static void main(String[] args) {
        System.out.println("Bot has started");
        try {
            App app = new App(api_key);
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(app);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!isActive) {
            System.out.println("Active: " + isActive);
            return;
        }
        Message message = update.getMessage();
        long userId = message.getFrom().getId();
        writeToFile("From: " + message.getFrom().getUserName() + " at: " + message.getDate() + " Message: "
                + message.getText());
        //System.out.println("User id: " + userId);

        if (!(message.getText().contains("Owner"))) {
            if (message.getText().contains("/wetter")) {
                userOptions.put(userId, "wetter");
                setProperty("Wetter gestartet");
                sendMessage(createMessage(message, enterCity));
                return;
            } else if (message.getText().contains("/task_new")) {
                userOptions.put(userId, "newTask");
                setProperty("Neue Task gestartet");
                sendMessage(createMessage(message, enterNewTask));
                return;
            } else if (message.getText().contains("/task_delete")) {
                userOptions.put(userId, "deleteTask");
                setProperty("Task löschen gestartet");
                sendMessage(createMessage(message, enterTaskId));
                return;
            } else if (message.getText().contains("/tasks_list")) {
                HashMap<Long, String> map = db.listTasks(userId);
                setProperty("Tasks auflisten");
                if (map == null || map.isEmpty()) {
                    sendMessage(createMessage(message, "Keine tasks"));
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Long, String> entry : map.entrySet()) {
                    sb.append(entry.getKey().toString() + ": " + entry.getValue() + "\n");
                }
                sendMessage(createMessage(message, sb.toString()));
                return;
            }
            if (userOptions.get(userId).equals("wetter")) {
                getWeather(update);
                userOptions.remove(userId);
                return;
            } else if (userOptions.get(userId).equals("newTask")) {
                db.addToDb(userId, message.getText());
                userOptions.remove(userId);
                return;
            } else if (userOptions.get(userId).equals("deleteTask")) {
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

            System.out.println(update.getMessage().getText());
            setProperty(message.getText());

        } else if (update.getMessage().getText().contains("Owner")) {
            sendOwner(message);

        }

    }

    @Override
    public String getBotUsername() {
        return "Bot";
    }

    public boolean sendOwner(Message message) {
        SendMessage sm = SendMessage.builder().chatId(message.getFrom().getId()).text("Martin D.").build();
        try {
            execute(sm);
            return true;
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return false;
        }
    }

    public TownTemperatureData connection(String url) {
        try {
            if (url != null) {
                URL urlWeather = new URI(url).toURL();
                HttpURLConnection con = (HttpURLConnection) urlWeather.openConnection();
                con.setRequestMethod("GET");
                // System.out.println("Response code: " + con.getResponseCode());
                if (con.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    // String line;
                    /*
                     * while(((line = reader.readLine()) != null)) { System.out.println("Line:" +
                     * line); if(line.contains("temp_c"));{ System.out.println("Temperatur: " +
                     * Double.parseDouble(line.split(":")[1])); break; } }
                     */

                    return extractData(reader);
                } else {
                    return null;
                }
            }
            return null;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
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
            System.out.println("Error:");
            e.printStackTrace();
        }
    }

    private void sendNotFound(Message message) {
        try {
            execute(createMessage(message, notFound));
        } catch (Exception e) {
            System.out.println("Error:");
            e.printStackTrace();
        }
    }

    private TownTemperatureData extractData(BufferedReader reader) {
        String[] lines;
        try {
            lines = reader.readLine().split("},");
            String tempLine = "";
            String townLine = "";
            for (String string : lines) {
                if (string.contains("temp_c")) {
                    tempLine = string;
                }
                if (string.contains("name")) {
                    townLine = string;
                }
            }
            String town = extractTown(townLine);
            Temperature temp = extractTemp(tempLine);
            return new TownTemperatureData(town, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Temperature extractTemp(String line) {
        if (line != null) {
            String[] splitLines = line.split(",");
            for (String string : splitLines) {
                if (string.contains("temp_c")) {
                    return new Temperature(Double.parseDouble(string.split(":")[1]));
                }
            }
        }
        return null;
    }

    private String extractTown(String line) {
        if (line != null) {
            String[] splitLines = line.split(",");
            for (String string : splitLines) {
                if (string.contains("name")) {
                    String town = string.split(":\"")[1].replaceAll("\"", "").replace(",", "");
                    return town;
                }
            }
        }
        return null;
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
        //System.out.println("isActive: " + isActive);
    }

    public void getWeather(Update update) {
        String inputTown = update.getMessage().getText().replaceAll("ä", "ae").replaceAll("ö", "oe")
                .replaceAll("ü", "ue").replaceAll(" ", "_");
        String tempUrl = url + inputTown.trim();
        tempUrl += "&key=" + weather_api_key;
        TownTemperatureData tempTownData = connection(tempUrl);
        Message message = update.getMessage();
        if (tempTownData != null) {
            SendMessage sm_2 = createMessage(message, "Stadt: " + tempTownData.getTown() + " Temperatur: "
                    + Double.toString(tempTownData.getTemperature().getTemperature()));
            sendMessage(sm_2);
        } else {
            sendNotFound(message);
        }

    }
}