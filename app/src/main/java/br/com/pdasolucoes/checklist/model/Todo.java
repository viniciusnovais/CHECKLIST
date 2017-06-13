package br.com.pdasolucoes.checklist.model;

import java.util.Date;

/**
 * Created by PDA on 06/12/2016.
 */

public class Todo {

    private int idTodo;
    private String justificativa;
    private String acao;
    private String responsavel;
    private int status;
    private int prazo;
    private String dataLimite;
    private String followup;
    private int idPergunta;
    private int idFormItem;

    public int getIdTodo() {
        return idTodo;
    }

    public void setIdTodo(int idTodo) {
        this.idTodo = idTodo;
    }


    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    public String getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(String dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getFollowup() {
        return followup;
    }

    public void setFollowup(String followup) {
        this.followup = followup;
    }

    public int getIdFormItem() {
        return idFormItem;
    }

    public void setIdFormItem(int idFormItem) {
        this.idFormItem = idFormItem;
    }

}
