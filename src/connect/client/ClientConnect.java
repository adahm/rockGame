package connect.client;

import model.Peer;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class ClientConnect {
    private  Socket client;
    private ObjectInputStream ObjIn;
    private  BufferedReader input;
    private  PrintWriter output;
    public void connect(String host, int port) throws IOException{
        //set up a connection and open up the streams to send and recive messages
        client = new Socket(host, port);
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        ObjIn = new ObjectInputStream((client.getInputStream()));
        output = new PrintWriter(client.getOutputStream(), true);
    }

    //closes the connection
    public void close() throws IOException{
        client.close();
    }
    //send quit message to a peer
    public void quit(String ip, int port) throws IOException{
        System.out.println("sending quit");
        output.println("quit "+ip+" "+ String.valueOf(port));
    }

    //send move to a peer
    public void sendMove(String move){
        output.println("choose "+move);
    }

    //send my ip and port to a group to join
    public void join(String myip, int myport){
        output.println("join "+myip+" "+ String.valueOf(myport));
    }

    //send out the new peer that has joined to other
    public void sendNewpeer(Peer p){
        output.println("newpeer "+p.getIp()+" "+p.getPortnumber());
    }

    //get the peerlist sent from a node in the network
    public ArrayList<Peer> getPeerList(){
        ArrayList<Peer> pList = new ArrayList<>();
        try {
            pList = (ArrayList<Peer>) ObjIn.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return pList;
    }


}
