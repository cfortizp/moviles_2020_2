package com.example.tic_tac_toe.Presentation;

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

public class GameActivity extends AppCompatActivity {

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
        board[0] = new BoardSpace((ImageButton)findViewById(R.id.one),-1);
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
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setEnabled(false);
                play.setText("Play again!");
                start_game();

            }
        });
        update_score();
        start_game();

    }
    private void start_game(){
        int turn = new Random().nextInt(2);
        for(int i = 0; i< board.length;i++){
            board[i].getButton().setEnabled(true);
            board[i].setSelect(-1);
            board[i].getButton().setImageResource(0);
            board[i].getButton().setOnClickListener(new GameActivity.ButtonClickListener(i));
        }
        if(turn==0){
            information.setText("Android go first!");
            android_move();
        }else{
            information.setText("You go first!");
        }
    }

    private void android_move(){

        switch (Constanst.dificulty){
            case 0:
                RandomMove();
                break;
            case 1:
                HardMove();
                break;
            case 2:
                ExpertMove();
                break;
        }
        int winner = checkForWinner();
        if(winner ==0){
            information.setText("Your turn");
        }else{
            String message = "";
            switch (winner){
                case 1:
                    message = "It's a tie!";
                    Constanst.score_Ties++;
                    break;
                case 2:
                    message = "You won!";
                    Constanst.score_Human++;
                    break;
                case 3:
                    message = "Android won!";
                    Constanst.score_Android++;
                    break;
            }
            information.setText(message);
            update_score();
            for(int i = 0; i< board.length;i++){
                board[i].getButton().setEnabled(false);
            }
            play.setEnabled(true);
        }

    }
    private void RandomMove(){
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
        return;

    }

    private void update_score(){
        score_human.setText(String.valueOf(Constanst.score_Human));
        score_android.setText(String.valueOf(Constanst.score_Android));
        score_ties.setText(String.valueOf(Constanst.score_Ties));
    }
    private void HardMove()
    {
        int move;

        // First see if there's a move O can make to win
        for (int i = 0; i < 9; i++) {
            if (board[i].getSelect() != 1 && board[i].getSelect() != 0) {
                board[i].setSelect(0);
                if (checkForWinner() == 3) {
                    board[i].getButton().setEnabled(false);
                    board[i].getButton().setImageResource(R.drawable.android);
                    return;
                }
                else{
                    board[i].setSelect(-1);
                }

            }
        }
        // Generate random move
        RandomMove();
    }
    private void ExpertMove()
    {
        int move;

        // First see if there's a move O can make to win
        for (int i = 0; i < 9; i++) {
            if (board[i].getSelect() != 1 && board[i].getSelect() != 0) {
                board[i].setSelect(0);
                if (checkForWinner() == 3) {
                    board[i].getButton().setEnabled(false);
                    board[i].getButton().setImageResource(R.drawable.android);
                    return;
                }
                else{
                    board[i].setSelect(-1);
                }

            }
        }

        // See if there's a move O can make to block X from winning
        for (int i = 0; i < 9; i++) {
            if (board[i].getSelect() != 1 && board[i].getSelect() != 0) {
                board[i].setSelect(1);
                if (checkForWinner() == 2) {
                    board[i].getButton().setEnabled(false);
                    board[i].getButton().setImageResource(R.drawable.android);
                    board[i].setSelect(0);
                    return;
                }
                else{
                    board[i].setSelect(-1);
                }
            }
        }

        // Generate random move
        RandomMove();
    }

    private int checkForWinner() {
        // Check for a winner.  Return
        //  0 if no winner or tie yet
        //  1 if it's a tie
        //  2 if X won
        //  3 if O won

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (board[i].getSelect() ==  1 && board[i+1].getSelect() ==  1 && board[i+2].getSelect() ==  1){
                return 2;
            }
            if (board[i].getSelect() ==  0 && board[i+1].getSelect() ==  0 && board[i+2].getSelect() ==  0){
                return 3;
            }
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (board[i].getSelect() == 1 && board[i + 3].getSelect() == 1 && board[i + 6].getSelect() == 1) {
                return 2;
            }
            if (board[i].getSelect() == 0 && board[i + 3].getSelect() == 0 && board[i + 6].getSelect() == 0){
                return 3;
            }
        }

        // Check for diagonal wins
        if ((board[0].getSelect() ==  1 && board[4].getSelect() ==  1 && board[8].getSelect() ==  1) ||
                (board[2].getSelect() ==  1 && board[4].getSelect() ==  1 && board[6].getSelect() ==  1)){
            return 2;
        }

        if ((board[0].getSelect() ==  0 && board[4].getSelect() ==  0 && board[8].getSelect() ==  0) ||
                (board[2].getSelect() ==  0 && board[4].getSelect() ==  0 && board[6].getSelect() ==  0)){
            return 3;
        }

        // Check for tie
        for (int i = 0; i < 9; i++) {
            // If we find a number, then no one has won yet
            if (board[i].getSelect() != 1 && board[i].getSelect() != 0){
                return 0;
            }

        }
        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
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
                int winner = checkForWinner();
                if(winner ==0){
                    information.setText("Android\'s turn");
                    android_move();
                }else{
                    String message = "";
                    switch (winner){
                        case 1:
                            message = "It's a tie!";
                            Constanst.score_Ties++;
                            break;
                        case 2:
                            message = "You won!";
                            Constanst.score_Human++;
                            break;
                        case 3:
                            message = "Android won!";
                            Constanst.score_Android++;
                            break;
                    }
                    information.setText(message);
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
