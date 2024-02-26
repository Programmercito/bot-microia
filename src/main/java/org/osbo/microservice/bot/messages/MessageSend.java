package org.osbo.microservice.bot.messages;

import java.io.Serializable;

public class MessageSend implements Serializable{
    private static final long serialVersionUID = 1L;
    private String message;
    private long idchat;
    private String tipo;
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public long getIdchat() {
        return idchat;
    }
    public void setIdchat(long idchat) {
        this.idchat = idchat;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
