package main.antlr4.compiladores.gerador;

import main.antlr4.compiladores.gerador.TabelaDeSimbolos.TipoGramatica;

public class GramaticaGeradorC {
    StringBuilder saida;
    TabelaDeSimbolos tabela;

    public GramaticaGeradorC() {
        saida = new StringBuilder();
        this.tabela = new TabelaDeSimbolos();
    }

    @Override
    public Void visitPrograma(GramaticaParser.ProgramaContext ctx) {
        saida.append("#include <stdio.h>\n");
        saida.append("#include <stdlib.h>\n");
        saida.append("\n");
        ctx.declaracao().forEach(dec -> visitDeclaracao(dec));
        saida.append("\n");
        saida.append("int main() {\n");
        ctx.comando().forEach(cmd -> visitComando(cmd));
        saida.append("}\n");
        return null;
    }

    @Override
    public Void visitDeclaracao(GramaticaParser.DeclaracaoContext ctx) {
        String nomeVar = ctx.VARIAVEL().getText();
        String strTipoVar = ctx.TIPO_VAR().getText();
        TabelaDeSimbolos.TipoGramatica tipoVar = TabelaDeSimbolos.TipoGramatica.INVALIDO;
        switch (strTipoVar) {
            case "INTEIRO":
                tipoVar = TabelaDeSimbolos.TipoGramatica.INTEIRO;
                strTipoVar = "int";
                break;
            case "REAL":
                tipoVar = TabelaDeSimbolos.TipoGramatica.REAL;
                strTipoVar = "float";
                break;
            default:
                // Nunca irá acontecer, pois o analisador sintático
                // não permite
                break;
        }
        // Podemos adicionar na tabela de símbolos sem verificar
        // pois a análise semântica já fez as verificações
        tabela.adicionar(nomeVar, tipoVar);
        saida.append(strTipoVar + " " + nomeVar + ";\n");
        return null;
    }

    @Override
    public Void visitComandoAtribuicao(GramaticaParser.ComandoAtribuicaoContext ctx) {
        saida.append(ctx.VARIAVEL().getText() + " = ");
        visitExpressaoAritmetica(ctx.expressaoAritmetica());
        saida.append(";\n");
        return null;
    }

    @Override
    public Void visitComandoCondicao(GramaticaParser.ComandoCondicaoContext ctx) {
        saida.append("if(");
        visitExpressaoRelacional(ctx.expressaoRelacional());
        saida.append(")\n");
        visitComando(ctx.comando(0));
        if (ctx.comando().size() > 1) { // tem else
            saida.append("else\n");
            visitComando(ctx.comando(1));
        }
        return null;
    }

    @Override
    public Void visitComandoEntrada(GramaticaParser.ComandoEntradaContext ctx) {
        String nomeVar = ctx.VARIAVEL().getText();
        TipoGramatica tipoVariavel = GramaticaSemanticoUtils.verificarTipo(tabela, nomeVar);
        String aux = "";
        switch (tipoVariavel) {
            case INTEIRO:
                aux = "%d";
                break;
            case REAL:
                aux = "%f";
                break;
        }
        saida.append("scanf(\"" + aux + "\", &" + nomeVar + ");\n");
        return null;
    }

    @Override
    public Void visitComandoRepeticao(GramaticaParser.ComandoRepeticaoContext ctx) {
        saida.append("while(");
        visitExpressaoRelacional(ctx.expressaoRelacional());
        saida.append(")\n");
        visitComando(ctx.comando());
        return null;
    }

    @Override
    public Void visitComandoSaida(GramaticaParser.ComandoSaidaContext ctx) {
        if (ctx.CADEIA() != null) {
            String aux = ctx.CADEIA().getText();
            aux = aux.substring(1, aux.length() - 1);
            saida.append("printf(\"" + aux + "\\n\");\n");
        } else {
            TipoGramatica tipoExpressao = GramaticaSemanticoUtils.verificarTipo(tabela, ctx.expressaoAritmetica());
            String aux = "";
            switch (tipoExpressao) {
                case INTEIRO:
                    aux = "%d";
                    break;
                case REAL:
                    aux = "%f";
                    break;
            }
            saida.append("printf(\"" + aux + "\\n\",");
            visitExpressaoAritmetica(ctx.expressaoAritmetica());
            saida.append(");\n");
        }
        return null;
    }

    @Override
    public Void visitSubAlgoritmo(GramaticaParser.SubAlgoritmoContext ctx) {
        saida.append("{\n");
        ctx.comando().forEach(cmd -> visitComando(cmd));
        saida.append("}\n");
        return null;
    }

    @Override
    public Void visitExpressaoAritmetica(GramaticaParser.ExpressaoAritmeticaContext ctx) {
        visitTermoAritmetico(ctx.termoAritmetico(0));
        for (int i = 0; i < ctx.OP_ARIT1().size(); i++) {
            saida.append(" " + ctx.OP_ARIT1(i).getText() + " ");
            visitTermoAritmetico(ctx.termoAritmetico(i + 1));
        }
        return null;
    }

    @Override
    public Void visitTermoAritmetico(GramaticaParser.TermoAritmeticoContext ctx) {
        visitFatorAritmetico(ctx.fatorAritmetico(0));
        for (int i = 0; i < ctx.OP_ARIT2().size(); i++) {
            saida.append(" " + ctx.OP_ARIT2(i).getText() + " ");
            visitFatorAritmetico(ctx.fatorAritmetico(i + 1));
        }
        return null;
    }

    @Override
    public Void visitFatorAritmetico(GramaticaParser.FatorAritmeticoContext ctx) {
        if (ctx.NUMINT() != null) {
            saida.append(ctx.NUMINT().getText());
        } else if (ctx.NUMREAL() != null) {
            saida.append(ctx.NUMREAL().getText());
        } else if (ctx.VARIAVEL() != null) {
            saida.append(ctx.VARIAVEL().getText());
        } else {
            saida.append("(");
            visitExpressaoAritmetica(ctx.expressaoAritmetica());
            saida.append(")");
        }
        return null;
    }

    @Override
    public Void visitExpressaoRelacional(GramaticaParser.ExpressaoRelacionalContext ctx) {
        visitTermoRelacional(ctx.termoRelacional(0));
        for (int i = 0; i < ctx.OP_BOOL().size(); i++) {
            String aux = null;
            if (ctx.OP_BOOL(i).getText().equals("E")) {
                aux = "&&";
            } else {
                aux = "||";
            }
            saida.append(" " + aux + " ");
            visitTermoRelacional(ctx.termoRelacional(i + 1));
        }
        return null;
    }

    @Override
    public Void visitTermoRelacional(GramaticaParser.TermoRelacionalContext ctx) {
        if (ctx.expressaoRelacional() != null) {
            saida.append("(");
            visitExpressaoRelacional(ctx.expressaoRelacional());
            saida.append(")");
        } else {
            visitExpressaoAritmetica(ctx.expressaoAritmetica(0));
            String aux = ctx.OP_REL().getText();
            if (aux.equals("<>")) {
                aux = "!=";
            } else if (aux.equals("=")) {
                aux = "==";
            }
            saida.append(" " + aux + " ");
            visitExpressaoAritmetica(ctx.expressaoAritmetica(1));
        }
        return null;
    }

}
