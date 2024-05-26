package com.example.snake;

public class Stat {
    String date;
    Long timeSession;
    Integer score;

    public Stat(String date, Long timeSession, Integer score) {
        this.date = date;
        this.timeSession = timeSession;
        this.score = score;
    }

    public Stat() {

    }

    public String getDate() {
        return date;
    }
    public Long getTimeSession() {
        return timeSession;
    }
    public Integer getScore() {
        return score;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTimeSession(Long timeSession) {
        this.timeSession = timeSession;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
