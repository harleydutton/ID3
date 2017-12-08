import java.util.ArrayList;
import java.util.HashMap;

public class table {

    private String[][] guts;
    public static double base;
    private int elements;
    private int catIndx;
    private int features;
    private int addIndex;


    //todo
    //write something that copies the table omitting various rows and columns
    //get gain to work
    //make the decision tree

    public table(int elements, int features){
        catIndx=features+1;
        this.features=features;
        guts = new String[elements][features+1];
        this.elements=elements;
        for(String[] sa : guts){
            for(String s : sa){
                s="";
            }
        }
    }
    public static void main(String[] args){
        base=2.0;
        table t = new table(6,2);
        t.set(0,"B","blue","square");
        t.set(1,"B","red","triangle");
        t.set(2,"B","red","square");
        t.set(3,"A","blue","circle");
        t.set(4,"A","red","square");
        t.set(5,"C","blue","circle");
        System.out.println(t);
        System.out.println(t.tableEntropy());

        for(table temp : t.makeSubTables(1)){
            System.out.println(temp);
            System.out.println("############");
        }
        /**
         *    bucketmanager bm = new bucketmanager();
         *    for(String[] s : t.guts){
         *          bm.add(s[s.length-1]);
         *    }
         *    System.out.println(bm);
         *    bucket b = new bucket("blah");
         *    System.out.println(b);
         *    System.out.println(bm.numBucks());
         */

    }

    void initAll4Disp(String str){
        for(int i = 0 ; i < guts.length; i++){
            for(int j  = 0 ; j < guts[i].length; j++){
                guts[i][j]=str;
            }
        }
    }

    void add(String[] strs){
        if(strs.length!=catIndx){
            System.out.println("the str[] was not the right size to be added.");
            System.out.println("this is the str[]:");
            for(String s : strs){
                System.out.print(s);
            }
            System.out.println("this is the table:\n"+this);
        }
        if(addIndex>=guts.length){
            System.out.println("this table isnt large enough to hold another " +
                    "element");
        }
        guts[addIndex]=strs;addIndex++;
    }

    //rows are features
    //columns are features and classification


    public String toString(){
        String out = "";

        int[] colMax = new int[guts[0].length];
        for(int i= 0 ; i < guts.length; i++){//traverses rows
            for(int j = 0 ; j<guts[0].length;j++) {//traverses columns
                colMax[j]=Math.max(colMax[j],guts[i][j].length());

            }
        }
        for(int i = 0 ; i < colMax.length;i++){
            colMax[i]+=2;
        }

//        for(String[] sa : guts){
//            for(String s : sa){
//                longest=Math.max(longest,s.length());
//            }
//        }
//        longest+=2;

        for(int i= 0 ; i < guts.length; i++){//traverses rows
            for(int j = 0 ; j<guts[0].length;j++) {//traverses columns

                String padding = padding=new String(new
                        char[colMax[j]-guts[i][j]
                        .length()]).replace("\0"," ");
                out+=guts[i][j]+padding;
            }

            out+="\n";
        }

        return out;
    }

    //feature# should only ever be input by a loop
    //im not going to guarantee that the features will be properly numbered
    table[] makeSubTables(int featureNum){
        //first check to make sure this is in bounds
        //we shouldnt need to but just to be safe
        //feature num is going to be zero indexed because it sounds good.
        //this means this method can take inputs of 0 and it should.
        //feature num needs to be less than tha total number of features.
        //this logic handles that problem i think.
        if(featureNum>=features){
            System.out.println("error! not enough features. feat#: " +
                    ""+featureNum+"\n"+this);
            System.out.println("returning null");
            return null;
        }

        //figure out how many sub tables we need.
        bucketmanager bm = new bucketmanager();
        for(String[] sa : guts){
            bm.add(sa[featureNum]);
        }
        int size = bm.numBucks();
        table[] out = new table[size];
        HashMap<String, table> blah = new HashMap<>();
        for(int i = 0 ; i < out.length;i++){
            out[i]=new table(bm.bucks.get(i).count,features-1);
            out[i].initAll4Disp(""+i);
            blah.put(bm.bucks.get(i).name,out[i]);
        }
        //now i need to figure out how to move the reduced elements into the
        // new tables... i could make a set of tables indexed by features.
        //an add method in table would make this work a bit better. then it
        // needs an index to keep track of how many it has.
        //my hands are cold. programming outside sucks. to be continued.
        /*
        for(iterate through the big table){
            //i need to make the elements slightly smaller.

            blah.get(bigtableelement.guts[featurenum]).add
            (bigtableelement[featurenum]);
        }*/
        for(String[] sa : guts){
            String[] reduced = new String[catIndx-1];
            for(int i  = 0; i < sa.length; i++){//iterate through sa
                if(i==featureNum) {//logic to skip the String at feat#
                    i++;}

            }
        }


        return out;
    }

    //this will only take as many features as the table can handle
    //(columns-1)
    public void set(int row, String classification, String... features){
        guts[row][guts[row].length-1]=classification;
        for(int i = 0 ; i < guts[row].length-1;i++){
            guts[row][i]=features[i];
        }
    }
    public static double logOfBase(double num) {
        return Math.log(num) / Math.log(base);
    }
    public double tableEntropy(){
        bucketmanager bm = new bucketmanager();
        for(int i = 0 ; i < guts.length; i++){
            bm.add(guts[i][guts[i].length-1]);
        }

        //debugging block
//        System.out.println(bm);
//        double[] temp = bm.getCategoryHistogram();
//        String temp2 = "";
//        for(double d : temp){
//            temp2+=d+" ";
//        }
//        System.out.println(temp2);
        //debugging block end

        return ent(elements,bm.getCategoryHistogram());
    }
    public static double ent(double totalElements, double...
            weightedOutcomes){
        //weighted outcomes is a histogram of the possible outcomes.
        double sum = 0;
        for(int i = 0 ; i < weightedOutcomes.length;i++){
            double temp = (weightedOutcomes[i]/totalElements);
            sum-=temp*logOfBase(temp);
        }
        return sum;
    }

}
class bucketmanager{
    ArrayList<bucket> bucks;
    bucketmanager(){
        bucks = new ArrayList<>();
    }
    void add(String str){
        //boolean foundit = false;
        for(bucket b : bucks){
            if(b.name.equals(str)){
                b.count++;
                return;
            }
        }
        bucks.add(new bucket(str));
    }
    double[] getCategoryHistogram(){
        double[] out = new double[bucks.size()];
        for(int i = 0; i < out.length; i++){
            out[i]=bucks.get(i).count;
        }
        return out;
    }
    public String toString(){
        String out = "";
        for(bucket b : bucks){
            out+=b+"  ";
        }
        return out;
    }
    public int numBucks(){
        return bucks.size();
    }
}
class bucket{
    String name;
    int count;
    bucket(String str){
        name=str;
        count=1;
    }
    public String toString(){
        return name+":"+count;
    }
}