package com.moviles.companys.Presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moviles.companys.R;
import com.moviles.companys.Util.Company;
import com.moviles.companys.Util.Constanst;

import java.util.ArrayList;

public class CompanyActivity extends AppCompatActivity {
    private Button back;
    private Button delete;
    private Button edit;
    private ImageView image;
    private TextView name;
    private TextView clasification;
    private TextView url;
    private TextView email;
    private TextView phone;
    private TextView services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        clasification = findViewById(R.id.clasification);
        url = findViewById(R.id.url);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        services = findViewById(R.id.services);
        String sql = "SELECT name,url,telephone,email,products FROM Companies WHERE id ="+Integer.toString(Constanst.company.getId());
        Cursor resultSet = Constanst.db.rawQuery(sql,null);
        while (resultSet.moveToNext()) {
            Constanst.company.setName(resultSet.getString(0));
            Constanst.company.setUrl(resultSet.getString(1));
            Constanst.company.setPhone(resultSet.getString(2));
            Constanst.company.setEmail(resultSet.getString(3));
            Constanst.company.setServices(resultSet.getString(4));
        }
        name.setText(Constanst.company.getName());
        url.setText(Constanst.company.getUrl());
        email.setText(Constanst.company.getEmail());
        phone.setText(Constanst.company.getPhone());
        services.setText(Constanst.company.getServices());
        clasification.setText(Constanst.company.getClassification());
        switch (Constanst.company.getIdclassification()){
            case 1:
                image.setImageResource(R.drawable.consulting);
                break;
            case 2:
                image.setImageResource(R.drawable.development);
                break;
            case 3:
                image.setImageResource(R.drawable.factory);
                break;
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListCompanyActivity.class);
                startActivityForResult(intent,0);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpsertCompanyActivity.class);
                startActivityForResult(intent,0);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });



    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case 0:
                builder.setMessage("Are you sure you want to eliminate the company?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete();
                                Intent intent = new Intent(getApplicationContext(), ListCompanyActivity.class);
                                startActivityForResult(intent,0);
                            }
                        })
                        .setNegativeButton("No", null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    public boolean delete(){
        return Constanst.db.delete("Companies", "id" + "=" + Constanst.company.getId(), null) > 0;
    }
}
