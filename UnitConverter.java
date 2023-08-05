package awtandswing;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnitConverter{
    private JFrame frame;
    private JLabel inputLabel, outputLabel;
    private JTextField input;
    private JTextField output;
    private JComboBox<String> fromUnit;
    private JComboBox<String> toUnit;
    private JButton convert;
    protected void initComponents(){
        frame = new JFrame("Unit Convertor");
        frame.setSize(700,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        input = new JTextField("0.0");
        inputLabel = new JLabel("Input: ");
        outputLabel = new JLabel("Output: ");
        output = new JTextField("0.0");
        output.setEditable(false);
        fromUnit = new JComboBox<String>(new String[]{"Meters", "Feet", "Inch", "Centimeter"});
        toUnit = new JComboBox<String>(new String[]{"Meters", "Feet", "Inch", "Centimeter"});
        convert = new JButton("Convert");


        inputLabel.setBounds(20,50,100,20);
        input.setBounds(150,50,200,20);
        fromUnit.setBounds(370,50,100,20);
        outputLabel.setBounds(20,80,100,20);
        output.setBounds(150,80,200,20);
        toUnit.setBounds(370,80,100,20);
        convert.setBounds(150,120,100,20);
        frame.add(inputLabel);
        frame.add(input);
        frame.add(outputLabel);
        frame.add(output);
        frame.add(fromUnit);
        frame.add(toUnit);
        frame.add(convert);

        frame.setVisible(true);

        convert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fromUnitType = fromUnit.getSelectedItem().toString();
                String toUnitType = toUnit.getSelectedItem().toString();
                double inval = Double.parseDouble(input.getText());
                double outval = 0.0;
                if(fromUnitType.equals("Meters")){
                    if(toUnitType.equals("Meters")){
                        outval = inval;
                    } else if (toUnitType.equals("Feet")) {
                        outval = inval*3.28084;
                    } else if (toUnitType.equals("Inch")) {
                        outval = inval*39.3701;
                    }else{
                        outval = inval*100;
                    }
                } else if (fromUnitType.equals("Feet")) {
                    if(toUnitType.equals("Meters")){
                        outval = inval*0.3048;
                    } else if (toUnitType.equals("Feet")) {
                        outval = inval;
                    } else if (toUnitType.equals("Inch")) {
                        outval = inval*12;
                    }else{
                        outval = inval*30.48;
                    }
                } else if (fromUnitType.equals("Inch")) {
                    if(toUnitType.equals("Meters")){
                        outval = inval*0.0254;
                    } else if (toUnitType.equals("Feet")) {
                        outval = inval * 1/12;
                    } else if (toUnitType.equals("Inch")) {
                        outval = inval;
                    }else{
                        outval = inval*2.54;
                    }
                }else{
                    if(toUnitType.equals("Meters")){
                        outval = inval/100;
                    } else if (toUnitType.equals("Feet")) {
                        outval = inval * 0.0328084;
                    } else if (toUnitType.equals("Inch")) {
                        outval = inval * 0.393701;
                    }else{
                        outval = inval;
                    }
                }

                output.setText(""+outval);
            }
        });
    }
    public UnitConverter(){
        initComponents();
    }

    public static void main(String[] args) {
        new UnitConverter();
    }
}
