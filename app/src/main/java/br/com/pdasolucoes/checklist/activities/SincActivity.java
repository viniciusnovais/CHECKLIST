package br.com.pdasolucoes.checklist.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.List;

import br.com.pdasolucoes.checklist.adapter.SyncAdapter;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.model.Form;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 01/11/2016.
 */

public class SincActivity extends AppCompatActivity {

    private ListView listView;;
    private SyncAdapter adapter2;
    private List<Form> listaForm;
    private FormItemDao dao;
    private TextView tvForm,tvPeriodo,tvSite,tvSync;
    String[] spinnerValuesForm = { "Form1", "Form2", "Form3","Form4", "Form5","Form6" };
    String [] spinnerSite={"Bela Vista","Mooca","Tatuapé","Higienópolis","Mauá","Brás"};
    String[] spinnerValuesPeriod = { "Dia","Semanal", "Quinzenal", "Mensal" };
    private String[] itemFiltroSelecao = new String[]{"Selecione uma Opção", "Selecionar", "Tudo"};
    private Spinner spinnerSync, mySpinner;
    private Button buttonSync;
    private RelativeLayout load;
    private Typeface tf;
    private int[] vetorCheckSelecionado;
    public static int POSITION;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        setContentView(R.layout.sinc_activity);

        tf= Typeface.createFromAsset(getAssets(),"OpenSans-Regular.ttf");
        tvForm= (TextView) findViewById(R.id.tvForm);
        tvForm.setTypeface(tf);
        tvPeriodo= (TextView) findViewById(R.id.tvPeriodo);
        tvPeriodo.setTypeface(tf);
        //tvSite.setTypeface(tf);
        tvSite= (TextView) findViewById(R.id.tvSite);
        tvSync= (TextView) findViewById(R.id.tvSync);
        tvSync.setTypeface(tf);

        listView=(ListView) findViewById(R.id.listViewSync);
        spinnerSync= (Spinner) findViewById(R.id.spinnerSync);

        buttonSync=(Button)findViewById(R.id.buttonSync);
        load= (RelativeLayout) findViewById(R.id.load);

        dao= new FormItemDao(this);

        mySpinner = (Spinner) findViewById(R.id.spinnerForm);
        final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>
                (this, R.layout.spinner_item,spinnerValuesForm);
        mySpinner.setAdapter(adapterSpinner);

        Spinner mySpinner2 = (Spinner) findViewById(R.id.spinnerPeriodo);
        ArrayAdapter<String> adapterSpinner2 = new ArrayAdapter<String>
                (this, R.layout.spinner_item,spinnerValuesPeriod);
        mySpinner2.setAdapter(adapterSpinner2);

        Spinner mySpinner3 = (Spinner) findViewById(R.id.spinnerSync);
        ArrayAdapter<String> adapterSpinner3 = new ArrayAdapter<String>
                (this, R.layout.spinner_item,itemFiltroSelecao);
        mySpinner3.setAdapter(adapterSpinner3);

        Spinner mySpinner4= (Spinner) findViewById(R.id.spinnerSite);
        ArrayAdapter<String> adapterSpinner4=new ArrayAdapter<String>
                (this,R.layout.spinner_item,spinnerSite);
        mySpinner4.setAdapter(adapterSpinner4);

        spinnerSync.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view,  final int position, long id) {

                //Guardo na variavel estatica para no adapter fazer a verificação se mostra ou não a check
                POSITION=position;
                    //Cria a lista na activity
                    adapter2= new SyncAdapter(getApplicationContext(),dao.listarItemFormSync());
                    listView.setAdapter(adapter2);

                adapter2.setOnItemClickListener(new SyncAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int[] vetorInt) {
                        vetorCheckSelecionado = vetorInt;
                    }
                });

                    buttonSync.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Verificar quais itens da listView estão checkados(checkbox)
                            // Pegar a posição dos itens checkado


                            Handler handler = new Handler();
                            handler.postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            load.setVisibility(View.GONE);
                                        }
                                    }, 3000L);
                            load.setVisibility(View.VISIBLE);
                        }
                    });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == adapterSpinner.getItemId(position)) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
