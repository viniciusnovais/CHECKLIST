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
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public static boolean error = false;

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
        for (Form f : lista) {
            if (existeForm(f.getIdForm()) == false) {
                values.put("_idForm", f.getIdForm());
                values.put("nomeForm", f.getNomeFom());
                values.put("dataForm", f.getDataForm());
                values.put("nomeLoja", f.getNomeLoja());
                values.put("hora", f.getHora());
                values.put("status", f.getStatus());
                values.put("alterado", f.getAlterado());

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
            form.setHora(cursor.getString(cursor.getColumnIndex("hora")));
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

    public boolean existeForm(int id) {
        boolean existe = false;
        Cursor cursor = getDataBase().rawQuery("SELECT _idForm FROM form where _idForm = ?", new String[]{id + ""});
        while (cursor.moveToNext()) {
            existe = true;
        }
        return existe;
    }

    public void deleteForm(List<Integer> lista) {

        String args = TextUtils.join(",", lista);
        getDataBase().execSQL(String.format("DELETE FROM form WHERE _idForm NOT IN (%s)", args));
    }

    public void deletar() {
        getDataBase().execSQL("DELETE FROM form");
    }

    public int contarForm() {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as total FROM form", null);
        while (cursor.moveToNext()) {
            cnt = cursor.getInt(cursor.getColumnIndex("total"));
        }
        return cnt;
    }

    private static String URL = "http://179.184.159.52/wsforms/wsform.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/GetForms";

    private static String NAMESPACE = "http://tempuri.org/";
    private static String SOAP_ACTION2 = "http://tempuri.org/setFormAlterado";

    private static String NAMESPACE2 = "http://tempuri.org/";

    public List<Form> listarForms(int idUsuario) {
        List<Form> lista = new ArrayList<>();

        SoapObject response;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetForms");
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        // Set Username
        unamePI.setName("idUsuario");
        // Set Value
        unamePI.setValue(idUsuario);
        // Set dataType
        unamePI.setType(String.class);

        request.addProperty(unamePI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // Get the response
            response = (SoapObject) envelope.getResponse();

            SoapObject itemResponse = new SoapObject();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
            for (int j = 0; j < response.getPropertyCount(); j++) {
                itemResponse = (SoapObject) response.getProperty(j);

                Form f = new Form();
                //if (Integer.parseInt(itemResponse.getProperty("alterado").toString()) == 1) {
                //delete resposta
                //deleta item
                //proprio formulario
                //delete(id)
                //insere form
                f.setIdForm(Integer.parseInt(itemResponse.getProperty("id").toString()));
                f.setNomeFom(itemResponse.getProperty("nomeForm").toString());
                f.setDataForm(sdf.format(new Date()));
                f.setHora(sdfHora.format(new Date()));
                f.setNomeLoja(itemResponse.getProperty("descricao").toString());
                f.setAlterado(Integer.parseInt(itemResponse.getProperty("alterado").toString()));
                f.setStatus(0);
                //}

                //delete quem não existe

                lista.add(f);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        //Return booleam to calling object
        return lista;
    }

    public void setFormAlterado(int idForm) {
        // Create request
        SoapObject request = new SoapObject(NAMESPACE2, "setFormAlterado");
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        // Set Username
        unamePI.setName("id");
        // Set Value
        unamePI.setValue(idForm);
        // Set dataType
        unamePI.setType(String.class);

        request.addProperty(unamePI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION2, envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
