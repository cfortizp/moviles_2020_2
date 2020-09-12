package com.example.tic_tac_toe.Presentation;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tic_tac_toe.R;
import com.example.tic_tac_toe.Util.BoardSpace;
import com.example.tic_tac_toe.Util.Constanst;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private BoardSpace board[];
    private TextView score_human;
    private TextView score_ties;
    private TextView score_android;
    private TextView information;
    private Button play;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        board = new BoardSpace[9];
        board[0] = new BoardSpace((ImageButton) findViewById(R.id.one),-1);
        board[1] = new BoardSpace((ImageButton)findViewById(R.id.two),-1);
        board[2] = new BoardSpace((ImageButton)findViewById(R.id.three),-1);
        board[3] = new BoardSpace((ImageButton)findViewById(R.id.four),-1);
        board[4] = new BoardSpace((ImageButton)findViewById(R.id.five),-1);
        board[5] = new BoardSpace((ImageButton)findViewById(R.id.six),-1);
        board[6] = new BoardSpace((ImageButton)findViewById(R.id.seven),-1);
        board[7] = new BoardSpace((ImageButton)findViewById(R.id.eight),-1);
        board[8] = new BoardSpace((ImageButton)findViewById(R.id.nine),-1);
        score_human = findViewById(R.id.human_score);
        score_ties = findViewById(R.id.ties_score);
        score_android = findViewById(R.id.android_score);
        information = findViewById(R.id.information);
        play = findViewById(R.id.again);
        play.setText("Play!");
        play.setEnabled(true);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setEnabled(false);
                play.setText("Play again!");
                start_game();

            }
        });
        Constanst.score_Human = 0;
        Constanst.score_Android = 0;
        Constanst.score_Ties = 0;
        update_score();

    }
    private void start_game(){
        int turn = new Random().nextInt(2);
        for(int i = 0; i< board.length;i++){
            board[i].getButton().setEnabled(true);
            board[i].setSelect(-1);
            board[i].getButton().setImageResource(0);
            board[i].getButton().setOnClickListener(new ButtonClickListener(i));
        }
        if(turn==0){
            information.setText("Android go first!");
            android_move();
        }else{
            information.setText("You go first!");
        }
    }

    private void android_move(){
        boolean isvalid = true;
        while(isvalid){
            int select = new Random().nextInt(9);
            if(board[select].getSelect()==-1){
                board[select].getButton().setEnabled(false);
                board[select].getButton().setImageResource(R.drawable.android);
                board[select].setSelect(0);
                isvalid = false;
            }

        }
        if(!check_game()){
            information.setText("Your turn");
        }else{
            update_score();
            for(int i = 0; i< board.length;i++){
                board[i].getButton().setEnabled(false);
            }
            play.setEnabled(true);
        }


    }

    private void update_score(){
        score_human.setText(String.valueOf(Constanst.score_Human));
        score_android.setText(String.valueOf(Constanst.score_Android));
        score_ties.setText(String.valueOf(Constanst.score_Ties));
    }
    private boolean check_game(){
        int winner = -1;

        if(board[0].getSelect() != -1 && board[0].getSelect() == board[1].getSelect() && board[0].getSelect() == board[2].getSelect()){
            winner = board[0].getSelect();
        }else if(board[0].getSelect() != -1 && board[0].getSelect() == board[3].getSelect() && board[0].getSelect() == board[6].getSelect()){
            winner = board[0].getSelect();
        }else if(board[0].getSelect() != -1 && board[0].getSelect() == board[4].getSelect() && board[0].getSelect() == board[8].getSelect()){
            winner = board[0].getSelect();
        }else if(board[3].getSelect() != -1 && board[3].getSelect() == board[4].getSelect() && board[3].getSelect() == board[5].getSelect()){
            winner = board[3].getSelect();
        }else if(board[6].getSelect() != -1 && board[6].getSelect() == board[7].getSelect() && board[6].getSelect() == board[8].getSelect()){
            winner = board[6].getSelect();
        }else if(board[1].getSelect() != -1 && board[1].getSelect() == board[4].getSelect() && board[1].getSelect() == board[7].getSelect()){
            winner = board[1].getSelect();
        }else if(board[2].getSelect() != -1 && board[2].getSelect() == board[5].getSelect() && board[2].getSelect() == board[8].getSelect()){
            winner = board[2].getSelect();
        }else if(board[2].getSelect() != -1 && board[2].getSelect() == board[4].getSelect() && board[2].getSelect() == board[6].getSelect()){
            winner = board[2].getSelect();
        }
        if(winner==0){
            information.setText("Android won!");
            Constanst.score_Android++;
            return true;
        }else if(winner == 1){
            information.setText("You won!");
            Constanst.score_Human++;
            return true;
        }
        boolean tie = true;
        for(int i = 0; i< board.length;i++){
            if(board[i].getSelect()==-1){
                tie = false;
                break;
            }
        }
        if(tie){
            Constanst.score_Ties++;
            information.setText("It's a tie!");
            return true;
        }
        return false;
    }
    public class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int i) {
            this.location = i;
        }

        @Override
        public void onClick(View view) {
            if(board[location].getSelect()==-1) {
                board[location].getButton().setEnabled(false);
                board[location].getButton().setImageResource(R.drawable.man);
                board[location].setSelect(1);
                if(!check_game()){
                    information.setText("Android\'s turn");
                    android_move();
                }else{
                    update_score();
                    for(int i = 0; i< board.length;i++){
                        board[i].getButton().setEnabled(false);
                    }
                    play.setEnabled(true);
                }
            }
        }
    }
}
