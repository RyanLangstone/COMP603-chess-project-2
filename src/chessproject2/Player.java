package chessproject2;

import java.io.Serializable;

/**
 * All of the player attributes are stored here
 *
 * @Author RyanL and Yaacoub
 */
public class Player implements Serializable {

    private String name;
    private int wins;
    private int losses;

    public Player(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void addWin() {
        wins++;
    }

    public void addLosses() {
        losses++;
    }

    @Override
    public String toString() {
        return name + "W: " + wins + "L: " + losses;
    }
}
