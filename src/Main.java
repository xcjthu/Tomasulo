public class Main {

    public static void main(String[] args) {
        try {


            Tomasulo tomasulo = new Tomasulo("experiment2/test0.nel");

            TomasuloGUI gui = new TomasuloGUI(tomasulo);

            // while (tomasulo.cycles < 40) {
            // while (tomasulo.lastInst == null || !tomasulo.checkFree()){
            //while (! tomasulo.finish()){
            //    tomasulo.timeCycle();
            //    gui.updateDisplay();
            //    Thread.sleep(2000);
            //}
            //for (Inst inst: tomasulo.instList) {
            //    System.out.println(inst.strinst + "\tissue time: " + inst.issuetime + "\trunning time: " + inst.runingtime + "\twb time: " + inst.wbtime);
            //}

        }catch (java.io.IOException e){
            e.printStackTrace();
            System.out.println("File not found");
        }
        //catch (InterruptedException e) {
        //    e.printStackTrace();
        //}


    }
}
