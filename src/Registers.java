import java.util.ArrayList;

public class Registers{
    public class Reg{
        public String status;
        public boolean wait = false;

        public Reg(){}

        public void setStatus(String _status){
            status = _status;
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
