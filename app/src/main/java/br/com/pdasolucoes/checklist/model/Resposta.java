package br.com.pdasolucoes.checklist.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by PDA on 06/12/2016.
 */

public class Resposta implements KvmSerializable {

    private int idResposta;
    private String txtResposta;
    private int idPergunta;
    private int idFormItem;
    private int idOpcao;
    private int todo;
    private int idUsuario;
    private float valor;
    private String txtPergunta;
    private String nomeUsuario;
    private String opcao;


    public Resposta() {
        idResposta = 0;
        txtResposta = "";
        idPergunta = 0;
        idFormItem = 0;
        idOpcao = 0;
        todo = 0;
        idUsuario = 0;
        valor = 0;
        txtPergunta = "";
        nomeUsuario = "";
        opcao = "";
    }

    public Resposta(int idResposta, String txtResposta, int idPergunta, int idFormItem, int idOpcao, int todo, int idUsuario, float valor, String txtPergunta, String nomeUsuario, String opcao) {
        this.idResposta = idResposta;
        this.txtResposta = txtResposta;
        this.idPergunta = idPergunta;
        this.idFormItem = idFormItem;
        this.idOpcao = idOpcao;
        this.todo = todo;
        this.idUsuario = idUsuario;
        this.valor = valor;
        this.txtPergunta = txtPergunta;
        this.nomeUsuario = nomeUsuario;
        this.opcao = opcao;
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

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

    public int getIdFormItem() {
        return idFormItem;
    }

    public void setIdFormItem(int idFormItem) {
        this.idFormItem = idFormItem;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getTxtPergunta() {
        return txtPergunta;
    }

    public void setTxtPergunta(String txtPergunta) {
        this.txtPergunta = txtPergunta;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }


    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return idResposta;
            case 1:
                return txtResposta;
            case 2:
                return idPergunta;
            case 3:
                return idFormItem;
            case 4:
                return idOpcao;
            case 5:
                return todo;
            case 6:
                return idUsuario;
            case 7:
                return valor;
            case 8:
                return txtPergunta;
            case 9:
                return nomeUsuario;
            case 10:
                return opcao;
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
                idResposta = Integer.parseInt(o.toString());
                break;
            case 1:
                txtResposta = o.toString();
                break;
            case 2:
                idPergunta = Integer.parseInt(o.toString());
                break;
            case 3:
                idFormItem = Integer.parseInt(o.toString());
                break;
            case 4:
                idOpcao = Integer.parseInt(o.toString());
                break;
            case 5:
                todo = Integer.parseInt(o.toString());
                break;
            case 6:
                idUsuario = Integer.parseInt(o.toString());
                break;
            case 7:
                valor = Float.parseFloat(o.toString());
                break;
            case 8:
                txtPergunta = o.toString();
                break;
            case 9:
                nomeUsuario = o.toString();
                break;
            case 10:
                opcao = o.toString();
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
                propertyInfo.name = "txtResposta";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idPergunta";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idForm";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idOpcaoQuestao";
                break;
            case 5:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idToDo";
                break;
            case 6:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idUsuarioResponde";
                break;
            case 7:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "valor";
                break;
            case 8:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "txtPergunta";
                break;
            case 9:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "nomeUsuario";
                break;
            case 10:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "opcao";
                break;
        }
    }
}
