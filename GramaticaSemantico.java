package main.antlr4.compiladores.gerador;

import main.antlr4.compiladores.gerador.TabelaDeSimbolos.TipoGramatica;

public class GramaticaSemantico extends GramaticaBaseVisitor<Void> {

    TabelaDeSimbolos tabela;

    @Override
    public Void visitPrograma(GramaticaParser.ProgramaContext ctx) {
        tabela = new TabelaDeSimbolos();
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitDeclaracao(GramaticaParser.DeclaracaoContext ctx) {
        String nomeVar = ctx.VARIAVEL().getText();
        String strTipoVar = ctx.TIPO_VAR().getText();
        TipoGramatica tipoVar = TipoGramatica.INVALIDO;
        switch (strTipoVar) {
            case "INTEIRO":
                tipoVar = TipoGramatica.INTEIRO;
                break;
            case "REAL":
                tipoVar = TipoGramatica.REAL;
                break;
            default:
                // Nunca irá acontecer, pois o analisador sintático
                // não permite
                break;
        }

        // Verificar se a variável já foi declarada
        if (tabela.existe(nomeVar)) {
            GramaticaSemanticoUtils.adicionarErroSemantico(ctx.VARIAVEL().getSymbol(),
                    "Variável " + nomeVar + " já existe");
        } else {
            tabela.adicionar(nomeVar, tipoVar);
        }

        return super.visitDeclaracao(ctx);
    }

    @Override
    public Void visitComandoAtribuicao(GramaticaParser.ComandoAtribuicaoContext ctx) {
        TipoGramatica tipoExpressao = GramaticaSemanticoUtils.verificarTipo(tabela, ctx.expressaoAritmetica());
        if (tipoExpressao != TipoGramatica.INVALIDO) {
            String nomeVar = ctx.VARIAVEL().getText();
            if (!tabela.existe(nomeVar)) {
                GramaticaSemanticoUtils.adicionarErroSemantico(ctx.VARIAVEL().getSymbol(),
                        "Variável " + nomeVar + " não foi declarada antes do uso");
            } else {
                TipoGramatica tipoVariavel = GramaticaSemanticoUtils.verificarTipo(tabela, nomeVar);
                if (tipoVariavel != tipoExpressao && tipoExpressao != TipoGramatica.INVALIDO) {
                    GramaticaSemanticoUtils.adicionarErroSemantico(ctx.VARIAVEL().getSymbol(),
                            "Tipo da variável " + nomeVar + " não é compatível com o tipo da expressão");
                }
            }
        }
        return super.visitComandoAtribuicao(ctx);
    }

    @Override
    public Void visitComandoEntrada(GramaticaParser.ComandoEntradaContext ctx) {
        String nomeVar = ctx.VARIAVEL().getText();
        if (!tabela.existe(nomeVar)) {
            GramaticaSemanticoUtils.adicionarErroSemantico(ctx.VARIAVEL().getSymbol(),
                    "Variável " + nomeVar + " não foi declarada antes do uso");
        }
        return super.visitComandoEntrada(ctx);
    }

    @Override
    public Void visitExpressaoAritmetica(GramaticaParser.ExpressaoAritmeticaContext ctx) {
        GramaticaSemanticoUtils.verificarTipo(tabela, ctx);
        return super.visitExpressaoAritmetica(ctx);
    }
}