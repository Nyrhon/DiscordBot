# DiscordBot

This project contains a Discord bot developed for personal use on several Discord guilds.
Functionality includes music / sound playback via YouTube, chat commands and mini games in chat.

## Features

Music commands:
| Command | Function |
| ------- | -------- |
| play x | Plays song or playlist x (url or search term). |
| pause | Pauses the music / sound playback. |
| queue | Returns a list of the upcoming 10 songs. |
| current | Shows information about the current song. |
| clear | Clears the queue. |
| leave | Stops playback and makes the bot leave the voice channel. |
| shuffle | Enables shuffle playback for the current queue. |
| skip | Skips the current song. |
| loop | Starts / stops looping of the current track. |


Game commands:
| Command | Function |
| ------- | -------- |

General commands:
| Command | Function |
| ------- | -------- |
| help | Returns a list of all commands and their descriptions. |
| purge x | Deletes the last x messages from the current channel. |
| stop | Stops the bot (only accessible by specified bot owner). |

## Configuration

The bot can be configured to allow only certain roles / people on a Discord to use commands.
By default the configuration commands can only be accessed by the guild owner.
Configuration commands:
| Command | Function |
| ------- | -------- |


## Self hosting

The bot can be self hosted to be used on your own Discord guild(s).
To use the bot simply build the project with
```
mvn install
```
in the root directory and run the JAR.
On first startup the application will create a config.properties file for configuration
purposes and will fail to authenticate since the Discord application token hasn't been set.

### Configuration

| Option | Description |
| ------ | ----------- |
| game | The "game" the bot will display as currently playing when started. |
| bot_owner | The bot owner Discord ID (used for specific commands like "stop"). |
| command_prefix | The default command prefix the bot should use. |
| bot_token | The token given to you by Discord for your developer application. |

## Built with

* [JDA](https://github.com/DV8FromTheWorld/JDA) - Discord API Wrapper
* [LavaPlayer](https://github.com/sedmelluq/lavaplayer) - Audio Player 
* [Maven](https://maven.apache.org/) - Dependency Management
* [Hibernate](http://hibernate.org/) - ORM Framework
* [H2](http://www.h2database.com/html/main.html) - Database
