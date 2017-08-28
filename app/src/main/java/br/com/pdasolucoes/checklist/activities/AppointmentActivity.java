package br.com.pdasolucoes.checklist.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.adapter.AppointmentAdapter;
import br.com.pdasolucoes.checklist.dao.FormDao;
import br.com.pdasolucoes.checklist.dao.SetorDao;
import br.com.pdasolucoes.checklist.model.Agenda;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.Setor;
import br.com.pdasolucoes.checklist.util.MostraLogoCliente;
import br.com.pdasolucoes.checklist.util.VerificaConexao;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 01/11/2016.
 */

public class AppointmentActivity extends AppCompatActivity {

    private ListView listView;
    private AppointmentAdapter adapter;
    private List<Form> listForm;
    private FloatingActionButton fab;
    private FormDao dao = new FormDao(this);
    private SetorDao setorDao = new SetorDao(this);
    private List<Setor> listaSetor = new ArrayList<>();
    private TextView tvForm, tvPeriodo;
    private String[] spinnerValuesPeriod = {"Dia", "Semanal", "Quinzenal", "Mensal"};
    private Typeface tf;
    private List<Integer> listaInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_agenda);
        MostraLogoCliente.mostra(this, getSupportActionBar().getCustomView());
        setContentView(R.layout.appointment_activity);


        listView = (ListView) findViewById(R.id.listViewAppointment);
        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        tvForm = (TextView) findViewById(R.id.tvForm);
        tvForm.setTypeface(tf);
        tvPeriodo = (TextView) findViewById(R.id.tvPeriodo);
        tvPeriodo.setTypeface(tf);

        fab = (FloatingActionButton) findViewById(R.id.floatAction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Adicionar um novo item na lista", Toast.LENGTH_SHORT).show();
            }
        });


        Spinner mySpinner2 = (Spinner) findViewById(R.id.spinnerPeriodo);
        ArrayAdapter<String> adapterSpinner2 = new ArrayAdapter<String>
                (this, R.layout.custom_spinner, spinnerValuesPeriod);
        adapterSpinner2.setDropDownViewResource(R.layout.spinner_item);
        mySpinner2.setAdapter(adapterSpinner2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent i = new Intent(AppointmentActivity.this, StartActivity.class);

                i.putExtra("form", dao.listar().get(position).getNomeFom().toString());
                i.putExtra("position", position);
                i.putExtra("site", dao.listar().get(position).getNomeLoja().toString());
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter = new AppointmentAdapter(dao.listar(), getApplicationContext());
        listView.setAdapter(adapter);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinnerForm);
        final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>
                (AppointmentActivity.this, R.layout.custom_spinner, dao.listarForm());
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item);
        mySpinner.setAdapter(adapterSpinner);
    }

    public class AsyncForm extends AsyncTask {

        SharedPreferences preferences = getSharedPreferences("MainActivityPreferences", MODE_PRIVATE);

        @Override
        protected Object doInBackground(Object[] params) {


//            if (VerificaConexao.isNetworkConnected(AppointmentActivity.this)) {
//                listForm = dao.listarForms(preferences.getInt("idUsuario", 0));
//                listaInt = new ArrayList<>();
//                for (Form f : listForm) {
//                    listaInt.add(f.getIdForm());
//                    listaSetor = setorDao.listaWeb(f.getIdForm());
//                    setorDao.incluir(listaSetor);
//                }
//
//                dao.deleteForm(listaInt);
//            } else {
//                listForm = dao.listar();
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            //dao.incluir(listForm);



        }
    }
}