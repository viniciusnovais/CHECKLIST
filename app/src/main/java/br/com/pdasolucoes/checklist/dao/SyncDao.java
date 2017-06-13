package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.Sync;

/**
 * Created by PDA on 12/12/2016.
 */

public class SyncDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public SyncDao(Context context) {
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

    public long incluir(Sync sync) {
        ContentValues values = new ContentValues();

            Log.w("idForm", sync.getIdForm() + "");
            values.put("idForm", sync.getIdForm());
            return getDataBase().insert("sync", null, values);

    }
}
