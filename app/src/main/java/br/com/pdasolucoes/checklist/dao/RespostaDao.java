package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.ObbInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Resposta;
import br.com.pdasolucoes.checklist.model.Todo;

/**
 * Created by PDA on 06/01/2017.
 */

public class RespostaDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public RespostaDao(Context context) {
        helper = new DataBaseHelper(context);
    }

    public SQLiteDatabase getDataBase() {
        if (database == null) {
            database = helper.getWritableDatabase();
        }

        return database;
    }

//    public void close() {
//        helper.close();
//        if (database != null && database.isOpen()) {
//            database.close();
//        }
//    }

    public long incluirResposta(Resposta resposta) {
        ContentValues values = new ContentValues();

        values.put("txtResposta", resposta.getTxtResposta());
        values.put("idPergunta", resposta.getIdPergunta());
        values.put("idFormItem", resposta.getIdFormItem());
        values.put("idOpcao", resposta.getIdOpcao());
        values.put("valor", resposta.getValor());
        values.put("todo", 0);

        return getDataBase().insert("resposta", null, values);
    }

    public long incluirRespostaLista(Resposta resposta) {
        ContentValues values = new ContentValues();

        values.put("txtResposta", resposta.getTxtResposta());
        values.put("idPergunta", resposta.getIdPergunta());
        values.put("idFormItem", resposta.getIdFormItem());
        values.put("idOpcao", resposta.getIdOpcao());
        values.put("valor", resposta.getValor());

        return getDataBase().insert("resposta", null, values);
    }

    public void deleteOpcaoResposta(int idResposta) {
        getDataBase().delete("resposta", "_idResposta" + "=" + idResposta, null);
    }

    public void alterarResposta(Resposta resposta) {
        ContentValues values = new ContentValues();
        values.put("txtResposta", resposta.getTxtResposta());
        values.put("todo", resposta.getTodo());
        values.put("valor", resposta.getValor());
        values.put("idOpcao", resposta.getIdOpcao());

        getDataBase().update("resposta", values, "_idResposta=?", new String[]{resposta.getIdResposta() + ""});
    }

    //tive que criar um exclusivo pra lista, pra resolver o problema do todo, nele eu faço a alteração diferente
    public void alterarRespostaLista(Resposta resposta) {
        ContentValues values = new ContentValues();
        values.put("txtResposta", resposta.getTxtResposta());
        values.put("valor", resposta.getValor());
        values.put("idOpcao", resposta.getIdOpcao());

        getDataBase().update("resposta", values, "_idResposta=?", new String[]{resposta.getIdResposta() + ""});
    }

    public void isTodo(int idResposta) {
        ContentValues values = new ContentValues();
        values.put("todo", 1);
        getDataBase().update("resposta", values, "_idResposta = " + idResposta, null);
    }

    public void isNotTodo(int idResposta) {
        ContentValues values = new ContentValues();
        values.put("todo", 0);
        getDataBase().update("resposta", values, "_idResposta = " + idResposta, null);
    }

    public List<Resposta> Resposta(int idResposta) {

        List<Resposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM resposta WHERE _idResposta = ? ", new String[]{idResposta + ""});
        try {
            while (cursor.moveToNext()) {
                Resposta r = new Resposta();
                r.setIdResposta(cursor.getInt(cursor.getColumnIndex("_idResposta")));
                r.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
                r.setTxtResposta(cursor.getString(cursor.getColumnIndex("txtResposta")));
                r.setIdFormItem(cursor.getInt(cursor.getColumnIndex("idFormItem")));
                r.setIdOpcao(cursor.getInt(cursor.getColumnIndex("idOpcao")));
                r.setTodo(cursor.getInt(cursor.getColumnIndex("todo")));
                r.setValor(cursor.getFloat(cursor.getColumnIndex("valor")));
                lista.add(r);
            }
        } finally {
            cursor.close();
        }


        return lista;
    }

    public List<Resposta> respotaPeloIdPergunta(int idPergunta, int idFormItem) {

        List<Resposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM resposta WHERE idPergunta = ? and idFormItem = ?", new String[]{idPergunta + "", idFormItem + ""});
        try {
            while (cursor.moveToNext()) {
                Resposta r = new Resposta();
                r.setIdResposta(cursor.getInt(cursor.getColumnIndex("_idResposta")));
                r.setTxtResposta(cursor.getString(cursor.getColumnIndex("txtResposta")));
                r.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
                r.setIdFormItem(cursor.getInt(cursor.getColumnIndex("idFormItem")));
                r.setIdOpcao(cursor.getInt(cursor.getColumnIndex("idOpcao")));
                r.setTodo(cursor.getInt(cursor.getColumnIndex("todo")));
                r.setValor(cursor.getFloat(cursor.getColumnIndex("valor")));

                lista.add(r);
            }
        } finally {
            cursor.close();
        }


        return lista;
    }

    public List<Resposta> listarResposta(int idFormItem, int idUsuario) {

        List<Resposta> lista = new ArrayList<>();

        Cursor cursor = getDataBase().rawQuery("SELECT r._idResposta,r.txtResposta, r.idPergunta, f.idForm, r.idOpcao, r.todo, r.valor " +
                "FROM resposta r, formItem f WHERE r.idFormItem = ? and r.idFormItem = f._idFormItem and f.idUsuario = ?", new String[]{idFormItem + "", idUsuario + ""});
        try {
            while (cursor.moveToNext()) {
                Resposta r = new Resposta();
                r.setIdResposta(cursor.getInt(cursor.getColumnIndex("_idResposta")));
                r.setTxtResposta(cursor.getString(cursor.getColumnIndex("txtResposta")));
                r.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
                r.setIdFormItem(cursor.getInt(cursor.getColumnIndex("idForm")));
                r.setIdOpcao(cursor.getInt(cursor.getColumnIndex("idOpcao")));
                r.setTodo(cursor.getInt(cursor.getColumnIndex("todo")));
                r.setValor(cursor.getFloat(cursor.getColumnIndex("valor")));
                r.setIdUsuario(idUsuario);
                lista.add(r);
            }
        } finally {
            cursor.close();
        }


        return lista;
    }


    public int buscarIdResposta(int idFormItem, int idPergunta) {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT _idResposta FROM resposta WHERE idFormItem = ? and idPergunta = ?", new String[]{idFormItem + "", idPergunta + ""});

        //try {
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("_idResposta"));
        }
        // } finally {
        cursor.close();
        //}
        return id;
    }

    public int buscarIdUltimaResposta() {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT MAX(_idResposta) as maxId FROM resposta", null);

        //try {
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("maxId"));
        }
        // } finally {
        cursor.close();
        //}
        return id;
    }

    public int buscarIdResposta(int idFormItem, int idPergunta, int idOpcao) {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM resposta WHERE idFormItem = ? and idPergunta = ? and idOpcao = ?", new String[]{idFormItem + "", idPergunta + "", idOpcao + ""});

        try {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("_idResposta"));
            }
        } finally {
            cursor.close();
        }
        return id;
    }

    public int contadorResposta(int idFormItem) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as respondidas FROM(" +
                " SELECT idPergunta as respondidas FROM resposta WHERE idFormItem = ? and" +
                " (txtResposta <> '' and txtResposta IS NOT NULL) GROUP BY idPergunta) X", new String[]{idFormItem + ""});

        try {
            while (cursor.moveToNext()) {
                cnt = cursor.getInt(cursor.getColumnIndex("respondidas"));
            }
        } finally {
            cursor.close();
        }
        return cnt;
    }

    public int respostaNaoConforme(int idFormItem) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as nao_conf FROM(" +
                " SELECT idPergunta FROM resposta WHERE idFormItem = ?" +
                " and (txtResposta <> '' and txtResposta IS NOT NULL)  GROUP BY idPergunta HAVING MAX(todo) > 0) X", new String[]{idFormItem + ""});

        try {
            while (cursor.moveToNext()) {
                cnt = cursor.getInt(cursor.getColumnIndex("nao_conf"));
            }

        } finally {
            cursor.close();
        }

        return cnt;
    }

    public int respostaConforme(int idFormItem) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as conf FROM(" +
                " SELECT idPergunta FROM resposta WHERE idFormItem = ?" +
                " and (txtResposta <> '' and txtResposta IS NOT NULL)  GROUP BY idPergunta HAVING MAX(todo)=0) X", new String[]{idFormItem + ""});

        try {
            while (cursor.moveToNext()) {
                cnt = cursor.getInt(cursor.getColumnIndex("conf"));
            }

        } finally {
            cursor.close();
        }

        return cnt;
    }

    public List<Resposta> listaFaltaTodo(int idUsuario) {
        List<Resposta> lista = new ArrayList<>();
        Cursor cursor = getDataBase().rawQuery("SELECT fi.idUsuario, r.idFormItem,f._idForm,f.nomeForm,p._idPergunta,p.txtPergunta, p.opcaoQuestaoTodo, r.txtResposta, r._idResposta" +
                " FROM resposta r, pergunta p, form f, formItem fi" +
                " WHERE r.todo=1 and p._idPergunta = r.idPergunta and fi.idForm = f._idForm and r.idFormItem = fi._idFormItem and p._idPergunta" +
                " NOT IN(SELECT idPergunta FROM TODO t, formItem f WHERE t.idFormItem = f._idFormItem and f.idUsuario = ?) and fi.idUsuario = ?", new String[]{idUsuario + "", idUsuario + ""});

        while (cursor.moveToNext()) {
            Form f = new Form();
            f.setIdForm(cursor.getInt(cursor.getColumnIndex("_idForm")));
            f.setNomeFom(cursor.getString(cursor.getColumnIndex("nomeForm")));
            Pergunta p = new Pergunta();
            p.setIdPergunta(cursor.getInt(cursor.getColumnIndex("_idPergunta")));
            p.setTxtPergunta(cursor.getString(cursor.getColumnIndex("txtPergunta")));
            p.setOpcaoQuestaoTodo(cursor.getString(cursor.getColumnIndex("opcaoQuestaoTodo")));
            p.setIdForm(f);

            Resposta r = new Resposta();
            r.setIdResposta(cursor.getInt(cursor.getColumnIndex("_idResposta")));
            r.setIdFormItem(cursor.getInt(cursor.getColumnIndex("idFormItem")));
            r.setIdPergunta(cursor.getInt(cursor.getColumnIndex("_idPergunta")));
            r.setTxtResposta(cursor.getString(cursor.getColumnIndex("txtResposta")));


            lista.add(r);
        }

        return lista;
    }

}
