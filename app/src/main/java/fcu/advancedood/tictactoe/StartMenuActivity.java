package fcu.advancedood.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;


public class StartMenuActivity extends Activity {

  Socket Connection = null;
  Context ThisContext = this;

  //<editor-fold desc="Don't Touch!" defaultstate="collapsed">
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_start_menu);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_start_menu, menu);
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

  public void cmdStartSinglePlayer(View v) {
    Intent SinglePlayerActivity = new Intent();
    SinglePlayerActivity.setClass(StartMenuActivity.this, NewSinglePlayerActivity.class);
    startActivity(SinglePlayerActivity);
  }

  public void cmdStartMultiPlayer(View v) {
    ConnectServer();
  }

  private void ConnectServer() {

    final String SERVER_IP = "192.168.191.1";
    final String LOCALHOST_IP = "10.0.2.2";
    //private final String SERVER_IP = "192.168.1.4";;
    final String SERVER_PORT = "6666";

    new AsyncTask<String, Void, Void>() {

      ProgressDialog ConnectServerLoadingDialog;
      SharedConnection SharedObj = (SharedConnection) getApplicationContext();

      char cPlayerSymbol;

      @Override
      protected void onPreExecute() {
        ConnectServerLoadingDialog = new ProgressDialog(ThisContext);
        ConnectServerLoadingDialog.setTitle("Connecting to server");
        ConnectServerLoadingDialog.setMessage("Please wait...");
        ConnectServerLoadingDialog.setCancelable(true);
        ConnectServerLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {
            ConnectServerLoadingDialog.dismiss();
            Toast.makeText(ThisContext, "Connection Cancelled.", Toast.LENGTH_SHORT).show();
          }
        });
        ConnectServerLoadingDialog.show();
      }

      @Override
      protected Void doInBackground(String... Params) {
        try {
          SharedObj.SetSocketConnection(new Socket(Params[0], Integer.parseInt(Params[1])));
          Connection = SharedObj.GetSocketConnection();
          SendWaitParing();
          cPlayerSymbol = WaitForPlayerSymbol();
        }
        catch (ConnectException e) {
          ConnectServerLoadingDialog.dismiss();
          Toast.makeText(ThisContext, "Error. Can't connect to the server.", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
          ConnectServerLoadingDialog.dismiss();
          Toast.makeText(ThisContext, "Unknown error.", Toast.LENGTH_SHORT).show();
        }
        return null;
      }

      @Override
      protected void onPostExecute(Void result) {
        ConnectServerLoadingDialog.dismiss();
        Intent MultiPlayerActivity = new Intent();
        MultiPlayerActivity.putExtra("Symbol", cPlayerSymbol);
        MultiPlayerActivity.setClass(StartMenuActivity.this, MultiPlayerActivity.class);
        startActivity(MultiPlayerActivity);
      }

    }.execute(SERVER_IP, SERVER_PORT);
  }

  private void SendWaitParing() {
    try {
      OutputStream OutToServer = Connection.getOutputStream();
      ObjectOutputStream ToServer = new ObjectOutputStream(OutToServer);
      ToServer.writeByte(Globals.CLIENT_WAIT_PAIRING);
      ToServer.writeObject("Wait for paring.");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  private char WaitForPlayerSymbol() {
    try {
      ObjectInputStream in = new ObjectInputStream(Connection.getInputStream());

      if (in.readByte() == Globals.SET_PLAYER_SYMBOL) {
        char Symbol = ((char) in.readObject());
        return  Symbol;
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return 0;
  }
}

