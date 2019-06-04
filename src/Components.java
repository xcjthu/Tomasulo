

public class Components{
    public String name;
    public boolean busy = false;
    public int op;

    public int time;

    static public final int vj = 0;
    static public final int vk = 1;
    static public final int qj = 2;
    static public final int qk = 3;

    public Components(String _name){
        name = _name;
    }

    public void runInst(Inst inst){
        busy = true;
        op = inst.opid;
        

    }

}
