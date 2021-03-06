package controller;

import connect.OutObserver;
import connect.client.ClientConnect;
import connect.server.ServerConnect;
import model.GameState;
import model.Peer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class ClientControll {
    private final ClientConnect connection = new ClientConnect();
    private ServerConnect serverListner;
    private GameState gameState;

    //create a new gamestate and a listener  for peer commands
    public ClientControll(OutObserver out) {
        gameState = new GameState(out); //create a new gamesesion send the outobserver so it can print
        serverListner = new ServerConnect(gameState);//give the servserconnection the gamestate object
    }

    //send out a join message to a member of a group with ip and port. And then get the peerlist they send and close connection
    public void join(String ip, int port) {
        try {
            connection.connect(ip,port); //connect to the peer
            connection.join(gameState.getMyIp(),gameState.getMyport()); //send my ip and port to the peer
            gameState.setPeerList(connection.getPeerList()); //get the peerlist sent from peer
            connection.close(); //close connection
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //send out quit to all peers
    public void quit(){
        try {
            //tell all the peers that you quit
            for (Peer p : gameState.getPeerList()) {
                connection.connect(p.getIp(), p.getPortnumber()); //connect to the peer
                connection.quit(gameState.getMyIp(), gameState.getMyport());
                connection.close(); //close connection
            }
        } catch(IOException e){
            e.printStackTrace();
        }

    }


    public void sendGuess(String move){
        try {
            if (!gameState.getMoveDone()) {//if we haven´t set our move send it to the others otherwise don´t do anything
                gameState.setMyMove(move); //set my move
                for (Peer p : gameState.getPeerList()) { //send move to all peers
                    connection.connect(p.getIp(), p.getPortnumber()); //connect to the peer
                    connection.sendMove(move);
                    connection.close(); //close connection
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    //set up the serversocket to listen for peer messages
    public void setUpMsgListner(int portnumber){
        serverListner.connection(portnumber);
    }

    //save my ip and port to the gamestate
    public void setMyIpAndPort(String ip,int port){
        gameState.setMyIp(ip);
        gameState.setMyport(port);
    }
}
