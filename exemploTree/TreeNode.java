
class TreeNode {
    String type, info;
    TreeNode childs[];

    TreeNode(String type, String info, TreeNode ...childs) {
        this.type = type;
        this.info = info;
        this.childs = childs;
    }

    public String toString() {
        String result = info;
        if (childs.length>0) {
            result += "[";
            for (TreeNode ch : childs) 
              result += " " + ch.toString();
            result += "]";
        }
        return result;
    }
    public void print(String indent) {
        System.out.println(indent + type + " " + info);
        if (childs.length>0) {
            //System.out.println(" [");
            for (TreeNode ch : childs) 
              ch.print(indent + "|--- ");
            //System.out.print(indent + "]");
        }
        //System.out.println();
    }

}