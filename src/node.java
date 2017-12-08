import java.util.HashMap;

public class node {
    String name;
    HashMap<String, node> children;
    public node(String name){
        this.name=name;
        children=new HashMap<>();
    }

    public static void main(String[] args){
        node choice1 = new node("color");
        node choice2 = new node("shape");
        node class1 = new node("class1");
        node class2 = new node("class2");
        node class3 = new node("class3");

        choice1.children.put("red",choice2);
        choice1.children.put("blue",class1);
        choice2.children.put("circle",class2);
        choice2.children.put("triangle",class3);

        System.out.println(choice1);

    }

    public String toString(){
        String out =name;
        if(!children.isEmpty()){ out+=":"; }
        out+="\n";
        int tab = 2;

        for(String key : children.keySet()){
            String temp = key+"-->"+children.get(key).name+"\n";
            if(!children.get(key).children.isEmpty())
                temp+=indent(tab,children.get(key).toString());
            out+=indent(0,temp);
        }

        return out;
    }
    public static String indent(int howmanyspaces, String input){
        String out = input;
        String pad = new String(new char[howmanyspaces]).replace("\0"," ");

        for(int i = out.length()-1;i>0;i--){
            if(out.substring(i-1,i).equals("\n")){
                out=out.substring(0,i)+pad+out.substring(i);
            }
        }
        out=pad+out;
        return out;
    }
}
