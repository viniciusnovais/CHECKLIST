package br.com.pdasolucoes.checklist.activities;

import android.content.DialogInterface;
import android.content.Intent;
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

import android.view.View;
import android.widget.Button;

import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import br.com.pdasolucoes.checklist.util.ComponenteResposta;
import br.com.pdasolucoes.checklist.util.CriaTodo;
import br.com.pdasolucoes.checklist.util.CustomLinearLayoutManager;
import br.com.pdasolucoes.checklist.util.CustomRecyclerView;
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
    private AlertDialog dialog, dialogOpcoes, dialogTodo, dialogComment, dialogHelp;
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

    private AlertDialog dialogProgress;
    private Handler handler = new Handler();
    private int progressStatus = 0;
    private static int progress;
    private ProgressBar progressBar, progressBarQuestao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        tvNomeFormAction = (TextView) findViewById(R.id.tvNomeFormAction);
        nomeFormAction = getIntent().getExtras().getString("form");
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

        //Dialog TODO
        View viewTodo = getLayoutInflater().inflate(R.layout.frame_todo, null);
        AlertDialog.Builder builderTodo = new AlertDialog.Builder(QuestionsActivity.this);
        builderTodo.setView(viewTodo);
        dialogTodo = builderTodo.create();
        btEnviarTodo = (Button) viewTodo.findViewById(R.id.btnGravar);

        btEnviarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(QuestionsActivity.this, "Sucesso", Toast.LENGTH_SHORT).show();
            }
        });

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final View view = getLayoutInflater().inflate(R.layout.frame_image_todo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        image = (ImageView) view.findViewById(R.id.image);
        btEnviarFoto = (Button) view.findViewById(R.id.btEnviarFoto);
        btCancelarImagem = (Button) view.findViewById(R.id.btCancelar);
        byte[] imageByte;
        ComplementoResposta cr = new ComplementoResposta();
        ComplementoTodo ct = new ComplementoTodo();
        if (resultCode == MainActivity.RESULT_OK) {
            //Colocar imagem da Camera no Dialog
            switch (requestCode) {

                case CAMERA:
                    photo = (Bitmap) data.getExtras().get("data");
                    originalBitmap = photo;
                    resizedBitmap = Bitmap.createScaledBitmap(
                            originalBitmap, 530, 530, false);


                    imageByte = getBitmapAsByteArray(originalBitmap);
                    cr.setImage(imageByte);
                    cr.setIdPergunta(adapter.getItem(POSITION).getIdPergunta());
                    cr.setIdFormItem(IDFORMITEM);

                    complementoRespostaDao.incluir(cr);

                    image.setImageBitmap(resizedBitmap);

                    dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setLayout(700, 800);
                    break;
                //Trazer imagem da galeria e colocar no dialog

                case 2:
                    photo = (Bitmap) data.getExtras().get("data");
                    originalBitmap = photo;
                    resizedBitmap = Bitmap.createScaledBitmap(
                            originalBitmap, 530, 530, false);


                    imageByte = getBitmapAsByteArray(originalBitmap);
                    ct.setImage(imageByte);
                    ct.setIdTodo(daoTodo.buscarMaior());

                    complementoTodoDao.incluir(ct);

                    image.setImageBitmap(resizedBitmap);

                    dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setLayout(700, 800);
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
                                originalBitmap, 530, 530, false);
                        image.setImageBitmap(resizedBitmap);

                        imageByte = getBitmapAsByteArray(originalBitmap);
                        cr.setImage(imageByte);
                        cr.setIdPergunta(adapter.getItem(POSITION).getIdPergunta());
                        cr.setIdFormItem(IDFORMITEM);

                        complementoRespostaDao.incluir(cr);

                        dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setLayout(700, 800);
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
                                originalBitmap, 530, 530, false);
                        image.setImageBitmap(resizedBitmap);

                        imageByte = getBitmapAsByteArray(originalBitmap);
                        ct.setImage(imageByte);
                        ct.setIdTodo(daoTodo.buscarMaior());

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
                Toast.makeText(QuestionsActivity.this, "Salvou", Toast.LENGTH_SHORT).show();


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

    public class AsyncPergunta extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            if (VerificaConexao.isNetworkConnected(QuestionsActivity.this)) {
                listaPergunta = new ArrayList<>();
                listaPergunta = dao.getPerguntaWS();
                dao.incluir(listaPergunta);
//            }else{
//                listaPergunta=new ArrayList<>();
//                listaPergunta=dao.listar(getIntent().getExtras().getInt("id"));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            mRecyclerView = (CustomRecyclerView) findViewById(R.id.rv_list);

            CustomLinearLayoutManager llm = new CustomLinearLayoutManager(QuestionsActivity.this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(llm);

            if (getIntent().hasExtra("StartActivity") == true) {
                Form f = new Form();
                f.setIdForm(getIntent().getExtras().getInt("id"));
                f.setNomeFom(getIntent().getExtras().getString("nomeForm"));
                formItem.setIdForm(f);
                daoItemForm.incluir(formItem);

                adapter = new Question2Adapter(QuestionsActivity.this, dao.listar(getIntent().getExtras().getInt("id")), daoItemForm.buscarMaxId());
                mRecyclerView.setAdapter(adapter);

            } else {
                adapter = new Question2Adapter(QuestionsActivity.this, dao.listar(getIntent().getExtras().getInt("idForm")),
                        getIntent().getExtras().getInt("ItemId"));
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

            if (dialogProgress.isShowing()) {
                dialogProgress.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            new Thread(new Runnable() {
                public void run() {
                    progressStatus = doSomeWork();
                    handler.post(new Runnable() {
                        public void run() {
                            dialogProgress.show();
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }

                private int doSomeWork() {
                    try {
                        // ---simulate doing some work---
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return ++progress;
                }
            }).start();
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
