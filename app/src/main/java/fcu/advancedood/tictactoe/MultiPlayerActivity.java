package fcu.advancedood.tictactoe;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;

public class MultiPlayerActivity extends Activity {

  private final String SERVER_IP = "192.168.191.1";
  private final String SERVER_PORT = "6666";

  private ImageButton[][] GameButton;
  Board GameBoard = new Board();

  Context ThisContext = this;
  Socket Connection;

  //<editor-fold desc="Don't touch">
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
    //ThisContext = this;
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

    new AsyncTask<String, Void, String>() {

      ProgressDialog ConnectServerLoadingDialog;
      Intent ReturnData = new Intent();

      @Override
      protected void onPreExecute() {
        ConnectServerLoadingDialog = new ProgressDialog(ThisContext);
        ConnectServerLoadingDialog.setTitle("Connecting to server");
        ConnectServerLoadingDialog.setMessage("Please wait...");
        ConnectServerLoadingDialog.setCancelable(true);
        ConnectServerLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {
            ReturnData.setData(Uri.parse("Connection cancelled."));
            setResult(RESULT_OK, ReturnData);
            finish();
          }
        });
        ConnectServerLoadingDialog.setIndeterminate(true);
        ConnectServerLoadingDialog.show();
      }

      @Override
      protected String doInBackground(String... AddressPort) {

        try {
          Connection = new Socket(AddressPort[0], Integer.parseInt(AddressPort[1]));
          Thread.sleep(5000);
        }
        catch (ConnectException e) {
          ReturnData.setData(Uri.parse("Error. Can't connect to the server."));
          setResult(RESULT_OK, ReturnData);
          finish();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        }
        return "Connected";
      }

      @Override
      protected void onPostExecute(String result) {
        ConnectServerLoadingDialog.dismiss();
        ((TextView) findViewById(R.id.lblStatus)).setText(result);
      }

    }.execute(SERVER_IP, SERVER_PORT);
  }

  private void ActionInit() {
    /*
    GameButton[0][0].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 0, 0, 'O');
      }
    });
    GameButton[0][1].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 0, 1, 'O');
      }
    });
    GameButton[0][2].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 0, 2, 'O');
      }
    });
    GameButton[1][0].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 1, 0, 'O');
      }
    });
    GameButton[1][1].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 1, 1, 'O');
      }
    });
    GameButton[1][2].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 1, 2, 'O');
      }
    });
    GameButton[2][0].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 2, 0, 'O');
      }
    });
    GameButton[2][1].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 2, 1, 'O');
      }
    });
    GameButton[2][2].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 2, 2, 'O');
      }
    });
    */
  }

  private void UpdateBoard(Point p, char Player) {
    if (Player == 'O') {
      GameButton[p.x][p.y].setImageResource(R.drawable.o);
    } else if (Player == 'X') {
      GameButton[p.x][p.y].setImageResource(R.drawable.x);
    }
  }

  private void DisableButtons() {
    for (int a = 0; a < 3; a++) {
      GameButton[a][0].setEnabled(false);
      GameButton[a][1].setEnabled(false);
      GameButton[a][2].setEnabled(false);
    }
  }

  private void UpdateStatusTextView(String message) {
    ((TextView) findViewById(R.id.lblStatus)).setText(message);
  }

  public void onGameButtonsClick(View v, int x, int y, char Player) {

    Point MovePoint = new Point(x, y);

    if (GameBoard.isPointAvailable(MovePoint) == false) {
      Toast.makeText(this, "This point in not available.", Toast.LENGTH_SHORT).show();
      return;
    }

    GameBoard.SetPlayerMove(MovePoint, Player);
    UpdateBoard(MovePoint, Player);

    if (GameBoard.isOWon()) {
      Toast.makeText(this, "You win.", Toast.LENGTH_SHORT).show();
      UpdateStatusTextView("Winner: O");
      DisableButtons();
      return;
    } else if (GameBoard.getAvailableStates().isEmpty()) {
      Toast.makeText(this, "Draw.", Toast.LENGTH_SHORT).show();
      UpdateStatusTextView("Draw");
      DisableButtons();
      return;
    }

    // Computer Move
    UpdateStatusTextView("It's computer turn");
    GameBoard.CallMinimax(0, 'X');
    MovePoint = GameBoard.ReturnBestMove();
    GameBoard.SetPlayerMove(MovePoint, 'X');
    UpdateBoard(MovePoint, 'X');

    if (GameBoard.isXWon()) {
      Toast.makeText(this, "Computer win.", Toast.LENGTH_SHORT).show();
      UpdateStatusTextView("Winner: X");
      DisableButtons();
      return;
    } else if (GameBoard.getAvailableStates().isEmpty()) {
      Toast.makeText(this, "Draw.", Toast.LENGTH_SHORT).show();
      UpdateStatusTextView("Draw");
      DisableButtons();
      return;
    }

    UpdateStatusTextView("It's your turn");
  }
}
