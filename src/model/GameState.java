package model;

import connect.OutObserver;

import java.util.ArrayList;

//class that conatins the current state of the game
public class GameState {
    private ArrayList<Peer> peerList = new ArrayList<>(); //list for the peers
    private ArrayList<String> moveList = new ArrayList<>();//list for the moves
    private int Points = 0;
    private String myMove;
    private int myport; //
    private String myIp;
    private OutObserver out;
    private Boolean moveDone = false;

    public GameState(OutObserver out){
        this.out = out;
    }

    //do a round in the game if I have done my move and all the peers has added their moves to the movelist
    public void doRound(){
        if (peerList.size() == moveList.size() && moveDone){ //check if everyone has done their moves otherwise do nothing
            int roundScore = 0;
            for (String m:moveList) { //if we beet a move add add a point
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
            Points = Points + roundScore; //add the roundscore to the total
            out.getServerInput("Round:"+roundScore+ "Total:"+ Points);
            out.getServerInput("write quit to quit or choose a new move");
            moveList = new ArrayList<>();//reset moveList
            moveDone = false; //set that we haven´t done our move
        }

    }

    //iterate thorgh the list and remove the peer
    public synchronized void removePeer(Peer p){
        int i = 0;
        int removeId = 0;
        for (Peer itP: peerList) {
            if (itP.getIp().equals(p.getIp()) && itP.getPortnumber() == p.getPortnumber()){
                removeId = i;
            }
            i++;
        }
        peerList.remove(removeId);
        out.getServerInput(p.toString()+ "quit");
    }
    //add the move sent by peer to list and call doRound
    public synchronized void addMove(String move){
        moveList.add(move);
        doRound();
    }

    //set our peerlist to the one with got when we joined
    public synchronized void setPeerList(ArrayList<Peer> peerList){
        out.getServerInput("joined game with:");
        out.getServerInput(peerList.toString());
        this.peerList = peerList;
    }

    //add a new peer to the peerlist
    public synchronized void addPeer(Peer p){ // lägg till sync
        out.getServerInput(p.toString() + " joined");
        peerList.add(p);

    }

    //set my move
    public synchronized void setMyMove(String move){
        if(!moveDone){
            moveDone = true; //set
            myMove = move;
            doRound();
        }
    }

    //set my ip
    public void setMyIp(String myIp) {
        this.myIp = myIp;
    }

    //set my port
    public void setMyport(int myport) {
        this.myport = myport;
    }

    public synchronized ArrayList<Peer> getPeerList() {
        return peerList;
    }

    public int getMyport() {
        return myport;
    }

    public String getMyIp() {
        return myIp;
    }

    public Boolean getMoveDone() {
        return moveDone;
    }
}
