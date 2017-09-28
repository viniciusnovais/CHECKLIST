package br.com.pdasolucoes.checklist.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.pdasolucoes.checklist.activities.QuestionsActivity;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.OpcaoRespostaDao;
import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.dao.TodoDao;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.OpcaoResposta;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Resposta;
import br.com.pdasolucoes.checklist.model.Todo;
import br.com.pdasolucoes.checklist.util.ComponenteResposta;
import br.com.pdasolucoes.checklist.util.VerificaConexao;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 16/12/2016.
 */

public class Question2Adapter extends RecyclerView.Adapter<Question2Adapter.MyViewHolder> {

    private List<Pergunta> listaPergunta;
    private Context context;
    private int id = 0, idPergunta = 0, cntqestoes = 0;
    int[] vetorIdPergunta, vetorTipoPergunta;
    private LayoutInflater mLayoutInflater;
    private PegaPosition pegaPosition;
    private PegaTodo pegaTodo;
    private FormItemDao daoItem;
    String[] vetorQestoes;
    private Resposta resposta;
    private RespostaDao daoRespostaa;
    private OpcaoResposta opResposta;
    private OpcaoRespostaDao daoOpResposta;
    private PerguntaDao perguntaDao;
    private List<OpcaoResposta> listaOpResposta;
    private int idFormItem = 0;

    //Interfaces
    public interface PegaTodo {
        void Pega(boolean todo);
    }

    public interface PegaPosition {
        void pegaPosition(int pegaPosition, int idFormItem, int idPergunta);
    }

    public void PegaTodoListener(PegaTodo pegaTodo) {
        this.pegaTodo = pegaTodo;
    }

    public void PegaPositionListener(PegaPosition pegaPosition) {
        this.pegaPosition = pegaPosition;
    }

    //Construtor
    public Question2Adapter(Context c, List<Pergunta> l, int idFormItem) {
        this.context = c;
        this.listaPergunta = l;
        this.idFormItem = idFormItem;
        vetorQestoes = new String[getItemCount()];
        vetorTipoPergunta = new int[getItemCount()];
        vetorIdPergunta = new int[getItemCount()];
        resposta = new Resposta();
        daoRespostaa = new RespostaDao(c);
        daoItem = new FormItemDao(c);
        opResposta = new OpcaoResposta();
        perguntaDao = new PerguntaDao(c);
        daoOpResposta = new OpcaoRespostaDao(c);
        listaOpResposta = new ArrayList<>();
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        AsyncOpResposta task = new AsyncOpResposta();
//        task.execute();
    }

