package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.pdasolucoes.checklist.model.Logo;

/**
 * Created by PDA on 10/08/2017.
 */

public class LogoDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public LogoDao(Context context) {
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

    public long incluir(Logo logo) {

        ContentValues values = new ContentValues();
        if (!alterarLogo(logo.getIdUsuario())) {
            values.put("idUsuario", logo.getIdUsuario());
            values.put("image", logo.getImagem());
            values.put("status", 0);

            return getDataBase().insert("logo", null, values);
        }
        return 0;
    }

    public boolean alterarLogo(int idUsuario) {
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM logo WHERE idUsuario = ?", new String[]{idUsuario + ""});

        try {
            while (cursor.moveToNext()) {
                return true;
            }
        } finally {
            cursor.close();
        }
        return false;
    }

    public Logo logo() {
        Logo logo = new Logo();
        Cursor cursor = getDataBase().rawQuery("SELECT image FROM logo", null);

        try {
            while (cursor.moveToNext()) {

                logo.setImagem(cursor.getBlob(cursor.getColumnIndex("image")));
            }
        } finally {
            cursor.close();
        }
        return logo;
    }

    public void deleteImage(int idUsuario) {
        getDataBase().delete("logo", "idUsuario = ?", new String[]{idUsuario + ""});
    }
}
