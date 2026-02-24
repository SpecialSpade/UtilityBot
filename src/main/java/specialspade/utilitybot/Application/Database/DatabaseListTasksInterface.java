package specialspade.utilitybot.Application.Database;

import java.util.HashMap;

public interface DatabaseListTasksInterface {
    HashMap<Long, String> listAllTasksUser(long id);

}
