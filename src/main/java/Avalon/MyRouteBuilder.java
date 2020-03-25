package Avalon;

import org.apache.camel.builder.RouteBuilder;



/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {


    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {


        // here is a sample which processes the input files
        // (leaving them in place - see the 'noop' flag)
        // then performs content based routing on the message using XPath
        String NewGameEndPoint = String.format("jetty://http://%s:%s/Avalon/newGame?Players=numOfPlayes", ConfigAvalon.ip, ConfigAvalon.port);
        String RegisterEndPoint = String.format("jetty://http://%s:%s/Avalon/Register?GameId=gameId&Name=name", ConfigAvalon.ip, ConfigAvalon.port);
        String StatusEndPoint = String.format("jetty://http://%s:%s/Avalon/Status?GameId=gameId", ConfigAvalon.ip, ConfigAvalon.port);
        String SuggestEndPoint = String.format("jetty://http://%s:%s/Avalon/Suggest?GameId=gameId&PlayerId=playerId&SuggestIds=suggestIds", ConfigAvalon.ip, ConfigAvalon.port);
        String VoteEndPoint = String.format("jetty://http://%s:%s/Avalon/Vote?GameId=gameId&PlayerId=playerId&VoteCard=voteCard", ConfigAvalon.ip, ConfigAvalon.port);
        String MissionEndPoint = String.format("jetty://http://%s:%s/Avalon/Mission?GameId=gameId&PlayerId=playerId&MissionCard=missionCard", ConfigAvalon.ip, ConfigAvalon.port);
        String whoamiEndPoint = String.format("jetty://http://%s:%s/Avalon/whoami?GameId=gameId&PlayerId=playerId", ConfigAvalon.ip, ConfigAvalon.port);
        String AssassinEndPoint = String.format("jetty://http://%s:%s/Avalon/Assassin?GameId=gameId&PlayerId=playerId&MerlinId=merlinId", ConfigAvalon.ip, ConfigAvalon.port);

        from(NewGameEndPoint)
                .log("New Avalon Game Request")
                .setHeader("Access-Control-Allow-Origin", constant("*"))
                .setHeader("Access-Control-Allow-Headers", constant("access-control-allow-methods,access-control-allow-origin,authorization,content-type"))
                .setHeader("Access-Control-Allow-Methods", constant("GET, DELETE, POST, OPTIONS, PUT"))
                .bean(GameManager.class,"newGame(${header.Players})")
                .setBody(simple("Welcome to Avalon Your Game ID is ${body}" + String.format("\nFor Registration please use\nhttp://%s:%s/Avalon/Register?GameId=${body}&Name=[your name]", ConfigAvalon.ip, ConfigAvalon.port)));

        from(RegisterEndPoint)
                .log("Game Registration: ${header.Name} want to register to GameId ${header.GameId}")
                .setHeader("Access-Control-Allow-Origin", constant("*"))
                .setHeader("Access-Control-Allow-Headers", constant("access-control-allow-methods,access-control-allow-origin,authorization,content-type"))
                .setHeader("Access-Control-Allow-Methods", constant("GET, DELETE, POST, OPTIONS, PUT"))
                .bean(GameManager.class, "register(${header.GameId},${header.Name})")
                .setBody(simple("${body}"));

        from(StatusEndPoint)
                .log("GetStatus for Game ID :${header.GameId}")
                .setHeader("Access-Control-Allow-Origin", constant("*"))
                .setHeader("Access-Control-Allow-Headers", constant("access-control-allow-methods,access-control-allow-origin,authorization,content-type"))
                .setHeader("Access-Control-Allow-Methods", constant("GET, DELETE, POST, OPTIONS, PUT"))
                .bean(GameManager.class,"getStatus(${header.GameId})")
                .setBody(simple("${body}"));

        from(SuggestEndPoint)
                .log("Suggest Players to go the missions for Game ID :${header.GameId}")
                .setHeader("Access-Control-Allow-Origin", constant("*"))
                .setHeader("Access-Control-Allow-Headers", constant("access-control-allow-methods,access-control-allow-origin,authorization,content-type"))
                .setHeader("Access-Control-Allow-Methods", constant("GET, DELETE, POST, OPTIONS, PUT"))
                .bean(GameManager.class,"Suggest(${header.GameId},${header.PlayerId},${header.SuggestIds})")
                .setBody(simple("${body}"));

        from(VoteEndPoint)
                .log("Player going to Vote")
                .setHeader("Access-Control-Allow-Origin", constant("*"))
                .setHeader("Access-Control-Allow-Headers", constant("access-control-allow-methods,access-control-allow-origin,authorization,content-type"))
                .setHeader("Access-Control-Allow-Methods", constant("GET, DELETE, POST, OPTIONS, PUT"))
                .bean(GameManager.class,"Vote(${header.GameId},${header.PlayerId},${header.VoteCard})")
                .setBody(simple("${body}"));

        from(MissionEndPoint)
                .log("Player going to mission")
                .setHeader("Access-Control-Allow-Origin", constant("*"))
                .setHeader("Access-Control-Allow-Headers", constant("access-control-allow-methods,access-control-allow-origin,authorization,content-type"))
                .setHeader("Access-Control-Allow-Methods", constant("GET, DELETE, POST, OPTIONS, PUT"))
                .bean(GameManager.class,"goMission(${header.GameId},${header.PlayerId},${header.MissionCard})")
                .setBody(simple("${body}"));

        from(whoamiEndPoint)
                .log("Player ask who am i")
                .setHeader("Access-Control-Allow-Origin", constant("*"))
                .setHeader("Access-Control-Allow-Headers", constant("access-control-allow-methods,access-control-allow-origin,authorization,content-type"))
                .setHeader("Access-Control-Allow-Methods", constant("GET, DELETE, POST, OPTIONS, PUT"))
                .bean(GameManager.class,"whoami(${header.GameId},${header.PlayerId})")
                .setBody(simple("${body}"));

        from(AssassinEndPoint)
                .log("Player ask who am i")
                .setHeader("Access-Control-Allow-Origin", constant("*"))
                .setHeader("Access-Control-Allow-Headers", constant("access-control-allow-methods,access-control-allow-origin,authorization,content-type"))
                .setHeader("Access-Control-Allow-Methods", constant("GET, DELETE, POST, OPTIONS, PUT"))
                .bean(GameManager.class,"Assassin(${header.GameId},${header.PlayerId},${header.MerlinId})")
                .setBody(simple("${body}"));
    }
}
