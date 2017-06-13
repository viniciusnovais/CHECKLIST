package br.com.pdasolucoes.checklist.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.dao.FormDao;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.dao.TodoDao;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.Pergunta;
import checklist.pdasolucoes.com.br.checklist.R;


/**
 * Created by PDA on 31/10/2016.
 */

public class HomeActivity extends AbsRuntimePermission implements View.OnClickListener{

    private ImageButton imageBtnAppointment, imageBtnstart,imageBtnTodo,imageBtnSinc,imageBtnQuery,imageBtnLogout;
    private TextView tvWelcome, tvContadorAgenda,tvContadorTodo,tvContadorConsulta,tvContadorSync;
    public static final int REQUEST_PERMISSION = 10;
    private FormDao formDao;
    private TodoDao todoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_home);
        setContentView(R.layout.home_activity);
        formDao = new FormDao(this);
        todoDao = new TodoDao(this);

        tvContadorAgenda = (TextView) findViewById(R.id.tvContadorAgenda);
        tvContadorTodo = (TextView) findViewById(R.id.tvContadorTodo);
        tvContadorConsulta = (TextView) findViewById(R.id.tvContadorConsulta);
        tvContadorSync = (TextView) findViewById(R.id.tvContadorSync);

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

        if (formDao.contarForm()>0){
            tvContadorAgenda.setVisibility(View.VISIBLE);
            tvContadorAgenda.setText(formDao.contarForm()+"");
        }

        if (todoDao.contadorRespostaFaltaTodo()>0 ){
            tvContadorTodo.setVisibility(View.VISIBLE);
            tvContadorTodo.setText(todoDao.contadorRespostaFaltaTodo()+"");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        imageBtnAppointment= (ImageButton) findViewById(R.id.appointmentBook);
        imageBtnstart=(ImageButton) findViewById(R.id.start);
        imageBtnTodo=(ImageButton) findViewById(R.id.toDo);
        imageBtnSinc=(ImageButton)findViewById(R.id.sinc);
        imageBtnQuery=(ImageButton) findViewById(R.id.query);
        imageBtnLogout=(ImageButton)findViewById(R.id.logout);
        tvWelcome= (TextView) findViewById(R.id.tvWelcome);
        SharedPreferences preferences= getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
        tvWelcome.setText(getResources().getString(R.string.welcome)+", "+preferences.getString("nome",""));

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
                Intent i= new Intent(HomeActivity.this, ListaRespostaFaltaTodo.class);
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
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.btSair:

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Deseja realmente sair ?").setNegativeButton("Não", dialogClickListener)
                        .setPositiveButton("Sim", dialogClickListener).show();

                break;
            case R.id.btSobre:
                Toast.makeText(HomeActivity.this,"Logo teremos esse serviço pronto",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
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


    public class AsyncReceberDados extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            List<Pergunta> lista;
            PerguntaDao dao = new PerguntaDao(HomeActivity.this);
           // lista = dao.getPerguntaWS();

            return null;
        }
    }
}
