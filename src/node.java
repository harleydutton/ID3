import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class node {
    String name;
    table myTab;
    HashMap<String, node> children;
    //HashMap<String, table> subTabs;//i dont think i need this. the kids can
    // keep track of their own tables.
    public node(String name, table t){
        this.name=name;
        myTab=t;
        children=new HashMap<>();
        //subTabs=new HashMap<>();
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
//        node choice1 = new node("color");
//        node choice2 = new node("shape");
//        node class1 = new node("class1");
//        node class2 = new node("class2");
//        node class3 = new node("class3");

//        choice1.children.put("red",choice2);
//        choice1.children.put("blue",class1);
//        choice2.children.put("circle",class2);
//        choice2.children.put("triangle",class3);

//        System.out.println(choice1);

        //everything is commented out because of the cunstructor change.
        //you now need a table to make a node. how will this work for leaf nodes
        //i need to trace through a simple example
    }


    public String toString(){
        String out ="";//name.toUpperCase();
        //if(!children.isEmpty()){ out+=":"; }
        //out+="\n";
        int tab = 4;

        for(String key : children.keySet()){
            String temp = children.get(key).name+"-->"+key+"\n";
            if(!children.get(key).children.isEmpty()){
                temp+=indent(tab,children.get(key).toString());
                //temp+=indent(tab,new ArrayList<>(Arrays.asList(myTab.getCats())).toString());
            }else{

            }
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
    public void split(){
        int feat = myTab.highestGainFeature();
        table[] tables=myTab.makeSubTables(feat);
        String[] featnames = myTab.getFeatLables(feat);
        for(int i = 0;i<featnames.length;i++){
            children.put(featnames[i],new node(myTab.featureLables.get(feat),
                    tables[i]));
            //System.out.println(indent(4,tables[i].toString()));
            if(children.get(featnames[i]).myTab.shouldSplit()){
                children.get(featnames[i]).split();
            }
        }
        //then split recursively
    }
}
