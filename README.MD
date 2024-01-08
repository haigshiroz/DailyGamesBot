# Introduction

The Daily Games Bot is a Discord bot that keeps track of players' scores for popular daily games.

The currently supported games are: Wordle (NYT), Connections (NYT), Mini (NYT), and Murdle (G.T. Karber).

The bot can be assigned a channel to read text messages from (otherwise reads from every channel)
and passively scans messages from users that match game score copy-pastes.

For example, after completing Wordle, a player can copy-paste their score which can look like this:

> Wordle 932 4/6*
> 
> 🟨⬛⬛⬛🟨
> \
> ⬛🟨⬛🟩🟨
> \
> 🟩🟩🟩🟩⬛
> \
> 🟩🟩🟩🟩🟩

The Daily Games Bot scans messages that match their respective copy-paste templates
and stores them in a database. Thus, in order to submit a score to the database, the
message must ONLY contain the copy-paste and nothing else before or after.
Additionally, players can submit scores from previous dates.
The bot confirms this storage by reacting to the message with a green check mark.
Users can then query the database to compare their score to others. 
Players can specify the date to compare scores and the game to compare scores. 
The default for these two are today's date and all games. 
Score comparisons only compare scores sent in that server.

Players can additionally delete or update their score. 
If they send a score for a game and day that the database already has (thus the same server
and player), they have an option to update the currently stored score to the new one sent.
The bot indicates this by reacting to the message with an arrows-counterclockwise emoji and
question mark emoji. If the player who sent the message reacts to it with the arrows-counterclockwise
emoji, the bot will update the stored score for that game and day and change its reaction to
a green checkmark to confirm. 

# Commands

To set a designated games channel,
> /set-channel Text-Channel

To see how everyone scored,
> /compare (Optional) Date  (Optional) Game
> \
> Date must follow the MM/DD/YYYY format.
> \
> Game must be one of the supported games: Wordle, Connections, Mini, Murdle

To delete your score,
> /delete-score Date Game
> \
> Date must follow the MM/DD/YYYY format.
> \
> Game must be one of the supported games: Wordle, Connections, Mini, Murdle

# APIs Used

The Daily Games Bot uses Java 11, Java Discord API, and Firebase.

## Java Discord API (JDA)

The JDA establishes a connection to the bot using a private token and allows 
developers to configure the bot. This incldues adding commands that the bot
supports, setting the status of the bot, and obtaining attributes of events
such as who sent a message in what channel in what server.

## Firebase 

Firebase is used to store the data that the bot collects such as game scores and
dedicated game channels. When storing and querying data, commands from the Firebase
API are used to access the database structure. 

There are two collections in the database: game scores and server settings (dedicated channels).
Inside the game scores are documents that store the server, player, date, game, and score.
Inside the server settings are documents that store the ID of the dedicated channel.

# Future Development

Many design decisions were made to only support one way of carrying out certain actions.
For example, users currently compare scores with every player which includes their score too. 
What if there was a way to omit your score when comparing with others? 
Additionally, what if all actions had both a command for it and a reaction for it.
For example, to delete a score you can type /delete-score or reaction an X emoji to the
message to delete it. 

The bot could always be updated to support more games.
Games to be considered currently are other Wordle variants and other NYT games.

If a bot is added to a new server, it could scan the server's message history for games
already sent to have a more accurate database.

Lastly, statistics can be calculated on the scores, both in the server and across all servers.
This may be difficult in some cases. For example, Murdle's score tracks both accuracy and 
time. Would the "average" be the average time, average accuracy, or both? 