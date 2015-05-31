package fcu.advancedood.tictactoe;

import android.app.Application;
import java.net.Socket;

public class SharedData extends Application{

  private Socket Connection;

  public void SetSocketConnection(Socket ServerConnection) {
    this.Connection = ServerConnection;
  }

  public Socket GetSocketConnection() {
    return Connection;
  }
}
