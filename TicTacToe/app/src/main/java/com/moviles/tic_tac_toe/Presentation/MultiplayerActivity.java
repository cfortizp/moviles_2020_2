package com.moviles.tic_tac_toe.Presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moviles.tic_tac_toe.R;
import com.moviles.tic_tac_toe.Util.BoardSpace;
import com.moviles.tic_tac_toe.Util.Constanst;
import com.moviles.tic_tac_toe.Util.Game;

import java.util.List;
import java.util.Random;

public class MultiplayerActivity extends AppCompatActivity {

    private BoardSpace board[];
    private TextView score_human;
    private TextView score_ties;
    private TextView score_android;
    private TextView information;
    private TextView Android_text;
    private Button play;
    MediaPlayer sound_move;
    MediaPlayer sound_win;
    MediaPlayer sound_lose;
    MediaPlayer sound_tie;
    private boolean mSoundOn = true;
    private SharedPreferences mPrefs;
    private boolean turno;
    private DatabaseReference mDatabase;
    private DatabaseReference gameReference;
    private Game game;
    private boolean isfull;
    private String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(Constanst.host) {
            game = new Game(Constanst.player_name);
            id = mDatabase.push().getKey();
            mDatabase.child("games").child(id).setValue(game);
            isfull = false;
        }else{
            game = new Game(Constanst.game_name);
            game.setOther(Constanst.player_name);
            id = Constanst.game_id;
            mDatabase.child("games").child(id).child("other").setValue(Constanst.player_name);
            mDatabase.child("games").child(id).child("full").setValue(true);

        }
        gameReference = FirebaseDatabase.getInstance().getReference().child("games").child(id);

        gameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean init = (Boolean) dataSnapshot.child("full").getValue();
                if(!(Boolean) dataSnapshot.child("finish").getValue()){

                if (!isfull && init) {
                    if(Constanst.host){
                        game.setOther((String) dataSnapshot.child("other").getValue());
                        Android_text.setText(game.getOther()+":");
                    }
                    game.setFull(init);
                    isfull = true;
                    turno = (Boolean) dataSnapshot.child("turno").getValue();
                    start_game();
                }
                if(game.isFinish()){
                    turno = (Boolean) dataSnapshot.child("turno").getValue();
                    start_game();
                    game.setFinish(false);
                    game.setAgain_other(false);
                    game.setAgain_host(false);
                }
                if(Constanst.host) {
                    if (init && (Boolean) dataSnapshot.child("turno").getValue()) {
                        opponent_move((List<Long>) dataSnapshot.child("board").getValue());
                    }
                }else{
                    if (init && ! (Boolean) dataSnapshot.child("turno").getValue()) {
                        opponent_move((List<Long>) dataSnapshot.child("board").getValue());
                    }
                }}else {
                    if ((Boolean) dataSnapshot.child("again_host").getValue() && (Boolean) dataSnapshot.child("again_other").getValue()) {
                        game.setAgain_other(false);
                        game.setAgain_host(false);
                        int random = new Random().nextInt(2);
                        if (random == 0) {
                            game.setTurno(false);
                        } else {
                            game.setTurno(true);
                        }
                        game.setFinish(false);
                        turno = game.getTurno();
                        start_game();
                        mDatabase.child("games").child(id).setValue(game);
                    }else {
                        if(game.isAgain_host() !=(Boolean) dataSnapshot.child("again_host").getValue()){
                        game.setAgain_host(true);
                        Toast toast = Toast.makeText(getApplicationContext(), game.getHost()+" wants to play again", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    if(game.isAgain_other() !=(Boolean) dataSnapshot.child("again_other").getValue()) {
                        game.setAgain_other(true);
                        Toast toast = Toast.makeText(getApplicationContext(), game.getOther()+" wants to play again", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    }
                    if(!game.isFinish()){
                        game.setFinish(true);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound", true);
        String difficultyLevel = mPrefs.getString("difficulty_level", "Expert");

        if (difficultyLevel.equals("Easy")) {
            Constanst.dificulty = 0;
        } else if (difficultyLevel.equals("Hard")) {
            Constanst.dificulty = 1;
        } else {
            Constanst.dificulty = 2;
        }



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
        Android_text = findViewById(R.id.android_text);
        information.setText("Waiting for an opponent!");
        if(Constanst.host){
            Android_text.setText("Opponent:");
        }else{
            Android_text.setText(game.getHost()+":");
        }

        play = findViewById(R.id.again);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setEnabled(false);
                play.setText("Play again!");
                if(Constanst.host){
                    game.setAgain_host(true);
                }else{
                    game.setAgain_other(true);
                }
                mDatabase.child("games").child(id).setValue(game);
            }
        });
        sound_move = MediaPlayer.create(getApplicationContext(), R.raw.move);
        sound_win = MediaPlayer.create(getApplicationContext(), R.raw.lose);
        sound_lose = MediaPlayer.create(getApplicationContext(), R.raw.win);
        sound_tie = MediaPlayer.create(getApplicationContext(), R.raw.tie);
        Constanst.score_Human = 0;
        Constanst.score_Android= 0;
        Constanst.score_Ties = 0;
        update_score();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.exit:
                showDialog(0);
                return true;
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();

        sound_move = MediaPlayer.create(getApplicationContext(), R.raw.move);
        sound_win = MediaPlayer.create(getApplicationContext(), R.raw.lose);
        sound_lose = MediaPlayer.create(getApplicationContext(), R.raw.win);
        sound_tie = MediaPlayer.create(getApplicationContext(), R.raw.tie);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sound_move.release();
        sound_win.release();
        sound_lose.release();
        sound_tie.release();
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case 1:

                builder.setTitle("Dificulty");

                final CharSequence[] levels = {"Easy","Hard","Expert"};

                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.

                builder.setSingleChoiceItems(levels, Constanst.dificulty,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                // TODO: Set the diff level of mGame based on which item was selected.

                                Constanst.dificulty = item;
                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;
            case 0:
                // Create the quit confirmation dialog

                builder.setMessage("Are you sure you want to quit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", null);
                dialog = builder.create();

                break;

        }

        return dialog;
    }
    private void start_game(){
        for(int i = 0; i< board.length;i++){
            board[i].getButton().setEnabled(true);
            board[i].setSelect(-1);
            board[i].getButton().setImageResource(0);
            board[i].getButton().setOnClickListener(new MultiplayerActivity.ButtonClickListener(i));
            game.setBoard(i,-1);
        }
        if(Constanst.host) {
            if (!turno) {
                information.setText(game.getOther() + " go first!");
            } else {
                information.setText("You go first!");
            }
        }else{
            if (!turno) {
                information.setText("You go first!");
            } else {
                information.setText(game.getHost() + " go first!");
            }
        }
    }

    private void opponent_move(List<Long> move){

        for(int i = 0; i<9;i++){
            int movement  = ((Long) move.get(i)).intValue();
            if(movement!=game.getBoard().get(i)){
                board[i].getButton().setEnabled(false);
                board[i].getButton().setImageResource(R.drawable.android);
                if(Constanst.host) {
                    board[i].setSelect(0);
                    game.setBoard(i, 0);
                }else{
                    board[i].setSelect(1);
                    game.setBoard(i, 1);
                }
            }
        }
        int winner = checkForWinner();
        if(winner ==0){
            if(mSoundOn) {
                sound_move.start();
            }
            information.setText("Your turn");
        }else{
            game.setFinish(true);
            mDatabase.child("games").child(id).setValue(game);
            String message = "";
            switch (winner){
                case 1:
                    message = "It's a tie!";
                    Constanst.score_Ties++;
                    if(mSoundOn) {
                        sound_tie.start();
                    }
                    break;
                case 2:
                    if(Constanst.host) {
                        message = "You won!";
                        if(mSoundOn){
                            sound_win.start();
                        }
                        Constanst.score_Human++;
                        message = mPrefs.getString("victory_message", message);
                    }else{
                        message = game.getHost()+" won!";
                        if(mSoundOn) {
                            sound_lose.start();
                        }
                        Constanst.score_Android++;
                    }


                    break;
                case 3:
                    if(Constanst.host) {
                        message = game.getOther()+ " won!";
                        if(mSoundOn){
                            sound_lose.start();
                        }
                        Constanst.score_Android++;
                    }else{
                        message = "You won!";
                        if(mSoundOn) {
                            sound_win.start();
                        }
                        Constanst.score_Human++;
                        message = mPrefs.getString("victory_message", message);
                    }

                    break;
            }
            information.setText(message);
            update_score();
            for(int i = 0; i< board.length;i++){
                board[i].getButton().setEnabled(false);
            }
            play.setEnabled(true);
        }
        if(Constanst.host) {
            turno = true;
        }else{
            turno = false;
        }

    }

    private void update_score(){
        score_human.setText(String.valueOf(Constanst.score_Human));
        score_android.setText(String.valueOf(Constanst.score_Android));
        score_ties.setText(String.valueOf(Constanst.score_Ties));
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
            if (board[location].getSelect() == -1) {
                if ((Constanst.host && turno) || (!Constanst.host && !turno)) {
                    if (Constanst.host) {
                        turno = false;
                    } else {
                        turno = true;
                    }
                    game.setTurno(turno);
                    board[location].getButton().setEnabled(false);
                    board[location].getButton().setImageResource(R.drawable.man);
                    if (Constanst.host) {
                        board[location].setSelect(1);
                        game.setBoard(location, 1);
                    } else {
                        board[location].setSelect(0);
                        game.setBoard(location, 0);
                    }
                    mDatabase.child("games").child(id).setValue(game);
                    int winner = checkForWinner();
                    if (winner == 0) {
                        if (mSoundOn) {
                            sound_move.start();
                        }
                        if(Constanst.host){
                            information.setText(game.getOther()+"\'s turn");
                        }else{
                            information.setText(game.getHost()+"\'s turn");
                        }

                    } else {
                        String message = "";
                        switch (winner) {
                            case 1:
                                message = "It's a tie!";
                                Constanst.score_Ties++;
                                if (mSoundOn) {
                                    sound_tie.start();
                                }
                                break;
                            case 2:
                                if(Constanst.host) {
                                    message = "You won!";
                                    if(mSoundOn){
                                        sound_win.start();
                                    }
                                    Constanst.score_Human++;
                                    message = mPrefs.getString("victory_message", message);
                                }else{
                                    message = game.getHost()+" won!";
                                    if(mSoundOn) {
                                        sound_lose.start();
                                    }
                                    Constanst.score_Android++;
                                }
                                break;
                            case 3:
                                if(Constanst.host) {
                                    message = game.getOther()+" won!";
                                    if(mSoundOn){
                                        sound_lose.start();
                                    }
                                    Constanst.score_Android++;
                                }else{
                                    message = "You won!";
                                    if(mSoundOn) {
                                        sound_win.start();
                                    }
                                    Constanst.score_Human++;
                                    message = mPrefs.getString("victory_message", message);
                                }
                                break;
                        }
                        information.setText(message);
                        update_score();
                        for (int i = 0; i < board.length; i++) {
                            board[i].getButton().setEnabled(false);
                        }
                        play.setEnabled(true);
                    }

                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CANCELED) {
            // Apply potentially new settings

            mSoundOn = mPrefs.getBoolean("sound", true);

            String difficultyLevel = mPrefs.getString("difficulty_level", "Expert");

            if (difficultyLevel.equals("Easy")) {
                Constanst.dificulty = 0;
            } else if (difficultyLevel.equals("Hard")) {
                Constanst.dificulty = 1;
            } else {
                Constanst.dificulty = 2;
            }

        }
    }
}
