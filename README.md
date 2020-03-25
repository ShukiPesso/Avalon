# Avalon
Camel jetty based for the game "the resistance - Avalon"

This Project developed during the COVID19 pandemic while we wanted to keep plying one each other


rules:

http://upload.snakesandlattes.com/rules/r/ResistanceAvalon.pdf

Build:
Build and run maven package

Run:
java -jar Avalon-1.0-SNAPSHOT-executable-jar.jar <ip (default 0.0.0.0)> <port>

APIs:

New Game
When you want to start new game , only one people should run this API, it will return the Game ID that need to be used in all the others APIs.

Players = number of players that part of the game (can be between 5 to 10)

10.61.80.246:8888/Avalon/newGame?Players=6

Welcome to Avalon Your Game ID is 0

Register
Each member (include the one who set the new game) should Register to the specific Game with Name that he want to be showed.

you will get your private Id and your card

10.61.80.246:8888/Avalon/Register?GameId=0&Name=Shani

Shani Welcome to Avalon, Your Id is:6705 Please use it for do your tasks in the Game 
Your Card is:Merlin

whomai
After all players registered you can run this API and get all the relevant people you should know

you should provide your private id and get your information

10.61.80.246:8888/Avalon/whoami?GameId=0&PlayerID=6705

Hello Shani you are Merlin
The Players You Should Know are:
Shuki

Status
For getting status of the game you just need to provide the Game ID

you will see all the players in the game with the public Ids of them

10.61.80.246:8888/Avalon/Status?GameId=0

Avalon Game id 0 Round 1 Knight score 0 Minion of Mordred Score 0
Round 1
Ronen N should choose 2 Kights for the mission
Players Ids:
Shuki		0
Ronen G		1
Ronen N		2
Shai		3
Shani		4
Tovi		5

Suggest
if you  are the leader of that specific round you should Suggest the knights that should go to the mission

please provide your game id, your private id and your suggested list with the public ids of the members comma separated as below:

10.61.80.246:8888/Avalon/Suggest?GameId=0&PlayerId=3697&SuggestIds=0,1,2

Suggestion Added All should vote

Avalon Game id 0 Round 2 Knight score 0 Minion of Mordred Score 1
Round 1Total Result is Fail , 1 Knights choose to Success the mission and 1 Knights choose to Fail the mission
Knights that went to mission
Shuki
Ronen G
Round 2
The Leader Shai Suggestion:
Shuki
Ronen G
Ronen N


Players Votes:
Players didn't vote yet
Shuki
Ronen G
Ronen N
Shai
Shani
Tovi


Players Ids:
Shuki		0
Ronen G		1
Ronen N		2
Shai		3
Shani		4
Tovi		5

Vote
After the leader suggest the Knights that should go to the mission all players should Vote if they approve or reject his suggestion

provide your game id, private id, and approve or reject

10.61.80.246:8888/Avalon/Vote?GameId=0&PlayerID=4718&VoteCard=approve

You Successfully Approved the Suggestion to mission

Avalon Game id 0 Round 1 Knight score 0 Minion of Mordred Score 0
Round 1
The Leader Ronen N Suggestion:
Shuki
Ronen G

Result: not enough votes for now
Players Votes:
Ronen G		Approve
Shuki		Approve
Players didn't vote yet
Ronen N
Shai
Shani
Tovi


Players Ids:
Shuki		0
Ronen G		1
Ronen N		2
Shai		3
Shani		4
Tovi		5

Mission
After Suggestion Approved only the knights that went to the mission should choose Fail or Success

10.61.80.246:8888/Avalon/Mission?GameId=0&PlayerID=665&MissionCard=success

You Successfully Set your Success Card for the mission

Avalon Game id 0 Round 1 Knight score 0 Minion of Mordred Score 0
Round 1
Waiting for the following to come back from the mission
Ronen G
Players Ids:
Shuki		0
Ronen G		1
Ronen N		2
Shai		3
Shani		4
Tovi		5

Assassin
When Knight Success 3 missions The Minion of Mordred revealed and The Assassin have one chance to assassinate the Merlin

http://10.153.181.14:8888/Avalon/Assassin?GameId=0&PlayerId=865&MerlinId=1