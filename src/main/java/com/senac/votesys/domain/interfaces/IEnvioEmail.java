package com.senac.votesys.domain.interfaces;

public interface IEnvioEmail {

    void  enviarEmailSimples (String para, String assunto, String texto);

    void enviarEmailTemplate (String para, String assunto, String texto);
}
