package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.pdasolucoes.checklist.model.Setor;
import br.com.pdasolucoes.checklist.model.Usuario;

/**
 * Created by PDA on 09/08/2017.
 */

public class UsuarioDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;
    public static boolean error = false;

    public UsuarioDao(Context context) {
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


    public void incluir(Usuario usuario) {

        ContentValues values = new ContentValues();
        if (!existeUsuario(usuario.getId())) {
            values.put("id", usuario.getId());
            values.put("nome", usuario.getNome());
            values.put("email", usuario.getEmail());
            values.put("senha", usuario.getSenha());
            getDataBase().insert("usuario", null, values);
        }
    }


    public boolean existeUsuario(int id) {
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM usuario WHERE id = ?", new String[]{id + ""});

        try {
            while (cursor.moveToNext()) {
                return true;
            }
        } finally {
            cursor.close();
        }
        return false;
    }


}
