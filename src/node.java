import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Node {
    String name;
    Table myTab;
    HashMap<String, Node> children;
    //HashMap<String, Table> subTabs;//i dont think i need this. the kids can
    // keep track of their own tables.
    public Node(String name, Table t){
        this.name=name;
        myTab=t;
        children=new HashMap<>();
        //subTabs=new HashMap<>();
    }

    //when making the tree we want to go a level deeper if:
    //a Node has more than one category && has more than 1 element
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
        //you now need a Table to make a Node. how will this work for leaf nodes
        //i need to trace through a simple example
    }


    public String toString(){
        String out ="";//name.toUpperCase();
        //if(!children.isEmpty()){ out+=":"; }
        //out+="\n";
        int tab = 4;
        String s = new ArrayList<>(Arrays.asList(myTab.getCats())).toString();
        for(String key : children.keySet()){
            String temp = children.get(key).name+"-->"+key+
                    (!children.get(key).myTab.shouldSplit()?
                            ":"+children.get(key).myTab.catsAsSet():
                            "")+"\n";
            if(!children.get(key).children.isEmpty()){
                temp+=indent(tab,children.get(key).toString());
                //temp+=indent(tab,new ArrayList<>(Arrays.asList(myTab.getCats())).toString());
            }else{
                //
            }
//            temp+=new ArrayList<>(Arrays.asList(myTab.getCats())).toString();
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
    public void split(){//always check to see if you should split before you do.
        if(!myTab.shouldSplit()){return;}
        int feat = myTab.highestGainFeature();
        Table[] tables=myTab.makeSubTables(feat);
        String[] featnames = myTab.getFeatLables(feat);
        for(int i = 0;i<featnames.length;i++){
            children.put(featnames[i],new Node(myTab.featureLables.get(feat),
                    tables[i]));
            //System.out.println(indent(4,tables[i].toString()));
            if(children.get(featnames[i]).myTab.shouldSplit()){
                children.get(featnames[i]).split();
            }
        }
        //then split recursively
    }
}
