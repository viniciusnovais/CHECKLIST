package br.com.pdasolucoes.checklist.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.formatter.IFillFormatter;

import java.math.BigDecimal;
import java.util.List;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.pdasolucoes.checklist.activities.HomeActivity;
import br.com.pdasolucoes.checklist.activities.MainActivity;
import br.com.pdasolucoes.checklist.activities.QueryActivity;
import br.com.pdasolucoes.checklist.activities.QuestionsActivity;
import br.com.pdasolucoes.checklist.adapter.ListQuestionAdapter;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.OpcaoRespostaDao;
import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.OpcaoResposta;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Resposta;
import checklist.pdasolucoes.com.br.checklist.R;


/**
 * Created by PDA on 09/02/2017.
 */

public class ComponenteResposta {

    public static int idPergunta = 0;
    public static int idResposta = 0;
    public static int concluido = 0;
    public static int pos = 0;
    public static int posCheck = 0;
    public static PegaTodo pegaTodo;
    public static PegaQtdeResposta pegaQtdeResposta;
    public static Context context;
    public static PerguntaDao perguntaDao;
    public static FormItemDao formItemDao;
    public static int statusForm = 0;


    public interface PegaTodo {
        void Pega(boolean todo);
    }

    public interface PegaQtdeResposta {
        void Pega(int valor);
    }

    public void PegaQtdeRespostaListener(PegaQtdeResposta pegaQtdeResposta) {
        this.pegaQtdeResposta = pegaQtdeResposta;
    }

    public void PegaTodoListener(PegaTodo pegaTodo) {
        this.pegaTodo = pegaTodo;
    }

