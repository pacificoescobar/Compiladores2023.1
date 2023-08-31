import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


class MainIMP {

    static void print(String fmt, Object ...args) {
        System.out.print(String.format(fmt, args));
    }
    static void println(String fmt, Object ...args) {
        System.out.println(String.format(fmt, args));
    }
    static String opName(ParseTree t) {
        String name = t.getClass().getName();
        name = name.substring(name.indexOf("$")+1);
        name = name.substring(0,name.indexOf("Context"));
        return name;
    }
    static void generateCode(ParseTree t) {
        switch (opName(t)) {
        case "Program":
            generateCode(t.getChild(0));
            return;
        case "Atrib":
            generateCode(t.getChild(2));
            println("store %s",t.getChild(0).getText());
            return;
        case "Print":
            generateCode(t.getChild(1));
            println("output\n");
            return;
        case "Seq":
            for (int c=1;c<t.getChildCount()-1;c++)
                generateCode(t.getChild(c));
            return;
        case "Const":
            println("push %s",t.getText());
            return;
        case "Var":
            println("load %s",t.getText());
            return;
        case "Op":
            generateCode(t.getChild(0));
            generateCode(t.getChild(2));
            String op = t.getChild(1).getText();
            if (op.equals("+")) println("sum");
            if (op.equals("-")) println("diff");
            if (op.equals("*")) println("mult");
            if (op.equals("==")) println("eq");
            if (op.equals("!=")) println("neq");
            return;
        }
            
    }

    public static void main(String args[]) throws Exception {


        CharStream stream = CharStreams.fromFileName("exemplo.imp");
        ImpLexer lexer = new ImpLexer(stream);
        TokenStream tkStream = new CommonTokenStream(lexer);
        ImpParser parser = new ImpParser(tkStream);

        ParseTree tree = parser.program();
        if (parser.getNumberOfSyntaxErrors()==0)
           System.out.println("Programa reconhecido corretamente");
        else {
            System.out.println("Programa possui erros corrija-os");
            return;
        }
        println("Gerando código de máquina:");
        generateCode(tree);
    }
}