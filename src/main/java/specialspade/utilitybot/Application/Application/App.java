package specialspade.utilitybot.Application.Application;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import specialspade.utilitybot.Application.Database.DatabaseAccess;
import specialspade.utilitybot.Application.Message.Processing.MessageProcessor;
import specialspade.utilitybot.Application.Message.Processing.MessageValidator;
import specialspade.utilitybot.Application.Message.Processing.Preprocessing.MessagePreprocessor;
import specialspade.utilitybot.Application.Message.Processing.ProcessingTypes.*;
import specialspade.utilitybot.Application.Message.MessageTypes.*;
import specialspade.utilitybot.Application.TemperatureData.TownTemperatureData;
import specialspade.utilitybot.Application.Weather.WeatherService;
import specialspade.utilitybot.Application.Weather.WeatherServiceInterface;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class App extends TelegramLongPollingBot implements AppInterface {

    private static final String api_key = System.getenv("TELEGRAM_API_KEY");
    private final PropertyChangeSupport propertyChanged = new PropertyChangeSupport(this);
    private String currentText = "";
    private boolean botIsActive = false;
    private final DatabaseAccess databaseAccess;
    private final WeatherServiceInterface weatherService;
    private final MessagePreprocessor preprocessor;

    public App() {
        super(api_key);
        databaseAccess = new DatabaseAccess();
        this.weatherService = new WeatherService();
        this.preprocessor = new MessagePreprocessor(this, initMessageValidator(), initMessageProcessorFactory(), initResponses());


    }

    public static void main(String[] args) {
        System.out.println("Bot has started");
        try {
            App app = new App();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(app);
        } catch (Exception e) {
            App app = new App();
            app.writeToLogFileException(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!botIsActive) {
            System.out.println("Bot is inactive.");
        } else {
            preprocessor.process(update);}
    }

    public void sendWeatherUpdate(TownTemperatureData weatherData, Message message) {
        if (weatherData != null) {
            SendMessage sm_2 = createMessageToSend(message, "Town: " + weatherData.getTown() + " Temperature: "
                    + weatherData.getTemperatureData());
            sendMessage(sm_2);
        } else {
            sendNotFound(message);
        }
    }

    @Override
    public String getBotUsername() {
        return "UtilityBot";
    }

    public void writeToLogFileFromUser(Message message) {
        try (FileWriter fileWriter = new FileWriter("./src/main/resources/log.txt", true)){
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String text = "From: " + message.getFrom().getUserName() + " at: " + createDate(message.getDate()) +
                    " Message: " + message.getText();
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            writeToLogFileException(e.getMessage());
        }
    }

    public void writeToLogFileToUser(String content, Message message){
        try (FileWriter fileWriter = new FileWriter("./src/main/resources/log.txt", true)){
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String text = "To: " + message.getFrom().getUserName() + " at: " + createDate(message.getDate()) +
                    " Message: " + content;
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            writeToLogFileException(e.getMessage());
        }
    }

    public void writeToLogFileTemperature(TownTemperatureData temperatureData, Message message){
        try (FileWriter fileWriter = new FileWriter("./src/main/resources/log.txt", true)){
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String text =  "To: " + message.getFrom().getUserName() + " at:  " + createDate(message.getDate()) +
                    " Town: " + temperatureData.getTown() + ". Temperature: " + temperatureData.getTemperatureData();
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            writeToLogFileException(e.getMessage());
        }
    }

    private void writeToLogFileException(String message){
        try (FileWriter fileWriter = new FileWriter("./src/main/resources/log.txt", true)){
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String text =  "Exception occured. " + message;
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            writeToLogFileException(e.getMessage());

        }
    }

    private Date createDate(int time){
        return new Date(time * 1000L);
    }


    public SendMessage createMessageToSend(Message message, String text) {
        //SendMessage sm = new SendMessage().builder().chatId(message.getFrom().getId()).text(text).build();
        return SendMessage.builder().chatId(message.getChatId()).text(text).build();
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (Exception e) {
            writeToLogFileException(e.getMessage());
        }
    }

    public void sendNotFound(Message message) {
        try {
            execute(createMessageToSend(message, "Town not found"));
        } catch (Exception e) {
            writeToLogFileException(e.getMessage());
        }
    }
    //Only for testing
    public void writeToFilePublic(Message message){
        this.writeToLogFileFromUser(message);
    }


    public void setProperty(String text) {
        String oldValue = this.currentText;
        this.currentText = text;
        propertyChanged.firePropertyChange("New Text", oldValue, text);
    }

    public void addToDb(long id, String task){
        databaseAccess.addToDb(id, task);
    }
    public int deleteTaskFromDb(long id, int taskId) {
        return databaseAccess.deleteTask(id, taskId);
    }
    public int deleteTaskFromDb(long id, String taskName){
        return databaseAccess.deleteTask(id, taskName);
    }
    public HashMap<Long, String> listAllTasksUser(long id){
        try{
            return databaseAccess.listTasks(id);
        } catch (SQLException e) {
            return new HashMap<Long, String>();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChanged.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChanged.removePropertyChangeListener(l);
    }

    public void changeActive(boolean active) {
        this.botIsActive = active;
    }

    @Override
    public TownTemperatureData getWeatherData(Update update) {
        return weatherService.getWeatherData(update);
    }

    private MessageProcessor initMessageProcessorFactory(){
        ArrayList<ProcessorInterface> messageProcessors = new ArrayList<>();
        messageProcessors.addAll(List.of(new WeatherProcessor(), new DeleteTaskProcessor(), new ListTasksProcessor(), new NewTaskProcessor()));
        return new MessageProcessor(messageProcessors);
    }

    private MessageValidator initMessageValidator(){
        ArrayList<UserMessage> messagesList = new ArrayList<>();
        messagesList.addAll(new ArrayList<>(List.of(TaskListMessage.userMessage(), WeatherRequestMessage.userMessage(), NewTaskMessage.userMessage(), DeleteTaskMessage.userMessage())));
        return new MessageValidator(messagesList);
    }

    private Map<String, String> initResponses(){
        HashMap<String, String> responses = new HashMap<>();
        responses.put("weather", "Input a city name!");
        responses.put("newtask", "Input a task to add!");
        responses.put("deletetask", "Input ID of task for deletion!");
        return responses;
    }
}