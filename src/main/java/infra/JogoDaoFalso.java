package infra;

import java.util.ArrayList;
import java.util.List;

import br.com.valueprojects.mock_spring.model.Jogo;

public class JogoDaoFalso implements JogoDaoInterface {

    private static List<Jogo> Jogos = new ArrayList<>();

    @Override
    public void salva(Jogo jogo) {
        Jogos.add(jogo);
    }

    @Override
    public List<Jogo> emAndamento() {
        List<Jogo> selecionados = new ArrayList<>();
        for (Jogo jogo : Jogos) {
            if (!jogo.isFinalizado()) {
                selecionados.add(jogo);
            }
        }
        return selecionados;
    }

    @Override
    public void atualiza(Jogo jogo) {
        // No mock não realizamos nenhuma ação real
    }

    public List<Jogo> finalizados() {
        List<Jogo> selecionados = new ArrayList<>();
        for (Jogo jogo : Jogos) {
            if (jogo.isFinalizado()) {
                selecionados.add(jogo);
            }
        }
        return selecionados;
    }
}
