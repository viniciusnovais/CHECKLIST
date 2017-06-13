package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import br.com.pdasolucoes.checklist.model.ComplementoResposta;

/**
 * Created by PDA on 18/04/2017.
 */

public class ComplementoRespostaDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public ComplementoRespostaDao(Context context) {
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

    public long incluir(ComplementoResposta complementoResposta) {
        ContentValues values = new ContentValues();

        values.put("image", complementoResposta.getImage());
        values.put("comentario", complementoResposta.getComentario());
        values.put("idPergunta", complementoResposta.getIdPergunta());
        values.put("idFormItem", complementoResposta.getIdFormItem());

        return getDataBase().insert("complementoResposta", null, values);

    }

    public int contadorImagens(int idFormItem) {
        int contador = 0;

        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(_idComplemento) as contador FROM complementoResposta " +
                "WHERE idFormItem=? and image IS NOT NULL", new String[]{idFormItem + ""});

        while (cursor.moveToNext()) {
            contador = cursor.getInt(cursor.getColumnIndex("contador"));
        }
        return contador;
    }

    public int contadorComentario(int idFormItem) {
        int contador = 0;

        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(_idComplemento) as contador FROM complementoResposta " +
                "WHERE idFormItem=? and comentario IS NOT NULL", new String[]{idFormItem + ""});

        while (cursor.moveToNext()) {
            contador = cursor.getInt(cursor.getColumnIndex("contador"));
        }
        return contador;
    }
}
