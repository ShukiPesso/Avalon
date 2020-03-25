package Avalon;

import org.apache.camel.main.Main;

/**
 * A Camel Application
 */

public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String[] args) throws Exception {
        if(args.length!=0){
            ConfigAvalon.ip=args[0];
            if(args.length>1)
                ConfigAvalon.port=args[1];
        }
        args=new String[]{};
        Main main = new Main();
        main.addRouteBuilder(new MyRouteBuilder());
        main.run(args);
    }

}

