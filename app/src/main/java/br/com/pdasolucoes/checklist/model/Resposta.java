package br.com.pdasolucoes.checklist.model;

/**
 * Created by PDA on 06/12/2016.
 */

public class Resposta {

    private int idResposta;
    private String txtResposta;
    private Pergunta idPergunta;
    private int idFormItem;
    private int idOpcao;
    private int todo;

    public int getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
    }


    public String getTxtResposta() {
        return txtResposta;
    }

    public void setTxtResposta(String txtResposta) {
        this.txtResposta = txtResposta;
    }

    public Pergunta getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(Pergunta idPergunta) {
        this.idPergunta = idPergunta;
    }

    public int getIdFormItem() {
        return idFormItem;
    }

    public void setIdFormItem(int idForm) {
        this.idFormItem = idForm;
    }

    public int getIdOpcao() {
        return idOpcao;
    }

    public void setIdOpcao(int idOpcao) {
        this.idOpcao = idOpcao;
    }

    public int getTodo() {
        return todo;
    }

    public void setTodo(int todo) {
        this.todo = todo;
    }
}
