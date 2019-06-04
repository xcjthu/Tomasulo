

public class Components{
    /* 运算器，可以用于加减运算，乘除运算 */
    public String name;
    public boolean busy = false;
    public int op;

    public int time;
    /*
    static public final int vj = 0;
    static public final int vk = 1;
    static public final int qj = 2;
    static public final int qk = 3;
    */
    class RegStatus{
        public int id;
        public boolean ready;
    }


    int tagetId;
    boolean running;

    public Components(String _name){
        name = _name;
    }

    public void issueInst(Inst inst){
        busy = true;
        op = inst.opid;
        tagetId = inst.operators.get(0).regid;
        regJId = inst.operators.get(1).regid;
        regKId = inst.operators.get(2).regid;
        time =
    }

}
