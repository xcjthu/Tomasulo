import java.util.ArrayList;

class Loader{
    public boolean busy = false;
    public String addr;

    public void setAddr(String _addr){
        addr = _addr;
    }

    public void changeStatus(){
        busy = !busy;
    }
}

public class LoaderManager{
    public ArrayList<Loader> loaders = new ArrayList<>();

    public boolean full = false;

    public LoaderManager(int num){
        for (int i = 0; i < num; i++) {
            loaders.add(new Loader());
        }
    }
}