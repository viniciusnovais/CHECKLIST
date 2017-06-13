package br.com.pdasolucoes.checklist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.pdasolucoes.checklist.adapter.QueryAdapter;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.Resposta;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 26/01/2017.
 */

public class ListaQuery extends AppCompatActivity {

    private QueryAdapter adapter;
    private FormItemDao daoItem;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_query);


        daoItem = new FormItemDao(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);

        adapter = new QueryAdapter(daoItem.listarItemForm(), getApplicationContext());
        recyclerView.setAdapter(adapter);


        adapter.ItemClickListener(new QueryAdapter.ItemClick() {
            @Override
            public void Click(int position) {

                Intent i = new Intent(ListaQuery.this, QueryActivity.class);
                //Pegando text do adapter que está na lista
                FormItem item = (FormItem) adapter.getItem(position);

                i.putExtra("idItem", item.getIdItem());
                i.putExtra("idForm", item.getIdForm().getIdForm());

                Toast.makeText(getApplicationContext(), "idItem: " + item.getIdItem(), Toast.LENGTH_SHORT).show();
                startActivity(i);
            }

        });
    }
}
