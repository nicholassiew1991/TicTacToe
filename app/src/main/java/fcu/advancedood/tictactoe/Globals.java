package fcu.advancedood.tictactoe;

import android.content.Context;
import android.widget.Toast;

public class Globals {

  /** Server Data Constant Declaration **/
  static final String SERVER_ADDRESS = "192.168.191.1";
  static final String SERVER_PORT = "6666";

  /** Message Type Constant Declaration **/
  static final byte CLIENT_WAIT_PAIRING = 1;
  static final byte SET_PLAYER_SYMBOL = 2;
  static final byte BOARD_STATUS = 3;
  static final byte CLIENT_DISCONNECT_WHILE_PLAYING = 4;

  /** Broadcast Type Constant Declaration **/

  public static void ShowToastMessage(Context context, String Messages, int Duration) {
    Toast.makeText(context, Messages, Duration).show();
  }
}
