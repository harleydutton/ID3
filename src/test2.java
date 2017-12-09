import java.util.ArrayList;
import java.util.Arrays;

public class test2 {
    public static void main(String[] args){
        table t = new table(5,2);
        String[] fl = new String[]{"color","shape"};
        t.featureLables = new ArrayList<String>(Arrays.asList(fl));
        t.add(new String[]{"blue","tri","A"});
        t.add(new String[]{"blue","tri","A"});
        t.add(new String[]{"blue","tri","B"});
        t.add(new String[]{"blue","tri","B"});
        t.add(new String[]{"blue","tri","B"});

        System.out.println(t);
        System.out.println(table.rowsep);
        node n = new node("root",t);
        n.split();
        System.out.println(n);
    }
}