    public static LinearLayout createCheck(final Context context, final String question, final CheckBox[] check, final String qestaoTodo, final List<OpcaoResposta> opcao, final int idFormItem, int getItemCount, int cntqestoes, final int idPergunta) {

        final RespostaDao daoResposta = new RespostaDao(context);
        final Resposta resposta = new Resposta();
        final OpcaoRespostaDao daoOpcaoResposta = new OpcaoRespostaDao(context);


        int cntcheck = 0, cnttxtcheck = 0;
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams lpViewRadio =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setText(question);
        tv.setTextSize(20);
        tv.setLayoutParams(lpView);
        linearLayout.addView(tv);


        String[] vetor = new String[daoOpcaoResposta.listarVolta(idFormItem, idPergunta).size()];

        //Carrega o vetor com os dados que foram salvos no banco
        for (int i = 0; i < daoOpcaoResposta.listarVolta(idFormItem, idPergunta).size(); i++) {
            vetor[i] = daoOpcaoResposta.listarVolta(idFormItem, idPergunta).get(i).getTxtResposta();
        }


        while (cntcheck < check.length) {
            check[cntcheck] = new CheckBox(context);
            check[cntcheck].setText(opcao.get(cnttxtcheck).getOpcao());
            check[cntcheck].setTextSize(20);

            //verficando quais as respostas e seta nas checkboxes
            for (int i = 0; i < vetor.length; i++) {
                if (opcao.get(cntcheck).getOpcao().equals(vetor[i])) {
                    check[cntcheck].setChecked(true);

                    if (opcao.get(cntcheck).getOpcao().equals(qestaoTodo)) {
                        pegaTodo.Pega(true);
                    }
                }
            }
            check[cntcheck].setLayoutParams(lpViewRadio);
            check[cntcheck].setTextColor(Color.BLACK);

            final int finalCnttxtcheck1 = cnttxtcheck;
            check[cntcheck].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    posCheck = finalCnttxtcheck1;
                    if (isChecked) {
                        //pega a resposta selecionada pelo usuario
                        resposta.setIdOpcao(opcao.get(finalCnttxtcheck1).getIdOpcao());
                        resposta.setTxtResposta(buttonView.getText().toString());
                        resposta.setIdPergunta(idPergunta);
                        resposta.setIdFormItem(idFormItem);
                        resposta.setValor(opcao.get(posCheck).getValor());
                        resposta.setTodo(opcao.get(posCheck).getToDo());

                        daoResposta.incluirResposta(resposta);
                        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);

                        if (qestaoTodo.equals(buttonView.getText().toString())) {
                            CriaTodo.criaTodo(context, idPergunta, idFormItem, idResposta, 1);
                            pegaTodo.Pega(true);

                            daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta, opcao.get(finalCnttxtcheck1).getIdOpcao()));
                        }

                    } else {

                        if (qestaoTodo.equals(buttonView.getText().toString())) {
                            pegaTodo.Pega(false);
                        }
                        //deletar opção selecionada
                        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta, opcao.get(finalCnttxtcheck1).getIdOpcao());
                        daoResposta.deleteOpcaoResposta(idResposta);
                    }

                    pegaQtdeResposta.Pega(daoResposta.contadorResposta(idFormItem));

                }
            });

            linearLayout.addView(check[cntcheck]);

            cntcheck++;
            cnttxtcheck++;
        }

        //Teste para saber se é a última questão, e botão para concluir form
        if (getItemCount == cntqestoes + 1) {
            Button bt = new Button(context);
            bt.setText("Concluir");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Salvar Resposta do formulário
                    perguntaDao = new PerguntaDao(context);
                    formItemDao = new FormItemDao(context);

                    if (daoResposta.contadorResposta(idFormItem) == perguntaDao.QtdePergunta(formItemDao.buscarIdSetor(idFormItem))) {
                        statusForm = 1;
                        formItemDao.alterarStatus(idFormItem);
                    }
                    FinalizacaoFormulario.DialogFinalizacao(context, statusForm, idFormItem);
                }
            });
            linearLayout.addView(bt);
        }

        return linearLayout;

    }


    public static LinearLayout createRadioButton(final Context context, String question, final RadioButton[] radio, final String qestaotodo, final List<OpcaoResposta> opcao, final int idFormItem, final int idPergunta, int getItemCount, int cntqestoes) {

        final RespostaDao daoResposta = new RespostaDao(context);
        final Resposta resposta = new Resposta();
        final OpcaoRespostaDao opcaoRespostaDao = new OpcaoRespostaDao(context);
        formItemDao = new FormItemDao(context);

        int cntradio = 0, cnttxt = 0;

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams lpViewRadio =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
        tv.setText(question);
        tv.setLayoutParams(lpView);
        linearLayout.addView(tv);


        final RadioGroup radioGroup = new RadioGroup(context);
        while (cntradio < radio.length) {

            radio[cntradio] = new RadioButton(context);
            radio[cntradio].setText(opcao.get(cnttxt).getOpcao());
            radio[cntradio].setLayoutParams(lpViewRadio);
            radio[cntradio].setTextColor(Color.BLACK);
            radio[cntradio].setTextSize(20);

            radioGroup.setLayoutParams(lpViewRadio);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    pos = radioGroup.indexOfChild(group.findViewById(checkedId));
                    radio[pos].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            resposta.setIdOpcao(opcao.get(pos).getIdOpcao());
                            resposta.setTxtResposta(radio[pos].getText().toString());
                            resposta.setValor(opcao.get(pos).getValor());
                            resposta.setTodo(opcao.get(pos).getToDo());
                            resposta.setIdPergunta(idPergunta);
                            resposta.setIdFormItem(idFormItem);

                            idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                            if (idResposta <= 0) {
                                daoResposta.incluirResposta(resposta);
                                idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                            } else {
                                idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);

                                resposta.setIdResposta(idResposta);
                                daoResposta.alterarResposta(resposta);
                            }

                            if (opcao.get(pos).getToDo() == 1) {
                                CriaTodo.criaTodo(context, idPergunta, idFormItem, idResposta, 1);
                                pegaTodo.Pega(true);
                                daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                            } else {
                                pegaTodo.Pega(false);
                            }

                            pegaQtdeResposta.Pega(daoResposta.contadorResposta(idFormItem));
                        }
                    });
                }
            });


            radioGroup.addView(radio[cntradio]);
            cntradio = cntradio + 1;
            cnttxt = cnttxt + 1;
        }

        linearLayout.addView(radioGroup);

        //Pegando a posição da questão
        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
        for (int i = 0; i < radio.length; i++) {
            for (Resposta r : daoResposta.Resposta(idResposta)) {
                if (radio[i].getText().toString().trim().equals(r.getTxtResposta())) {
                    radio[i].setChecked(true);

                    //Verifica se é todo e libera o botão
                    if (opcaoRespostaDao.buscarTodo(r.getIdOpcao()) == 1) {
                        pegaTodo.Pega(true);
                    }
                }
            }


        }


