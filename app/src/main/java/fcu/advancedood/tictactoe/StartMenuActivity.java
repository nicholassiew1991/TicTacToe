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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;


public class StartMenuActivity extends Activity {

  private final int CONNECT_SERVER = 1;

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

    AlertDialog alertDialogBuilder = new AlertDialog.Builder(StartMenuActivity.this).create();
    alertDialogBuilder.setTitle("Game in under construction");
    alertDialogBuilder.setMessage("Please wait patiently.");

    alertDialogBuilder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        Intent MultiPlayerActivity = new Intent();
        MultiPlayerActivity.setClass(StartMenuActivity.this, MultiPlayerActivity.class);
        startActivityForResult(MultiPlayerActivity, CONNECT_SERVER);
        dialog.dismiss();
      }
    });
    alertDialogBuilder.show();

  }
}

