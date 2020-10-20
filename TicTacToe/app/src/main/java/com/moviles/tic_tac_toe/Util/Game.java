package com.moviles.tic_tac_toe.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private List<Integer> board;
    private boolean full;
    private String name;
    private boolean turno;
    private boolean again_host;
    private boolean again_other;
    private boolean finish;
    private String host;
    private String other;

    public Game(String name) {
        List<Integer> board = new ArrayList<Integer>();
        for( int i = 0; i < 9; i++){
            board.add(0, -1);
        }
        this.full = false;
        this.board = board;
        this.name = name+"\'s Game ";
        this.again_host = false;
        this.again_other = false;
        this.finish = false;
        this.host = name;
        this.other = "";
        int random = new Random().nextInt(2);
        if(random==0){
            this.turno = false;
        }else {
            this.turno = true;
        }
    }

    public List<Integer> getBoard() {
        return board;
    }

    public void setBoard(int box, int value) {
        this.board.set(box, value);
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public boolean isAgain_host() {
        return again_host;
    }

    public void setAgain_host(boolean again_host) {
        this.again_host = again_host;
    }

    public boolean isAgain_other() {
        return again_other;
    }

    public void setAgain_other(boolean again_other) {
        this.again_other = again_other;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
