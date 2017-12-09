import java.util.ArrayList;
import java.util.Arrays;

public class test2 {
    public static void main(String[] args){
        Table t = new Table(5,2);
        String[] fl = new String[]{"color","shape"};
        t.featureLabels = new ArrayList<String>(Arrays.asList(fl));
        t.add(new String[]{"blue","tri","A"});
        t.add(new String[]{"blue","tri","A"});
        t.add(new String[]{"blue","tri","B"});
        t.add(new String[]{"blue","tri","B"});
        t.add(new String[]{"blue","tri","B"});

        System.out.println(t);
        System.out.println("entropy of t:"+t.tableEntropy());
        System.out.println("highest gain feature is:"+t.highestGainFeature());
        int i = t.highestGainFeature();
        for(Table temp : t.makeSubTables(i)){
            System.out.println(temp);
            System.out.println(Table.rowsep);
        }
        System.out.print("IG of highest gain feature is: ");
        System.out.println(t.gain(t.highestGainFeature()));
    }
}
