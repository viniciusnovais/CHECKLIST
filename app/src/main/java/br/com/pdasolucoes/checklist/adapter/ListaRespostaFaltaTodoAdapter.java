package br.com.pdasolucoes.checklist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.Resposta;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 30/05/2017.
 */

public class ListaRespostaFaltaTodoAdapter extends BaseAdapter {

    private List<Resposta> lista;
    private Context context;
    private PerguntaDao perguntaDao;

    public ListaRespostaFaltaTodoAdapter(List<Resposta> lista, Context context) {
        this.lista = lista;
        this.context = context;
        perguntaDao = new PerguntaDao(context);

    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.lista_item_resposta_falta_todo, null);

        TextView tvNomeForm = (TextView) v.findViewById(R.id.tvNomeForm);
        tvNomeForm.setText(perguntaDao.buscaNomePerguntaeForm(lista.get(position).getIdPergunta()).get(0).getIdForm().getNomeFom());

        TextView tvTituloPergunta = (TextView) v.findViewById(R.id.tvNumeroPergunta);
        tvTituloPergunta.setText(perguntaDao.buscaNomePerguntaeForm(lista.get(position).getIdPergunta()).get(0).getTxtPergunta());

        TextView tvRespostaNaoConforme = (TextView) v.findViewById(R.id.tvtextoTodo);
        tvRespostaNaoConforme.setText(lista.get(position).getTxtResposta());


        return v;
    }
}
