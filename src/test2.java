import java.util.ArrayList;
import java.util.Arrays;

public class Test2 {
    public static void main(String[] args){
        Table t = new Table(5,2);
        String[] fl = new String[]{"color","shape"};
        t.featureLables = new ArrayList<String>(Arrays.asList(fl));
        t.add(new String[]{"blue","tri","A"});
        t.add(new String[]{"blue","tri","A"});
        t.add(new String[]{"blue","tri","B"});
        t.add(new String[]{"blue","tri","B"});
        t.add(new String[]{"blue","tri","B"});

        System.out.println(t);
        System.out.println(Table.rowsep);
        Node n = new Node("root",t);
        n.split();
        System.out.println(n);
    }
}
