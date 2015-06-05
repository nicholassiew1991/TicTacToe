package fcu.advancedood.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NewSinglePlayerActivity extends Activity {

  private ImageButton[][] GameButton;
  Board GameBoard = new Board();

  //<editor-fold desc="Don't Touch!" defaultstate="collapsed">
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_player);
    Init();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_game, menu);
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

  private void Init() {
    GameButton = new ImageButton[][] {
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
    UpdateStatusTextView("It's your turn");
  }

  private void ActionInit() {
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
  }

  private void UpdateStatusTextView(String message) {
    ((TextView) findViewById(R.id.lblStatus)).setText(message);
  }

  @Override
  public void onBackPressed() {

    if (GameBoard.IsGameOver()) {
      finish();
      return;
    }

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewSinglePlayerActivity.this);
    alertDialog.setTitle("Exit");
    alertDialog.setMessage("Game is in the progress.\nAre you sure want to exit?");

    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    });
    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    alertDialog.show();
  }

  public void onGameButtonsClick(View v, int x, int y, char Player) {

    Point MovePoint = new Point(x, y);

    if (GameBoard.isPointAvailable(MovePoint) == false) {
      Toast.makeText(this, "This point in not available.", Toast.LENGTH_SHORT).show();
      return;
    }

    GameBoard.SetPlayerMove(MovePoint, Player);
    GameBoard.UpdateBoard(GameButton, MovePoint, Player);

    if (GameBoard.CheckIsPlayerWin('O')) {
      Toast.makeText(this, "You win.", Toast.LENGTH_SHORT).show();
      UpdateStatusTextView("Winner: O");
      GameBoard.SetButtonsEnabled(GameButton, false);
      return;
    }
    else if (GameBoard.GetAvailableStates().isEmpty()) {
      Toast.makeText(this, "Draw.", Toast.LENGTH_SHORT).show();
      UpdateStatusTextView("Draw");
      GameBoard.SetButtonsEnabled(GameButton, false);
      return;
    }

    // Computer Move
    UpdateStatusTextView("It's computer turn");
    GameBoard.CallMinimax(0, 'X');
    MovePoint = GameBoard.ReturnBestMove();
    GameBoard.SetPlayerMove(MovePoint, 'X');
    GameBoard.UpdateBoard(GameButton, MovePoint, 'X');

    if (GameBoard.CheckIsPlayerWin('X')) {
      Toast.makeText(this, "Computer win.", Toast.LENGTH_SHORT).show();
      UpdateStatusTextView("Winner: X");
      GameBoard.SetButtonsEnabled(GameButton, false);
      return;
    }
    else if (GameBoard.GetAvailableStates().isEmpty()) {
      Toast.makeText(this, "Draw.", Toast.LENGTH_SHORT).show();
      UpdateStatusTextView("Draw");
      GameBoard.SetButtonsEnabled(GameButton, false);
      return;
    }

    UpdateStatusTextView("It's your turn");
  }
}
