package fcu.advancedood.tictactoe;

import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

class Point {

  int x, y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

class PointsAndScores {

  int score;
  Point point;

  PointsAndScores(int score, Point point) {
    this.score = score;
    this.point = point;
  }
}

class Board {

  List<PointsAndScores> rootsChildrenScores;
  List<Point> availablePoints;

  private char[][] BoardStatus = new char[][] {
    {'?', '?', '?'},
    {'?', '?', '?'},
    {'?', '?', '?'}
  };

  public void UpdateBoard(ImageButton[][] GameButton, Point p, char Player) {
    if (Player == 'O') {
      GameButton[p.x][p.y].setImageResource(R.drawable.o);
    } else if (Player == 'X') {
      GameButton[p.x][p.y].setImageResource(R.drawable.x);
    }
  }

  public void DisableBoardButtons(ImageButton[][] GameButton) {
    for (int a = 0; a < 3; a++) {
      GameButton[a][0].setEnabled(false);
      GameButton[a][1].setEnabled(false);
      GameButton[a][2].setEnabled(false);
    }
  }

  public char[][] GetBoardStatus() {
    return BoardStatus;
  }

  private boolean CheckIsWin(char Player) {

    for (int i = 0; i < 3; i++) {
      if (BoardStatus[i][0] == Player && BoardStatus[i][1] == Player && BoardStatus[i][2] == Player) {
        return true; // Horizontal
      }
      if (BoardStatus[0][i] == Player && BoardStatus[1][i] == Player && BoardStatus[2][i] == Player) {
        return true; // Vertical
      }
    }

    // Check Diagonal
    if (BoardStatus[0][0] == Player && BoardStatus[1][1] == Player && BoardStatus[2][2] == Player) {
      return true;
    }
    else if (BoardStatus[0][2] == Player && BoardStatus[1][1] == Player && BoardStatus[2][0] == Player) {
      return true;
    }

     return false;
  }

  public List<Point> getAvailableStates() {
    availablePoints = new ArrayList<>();
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 3; ++j) {
        if (BoardStatus[i][j] == '?') {
          availablePoints.add(new Point(i, j));
        }
      }
    }
    return availablePoints;
  }

  public boolean isXWon() {
    return CheckIsWin('X');
  }

  public boolean isOWon() {
    return CheckIsWin('O');
  }

  public boolean IsGameOver() {
    return (getAvailableStates().isEmpty() || isXWon() || isOWon());
  }

  public boolean isPointAvailable(Point p) {
    return (BoardStatus[p.x][p.y] == '?' ? true : false);
  }

  public void SetPlayerMove(Point point, char player) {
    BoardStatus[point.x][point.y] = player;
  }

  public Point ReturnBestMove() {
    int MAX = -100000;
    int best = -1;

    for (int i = 0; i < rootsChildrenScores.size(); ++i) {
      if (MAX < rootsChildrenScores.get(i).score) {
        MAX = rootsChildrenScores.get(i).score;
        best = i;
      }
    }
    return rootsChildrenScores.get(best).point;
  }

  public void CallMinimax(int depth, char PlayerTurn) {
    rootsChildrenScores = new ArrayList<>();
    Minimax(depth, PlayerTurn);
  }

  private int returnMin(List<Integer> list) {
    int min = Integer.MAX_VALUE;
    int index = -1;
    for (int i = 0; i < list.size(); ++i) {
      if (list.get(i) < min) {
        min = list.get(i);
        index = i;
      }
    }
    return list.get(index);
  }

  private int returnMax(List<Integer> list) {
    int max = Integer.MIN_VALUE;
    int index = -1;
    for (int i = 0; i < list.size(); ++i) {
      if (list.get(i) > max) {
        max = list.get(i);
        index = i;
      }
    }
    return list.get(index);
  }

  private int Minimax(int depth, char PlayerTurn) {

    if (isXWon()) {
      return +1;
    }
    if (isOWon()) {
      return -1;
    }

    List<Point> pointsAvailable = getAvailableStates();
    if (pointsAvailable.isEmpty()) {
      return 0;
    }

    List<Integer> scores = new ArrayList<>();

    for (int i = 0; i < pointsAvailable.size(); ++i) {
      Point point = pointsAvailable.get(i);

      if (PlayerTurn == 'X') { //X's turn select the highest from below Minimax() call
        SetPlayerMove(point, 'X');
        int currentScore = Minimax(depth + 1, 'O');
        scores.add(currentScore);

        if (depth == 0)
          rootsChildrenScores.add(new PointsAndScores(currentScore, point));

      }
      else if (PlayerTurn == 'O') {//O's turn select the lowest from below Minimax() call
        SetPlayerMove(point, 'O');
        scores.add(Minimax(depth + 1, 'X'));
      }
      BoardStatus[point.x][point.y] = '?'; //Reset this point
    }
    return PlayerTurn == 'X' ? returnMax(scores) : returnMin(scores);
  }
}
