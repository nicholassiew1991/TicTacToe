package fcu.advancedood.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class MultiPlayerActivity extends Activity {

  //<editor-fold desc="Global Constant and Variable Declaration.">
  SharedData SharedObj = null;
  Context ThisContext = this;
  Socket Connection;
  Board GameBoard;

  /** Message Type Constant Declaration **/
  private final byte CLIENT_CONNECTED = 1;
  private final byte BOARD_STATUS = 2;
  private final byte GET_PLAYER_SYMBOL = 3;
  /** Message Type Constant Declaration **/

  private ImageButton[][] GameButton;
  boolean swap = false;

  char PlayerSymbol;
  //</editor-fold>

  //<editor-fold desc="Take your own responsibility when you touch this.">
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_player);
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

  public void Init() {
    this.SharedObj = (SharedData) getApplicationContext();
    this.Connection = SharedObj.GetSocketConnection();
  }

  private class ReceiveMessages extends Thread {

    public void run() {
      while (true) {
        try {
          ObjectInputStream in = new ObjectInputStream(Connection.getInputStream());

          switch (in.readByte()) {
            case BOARD_STATUS:
              GameBoard.refreshBoardStatus((char[][]) in.readObject(), GameButton);
              break;

            case GET_PLAYER_SYMBOL:
              PlayerSymbol = in.readChar();
              Toast.makeText(ThisContext, PlayerSymbol + "", Toast.LENGTH_SHORT).show();
              Log.v("Symbol", PlayerSymbol + "");
              break;
          }
        }
        catch (IOException | ClassNotFoundException ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
