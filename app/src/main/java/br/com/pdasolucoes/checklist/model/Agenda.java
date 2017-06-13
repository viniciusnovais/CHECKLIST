package br.com.pdasolucoes.checklist.model;

import java.util.Date;

/**
 * Created by PDA on 06/12/2016.
 */

public class Agenda {

    private int idAgenda;
    private int idForm;
    private String codForm;

    public int getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(int idAgenda) {
        this.idAgenda = idAgenda;
    }

    public int getIdForm() {
        return idForm;
    }

    public void setIdForm(int idForm) {
        this.idForm = idForm;
    }

    public String getCodForm() {
        return codForm;
    }
    public void setCodForm(String codForm) {
        this.codForm = codForm;
    }
}
