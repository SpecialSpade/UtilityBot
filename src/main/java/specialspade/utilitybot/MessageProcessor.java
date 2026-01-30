package specialspade.utilitybot;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageProcessor implements MessageProcessorInterface{

    private HashMap<Long, String> userOptions = new HashMap<>();
    final String enterCity = "Input a city name!";
    final String enterNewTask = "Input a task to add!";
    final String enterTaskId = "Input ID of task for deletion!";




    public void process(Update update, AppInterface app){
        Message message = update.getMessage();
        long userId = message.getFrom().getId();
        app.writeToFile("From: " + message.getFrom().getUserName() + " at: " + new Date(message.getDate() * 1000L) + " Message: "
                + message.getText());

        if (!(message.getText().contains("Owner"))) {
            if (message.getText().contains("/weather")) {
                userOptions.put(userId, "wetter");
                app.setProperty("Weather started");
                app.sendMessage(app.createMessage(message, enterCity));
                return;
            } else if (message.getText().contains("/task_new")) {
                userOptions.put(userId, "newTask");
                app.setProperty("New task started");
                app.sendMessage(app.createMessage(message, enterNewTask));
                return;
            } else if (message.getText().contains("/task_delete")) {
                userOptions.put(userId, "deleteTask");
                app.setProperty("Task deletion started");
                app.sendMessage(app.createMessage(message, enterTaskId));
                return;
            } else if (message.getText().contains("/tasks_list")) {
                HashMap<Long, String> map = app.listTasks(userId);
                app.setProperty("List tasks");
                if (map == null || map.isEmpty()) {
                    app.sendMessage(app.createMessage(message, "No tasks"));
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Long, String> entry : map.entrySet()) {
                    sb.append(entry.getKey().toString() + ": " + entry.getValue() + "\n");
                }
                app.sendMessage(app.createMessage(message, sb.toString()));
                return;
            }
            if (userOptions.get(userId) == "wetter") {
                TownTemperatureData weatherData = app.getWeatherData(update);
                app.sendWeatherUpdate(weatherData, message);
                //getWeather(update);
                userOptions.remove(userId);
                return;
            } else if (userOptions.get(userId) == "newTask") {
                app.addToDb(userId, message.getText());
                userOptions.remove(userId);
                return;
            } else if (userOptions.get(userId) == "deleteTask") {
                try {
                    app.deleteTask(userId, Integer.parseInt(message.getText()));
                    return;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                app.deleteTask(userId, message.getText());
                userOptions.remove(userId);
                return;

            }
            app.setProperty(message.getText());

        } else if (update.getMessage().getText().contains("Owner")) {
            app.sendOwner(message);

        }


    }


}
