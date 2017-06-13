package br.com.pdasolucoes.checklist.model;

/**
 * Created by PDA on 23/01/2017.
 */

public class FormItem {

    private int idItem;
    private Form idForm;
    private int status;

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public Form getIdForm() {
        return idForm;
    }

    public void setIdForm(Form idForm) {
        this.idForm = idForm;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
