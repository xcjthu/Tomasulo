import java.util.ArrayList;
import java.util.LinkedList;

class Buffer{
    public Inst inst = null;
    // public boolean ready = false;
    public boolean occupied = false;
    public boolean completed = false;
    public boolean running = false;
    boolean waitWB = false;

    String bufferName;

    class RegQ{
        public boolean ready;
        public Buffer buffer = null;
        int value;
    }

    public RegQ qj = new RegQ();
    public RegQ qk = new RegQ();

    public int targetRegId;
    int tagetValue;

    void setBufferName(String _name){
        bufferName = _name;
    }

    public void issueInst(Inst _inst){
        inst = _inst;
        occupied = true;
        completed = false;
        running = false;
        waitWB = false;


        int qjid = inst.operators.get(1).getRegid();
        qj.ready = ! Registers.getInstance().getWait(qjid);
        if (! qj.ready)
            qj.buffer = Registers.getInstance().getBuffer(qjid);
        else
            qj.value = Registers.getInstance().getValue(qjid);


        if (inst.opid == Inst.JUMP){
            // 利用qk的value 来表示jump指令第一个运算数的值
            qk.value = inst.operators.get(0).regid;

            qk.ready = true;
            return;
        }

        int qkid = inst.operators.get(2).getRegid();
        qk.ready = ! Registers.getInstance().getWait(qkid);
        if (! qk.ready)
            qk.buffer = Registers.getInstance().getBuffer(qkid);
        else
            qk.value = Registers.getInstance().getValue(qkid);

        targetRegId = inst.operators.get(0).getRegid();
        Registers.getInstance().setWait(targetRegId, this);

    }

    public boolean checkReady(){
        if (! qj.ready) {
            qj.ready = qj.buffer.completed;
            qj.value= qj.buffer.tagetValue;
        }
        if (! qk.ready) {
            qk.ready = qk.buffer.completed;
            qk.value= qk.buffer.tagetValue;
        }
        // System.out.println(inst.strinst);
        // System.out.print(" qj.ready: " + qj.ready);
        // System.out.println(" qk.ready: " + qk.ready + "\t");
        if (qj.ready && qk.ready) {
            if (inst.opid == Inst.JUMP)
                inst.jump = (qj.value == qk.value);
        }


        return qj.ready && qk.ready;
    }

    public void complete(int cycles){ // write back

        completed = true;
        int ans = -1;
        switch (inst.opid){
            case Inst.ADD:
                ans = qj.value + qk.value;
                break;
            case Inst.SUB:
                ans = qj.value - qk.value;
                break;
            case Inst.MUL:
                ans = qj.value * qk.value;
                break;
            case Inst.DIV:
                if (qk.value == 0)
                    ans = qj.value;
                else
                    ans = qj.value / qk.value;
                break;
            case Inst.LD:
                ans = qj.value;
                break;
            case Inst.JUMP:
                // System.out.println("Jump write back qj.value: " + qj.value + "\t qk.value: " + qk.value);
                break;
        }
        tagetValue = ans;

        System.out.println("[complete inst] " + inst.strinst + " value: " + tagetValue);

        if (inst.opid != Inst.JUMP && Registers.getInstance().getBuffer(targetRegId) == this){

            Registers.getInstance().setArrive(targetRegId, ans);
        }
        occupied = false;
        running = false;

        //if (inst.wbtime == -1)
            inst.wbtime = cycles;

        inst = null;
        waitWB = false;
    }

    public String[] getBufferStatus(){
        String[] ans = new String[6];
        if (occupied){
            ans[0] = "Yes";
            ans[1] = Inst.InstName[inst.opid];
            if (qj.ready){
                ans[2] = "" + qj.value;
                ans[4] = "";
            }
            else{
                ans[2] = "";
                ans[4] = qj.buffer.bufferName;
            }
            if (qk.ready){
                ans[3] = "" + qk.value;
                ans[5] = "";
            }
            else{
                ans[3] = "";
                ans[5] = qk.buffer.bufferName;
            }
        }
        else {
            ans[0] = "No";
            for (int i = 1; i < 5; ++ i)
                ans[i] = "";
        }
        return ans;
    }
}


