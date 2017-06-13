package br.com.pdasolucoes.checklist.model;

/**
 * Created by PDA on 30/11/2016.
 */

import java.sql.Timestamp;
import java.util.Date;

public class Pergunta {

    private int idPergunta;
    private String txtPergunta;
    private int tipoPergunta;
    private String opcaoQuestaoTodo;
    private Form idForm;

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public String getTxtPergunta() {
        return txtPergunta;
    }

    public void setTxtPergunta(String txtPergunta) {
        this.txtPergunta = txtPergunta;
    }

    public int getTipoPergunta() {
        return tipoPergunta;
    }

    public void setTipoPergunta(int tipoPergunta) {
        this.tipoPergunta = tipoPergunta;
    }

    public String getOpcaoQuestaoTodo() {
        return opcaoQuestaoTodo;
    }

    public void setOpcaoQuestaoTodo(String opcaoQuestaoTodo) {
        this.opcaoQuestaoTodo = opcaoQuestaoTodo;
    }

    public Form getIdForm() {
        return idForm;
    }

    public void setIdForm(Form idForm) {
        this.idForm = idForm;
    }
}
