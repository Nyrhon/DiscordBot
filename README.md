# DiscordBot

This project contains a Discord bot developed for personal use on several Discord guilds.
Functionality includes music / sound playback via YouTube, chat commands and mini games in chat.

## Features

Music commands:

| Command | Function |
| ------- | -------- |
| play \<x\> | Plays song or playlist x (url or search term). |
| pause | Pauses the music / sound playback. |
| queue | Returns a list of the upcoming 10 songs. |
| current | Shows information about the current song. |
| clear | Clears the queue. |
| leave | Stops playback and makes the bot leave the voice channel. |
| shuffle | Enables shuffle playback for the current queue. |
| skip \<x\> | Skips the current song or x amount of songs. |
| loop | Starts / stops looping of the current track. |


Game commands:

| Command | Function |
| ------- | -------- |
| invite \<x\> \<y\> | Invites person y to a game of type x. Current game types: 0 - Connect Four |
| accept | Accepts a pending invitation. |
| decline | Declines a pending game invitation. |
| cancel | Cancels a pending game invitation. |
| move | Performs a move for the game the user is currently playing. |

General commands:

| Command | Function |
| ------- | -------- |
| help | Returns a list of all commands and their descriptions. |
| purge \<x\> | Deletes the last x messages from the current channel (range 2-100). |
| stop | Stops the bot (only accessible by specified bot owner). |
| info | Returns information about the current guild configuration. |
| addcom \<x\> \<y\> | Adds command x with result y for the current guild. |
| removecom \<x\> | Removes command x for the current guild. |
| updatecom \<x\> \<y\> | Updates command x to have y as new result. |
| configure | Used for guild configuration (continue reading in next section). |

## Configuration

The bot can be configured to allow only certain roles / people on a Discord to use commands.
By default the configuration commands can only be accessed by the guildId owner.
Configuration commands:

| Command | Function |
| ------- | -------- |
| configure p \<cmd\> \<role\> | Restricts access to command \<cmd\> to role \<role\>.* |
| configure pr \<cmd\> \<role\> | Removes access to command \<cmd\> for role \<role\>.* |
| configure c \<channel\> | Adds \<channel\> to the list of dedicated channels.** |
| configure cr \<channel\> | Removes \<channel\> fromt he list of dedicated channel.** |
| configure ca \<channel\> | Enables / disables auto clear for \<channel\>.** |
| configure e | Enables / disables exclusive mode (bot will only react in dedicated channels). |
| configure d \<cmd\> | Disables / re-enables \<cmd\> for the guild.* |
  
\* = either command or command category can be used as param. To use command category you have to add the prefix cc: without a following space.

\*\* = either ID or name can be used as param. To use the ID you have to add the prefix id: without a space afterwards.


## Self hosting

The bot can be self hosted to be used on your own Discord Guild(s).
To use the bot simply build the project with
```
mvn install
```
in the root directory and run the JAR or download the JAR from the releases section to get a stable version.
On first startup add -init as an argument to make the program run shortly to create the db with it's schema and the empty config file. Afterwards fill in the information in the config file and start up the program without any arguments.

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
