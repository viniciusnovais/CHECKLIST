package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.OpcaoResposta;
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
            if (existeOpcaoQuestao(or.getIdOpcao()) == false) {
                values.put("_idOpcao", or.getIdOpcao());
                values.put("opcao", or.getOpcao());
                values.put("idPergunta", or.getIdPergunta());

                getDataBase().insert("opcaoQuestao", null, values);
            }
        }
    }

    public boolean existeOpcaoQuestao(int id) {
        boolean existe = false;
        Cursor cursor = getDataBase().rawQuery("SELECT _idOpcao FROM opcaoQuestao where _idOpcao = ?", new String[]{id + ""});
        while (cursor.moveToNext()) {
            existe = true;
        }
        return existe;
    }

    public List<OpcaoResposta> listar(int id) {
        List<OpcaoResposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM opcaoQuestao where idPergunta=?", new String[]{id + ""});

        try {
            while (cursor.moveToNext()) {
                OpcaoResposta or = new OpcaoResposta();

                or.setIdOpcao(cursor.getInt(cursor.getColumnIndex("_idOpcao")));
                or.setOpcao(cursor.getString(cursor.getColumnIndex("opcao")));
                or.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
                lista.add(or);
            }

        } finally {
            cursor.close();
        }
        return lista;
    }

    public List<OpcaoResposta> listarVolta(int idFormItem, int idPergunta) {
        List<OpcaoResposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT txtResposta,o._idOpcao,opcao, o.idPergunta FROM opcaoQuestao o " +
                "LEFT JOIN resposta r ON o.idPergunta=r.idPergunta and o._idOpcao=r.idOpcao where r.idFormItem=? and r.idPergunta= ? " +
                "GROUP BY txtResposta,o._idOpcao,opcao, o.idPergunta", new String[]{idFormItem + "", idPergunta + ""});
        try {
            while (cursor.moveToNext()) {
                OpcaoResposta or = new OpcaoResposta();

                or.setIdOpcao(cursor.getInt(cursor.getColumnIndex("_idOpcao")));
                or.setOpcao(cursor.getString(cursor.getColumnIndex("opcao")));
                or.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
                or.setTxtResposta(cursor.getString(cursor.getColumnIndex("txtResposta")));

                lista.add(or);
            }
        } finally {
            cursor.close();
            //getDataBase().close();
        }
        return lista;
    }

    public List<OpcaoResposta> getOpcaoQuestaoWS() {
        List<OpcaoResposta> lista = new ArrayList<>();

        String url = WebService.URL_SHEETS + "ac9082c2e0c4";
        String resposta = WebService.makeRequest(url);
        try {
            JSONArray array = new JSONArray(resposta);

            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                OpcaoResposta or = new OpcaoResposta();
                or.setIdOpcao(json.getInt("idOpcao"));
                or.setOpcao(json.getString("opcao"));
                or.setIdPergunta(json.getInt("idPergunta"));

                lista.add(or);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }


}
