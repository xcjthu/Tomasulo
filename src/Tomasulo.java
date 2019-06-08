import java.io.*;
import java.util.ArrayList;


public class Tomasulo {
    public ArrayList<Inst> instList = new ArrayList<>();

    BufferedReader fin;

    Inst nowInst;
    Inst lastInst = null;

    int pc = 0;

    int cycles = 1;

    // 三个运算主要器件
    ComponentsManager ars = new ComponentsManager(6, 3, "ADDs"); //为加法运算器创建的，可以接受 ADD/SUB/JUMP三条指令
    ComponentsManager mrs = new ComponentsManager(3, 2, "MULs"); //为乘除法运算器创建的，可以接受 MUL/DIV 三条指令
    LoaderManager loaders = new LoaderManager(3, 2, "Loaders"); // loader



    public Tomasulo(String filename) throws IOException {
        fin = new BufferedReader(new FileReader(filename));
        String line  = fin.readLine();
        while (line != null){
            instList.add(new Inst(line));
            line = fin.readLine();
        }
        nowInst = instList.get(pc);
        pc ++;
    }

    public Inst nextInst(){
        /*
        try {
            String line = fin.readLine();
            if (line == null)
                return null;
            Inst ans = new Inst(line);
            instList.add(ans);
            return ans;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("read inst error!!!");
        }
        return null;
        */

        if (pc < instList.size()) {
            Inst ans = instList.get(pc).copy();
            pc++;
            return ans;
        }
        else {
            lastInst = nowInst;
            return null;
        }
    }


    boolean jumpwaiting = false;
    public void issueInst(){
        if (jumpwaiting){
            if(nowInst.runingtime != -1 && cycles > nowInst.runingtime){ //判断jump指令有没有执行完


                jumpwaiting = false;
                System.out.println(nowInst.strinst + " jumpjump " + nowInst.jump);
                if (nowInst.jump) {
                    pc = pc + nowInst.operators.get(2).regid - 1;
                    System.out.println(nowInst.operators.get(2).regid);
                    System.out.println("jump!!");
                }
                nowInst = nextInst();
            }else
                return;
        }

        if (nowInst == null)
            return;
        boolean result = false;
        switch (nowInst.opid){
            case Inst.JUMP:
                jumpwaiting = true;
                System.out.println("begin waiting");
                // System.out.println("jumpReg0 " + nowInst.operators.get(0).regid);
            case Inst.ADD:
            case Inst.SUB:
                result = ars.issueInst(nowInst, cycles);
                break;
            case Inst.DIV:
            case Inst.MUL:
                result = mrs.issueInst(nowInst, cycles);
                break;
            case Inst.LD:
                result = loaders.issueInst(nowInst, cycles);
                break;
        }
        if (result) {
            //if (nowInst.issuetime == -1)
                nowInst.issuetime = cycles;

            if (! jumpwaiting)
                nowInst = nextInst();
        }
    }

    public void match(){
        ars.match();
        mrs.match();
        loaders.match();
    }

    public void run(int cycles){
        ars.timeCycleRun(cycles);
        mrs.timeCycleRun(cycles);
        loaders.timeCycleRun(cycles);
    }

    public void checkReady(){
        ars.checkReady();
        mrs.checkReady();
        loaders.checkReady();
    }

    public void writeBack(){
        ars.writeBack(cycles);
        mrs.writeBack(cycles);
        loaders.writeBack(cycles);
    }

    public void timeCycle(){
        System.out.println("\n========== In time cycle " + cycles + " pc:" + pc + "==========");
        //System.out.println("In time cycle: " + cycles);
        /*
        issueInst();
        ars.timeCycle(cycles);
        mrs.timeCycle(cycles);
        loaders.timeCycle(cycles);
        cycles ++;
        */
        issueInst();
        match();
        run(cycles);
        checkReady();
        // System.out.println("after running");
        match();
        writeBack();

        cycles ++;
    }

    boolean checkFree(){
        return ars.checkFree() && mrs.checkFree() && loaders.checkFree();
    }

    boolean finish(){
        return lastInst != null && checkFree();
    }
}
