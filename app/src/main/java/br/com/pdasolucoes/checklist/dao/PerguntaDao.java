package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.util.WebService;

/**
 * Created by PDA on 01/12/2016.
 */

public class PerguntaDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;
    public static boolean error = false;

    public PerguntaDao(Context context) {
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

    public void incluir(List<Pergunta> lista) {
        ContentValues values = new ContentValues();
        for (Pergunta p : lista) {
            if (existePergunta(p.getIdPergunta()) == false) {
                values.put("_idPergunta", p.getIdPergunta());
                values.put("txtPergunta", p.getTxtPergunta());
                values.put("opcaoQuestaoTodo", p.getOpcaoQuestaoTodo());
                values.put("tipoPergunta", p.getTipoPergunta());
                values.put("peso", p.getPeso());
                values.put("idForm", p.getIdForm().getIdForm());
                values.put("alterado", p.getAlterado());
                values.put("idSetor", p.getIdSetor());
                values.put("idPadraoResposta", p.getIdPadraoResposta());

                getDataBase().insert("pergunta", null, values);
            }
        }
    }

    public boolean existePergunta(int id) {
        boolean existe = false;
        Cursor cursor = getDataBase().rawQuery("SELECT _idPergunta FROM pergunta where _idPergunta = ?", new String[]{id + ""});
        while (cursor.moveToNext()) {
            existe = true;
        }
        return existe;
    }

    public int idPadrao(int idPergunta) {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT idPadraoResposta FROM pergunta WHERE _idPergunta = ? ", new String[]{idPergunta + ""});

        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("idPadraoResposta"));
        }

        return id;
    }

    public void deletar(){
        getDataBase().execSQL("DELETE FROM pergunta");
    }

    public List<Pergunta> listar(int idSetor) {


        List<Pergunta> lista = new ArrayList<>();

        Cursor cursor = getDataBase().rawQuery("SELECT * FROM pergunta where idSetor=?", new String[]{idSetor + ""});
        try {

            while (cursor.moveToNext()) {
                Pergunta p = new Pergunta();
                p.setIdPergunta(cursor.getInt(cursor.getColumnIndex("_idPergunta")));
                p.setTxtPergunta(cursor.getString(cursor.getColumnIndex("txtPergunta")));
                p.setOpcaoQuestaoTodo(cursor.getString(cursor.getColumnIndex("opcaoQuestaoTodo")));
                p.setTipoPergunta(cursor.getInt(cursor.getColumnIndex("tipoPergunta")));
                p.setIdSetor(cursor.getInt(cursor.getColumnIndex("idSetor")));
                p.setPeso(cursor.getFloat(cursor.getColumnIndex("peso")));
                p.setIdPadraoResposta(cursor.getInt(cursor.getColumnIndex("idPadraoResposta")));
                Form f = new Form();
                f.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
                p.setIdForm(f);
                lista.add(p);
            }


        } finally {
            cursor.close();
        }

        return lista;

    }

    public List<Pergunta> listarNaoRespondidas(int idSetor, int idFormItem) {
        List<Pergunta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM PERGUNTA P where p.idSetor = ? and p._idPergunta" +
                " not in (SELECT r.idPergunta FROM resposta r WHERE r.idFormItem = ?)", new String[]{idSetor + "", idFormItem + ""});
        while (cursor.moveToNext()) {
            Pergunta p = new Pergunta();
            p.setIdPergunta(cursor.getInt(cursor.getColumnIndex("_idPergunta")));
            p.setTxtPergunta(cursor.getString(cursor.getColumnIndex("txtPergunta")));
            p.setOpcaoQuestaoTodo(cursor.getString(cursor.getColumnIndex("opcaoQuestaoTodo")));
            p.setTipoPergunta(cursor.getInt(cursor.getColumnIndex("tipoPergunta")));
            p.setIdSetor(cursor.getInt(cursor.getColumnIndex("idSetor")));
            p.setPeso(cursor.getFloat(cursor.getColumnIndex("peso")));
            Form f = new Form();
            f.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
            p.setIdForm(f);
            lista.add(p);
        }
        return lista;
    }


    public List<Pergunta> buscaNomePerguntaeForm(int id) {
        List<Pergunta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT p.txtPergunta, f.nomeForm FROM pergunta p, form f where p._idPergunta=? and p.idForm = f._idForm", new String[]{id + ""});
        while (cursor.moveToNext()) {
            Pergunta p = new Pergunta();
            p.setTxtPergunta(cursor.getString(cursor.getColumnIndex("txtPergunta")));
            Form f = new Form();
            f.setNomeFom(cursor.getString(cursor.getColumnIndex("nomeForm")));
            p.setIdForm(f);
            lista.add(p);
        }
        return lista;
    }

    public int QtdePergunta(int idSetor) {
        int qtde = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as cnt FROM pergunta WHERE idSetor = ? ", new String[]{idSetor + ""});
        try {

            while (cursor.moveToNext()) {
                qtde = cursor.getInt(cursor.getColumnIndex("cnt"));
            }
        } finally {
            cursor.close();
        }

        return qtde;
    }

//    public List<Pergunta> getPerguntaWS(int idForm) {
//        String url = WebService.URL_SHEETS + "ebcb036a601a/search?idForm=" + idForm;
//        String resposta = WebService.makeRequest(url);
//        JSONObject jsonObject;
//        List<Pergunta> lista = new ArrayList<>();
//
//
//        if (resposta == null) {
//
//        } else {
//
//            try {
//                JSONArray json = new JSONArray(resposta);
//                for (int i = 0; i < json.length(); i++) {
//                    jsonObject = json.getJSONObject(i);
//
//                    Pergunta p = new Pergunta();
//                    p.setIdPergunta(jsonObject.getInt("idPergunta"));
//                    p.setTxtPergunta(jsonObject.getString("pergunta"));
//                    p.setTipoPergunta(jsonObject.getInt("tipoPergunta"));
//                    p.setOpcaoQuestaoTodo(jsonObject.getString("opcaoQuestaoTodo"));
//                    Form f = new Form();
//                    f.setIdForm(jsonObject.getInt("idForm"));
//                    p.setIdForm(f);
//
//                    lista.add(p);
//
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return lista;
//    }

    private static String URL = "http://179.184.159.52/wsforms/wspergunta.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/GetPerguntasForm";

    private static String NAMESPACE = "http://tempuri.org/";

    public List<Pergunta> listarPerguntas(int idForm) {
        List<Pergunta> lista = new ArrayList<>();

        SoapObject response = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetPerguntasForm");
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
                Pergunta p = new Pergunta();

                p.setIdPergunta(Integer.parseInt(itemResponse.getProperty("id").toString()));
                p.setTxtPergunta(itemResponse.getProperty("txtPergunta").toString());
                p.setTipoPergunta(Integer.parseInt(itemResponse.getProperty("tipoPergunta").toString()));
                p.setOpcaoQuestaoTodo(itemResponse.getProperty("idOpcaoQuestaoToDo").toString());
                p.setPeso(Float.parseFloat(itemResponse.getProperty("peso").toString()));
                p.setIdSetor(Integer.parseInt(itemResponse.getProperty("idSetor").toString()));
                p.setIdPadraoResposta(Integer.parseInt(itemResponse.getProperty("idPadraoResposta").toString()));
                Form f = new Form();
                f.setIdForm(Integer.parseInt(itemResponse.getProperty("idForm").toString()));
                f.setNomeFom(itemResponse.getProperty("nomeForm").toString());
                p.setIdForm(f);

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Return booleam to calling object
        return lista;
    }

}
