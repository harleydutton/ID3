import java.util.ArrayList;

class BucketManager {
    ArrayList<Bucket> bucks;
    BucketManager(){
        bucks = new ArrayList<>();
    }
    void add(String str){
        //boolean foundit = false;
        for(Bucket b : bucks){
            if(b.name.equals(str)){
                b.count++;
                return;
            }
        }
        bucks.add(new Bucket(str));
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
        for(Bucket b : bucks){
            out+=b+"  ";
        }
        return out;
    }
    public int numBucks(){
        return bucks.size();
    }
}