//        Teste para saber se é a última questão, e botão para concluir form
        if (getItemCount == cntqestoes + 1) {
            Button bt = new Button(context);
            bt.setText("Concluir");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perguntaDao = new PerguntaDao(context);
                    formItemDao = new FormItemDao(context);

                    if (daoResposta.contadorResposta(idFormItem) == perguntaDao.QtdePergunta(formItemDao.buscarIdSetor(idFormItem))) {
                        statusForm = 1;
                        formItemDao.alterarStatus(idFormItem);
                    }
                    FinalizacaoFormulario.DialogFinalizacao(context, statusForm, idFormItem);
                }
            });
            linearLayout.addView(bt);
        }

        return linearLayout;
    }

    public static LinearLayout createEditText(final Context context, String question, EditText editText, final String qestaoTodo, final List<OpcaoResposta> opcao, final int idFormItem, final int getItemCount, final int cntqestoes, final int idPergunta) {

        final RespostaDao daoResposta = new RespostaDao(context);
        final Resposta resposta = new Resposta();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams
                lpViewEdit = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
        tv.setText(question);
        tv.setLayoutParams(lpView);
        linearLayout.addView(tv);

        editText = new EditText(context);
        lpViewEdit.setMargins(10, 0, 15, 0);
        editText.setLayoutParams(lpViewEdit);


        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
        if (daoResposta.Resposta(idResposta).size() != 0) {
            editText.setText(daoResposta.Resposta(idResposta).get(0).getTxtResposta());
        }
        final EditText finalEditText = editText;

        if (finalEditText.getText().toString().equals(qestaoTodo)) {
            pegaTodo.Pega(true);
        }

        finalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (finalEditText.getText().toString().equals(qestaoTodo)) {
                    CriaTodo.criaTodo(context, idPergunta, idFormItem, idResposta, 3);
                    pegaTodo.Pega(true);
                    daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                } else {
                    pegaTodo.Pega(false);
                }

            }
        });

        //Incluindo texto
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (!hasFocus) {
                    Pergunta p = new Pergunta();
                    p.setIdPergunta(idPergunta);
                    resposta.setIdPergunta(idPergunta);
                    resposta.setIdFormItem(idFormItem);
                    resposta.setTxtResposta(finalEditText.getText().toString());

                    idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);

                    if (idResposta <= 0) {
                        daoResposta.incluirResposta(resposta);

                        if (finalEditText.getText().toString().equals(qestaoTodo)) {
                            daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                        }
                    } else {
                        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                        resposta.setIdResposta(idResposta);
                        resposta.setTxtResposta(finalEditText.getText().toString());
                        daoResposta.alterarResposta(resposta);
                    }
                    pegaQtdeResposta.Pega(daoResposta.contadorResposta(idFormItem));
                }

            }
        });

        linearLayout.addView(editText);

        if (getItemCount == cntqestoes + 1) {
            Button bt = new Button(context);
            bt.setText("Concluir");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Salvar Resposta do formulário

                    finalEditText.clearFocus();
                    perguntaDao = new PerguntaDao(context);
                    formItemDao = new FormItemDao(context);

                    if (daoResposta.contadorResposta(idFormItem) == perguntaDao.QtdePergunta(formItemDao.buscarIdSetor(idFormItem))) {
                        statusForm = 1;
                        formItemDao.alterarStatus(idFormItem);
                    }
                    FinalizacaoFormulario.DialogFinalizacao(context, statusForm, idFormItem);
                }
            });
            linearLayout.addView(bt);
        }

        return linearLayout;
    }

    public static LinearLayout createDate(final Context context, String question, EditText editData, final String qestaotodo, final List<OpcaoResposta> opcao, final int cntqestoes, final int getItemCount, final int idFormItem, final int idPergunta) {

        final RespostaDao daoResposta = new RespostaDao(context);
        final Resposta resposta = new Resposta();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout llText = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams lpViewEdit =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
        tv.setText(question);
        tv.setLayoutParams(lpView);
        linearLayout.addView(tv);


        //Dia
        editData = new EditText(context);
        editData.setEnabled(true);
        editData.setInputType(InputType.TYPE_CLASS_NUMBER);
        editData.setHint("dd/mm/aaaa");


        MaskEditTextChangedListener maskData = new MaskEditTextChangedListener("##/##/####", editData);
        editData.addTextChangedListener(maskData);

        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
        if (daoResposta.Resposta(idResposta).size() != 0) {
            editData.setText(daoResposta.Resposta(idResposta).get(0).getTxtResposta());
        }

        final EditText finalEditDia = editData;

        String data = finalEditDia.getText().toString();
        data = data.replaceAll("/", "");
        if (data.toString().equals(qestaotodo)) {
            pegaTodo.Pega(true);
        }

        editData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String data = finalEditDia.getText().toString();
                data = data.replaceAll("/", "");
                if (data.toString().equals(qestaotodo)) {
                    CriaTodo.criaTodo(context, idPergunta, idFormItem, idResposta, 4);
                    pegaTodo.Pega(true);
                    daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                } else {
                    pegaTodo.Pega(false);
                }
            }
        });

        editData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    resposta.setIdFormItem(idFormItem);
                    Pergunta p = new Pergunta();
                    p.setIdPergunta(idPergunta);
                    resposta.setIdPergunta(idPergunta);
                    resposta.setTxtResposta(finalEditDia.getText().toString());

                    idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                    if (idResposta <= 0) {
                        daoResposta.incluirResposta(resposta);

                        String data = finalEditDia.getText().toString();
                        data = data.replaceAll("/", "");
                        if (data.toString().equals(qestaotodo)) {
                            daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                        }

                    } else {
                        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                        resposta.setIdResposta(idResposta);
                        resposta.setTxtResposta(finalEditDia.getText().toString());
                        daoResposta.alterarResposta(resposta);
                    }
                    pegaQtdeResposta.Pega(daoResposta.contadorResposta(idFormItem));
                }

            }
        });

        editData.setLayoutParams(lpViewEdit);

        ll.addView(editData);
        linearLayout.addView(ll);

        if (getItemCount == cntqestoes + 1) {
            Button bt = new Button(context);
            bt.setText("Concluir");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Salvar Resposta do formulário
                    finalEditDia.clearFocus();
                    perguntaDao = new PerguntaDao(context);
                    formItemDao = new FormItemDao(context);

                    if (daoResposta.contadorResposta(idFormItem) == perguntaDao.QtdePergunta(formItemDao.buscarIdSetor(idFormItem))) {
                        statusForm = 1;
                        formItemDao.alterarStatus(idFormItem);
                    }
                    FinalizacaoFormulario.DialogFinalizacao(context, statusForm, idFormItem);
                }
            });
            linearLayout.addView(bt);
        }
        return linearLayout;


    }

    public static LinearLayout createHour(final Context context, String question, EditText editHora, final String qestaotodo, final List<OpcaoResposta> opcao, final int idFormItem, final int getItemCount, final int cntquestoes, final int idPergunta) {

        final RespostaDao daoResposta = new RespostaDao(context);
        final Resposta resposta = new Resposta();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);


        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams lpViewEdit =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
        tv.setText(question);
        tv.setLayoutParams(lpView);
        linearLayout.addView(tv);

        editHora = new EditText(context);
        editHora.setEnabled(true);
        editHora.setHint("hh:mm");
        editHora.setInputType(InputType.TYPE_CLASS_NUMBER);

        MaskEditTextChangedListener maskHora = new MaskEditTextChangedListener("##:##", editHora);
        editHora.addTextChangedListener(maskHora);

        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
        if (daoResposta.Resposta(idResposta).size() != 0) {
            editHora.setText(daoResposta.Resposta(idResposta).get(0).getTxtResposta());
        }
        final EditText finalEditHora = editHora;

        String stringHora = finalEditHora.getText().toString();
        stringHora = stringHora.replaceAll("[:]", "");
        if (stringHora.toString().equals(qestaotodo)) {
            pegaTodo.Pega(true);
        }

        finalEditHora.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String stringHora = finalEditHora.getText().toString();
                stringHora = stringHora.replaceAll("[:]", "");
                if (stringHora.toString().equals(qestaotodo)) {
                    CriaTodo.criaTodo(context, idPergunta, idFormItem, idResposta, 5);
                    pegaTodo.Pega(true);
                    daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                } else {
                    pegaTodo.Pega(false);
                }

            }
        });

        editHora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Pergunta p = new Pergunta();
                    p.setIdPergunta(idPergunta);
                    resposta.setIdPergunta(idPergunta);
                    resposta.setIdFormItem(idFormItem);
                    resposta.setTxtResposta(finalEditHora.getText().toString());

                    idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                    if (idResposta <= 0) {
                        //salva
                        daoResposta.incluirResposta(resposta);

                        String stringHora = finalEditHora.getText().toString();
                        stringHora = stringHora.replaceAll("[:]", "");
                        if (stringHora.toString().equals(qestaotodo)) {
                            daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                        }
                    } else {
                        //altera
                        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                        resposta.setIdResposta(idResposta);
                        resposta.setTxtResposta(finalEditHora.getText().toString());
                        daoResposta.alterarResposta(resposta);
                    }

                    pegaQtdeResposta.Pega(daoResposta.contadorResposta(idFormItem));
                }
            }
        });

        editHora.setLayoutParams(lpViewEdit);
        ll.addView(editHora);

        linearLayout.addView(ll);

        if (getItemCount == cntquestoes + 1) {
            Button bt = new Button(context);
            bt.setText("Concluir");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finalEditHora.clearFocus();
                    //Salvar Resposta do formulário
                    perguntaDao = new PerguntaDao(context);
                    formItemDao = new FormItemDao(context);

                    if (daoResposta.contadorResposta(idFormItem) == perguntaDao.QtdePergunta(formItemDao.buscarIdSetor(idFormItem))) {
                        statusForm = 1;
                        formItemDao.alterarStatus(idFormItem);
                    }
                    FinalizacaoFormulario.DialogFinalizacao(context, statusForm, idFormItem);
                }
            });
            linearLayout.addView(bt);
        }
        return linearLayout;
    }

    public static LinearLayout createRating(final Context context, String question, RatingBar rating, final String qestaotodo, final List<OpcaoResposta> opcao, final int idFormItem, final int idPergunta, final int cntquestoes, final int getItemCount) {

        final RespostaDao daoResposta = new RespostaDao(context);
        final Resposta resposta = new Resposta();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams lpViewRating =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
        tv.setText(question);
        tv.setLayoutParams(lpView);
        linearLayout.addView(tv);

        rating = new RatingBar(context);
        rating.setNumStars(3);
        rating.setStepSize(1);
        rating.setProgress(0);

        final RatingBar finalRating = rating;
        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
        //trazendo do banco se já estiver sido salvo
        if (daoResposta.Resposta(idResposta).size() != 0) {
            rating.setRating(Float.parseFloat(daoResposta.Resposta(idResposta).get(0).getTxtResposta()));
        }


        String stringRating = finalRating.getRating() + "";
        stringRating = stringRating.replaceAll("[.]", "");
        stringRating = stringRating.replace("0", "");

        if (stringRating.equals(qestaotodo)) {
            pegaTodo.Pega(true);
        }

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b) {
                    Pergunta p = new Pergunta();
                    p.setIdPergunta(idPergunta);
                    resposta.setIdPergunta(idPergunta);
                    resposta.setIdFormItem(idFormItem);
                    resposta.setTxtResposta(finalRating.getRating() + "");
                    idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                    if (idResposta <= 0) {
                        //salva
                        daoResposta.incluirResposta(resposta);
                    } else {
                        //altera
                        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                        resposta.setIdResposta(idResposta);
                        resposta.setTxtResposta(finalRating.getRating() + "");
                        daoResposta.alterarResposta(resposta);
                    }


                    String stringRating = finalRating.getRating() + "";
                    stringRating = stringRating.replaceAll("[.]", "");
                    stringRating = stringRating.replace("0", "");

                    if (stringRating.equals(qestaotodo)) {
                        CriaTodo.criaTodo(context, idPergunta, idFormItem, idResposta, 6);
                        pegaTodo.Pega(true);
                        daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                    }

                    pegaQtdeResposta.Pega(daoResposta.contadorResposta(idFormItem));

                }
            }
        });

        if (getItemCount == cntquestoes + 1) {
            Button bt = new Button(context);
            bt.setText("Concluir");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Salvar Resposta do formulário
                    finalRating.clearFocus();
                    perguntaDao = new PerguntaDao(context);
                    formItemDao = new FormItemDao(context);

                    if (daoResposta.contadorResposta(idFormItem) == perguntaDao.QtdePergunta(formItemDao.buscarIdSetor(idFormItem))) {
                        statusForm = 1;
                        formItemDao.alterarStatus(idFormItem);
                    }
                    FinalizacaoFormulario.DialogFinalizacao(context, statusForm, idFormItem);
                }
            });
            linearLayout.addView(bt);
        }

        rating.setLayoutParams(lpViewRating);
        linearLayout.addView(rating);


        return linearLayout;
    }


    public static LinearLayout createEditNumber(final Context context, String question, EditText editText, final String qestaotodo, final List<OpcaoResposta> opcao, final int idFormItem, final int idPergunta, final int cntquestoes, final int getItemCount) {

        final RespostaDao daoResposta = new RespostaDao(context);
        final Resposta resposta = new Resposta();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams
                lpViewEdit = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
        tv.setText(question);
        tv.setLayoutParams(lpView);
        linearLayout.addView(tv);

        editText = new EditText(context);
        editText.setEnabled(true);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(lpViewEdit);
        editText.setFocusable(true);

        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);

        if (daoResposta.Resposta(idResposta).size() != 0) {
            editText.setText(daoResposta.Resposta(idResposta).get(0).getTxtResposta());
        }

        final EditText finalEditText = editText;

        if (finalEditText.getText().toString().equals(qestaotodo)) {
//                CriaTodo.criaTodo(context);
            pegaTodo.Pega(true);
        }

        finalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (finalEditText.getText().toString().equals(qestaotodo)) {
                    CriaTodo.criaTodo(context, idPergunta, idFormItem, idResposta, 7);
                    pegaTodo.Pega(true);
                    daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                } else {
                    pegaTodo.Pega(false);
                }
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Pergunta p = new Pergunta();
                    p.setIdPergunta(idPergunta);
                    resposta.setIdPergunta(idPergunta);
                    resposta.setIdFormItem(idFormItem);
                    resposta.setTxtResposta(finalEditText.getText().toString());
                    idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                    if (idResposta <= 0) {
                        //salva
                        daoResposta.incluirResposta(resposta);
                        if (finalEditText.getText().toString().equals(qestaotodo)) {
                            daoResposta.isTodo(daoResposta.buscarIdResposta(idFormItem, idPergunta));
                        }

                    } else {
                        //altera
                        idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta);
                        resposta.setIdResposta(idResposta);
                        resposta.setTxtResposta(finalEditText.getText().toString());
                        daoResposta.alterarResposta(resposta);
                    }

                    pegaQtdeResposta.Pega(daoResposta.contadorResposta(idFormItem));

                }
            }
        });

        linearLayout.addView(editText);
        concluido = 0;
        if (getItemCount == cntquestoes + 1) {
            Button bt = new Button(context);
            bt.setText("Concluir");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Salvar Resposta do formulário
                    finalEditText.clearFocus();
                    perguntaDao = new PerguntaDao(context);
                    formItemDao = new FormItemDao(context);

                    if (daoResposta.contadorResposta(idFormItem) == perguntaDao.QtdePergunta(formItemDao.buscarIdSetor(idFormItem))) {
                        statusForm = 1;
                        formItemDao.alterarStatus(idFormItem);
                    }
                    FinalizacaoFormulario.DialogFinalizacao(context, statusForm, idFormItem);
                }
            });
            linearLayout.addView(bt);
        }


        return linearLayout;
    }

    public static LinearLayout createListOpcoes(final Context context, String question, final List<OpcaoResposta> opcao, final int idFormItem, final int idPergunta, final int cntQuestoes, final int getItemCount) {
        final RespostaDao daoResposta = new RespostaDao(context);
        final Resposta resposta = new Resposta();
        final OpcaoRespostaDao opcaoRespostaDao = new OpcaoRespostaDao(context);
        formItemDao = new FormItemDao(context);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
        tv.setText(question);
        tv.setLayoutParams(lpView);
        linearLayout.addView(tv);

        //criacao da Lista
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParamsLeft = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 20f);
        TableRow.LayoutParams rowParamsRight = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 30f);

        tableParams.setMargins(16, 16, 16, 0);

        TableRow row = new TableRow(context);
        row.setLayoutParams(tableParams);

        TextView tvTitulo = new TextView(context);
        tvTitulo.setTextColor(Color.BLACK);
        tvTitulo.setTextSize(15);
        tvTitulo.setText("Item");
        tvTitulo.setGravity(Gravity.CENTER);
        tvTitulo.setLayoutParams(rowParamsLeft);
        tvTitulo.setBackgroundResource(R.drawable.border_lista_titulo);
        row.addView(tvTitulo);

        TextView tvValue = new TextView(context);
        tvValue.setTextColor(Color.BLACK);
        tvValue.setTextSize(15);
        tvValue.setText("Valor");
        tvValue.setGravity(Gravity.CENTER);
        tvValue.setLayoutParams(rowParamsRight);
        tvValue.setBackgroundResource(R.drawable.border_lista_titulo);
        row.addView(tvValue);

        TextView tvTodo = new TextView(context);
        tvTodo.setTextColor(Color.BLACK);
        tvTodo.setTextSize(15);
        tvTodo.setText("Todo");
        tvTodo.setGravity(Gravity.CENTER);
        tvTodo.setLayoutParams(rowParamsRight);
        tvTodo.setBackgroundResource(R.drawable.border_lista_titulo);
        row.addView(tvTodo);

        linearLayout.addView(row);

        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(lpView);
        linearLayout.addView(recyclerView);

        final LinearLayoutManager ll = new LinearLayoutManager(context);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);


        ListQuestionAdapter adapter = new ListQuestionAdapter(opcaoRespostaDao.listarVoltaParaListaItem(idFormItem, idPergunta), context, idFormItem);
        recyclerView.setAdapter(adapter);

        final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        adapter.setChangePosition(new ListQuestionAdapter.ChangePosition() {
            @Override
            public void onItemPosition(int position) {
                smoothScroller.setTargetPosition(position);
                ll.startSmoothScroll(smoothScroller);
            }
        });

        adapter.setChangeTextListener(new ListQuestionAdapter.ItemChangeListener() {
            @Override
            public void onItemClick(int position, BigDecimal valor) {

                resposta.setIdOpcao(opcao.get(position).getIdOpcao());
                resposta.setTxtResposta(valor.toString());
                resposta.setValor(opcao.get(position).getValor());
                resposta.setTodo(opcao.get(position).getToDo());
                resposta.setIdPergunta(idPergunta);
                resposta.setIdFormItem(idFormItem);

                idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta, opcao.get(position).getIdOpcao());
                if (idResposta <= 0) {
                    daoResposta.incluirResposta(resposta);
                } else {
                    resposta.setIdResposta(idResposta);
                    daoResposta.alterarResposta(resposta);
                }

                pegaQtdeResposta.Pega(daoResposta.contadorResposta(idFormItem));
            }

        });

        adapter.setCriaTodo(new ListQuestionAdapter.CriaTodo() {
            @Override
            public void onItemCriaTodo(boolean cria, int position) {
                if (cria) {
                    int idResposta = daoResposta.buscarIdResposta(idFormItem, idPergunta, opcao.get(position).getIdOpcao());
                    CriaTodo.criaTodo(context, idPergunta, idFormItem, idResposta, 1);
                }
            }
        });


        if (getItemCount == cntQuestoes + 1)

        {
            Button bt = new Button(context);
            bt.setText("Concluir");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perguntaDao = new PerguntaDao(context);
                    formItemDao = new FormItemDao(context);

                    if (daoResposta.contadorResposta(idFormItem) == perguntaDao.QtdePergunta(formItemDao.buscarIdSetor(idFormItem))) {
                        statusForm = 1;
                        formItemDao.alterarStatus(idFormItem);
                    }
                    FinalizacaoFormulario.DialogFinalizacao(context, statusForm, idFormItem);
                }
            });
            linearLayout.addView(bt);
        }

        return linearLayout;
    }

}
