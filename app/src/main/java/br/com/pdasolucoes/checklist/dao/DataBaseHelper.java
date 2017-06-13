package br.com.pdasolucoes.checklist.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by PDA on 06/12/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "forms";
    private static final int VERSAO = 1;


    public DataBaseHelper(Context context) {

        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not exists form(_idForm INTEGER, nomeForm TEXT, dataForm TEXT," +
                "nomeLoja TEXT, hora INTEGER, status INTEGER)");

        db.execSQL("CREATE TABLE if not exists todo(_idTodo INTEGER PRIMARY KEY, justificativa TEXT, acao TEXT, status INTEGER, responsavel TEXT,prazo INTEGER, " +
                "data TEXT, follow TEXT, idPergunta INTEGER, idFormItem INTEGER)");

        db.execSQL("CREATE TABLE if not exists opcaoQuestao(_idOpcao INTEGER, opcao TEXT,idPergunta INTEGER )");

        db.execSQL("CREATE TABLE if not exists pergunta(_idPergunta INTEGER, txtPergunta TEXT, tipoPergunta INTEGER, opcaoQuestaoTodo TEXT, " +
                "idForm INTEGER)");

        db.execSQL("CREATE TABLE if not exists resposta(_idResposta INTEGER PRIMARY KEY, " +
                "txtResposta TEXT,idPergunta INTEGER, idFormItem INTEGER, idOpcao INTEGER, todo INTEGER)");

        db.execSQL("CREATE TABLE if not exists formItem(_idFormItem INTEGER PRIMARY KEY AUTOINCREMENT,status INTEGER, idForm INTEGER, FOREIGN KEY(idForm) REFERENCES form(_idForm))");

        db.execSQL("CREATE TABLE if not exists complementoResposta(_idComplemento INTEGER PRIMARY KEY, image BLOB, comentario TEXT, " +
                "idPergunta INTEGER, idFormItem INTEGER)");

        db.execSQL("CREATE TABLE if not exists complementoTodo(_idComplemento INTEGER PRIMARY KEY, image BLOB, comentario TEXT, " +
                "idTodo INTEGER, FOREIGN KEY(idTodo) REFERENCES todo(_idTodo))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
