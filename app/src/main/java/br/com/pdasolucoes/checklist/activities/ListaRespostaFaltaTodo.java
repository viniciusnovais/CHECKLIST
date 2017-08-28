package br.com.pdasolucoes.checklist.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import br.com.pdasolucoes.checklist.adapter.ListaRespostaFaltaTodoAdapter;
import br.com.pdasolucoes.checklist.adapter.Question2Adapter;
import br.com.pdasolucoes.checklist.dao.ComplementoRespostaDao;
import br.com.pdasolucoes.checklist.dao.ComplementoTodoDao;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.dao.TodoDao;
import br.com.pdasolucoes.checklist.model.ComplementoResposta;
import br.com.pdasolucoes.checklist.model.ComplementoTodo;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Resposta;
import br.com.pdasolucoes.checklist.util.ComponenteResposta;
import br.com.pdasolucoes.checklist.util.CriaTodo;
import br.com.pdasolucoes.checklist.util.CustomRecyclerView;
import br.com.pdasolucoes.checklist.util.MostraLogoCliente;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 30/05/2017.
 */

public class ListaRespostaFaltaTodo extends AppCompatActivity {


    private ListView listView;
    private ListaRespostaFaltaTodoAdapter adapter;
    private RespostaDao respostaDao;
    private ComplementoRespostaDao complementoRespostaDao;
    private ComplementoTodoDao complementoTodoDao;
    private TodoDao todoDao;
    private FormItemDao formItemDao;
    private ImageView image;
    private TextView tvNumeroNotificaCamera;
    private Button btEnviarFoto, btCancelarImagem;
    private Bitmap originalBitmap, resizedBitmap, photo;
    private AlertDialog dialog, dialogOpcoes;
    private Resposta r;
    private Uri uriImagem;
    private byte[] imageByte;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_todo);
        MostraLogoCliente.mostra(this, getSupportActionBar().getCustomView());
        setContentView(R.layout.lista_resposta_falta_todo_activity);
        respostaDao = new RespostaDao(this);
        complementoTodoDao = new ComplementoTodoDao(this);
        todoDao = new TodoDao(this);
        complementoRespostaDao = new ComplementoRespostaDao(this);
        formItemDao = new FormItemDao(this);

        SharedPreferences preferences = getSharedPreferences(MainActivity.PREF_NAME, MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.listView);

        adapter = new ListaRespostaFaltaTodoAdapter(respostaDao.listaFaltaTodo(preferences.getInt("idUsuario", 0)), this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                r = (Resposta) adapter.getItem(position);

                CriaTodo.criaTodo(ListaRespostaFaltaTodo.this, r.getIdPergunta(), r.getIdFormItem(), r.getIdResposta());


            }
        });


    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final View view = getLayoutInflater().inflate(R.layout.frame_image_todo, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        image = (ImageView) view.findViewById(R.id.image);
        btEnviarFoto = (Button) view.findViewById(R.id.btEnviarFoto);
        btCancelarImagem = (Button) view.findViewById(R.id.btCancelar);
        final ComplementoResposta cr = new ComplementoResposta();
        final ComplementoTodo ct = new ComplementoTodo();
        //Colocar imagem da Camera no Dialog
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                //Trazer imagem da galeria e colocar no dialog

                case 2:
                    photo = (Bitmap) data.getExtras().get("data");
                    originalBitmap = photo;
                    resizedBitmap = Bitmap.createScaledBitmap(
                            originalBitmap, 530, 530, false);

                    imageByte = getBitmapAsByteArray(originalBitmap);

                    image.setImageBitmap(resizedBitmap);

                    dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setLayout(700, 800);
                    break;
                case 3:
                    String[] colunaCaminhoArquivoo = {MediaStore.Images.Media.DATA};
                    uriImagem = data.getData();
                    Cursor cursorr = getContentResolver().query(
                            uriImagem, colunaCaminhoArquivoo, null, null, null);
                    cursorr.moveToFirst();
                    int colunaIndexx = cursorr
                            .getColumnIndex(colunaCaminhoArquivoo[0]);
                    String caminhoImagemm = cursorr.getString(colunaIndexx);
                    cursorr.close();
                    uriImagem = Uri.fromFile(new File(caminhoImagemm));
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImagem);
                        originalBitmap = photo;
                        resizedBitmap = Bitmap.createScaledBitmap(
                                originalBitmap, 530, 530, false);
                        image.setImageBitmap(resizedBitmap);
                        complementoTodoDao.incluir(ct);

                        dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setLayout(700, 800);
                    } catch (Exception e) {
                    }
                    break;
            }
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcoes.show();
            }
        });


        btCancelarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Salvou", Toast.LENGTH_SHORT).show();

                if (requestCode == 2 || requestCode == 3) {
                    ct.setImage(imageByte);
                    ct.setIdTodo(todoDao.ultimoId() + 1);

                    complementoTodoDao.incluir(ct);
                } else {
                    cr.setImage(imageByte);
                    cr.setIdPergunta(r.getIdPergunta());
                    cr.setIdFormItem(r.getIdFormItem());

                    complementoRespostaDao.incluir(cr);

                }

                CriaTodo.atualizaNotificacoesTempoRealCamera(getApplicationContext(), r.getIdFormItem(), r.getIdPergunta());

                dialog.dismiss();

            }
        });

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
