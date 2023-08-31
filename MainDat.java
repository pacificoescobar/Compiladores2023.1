import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


class MainDat {

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
            for (int c=0;c<t.getChildCount()-1;c++)
                generateCode(t.getChild(c));
            return;
        case "Datatype":
            String cName = t.getChild(1).getText();
            int firstArg = 3;
            int lastArg = t.getChildCount()-2;
            println("class %s {",cName);
            print("     %s(",cName);
            for (int c=firstArg;c<lastArg;c++) {
                ParseTree arg = t.getChild(c);
                if (c>firstArg) print(",");
                print("%s %s",arg.getChild(0).getText(),arg.getChild(1).getText());
            }
            println(") {");
            for (int c=firstArg;c<lastArg;c++) {
                ParseTree arg = t.getChild(c);
                String type = arg.getChild(0).getText();
                String name = arg.getChild(1).getText();                
                print("        this.%s = %s;\n",name,name);
            }
            println("     }\n");
            for (int c=firstArg;c<lastArg;c++) {
                ParseTree arg = t.getChild(c);
                String type = arg.getChild(0).getText();
                String name = arg.getChild(1).getText();                
                print("     private %s %s;\n",type,name);
                print("     void %s(%s %s) { this.%s = %s; }\n",name,type,name,name,name);
                print("     %s %s() { return this.%s; }\n\n",type, name,name);
                
            }
            
            println("}");
            
        }
            
    }

    public static void main(String args[]) throws Exception {


        CharStream stream = CharStreams.fromFileName("exemplo.dat");
        DatatypeLexer lexer = new DatatypeLexer(stream);
        TokenStream tkStream = new CommonTokenStream(lexer);
        DatatypeParser parser = new DatatypeParser(tkStream);

        ParseTree tree = parser.program();
        if (parser.getNumberOfSyntaxErrors()==0)
           System.out.println("Programa reconhecido corretamente");
        else {
            System.out.println("Programa possui erros corrija-os");
            return;
        }
        println("Gerando artefatos de código:");
        generateCode(tree);
    }
}