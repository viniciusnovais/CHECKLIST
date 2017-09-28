package br.com.pdasolucoes.checklist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.security.acl.NotOwnerException;
import java.util.List;

import br.com.pdasolucoes.checklist.activities.QuestionsActivity;
import br.com.pdasolucoes.checklist.dao.OpcaoRespostaDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.model.OpcaoResposta;
import br.com.pdasolucoes.checklist.util.CriaTodo;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 12/09/2017.
 */

public class ListQuestionAdapter extends RecyclerView.Adapter<ListQuestionAdapter.MyViewHolder> {

    private List<OpcaoResposta> lista;
    private Context context;
    private ItemChangeListener itemChangeListener;
    private static ChangePosition changePosition;
    private int idFormItem;
    private RespostaDao daoResposta;
    private static NotTodo notTodo;
    private CriaTodo criaTodo;


    public interface ItemChangeListener {
        void onItemClick(int position, BigDecimal valor);
    }

    public void setChangeTextListener(ItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    public interface ChangePosition {
        void onItemPosition(int position);
    }

    public void setChangePosition(ChangePosition changePosition) {
        this.changePosition = changePosition;
    }

    public interface CriaTodo {
        void onItemCriaTodo(boolean cria, int position);
    }

    public void setCriaTodo(CriaTodo criaTodo) {
        this.criaTodo = criaTodo;
    }

    public interface NotTodo {
        void onItemNotTodo(boolean not, int position);
    }

    public void setNotTodo(NotTodo notTodo) {
        this.notTodo = notTodo;
    }


    public ListQuestionAdapter(List<OpcaoResposta> lista, Context context, int idFormItem) {
        this.lista = lista;
        this.context = context;
        this.idFormItem = idFormItem;
        daoResposta = new RespostaDao(context);
    }

    @Override
    public ListQuestionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_opcoes, parent, false);
        MyViewHolder mv = new MyViewHolder(v);

        //mv.setIsRecyclable(false);

        return mv;
    }

    @Override
    public void onBindViewHolder(final ListQuestionAdapter.MyViewHolder holder, final int position) {

        final OpcaoResposta op = lista.get(position);

        holder.textView.setText(op.getOpcao());

        holder.editText.setText(op.getTxtResposta());

        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    changePosition.onItemPosition(position);
                }
            }
        });


        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!holder.editText.getText().toString().equals("")) {
                        op.setTxtResposta(holder.editText.getText().toString());
                        VerificaoTodo(holder.imageView, op, position);
                        itemChangeListener.onItemClick(position, new BigDecimal(String.valueOf(op.getTxtResposta())));
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        VerificaoTodo(holder.imageView, op, position);


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private EditText editText;
        private TextView textView;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            editText = (EditText) itemView.findViewById(R.id.editText);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public void VerificaoTodo(ImageView imageView, OpcaoResposta op, final int position) {
        if (!op.getTxtResposta().toString().equals("")) {
            if (op.getTipoCondicao().equals("intervalo")) {
                if (!(Float.parseFloat(op.getTxtResposta().toString()) >= op.getMaior() &&
                        Float.parseFloat(op.getTxtResposta().toString()) <= op.getMenor())
                        && op.getToDo() == 1) {
                    imageView.setImageResource(R.drawable.todo_48);
                    imageView.setEnabled(true);
                    notTodo.onItemNotTodo(false,position);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            criaTodo.onItemCriaTodo(true, position);
                        }
                    });
                } else {
                    notTodo.onItemNotTodo(true,position);
                    imageView.setImageResource(R.drawable.todo_48_gray);
                    imageView.setEnabled(true);
                }
            } else if (op.getTipoCondicao().equals("maior")) {
                if (!(op.getMaior() <= Float.parseFloat(op.getTxtResposta().toString()))
                        && op.getToDo() == 1) {
                    notTodo.onItemNotTodo(false,position);
                    imageView.setImageResource(R.drawable.todo_48);
                    imageView.setEnabled(true);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            criaTodo.onItemCriaTodo(true, position);
                        }
                    });
                } else {
                    notTodo.onItemNotTodo(true,position);
                    imageView.setImageResource(R.drawable.todo_48_gray);
                    imageView.setEnabled(true);
                }
            } else if (op.getTipoCondicao().equals("menor")) {

                if (!(op.getMenor() >= Float.parseFloat(op.getTxtResposta().toString()))
                        && op.getToDo() == 1) {
                    notTodo.onItemNotTodo(false,position);
                    imageView.setImageResource(R.drawable.todo_48);
                    imageView.setEnabled(true);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            criaTodo.onItemCriaTodo(true, position);
                        }
                    });
                } else {
                    notTodo.onItemNotTodo(true,position);
                    imageView.setImageResource(R.drawable.todo_48_gray);
                    imageView.setEnabled(true);
                }
            }

        }
    }
}
