import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class table {

    private String[][] guts;
    private int elements;
    private int catIndx;
    private int features;
    private int addIndex;
    private String choiceOrClass;
    static String rowsep = "##############";
    ArrayList<String> featureLables;


    //todo
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
        featureLables=new ArrayList<>();
    }
    public static void main(String[] args){

        //data input
        table t = new table(10,3);
        String[] fl = new String[]{"quant","color","shape"};
        t.featureLables = new ArrayList<String>(Arrays.asList(fl));
        t.add(new String[]{"1","red","square","A"});
        t.add(new String[]{"1","blue","square","B"});
        t.add(new String[]{"1","red","circle","C"});
        t.add(new String[]{"1","blue","circle","D"});
        t.add(new String[]{"2","red","triangle","A"});
        t.add(new String[]{"2","blue","triangle","B"});
        t.add(new String[]{"2","green","triangle","C"});
        t.add(new String[]{"1","green","square","A"});
        t.add(new String[]{"2","red","circle","B"});
        t.add(new String[]{"1","red","triangle","C"});

        //info output
        System.out.println(t);
        System.out.println("entropy of t:"+t.tableEntropy());
        System.out.println("highest gain feature is:"+t.highestGainFeature());
        System.out.println("subtables split on feature "+t.highestGainFeature
                ()+":");
        for(table temp : t.makeSubTables(t.highestGainFeature())){
            System.out.println(temp);
            System.out.println(rowsep);
        }
        System.out.println(rowsep);

        node n = new node("my node", t);
        n.split();
        System.out.println(n);

    }
    public int highestGainFeature(){
        int bestIndex = -1;
        double bestValue = -1;
        for(int i  = 0 ; i < features; i++){
            if(gain(i)>bestValue){
                bestIndex=i;
                bestValue=gain(i);
            }
        }
        return bestIndex;
    }
    boolean cantGainInfo(){
        return gain(highestGainFeature())==0.0;
    }
    boolean outOfFeats(){
        return features==0;
    }
    boolean shouldSplit(){
        return !(outOfFeats()||cantGainInfo()||allSameClass());
    }

    public ArrayList<String> catsAsSet(){
        ArrayList<String> out = new ArrayList<>();
        String[] temp = getCats();
        boolean inSet;
        for(String t : temp){//iterates through temp
            inSet=false;
            for(String o : out) {//iterates through out
                if(o.equals(t))inSet=true;
            }
            if(!inSet)out.add(t);
        }
        return out;
    }

    public double gain(int featNum){
        double out = this.tableEntropy();
        table[] subTables = this.makeSubTables(featNum);
        for(table t : subTables){
            double temp = t.elements*t.tableEntropy()/this.elements;
            out-=temp;
        }
        return out;
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
    public String[] getFeatLables(int featNum){
        bucketmanager bm = new bucketmanager();
        for(int i =0 ; i < guts.length;i++){
            bm.add(guts[i][featNum]);
        }
        return bm.handOverTheStrings();
    }

    public String toString(){
        String out = "";
        if(choiceOrClass!=null)out+=choiceOrClass.toUpperCase()
                +"   LEAF:"+allSameClass()+"\n";

        int[] colMax = new int[guts[0].length];
        for(int i= 0 ; i < guts.length; i++){//traverses rows
            for(int j = 0 ; j<guts[0].length;j++) {//traverses columns
                colMax[j]=Math.max(colMax[j],guts[i][j].length());

            }
        }
        for(int i = 0 ; i < colMax.length;i++){
            colMax[i]+=2;
        }

        for(int i= 0 ; i < guts.length; i++){//traverses rows
            for(int j = 0 ; j<guts[0].length;j++) {//traverses columns

                String padding = padding=new String(new
                        char[colMax[j]-guts[i][j]
                        .length()]).replace("\0"," ");
                out+=guts[i][j]+padding;
            }

            out+="\n";
        }
        out+=catsAsSet();

        return out;
    }
    String[] getCats(){
        String[] out = new String[elements];
        for(int r = 0; r < guts.length; r++){//r=row
            out[r]=guts[r][guts[r].length-1];
        }
        return out;
    }

    boolean allSameClass(){//part of recursive check for making nodes on tree
        String[] cats = getCats();
        if(cats.length==0){
            System.out.println("ERROR: allSameClass() called on a table with " +
                    "0 elements");
            return true;//this should never happen
        }
        String first = cats[0];//if any dont match this return false;
        for(String s : cats){
            if(!s.equals(first))return false;
        }
        return true;
    }

    public table[] makeSubTables(int featureNum){
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
            out[i].choiceOrClass=bm.bucks.get(i).name;
            out[i].featureLables=new ArrayList(featureLables);
            out[i].featureLables.remove(featureNum);
            blah.put(bm.bucks.get(i).name,out[i]);
        }
        for(String[] sa : guts){//this is the big table. blah is the subtables
            String[] reduced = new String[catIndx-1];
            int r = 0;
            for(int i  = 0; i < sa.length; i++){//iterate through sa (bigT)
                if(i==featureNum) {//logic to skip the String at feat#
                    i++;}
                reduced[r]=sa[i];//fill em up.
                r++;//keeps track of the skip
            }
            String tabName = sa[featureNum];//i think this doesnt matter now
            blah.get(tabName).add(reduced);//add each element to its subtable
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
        return Math.log(num) / Math.log(2.0);
    }
    public double tableEntropy(){
        bucketmanager bm = new bucketmanager();
        for(int i = 0 ; i < guts.length; i++){
            bm.add(guts[i][guts[i].length-1]);
        }
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
    public String[] handOverTheStrings(){
        String[] out = new String[bucks.size()];
        for(int i = 0 ; i < out.length; i++){
            out[i]=bucks.get(i).name;
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
