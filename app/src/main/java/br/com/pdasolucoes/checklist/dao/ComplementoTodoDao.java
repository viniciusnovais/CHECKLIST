package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.pdasolucoes.checklist.model.ComplementoTodo;

/**
 * Created by PDA on 25/04/2017.
 */

public class ComplementoTodoDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public ComplementoTodoDao(Context context) {
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

    public long incluir(ComplementoTodo ct) {

        ContentValues values = new ContentValues();

        values.put("image", ct.getImage());
        values.put("comentario", ct.getComentario());
        values.put("idTodo", ct.getIdTodo().getIdTodo());

        return getDataBase().insert("complementoTodo", null, values);
    }

    public int contadorComentario(int idFormItem, int idPergunta) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(_idComplemento) as cnt FROM complementoTodo ct, todo t " +
                "WHERE ct.idTodo=t._idTodo and t.idFormItem = ? and idPergunta = ? and ct.comentario IS NOT NULL", new String[]{idFormItem + "", idPergunta + ""});

        while (cursor.moveToNext()) {
            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));

        }
        return cnt;
    }

    public int contadorCamera(int idFormItem, int idPergunta) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(_idComplemento) as cnt FROM complementoTodo ct, todo t " +
                "WHERE ct.idTodo=t._idTodo and t.idFormItem = ? and idPergunta = ? and ct.image IS NOT NULL", new String[]{idFormItem + "", idPergunta + ""});

        while (cursor.moveToNext()) {
            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));

        }
        return cnt;
    }

}
