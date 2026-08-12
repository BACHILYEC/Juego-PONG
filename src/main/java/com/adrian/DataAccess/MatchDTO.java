package com.adrian.DataAccess;

public class MatchDTO {

    private int id;
    private String player1;
    private String player2;
    private int player1Score;
    private int player2Score;
    private long durationSeconds;
    private String date;

    public MatchDTO() {
    }

    public MatchDTO(
            String player1,
            String player2,
            int player1Score,
            int player2Score,
            long durationSeconds) {

        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.durationSeconds = durationSeconds;
    }

    public MatchDTO(
            int id,
            String player1,
            String player2,
            int player1Score,
            int player2Score,
            long durationSeconds,
            String date) {

        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.durationSeconds = durationSeconds;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public void setDurationSeconds(long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setDate(String date) {
        this.date = date;
    }
}