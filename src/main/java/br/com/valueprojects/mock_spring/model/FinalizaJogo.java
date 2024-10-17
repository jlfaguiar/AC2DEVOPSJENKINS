package br.com.valueprojects.mock_spring.model;

import infra.JogoDaoInterface;
import br.com.valueprojects.mock_spring.notificacao.Notificacao;
import java.util.List;
import java.util.Calendar;

public class FinalizaJogo {

    private int total = 0;
    private final JogoDaoInterface dao;
    private final Notificacao notificacao;

    public FinalizaJogo(JogoDaoInterface dao, Notificacao notificacao) {
        this.dao = dao;
        this.notificacao = notificacao;
    }

    public void finaliza() {
        List<Jogo> todosJogosEmAndamento = dao.emAndamento();

        for (Jogo jogo : todosJogosEmAndamento) {
            if (iniciouSemanaAnterior(jogo)) {
                jogo.finaliza();
                dao.atualiza(jogo);  // Salva o jogo finalizado
                total++;

                // Enviar notificação ao vencedor
                Participante vencedor = getVencedor(jogo); // Método que define o vencedor
                if (vencedor != null) {
                    String mensagem = "Parabéns " + vencedor.getNome() + ", você venceu o jogo " + jogo.getDescricao();
                    notificacao.enviar(vencedor.getNome(), "Você é o vencedor!", mensagem);
                }
            }
        }
    }

    private Participante getVencedor(Jogo jogo) {
        Resultado vencedorResultado = null;

        for (Resultado resultado : jogo.getResultados()) {
            if (vencedorResultado == null || resultado.getMetrica() > vencedorResultado.getMetrica()) {
                vencedorResultado = resultado;
            }
        }

        return vencedorResultado != null ? vencedorResultado.getParticipante() : null;
    }

    private boolean iniciouSemanaAnterior(Jogo jogo) {
        return diasEntre(jogo.getData(), Calendar.getInstance()) >= 7;
    }

    private int diasEntre(Calendar inicio, Calendar fim) {
        Calendar data = (Calendar) inicio.clone();
        int diasNoIntervalo = 0;
        while (data.before(fim)) {
            data.add(Calendar.DAY_OF_MONTH, 1);
            diasNoIntervalo++;
        }
        return diasNoIntervalo;
    }

    public int getTotalFinalizados() {
        return total;
    }
}