class Calculator{
    public Inst inst = null;
    public boolean busy = false;
    Buffer buffer;

    int time;

    public void getInst(Buffer _buffer){
        buffer = _buffer;
        inst = _buffer.inst;
        time = Inst.CYCLE[inst.opid];
        busy = true;
        buffer.running = true;

        if(inst.opid == Inst.DIV && buffer.qk.value == 0){
            time = 1;
        }

    }

    public void run(final int cycles){
        if (busy){

            if (cycles == inst.issuetime)
                return;
            /*
            if (time == 0){
                //写回操作
                buffer.complete(cycles);
                busy = false;
            }
            else{
            */

            time --;
            if (time == 0) {
                //if (inst.runingtime == -1)
                    inst.runingtime = cycles;
                busy = false;
                buffer.waitWB = true;
                buffer = null;
                inst = null;

                //if (inst.opid == Inst.JUMP) {
                //    inst.jump = (qj.value == qk.value);
                //    System.out.println(qj.value + " jumptest " + qk.value + "  " + inst.jump);
                //}
            }
            //}
        }
    }
}


public class ComponentsManager {
    public ArrayList<Buffer> buffers;
    public ArrayList<Calculator> calculators;

    // public LinkedList<Integer> queue; //等待队列
    String name;

    public ComponentsManager(int bufferNum, int calNum, String _name){
        buffers = new ArrayList<>();
        calculators = new ArrayList<>();
        name = _name;

        for (int i = 0; i < bufferNum; i ++) {
            buffers.add(new Buffer());
            buffers.get(i).setBufferName(name + i);
        }
        for (int j = 0; j < calNum; j ++){
            calculators.add(new Calculator());
        }


    }

    public boolean issueInst(Inst inst, int cycles){
        for (int i = 0; i < buffers.size(); ++ i){
            if (buffers.get(i).occupied)
                continue;
            System.out.println("[issue Inst]\t" + name + "\t" + inst.strinst + "\t\tto buffer " + i);
            buffers.get(i).issueInst(inst);
            return true;
        }
        return false;
    }


    public void match(){
        int j = 0;
        for (int i = 0; i < calculators.size(); ++ i){
            //System.out.println("ggg");
            if (calculators.get(i).busy)
                continue;
            for(; j < buffers.size(); ++ j){

                if (buffers.get(j).occupied && ! buffers.get(j).running && buffers.get(j).checkReady()) {
                    System.out.println("[MATCH]\t" + name + "\tmatch calculator " + i + " buffer " + j);
                    calculators.get(i).getInst(buffers.get(j));
                    j ++;
                    break;
                }
            }
        }
    }

    public void checkReady(){

        for(int j = 0; j < buffers.size(); ++ j){
            if (buffers.get(j).occupied && !buffers.get(j).running){
                buffers.get(j).checkReady();
            }
        }

    }

    public void timeCycleRun(int cycles){

        // 所有运算器都进行运算
        for(int i = 0; i < calculators.size(); ++ i)
            calculators.get(i).run(cycles);

    }

    public void writeBack(int cycles){
        for (Buffer buf: buffers) {
            if (buf.waitWB && buf.inst.runingtime + 1 == cycles)
                buf.complete(cycles);
        }
    }

    public boolean checkFree(){
        boolean free = true;
        for (Buffer buf: buffers) {
            if (buf.occupied) {
                free = false;
                break;
            }
        }
        return free;
    }

    public String[][] getBufferStatus(){
        String[][] ans = new String[buffers.size()][6];
        for(int i = 0; i < buffers.size(); ++ i){
            ans[i] = buffers.get(i).getBufferStatus();
        }
        return ans;
    }

}
