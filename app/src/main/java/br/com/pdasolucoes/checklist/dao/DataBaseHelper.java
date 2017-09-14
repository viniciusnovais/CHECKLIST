package br.com.pdasolucoes.checklist.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PDA on 06/12/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "forms";
    private static final int VERSAO = 6;


    public DataBaseHelper(Context context) {

        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not exists form(_idForm INTEGER, nomeForm TEXT, dataForm TEXT," +
                "nomeLoja TEXT, hora TEXT, status INTEGER, alterado INTEGER)");

        db.execSQL("CREATE TABLE if not exists todo(_idTodo INTEGER PRIMARY KEY, justificativa TEXT, acao TEXT, status INTEGER, responsavel TEXT,prazo INTEGER, " +
                "data TEXT, follow TEXT, idPergunta INTEGER, idFormItem INTEGER, idResposta INTEGER)");

        db.execSQL("CREATE TABLE if not exists opcaoQuestao(_idOpcao INTEGER, opcao TEXT,idPergunta INTEGER, percentual REAL, maior REAL, menor REAL," +
                " valor REAL, horaMaior TEXT, horaMenor TEXT, dataMaior, dataMenor TEXT, todo INTEGER, valorDiferente REAL)");

        db.execSQL("CREATE TABLE if not exists setor(id INTEGER, nome TEXT, idForm INTEGER, status INTEGER)");

        db.execSQL("CREATE TABLE if not exists pergunta(_idPergunta INTEGER, txtPergunta TEXT, tipoPergunta INTEGER, opcaoQuestaoTodo TEXT, " +
                "idForm INTEGER, peso REAL, alterado INTEGER, idSetor INTEGER, idPadraoResposta INTEGER)");

        db.execSQL("CREATE TABLE if not exists resposta(_idResposta INTEGER PRIMARY KEY, " +
                "txtResposta TEXT,idPergunta INTEGER, idFormItem INTEGER, idOpcao INTEGER, todo INTEGER, valor REAL)");

        db.execSQL("CREATE TABLE if not exists formItem(_idFormItem INTEGER PRIMARY KEY AUTOINCREMENT,status INTEGER," +
                " sync INTEGER,idSetor INTEGER,horaInicio TEXT, horaFim TEXT, dataInicio TEXT, dataFim TEXT,idUsuario INTEGER, idForm INTEGER, FOREIGN KEY(idForm) REFERENCES form(_idForm), FOREIGN KEY(idSetor) REFERENCES setor(id))");

        db.execSQL("CREATE TABLE if not exists complementoResposta(_idComplemento INTEGER PRIMARY KEY, image BLOB, comentario TEXT, " +
                "idPergunta INTEGER, idFormItem INTEGER)");

        db.execSQL("CREATE TABLE if not exists complementoTodo(_idComplemento INTEGER PRIMARY KEY, image BLOB, comentario TEXT," +
                " idTodo INTEGER)");

        db.execSQL("CREATE TABLE if not exists usuario(id INTEGER, email TEXT, senha TEXT, nome TEXT)");

        db.execSQL("CREATE TABLE if not exists logo(image BLOB, idUsuario INTEGER, status INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            deleteDataBases(db);
            onCreate(db);
        }
    }


    public void deleteDataBases(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

        while (c.moveToNext()) {
            if (!c.getString(0).equals("sqlite_sequence")) {
                tables.add(c.getString(0));
            }
        }

        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }
    }
}
