class Bucket {
    String name;
    int count;
    Bucket(String str){
        name=str;
        count=1;
    }
    public String toString(){
        return name+":"+count;
    }
}