package fcu.advancedood.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MultiPlayerActivity extends Activity {

    //<editor-fold desc="Global Constant and Variable Declaration.">
    private SharedConnection SharedObj = null;
    private Context ThisContext = this;
    private Socket Connection;
    private Board GameBoard;
    private ImageButton[][] GameButton;

    /** Game play control variable declaration. **/
    private char cPlayerSymbol;
    private char cOpponentSymbol;
    //</editor-fold>

    //<editor-fold desc="Take your own responsibility when you touch this.">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_single_player);
        setContentView(R.layout.activity_game_board_layout);
        Init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //</editor-fold>

    //<editor-fold desc="Initialization">
    private void Init() {

        /** Get data passed from previous activity. **/
        Bundle IntentData = getIntent().getExtras();
        this.cPlayerSymbol = IntentData.getChar("Symbol");

        /** Game initializing. **/
        this.GameBoard = new Board();
        GameButton = new ImageButton[][]{
                {
                        (ImageButton) findViewById(R.id.imageButton1),
                        (ImageButton) findViewById(R.id.imageButton2),
                        (ImageButton) findViewById(R.id.imageButton3)
                },
                {
                        (ImageButton) findViewById(R.id.imageButton4),
                        (ImageButton) findViewById(R.id.imageButton5),
                        (ImageButton) findViewById(R.id.imageButton6)
                },
                {
                        (ImageButton) findViewById(R.id.imageButton7),
                        (ImageButton) findViewById(R.id.imageButton8),
                        (ImageButton) findViewById(R.id.imageButton9)
                }
        };

        ActionInit();
        if (cPlayerSymbol == 'O') {
            cOpponentSymbol = 'X';
            Globals.ShowToastMessage(ThisContext, "You take first", Toast.LENGTH_SHORT);
        }
        else {
            cOpponentSymbol = 'O';
            Globals.ShowToastMessage(ThisContext, "Opponent take first", Toast.LENGTH_SHORT);
            GameBoard.SetButtonsEnabled(GameButton, false);
        }

        this.SharedObj = (SharedConnection) getApplicationContext();
        this.Connection = SharedObj.GetSocketConnection();
        new ReceiveData().start();
    }

    private void ActionInit() {

        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                GameButtonActionInit(a, b);
            }
        }

        ImageButton SendChatMessageButton = (ImageButton) findViewById(R.id.sendMessage);
        SendChatMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSendChatClicked();
            }
        });
    }

    private void GameButtonActionInit(int x, int y) {

        final int PointX = x;
        final int PointY = y;
        GameButton[x][y].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnGameButtonClicked(PointX, PointY);
            }
        });
    }
    //</editor-fold>

    @Override
    public void onBackPressed() {

        if (GameBoard.IsGameOver()) {
            finish();
            return;
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MultiPlayerActivity.this);
        alertDialog.setTitle("Exit");
        alertDialog.setMessage("Game is in the progress.\nAre you sure want to exit?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Connection.close();
                    finish();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void OnGameButtonClicked(int x, int y) {

        Point MovePoint = new Point(x, y);

        if (GameBoard.isPointAvailable(MovePoint) == false) {
            Globals.ShowToastMessage(this, "This point in not available.", Toast.LENGTH_SHORT);
            return;
        }

        GameBoard.SetPlayerMove(MovePoint, cPlayerSymbol);
        GameBoard.UpdateBoard(GameButton, MovePoint, cPlayerSymbol);
        SendGameStatus();
        GameBoard.SetButtonsEnabled(GameButton, false);

        if (GameBoard.CheckIsPlayerWin(cPlayerSymbol)) {
            Toast.makeText(ThisContext, "You win.", Toast.LENGTH_SHORT).show();
        }
        else if (GameBoard.GetAvailableStates().isEmpty()) {
            Toast.makeText(ThisContext, "Draw.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ThisContext, "It's opponent turn.", Toast.LENGTH_SHORT).show();
        }
    }

    private void OnSendChatClicked() {

        EditText messageText = (EditText) findViewById(R.id.ChatMessage);
        String strChatMessage = messageText.getText().toString();
        if(strChatMessage.equals("")){
            Toast.makeText(ThisContext, "Please insert message!!!", Toast.LENGTH_SHORT).show();
        }else{

            try {

                DataOutputStream dOut = new DataOutputStream(Connection.getOutputStream());

                Toast ShowSentMessage = Toast.makeText(ThisContext, strChatMessage, Toast.LENGTH_SHORT);

                dOut.writeByte(Globals.CHAT_MESSAGE);

                dOut.writeUTF(strChatMessage);


                if (cPlayerSymbol == 'O') {
                    ShowSentMessage.setGravity(Gravity.TOP | Gravity.LEFT, 50, 300);

                }
                else {
                    ShowSentMessage.setGravity(Gravity.TOP | Gravity.RIGHT, 50, 300);
                }

                ShowSentMessage.show();
                dOut.flush();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void SendGameStatus() {
        try {
            char[][] BoardStatus = GameBoard.GetBoardStatus();
            DataOutputStream dOut = new DataOutputStream(Connection.getOutputStream());
            dOut.writeByte(Globals.BOARD_STATUS);
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 3; b++) {
                    dOut.writeChar(BoardStatus[a][b]);
                }
            }
            dOut.flush(); // Send out
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReceiveData extends Thread {

        private DataInputStream in;

        public void run() {

            while (true) {
                try {
                    in = new DataInputStream(Connection.getInputStream());

                    byte MessagesType = in.readByte();

                    if (MessagesType == Globals.BOARD_STATUS) {
                        ReceiveBoardStatus();
                    }
                    else if (MessagesType == Globals.CLIENT_DISCONNECT_WHILE_PLAYING) {
                        if (GameBoard.IsGameOver() == false) {
                            ReceiveOpponentDisconnected();
                        }
                    }
                    else if (MessagesType == Globals.CHAT_MESSAGE) {
                        ReceiveChatMessage();
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void ReceiveBoardStatus() {
            char[][] BoardStatus = new char[3][3];

            try {
                for (int a = 0; a < 3; a++) {
                    for (int b = 0; b < 3; b++) {
                        BoardStatus[a][b] = in.readChar();
                    }
                }
                GameBoard.SetBoardStatus(BoardStatus);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GameBoard.UpdateWholeBoard(GameButton);
                        GameBoard.SetButtonsEnabled(GameButton, true);

                        if (GameBoard.CheckIsPlayerWin(cOpponentSymbol)) {
                            Toast.makeText(ThisContext, "Opponent win.", Toast.LENGTH_SHORT).show();
                        }
                        else if (GameBoard.GetAvailableStates().isEmpty()) {
                            Toast.makeText(ThisContext, "Draw.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ThisContext, "It's your turn.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void ReceiveOpponentDisconnected() {
            try {

                final String ShowMessage = in.readUTF();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThisContext);
                        alertDialog.setTitle("Game Over");
                        alertDialog.setMessage(ShowMessage);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });

                        alertDialog.show();
                    }
                });
                Connection.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void ReceiveChatMessage() {

            try {

                final String strReceivedMessage = in.readUTF();
                Log.v("Chat", strReceivedMessage);


                if (cOpponentSymbol == 'O') {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast ShowReceiveMessage = Toast.makeText(ThisContext, strReceivedMessage, Toast.LENGTH_SHORT);
                            ShowReceiveMessage.setGravity(Gravity.TOP | Gravity.LEFT, 50, 300);
                            ShowReceiveMessage.show();
                        }
                    });
                }
                else if (cOpponentSymbol == 'X') {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast ShowReceiveMessage = Toast.makeText(ThisContext, strReceivedMessage, Toast.LENGTH_SHORT);
                            ShowReceiveMessage.setGravity(Gravity.TOP | Gravity.RIGHT, 50, 300);
                            ShowReceiveMessage.show();
                        }
                    });
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
