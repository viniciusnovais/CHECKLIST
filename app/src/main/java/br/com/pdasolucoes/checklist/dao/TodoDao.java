package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.renderer.scatter.ChevronUpShapeRenderer;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.Agenda;
import br.com.pdasolucoes.checklist.model.Todo;

/**
 * Created by PDA on 20/12/2016.
 */

public class TodoDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public TodoDao(Context context) {
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

    public long incluir(Todo t) {

        ContentValues values = new ContentValues();
        values.put("justificativa", t.getJustificativa());
        values.put("acao", t.getAcao());
        values.put("status", t.getStatus());
        values.put("responsavel", t.getResponsavel());
        values.put("prazo", t.getPrazo());
        values.put("data", t.getDataLimite());
        values.put("follow", t.getFollowup());
        values.put("idPergunta", t.getIdPergunta());
        values.put("idFormItem", t.getIdFormItem());

        return getDataBase().insert("todo",null,values);

    }


    public List<Todo> listarTodo() {
        List<Todo> lista = new ArrayList<Todo>();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM todo", null);
        while (cursor.moveToNext()) {

            Todo todo = new Todo();
            todo.setIdTodo(cursor.getInt(cursor.getColumnIndex("_idTodo")));
            todo.setJustificativa(cursor.getString(cursor.getColumnIndex("justificativa")));
            todo.setAcao(cursor.getString(cursor.getColumnIndex("acao")));
            todo.setResponsavel(cursor.getString(cursor.getColumnIndex("responsavel")));
            todo.setPrazo(cursor.getInt(cursor.getColumnIndex("prazo")));
            todo.setFollowup(cursor.getString(cursor.getColumnIndex("follow")));
            todo.setDataLimite(cursor.getString(cursor.getColumnIndex("data")));
            todo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            todo.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
            todo.setIdFormItem(cursor.getInt(cursor.getColumnIndex("idFormItem")));
            lista.add(todo);
        }
        return lista;
    }


    public Todo buscarMaior() {
        Todo todo = new Todo();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM todo WHERE _idTodo = (SELECT MAX(_idTodo) FROM todo)", null);

        while (cursor.moveToNext()) {

            todo.setIdTodo(cursor.getInt(cursor.getColumnIndex("_idTodo")));
            todo.setJustificativa(cursor.getString(cursor.getColumnIndex("justificativa")));
            todo.setAcao(cursor.getString(cursor.getColumnIndex("acao")));
            todo.setResponsavel(cursor.getString(cursor.getColumnIndex("responsavel")));
            todo.setPrazo(cursor.getInt(cursor.getColumnIndex("prazo")));
            todo.setFollowup(cursor.getString(cursor.getColumnIndex("follow")));
            todo.setDataLimite(cursor.getString(cursor.getColumnIndex("data")));
            todo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            todo.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
            todo.setIdFormItem(cursor.getInt(cursor.getColumnIndex("idFormItem")));
        }

        return todo;
    }

    public int contadorTodo(int idFormItem){
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(_idTodo) as cnt FROM todo WHERE idFormItem = ?",new String[]{idFormItem+""});

        while (cursor.moveToNext()){
            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
        }

        return cnt;
    }

    public int contadorTodoPergunta(int idFormItem, int idPergunta){
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(_idTodo) as cnt FROM todo WHERE idFormItem = ? and idPergunta = ?",new String[]{idFormItem+"",idPergunta+""});

        while (cursor.moveToNext()){
            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
        }

        return cnt;
    }

    public int contadorRespostaFaltaTodo(){
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as cnt FROM(SELECT r._idResposta, r.txtResposta, r.idPergunta,r.idFormItem,r.idOpcao,r.todo" +
                " FROM resposta r where r.todo=1 and idPergunta NOT IN(SELECT idPergunta FROM todo)) X",null);

        try{
            while (cursor.moveToNext()){
                cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
            }
        }finally {
            cursor.close();
        }

        return cnt;
    }


}
