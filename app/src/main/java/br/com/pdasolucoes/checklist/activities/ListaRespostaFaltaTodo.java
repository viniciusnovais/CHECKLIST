package br.com.pdasolucoes.checklist.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.pdasolucoes.checklist.adapter.ListaRespostaFaltaTodoAdapter;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.model.Resposta;
import br.com.pdasolucoes.checklist.util.CriaTodo;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 30/05/2017.
 */

public class ListaRespostaFaltaTodo extends AppCompatActivity {


    private ListView listView;
    private ListaRespostaFaltaTodoAdapter adapter;
    private RespostaDao respostaDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_resposta_falta_todo_activity);
        respostaDao = new RespostaDao(this);

        listView = (ListView) findViewById(R.id.listView);

        adapter = new ListaRespostaFaltaTodoAdapter(respostaDao.listaFaltaTodo(),this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Resposta r = (Resposta) adapter.getItem(position);
                Log.w("position",position+"");
                Log.w("idPrergunta",r.getIdPergunta().getIdPergunta()+"");
                Log.w("idFormItem",r.getIdFormItem()+"");
                CriaTodo.criaTodo(ListaRespostaFaltaTodo.this,r.getIdPergunta().getIdPergunta(),r.getIdFormItem());
            }
        });
    }
}
