import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Node {
    String name;
    Table table;
    HashMap<String, Node> children;

    public Node(String name, Table table){
        this.name = name;
        this.table = table;

        children = new HashMap<>();
    }

    //when making the tree we want to go a level deeper if:
    //a node has more than one category && has more than 1 element
    //this doesnt check for the case where the cats are mixed, there are
    // multiple elements, and the features all have the same values.
    //i suppose it will continue eliminating features but it makes
    // the tree deeper without purpose.
    //how can i check if there is a useful feature to test.

    //the reverse. when do we stop:
    //features=0 how do we get here? what happens when IG(T,a1)==IG(T,a2)?
    //categories=1
    //elements=0? can this happen?

    public static void main(String[] args){
//        Node choice1 = new Node("color");
//        Node choice2 = new Node("shape");
//        Node class1 = new Node("class1");
//        Node class2 = new Node("class2");
//        Node class3 = new Node("class3");

//        choice1.children.put("red",choice2);
//        choice1.children.put("blue",class1);
//        choice2.children.put("circle",class2);
//        choice2.children.put("triangle",class3);

//        System.out.println(choice1);

        //everything is commented out because of the cunstructor change.
        //you now need a table to make a node. how will this work for leaf nodes
        //i need to trace through a simple example
    }


    public String toString() {
        String out = "";
        int tab = 4;

        for (String key : children.keySet()) {
            String temp = children.get(key).name + "-->" + key;
            if (children.get(key).children.isEmpty()) {
                temp += new ArrayList<>(Arrays.asList(table.getCategories())).toString();
            }
            temp += "\n";
            //temp += "test1";
            if (!children.get(key).children.isEmpty()) {
                //temp += "test2";
                temp += indent(tab,children.get(key).toString());
                //temp+=indent(tab,new ArrayList<>(Arrays.asList(table.getCategories())).toString());
                //temp += "test3";
            }

            out += indent(0,temp);
        }

        return out;
    }

    public static String indent(int length, String input){
        String out = input;
        String pad = new String(new char[length]).replace("\0"," ");

        for (int i = out.length()-1; i>0; i--) {
            if (out.substring(i-1,i).equals("\n")) {
                out = out.substring(0,i)+pad+out.substring(i);
            }
        }
        out = pad + out;
        return out;
    }

    public void split() {
        int featureIndex = table.highestGainFeature();

        Table[] tables = table.makeSubTables(featureIndex);
        String[] features = table.getFeatureLabels(featureIndex);

        for (int i = 0; i < features.length; i++) {
            children.put(features[i], new Node(table.featureLabels.get(featureIndex),
                         tables[i]));
            if (children.get(features[i]).table.shouldSplit()) {
                children.get(features[i]).split();
            }
        }
        //then split recursively
    }
}
