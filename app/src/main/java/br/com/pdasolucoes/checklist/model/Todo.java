package br.com.pdasolucoes.checklist.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by PDA on 06/12/2016.
 */

public class Todo implements KvmSerializable {

    private int idTodo;
    private String justificativa;
    private String acao;
    private String responsavel;
    private int status;
    private int prazo;
    private Date dataLimite;
    private String followup;
    private int idPergunta;
    private int idFormItem;
    private int idForm;
    private int idResposta;

    public Todo() {
        idTodo = 0;
        justificativa ="";
        acao="";
        status=0;
        responsavel="";
        prazo=0;
        dataLimite = new Date();
        followup="";
        idPergunta=0;
        idResposta=0;
        idForm = 0;
    }

    public Todo(int idTodo, String justificativa, String acao, String responsavel, int status, int prazo, Date dataLimite, String followup, int idPergunta, int idResposta, int idForm) {
        this.idTodo = idTodo;
        this.justificativa = justificativa;
        this.acao = acao;
        this.responsavel = responsavel;
        this.status = status;
        this.prazo = prazo;
        this.dataLimite = dataLimite;
        this.followup = followup;
        this.idPergunta = idPergunta;
        this.idResposta = idResposta;
        this.idForm = idForm;
    }

    public int getIdTodo() {
        return idTodo;
    }

    public void setIdTodo(int idTodo) {
        this.idTodo = idTodo;
    }

    public int getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
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

    public void setDataLimite(Date dataLimite) {
        this.dataLimite = dataLimite;
    }

    public Date getDataLimite() {
        return dataLimite;
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

    public int getIdForm() {
        return idForm;
    }

    public void setIdForm(int idForm) {
        this.idForm = idForm;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return idTodo;
            case 1:
                return justificativa;
            case 2:
                return acao;
            case 3:
                return status;
            case 4:
                return responsavel;
            case 5:
                return prazo;
            case 6:
                return dataLimite;
            case 7:
                return followup;
            case 8:
                return idPergunta;
            case 9:
                return idResposta;
            case 10:
                return idForm;
        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 11;
    }

    @Override
    public void setProperty(int i, Object o) {

        switch (i) {
            case 0:
                idTodo = Integer.parseInt(o.toString());
                break;
            case 1:
                justificativa = o.toString();
                break;
            case 2:
                acao = o.toString();
                break;
            case 3:
                status = Integer.parseInt(o.toString());
                break;
            case 4:
                responsavel = o.toString();
                break;
            case 5:
                prazo = Integer.parseInt(o.toString());
                break;
            case 6:
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    dataLimite = format.parse(o.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                followup = o.toString();
                break;
            case 8:
                idPergunta = Integer.parseInt(o.toString());
                break;
            case 9:
                idResposta = Integer.parseInt(o.toString());
                break;
            case 10:
                idForm = Integer.parseInt(o.toString());
                break;
        }


    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

        switch (i) {
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "id";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "justificativa";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "acao";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "status";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "responsavel";
                break;
            case 5:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "prazo";
                break;
            case 6:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "data";
                break;
            case 7:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "follow";
                break;
            case 8:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idPergunta";
                break;
            case 9:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idResposta";
                break;
            case 10:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idForm";
                break;
        }
    }
}
