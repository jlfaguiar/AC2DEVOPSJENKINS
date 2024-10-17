package br.com.valueprojects.mock_spring.notificacao;

public class SmsNotificacao implements Notificacao {

    @Override
    public void enviar(String destinatario, String assunto, String mensagem) {
        // Simulando o envio de SMS com um print
        System.out.println("Enviando SMS para " + destinatario);
        System.out.println("Assunto: " + assunto);
        System.out.println("Mensagem: " + mensagem);
    }
}
