package br.com.pdasolucoes.checklist.model;

/**
 * Created by PDA on 23/01/2017.
 */

public class FormItem {

    private int idItem;
    private Form idForm;
    private int status;
    private Setor idSetor;
    private String horaInicio;
    private String horaFim;
    private String dataInicio;
    private String dataFim;
    private int idUsuario;

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

    public void setIdSetor(Setor idSetor) {
        this.idSetor = idSetor;
    }

    public Setor getIdSetor() {
        return idSetor;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
