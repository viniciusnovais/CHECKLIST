package br.com.pdasolucoes.checklist.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.test.espresso.core.deps.guava.util.concurrent.Service;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import br.com.pdasolucoes.checklist.model.Usuario;
import br.com.pdasolucoes.checklist.util.ServiceLogin;
import br.com.pdasolucoes.checklist.util.WebService;
import checklist.pdasolucoes.com.br.checklist.R;

import static android.R.attr.onClick;
import static android.R.attr.state_checkable;

public class MainActivity extends AppCompatActivity{

    private Button  btnLogin;
    private TextView textForgetPass;
    private EditText editEmail, editPassword;
    private Usuario usuario = new Usuario();
    public static final String PREF_NAME="MainActivityPreferences";
    private String email="",senha="";
    private JSONObject obj=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        setContentView(R.layout.activity_main);

        btnLogin= (Button) findViewById(R.id.btnLogin);
        textForgetPass= (TextView) findViewById(R.id.forgetPassword);
        editEmail= (EditText) findViewById(R.id.editEmail);
        editPassword= (EditText) findViewById(R.id.editPassword);

        SharedPreferences pref= getSharedPreferences(PREF_NAME,MODE_PRIVATE);

        //Trazendo das preferencias
        if (pref!=null) {
            //Email
            email=pref.getString("email","");
            usuario.setEmail(email);
            //Senha
            senha=pref.getString("senha","");
            usuario.setSenha(senha);

            if(email !="" && senha!="" ) {
                AsyncLogin task = new AsyncLogin();
                task.execute();
            }

        }

        //Primeiro acesso
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editEmail.getEditableText().toString();
                senha = editPassword.getEditableText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Preencha os Campos",
                            Toast.LENGTH_SHORT).show();
                } else {
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                  AsyncLogin task = new AsyncLogin();
                    task.execute();
                }
            }
        });

        textForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, ForgetPassActivity.class);
                startActivity(i);
            }
        });
    }

    public class AsyncLogin extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            obj = ServiceLogin.Login(usuario);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if (obj.getString("email").equals(email) && obj.getString("senha").equals(senha)) {

                    SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email);
                    editor.putString("senha", senha);
                    editor.putString("nome",obj.getString("nome"));
                    editor.commit();

                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    i.putExtra("nome", obj.getString("nome"));
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Usuário ou Senha incorreto", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception j) {
                j.printStackTrace();
            }
        }
    }

   }
