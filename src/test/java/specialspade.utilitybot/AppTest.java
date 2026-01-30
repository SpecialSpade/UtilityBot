import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import specialspade.utilitybot.App;
import specialspade.utilitybot.MessageProcessor;
import specialspade.utilitybot.WeatherService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AppTest {

    App app;

    @BeforeEach
    public void init(){
        this.app = new App(new WeatherService(), new MessageProcessor());
    }


    @Test
    public void writeToFile(){
        app.writeToFilePublic("TestContent");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./log.txt"));
            String line = reader.readLine();
            String lastLine = "";
            while(line != null){
                lastLine = line;
                line = reader.readLine();
            }
            assertEquals("TestContent", lastLine);

            reader.close();
        } catch (IOException e){

        }
    }
    
}
