package testgroupz.utilitybot;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserInterface extends Application implements PropertyChangeListener {
    private static final String api_key = "8513567387:AAG6RKhUrHVnl_4Ep4lZTRzVzhxpXK0pbb0";
    private App app = new App(api_key);
    private boolean isActive = false;
    private boolean initiated = false;
    TextArea textArea = new TextArea();

    public static void main(String[] args) {
        launch(UserInterface.class);
    }

    @Override
    public void start(Stage window) {
        if (!initiated) {
            textArea.setEditable(false);
            try {
                textArea.appendText("Start registration\n");
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(app);
                textArea.appendText("Registration successful\n");
                initiated = true;

            } catch (TelegramApiException e) {
                System.out.println("Registration failed!");
            }
        }
        textArea.appendText("UI gestartet\n");
        Button buttonStart = new Button("Bot starten");
        buttonStart.setPrefWidth(100);
        Button buttonEnd = new Button("Bot beenden");
        buttonEnd.setPrefWidth(100);
        VBox buttons = new VBox();
        buttons.setStyle("-fx-background-color: #FF0000");
        buttons.setSpacing(20);
        buttons.getChildren().addAll(buttonStart, buttonEnd);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPrefSize(300, 100);
        BorderPane layout = new BorderPane();
        layout.setPrefSize(300, 600);
        layout.setTop(buttons);
        layout.setCenter(textArea);
        buttonStart.setOnAction((event) -> {
            if (!isActive) {
                app.addPropertyChangeListener(this);
                buttons.setStyle("-fx-background-color: #00FF00");
                isActive = true;
                app.changeActive(true);
            }
        });
        buttonEnd.setOnAction((event) -> {
            if (isActive) {
                app.removePropertyChangeListener(this);
                buttons.setStyle("-fx-background-color: #FF0000");
                isActive = false;
                app.changeActive(false);
            }
        });
        Scene scene = new Scene(layout);
        window.setOnCloseRequest((event) -> {
                    Platform.exit();
                    System.exit(0);
                }
        );
        window.setScene(scene);
        window.show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        textArea.appendText(evt.getNewValue() + "\n");
    }

}