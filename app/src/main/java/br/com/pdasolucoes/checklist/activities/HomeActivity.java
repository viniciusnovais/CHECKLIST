package br.com.pdasolucoes.checklist.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import br.com.pdasolucoes.checklist.dao.FormDao;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.LogoDao;
import br.com.pdasolucoes.checklist.dao.OpcaoRespostaDao;
import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.dao.SetorDao;
import br.com.pdasolucoes.checklist.dao.TodoDao;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.Logo;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Setor;
import br.com.pdasolucoes.checklist.util.MostraLogoCliente;
import br.com.pdasolucoes.checklist.util.ReceberLogo;
import br.com.pdasolucoes.checklist.util.ServiceVerificaForm;
import br.com.pdasolucoes.checklist.util.VerificaConexao;
import checklist.pdasolucoes.com.br.checklist.R;


/**
 * Created by PDA on 31/10/2016.
 */

public class HomeActivity extends AbsRuntimePermission implements View.OnClickListener {

    private ImageButton imageBtnAppointment, imageBtnstart, imageBtnTodo, imageBtnSinc, imageBtnQuery, imageBtnLogout;
    private TextView tvWelcome, tvContadorAgenda, tvContadorTodo, tvContadorConsulta, tvContadorSync;
    public static final int REQUEST_PERMISSION = 10;
    private int ATUALIZA = 0;
    private FormDao formDao;
    private FormDao dao = new FormDao(this);
    private PerguntaDao perguntaDao = new PerguntaDao(this);
    private FormItemDao formItemDao;
    private TodoDao todoDao;
    private OpcaoRespostaDao opcaoRespostaDao = new OpcaoRespostaDao(this);
    private List<Form> listForm;
    private SetorDao setorDao = new SetorDao(this);
    private List<Setor> listaSetor = new ArrayList<>();
    private List<Integer> listaInt;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_home);
        MostraLogoCliente.mostra(this, getSupportActionBar().getCustomView());
        setContentView(R.layout.home_activity);
        formDao = new FormDao(this);
        formItemDao = new FormItemDao(this);
        todoDao = new TodoDao(this);

        requestAppPermissions(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,}, R.string.msg, REQUEST_PERMISSION);

        AsyncReceberDados task = new AsyncReceberDados();
        task.execute();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences(MainActivity.PREF_NAME, MODE_PRIVATE);
        tvContadorAgenda = (TextView) findViewById(R.id.tvContadorAgenda);
        tvContadorTodo = (TextView) findViewById(R.id.tvContadorTodo);
        tvContadorConsulta = (TextView) findViewById(R.id.tvContadorConsulta);
        tvContadorSync = (TextView) findViewById(R.id.tvContadorSync);

        if (formDao.contarForm() > 0) {
            tvContadorAgenda.setVisibility(View.VISIBLE);
            tvContadorAgenda.setText(formDao.contarForm() + "");
        }

        if (todoDao.contadorRespostaFaltaTodo(preferences.getInt("idUsuario", 0)) > 0) {
            tvContadorTodo.setVisibility(View.VISIBLE);
            tvContadorTodo.setText(todoDao.contadorRespostaFaltaTodo(preferences.getInt("idUsuario", 0)) + "");
        } else {
            tvContadorTodo.setVisibility(View.GONE);
        }

        if (formItemDao.contadorSync(preferences.getInt("idUsuario", 0)) > 0) {
            tvContadorSync.setVisibility(View.VISIBLE);
            tvContadorSync.setText(formItemDao.contadorSync(preferences.getInt("idUsuario", 0)) + "");
        }

        if (formItemDao.contadorFormItemConsulta(preferences.getInt("idUsuario", 0)) > 0) {
            tvContadorConsulta.setVisibility(View.VISIBLE);
            tvContadorConsulta.setText(formItemDao.contadorFormItemConsulta(preferences.getInt("idUsuario", 0)) + "");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        imageBtnAppointment = (ImageButton) findViewById(R.id.appointmentBook);
        imageBtnstart = (ImageButton) findViewById(R.id.start);
        imageBtnTodo = (ImageButton) findViewById(R.id.toDo);
        imageBtnSinc = (ImageButton) findViewById(R.id.sinc);
        imageBtnQuery = (ImageButton) findViewById(R.id.query);
        imageBtnLogout = (ImageButton) findViewById(R.id.logout);
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        SharedPreferences preferences = getSharedPreferences(MainActivity.PREF_NAME, MODE_PRIVATE);
        tvWelcome.setText(getResources().getString(R.string.welcome) + ", " + preferences.getString("nome", ""));

        imageBtnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, AppointmentActivity.class);
                startActivity(i);

            }
        });

        imageBtnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, StartActivity.class);
                startActivity(i);

            }
        });

        imageBtnTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ListaRespostaFaltaTodo.class);
                startActivity(i);

            }
        });

        imageBtnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ListaQuery.class);
                startActivity(i);

            }
        });

        imageBtnSinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SincActivity.class);
                startActivity(i);

            }
        });

        imageBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Logo teremos esse módulo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.btSair:

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Deseja realmente sair ?").setNegativeButton("Não", dialogClickListener)
                        .setPositiveButton("Sim", dialogClickListener).show();

                break;
            case R.id.btSobre:
                Toast.makeText(HomeActivity.this, "Logo teremos esse serviço pronto", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btAtualizar:
                deletarTodos();
                ATUALIZA = 1;
                AsyncReceberDados task = new AsyncReceberDados();
                task.execute();

        }
        return super.onOptionsItemSelected(item);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear().commit();
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };


    public class AsyncReceberDados extends AsyncTask<Objects, Integer, Long> {

        SharedPreferences preferences = getSharedPreferences("MainActivityPreferences", MODE_PRIVATE);
        int cnt = 0, totalSize = 0;
        List<Pergunta> listaPergunta = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(HomeActivity.this);
            if (VerificaConexao.isNetworkConnected(HomeActivity.this)) {
                if (getIntent().hasExtra("primeiravez") || ATUALIZA == 1) {
                    progressDialog.setMessage("Importando Dados...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }
        }

        @Override
        protected Long doInBackground(Objects... objectses) {

            //pegando Formulários
            if (VerificaConexao.isNetworkConnected(HomeActivity.this)) {
                if (getIntent().hasExtra("primeiravez") || ATUALIZA == 1) {
                    deletarTodos();
                    listForm = dao.listarForms(preferences.getInt("idConta", 0));
                    totalSize = listForm.size();
                    listaInt = new ArrayList<>();
                    cnt = 1;
                    for (Form f : listForm) {

                        listaInt.add(f.getIdForm());
                        listaSetor = setorDao.listaWeb(f.getIdForm());
                        setorDao.incluir(listaSetor);
                    }

                    //incluindo forms
                    dao.incluir(listForm);
                    //excluindo aqueles q já estao cadastrados
                    dao.deleteForm(listaInt);

                    //Pegando Perguntas
                    totalSize = 0;
                    progressDialog.setProgress(0);
                    for (Form f : listForm) {
                        listaPergunta = perguntaDao.listarPerguntas(f.getIdForm());
                        totalSize = listaPergunta.size();
                        perguntaDao.incluir(listaPergunta);
                    }

                    //pegando Opcoes das perguntas
                    for (Setor s : setorDao.listar()) {
                        List<Pergunta> lista = perguntaDao.listar(s.getId());
                        totalSize = 0;
                        progressDialog.setProgress(0);
                        for (int cnt = 0; cnt < lista.size(); cnt++) {
                            opcaoRespostaDao.incluir(opcaoRespostaDao.listarOpcaoResposta(lista.get(cnt).getIdPadraoResposta(), lista.get(cnt).getIdPergunta()));
                        }
                    }

                }
            }

            //onResume();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            progressDialog.dismiss();
            onResume();
        }
    }

    public void deletarTodos() {
        FormDao formDao = new FormDao(this);
        OpcaoRespostaDao opcaoRespostaDao = new OpcaoRespostaDao(this);
        PerguntaDao perguntaDao = new PerguntaDao(this);
        FormItemDao formItemDao = new FormItemDao(this);

        formDao.deletar();
        perguntaDao.deletar();
        opcaoRespostaDao.deletar();
        formItemDao.deletar();

    }

}
