package com.moviles.companys.Presentation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moviles.companys.R;
import com.moviles.companys.Util.Constanst;

import java.util.ArrayList;

public class UpsertCompanyActivity extends AppCompatActivity {
    private Button back;
    private Button add;
    private EditText name;
    private Spinner clasification;
    private EditText url;
    private EditText email;
    private EditText phone;
    private EditText services;
    private ArrayList<String> classifications = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsertcompany);
        back = findViewById(R.id.back);
        add = findViewById(R.id.add);
        name = findViewById(R.id.name);
        clasification = findViewById(R.id.clasification);
        url = findViewById(R.id.url);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        services = findViewById(R.id.services);
        Cursor resultSet = Constanst.db.rawQuery("SELECT * FROM CompanyClasification",null);
        classifications.add("Select One");
        while (resultSet.moveToNext()) {
            classifications.add(resultSet.getString(1));
        }
        clasification.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,classifications));
        clasification.setSelection(0);
        if(Constanst.company.getId()!=-1){
            add.setText("Update");
            name.setText(Constanst.company.getName());
            url.setText(Constanst.company.getUrl());
            email.setText(Constanst.company.getEmail());
            phone.setText(Constanst.company.getPhone());
            services.setText(Constanst.company.getServices());
            clasification.setSelection(Constanst.company.getIdclassification());
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constanst.company.getId()!=-1){
                    Intent intent = new Intent(getApplicationContext(), CompanyActivity.class);
                    startActivityForResult(intent,0);
                }else {
                    Intent intent = new Intent(getApplicationContext(), ListCompanyActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constanst.company.setName(String.valueOf(name.getText()));
                Constanst.company.setUrl(String.valueOf(url.getText()));
                Constanst.company.setEmail(String.valueOf(email.getText()));
                Constanst.company.setPhone(String.valueOf(phone.getText()));
                Constanst.company.setServices(String.valueOf(services.getText()));
                Constanst.company.setIdclassification(clasification.getSelectedItemPosition());
                Constanst.company.setClassification(classifications.get(clasification.getSelectedItemPosition()));

                if(Constanst.company.getId()==-1){
                    String sql = "INSERT INTO Companies (name,url,telephone,email,products,clasification) VALUES('"+Constanst.company.getName()+"','"+Constanst.company.getUrl()+"','"+Constanst.company.getPhone()+"','"
                    +Constanst.company.getEmail()+"','"+Constanst.company.getServices()+"',"+Constanst.company.getIdclassification()+");";
                    Constanst.db.execSQL(sql);
                    Cursor resultSet = Constanst.db.rawQuery("SELECT last_insert_rowid();",null);
                    while (resultSet.moveToNext()) {
                        Constanst.company.setId(Integer.parseInt(resultSet.getString(0)));
                    }
                }else{
                    String sql = "UPDATE Companies SET name = '"+Constanst.company.getName()+"', url ='"+Constanst.company.getUrl()+"',telephone = '"+Constanst.company.getPhone()+"'," +
                            "email = '"+Constanst.company.getEmail()+"',products = '"+Constanst.company.getServices()+"',clasification = "+Constanst.company.getIdclassification()+" WHERE id = "+Constanst.company.getId()+";";
                    Constanst.db.execSQL(sql);
                }
                Intent intent = new Intent(getApplicationContext(), CompanyActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }
}
