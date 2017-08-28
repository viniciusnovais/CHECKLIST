package br.com.pdasolucoes.checklist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.pdasolucoes.checklist.adapter.SpinnerAdapterForm;
import br.com.pdasolucoes.checklist.adapter.SpinnerAdapterSetor;
import br.com.pdasolucoes.checklist.dao.FormDao;
import br.com.pdasolucoes.checklist.dao.SetorDao;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.Setor;
import br.com.pdasolucoes.checklist.util.MostraLogoCliente;
import br.com.pdasolucoes.checklist.util.VerificaConexao;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 01/11/2016.
 */

public class StartActivity extends AppCompatActivity {

    private ImageButton imageBtnStart, imageBtnHome, imageBtnAppointment;
    private EditText editSite, editData;
    int dia, mes, ano, hora, minuto;
    private Spinner spinnerForm, spinnerSetor;
    private List<Form> listForm;
    private FormDao dao = new FormDao(this);
    private SetorDao setorDao = new SetorDao(this);
    private SpinnerAdapterForm adapterSpinner;
    private SpinnerAdapterSetor adapterSetor;
    private List<Integer> listaInt;
    private int idFormSpinner = 0, idSetor;
    private List<Setor> listaSetor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_iniciar);
        MostraLogoCliente.mostra(this, getSupportActionBar().getCustomView());
        setContentView(R.layout.start_activity);

        imageBtnAppointment = (ImageButton) findViewById(R.id.appointmentBook);
        imageBtnStart = (ImageButton) findViewById(R.id.start);
        imageBtnHome = (ImageButton) findViewById(R.id.home);

        spinnerForm = (Spinner) findViewById(R.id.spinnerForm);
        spinnerSetor = (Spinner) findViewById(R.id.spinnerSetor);
        editSite = (EditText) findViewById(R.id.editSite);
        editData = (EditText) findViewById(R.id.editData);

        Calendar data = Calendar.getInstance();
        dia = data.get(Calendar.DAY_OF_MONTH);
        mes = data.get(Calendar.MONTH);
        ano = data.get(Calendar.YEAR);

        hora = data.get(Calendar.HOUR_OF_DAY);
        minuto = data.get(Calendar.MINUTE);

        adapterSpinner = new SpinnerAdapterForm(
                StartActivity.this,
                R.layout.custom_spinner_start, dao.listar());

    }

    @Override
    protected void onStart() {
        super.onStart();

        imageBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, HomeActivity.class);

                startActivity(i);
                finish();

            }
        });

        imageBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(StartActivity.this, QuestionsActivity.class);

                i.putExtra("idForm", idFormSpinner);
                i.putExtra("idSetor", idSetor);
                i.putExtra("nomeForm", adapterSpinner.getItem(spinnerForm.getSelectedItemPosition()).getNomeFom());
                i.putExtra("numero", spinnerForm.getSelectedItem().toString().lastIndexOf(spinnerForm.getSelectedItem().toString().length() - 1));
                i.putExtra("StartActivity", "StartActivity");

                Calendar data = Calendar.getInstance();
                dia = data.get(Calendar.DAY_OF_MONTH);
                mes = data.get(Calendar.MONTH);
                ano = data.get(Calendar.YEAR);

                hora = data.get(Calendar.HOUR_OF_DAY);
                minuto = data.get(Calendar.MINUTE);

                editData.setText(String.format("%02d/%02d/%04d %02d:%02d ", dia, mes + 1, ano, hora, minuto));

                i.putExtra("dataHora", String.format("%02d/%02d/%04d %02d:%02d ", dia, mes + 1, ano, hora, minuto));

                //criarNovoForm
                startActivity(i);
                finish();
            }
        });

        imageBtnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, AppointmentActivity.class);
                startActivity(i);

            }
        });

        //Vindo pela agenda
        if (getIntent().getExtras() != null) {

            adapterSpinner = new SpinnerAdapterForm(
                    StartActivity.this, R.layout.custom_spinner_start, dao.listar());
            adapterSpinner.setDropDownViewResource(R.layout.spinner_item_start);
            spinnerForm.setAdapter(adapterSpinner);
            spinnerForm.setSelection(getIntent().getExtras().getInt("position"));
            spinnerForm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Form f = adapterSpinner.getItem(position);
                    editSite.setText(f.getNomeLoja());
                    editData.setText(String.format("%02d/%02d/%04d %02d:%02d ", dia, mes + 1, ano, hora, minuto));
                    //pegando o id do Form selecionado
                    idFormSpinner = f.getIdForm();


                    listaSetor = new ArrayList<>();
                    listaSetor = setorDao.listar(idFormSpinner);
                    Setor s = new Setor();
                    s.setId(-1);
                    s.setNome("Tudo");
                    s.setIdForm(idFormSpinner);
                    s.setStatus(0);

                    listaSetor.add(s);

                    adapterSetor =
                            new SpinnerAdapterSetor(StartActivity.this, R.layout.custom_spinner_start, listaSetor);
                    adapterSetor.setDropDownViewResource(R.layout.spinner_item_start);
                    spinnerSetor.setAdapter(adapterSetor);
                    spinnerSetor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            Setor s = adapterSetor.getItem(position);

                            idSetor = s.getId();


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            //Vindo pelo Próprio iniciar
        } else {
            AsyncForm task = new AsyncForm();
            task.execute();
        }
    }

    public class AsyncForm extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences preferences = getSharedPreferences("MainActivityPreferences", MODE_PRIVATE);

            if (VerificaConexao.isNetworkConnected(StartActivity.this)) {
                listForm = dao.listarForms(preferences.getInt("idConta", 0));

                listaInt = new ArrayList<>();
                listaSetor = new ArrayList<>();
                for (Form f : listForm) {
                    listaInt.add(f.getIdForm());
                    listaSetor = setorDao.listaWeb(f.getIdForm());
                    setorDao.incluir(listaSetor);
                }

                dao.deleteForm(listaInt);
            } else {
                listForm = dao.listar();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            dao.incluir(listForm);

            //adiicionando forms na lista de spinner
            adapterSpinner = new SpinnerAdapterForm(
                    StartActivity.this,
                    R.layout.custom_spinner_start, listForm);

            adapterSpinner.setDropDownViewResource(R.layout.spinner_item_start);
            spinnerForm.setAdapter(adapterSpinner);

            spinnerForm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Form f = adapterSpinner.getItem(position);
                    editSite.setText(f.getNomeLoja());
                    editData.setText(String.format("%02d/%02d/%04d %02d:%02d ", dia, mes + 1, ano, hora, minuto));
                    //pegando o id do Form selecionado
                    idFormSpinner = f.getIdForm();

                    listaSetor = new ArrayList<>();
                    listaSetor = setorDao.listar(idFormSpinner);
                    Setor s = new Setor();
                    s.setId(-1);
                    s.setNome("Tudo");
                    s.setIdForm(idFormSpinner);
                    s.setStatus(0);

                    listaSetor.add(s);


                    adapterSetor =
                            new SpinnerAdapterSetor(StartActivity.this, R.layout.custom_spinner_start, listaSetor);
                    adapterSetor.setDropDownViewResource(R.layout.spinner_item_start);
                    spinnerSetor.setAdapter(adapterSetor);
                    spinnerSetor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            Setor s = adapterSetor.getItem(position);
                            idSetor = s.getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }
}
