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

        // set up serversocket to receive messages from peers
        ForkJoinPool.commonPool().execute(() -> control.setUpMsgListner(port));
        do {
            System.out.println("AT any time write QUIT to quit the app:");
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
                    System.out.println("s for sizzor, r for rock, b for bag");
                    String move = in.nextLine();
                    ForkJoinPool.commonPool().execute(() -> control.sendGuess(move));  //use commonpool to run a seperat thread
                    break;

                case "QUIT":
                    go = false;
                    ForkJoinPool.commonPool().execute(() ->control.quit());
                    break;
                default:
                    System.out.println("not known command");
            }
        }while(go);

        System.exit(1);
    }
    private class PrintOut implements OutObserver {
        @Override
        public void getServerInput(String input){
            System.out.println(input);
        }
    }

}
//starta ned
//sätt upp serversocket på portnummer som är givet och läggs nummer i tabell.
//serversocket får join -> lägg i peertable och skicka din table .
//får move
//får quit ta bort nummer från table

//sen gå till inputswitch
//välja join/quit
//join -> öppnna connection till portnummer givet skicka join medelande -> vänta på retur medelande som blir en lista med peers
//join går till att man får skriva sin move eller quit -> move öppna conection til varje socket i peertable skicak move
//quit skicak quit till varje i peertable

//gamestate  håller poäng för hela spelet.
// lista med nuvarnde moves.
//den e ny så när vi fick en move lägg till i listan om listan inte är full gör inget mer. Anars jämrför och sätt socre för rundan.
//har en counter för antal spelare