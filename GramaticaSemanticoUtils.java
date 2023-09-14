package main.antlr4.compiladores.gerador;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;

public class GramaticaSemanticoUtils {
    public static List<String> errosSemanticos = new ArrayList<>();

    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        int coluna = t.getCharPositionInLine();
        errosSemanticos.add(String.format("Erro %d:%d - %s", linha, coluna, mensagem));
    }

    public static TabelaDeSimbolos.TipoGramatica verificarTipo(TabelaDeSimbolos tabela,
            GramaticaParser.ExpressaoAritmeticaContext ctx) {
        TabelaDeSimbolos.TipoGramatica ret = null;
        for (var ta : ctx.termoAritmetico()) {
            TabelaDeSimbolos.TipoGramatica aux = verificarTipo(tabela, ta);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != TabelaDeSimbolos.TipoGramatica.INVALIDO) {
                adicionarErroSemantico(ctx.start, "Expressão " + ctx.getText() + " contém tipos incompatíveis");
                ret = TabelaDeSimbolos.TipoGramatica.INVALIDO;
            }
        }

        return ret;
    }

    public static TabelaDeSimbolos.TipoGramatica verificarTipo(TabelaDeSimbolos tabela,
            GramaticaParser.TermoAritmeticoContext ctx) {
        TabelaDeSimbolos.TipoGramatica ret = null;

        for (var fa : ctx.fatorAritmetico()) {
            TabelaDeSimbolos.TipoGramatica aux = verificarTipo(tabela, fa);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != TabelaDeSimbolos.TipoGramatica.INVALIDO) {
                adicionarErroSemantico(ctx.start, "Termo " + ctx.getText() + " contém tipos incompatíveis");
                ret = TabelaDeSimbolos.TipoGramatica.INVALIDO;
            }
        }
        return ret;
    }

    public static TabelaDeSimbolos.TipoGramatica verificarTipo(TabelaDeSimbolos tabela,
            GramaticaParser.FatorAritmeticoContext ctx) {
        if (ctx.NUMINT() != null) {
            return TabelaDeSimbolos.TipoGramatica.INTEIRO;
        }
        if (ctx.NUMREAL() != null) {
            return TabelaDeSimbolos.TipoGramatica.REAL;
        }
        if (ctx.VARIAVEL() != null) {
            String nomeVar = ctx.VARIAVEL().getText();
            if (!tabela.existe(nomeVar)) {
                adicionarErroSemantico(ctx.VARIAVEL().getSymbol(),
                        "Variável " + nomeVar + " não foi declarada antes do uso");
                return TabelaDeSimbolos.TipoGramatica.INVALIDO;
            }
            return verificarTipo(tabela, nomeVar);
        }
        // se não for nenhum dos tipos acima, só pode ser uma expressão
        // entre parêntesis
        return verificarTipo(tabela, ctx.expressaoAritmetica());
    }

    public static TabelaDeSimbolos.TipoGramatica verificarTipo(TabelaDeSimbolos tabela, String nomeVar) {
        return tabela.verificar(nomeVar);
    }
}
