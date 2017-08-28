package br.com.pdasolucoes.checklist.model;

/**
 * Created by PDA on 21/07/2017.
 */

public class Setor {

    private int id;
    private String nome;
    private int status;
    private int idForm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIdForm() {
        return idForm;
    }

    public void setIdForm(int idForm) {
        this.idForm = idForm;
    }
}
