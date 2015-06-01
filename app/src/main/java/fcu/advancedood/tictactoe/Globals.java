package fcu.advancedood.tictactoe;

import android.content.Context;
import android.widget.Toast;

public class Globals {
  /* Message Type Constant Declaration */
  static final byte CLIENT_WAIT_PAIRING = 1;
  static final byte SET_PLAYER_SYMBOL = 2;
  static final byte BOARD_STATUS = 3;
  static final byte GET_PLAYER_SYMBOL = 4;

  public static void ShowToastMessage(Context context, String Messages, int Duration) {
    Toast.makeText(context, Messages, Duration).show();
  }
}