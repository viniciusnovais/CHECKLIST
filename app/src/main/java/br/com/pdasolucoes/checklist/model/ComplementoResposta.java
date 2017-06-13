package br.com.pdasolucoes.checklist.model;

/**
 * Created by PDA on 18/04/2017.
 */

public class ComplementoResposta {

    private int idComplemento;
    private byte[] image;
    private String comentario;
    private int idPergunta;
    private int idFormItem;

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

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public int getIdFormItem() {
        return idFormItem;
    }

    public void setIdFormItem(int idFormItem) {
        this.idFormItem = idFormItem;
    }
}
