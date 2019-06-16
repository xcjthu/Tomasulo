import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


class ReservationPanel extends JPanel{
    int row = 10;
    int col = 7;
    GridLayout layout;
    JLabel[] labels;

    public ReservationPanel(){
        super();
        layout = new GridLayout(row, col);
        labels = new JLabel[row * col];

        String[] headers = {"Busy", "Op", "Vj", "Vk", "Qj", "Qk"};

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel("", JLabel.CENTER);
            labels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.add(labels[i]);
        }

        labels[0].setText("保留站状态");
        labels[1].setText("Busy");
        labels[2].setText("Op");
        labels[3].setText("Vj");
        labels[4].setText("Vk");
        labels[5].setText("Qj");
        labels[6].setText("Qk");

        for (int i = 0; i < 6; i ++) {
            labels[7 + i * 7].setText("Ars" + (i + 1));
        }

        for (int i = 0; i < 3; i ++) {
            labels[49 + i * 7].setText("Mrs" + (i + 1));
        }

        this.setLayout(layout);
    }

    public void updateValue(String[][] addValue, String[][] mrsValue){
        for(int r = 0; r < 6; ++ r){
            for (int c = 0; c < 6; ++ c){
                labels[8 + r * 7 + c].setText(addValue[r][c]);
            }
        }
        for (int r = 0; r < 3; ++ r){
            for (int c = 0; c < 6; ++ c){
                labels[50 + r * 7 + c].setText(mrsValue[r][c]);
            }
        }
    }
}

class BufferPanel extends JPanel {
    GridLayout gridLayout;
    JLabel[] labels;
    int row = 4, col = 3;

    public BufferPanel() {
        super();
        gridLayout = new GridLayout(row, col);

        labels = new JLabel[row * col];
        for (int i = 0; i < labels.length; i ++) {
            labels[i] = new JLabel("", JLabel.CENTER);
            labels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.add(labels[i]);
        }
        labels[0].setText("LoadBuffer");
        labels[1].setText("Busy");
        labels[2].setText("Address");
        for (int i = 0; i < 3; i ++) {
            labels[3 + i * 3].setText("LB" + (i + 1));
        }
        this.setLayout(gridLayout);
    }

    public void updateValue(String[][] loadbuffer){
        for (int r = 0; r < 3; ++ r){
            for (int c = 0; c < 2; ++ c){
                labels[4 + r * 3 + c].setText(loadbuffer[r][c]);
            }
        }
    }
}

class RegisterPanel extends JPanel {
    GridLayout gridLayout;
    JLabel[] labels;
    int row = 6, col = 17;

    public RegisterPanel() {
        super();
        gridLayout = new GridLayout(row, col);

        labels = new JLabel[row*col];
        for (int i = 0; i < labels.length; i ++) {
            labels[i] = new JLabel("", JLabel.CENTER);
            labels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.add(labels[i]);
        }
        labels[0].setText("Reg");
        labels[17].setText("State");
        labels[34].setText("Value");
        labels[51].setText("Reg");
        labels[68].setText("State");
        labels[85].setText("Value");
        for (int i = 0; i < 16; i++) {
            labels[1 + i].setText("F" + i);
        }
        for (int i = 16; i < 32; i++) {
            labels[52 + i - 16].setText("F" + i);
        }

        this.setLayout(gridLayout);
    }

    public void updateValue(){
        for(int i = 0; i < 16; ++ i){
            if (Registers.getInstance().regs.get(i).wait)
                labels[18 + i].setText(Registers.getInstance().regs.get(i).waitBuffer.bufferName);
            else
                labels[18 + i].setText("");
            labels[35 + i].setText("0x" + Integer.toHexString(Registers.getInstance().regs.get(i).value));
        }
        for(int i = 0; i < 16; ++ i){
            int num = i + 16;
            if (Registers.getInstance().regs.get(num).wait) {
                labels[69 + i].setText(Registers.getInstance().regs.get(num).waitBuffer.bufferName);
            }
            else{
                labels[69 + i].setText("");
            }
            labels[86 + i].setText("0x" + Integer.toHexString(Registers.getInstance().regs.get(num).value));
        }
    }
}

class ControlPanel extends JPanel {
    GridLayout gridLayout;
    JButton button, endButton;
    JLabel timerText, timer;

    public ControlPanel() {
        super();

        gridLayout = new GridLayout(1, 4);
        this.setLayout(gridLayout);

        button = new JButton("next");
        endButton = new JButton("end");
        timerText = new JLabel("cycle: ");
        timer = new JLabel("0");


        this.add(button);
        this.add(endButton);
        this.add(timerText);
        this.add(timer);
    }

