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
        try{
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
                    case "join":
                        ArrayList<Peer> listWithMypeer = (ArrayList<Peer>) gameState.getPeerList().clone();
                        listWithMypeer.add(new Peer(gameState.getMyIp(),gameState.getMyport()));
                        objout.writeObject(listWithMypeer);
                        objout.flush();
                        Peer newPeer = new Peer(msgspliter[1],Integer.parseInt(msgspliter[2]));
                        for (Peer p : gameState.getPeerList()) {
                            connection.connect(p.getIp(), p.getPortnumber()); //connect to the peer
                            connection.sendNewpeer(newPeer);
                            connection.close(); //close connection
                        }
                        gameState.addPeer(newPeer);
                        break;

                        //lägg i peerlista och skicka ut ny peer till alla och skicka din lista till honom
                    case "newpeer":
                        gameState.addPeer(new Peer(msgspliter[1],Integer.parseInt(msgspliter[2])));
                        break;
                        //lägg i peerlista
                    case "choose":
                        gameState.addMove(msgspliter[1]);
                        //lägg move i kön
                        break;
                    case "quit":
                        gameState.removePeer(new Peer(msgspliter[1],Integer.parseInt(msgspliter[2])));
                        break;
                        //ta bort från peerlista
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
