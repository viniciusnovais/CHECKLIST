package br.com.pdasolucoes.checklist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.pdasolucoes.checklist.adapter.Question2Adapter;
import br.com.pdasolucoes.checklist.dao.ComplementoRespostaDao;
import br.com.pdasolucoes.checklist.dao.ComplementoTodoDao;
import br.com.pdasolucoes.checklist.dao.FormDao;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.dao.TodoDao;
import br.com.pdasolucoes.checklist.model.ComplementoResposta;
import br.com.pdasolucoes.checklist.model.ComplementoTodo;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Setor;
import br.com.pdasolucoes.checklist.util.ComponenteResposta;
import br.com.pdasolucoes.checklist.util.CriaTodo;
import br.com.pdasolucoes.checklist.util.CustomLinearLayoutManager;
import br.com.pdasolucoes.checklist.util.CustomRecyclerView;
import br.com.pdasolucoes.checklist.util.MostraLogoCliente;
import br.com.pdasolucoes.checklist.util.VerificaConexao;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 07/11/2016.
 */

public class QuestionsActivity extends AppCompatActivity {

    private TextView textBar, textMax, tvNomeFormAction, tvNumeroNotificaCamera, getTvNumeroNotificaComment, tvNumeroNotificaNotify, tvNotificaTodo, btDataNasc, btHora;
    private int teste = 0, cntNotifica = 1;
    private Button btEnviarFoto, btEnviarComment, btEnviarTodo, btCancelarComentario, btCancelarImagem;
    private ImageView imageCamera, imageTodo, image, imageComment, imageHelp, imageNotifica, arrowRight, arrowLeft;
    private final int CAMERA = 0, GALERIA = 1;
    private Bitmap originalBitmap, resizedBitmap, photo;
    private AlertDialog dialog, dialogOpcoes, dialogComment, dialogHelp;
    private Uri uriImagem;
    private List<Pergunta> listaPergunta;
    private PerguntaDao dao;
    private RespostaDao daoResposta;
    private CustomRecyclerView mRecyclerView;
    private Question2Adapter adapter;
    private TodoDao daoTodo;
    private FormItem formItem;
    private FormItemDao daoItemForm;
    private String nomeFormAction = "";
    private static int POSITION = 0, IDFORMITEM = 0, IDPERGUNTA;
    private ComplementoRespostaDao complementoRespostaDao;
    private ComplementoTodoDao complementoTodoDao;
    final ComponenteResposta cr = new ComponenteResposta();
    private LinearLayout ll;
    private AlertDialog dialogProgress;
    private ProgressBar progressBar, progressBarQuestao;
    private byte[] imageByte;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_formulario);
        MostraLogoCliente.mostra(this, getSupportActionBar().getCustomView());
        tvNomeFormAction = (TextView) findViewById(R.id.nomeForm);
        nomeFormAction = getIntent().getExtras().getString("nomeForm");
        setContentView(R.layout.question_activity);
        tvNomeFormAction.setText(nomeFormAction);

        dao = new PerguntaDao(this);
        daoResposta = new RespostaDao(this);
        daoTodo = new TodoDao(this);
        complementoRespostaDao = new ComplementoRespostaDao(this);
        complementoTodoDao = new ComplementoTodoDao(this);

        //alert progressBar
        AlertDialog.Builder builderProgress = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.progress_bar, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        builderProgress.setView(view);
        dialogProgress = builderProgress.create();

        AsyncPergunta task = new AsyncPergunta();
        task.execute();

        ll = (LinearLayout) findViewById(R.id.erroFormulario);

        textBar = (TextView) findViewById(R.id.textBar);
        textMax = (TextView) findViewById(R.id.textMax);

        progressBarQuestao = (ProgressBar) findViewById(R.id.progressBar);
        imageCamera = (ImageView) findViewById(R.id.camera);
        tvNumeroNotificaCamera = (TextView) findViewById(R.id.numeroNotificaCamera);
        getTvNumeroNotificaComment = (TextView) findViewById(R.id.numeroNotificaComment);
        tvNumeroNotificaNotify = (TextView) findViewById(R.id.numeroNotificaNotify);
        tvNotificaTodo = (TextView) findViewById(R.id.numeroNotificaTodo);
        imageTodo = (ImageView) findViewById(R.id.imagetoDo);
        imageHelp = (ImageView) findViewById(R.id.help);
        imageNotifica = (ImageView) findViewById(R.id.bell);

        arrowLeft = (ImageView) findViewById(R.id.arrowLeft);
        arrowRight = (ImageView) findViewById(R.id.arrowRight);

        formItem = new FormItem();
        daoItemForm = new FormItemDao(this);

        textBar.setText(daoResposta.contadorResposta(getIntent().getExtras().getInt("ItemId")) + "");
        progressBarQuestao.setProgress(daoResposta.contadorResposta(getIntent().getExtras().getInt("ItemId")));

        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        cr.pegaTodo.Pega(false);
                        arrowLeft.setVisibility(View.VISIBLE);
                        //pegar a posição atual + 1
                        teste++;
                        mRecyclerView.scrollToPosition(teste);

                        if (teste == (mRecyclerView.getAdapter().getItemCount() - 1)) {
                            arrowRight.setVisibility(View.GONE);
                        }
                    }
                }, 100);
            }
        });

        arrowLeft.setVisibility(View.GONE);
        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cr.pegaTodo.Pega(false);
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    //pegar a posição atual e -1
                    public void run() {
                        arrowRight.setVisibility(View.VISIBLE);
                        teste--;
                        mRecyclerView.scrollToPosition(teste);

                        if (teste == 0) {
                            arrowLeft.setVisibility(View.GONE);
                        }
                    }
                }, 100);
            }
        });

        cr.PegaQtdeRespostaListener(new ComponenteResposta.PegaQtdeResposta() {
            @Override
            public void Pega(int valor) {
                textBar.setText(valor + "");
                progressBarQuestao.setProgress(valor);
            }
        });


        //Dialog com as opçoes da camera
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        String[] item = new String[2];
        item[0] = getString(R.string.camera);
        item[1] = getString(R.string.galeria);

        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    //Abrir Camera
                    case 0:
                        intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA);
                        break;
                    //Abrir Galeria
                    case 1:
                        intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALERIA);
                        break;
                    default:
                        break;
                }
            }
        });

        dialogOpcoes = builder.create();

        //Listener para o dialog da camera
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcoes.show();
            }
        });

        imageComment = (ImageView) findViewById(R.id.comment);
        imageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewComment = getLayoutInflater().inflate(R.layout.frame_todo_comment, null);
                AlertDialog.Builder builderComment = new AlertDialog.Builder(QuestionsActivity.this);
                final EditText editComentario = (EditText) viewComment.findViewById(R.id.editComment);

                builderComment.setView(viewComment);
                dialogComment = builderComment.create();
                dialogComment.show();

                btCancelarComentario = (Button) viewComment.findViewById(R.id.btCancelar);
                btCancelarComentario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogComment.dismiss();
                    }
                });

                btEnviarComment = (Button) viewComment.findViewById(R.id.btEnviar);
                btEnviarComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ComplementoResposta cr = new ComplementoResposta();
                        cr.setIdFormItem(IDFORMITEM);
                        cr.setIdPergunta(adapter.getItem(POSITION).getIdPergunta());
                        cr.setComentario(editComentario.getText().toString());
                        complementoRespostaDao.incluir(cr);

                        if (getIntent().hasExtra("QueryActivity")) {
                            if (complementoRespostaDao.contadorComentario(getIntent().getExtras().getInt("ItemId")) > 0) {
                                getTvNumeroNotificaComment.setVisibility(View.VISIBLE);
                                getTvNumeroNotificaComment.setText(complementoRespostaDao.contadorComentario(getIntent().getExtras().getInt("ItemId")) + "");
                            }
                        } else {
                            if (complementoRespostaDao.contadorComentario(daoItemForm.buscarMaxId()) > 0) {
                                getTvNumeroNotificaComment.setVisibility(View.VISIBLE);
                                getTvNumeroNotificaComment.setText(complementoRespostaDao.contadorComentario(daoItemForm.buscarMaxId()) + "");
                            }
                        }
                        dialogComment.dismiss();
                    }
                });
            }
        });

        AlertDialog.Builder builderHelp = new AlertDialog.Builder(this);
        builderHelp.setTitle("Fale Conosco");
        String[] itemHelp = new String[1];
        itemHelp[0] = "support@pdasolucoes.com.br";
        builderHelp.setItems(itemHelp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Toast.makeText(getApplicationContext(), "Enviar email", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialogHelp = builderHelp.create();

        imageHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHelp.show();
            }
        });

        //Notificação
        imageNotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Enviar Notificação", Toast.LENGTH_SHORT).show();
                tvNumeroNotificaNotify.setVisibility(View.VISIBLE);
                tvNumeroNotificaNotify.setText((cntNotifica++) + "");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (complementoRespostaDao.contadorImagens(getIntent().getExtras().getInt("ItemId")) > 0) {
            tvNumeroNotificaCamera.setVisibility(View.VISIBLE);
            tvNumeroNotificaCamera.setText((complementoRespostaDao.contadorImagens(getIntent().getExtras().getInt("ItemId")) + ""));
        }

        if (complementoRespostaDao.contadorComentario(getIntent().getExtras().getInt("ItemId")) > 0) {
            getTvNumeroNotificaComment.setVisibility(View.VISIBLE);
            getTvNumeroNotificaComment.setText(complementoRespostaDao.contadorComentario(getIntent().getExtras().getInt("ItemId")) + "");
        }

        if (daoTodo.contadorTodo(getIntent().getExtras().getInt("ItemId")) > 0) {
            tvNotificaTodo.setVisibility(View.VISIBLE);
            tvNotificaTodo.setText(daoTodo.contadorTodo(getIntent().getExtras().getInt("ItemId")) + "");
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
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

                case CAMERA:
                    photo = (Bitmap) data.getExtras().get("data");
                    originalBitmap = photo;
                    resizedBitmap = Bitmap.createScaledBitmap(
                            originalBitmap, photo.getWidth() * 3, photo.getHeight() * 3, false);


                    imageByte = getBitmapAsByteArray(originalBitmap);
                    image.setImageBitmap(resizedBitmap);

                    dialog = builder.create();
                    dialog.show();

                    break;
                //Trazer imagem da galeria e colocar no dialog

                case 2:
                    photo = (Bitmap) data.getExtras().get("data");
                    originalBitmap = photo;
                    resizedBitmap = Bitmap.createScaledBitmap(
                            originalBitmap, photo.getWidth() * 3, photo.getHeight() * 3, false);

                    imageByte = getBitmapAsByteArray(originalBitmap);

                    image.setImageBitmap(resizedBitmap);

                    dialog = builder.create();
                    dialog.show();
                    break;
                case GALERIA:
                    String[] colunaCaminhoArquivo = {MediaStore.Images.Media.DATA};
                    uriImagem = data.getData();
                    Cursor cursor = getContentResolver().query(
                            uriImagem, colunaCaminhoArquivo, null, null, null);
                    cursor.moveToFirst();
                    int colunaIndex = cursor
                            .getColumnIndex(colunaCaminhoArquivo[0]);
                    String caminhoImagem = cursor.getString(colunaIndex);
                    cursor.close();
                    uriImagem = Uri.fromFile(new File(caminhoImagem));
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImagem);
                        originalBitmap = photo;
                        resizedBitmap = Bitmap.createScaledBitmap(
                                originalBitmap, photo.getWidth() * 3, photo.getHeight() * 3, false);
                        image.setImageBitmap(resizedBitmap);

                        imageByte = getBitmapAsByteArray(originalBitmap);
                        complementoRespostaDao.incluir(cr);

                        dialog = builder.create();
                        dialog.show();
                    } catch (Exception e) {
                    }
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
                                originalBitmap, photo.getWidth() * 3, photo.getHeight() * 3, false);
                        image.setImageBitmap(resizedBitmap);
                        complementoTodoDao.incluir(ct);

                        dialog = builder.create();
                        dialog.show();
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
                Toast.makeText(QuestionsActivity.this, "Salvou", Toast.LENGTH_SHORT).show();

                if (requestCode == 2 || requestCode == 3) {
                    ct.setImage(imageByte);
                    ct.setIdTodo(daoTodo.ultimoId() + 1);

                    complementoTodoDao.incluir(ct);

                    CriaTodo.atualizaNotificacoesTempoRealCamera(QuestionsActivity.this, IDFORMITEM, IDPERGUNTA);
                } else {
                    cr.setImage(imageByte);
                    cr.setIdPergunta(adapter.getItem(POSITION).getIdPergunta());
                    cr.setIdFormItem(IDFORMITEM);

                    complementoRespostaDao.incluir(cr);

                }


                if (getIntent().hasExtra("QueryActivity")) {
                    if (complementoRespostaDao.contadorImagens(getIntent().getExtras().getInt("ItemId")) > 0) {
                        tvNumeroNotificaCamera.setVisibility(View.VISIBLE);
                        tvNumeroNotificaCamera.setText(((complementoRespostaDao.contadorImagens(getIntent().getExtras().getInt("ItemId"))) + ""));
                    }

                } else {
                    if (complementoRespostaDao.contadorImagens(daoItemForm.buscarMaxId()) > 0) {
                        tvNumeroNotificaCamera.setVisibility(View.VISIBLE);
                        tvNumeroNotificaCamera.setText(((complementoRespostaDao.contadorImagens(daoItemForm.buscarMaxId())) + ""));
                    }
                }

                dialog.dismiss();

            }
        });

    }


    public class AsyncPergunta extends AsyncTask<Objects, Integer, Integer> {

        SharedPreferences preferences = getSharedPreferences(MainActivity.PREF_NAME, MODE_PRIVATE);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!getIntent().hasExtra("QueryActivity")) {
                if (VerificaConexao.isNetworkConnected(QuestionsActivity.this)) {
                    dialogProgress.show();
                }
            }

        }

        @Override
        protected Integer doInBackground(Objects... objectses) {
//            if (!getIntent().hasExtra("QueryActivity")) {
//                if (VerificaConexao.isNetworkConnected(QuestionsActivity.this)) {
//                }
//            }

            return 1;
        }


        @Override
        protected void onPostExecute(Integer progres) {
            super.onPostExecute(progres);

            mRecyclerView = (CustomRecyclerView) findViewById(R.id.rv_list);

            if (dao.listar(getIntent().getExtras().getInt("idSetor")).size() < 1) {
                mRecyclerView.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
            } else {

                CustomLinearLayoutManager llm = new CustomLinearLayoutManager(QuestionsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                mRecyclerView.setLayoutManager(llm);

                if (getIntent().hasExtra("StartActivity")) {
                    Form f = new Form();
                    f.setIdForm(getIntent().getExtras().getInt("idForm"));
                    f.setNomeFom(getIntent().getExtras().getString("nomeForm"));
                    formItem.setIdForm(f);
                    Setor s = new Setor();
                    s.setId(getIntent().getExtras().getInt("idSetor"));
                    formItem.setIdSetor(s);
                    String dataInicio = getIntent().getExtras().getString("dataHora").substring(0, 10);
                    String horaInicio = getIntent().getExtras().getString("dataHora").substring(11, 16);
                    formItem.setDataInicio(dataInicio);
                    formItem.setHoraInicio(horaInicio);
                    formItem.setIdUsuario(preferences.getInt("idUsuario", 0));
                    daoItemForm.incluir(formItem);

                    adapter = new Question2Adapter(QuestionsActivity.this, dao.listar(getIntent().getExtras().getInt("idSetor")), daoItemForm.buscarMaxId());
                    mRecyclerView.setAdapter(adapter);

                } else {
                    adapter = new Question2Adapter(QuestionsActivity.this, dao.listar(getIntent().getExtras().getInt("idSetor")),
                            getIntent().getExtras().getInt("ItemId"));
                    mRecyclerView.setAdapter(adapter);
                }
            }

            if (getIntent().hasExtra("pendencias")) {
                adapter = new Question2Adapter(QuestionsActivity.this, dao.listarNaoRespondidas(getIntent().getExtras().getInt("idSetor"), daoItemForm.buscarMaxId()),
                        daoItemForm.buscarMaxId());
                mRecyclerView.setAdapter(adapter);
            }

            textMax.setText(adapter.getItemCount() + "");
            progressBarQuestao.setMax(adapter.getItemCount());

            ComponenteResposta cr = new ComponenteResposta();
            cr.PegaTodoListener(new ComponenteResposta.PegaTodo() {
                @Override
                public void Pega(boolean todo) {
                    if (todo == true) {
                        imageTodo.setImageResource(R.drawable.todo_48);
                        imageTodo.setEnabled(true);
                        imageTodo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CriaTodo.criaTodo(QuestionsActivity.this, IDPERGUNTA, IDFORMITEM);
                            }
                        });
                    } else if (todo != true) {
                        imageTodo.setImageResource(R.drawable.todo_48_gray);
                        imageTodo.setEnabled(false);
                    }
                }
            });

            //pegando position e idFormItem dos formularios das questões(adapter)
            adapter.PegaPositionListener(new Question2Adapter.PegaPosition() {
                @Override
                public void pegaPosition(int position, int idFormItem, int idPergunta) {
                    POSITION = position;
                    IDFORMITEM = idFormItem;
                    IDPERGUNTA = idPergunta;
                }
            });

            if (progres == 1)

            {
                dialogProgress.dismiss();
            }

        }

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
