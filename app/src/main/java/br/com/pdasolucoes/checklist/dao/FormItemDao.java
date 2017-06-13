package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.Resposta;

/**
 * Created by PDA on 23/01/2017.
 */

public class FormItemDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public FormItemDao(Context context) {
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

    public long incluir(FormItem item) {
        ContentValues values = new ContentValues();
        values.put("idForm", item.getIdForm().getIdForm());
        values.put("status",0);
        return getDataBase().insert("formItem", null, values);
    }

    public List<FormItem> listarItemForm(){
        List<FormItem> lista =new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery(
                "SELECT i._idFormItem, i.idForm,f.nomeForm,i.status FROM formItem i, form f WHERE i.idForm = f._idForm", null);
        while (cursor.moveToNext()) {
            FormItem form = new FormItem();
            form.setIdItem(cursor.getInt(cursor.getColumnIndex("_idFormItem")));
            form.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            Form f = new Form();
            f.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
            f.setNomeFom(cursor.getString(cursor.getColumnIndex("nomeForm")));
            form.setIdForm(f);

            lista.add(form);
        }

        return  lista;
    }

    public List<FormItem> listarItemFormSync(){
        List<FormItem> lista =new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery(
                "SELECT i._idFormItem, i.idForm,f.nomeForm,i.status,f.dataForm,f.nomeLoja,f.hora FROM formItem i, form f WHERE i.idForm = f._idForm and i.status=1", null);
        while (cursor.moveToNext()) {
            FormItem form = new FormItem();
            form.setIdItem(cursor.getInt(cursor.getColumnIndex("_idFormItem")));
            form.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            Form f = new Form();
            f.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
            f.setNomeFom(cursor.getString(cursor.getColumnIndex("nomeForm")));
            f.setDataForm(cursor.getString(cursor.getColumnIndex("dataForm")));
            f.setNomeLoja(cursor.getString(cursor.getColumnIndex("nomeLoja")));
            f.setHora(cursor.getInt(cursor.getColumnIndex("hora")));
            form.setIdForm(f);

            lista.add(form);
        }

        return  lista;
    }

    public long alterarStatus(int idFormItem){
        ContentValues values = new ContentValues();
        values.put("status",1);

        return getDataBase().update("formItem",values,"_idFormItem  = ?",new String[]{idFormItem+""});
    }

    public int buscarIdForm(int idFormItem){
        int id=0;
        Cursor cursor = getDataBase().rawQuery(
                "SELECT idForm FROM formItem WHERE _idFormItem=?",new String[]{idFormItem+""});
        while (cursor.moveToNext()){
            id=cursor.getInt(cursor.getColumnIndex("idForm"));
        }
        return id;
    }

    public int buscarMaxId(){
        int id=0;
        Cursor cursor = getDataBase().rawQuery(
                "SELECT MAX(_idFormItem) as id from formItem",null);
        while (cursor.moveToNext()) {
            id=cursor.getInt(0);
        }
        cursor.close();

        return id;
    }
}
