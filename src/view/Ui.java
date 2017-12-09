package view;

import connect.OutObserver;
import controller.ClientControll;

import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Ui extends Thread{
    private final Scanner in = new Scanner(System.in);
    private Boolean go = true;
    private String input;
    private ClientControll control;
    private PrintOut out = new PrintOut();
    @Override
    public void run(){
        control = new ClientControll(out);

        System.out.println("choose portnumber");
        int port = Integer.parseInt(in.nextLine());
        System.out.println("Your ip");
        String ip = in.nextLine();
        control.setMyIpAndPort(ip,port);
        System.out.println("To play the game either join a game or wait for other players to join");
        System.out.println("CMDS:");
        System.out.println("join: to join a game");
        System.out.println("move: to choose a move in the game");
        System.out.println("quit: to quit the game");

        // set up serversocket to receive messages from peers on seperate thread
        ForkJoinPool.commonPool().execute(() -> control.setUpMsgListner(port));
        do {
            System.out.println("Choose action:");
            input = in.nextLine();
            switch(input){
                //join the peernetwork
                case "join":
                    System.out.println("Ip of peer");
                    String peerIP = in.nextLine();
                    System.out.println("Port of peer");
                    int peerPort = Integer.parseInt(in.nextLine());;
                    ForkJoinPool.commonPool().execute(() ->control.join(peerIP,peerPort)); //use commonpool to run on a thread
                    break;
                case "move":
                    System.out.println("s for sizzor, r for rock, b for bag then wait for others to choose");
                    String move = in.nextLine();
                    ForkJoinPool.commonPool().execute(() -> control.sendGuess(move));  //use commonpool to run a seperat thread
                    break;

                case "quit":
                    go = false;
                    control.quit(); //not run on a seprete thread because we want to make sure message get sent and then quit the application
                    break;
                default:
                    System.out.println("not known command");
            }
        }while(go);

        System.exit(1);
    }

    //observer pattern used to print to user
    private class PrintOut implements OutObserver {
        @Override
        public void getServerInput(String input){
            System.out.println(input);
        }
    }

}