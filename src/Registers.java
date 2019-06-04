import java.util.ArrayList;

public class Registers{
    /* 寄存器状态，是在等待还是已经有数值 */
    public class Reg{
        static public final int LOADER = 0; // 等待loader出值
        static public final int COM = 1;    // 等待运算器出值

        public int comp;
        public int number;
        public boolean wait = false;

        public Reg(){}

        public void setStatus(boolean _wait, int _comp = -1, int _num = -1){
            wait = _wait;

            comp = _comp;
            number = _num;

        }
    }

    public ArrayList<Reg> regs;

    private static Registers ins = new Registers(32);

    private Registers(int num){
        for (int i = 0; i < num; i++) {
            regs.add(new Reg());
        }
    }

    public Registers getInstance(){
        return ins;
    }

    public void setStatus(String _status, int index) {
        regs.get(index).setStatus(_status);
    }

    public boolean busy(int index){
        return regs.get(index).wait;
    }
}