    //Cria uma nova View
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.question_item, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);

        mvh.setIsRecyclable(false);

        return mvh;
    }

    //Chamado toda  vez, vincula os dados da lista com a view
    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {
        int qtdeQuestoes;

        listaPergunta.get(position).getIdForm().getIdForm();

        //Create RadioButton
        if (listaPergunta.get(position).getTipoPergunta() == 2) {
            idPergunta = listaPergunta.get(position).getIdPergunta();
            myViewHolder.radio = new RadioButton[daoOpResposta.listar(idPergunta).size()];
            cntqestoes = position;

            pegaPosition.pegaPosition(position, idFormItem, idPergunta);

            myViewHolder.frameLayout.addView(
                    ComponenteResposta.createRadioButton(context, position + 1 + "." + listaPergunta.get(position).getTxtPergunta(), myViewHolder.radio, listaPergunta.get(position).getOpcaoQuestaoTodo(), daoOpResposta.listar(idPergunta), idFormItem, idPergunta, getItemCount(), cntqestoes));

            //Create CheckBox
        } else if (listaPergunta.get(position).getTipoPergunta() == 4) {
            idPergunta = listaPergunta.get(position).getIdPergunta();
            myViewHolder.check = new CheckBox[daoOpResposta.listar(idPergunta).size()];
            cntqestoes = position;
            pegaPosition.pegaPosition(position, idFormItem, idPergunta);

            myViewHolder.frameLayout.addView(
                    ComponenteResposta.createCheck(context, position + 1 + "." + listaPergunta.get(position).getTxtPergunta(), myViewHolder.check, listaPergunta.get(position).getOpcaoQuestaoTodo(), daoOpResposta.listar(idPergunta), idFormItem, getItemCount(), cntqestoes, idPergunta));
            //Create EditText
        } else if (listaPergunta.get(position).getTipoPergunta() == 1) {
            idPergunta = listaPergunta.get(position).getIdPergunta();
            cntqestoes = position;
            pegaPosition.pegaPosition(position, idFormItem, idPergunta);

            myViewHolder.frameLayout.addView(
                    ComponenteResposta.createEditText(context, position + 1 + "." + listaPergunta.get(position).getTxtPergunta(),
                            myViewHolder.editText, listaPergunta.get(position).getOpcaoQuestaoTodo(), daoOpResposta.listar(idPergunta),
                            idFormItem, getItemCount(), cntqestoes, idPergunta));
            //Create Date
        } else if (listaPergunta.get(position).getTipoPergunta() == 5) {
            idPergunta = listaPergunta.get(position).getIdPergunta();
            cntqestoes = position;

            pegaPosition.pegaPosition(position, idFormItem, idPergunta);

            myViewHolder.frameLayout.addView(
                    ComponenteResposta.createDate(context, position + 1 + "." + listaPergunta.get(position).getTxtPergunta(), myViewHolder.editData,
                            listaPergunta.get(position).getOpcaoQuestaoTodo(), daoOpResposta.listar(idPergunta), cntqestoes, getItemCount(), idFormItem, idPergunta));
            //Create Hour
        } else if (listaPergunta.get(position).getTipoPergunta() == 7) {
            idPergunta = listaPergunta.get(position).getIdPergunta();
            cntqestoes = position;

            pegaPosition.pegaPosition(position, idFormItem, idPergunta);

            myViewHolder.frameLayout.addView(
                    ComponenteResposta.createHour(context, position + 1 + "." + listaPergunta.get(position).getTxtPergunta(),
                            myViewHolder.editTextHora, listaPergunta.get(position).getOpcaoQuestaoTodo(),
                            daoOpResposta.listar(idPergunta), idFormItem, getItemCount(), cntqestoes, idPergunta));

        //avaliação
        } else if (listaPergunta.get(position).getTipoPergunta() == 9) {
            idPergunta = listaPergunta.get(position).getIdPergunta();
            myViewHolder.rating = new RatingBar(context);
            cntqestoes = position;
            pegaPosition.pegaPosition(position, idFormItem, idPergunta);

            myViewHolder.frameLayout.addView(
                    ComponenteResposta.createRating(context, position + 1 + "." + listaPergunta.get(position).getTxtPergunta(), myViewHolder.rating, listaPergunta.get(position).getOpcaoQuestaoTodo(),
                            daoOpResposta.listar(idPergunta), idFormItem, idPergunta, cntqestoes, getItemCount()));
        } else if (listaPergunta.get(position).getTipoPergunta() == 3) {
            idPergunta = listaPergunta.get(position).getIdPergunta();
            myViewHolder.editNumber = new EditText(context);
            cntqestoes = position;

            pegaPosition.pegaPosition(position, idFormItem, idPergunta);

            myViewHolder.frameLayout.addView(
                    ComponenteResposta.createEditNumber(context, position + 1 + "." + listaPergunta.get(position).getTxtPergunta(), myViewHolder.editNumber, listaPergunta.get(position).getOpcaoQuestaoTodo(),
                            daoOpResposta.listar(idPergunta), idFormItem, idPergunta, cntqestoes, getItemCount()));
        } else if (listaPergunta.get(position).getTipoPergunta() == 8) {
            idPergunta = listaPergunta.get(position).getIdPergunta();
            cntqestoes = position;

            pegaPosition.pegaPosition(position, idFormItem, idPergunta);

            myViewHolder.frameLayout.addView(ComponenteResposta.createListOpcoes(context, position + 1 + "." + listaPergunta.get(position).getTxtPergunta(), daoOpResposta.listar(idPergunta),
                    idFormItem, idPergunta, cntqestoes, getItemCount()));


        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //Tamanho da Lista
    @Override
    public int getItemCount() {
        return listaPergunta.size();
    }

    public Pergunta getItem(int position) {
        return listaPergunta.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout frameLayout;
        public RadioButton radio[];
        public CheckBox check[];
        public EditText editText, editNumber, editTextHora, editData;
        public RatingBar rating;

        public MyViewHolder(View itemView) {
            super(itemView);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frameLayoutQuestion);
        }

    }


    public class AsyncOpResposta extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            Intent i = ((Activity) context).getIntent();
            if (!i.hasExtra("QueryActivity")) {
//                if (VerificaConexao.isNetworkConnected(context)) {
//                    List<Pergunta> lista = perguntaDao.listar(daoItem.buscarIdSetor(idFormItem));
//                    for (int cnt = 0; cnt < lista.size(); cnt++) {
//                        daoOpResposta.incluir(daoOpResposta.listarOpcaoResposta(lista.get(cnt).getIdPadraoResposta(), lista.get(cnt).getIdPergunta()));
//                    }
//                }
            }
            return null;
        }
    }

}

