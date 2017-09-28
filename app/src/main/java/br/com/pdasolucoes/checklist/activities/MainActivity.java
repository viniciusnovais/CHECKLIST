package br.com.pdasolucoes.checklist.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import br.com.pdasolucoes.checklist.dao.LogoDao;
import br.com.pdasolucoes.checklist.dao.UsuarioDao;
import br.com.pdasolucoes.checklist.model.Logo;
import br.com.pdasolucoes.checklist.model.Usuario;
import br.com.pdasolucoes.checklist.util.ReceberLogo;
import br.com.pdasolucoes.checklist.util.ServiceLogin;
import br.com.pdasolucoes.checklist.util.WebService;
import checklist.pdasolucoes.com.br.checklist.R;

import static android.R.attr.onClick;
import static android.R.attr.state_checkable;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView textForgetPass;
    private EditText editEmail, editPassword;
    private Usuario usuario = new Usuario();
    public static final String PREF_NAME = "MainActivityPreferences";
    private String email = "", senha = "";
    private SoapObject obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        textForgetPass = (TextView) findViewById(R.id.forgetPassword);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        //Trazendo das preferencias
        if (pref != null) {
            //Email
            email = pref.getString("email", "");
            usuario.setEmail(email);
            //Senha
            senha = pref.getString("senha", "");
            usuario.setSenha(senha);

            if (email != "" && senha != "") {
                AsyncLogin task = new AsyncLogin();
                task.execute("");
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
                    task.execute("primeiravez");
                }
            }
        });

        textForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ForgetPassActivity.class);
                startActivity(i);
            }
        });
    }

    public class AsyncLogin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                obj = ServiceLogin.invokeLoginWS(usuario);
                UsuarioDao usuarioDao = new UsuarioDao(MainActivity.this);
                for (int i = 0; i < obj.getPropertyCount(); i++) {
                    Usuario usuario1 = new Usuario();
                    usuario1.setId(Integer.parseInt(obj.getProperty("Codigo").toString()));
                    usuario1.setNome(obj.getProperty("Nome").toString());
                    usuario1.setEmail(usuario.getEmail());
                    usuario1.setSenha(usuario.getSenha());

                    usuarioDao.incluir(usuario1);
                }

                //Receber Logo
                Logo logo = new Logo();
                LogoDao logoDao = new LogoDao(MainActivity.this);

                logo.setIdUsuario(Integer.parseInt(obj.getProperty("Codigo").toString()));
                logo.setImagem(getBitmapAsByteArray(ReceberLogo.logo(Integer.parseInt(obj.getProperty("Codigo").toString()))));

                logoDao.incluir(logo);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                if (Integer.parseInt(obj.getProperty("Codigo").toString()) > 0) {

                    SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email);
                    editor.putString("senha", senha);
                    editor.putInt("idUsuario", Integer.parseInt(obj.getProperty("Codigo").toString()));
                    editor.putString("nome", obj.getProperty("Nome").toString());
                    editor.commit();

                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    i.putExtra("nome", obj.getProperty("Nome").toString());
                    if (s != "") {
                        i.putExtra("primeiravez", s);
                    }
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Usuário ou Senha incorreto", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Usuário ou Senha incorreto", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
