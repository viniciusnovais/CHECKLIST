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
    private float peso;
    private int alterado;
    private int idSetor;
    private int idPadraoResposta;

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

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public int getAlterado() {
        return alterado;
    }

    public void setAlterado(int alterado) {
        this.alterado = alterado;
    }

    public int getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(int idSetor) {
        this.idSetor = idSetor;
    }

    public int getIdPadraoResposta() {
        return idPadraoResposta;
    }

    public void setIdPadraoResposta(int idPadraoResposta) {
        this.idPadraoResposta = idPadraoResposta;
    }
}
