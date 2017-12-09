import java.util.ArrayList;
import java.util.Arrays;

public class Test3 {
    public static void main(String[] args){

        //data input
        Table t = new Table(10,3);
        String[] fl = new String[]{"quant","color","shape"};
        t.featureLables = new ArrayList<String>(Arrays.asList(fl));
        t.add(new String[]{"1","red","square","A"});//0
        t.add(new String[]{"1","blue","square","B"});//1
        t.add(new String[]{"1","red","circle","C"});//2
        t.add(new String[]{"1","blue","circle","D"});//3
        t.add(new String[]{"2","red","triangle","A"});//4
        t.add(new String[]{"2","blue","triangle","B"});//5
        t.add(new String[]{"2","green","triangle","C"});//6
        t.add(new String[]{"1","green","square","A"});//7
        t.add(new String[]{"2","red","circle","B"});//8
        t.add(new String[]{"1","red","triangle","C"});//9

        //info output
        System.out.println(t);
        System.out.println("entropy of t:"+t.tableEntropy());
        System.out.println("highest gain feature is:"+t.highestGainFeature());
        System.out.println("subtables split on feature "+t.highestGainFeature
                ()+":");
        for(Table temp : t.makeSubTables(t.highestGainFeature())){
            System.out.println(temp);
            System.out.println(Table.rowsep);
        }
        System.out.println(Table.rowsep);

        Node n = new Node("my Node", t);
        n.split();
        System.out.println(n);

    }
}
