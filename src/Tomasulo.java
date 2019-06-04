import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


public class Tomasulo {
    public ArrayList<Inst> instList = new ArrayList<>();

    BufferedReader fin;

    public class instStatus{
        public int issue;
        public int exec;
        public int wr;
    }

    public ArrayList<instStatus> instStatusTab = new ArrayList<>();

    public Tomasulo(String filename, int loader_num) throws FileNotFoundException {
        fin = new BufferedReader(new FileReader(filename));
    }

    public void nextTimeCycle(){

    }


}
