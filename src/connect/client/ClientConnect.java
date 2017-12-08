package connect.client;

import connect.OutObserver;
import model.Peer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
//fixa svar från server

public class ClientConnect {
    private  Socket client;
    private ObjectInputStream ObjIn;
    private  BufferedReader input;
    private  PrintWriter output;
    public void connect(String host, int port) throws IOException{
        client = new Socket(host, port);
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        ObjIn = new ObjectInputStream((client.getInputStream()));
        output = new PrintWriter(client.getOutputStream(), true);
        //ForkJoinPool.commonPool().execute(() -> readInput(out));
    }

    public void close() throws IOException{
        client.close();
    }
    public void quit(String ip, int port) throws IOException{
        output.println("quit "+ip+" "+ String.valueOf(port));
    }

    public void sendMove(String move){
        output.println("choose "+move);
    }

    //send my ip and port to a group to join
    public void join(String myip, int myport){
        output.println("join "+myip+" "+ String.valueOf(myport));
        //få peerlista och sätt till min lista
    }

    public void sendNewpeer(Peer p){
        output.println("newpeer "+p.getIp()+" "+p.getPortnumber());
    }
    public ArrayList<Peer> getPeerList(){
        ArrayList<Peer> pList = new ArrayList<>();
        try {
            pList = (ArrayList<Peer>) ObjIn.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
       // läs input retunera listan av peers
        return pList;
    }


}
