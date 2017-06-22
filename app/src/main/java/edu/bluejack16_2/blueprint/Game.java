package edu.bluejack16_2.blueprint;

/**
 * Created by BAGAS on 22-Jun-17.
 */

public class Game {
    String gameID, gameTitle,gameReleaseYear;
    Game(){}

    public Game(String gameID, String gameTitle, String gameReleaseYear) {
        this.gameID = gameID;
        this.gameTitle = gameTitle;
        this.gameReleaseYear = gameReleaseYear;
    }
}
