package fcu.advancedood.tictactoe;

import android.util.Log;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by Win 7 Ultimate on 2015/5/28.
 */
public class GameThread implements Runnable {
    private Board GameBoard;
    private ImageButton[][] GameButton;
    private Socket clientSocket;

    public GameThread(Socket client, Board board, ImageButton[][] gButton){
        this.GameBoard = board;
        this.GameButton = gButton;
        this.clientSocket = client;
    }
    @Override
    public void run() {
//        Log.v("Test", "1");
        ReceiveGameStatus(clientSocket);
//        Log.v("Test", "2");
    }

    private void ReceiveGameStatus(Socket ClientSocket) {
        while(true){
            try {
                ObjectInputStream in = new ObjectInputStream(ClientSocket.getInputStream());
                GameBoard.refreshBoardStatus((char[][]) in.readObject(), GameButton);
            }
            catch (IOException ex) {
//        Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (ClassNotFoundException ex) {
//        Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
