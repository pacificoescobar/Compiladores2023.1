class Main {

    //Tradução Orientada a Sintaxe
    public static int avalie(TreeNode n) {
        switch (n.info) {
            case "add": {
                TreeNode esq = n.childs[0];
                TreeNode dir = n.childs[2];
                int vEsq = avalie(esq);
                int vDir = avalie(dir);
                return vEsq + vDir;
            }          
            case "const": {
                String num = n.childs[0].info;
                int v = Integer.parseInt(num);
                return v;
            }

        }
        throw new RuntimeException("No Arvore " + n + " de tipo desconhecido");

    }
    public static void main(String args[]) {
        //Representação da expressão: 1 + 2
        TreeNode n1 = new TreeNode("exp","add",
            new TreeNode("exp", "const", new TreeNode("NUM","1")),
            new TreeNode("SOMA","+"),
            new TreeNode("exp", "const", new TreeNode("NUM","2"))
        );
        System.out.printf("A Árvore Sintática de 1+2 = %s\n", n1.toString());
        n1.print("");
        System.out.printf("Valor da expressão = %d\n",avalie(n1));
    }
}