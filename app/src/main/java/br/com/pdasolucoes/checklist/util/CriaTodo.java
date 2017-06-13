package br.com.pdasolucoes.checklist.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.pdasolucoes.checklist.dao.ComplementoTodoDao;
import br.com.pdasolucoes.checklist.dao.TodoDao;
import br.com.pdasolucoes.checklist.model.ComplementoTodo;
import br.com.pdasolucoes.checklist.model.Todo;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 09/02/2017.
 */

public class CriaTodo {
    public static EditText edJustificativa, edAcao, edResponsavel, edPrazo, edData, edFollow;
    public static Spinner spinnerStatus;
    public static AlertDialog dialogComentario, dialog;
    public static ImageButton btComment, btCamera, btHelp, btBell, btTodo;
    private static int dia, mes, ano;

    public static void criaTodo(final Context context, final int idPergunta, final int idFormItem) {
        final AlertDialog dialogOpcoes;

        final Todo t;
        final ComplementoTodo ct = new ComplementoTodo();
        final TodoDao dao;
        final ComplementoTodoDao daoComplemento;
        final Button bt, btCancelar, btLimpar, btEnviarFoto;
        final TextView getTvNumeroNotificaComment, tvNumeroNotificaCamera, tvNotificaTodo;

        View view = View.inflate(context, R.layout.frame_todo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        builder.setView(view);
        dialog = builder.create();
        dialog = builder.show();

        edJustificativa = (EditText) view.findViewById(R.id.editJustificativa);
        edAcao = (EditText) view.findViewById(R.id.editAcao);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        edResponsavel = (EditText) view.findViewById(R.id.editResponsavel);
        edPrazo = (EditText) view.findViewById(R.id.editPrazo);
        edData = (EditText) view.findViewById(R.id.editData);
        edFollow = (EditText) view.findViewById(R.id.editFollow);
        btLimpar = (Button) view.findViewById(R.id.btLimpar);
        bt = (Button) view.findViewById(R.id.btnGravar);
        btCancelar = (Button) view.findViewById(R.id.btCancelar);
        btComment = (ImageButton) view.findViewById(R.id.btnComment);
        btCamera = (ImageButton) view.findViewById(R.id.btnCamera);
        btHelp = (ImageButton) view.findViewById(R.id.help);
        btBell = (ImageButton) view.findViewById(R.id.btnBell);
        btTodo = (ImageButton) view.findViewById(R.id.imagetoDo);
        getTvNumeroNotificaComment = (TextView) view.findViewById(R.id.numeroNotificaComment);
        tvNumeroNotificaCamera = (TextView) view.findViewById(R.id.numeroNotificaCamera);
        tvNotificaTodo = (TextView) view.findViewById(R.id.numeroNotificaTodo);


        //Bloqueando alguns botões que serão habilitados depois de salvar o todo
        bloquearBotoes();

        edPrazo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                edData.setInputType(InputType.TYPE_NULL);
                edData.setBackgroundColor(Color.GRAY);
                edData.setText("");
                edPrazo.setInputType(InputType.TYPE_CLASS_NUMBER);
                edPrazo.setBackgroundColor(Color.WHITE);

                return false;
            }
        });

        Calendar dataAtual = Calendar.getInstance();
        ano = dataAtual.get(Calendar.YEAR);
        mes = dataAtual.get(Calendar.MONTH);
        dia = dataAtual.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dia = dayOfMonth;
                ano = year;
                mes = monthOfYear;
                edData.setText(String.format("%02d/%02d/%04d", dia, mes + 1, ano));

            }
        };
        edData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datPicker = new DatePickerDialog(
                        context, date, ano, mes, dia);

                edPrazo.setInputType(InputType.TYPE_NULL);
                edPrazo.setBackgroundColor(Color.GRAY);
                edPrazo.setText("");
                edData.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                edData.setBackgroundColor(Color.WHITE);


                datPicker.show();


            }
        });


        //máscara na data
        MaskEditTextChangedListener maskEditData = new MaskEditTextChangedListener("##/##/####", edData);
        edData.addTextChangedListener(maskEditData);

        dao = new TodoDao(context);
        daoComplemento = new ComplementoTodoDao(context);

        if (daoComplemento.contadorComentario(idFormItem, idPergunta) > 0) {
            getTvNumeroNotificaComment.setVisibility(View.VISIBLE);
            getTvNumeroNotificaComment.setText(daoComplemento.contadorComentario(idFormItem, idPergunta) + "");
        }

        if (dao.contadorTodoPergunta(idFormItem, idPergunta) > 0) {
            tvNotificaTodo.setVisibility(View.VISIBLE);
            tvNotificaTodo.setText(dao.contadorTodoPergunta(idFormItem, idPergunta) + "");
        }

        if (daoComplemento.contadorCamera(idFormItem, idPergunta) > 0) {
            tvNumeroNotificaCamera.setVisibility(View.VISIBLE);
            tvNumeroNotificaCamera.setText(daoComplemento.contadorCamera(idFormItem, idPergunta)+"");
        }

        //botão comentário
        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(context, R.layout.frame_todo_comment, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);

                Button btEnviar, btCancelar;
                final EditText editComment;
                btEnviar = (Button) view.findViewById(R.id.btEnviar);
                btCancelar = (Button) view.findViewById(R.id.btCancelar);
                editComment = (EditText) view.findViewById(R.id.editComment);

                dialogComentario = builder.create();
                dialogComentario.show();

                btEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ct.setComentario(editComment.getText().toString());
                        ct.setIdTodo(dao.buscarMaior());
                        daoComplemento.incluir(ct);

                        Toast.makeText(context, "Comentário gravado com sucesso", Toast.LENGTH_SHORT).show();

                        editComment.setText("");

                        if (daoComplemento.contadorComentario(idFormItem, idPergunta) > 0) {
                            getTvNumeroNotificaComment.setVisibility(View.VISIBLE);
                            getTvNumeroNotificaComment.setText(daoComplemento.contadorComentario(idFormItem, idPergunta) + "");
                        }

                    }
                });

                btCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogComentario.dismiss();
                    }
                });
            }
        });

        //botão help
        AlertDialog.Builder builderHelp = new AlertDialog.Builder(context);
        builderHelp.setTitle("Fale Conosco");
        String[] itemHelp = new String[1];
        itemHelp[0] = "support@pdasolucoes.com.br";
        builderHelp.setItems(itemHelp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Toast.makeText(context, "Enviar email", Toast.LENGTH_LONG).show();
                }
            }
        });

        final AlertDialog dialogHelp = builderHelp.create();
        btHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHelp.show();
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edAcao.getText().toString().equals("") || edFollow.getText().toString().equals("")
                        || edResponsavel.getText().toString().equals("")) {
                    Toast.makeText(context, "Preencha os campos", Toast.LENGTH_SHORT).show();
                } else {
                    final Todo t = new Todo();
                    t.setJustificativa(edJustificativa.getText().toString());
                    t.setAcao(edAcao.getText().toString());
                    spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            t.setStatus(pos);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    t.setFollowup(edFollow.getText().toString());
                    t.setResponsavel(edResponsavel.getText().toString());
                    try {
                        t.setPrazo(Integer.parseInt(edPrazo.getText().toString()));
                        t.setDataLimite(edData.getText().toString());
                    }catch (NumberFormatException n){
                        n.printStackTrace();
                    }
                    t.setIdPergunta(idPergunta);
                    t.setIdFormItem(idFormItem);

                    dao.incluir(t);

                    Toast.makeText(context, "Salvo com sucesso", Toast.LENGTH_SHORT).show();

                    desbloquearBotoes();

                }
            }
        });


        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Limpando", Toast.LENGTH_SHORT).show();
                limparCampos();
                bloquearBotoes();
            }
        });

        //Dialog com as opçoes
        AlertDialog.Builder builderCamera = new AlertDialog.Builder(context);
        builderCamera.setTitle(R.string.opcoes);
        String[] item = new String[2];
        item[0] = "Câmera";
        item[1] = "Galeria";

        builderCamera.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;

                switch (which) {
                    //Abrir Camera
                    case 0:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        ((Activity) context).startActivityForResult(intent, 2);
                        break;
                    //Abrir Galeria
                    case 1:
                        intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        ((Activity) context).startActivityForResult(intent, 3);
                        break;
                    default:
                        break;
                }
            }
        });
        dialogOpcoes = builderCamera.create();

        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcoes.show();
            }
        });

    }

    public static void limparCampos() {
        //Limpando Campos
        edJustificativa.setText("");
        edAcao.setText("");
        edData.setText("");
        edPrazo.setText("");
        edResponsavel.setText("");
        edFollow.setText("");
    }

    public static void desbloquearBotoes() {
        btCamera.setEnabled(true);
        btCamera.setImageResource(R.drawable.camera);
        btComment.setEnabled(true);
        btComment.setImageResource(R.drawable.comment);
        btBell.setEnabled(true);
        btBell.setImageResource(R.drawable.bell);
        btTodo.setEnabled(true);
        btTodo.setImageResource(R.drawable.todo_48);
    }

    public static void bloquearBotoes() {

        btCamera.setEnabled(false);
        btCamera.setImageResource(R.drawable.camera_gray);
        btComment.setEnabled(false);
        btComment.setImageResource(R.drawable.comment_gray);
        btBell.setEnabled(false);
        btBell.setImageResource(R.drawable.bell_gray);
        btTodo.setEnabled(false);
        btTodo.setImageResource(R.drawable.todo_48_gray);
    }


}
