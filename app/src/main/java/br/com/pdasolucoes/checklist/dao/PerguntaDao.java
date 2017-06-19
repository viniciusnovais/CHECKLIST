package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.util.WebService;

/**
 * Created by PDA on 01/12/2016.
 */

public class PerguntaDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;
    public static boolean error=false;

    public PerguntaDao(Context context) {
        helper = new DataBaseHelper(context);
    }

    public SQLiteDatabase getDataBase() {
        if (database == null) {
            database = helper.getWritableDatabase();
        }

        return database;
    }

    public void close() {
        helper.close();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public void incluir(List<Pergunta> lista) {
        ContentValues values = new ContentValues();
        for(Pergunta p: lista) {
            if (existePergunta(p.getIdPergunta()) == false) {
                values.put("_idPergunta", p.getIdPergunta());
                values.put("txtPergunta", p.getTxtPergunta());
                values.put("opcaoQuestaoTodo", p.getOpcaoQuestaoTodo());
                values.put("tipoPergunta",p.getTipoPergunta());
                values.put("idForm", p.getIdForm().getIdForm());

                getDataBase().insert("pergunta", null, values);
            }
        }
    }

    public boolean existePergunta(int id){
        boolean existe=false;
        Cursor cursor = getDataBase().rawQuery("SELECT _idPergunta FROM pergunta where _idPergunta = ?",new String[]{id+""});
        while (cursor.moveToNext()){
            existe=true;
        }
        return existe;
    }

    public List<Pergunta> listar(int id){
        List<Pergunta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM pergunta where idForm=?",new String[]{id+""});
        while (cursor.moveToNext()){
            Pergunta p = new Pergunta();
            p.setIdPergunta(cursor.getInt(cursor.getColumnIndex("_idPergunta")));
            p.setTxtPergunta(cursor.getString(cursor.getColumnIndex("txtPergunta")));
            p.setOpcaoQuestaoTodo(cursor.getString(cursor.getColumnIndex("opcaoQuestaoTodo")));
            p.setTipoPergunta(cursor.getInt(cursor.getColumnIndex("tipoPergunta")));
            Form f = new Form();
            f.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
            p.setIdForm(f);
            lista.add(p);
        }
        return lista;
    }

    public int QtdePergunta (int idForm){
        int qtde = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as cnt FROM pergunta WHERE idForm = ? ",new String[]{idForm+""});
        try {

            while (cursor.moveToNext()) {
                qtde = cursor.getInt(cursor.getColumnIndex("cnt"));
            }
        }finally {
            cursor.close();
        }

        return qtde;
    }


    public List<Pergunta> getPerguntaWS(int idForm) {
        String url = WebService.URL_SHEETS + "ebcb036a601a/search?idForm="+idForm;
        String resposta = WebService.makeRequest(url);
        JSONObject jsonObject;
        List<Pergunta> lista = new ArrayList<>();


        if (resposta == null) {

        } else {

            try {
                JSONArray json = new JSONArray(resposta);
                for (int i = 0; i < json.length(); i++) {
                    jsonObject = json.getJSONObject(i);

                    Pergunta p = new Pergunta();
                    p.setIdPergunta(jsonObject.getInt("idPergunta"));
                    p.setTxtPergunta(jsonObject.getString("pergunta"));
                    p.setTipoPergunta(jsonObject.getInt("tipoPergunta"));
                    p.setOpcaoQuestaoTodo(jsonObject.getString("opcaoQuestaoTodo"));
                    Form f = new Form();
                    f.setIdForm(jsonObject.getInt("idForm"));
                    p.setIdForm(f);

                    lista.add(p);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

}
