package com.moviles.webservices;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner departamenos_spinner;
    private ArrayList<String> departamentoss;
    private ArrayAdapter<String> departamenos_adapter;
    private Spinner municipios_spinner;
    private ArrayList<String> municipioss;
    private ArrayAdapter<String> municipios_adapter;
    private ListView list;
    private ArrayList<String> peajes;
    private ArrayAdapter<String> cod_adapter;
    private Context context = this;
    private RequestQueue queue;
    private String[] categorias = {"i","ii","iii","iv","v","vi","vii","viii","ix"};
    private TextView n_peajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        departamentoss = new ArrayList<>();
        municipioss = new ArrayList<>();
        peajes = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        String url = "https://hermes.invias.gov.co/arcgis/rest/services/Mapa_Carreteras/Mapa_de_Carreteras/MapServer/1/query?where=1%3D1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=territoria&returnGeometry=false&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=territoria&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=true&resultOffset=&resultRecordCount=&f=pjson";
        departamenos_spinner = (Spinner) findViewById(R.id.departamentos);
        municipios_spinner = (Spinner) findViewById(R.id.municipios);
        n_peajes = findViewById(R.id.zona_postal);
        list = findViewById(R.id.list);
        JsonObjectRequest departamentos = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonResponse.getJSONArray("features");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject department = jsonArray.getJSONObject(i);
                                String departamento =department.getJSONObject("attributes").getString("territoria");
                                if(departamento.equals(departamento.toUpperCase())){
                                    departamentoss.add(departamento);
                                }
                            }
                            departamenos_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, departamentoss);
                            departamenos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            departamenos_spinner.setAdapter(departamenos_adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        departamenos_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                municipioss.clear();
                String tmp = (String) parent.getItemAtPosition(pos);
                String url = "https://hermes.invias.gov.co/arcgis/rest/services/Mapa_Carreteras/Mapa_de_Carreteras/MapServer/1/query?where=territoria+%3D+%27"+tmp+"%27&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=sector&returnGeometry=false&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=sector&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=true&resultOffset=&resultRecordCount=&f=pjson";
                Log.d("URLLLL",url);
                JsonObjectRequest municipios = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response.toString());
                                    JSONArray jsonArray = jsonResponse.getJSONArray("features");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject muni = jsonArray.getJSONObject(i);
                                        String municipio =muni.getJSONObject("attributes").getString("sector");
                                        municipioss.add(municipio);
                                    }
                                    municipios_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, municipioss);
                                    municipios_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    municipios_spinner.setAdapter(municipios_adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                queue.add(municipios);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        municipios_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                peajes.clear();
                final String tmp = (String) parent.getItemAtPosition(pos);
                final String dep = (String) departamenos_spinner.getSelectedItem();
                String url = "https://hermes.invias.gov.co/arcgis/rest/services/Mapa_Carreteras/Mapa_de_Carreteras/MapServer/1/query?where=territoria+%3D+%27"+dep+"%27+AND+sector+%3D+%27"+tmp+"%27&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=nombre%2Cadministra%2Cterritoria%2Csector%2Cubicacion%2Ccat_1%2Ccat_2%2Ccat_3%2Ccat_4%2Ccat_5%2Ccat_6%2Ccat_7%2Ccat_8%2Ccat_9%2Cd_cat_i%2Cd_cat_ii%2Cd_cat_iii%2Cd_cat_iv%2Cd_cat_v%2Cd_cat_vi%2Cd_cat_vii%2Cd_cat_viii%2Cd_cat_ix&returnGeometry=false&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&f=pjson";
                JsonObjectRequest codes = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response.toString());
                                    JSONArray jsonArray = jsonResponse.getJSONArray("features");
                                    n_peajes.setText(Integer.toString(jsonArray.length()));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i).getJSONObject("attributes");
                                        String tmp2 = "Nombre: "+obj.getString("nombre") +"\n";
                                        tmp2 += "Ubicación: "+obj.getString("ubicacion") +"\n";
                                        tmp2 += "Precios:\n";
                                        for(int k = 0; k<9;k++){
                                            String column = "cat_"+(k+1);
                                            int value = obj.getInt(column);
                                            if(value!=0){
                                                column = "d_cat_"+categorias[k];
                                                tmp2 += "   Categoria "+categorias[k].toUpperCase()+": "+value+"\n";
                                            }
                                        }
                                        peajes.add(tmp2);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                cod_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, peajes);
                                list.setAdapter(cod_adapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQ", "bad");
                            }
                        });
                queue.add(codes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        queue.add(departamentos);

    }
}
