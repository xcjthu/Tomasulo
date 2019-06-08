import java.util.ArrayList;

public class Registers{
    /* 寄存器状态，是在等待还是已经有数值 */
    class Reg{
        // static public final int LOADER = 0; // 等待loader出值
        // static public final int COM = 1;    // 等待运算器出值


        public boolean wait = false;
        public Buffer waitBuffer;
        int value;

        public Reg(){}

        public void setWait(Buffer buffer){
            wait = true;
            waitBuffer = buffer;
            // comp = _comp;
            // number = _num;
        }

        public void setArrive(int _value){
            wait = false;
            value = _value;
            // comp = -1;
            // number = -1;
        }
    }

    private static Registers ins = new Registers(32);

    public ArrayList<Reg> regs = new ArrayList<>();
    private Registers(int num){
        for (int i = 0; i < num; i++) {
            regs.add(new Reg());
        }
    }

    static public Registers getInstance(){
        return ins;
    }

    //public void setStatus(String _status, int index) {
    //    regs.get(index).setStatus(_status);
    //}
    public void setWait(int index, Buffer buffer){
        // regs.get(index).setWait(_comp, _num);
        regs.get(index).setWait(buffer);
    }

    public void setArrive(int index, int value){
        regs.get(index).setArrive(value);
    }


    public boolean getWait(int index){
        return regs.get(index).wait;
    }

    public Buffer getBuffer(int index){
        return regs.get(index).waitBuffer;
    }

    public int getValue(int index){
        return regs.get(index).value;
    }
}
