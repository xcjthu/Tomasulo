import java.math.BigInteger;
import java.util.ArrayList;

class Operator{
    /* 操作数，有可能是寄存器，有可能是直接的数值 */
    public static final int REG = 0;
    public static final int INT = 1;

    public int numid;
    public int regid;
    public String value;

    public Operator(String opera){
        if(opera.charAt(0) == 'F') {
            numid = REG;
            regid = Integer.parseInt(opera.substring(1));
        }else{
            numid = INT;
            // System.out.println(opera);
            // regid = Integer.parseInt(opera.substring(2), 16);
            regid = new BigInteger(opera.substring(2), 16).intValue();

        }
        value = opera;
    }

    public int getRegid(){
        if (numid != REG)
            return -1;
        return regid;
    }
}

public class Inst {
    public static final int ADD = 0;
    public static final int SUB = 1;
    public static final int MUL = 2;
    public static final int DIV = 3;
    public static final int LD = 4;
    public static final int JUMP = 5;

    static final String[] InstName = {"Add", "Sub", "Mul", "Div", "LD", "Jump"};
    //public static final int[] CYCLE = {3, 3, 12, 40, 3, 1};
    public static final int[] CYCLE = {3, 3, 4, 4, 3, 1};


    public int opid;
    public ArrayList<Operator> operators = new ArrayList<>();

    public boolean jump;

    private void initOperators(String[] lll){

        for (int i = 1; i < lll.length; i++) {
            operators.add(new Operator(lll[i]));
        }
    }
    String strinst;

    public int issuetime = -1;
    public int runingtime = -1;
    public int wbtime = -1;

    public Inst(String inst){
        // System.out.println(inst);
        strinst = inst;
        String[] tmp = inst.split(",");
        switch (tmp[0]){
            case "ADD":
                opid = ADD;
                break;
            case "MUL":
                opid = MUL;
                break;
            case "SUB":
                opid = SUB;
                break;
            case "DIV":
                opid = DIV;
                break;
            case "LD":
                opid = LD;
                break;
            case "JUMP":
                opid = JUMP;
                break;
            default:
                System.out.println("instruction error: No such instruction");
        }
        initOperators(tmp);
    }

    public Inst copy(){
        if (issuetime == -1){
            return this;
        }
        else{
            Inst copyInst = new Inst(strinst);
            return copyInst;
        }
    }
}
