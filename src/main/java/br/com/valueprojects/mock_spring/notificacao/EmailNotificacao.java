package br.com.valueprojects.mock_spring.notificacao;

public class EmailNotificacao implements Notificacao {

    @Override
    public void enviar(String destinatario, String assunto, String mensagem) {
        // Simulando o envio de Email com um print
        System.out.println("Enviando Email para " + destinatario);
        System.out.println("Assunto: " + assunto);
        System.out.println("Mensagem: " + mensagem);
    }
}
