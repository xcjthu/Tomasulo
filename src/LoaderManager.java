class LoaderBuffer extends Buffer{
    @Override
    public boolean checkReady() {
        return true;
    }

    @Override
    public void issueInst(Inst _inst) {
        inst = _inst;
        occupied = true;
        completed = false;
        running = false;

        targetRegId = inst.operators.get(0).getRegid();
        Registers.getInstance().setWait(targetRegId, this);

        qk.ready = true;
        qj.ready = true;
        qj.value = inst.operators.get(1).regid;
    }

    @Override
    public String[] getBufferStatus() {
        String[] ans = new String[2];
        if (occupied){
            ans[0] = "Yes";
            ans[1] = inst.operators.get(1).value;
        }
        else{
            ans[0] = "No";
            ans[1] = "";
        }
        return ans;
    }

    /*
    @Override
    public void complete() {
        super.complete();
    }
    */
}


public class LoaderManager extends ComponentsManager{
    public LoaderManager(int bufferNum, int calNum, String _name){
        super(0, calNum, _name);
        // buffers.clear();
        for (int i = 0; i < bufferNum; ++ i){
            buffers.add(new LoaderBuffer());
            buffers.get(i).setBufferName("LD" + i);
        }
    }
}
