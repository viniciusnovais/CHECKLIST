package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.StreamHandler;

import br.com.pdasolucoes.checklist.activities.AppointmentActivity;
import br.com.pdasolucoes.checklist.model.Agenda;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Resposta;
import br.com.pdasolucoes.checklist.util.WebService;

/**
 * Created by PDA on 06/12/2016.
 */

public class FormDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;
    public static boolean error=false;

    public FormDao(Context context) {
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

    public void incluir(List<Form> lista) {
        ContentValues values = new ContentValues();
        for(Form f: lista) {
            if (existeForm(f.getIdForm()) == false) {
                values.put("_idForm", f.getIdForm());
                values.put("nomeForm", f.getNomeFom());
                values.put("dataForm", f.getDataForm());
                values.put("nomeLoja",f.getNomeLoja());
                values.put("hora", f.getHora());
                values.put("status", f.getStatus());

                getDataBase().insert("form", null, values);
            }
        }
    }


    public List<Form> listar() {
        List<Form> lista = new ArrayList<Form>();
        Cursor cursor = getDataBase().rawQuery(
                "SELECT * FROM form", null);
        while (cursor.moveToNext()) {
            Form form = new Form();
            form.setIdForm(cursor.getInt(cursor.getColumnIndex("_idForm")));
            form.setNomeFom(cursor.getString(cursor.getColumnIndex("nomeForm")));
            form.setNomeLoja(cursor.getString(cursor.getColumnIndex("nomeLoja")));
            form.setDataForm(cursor.getString(cursor.getColumnIndex("dataForm")));
            form.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            form.setHora(cursor.getInt(cursor.getColumnIndex("hora")));
            lista.add(form);
        }
        return lista;
    }

    public List<String> listarForm() {
        List<String> lista = new ArrayList<String>();
        Cursor cursor = getDataBase().rawQuery(
                "SELECT nomeForm FROM form", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(cursor.getColumnIndex("nomeForm")));
        }
        return lista;
    }

    public boolean existeForm(int id){
        boolean existe=false;
        Cursor cursor = getDataBase().rawQuery("SELECT _idForm FROM form where _idForm = ?",new String[]{id+""});
        while (cursor.moveToNext()){
            existe=true;
        }
        return existe;
    }
    public void deleteForm(List<Integer> lista) {

        String args = TextUtils.join(",",lista);
        getDataBase().execSQL(String.format("DELETE FROM form where _idForm NOT IN (%s)",args));
    }

    public int contarForm(){
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as total FROM form",null);
        while (cursor.moveToNext()){
            cnt = cursor.getInt(cursor.getColumnIndex("total"));
        }
        return cnt;
    }

    public List<Form> getFormWS(){
        List<Form> l= new ArrayList<>();
        String url = WebService.URL;
        String resposta = WebService.makeRequest(url);
        if (resposta==null){
            error=true;
        }else {

            try {
                JSONArray array = new JSONArray(resposta);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);

                    Form f = new Form();
                    f.setIdForm(json.getInt("idForm"));
                    f.setNomeFom(json.getString("nomeForm"));
                    f.setDataForm(json.getString("dataForm"));
                    f.setNomeLoja(json.getString("nomeLoja"));
                    f.setStatus(json.getInt("status"));
                    f.setHora(json.getInt("hora"));

                    //incluir(f);
                    l.add(f);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return l;
    }
}
