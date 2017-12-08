package model;

import connect.OutObserver;

import java.util.ArrayList;

//innhålla lista med peers
public class GameState {
    private ArrayList<Peer> peerList = new ArrayList<>(); //gör om till sync
    private ArrayList<String> moveList = new ArrayList<>();//gör om till synch
    private int Points = 0;
    private String myMove;
    private int myport; //spara som en peer
    private String myIp;
    private OutObserver out;
    private Boolean moveDone = false;
    //kanske ha en hashmap med peer och moves

    public GameState(OutObserver out){
        this.out = out;
    }
    public void doRound(){
        if (peerList.size() == moveList.size() && moveDone){ //check if everyone has done their moves otherwise do nothing
            int roundScore = 0;
            for (String m:moveList) {
                if(m.equals("s") && myMove.equals("r")){
                    roundScore++;
                }
                if(m.equals("r") && myMove.equals("b")){
                    roundScore++;
                }
                if(m.equals("b") && myMove.equals("s")){
                    roundScore++;
                }
            }
            Points = Points + roundScore;
            out.getServerInput("Round:"+roundScore+ "Total:"+ Points);
            moveList = new ArrayList<>();//reset moveList
            moveDone = false;
        }
        //kallas när vi fått en move
        //kolla om moves = length av peerlist och om vi har valt
        //anars gör inget
        //räkna poäng annars
        //reseta movelista
        //skriv ut runda poäng och totalpoäng

    }

    public void removePeer(Peer p){
        peerList.remove(p);
    }
    public void addMove(String move){
        moveList.add(move);
        doRound();
    }

    public void setPeerList(ArrayList<Peer> peerList){
        this.peerList = peerList;
    }
    public void addPeer(Peer p){ // lägg till sync
        out.getServerInput(peerList.toString());
        peerList.add(p);
    }

    public void setMyMove(String move){
        if(!moveDone){
            moveDone = true; //set
            myMove = move;
            doRound();
        }
    }

    public void setMyIp(String myIp) {
        this.myIp = myIp;
    }

    public void setMyport(int myport) {
        this.myport = myport;
    }

    public ArrayList<Peer> getPeerList() {
        return peerList;
    }

    public int getMyport() {
        return myport;
    }

    public String getMyIp() {
        return myIp;
    }
}
