package com.moviles.companys.Presentation;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.moviles.companys.R;
import com.moviles.companys.Util.Constanst;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constanst.db = openOrCreateDatabase("companies",MODE_PRIVATE,null);
        Constanst.db.execSQL("CREATE TABLE IF NOT EXISTS CompanyClasification(id int PRIMARY KEY,name VARCHAR);");
        Constanst.db.execSQL("CREATE TABLE IF NOT EXISTS Companies(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, url VARCHAR,telephone VARCHAR,email VARCHAR, products VARCHAR, clasification INTEGER);");
        Constanst.db.execSQL("INSERT OR REPLACE INTO CompanyClasification(id , name) VALUES (1,'Consulting'),(2,'Custom Development'),(3,'Software Factory');");
        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListCompanyActivity.class);
                startActivityForResult(intent,0);
            }
        });

    }

}