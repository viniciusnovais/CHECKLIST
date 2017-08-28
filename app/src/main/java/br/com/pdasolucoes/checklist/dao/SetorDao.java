package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.Setor;

/**
 * Created by PDA on 21/07/2017.
 */

public class SetorDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;
    public static boolean error = false;

    public SetorDao(Context context) {
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


    public void incluir(List<Setor> lista) {

        ContentValues values = new ContentValues();
        for (Setor s : lista) {
            if (!existeSetor(s.getId())) {
                values.put("id", s.getId());
                values.put("nome", s.getNome());
                values.put("status", s.getStatus());
                values.put("idForm", s.getIdForm());
                getDataBase().insert("setor", null, values);
            }
        }
    }


    public List<Setor> listar(int idForm) {
        List<Setor> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT id,nome,status,idForm FROM setor WHERE idForm = ?", new String[]{idForm + ""});
        {
            while (cursor.moveToNext()) {
                Setor s = new Setor();
                s.setId(cursor.getInt(cursor.getColumnIndex("id")));
                s.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                s.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                s.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
                lista.add(s);
            }
        }
        return lista;
    }

    public List<Setor> listar() {
        List<Setor> lista = new ArrayList<>();

        Cursor cursor = getDataBase().rawQuery("SELECT id,nome,status,idForm FROM setor", null);
        {
            while (cursor.moveToNext()) {
                Setor s = new Setor();
                s.setId(cursor.getInt(cursor.getColumnIndex("id")));
                s.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                s.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                s.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
                lista.add(s);
            }
            return lista;
        }
    }


    public boolean existeSetor(int id) {
        Cursor cursor = getDataBase().rawQuery("SELECT id FROM setor WHERE id = ?", new String[]{id + ""});

        while (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    private static String URL = "http://179.184.159.52/wsforms/wssetor.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/GetSetorForms ";

    private static String NAMESPACE = "http://tempuri.org/";

    public List<Setor> listaWeb(int idForm) {

        List<Setor> lista = new ArrayList<>();

        SoapObject response = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetSetorForms");
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        // Set Username
        unamePI.setName("idForm");
        // Set Value
        unamePI.setValue(idForm);
        // Set dataType
        unamePI.setType(String.class);
        //Add the property to request object
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
            SoapObject itemResponse;
            for (int i = 0; i < response.getPropertyCount(); i++) {
                itemResponse = (SoapObject) response.getProperty(i);
                Setor s = new Setor();

                s.setId(Integer.parseInt(itemResponse.getProperty("id").toString()));
                s.setNome(itemResponse.getProperty("nome").toString());
                s.setStatus(Integer.parseInt(itemResponse.getProperty("status").toString()));
                s.setIdForm(Integer.parseInt(itemResponse.getProperty("idForm").toString()));
                lista.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Return booleam to calling object
        return lista;
    }
}
