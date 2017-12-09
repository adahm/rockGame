package connect.server;

import connect.client.ClientConnect;
import model.GameState;
import model.Peer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandler extends Thread{
    private Socket peerSocket;
    private BufferedReader input;
    private PrintWriter output;
    private GameState gameState;
    private ObjectOutputStream objout;
    private ClientConnect connection = new ClientConnect();
    public ServerHandler(Socket socket, GameState gameState){
        this.gameState = gameState;
        peerSocket = socket;
        try{ //set up reader to read input and writers to write text and objects
            input = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
            output = new PrintWriter(peerSocket.getOutputStream(),true);
            objout = new ObjectOutputStream(peerSocket.getOutputStream());
        }catch (IOException error) {
            error.printStackTrace();
        }
    }
    @Override
    public void run(){
        String msg;
        try {
            while((msg = input.readLine()) != null){
                String[] msgspliter = msg.split(" ");
                switch (msgspliter[0]){
                    case "join": //someone wants to join
                        ArrayList<Peer> listWithMypeer = (ArrayList<Peer>) gameState.getPeerList().clone();
                        listWithMypeer.add(new Peer(gameState.getMyIp(),gameState.getMyport()));//send my peerlist with myip and port added
                        objout.writeObject(listWithMypeer); //send the list
                        objout.flush();
                        Peer newPeer = new Peer(msgspliter[1],Integer.parseInt(msgspliter[2]));
                        for (Peer p : gameState.getPeerList()) { //send out that a new peer has joined to all the other nodes in network
                            connection.connect(p.getIp(), p.getPortnumber()); //connect to the peer
                            connection.sendNewpeer(newPeer);
                            connection.close(); //close connection
                        }
                        gameState.addPeer(newPeer);
                        break;
                    case "newpeer": //new peer joined add to list
                        gameState.addPeer(new Peer(msgspliter[1],Integer.parseInt(msgspliter[2])));
                        break;
                    case "choose"://player choose move add to movelist
                        gameState.addMove(msgspliter[1]);
                        break;
                    case "quit": //someone quit remove from peerlist
                        gameState.removePeer(new Peer(msgspliter[1],Integer.parseInt(msgspliter[2])));
                        break;

                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
