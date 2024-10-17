package br.com.valueprojects.mock_spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.valueprojects.mock_spring.builder.CriadorDeJogo;
import br.com.valueprojects.mock_spring.model.FinalizaJogo;
import br.com.valueprojects.mock_spring.model.Jogo;
import br.com.valueprojects.mock_spring.model.Participante;
import br.com.valueprojects.mock_spring.model.Resultado;
import br.com.valueprojects.mock_spring.notificacao.Notificacao;
import infra.JogoDao;
import infra.JogoDaoFalso;

public class FinalizaJogoTest {

    // Teste usando Mock para verificar se o jogo foi salvo antes da notificação
    @Test
    public void deveFinalizarJogosDaSemanaAnteriorEEnviarNotificacao() {

        // Mocks para o JogoDao e Notificacao
        JogoDao dao_mock = mock(JogoDao.class);
        Notificacao notificacao_mock = mock(Notificacao.class);

        // Simulação de jogos da semana anterior
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Participante vencedor = new Participante("Vencedor");
        Jogo jogo = new CriadorDeJogo().para("Jogo de Teste").naData(antiga)
            .resultado(vencedor, 10).constroi();

        List<Jogo> jogos_em_andamento = Arrays.asList(jogo);

        // Configuração do comportamento dos mocks
        when(dao_mock.emAndamento()).thenReturn(jogos_em_andamento);

        // Instância do objeto finalizador
        FinalizaJogo finalizador = new FinalizaJogo(dao_mock, notificacao_mock);
        finalizador.finaliza();

        // Verifica se o jogo foi salvo antes de enviar a notificação
        verify(dao_mock, times(1)).atualiza(jogo);
        verify(notificacao_mock, times(1)).enviar(eq(vencedor.getNome()), eq("Você é o vencedor!"), anyString());

        // Garante que a notificação só foi enviada após a atualização
        verifyNoMoreInteractions(notificacao_mock);
    }

    // Teste usando Stub, onde um comportamento básico é simulado sem verificação detalhada
    @Test
    public void deveFinalizarJogosDaSemanaAnteriorComStub() {

        // Criação de um stub para o JogoDao
        JogoDaoFalso dao_stub = new JogoDaoFalso();

        // Simulação de uma implementação mínima de Notificacao (stub)
        Notificacao notificacao_stub = new Notificacao() {
            @Override
            public void enviar(String destinatario, String assunto, String mensagem) {
                System.out.println("Stub de notificação: " + mensagem); // Apenas um print
            }
        };

        // Simulação de jogos da semana anterior
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Participante vencedor = new Participante("Vencedor");
        Jogo jogo = new CriadorDeJogo().para("Jogo de Teste")
            .naData(antiga).resultado(vencedor, 10).constroi();

        dao_stub.salva(jogo); // Salva o jogo usando o stub de JogoDao

        // Instância do objeto finalizador com stub
        FinalizaJogo finalizador = new FinalizaJogo(dao_stub, notificacao_stub);
        finalizador.finaliza();

        // Verifica se o jogo foi finalizado corretamente
        assertTrue(jogo.isFinalizado());
    }

    // Teste usando Spy para verificar o comportamento real do objeto e monitorar interações
    @Test
    public void deveVerificarInteracoesComSpy() {

        // Criação de um stub de JogoDao e um Spy para monitorar as interações
        JogoDaoFalso dao_stub = new JogoDaoFalso();
        JogoDaoFalso dao_spy = spy(dao_stub); // Criação do spy a partir do stub

        // Mock da notificação para simular o envio de mensagens
        Notificacao notificacao_mock = mock(Notificacao.class);

        // Simulação de jogos da semana anterior
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Participante vencedor = new Participante("Vencedor");
        Jogo jogo = new CriadorDeJogo().para("Jogo de Teste")
            .naData(antiga).resultado(vencedor, 10).constroi();

        dao_spy.salva(jogo); // Salvando o jogo no stub/spy

        // Instância do objeto finalizador usando o spy e mock de notificação
        FinalizaJogo finalizador = new FinalizaJogo(dao_spy, notificacao_mock);
        finalizador.finaliza();

        // Verifica se o método 'atualiza' foi invocado pelo spy
        verify(dao_spy).atualiza(jogo);

        // Verifica se o jogo foi finalizado corretamente
        assertTrue(jogo.isFinalizado());

        // Verifica se a notificação foi enviada
        verify(notificacao_mock).enviar(eq(vencedor.getNome()), eq("Você é o vencedor!"), anyString());
    }

    // Teste adicional usando Mock para verificar se o método 'atualiza' foi chamado corretamente
    @Test
    public void deveVerificarSeMetodoAtualizaFoiInvocado() {

        // Mocks para JogoDao e Notificacao
        JogoDao dao_mock = mock(JogoDao.class);
        Notificacao notificacao_mock = mock(Notificacao.class);

        // Simulação de jogos da semana anterior
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Jogo jogo1 = new CriadorDeJogo().para("Cata moedas").naData(antiga).constroi();
        Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras").naData(antiga).constroi();

        List<Jogo> jogos_em_andamento = Arrays.asList(jogo1, jogo2);

        // Configuração do mock para retornar os jogos em andamento
        when(dao_mock.emAndamento()).thenReturn(jogos_em_andamento);

        // Instância do objeto finalizador
        FinalizaJogo finalizador = new FinalizaJogo(dao_mock, notificacao_mock);
        finalizador.finaliza();

        // Verifica se o método 'atualiza' foi invocado para cada jogo
        verify(dao_mock, times(1)).atualiza(jogo1);
        verify(dao_mock, times(1)).atualiza(jogo2);
    }
}
