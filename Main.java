package main.antlr4.compiladores.gerador;

import main.antlr4.compiladores.gerador.GramaticaParser.ProgramaContext;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {
    public static void main(String args[]) throws IOException {
        CharStream cs = CharStreams.fromFileName(args[0]);
        GramaticaLexer lexer = new GramaticaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GramaticaParser parser = new GramaticaParser(tokens);
        ProgramaContext arvore = parser.programa();
        GramaticaSemantico as = new GramaticaSemantico();
        as.visitPrograma(arvore);
        GramaticaSemanticoUtils.errosSemanticos.forEach((s) -> System.out.println(s));

        if (GramaticaSemanticoUtils.errosSemanticos.isEmpty()) {
            GramaticaGeradorC agc = new GramaticaGeradorC();
            agc.visitPrograma(arvore);
            try (PrintWriter pw = new PrintWriter(args[1])) {
                pw.print(agc.saida.toString());
            }
        }
    }
}
