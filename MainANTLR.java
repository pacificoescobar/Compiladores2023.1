import org.antlr.v4.runtime.*;


class MainANTLR {
    public static void main(String args[]) {
        String src = "20 + 4";
        CharStream stream = CharStreams.fromString(src);
        ExpLexer lexer = new ExpLexer(stream);
        TokenStream tkStream = new CommonTokenStream(lexer);
        ExpParser parser = new ExpParser(tkStream);

        Object tree = parser.program();
        if (parser.getNumberOfSyntaxErrors()==0)
           System.out.println("Programa compilado corretamente");
        else
            System.out.println("Programa possui erros corrija-os");
        
    }
}