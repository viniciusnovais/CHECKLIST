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
import br.com.pdasolucoes.checklist.model.Setor;

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
        values.put("status", 0);
        values.put("sync", 0);
        values.put("idSetor", item.getIdSetor().getId());
        values.put("horaInicio", item.getHoraInicio());
        values.put("horaFim", item.getHoraFim());
        values.put("dataInicio", item.getDataInicio());
        values.put("dataFim", item.getDataFim());
        values.put("idUsuario", item.getIdUsuario());
        return getDataBase().insert("formItem", null, values);
    }

    public List<FormItem> listarItemFormUsuario(int idUsuario) {
        List<FormItem> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery(
                "SELECT i._idFormItem, i.idForm,f.nomeForm,i.status,i.idSetor,s.nome as nomeSetor FROM formItem i, setor s,form f" +
                        " WHERE i.idUsuario = ? and i.idSetor = s.id and i.idForm = f._idForm order by i.status", new String[]{idUsuario + ""});
        while (cursor.moveToNext()) {
            FormItem form = new FormItem();
            form.setIdItem(cursor.getInt(cursor.getColumnIndex("_idFormItem")));
            form.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            Setor s = new Setor();
            s.setId(cursor.getInt(cursor.getColumnIndex("idSetor")));
            s.setNome(cursor.getString(cursor.getColumnIndex("nomeSetor")));
            form.setIdSetor(s);
            Form f = new Form();
            f.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
            f.setNomeFom(cursor.getString(cursor.getColumnIndex("nomeForm")));
            form.setIdForm(f);

            lista.add(form);
        }

        return lista;
    }

    public List<FormItem> listarItemForm(int idFormItem) {
        List<FormItem> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery(
                "SELECT dataInicio, dataFim, horaInicio, horaFim FROM formItem WHERE _idFormItem = ?", new String[]{idFormItem + ""});
        while (cursor.moveToNext()) {
            FormItem f = new FormItem();
            f.setDataInicio(cursor.getString(cursor.getColumnIndex("dataInicio")));
            f.setDataFim(cursor.getString(cursor.getColumnIndex("dataFim")));
            f.setHoraInicio(cursor.getString(cursor.getColumnIndex("horaInicio")));
            f.setHoraFim(cursor.getString(cursor.getColumnIndex("horaFim")));

            lista.add(f);
        }

        return lista;
    }

    public int idSetor(int idFormItem) {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery(
                "SELECT idSetor FROM formItem WHERE _idFormItem = ?", new String[]{idFormItem + ""});
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("idSetor"));
        }

        return id;
    }

    public List<FormItem> listarItemFormSync() {
        List<FormItem> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery(
                "SELECT i._idFormItem, i.idForm,f.nomeForm,i.status,f.dataForm,f.nomeLoja,f.hora FROM formItem i, form f WHERE i.idForm = f._idForm and i.status=1 and i.sync=0", null);
        while (cursor.moveToNext()) {
            FormItem form = new FormItem();
            form.setIdItem(cursor.getInt(cursor.getColumnIndex("_idFormItem")));
            form.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            Form f = new Form();
            f.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
            f.setNomeFom(cursor.getString(cursor.getColumnIndex("nomeForm")));
            f.setDataForm(cursor.getString(cursor.getColumnIndex("dataForm")));
            f.setNomeLoja(cursor.getString(cursor.getColumnIndex("nomeLoja")));
            f.setHora(cursor.getString(cursor.getColumnIndex("hora")));
            form.setIdForm(f);

            lista.add(form);
        }

        return lista;
    }

    public int contadorSync(int idUsuario) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery(
                "SELECT count(i._idFormItem) as cnt FROM formItem i WHERE i.sync=0 and i.status=1 and idUsuario = ?", new String[]{idUsuario + ""});
        while (cursor.moveToNext()) {

            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
        }

        return cnt;
    }

    public void deletar() {
        getDataBase().execSQL("DELETE FROM FORMITEM WHERE IDFORM NOT IN (SELECT _IDFORM FROM FORM)");
    }

    public int contadorFormItemConsulta(int idUsuario) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery(
                "SELECT count(_idFormItem) as cnt FROM formItem WHERE idUsuario = ?", new String[]{idUsuario + ""});
        while (cursor.moveToNext()) {

            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
        }

        return cnt;
    }

    public long alterarStatus(int idFormItem) {
        ContentValues values = new ContentValues();
        values.put("status", 1);

        return getDataBase().update("formItem", values, "_idFormItem  = ?", new String[]{idFormItem + ""});
    }

    public long alterarStatusSync(int idFormItem) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);

        return getDataBase().update("formItem", values, "_idFormItem  = ?", new String[]{idFormItem + ""});
    }

    public int buscarIdSetor(int idFormItem) {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery(
                "SELECT idSetor FROM formItem WHERE _idFormItem=?", new String[]{idFormItem + ""});
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("idSetor"));
        }
        return id;
    }

//    public int buscarIdForm(int idFormItem) {
//        int id = 0;
//        Cursor cursor = getDataBase().rawQuery(
//                "SELECT idForm FROM formItem WHERE _idFormItem=?", new String[]{idFormItem + ""});
//        while (cursor.moveToNext()) {
//            id = cursor.getInt(cursor.getColumnIndex("idForm"));
//        }
//        return id;
//    }

    public int buscarMaxId() {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery(
                "SELECT MAX(_idFormItem) as id from formItem", null);
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();

        return id;
    }

    public int status(int idFormItem) {
        int status = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT status FROM formItem WHERE _idFormItem = ?", new String[]{idFormItem + ""});

        while (cursor.moveToNext()) {
            status = cursor.getInt(cursor.getColumnIndex("status"));
        }

        return status;
    }

}
