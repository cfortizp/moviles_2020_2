package com.moviles.tic_tac_toe.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moviles.tic_tac_toe.R;
import com.moviles.tic_tac_toe.Util.Adaptador;
import com.moviles.tic_tac_toe.Util.Constanst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListGameActivity extends AppCompatActivity {

    private ListView lista;
    private ArrayList<ArrayList<String>> games = new ArrayList<>();
    private DatabaseReference gameReference;
    private TextView name;
    private TextView new_game;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listgames);
        name = findViewById(R.id.name);
        new_game = findViewById(R.id.new_game);
        name.setText(Constanst.player_name);

        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constanst.host = true;
                Intent intent = new Intent(getApplicationContext(), MultiplayerActivity.class);
                startActivityForResult(intent,0);
            }
        });


        gameReference = FirebaseDatabase.getInstance().getReference().child("games");
        gameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(!(Boolean) ds.child("full").getValue()){
                        ArrayList<String> information = new ArrayList<>();
                        information.add(ds.getKey());
                        information.add((String) ds.child("name").getValue());
                        information.add((String) ds.child("host").getValue());
                        games.add(information);
                    }
                }
                Log.d("AAAAAAAAAAAAAAAAAAAAA",games.toString());
                load();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });





    }

    void load(){



        lista = (ListView) findViewById(R.id.lvLista);
        lista.setAdapter(new Adaptador(this, games));

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constanst.host = false;
                Constanst.game_id = games.get(position).get(0);
                Constanst.game_name = games.get(position).get(2);
                Intent intent = new Intent(getApplicationContext(), MultiplayerActivity.class);
                startActivityForResult(intent,0);
            }
        });


    }
}
