package br.com.pdasolucoes.checklist.model;

/**
 * Created by PDA on 25/04/2017.
 */

public class ComplementoTodo {

    private int idComplemento;
    private byte[] image;
    private String comentario;
    private Todo idTodo;

    public int getIdComplemento() {
        return idComplemento;
    }

    public void setIdComplemento(int idComplemento) {
        this.idComplemento = idComplemento;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Todo getIdTodo() {
        return idTodo;
    }

    public void setIdTodo(Todo idTodo) {
        this.idTodo = idTodo;
    }
}
