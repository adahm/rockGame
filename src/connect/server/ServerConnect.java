package connect.server;
import model.GameState;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerConnect {
    private GameState gameState;
    public ServerConnect(GameState gameState){
        this.gameState = gameState;
    }
    public void connection(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            try {
                while(true){
                    new ServerHandler(serverSocket.accept(),gameState).start(); // accept connection from a peer and start a handler
                }
            }catch (IOException error){
                error.printStackTrace();
            }
        }catch (IOException error){
            error.printStackTrace();
        }
    }
}
