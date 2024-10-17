package br.com.valueprojects.mock_spring.notificacao;

public interface Notificacao {
    void enviar(String destinatario, String assunto, String mensagem);
}
