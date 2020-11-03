package com.moviles.companys.Presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moviles.companys.R;
import com.moviles.companys.Util.Adaptador;
import com.moviles.companys.Util.Constanst;

import java.util.ArrayList;

public class ListCompanyActivity extends AppCompatActivity {

    private Button search;
    private Button add;
    private ListView lista;
    private ArrayList<ArrayList<String>> companies = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcompany);
        search = findViewById(R.id.search);
        add = findViewById(R.id.add);
        Cursor resultSet = Constanst.db.rawQuery("SELECT a.id, a.name,a.clasification, b.name FROM Companies a, CompanyClasification b WHERE a.clasification = b.id",null);
        while (resultSet.moveToNext()) {
            ArrayList<String> information = new ArrayList<>();
            information.add(resultSet.getString(0));
            information.add(resultSet.getString(1));
            information.add(resultSet.getString(2));
            information.add(resultSet.getString(3));
            companies.add(information);
        }
        load();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constanst.company.setId(-1);
                Intent intent = new Intent(getApplicationContext(), UpsertCompanyActivity.class);
                startActivityForResult(intent,0);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });
    }
    void load(){
        lista = (ListView) findViewById(R.id.lvLista);
        lista.setAdapter(new Adaptador(this, companies));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CompanyActivity.class);
                Constanst.company.setId(Integer.parseInt(companies.get(position).get(0)));
                Constanst.company.setClassification(companies.get(position).get((3)));
                Constanst.company.setIdclassification(Integer.parseInt(companies.get(position).get((2))));
                startActivityForResult(intent,0);
            }
        });


    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Constanst.search_name = "";
        Constanst.search_classification = 0;
        ArrayList<String> classifications = new ArrayList<>();
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search");
        LayoutInflater factory = LayoutInflater.from(this);
        final View search = factory.inflate(R.layout.search,null);
        final Spinner clasification = search.findViewById(R.id.clasification);
        builder.setView(search);
        Cursor resultSet = Constanst.db.rawQuery("SELECT * FROM CompanyClasification",null);
        classifications.add("Select One");
        while (resultSet.moveToNext()) {
            classifications.add(resultSet.getString(1));
        }
        clasification.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,classifications));
        clasification.setSelection(0);
        builder.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Constanst.search_name = String.valueOf(((EditText) search.findViewById(R.id.name)).getText());
                Constanst.search_classification = clasification.getSelectedItemPosition();
                search();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dialog = builder.create();
        return dialog;
    }

    public void search(){
        String sql = "SELECT a.id, a.name,a.clasification, b.name FROM Companies a, CompanyClasification b WHERE a.clasification = b.id";
        sql += " AND a.name LIKE \"%"+Constanst.search_name+"%\"";
        if(Constanst.search_classification!=0){
            sql+= " AND a.clasification = "+Constanst.search_classification;
        }
        sql+=";";
        companies = new ArrayList<>();
        Cursor resultSet = Constanst.db.rawQuery(sql,null);
        while (resultSet.moveToNext()) {
            ArrayList<String> information = new ArrayList<>();
            information.add(resultSet.getString(0));
            information.add(resultSet.getString(1));
            information.add(resultSet.getString(2));
            information.add(resultSet.getString(3));
            companies.add(information);
        }
        load();
    }
}
