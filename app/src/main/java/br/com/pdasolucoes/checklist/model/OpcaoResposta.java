package br.com.pdasolucoes.checklist.model;

/**
 * Created by PDA on 09/02/2017.
 */

public class OpcaoResposta {

    private int idOpcao;
    private String opcao;
    private int idPergunta;
    private float percentual;
    private float maior;
    private float menor;
    private float valor;
    private int toDo;
    private String horaMaior;
    private String horaMenor;
    private String dataMenor;
    private String dataMaior;
    private String txtResposta;
    private int adicionado;

    public int getAdicionado() {
        return adicionado;
    }

    public void setAdicionado(int adicionado) {
        this.adicionado = adicionado;
    }

    public int getIdOpcao() {
        return idOpcao;
    }

    public void setIdOpcao(int idOpcao) {
        this.idOpcao = idOpcao;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public String getTxtResposta() {
        return txtResposta;
    }

    public void setTxtResposta(String txtResposta) {
        this.txtResposta = txtResposta;
    }

    public float getPercentual() {
        return percentual;
    }

    public void setPercentual(float percentual) {
        this.percentual = percentual;
    }

    public float getMaior() {
        return maior;
    }

    public void setMaior(float maior) {
        this.maior = maior;
    }

    public float getMenor() {
        return menor;
    }

    public void setMenor(float menor) {
        this.menor = menor;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getHoraMaior() {
        return horaMaior;
    }

    public void setHoraMaior(String horaMaior) {
        this.horaMaior = horaMaior;
    }

    public String getHoraMenor() {
        return horaMenor;
    }

    public void setHoraMenor(String horaMenor) {
        this.horaMenor = horaMenor;
    }

    public String getDataMenor() {
        return dataMenor;
    }

    public void setDataMenor(String dataMenor) {
        this.dataMenor = dataMenor;
    }

    public String getDataMaior() {
        return dataMaior;
    }

    public void setDataMaior(String dataMaior) {
        this.dataMaior = dataMaior;
    }

    public int getToDo() {
        return toDo;
    }

    public void setToDo(int toDo) {
        this.toDo = toDo;
    }
}
