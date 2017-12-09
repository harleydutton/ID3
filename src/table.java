import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Table {
    private String[][] contents;
    private int elements;
    private int categoryIndex;
    private int features;
    private int addIndex;
    private String choiceOrClass;
    static String rowsep = "##############";
    ArrayList<String> featureLabels;

    //todo
    //make the decision tree

    public Table(int elements, int features) {
        this.features = features;
        this.elements = elements;

        categoryIndex = features + 1;
        featureLabels = new ArrayList<String>();

        contents = new String[elements][features + 1];
        for (String[] row : contents) {
            for (String cell : row) {
                cell = "";
            }
        }
    }

    public static void main(String[] args) {
        //data input
        Table t = new Table(10,3);
        String[] fl = new String[]{"quant","color","shape"};
        t.featureLabels = new ArrayList<String>(Arrays.asList(fl));
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
        for(Table temp : t.makeSubTables(t.highestGainFeature())){
            System.out.println(temp);
            System.out.println(rowsep);
        }
        System.out.println(rowsep);

        Node n = new Node("my Node", t);
        n.split();
        System.out.println(n);
    }

    public int highestGainFeature() {
        int bestIndex = -1;
        double bestValue = -1;

        for (int i = 0; i < features; i++) {
            if (gain(i) > bestValue) {
                bestIndex = i;
                bestValue = gain(i);
            }
        }
        return bestIndex;
    }

    public boolean shouldSplit() {
        return !(outOfFeats() || cantGainInfo() || allSameClass());
    }

    private boolean cantGainInfo() {
        return gain(highestGainFeature()) == 0.0;
    }

    private boolean outOfFeats() {
        return features == 0;
    }

    private ArrayList<String> catsAsSet() {
        ArrayList<String> out = new ArrayList<>();
        String[] categories = getCategories();
        boolean inSet;

        for (String category : categories) {
            inSet = false;
            for (String o : out) {//iterates through out
                if (o.equals(category)) {
                    inSet = true;
                }
            }

            if (!inSet) {
                out.add(category);
            }
        }

        return out;
    }

    public double gain(int featureIndex) {
        double out = this.tableEntropy();
        Table[] subtables = this.makeSubTables(featureIndex);

        for (Table t : subtables) {
            double temp = t.elements * t.tableEntropy() / this.elements;
            out -= temp;
        }

        return out;
    }

    public void add(String[] strs) {
        if (strs.length != categoryIndex) {
            System.out.println("the str[] was not the right size to be added.");
            System.out.println("this is the str[]:");

            for (String s : strs) {
                System.out.print(s + " ");
            }

            System.out.println("this is the table:\n" + this);
        }
        if (addIndex >= contents.length) {
            System.out.println("this table isnt large enough to hold another element");
        }
        contents[addIndex++] = strs;
    }

    public String[] getFeatureLabels(int featureIndex) {
        DataSet buffer = new DataSet();

        for (int i = 0; i < contents.length; i++) {
            buffer.add(contents[i][featureIndex]);
        }

        return buffer.getStringArray();
    }

    @Override
    public String toString() {
        String out = "";

        if (choiceOrClass != null) {
            out += choiceOrClass.toUpperCase() + "   LEAF:" + allSameClass() + "\n";
        }

        int[] colMax = new int[contents[0].length];

        for (int row = 0; row < contents.length; row++) {
            for (int column = 0; column < contents[0].length; column++) {
                colMax[column] = Math.max(colMax[column], contents[row][column].length());
                colMax[column] += 2;

                String padding = new String(new char
                        [colMax[column]-contents[row][column].length()])
                        .replace("\0"," ");

                out += contents[row][column] + padding;
            }
            out += "\n";
        }

        out += catsAsSet();

        return out;
    }

    public String[] getCategories() {
        String[] out = new String[elements];

        for (int row = 0; row < contents.length; row++) {
            int last = contents[row].length - 1;
            out[row] = contents[row][last];
        }

        return out;
    }

    private boolean allSameClass() { //part of recursive check for making data on tree
        String[] categories = getCategories();

        if (categories.length == 0) { //this should never happen
            System.out.println("ERROR: allSameClass() called on a table with " +
                    "0 elements");
            return true;
        }

        // enssure all elements match first
        String first = categories[0];
        for (String s : categories) {
            if (!s.equals(first)) {
                return false;
            }
        }
        return true;
    }

    public Table[] makeSubTables(int featureIndex) {
        if (featureIndex >= features) {
            System.out.println("error! not enough features. feat#: " +
                               featureIndex + "\n" + this);
            System.out.println("returning null");
            return null;
        }

        //figure out how many subtables we need.
        DataSet bufferSet = new DataSet();
        for (String[] row : contents){
            bufferSet.add(row[featureIndex]);
        }

        int size = bufferSet.size();
        Table[] out = new Table[size];
        HashMap<String, Table> subtables = new HashMap<>();

        for(int i = 0; i < out.length; i++) {
            out[i] = new Table(bufferSet.data.get(i).quantity, features-1);
            out[i].choiceOrClass = bufferSet.data.get(i).name;
            out[i].featureLabels = new ArrayList<String>(featureLabels);
            out[i].featureLabels.remove(featureIndex);
            subtables.put(bufferSet.data.get(i).name, out[i]);
        }

        for (String[] row : contents) {
            String[] reduced = new String[categoryIndex - 1];

            for (int i = 0, r = 0; i < row.length; i++, r++) {
                if (i == featureIndex) { //skip the String at feat#
                    i++;
                }
                reduced[r] = row[i]; //fill em up.
            }

            String tabName = row[featureIndex]; //i think this doesnt matter now
            subtables.get(tabName).add(reduced); //add each element to its subtable
        }
        return out;
    }

    //this will only take as many features as the table can handle
    //(columns-1)
    private void setRow(int row, String classification, String... features) {
        int last = contents[row].length - 1; // column index
        contents[row][last] = classification;

        for (int column = 0; column < last; column++) {
            contents[row][column] = features[column];
        }
    }

    public double tableEntropy() {
        DataSet bufferSet = new DataSet();

        for (int row = 0; row < contents.length; row++) {
            int last = contents[row].length - 1;
            bufferSet.add(contents[row][last]);
        }

        // histogram of the possible weighted outcomes.
        int[] outcomes = bufferSet.getHistogram();

        double sum = 0;
        for (int i = 0; i < outcomes.length; i++) {
            double temp = outcomes[i] / (double)elements;
            sum -= temp * (Math.log(temp) / Math.log(2.0));
        }

        return sum;
    }

    private class DataSet {
        private class Datum {
            private String name;
            private int quantity;

            private Datum(String name) {
                this.name = name;
                quantity = 1;
            }

            @Override
            public String toString() {
                return name + ":" + quantity;
            }
        }

        private ArrayList<Datum> data;

        private DataSet() {
            data = new ArrayList<Datum>();
        }

        private void add(String name) {
            for (Datum iterator : data) {
                if (iterator.name.equals(name)) {
                    iterator.quantity++;
                    return;
                }
            }

            data.add(new Datum(name));
        }


        private int size() {
            return data.size();
        }

        @Override
        public String toString() {
            String buffer = "";

            for (Datum iterator : data) {
                buffer += iterator.name + "  ";
            }

            return buffer;
        }

        private String[] getStringArray() {
            String[] buffer = new String[data.size()];

            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = data.get(i).name;
            }

            return buffer;
        }

        private int[] getHistogram() {
            int[] buffer = new int[data.size()];

            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = data.get(i).quantity;
            }

            return buffer;
        }
    }
}