    public void updateValue(int cycles){
        timer.setText("" + cycles);
    }
}

class InstructionPanel extends JPanel{
    JPanel leftPanel, rightPanel;
    JLabel[] instructionLabels;

    ArrayList<Inst> insts;
    int lineNum;
    InstructionPanel(ArrayList<Inst> instructions){
        super();
        insts = instructions;

        leftPanel = new JPanel();
        rightPanel = new JPanel();
        lineNum = instructions.size();
        if (lineNum > 100)
            lineNum = 100;

        leftPanel.setLayout(new GridLayout(lineNum+1, 1));
        rightPanel.setLayout(new GridLayout(lineNum+1, 3));
        instructionLabels = new JLabel[(lineNum + 1) * 4];


        for (int i = 0; i < instructionLabels.length; i++) {
            instructionLabels[i] = new JLabel("", JLabel.CENTER);
            instructionLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            if(i % 4 == 0)
                leftPanel.add(instructionLabels[i]);
            else
                rightPanel.add(instructionLabels[i]);
        }

        instructionLabels[1].setText("Issue");
        instructionLabels[2].setText("Exec");
        instructionLabels[3].setText("WB");
        for (int i = 0; i < lineNum; i++) {
            instructionLabels[4 + 4 * i].setText(instructions.get(i).strinst);
        }
        this.setLayout(new GridLayout(1, 2));
        this.add(leftPanel);
        this.add(rightPanel);
    }

    public void updateValue() {
        for(int i = 0; i < lineNum; i ++ ){
            if (insts.get(i).issuetime != -1)
                instructionLabels[5 + 4 * i].setText("" + insts.get(i).issuetime);
            if (insts.get(i).runingtime != -1)
                instructionLabels[6 + 4 * i].setText("" + insts.get(i).runingtime);
            if (insts.get(i).wbtime != -1)
                instructionLabels[7 + 4 * i].setText("" + insts.get(i).wbtime);
        }
    }
}

public class TomasuloGUI extends JFrame {
    ReservationPanel resPanel;
    BufferPanel bufferPanel;
    RegisterPanel registerPanel;
    ControlPanel controlPanel;
    InstructionPanel instructionPanel;
    JScrollPane scrollPane;

    Tomasulo tomasulo;


    public TomasuloGUI(Tomasulo _tomasulo){
        this.setLayout(null);
        this.setSize(1000, 1200);


        tomasulo = _tomasulo;

        resPanel = new ReservationPanel();
        bufferPanel = new BufferPanel();
        registerPanel = new RegisterPanel();
        controlPanel = new ControlPanel();
        instructionPanel = new InstructionPanel(tomasulo.instList);
        if (tomasulo.instList.size() > 100)
            instructionPanel.setPreferredSize(new Dimension(200, 25 * (100 + 1)));
        else
            instructionPanel.setPreferredSize(new Dimension(200, 25 * (tomasulo.instList.size() + 1)));
        scrollPane = new JScrollPane(instructionPanel);


        controlPanel.setBounds(100, 50, 400, 50);
        resPanel.setBounds(100, 100, 800, 200);
        bufferPanel.setBounds(100, 320, 300, 135);
        //instructionPanel.setBounds(400, 320, 500, 300);
        scrollPane.setBounds(400, 320, 500, 300);
        registerPanel.setBounds(100, 630, 900, 200);

        controlPanel.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tomasulo.finish())
                    controlPanel.button.setText("finish");
                else{
                    tomasulo.timeCycle();
                    updateDisplay();
                }
            }
        });

        controlPanel.endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long beginTime = System.currentTimeMillis();
                while (!tomasulo.finish())
                    tomasulo.timeCycle();
                controlPanel.button.setText("finish");
                updateDisplay();
                long endTime = System.currentTimeMillis();
                System.out.println(endTime - beginTime);;
            }
        });



        this.add(resPanel);
        this.add(bufferPanel);
        this.add(registerPanel);
        this.add(controlPanel);
        this.add(registerPanel);
        //this.add(instructionPanel);
        this.add(scrollPane);
        this.setVisible(true);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void updateDisplay(){
        resPanel.updateValue(tomasulo.ars.getBufferStatus(), tomasulo.mrs.getBufferStatus());
        bufferPanel.updateValue(tomasulo.loaders.getBufferStatus());
        registerPanel.updateValue();
        instructionPanel.updateValue();
        controlPanel.updateValue(tomasulo.cycles - 1);
    }




}
