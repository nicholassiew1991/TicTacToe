package fcu.advancedood.tictactoe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

  private final int CONNECT_SERVER = 1;

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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CONNECT_SERVER) {
      if (resultCode == RESULT_OK) {
        Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
      }
    }
  }

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
          ConnectServerLoadingDialog.setTitle("Pairing...");
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

    }.execute(LOCALHOST_IP, SERVER_PORT);
  }

  private void SendWaitParing() {
    try {
      OutputStream OutToServer = Connection.getOutputStream();
      ObjectOutputStream ToServer = new ObjectOutputStream(OutToServer);
      ToServer.writeByte(GlobalData.CLIENT_WAIT_PAIRING);
      ToServer.writeObject("Wait for paring.");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  private char WaitForPlayerSymbol() {
    try {
      Log.v("Wait", "Start");
      ObjectInputStream in = new ObjectInputStream(Connection.getInputStream());
      Log.v("Wait", "End");

      if (in.readByte() == GlobalData.SET_PLAYER_SYMBOL) {
        char Symbol = ((char) in.readObject());
        Log.v("Wait", "" + Symbol);
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

