import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import specialspade.utilitybot.Application.Application.App;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;


public class AppTest {

    App app;

    @BeforeEach
    public void init(){
        this.app = new App();
    }


    @Test
    public void writeToFile(){
        app.writeToFilePublic(mock(Message.class));
        try {
            ArrayList<String> content = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/log.txt"));
            String line = reader.readLine();
            content.add(line);
            String lastLine = "";
            while(line != null){
                lastLine = line;
                line = reader.readLine();
                if(line != null && !line.equals("TestContent"))
                    content.add(line);
            }
            assertEquals("TestContent", lastLine);
            FileWriter fileWriter = new FileWriter("./src/main/resources/log.txt");
            content.forEach(currentLine -> {
                try {
                    fileWriter.write(currentLine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fileWriter.close();
            reader.close();
        } catch (IOException e){
            e.printStackTrace();

        }
    }
    
}
