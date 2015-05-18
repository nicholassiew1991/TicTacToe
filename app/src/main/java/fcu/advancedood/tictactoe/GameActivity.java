package fcu.advancedood.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends Activity {

  /** Global Variable Declaration **/
  private ImageButton[] GameButton;
  private char cWinner = ' ';
  private char cCurrentTurn = 'O'; // Default is O
  private char[] GamePlayStatus = {'?', '?', '?', '?', '?', '?', '?', '?', '?'};

  //<editor-fold desc="Don't Touch!" defaultstate="collapsed">
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);
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
    GameButton = new ImageButton[] {
      (ImageButton) findViewById(R.id.imageButton1),
      (ImageButton) findViewById(R.id.imageButton2),
      (ImageButton) findViewById(R.id.imageButton3),
      (ImageButton) findViewById(R.id.imageButton4),
      (ImageButton) findViewById(R.id.imageButton5),
      (ImageButton) findViewById(R.id.imageButton6),
      (ImageButton) findViewById(R.id.imageButton7),
      (ImageButton) findViewById(R.id.imageButton8),
      (ImageButton) findViewById(R.id.imageButton9)      
    };

    ActionInit();
  }

  private void ActionInit() {
    GameButton[0].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 1);
      }
    });
    GameButton[1].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 2);
      }
    });
    GameButton[2].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 3);
      }
    });
    GameButton[3].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 4);
      }
    });
    GameButton[4].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 5);
      }
    });
    GameButton[5].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 6);
      }
    });
    GameButton[6].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 7);
      }
    });
    GameButton[7].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 8);
      }
    });
    GameButton[8].setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onGameButtonsClick(v, 9);
      }
    });
  }

  private void CheckIsWin(char Player) {

    if (GamePlayStatus[0] == Player && GamePlayStatus[1] == Player && GamePlayStatus[2] == Player) {
      cWinner = Player;
    }
    else if (GamePlayStatus[3] == Player && GamePlayStatus[4] == Player && GamePlayStatus[5] == Player) {
      cWinner = Player;
    }
    else if (GamePlayStatus[6] == Player && GamePlayStatus[7] == Player && GamePlayStatus[8] == Player) {
      cWinner = Player;
    }
    else if (GamePlayStatus[0] == Player && GamePlayStatus[3] == Player && GamePlayStatus[6] == Player) {
      cWinner = Player;
    }
    else if (GamePlayStatus[1] == Player && GamePlayStatus[4] == Player && GamePlayStatus[7] == Player) {
      cWinner = Player;
    }
    else if (GamePlayStatus[2] == Player && GamePlayStatus[5] == Player && GamePlayStatus[8] == Player) {
      cWinner = Player;
    }
    else if (GamePlayStatus[0] == Player && GamePlayStatus[4] == Player && GamePlayStatus[8] == Player) {
      cWinner = Player;
    }
    else if (GamePlayStatus[2] == Player && GamePlayStatus[4] == Player && GamePlayStatus[6] == Player) {
      cWinner = Player;
    }
  }

  private void SendData() {
    // Send GamePlayStatus
  }

  private void ReceiveData() {
    // Receive GamePlayStatus
    // Update UI base on GamePlayStatus
    // CheckWinner.
  }

  public void onGameButtonsClick(View v, int ButttonNum) {

    if (GamePlayStatus[ButttonNum - 1] != '?') {
      // Clicked
    }
    else {
      GamePlayStatus[ButttonNum - 1] = cCurrentTurn;

      if (cCurrentTurn == 'O') {
        GameButton[ButttonNum - 1].setImageResource(R.drawable.o);
        CheckIsWin(cCurrentTurn);
        cCurrentTurn = 'X';
      }
      else if (cCurrentTurn == 'X') {
        GameButton[ButttonNum - 1].setImageResource(R.drawable.x);
        CheckIsWin(cCurrentTurn);
        cCurrentTurn = 'O';
      }
    }
    if (cWinner != ' ') {
      Toast.makeText(this, "Winner: " + cWinner, Toast.LENGTH_SHORT).show();
      ((TextView) findViewById(R.id.lblStatus)).setText("Winner: " + cWinner);

      for (int a = 0; a < GameButton.length; a++) {
        GameButton[a].setEnabled(false);
      }
    }
  }
}
