package br.com.pdasolucoes.checklist.model;

/**
 * Created by PDA on 10/08/2017.
 */

public class Logo {

    private int idUsuario;
    private byte[] imagem;
    private int status;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
