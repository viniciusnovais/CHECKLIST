package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.OpcaoResposta;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Resposta;
import br.com.pdasolucoes.checklist.util.WebService;

/**
 * Created by PDA on 09/02/2017.
 */

public class OpcaoRespostaDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public OpcaoRespostaDao(Context context) {
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

    public void incluir(List<OpcaoResposta> lista) {
        ContentValues values = new ContentValues();
        for (OpcaoResposta or : lista) {
            if (!adicionado(or.getIdOpcao(), or.getIdPergunta())) {
                values.put("_idOpcao", or.getIdOpcao());
                values.put("opcao", or.getOpcao());
                values.put("idPergunta", or.getIdPergunta());
                values.put("percentual", or.getPercentual());
                values.put("maior", or.getMaior());
                values.put("menor", or.getMenor());
                values.put("valor", or.getValor());
                values.put("todo", or.getToDo());
                values.put("horaMaior", or.getHoraMaior());
                values.put("horaMenor", or.getHoraMenor());
                values.put("dataMaior", or.getDataMaior());
                values.put("dataMenor", or.getDataMenor());
                //values.put("valorDiferente", or.getValorDiferente());

                getDataBase().insert("opcaoQuestao", null, values);
            }
        }
    }

    public boolean adicionado(int idOpcao, int idPergunta) {
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM opcaoQuestao where _idOpcao = ? and idPergunta = ?", new String[]{idOpcao + "", idPergunta + ""});
        while (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public void deletar() {
        getDataBase().execSQL("DELETE FROM opcaoQuestao");
    }

    public List<OpcaoResposta> listar(int id) {
        List<OpcaoResposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM opcaoQuestao where idPergunta=?", new String[]{id + ""});

        try {
            while (cursor.moveToNext()) {
                OpcaoResposta or = new OpcaoResposta();

                or.setIdOpcao(cursor.getInt(cursor.getColumnIndex("_idOpcao")));
                or.setOpcao(cursor.getString(cursor.getColumnIndex("opcao")));
                or.setValor(cursor.getFloat(cursor.getColumnIndex("valor")));
                or.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
                or.setPercentual(cursor.getFloat(cursor.getColumnIndex("percentual")));
                or.setMaior(cursor.getFloat(cursor.getColumnIndex("maior")));
                or.setToDo(cursor.getInt(cursor.getColumnIndex("todo")));
                or.setMenor(cursor.getFloat(cursor.getColumnIndex("menor")));
                //or.setValorDiferente(cursor.getFloat(cursor.getColumnIndex("valorDiferente")));
                or.setHoraMaior(cursor.getString(cursor.getColumnIndex("horaMaior")));
                or.setHoraMenor(cursor.getString(cursor.getColumnIndex("horaMenor")));
                or.setDataMenor(cursor.getString(cursor.getColumnIndex("dataMenor")));
                or.setDataMaior(cursor.getString(cursor.getColumnIndex("dataMenor")));

                lista.add(or);
            }

        } finally {
            cursor.close();
        }
        return lista;
    }


    public int buscarTodo(int idOpcao) {
        int todo = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT todo FROM opcaoQuestao WHERE _idOpcao = ?", new String[]{idOpcao + ""});
        {

            while (cursor.moveToNext()) {
                todo = cursor.getInt(cursor.getColumnIndex("todo"));
            }
        }
        return todo;
    }

    public List<OpcaoResposta> listarTodasOpcoesForm(int idFormItem, int idPergunta) {
        List<OpcaoResposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT MAX(valor) as maxValor FROM opcaoQuestao op, formItem f WHERE op.idPergunta = ? and f._idFormItem = ?", new String[]{idPergunta + "", idFormItem + ""});
        try {
            while (cursor.moveToNext()) {
                OpcaoResposta or = new OpcaoResposta();

                or.setValor(cursor.getFloat(cursor.getColumnIndex("maxValor")));

                lista.add(or);
            }
        } finally {
            cursor.close();
            //getDataBase().close();
        }
        return lista;
    }

    public List<OpcaoResposta> listarTodasOpcoesFormMultiplo(int idFormItem, int idPergunta) {
        List<OpcaoResposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT SUM(valor) as somaValor FROM opcaoQuestao op, formItem f WHERE op.idPergunta = ? and f._idFormItem = ?", new String[]{idPergunta + "", idFormItem + ""});
        try {
            while (cursor.moveToNext()) {
                OpcaoResposta or = new OpcaoResposta();

                or.setValor(cursor.getFloat(cursor.getColumnIndex("somaValor")));

                lista.add(or);
            }
        } finally {
            cursor.close();
            //getDataBase().close();
        }
        return lista;
    }

    public List<OpcaoResposta> listarVolta(int idFormItem, int idPergunta) {
        List<OpcaoResposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT r.txtResposta,o._idOpcao,opcao, o.idPergunta, r.valor FROM opcaoQuestao o " +
                "LEFT JOIN resposta r ON o.idPergunta=r.idPergunta and o._idOpcao=r.idOpcao where r.idFormItem=? and r.idPergunta= ? " +
                "GROUP BY txtResposta,o._idOpcao,opcao, o.idPergunta", new String[]{idFormItem + "", idPergunta + ""});
        try {
            while (cursor.moveToNext()) {
                OpcaoResposta or = new OpcaoResposta();

                or.setIdOpcao(cursor.getInt(cursor.getColumnIndex("_idOpcao")));
                or.setOpcao(cursor.getString(cursor.getColumnIndex("opcao")));
                or.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
                or.setTxtResposta(cursor.getString(cursor.getColumnIndex("txtResposta")));
                or.setValor(cursor.getFloat(cursor.getColumnIndex("valor")));

                lista.add(or);
            }
        } finally {
            cursor.close();
        }
        return lista;
    }

    public List<OpcaoResposta> listarVoltaParaListaItem(int idFormItem, int idPergunta) {
        List<OpcaoResposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("select * from (SELECT o._idOpcao,o.opcao, o.idPergunta, r.txtresposta resp, r.valor v, o.maior, o.menor,o.todo FROM opcaoQuestao o" +
                " inner join resposta r on o.idPergunta=r.idPergunta and o._idOpcao = r.idOpcao" +
                " where  o.idPergunta = ? and r.idFormItem = ?" +
                " UNION ALL" +
                " SELECT o._idOpcao,o.opcao, o.idPergunta, '' resp, '' v, o.maior, o.menor,o.todo FROM opcaoQuestao o where  o.idPergunta = ?" +
                " AND o._idOpcao NOT IN (SELECT idOpcao FROM resposta r WHERE r.idFormItem = ? and r.idPergunta = ?)) order by _idOpcao", new String[]{idPergunta + "", idFormItem + "", idPergunta + "", idFormItem + "", idPergunta + ""});
        try {
            while (cursor.moveToNext()) {
                OpcaoResposta or = new OpcaoResposta();

                or.setIdOpcao(cursor.getInt(cursor.getColumnIndex("_idOpcao")));
                or.setOpcao(cursor.getString(cursor.getColumnIndex("opcao")));
                or.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
                or.setTxtResposta(cursor.getString(cursor.getColumnIndex("resp")));
                or.setValor(cursor.getFloat(cursor.getColumnIndex("v")));
                //or.setValorDiferente(cursor.getFloat(cursor.getColumnIndex("valorDiferente")));
                or.setMaior(cursor.getFloat(cursor.getColumnIndex("maior")));
                or.setMenor(cursor.getFloat(cursor.getColumnIndex("menor")));
                or.setToDo(cursor.getInt(cursor.getColumnIndex("todo")));
                lista.add(or);
            }
        } finally {
            cursor.close();
        }
        return lista;
    }

    private static String URL = "http://179.184.159.52/wsforms/wsopcao.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/GetOpcaoPorPadraoResposta";

    private static String NAMESPACE = "http://tempuri.org/";

    public List<OpcaoResposta> listarOpcaoResposta(int idPadrao, int idPergunta) {
        List<OpcaoResposta> lista = new ArrayList<>();

        SoapObject response = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetOpcaoPorPadraoResposta");
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        // Set Username
        unamePI.setName("idPadrao");
        // Set Value
        unamePI.setValue(idPadrao);
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
                OpcaoResposta op = new OpcaoResposta();

                op.setIdOpcao(Integer.parseInt(itemResponse.getProperty("id").toString()));
                op.setOpcao(itemResponse.getProperty("opcao").toString());
                op.setIdPergunta(idPergunta);
                op.setPercentual(Float.parseFloat(itemResponse.getProperty("percentual").toString()));
                op.setMaior(Float.parseFloat(itemResponse.getProperty("maior").toString()));
                op.setMenor(Float.parseFloat(itemResponse.getProperty("menor").toString()));
                op.setValor(Float.parseFloat(itemResponse.getProperty("valor").toString()));
                op.setToDo(Integer.parseInt(itemResponse.getProperty("toDo").toString()));
                op.setValorDiferente(Float.parseFloat(itemResponse.getProperty("valorDiferente").toString()));
                op.setHoraMaior(itemResponse.getProperty("horaMaior").toString());
                op.setHoraMenor(itemResponse.getProperty("horaMenor").toString());
                op.setDataMaior(itemResponse.getProperty("dataMaior").toString());
                op.setDataMenor(itemResponse.getProperty("dataMenor").toString());

                lista.add(op);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Return booleam to calling object
        return lista;
    }


}
