## Introduction
This is a Telegram Bot written in Java. It offers a temperature query feature which uses <https://www.weatherapi.com/> to fetch data. It also features a basic To-Do-List. A log file is included and a database that manages all tasks. First initialization of the db can take a few seconds. <br/>
## How to use
Requires an API-Key from Telegram named "TELEGRAM_API_KEY" for the bot and a <https://www.weatherapi.com/> API-Key named "WEATHER_API_KEY" as enviromental variables.<br/>
Follow <https://core.telegram.org/bots/tutorial> to create your own bot and add commands to it.<br/>
To get current temperatures the user needs to send `/weather` first and then gets prompted to input a city.<br/>
The same goes for adding a new task with `/task_new` and deleting a task with `/task_delete`. If the user types in `/tasks_list` he gets all tasks that have been added by that user.<br/>
On Windows in a commandline: `set TELEGRAM_API_KEY={your_key}` followed by `set WEATHER_API_KEY={your_key}`.<br/> 
You can check by typing `set TELEGRAM_API_KEY` and `set WEATHER_API_KEY` if they are set.<br/>
Then you can run the jar with: `java -jar UtilityBot_version.jar`
