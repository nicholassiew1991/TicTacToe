package fcu.advancedood.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nicholas on 18/5/2015.
 */
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

  private void UpdateBoard(Point p, char Player) {
    if (Player == 'O') {
      GameButton[p.x][p.y].setImageResource(R.drawable.o);
    }
    else if (Player == 'X'){
      GameButton[p.x][p.y].setImageResource(R.drawable.x);
    }

  }

  public void onGameButtonsClick(View v, int x, int y, char Player) {

    Point MovePoint = new Point(x, y);

    if (GameBoard.isPointAvailable(MovePoint) == false) {
      Toast.makeText(this, "This point in not available.", Toast.LENGTH_SHORT).show();
      return;
    }

    GameBoard.PlayerMove(MovePoint, Player);
    UpdateBoard(MovePoint, Player);

    if (GameBoard.isOWon()) {
      Toast.makeText(this, "You win.", Toast.LENGTH_SHORT).show();
      ((TextView) findViewById(R.id.lblStatus)).setText("Winner: O");
      return;
    }
    else if (GameBoard.getAvailableStates().isEmpty()) {
      Toast.makeText(this, "Draw.", Toast.LENGTH_SHORT).show();
      ((TextView) findViewById(R.id.lblStatus)).setText("Draw");
      return;
    }

    // Computer Move
    GameBoard.callMinimax(0, 'X');
    MovePoint = GameBoard.returnBestMove();
    GameBoard.PlayerMove(MovePoint, 'X');
    UpdateBoard(MovePoint, 'X');

    if (GameBoard.isXWon()) {
      Toast.makeText(this, "Computer win.", Toast.LENGTH_SHORT).show();
      ((TextView) findViewById(R.id.lblStatus)).setText("Winner: X");
      return;
    }
    else if (GameBoard.getAvailableStates().isEmpty()) {
      Toast.makeText(this, "Draw.", Toast.LENGTH_SHORT).show();
      ((TextView) findViewById(R.id.lblStatus)).setText("Draw");
      return;
    }
  }
}
