package br.com.pdasolucoes.checklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.renderer.scatter.ChevronUpShapeRenderer;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.Agenda;
import br.com.pdasolucoes.checklist.model.Resposta;
import br.com.pdasolucoes.checklist.model.Todo;

/**
 * Created by PDA on 20/12/2016.
 */

public class TodoDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public TodoDao(Context context) {
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

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public long incluir(Todo t) {

        ContentValues values = new ContentValues();
        values.put("justificativa", t.getJustificativa());
        values.put("acao", t.getAcao());
        values.put("status", t.getStatus() + 1);
        values.put("responsavel", t.getResponsavel());
        values.put("prazo", t.getPrazo());
        values.put("data", sdf.format(t.getDataLimite()));
        values.put("follow", t.getFollowup());
        values.put("idPergunta", t.getIdPergunta());
        values.put("idFormItem", t.getIdFormItem());
        values.put("idResposta", t.getIdResposta());

        return getDataBase().insert("todo", null, values);

    }


    public List<Todo> listarTodo(int idFormItem) {
        List<Todo> lista = new ArrayList<Todo>();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM todo t, formItem f WHERE t.idFormItem = ? and t.idFormItem = f._idFormItem", new String[]{idFormItem + ""});
        while (cursor.moveToNext()) {

            Todo todo = new Todo();
            todo.setIdTodo(cursor.getInt(cursor.getColumnIndex("_idTodo")));
            todo.setJustificativa(cursor.getString(cursor.getColumnIndex("justificativa")));
            todo.setAcao(cursor.getString(cursor.getColumnIndex("acao")));
            todo.setResponsavel(cursor.getString(cursor.getColumnIndex("responsavel")));
            todo.setPrazo(cursor.getInt(cursor.getColumnIndex("prazo")));
            todo.setFollowup(cursor.getString(cursor.getColumnIndex("follow")));

            try {
                todo.setDataLimite(sdf.parse(cursor.getString(cursor.getColumnIndex("data"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            todo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            todo.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
            todo.setIdResposta(cursor.getInt(cursor.getColumnIndex("idResposta")));
            todo.setIdForm(cursor.getInt(cursor.getColumnIndex("idForm")));
            lista.add(todo);
        }
        return lista;
    }


    public Todo buscarMaior() {
        Todo todo = new Todo();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM todo WHERE _idTodo = (SELECT MAX(_idTodo) FROM todo)", null);

        while (cursor.moveToNext()) {

            todo.setIdTodo(cursor.getInt(cursor.getColumnIndex("_idTodo")));
            todo.setJustificativa(cursor.getString(cursor.getColumnIndex("justificativa")));
            todo.setAcao(cursor.getString(cursor.getColumnIndex("acao")));
            todo.setResponsavel(cursor.getString(cursor.getColumnIndex("responsavel")));
            todo.setPrazo(cursor.getInt(cursor.getColumnIndex("prazo")));
            todo.setFollowup(cursor.getString(cursor.getColumnIndex("follow")));
            try {
                todo.setDataLimite(sdf.parse(cursor.getString(cursor.getColumnIndex("data"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            todo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            todo.setIdPergunta(cursor.getInt(cursor.getColumnIndex("idPergunta")));
            todo.setIdFormItem(cursor.getInt(cursor.getColumnIndex("idFormItem")));
        }

        return todo;
    }

    public int ultimoId() {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT MAX(_idTodo) as maxTodo FROM todo", null);

        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("maxTodo"));
        }
        return id;
    }

    public boolean existeId(int id) {
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM todo WHERE _idTodo = ?", new String[]{id + ""});

        while (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public int contadorTodo(int idFormItem) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(_idTodo) as cnt FROM todo WHERE idFormItem = ?", new String[]{idFormItem + ""});

        while (cursor.moveToNext()) {
            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
        }

        return cnt;
    }

    public int contadorTodoPergunta(int idFormItem, int idPergunta) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(_idTodo) as cnt FROM todo WHERE idFormItem = ? and idPergunta = ?", new String[]{idFormItem + "", idPergunta + ""});

        while (cursor.moveToNext()) {
            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
        }

        return cnt;
    }

    public int contadorRespostaFaltaTodo(int idUsuario) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT count(*) as cnt FROM(SELECT r._idResposta, r.txtResposta, r.idPergunta,r.idFormItem,r.idOpcao,r.todo" +
                " FROM resposta r,formItem f where r.todo=1 and f._idFormItem = r.idFormItem and f.idUsuario = ? and idPergunta NOT IN(SELECT idPergunta FROM TODO t, formItem f" +
                " WHERE t.idFormItem = f._idFormItem and f.idUsuario = ?)) X", new String[]{idUsuario + "", idUsuario + ""});

        try {
            while (cursor.moveToNext()) {
                cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
            }
        } finally {
            cursor.close();
        }

        return cnt;
    }

    public int acaoDC(int idFormItem) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as cnt FROM(SELECT r._idResposta, r.txtResposta, r.idPergunta,r.idFormItem,r.idOpcao,r.todo" +
                " FROM resposta r where r.todo=1 and r.idFormItem = ? and idPergunta IN(SELECT idPergunta FROM todo)) X", new String[]{idFormItem + ""});

        while (cursor.moveToNext()) {
            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
        }
        return cnt;
    }

    public int acaoFC(int idFormItem, int idUsuario) {
        int cnt = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT COUNT(*) as cnt FROM(SELECT r._idResposta, r.txtResposta, r.idPergunta,r.idFormItem,r.idOpcao,r.todo" +
                " FROM resposta r where r.todo=1 and r.idFormItem = ? and idPergunta NOT IN(SELECT idPergunta FROM TODO t, formItem f WHERE t.idFormItem = f._idFormItem and f.idUsuario = ?)) X", new String[]{idFormItem + "", idUsuario + ""});

        while (cursor.moveToNext()) {
            cnt = cursor.getInt(cursor.getColumnIndex("cnt"));
        }
        return cnt;
    }
}
