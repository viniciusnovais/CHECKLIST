package br.com.pdasolucoes.checklist.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by PDA on 04/11/2016.
 */

public class Form implements Serializable{
    private int idForm;
    private String nomeFom;
    private String dataForm;
    private String nomeLoja;
    private int hora;
    private int status;

    public int getIdForm() {
        return idForm;
    }

    public void setIdForm(int idForm) {
        this.idForm = idForm;
    }

    public String getNomeFom() {
        return nomeFom;
    }

    public void setNomeFom(String nomeFom) {
        this.nomeFom = nomeFom;
    }

    public String getDataForm() {
        return dataForm;
    }

    public void setDataForm(String dataForm) {
        this.dataForm = dataForm;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public void setNomeLoja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }
}
