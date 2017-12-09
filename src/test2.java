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
        System.out.println("entropy of t:"+t.tableEntropy());
        System.out.println("highest gain feature is:"+t.highestGainFeature());
        int i = t.highestGainFeature();
        for(table temp : t.makeSubTables(i)){
            System.out.println(temp);
            System.out.println(table.rowsep);
        }
        System.out.print("IG of highest gain feature is: ");
        System.out.println(t.gain(t.highestGainFeature()));
    }
}
