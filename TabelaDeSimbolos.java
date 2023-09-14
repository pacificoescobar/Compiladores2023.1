package main.antlr4.compiladores.gerador;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    public enum TipoGramatica {
        INTEIRO,
        REAL,
        INVALIDO
    }

    class EntradaTabelaDeSimbolos {
        String nome;
        TipoGramatica tipo;

        private EntradaTabelaDeSimbolos(String nome, TipoGramatica tipo) {
            this.nome = nome;
            this.tipo = tipo;
        }
    }

    private final Map<String, EntradaTabelaDeSimbolos> tabela;

    public TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }

    public void adicionar(String nome, TipoGramatica tipo) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome, tipo));
    }

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public TipoGramatica verificar(String nome) {
        return tabela.get(nome).tipo;
    }

}
