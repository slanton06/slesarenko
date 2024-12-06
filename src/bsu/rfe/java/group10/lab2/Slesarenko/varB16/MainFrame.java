package bsu.rfe.java.group10.lab2.Slesarenko.varB16;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    enum radioButtonsType{
        MEMORY,
        FORMULA
    }

    private static final int WIDTH = 650;
    private static final int HEIGHT = 320;
    private double sum = 0.0;

    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldZ;
    private Double[] memCell = new Double[3];
    private JTextField textFieldResult;
    private JLabel labelForMemory = new JLabel("0.0", 10);

    private ButtonGroup radioButtons = new ButtonGroup();
    private ButtonGroup radioButtons2 = new ButtonGroup();


    private int formulaId = 1;
    private int memoryId = 0;

    public Double calculate1(Double x, Double y, Double z) {
        return Math.sqrt(Math.pow(Math.sin(y)+Math.pow(y, 2)+Math.exp(Math.cos(y)), 2) + Math.pow(Math.log(Math.pow(z,2)) + Math.sin(Math.PI*Math.pow(x,2)), 3));
    }

    public Double calculate2(Double x, Double y, Double z) {
        return Math.sqrt(y) * 3*Math.pow(z,x) / Math.sqrt(1+Math.pow(y,3));
    }

    private JRadioButton addRadioButton(String buttonName, final int Id, radioButtonsType Type) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(Type == radioButtonsType.FORMULA){
                    MainFrame.this.formulaId = Id;
                }
                else if(Type == radioButtonsType.MEMORY){
                    MainFrame.this.memoryId = Id;
                    labelForMemory.setText(Double.toString(memCell[Id]));
                }


            }
        });

        if(Type == radioButtonsType.FORMULA)
            radioButtons.add(button);
        else if(Type == radioButtonsType.MEMORY)
            radioButtons2.add(button);

        return button;
    }

    public MainFrame() {
        super("Вычисление формулы");
        setSize(WIDTH, HEIGHT);

        getContentPane().setBackground(new Color(255, 255, 240));

        for(int i = 0; i < 3; i++)
            memCell[i] = 0.0;

        Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);


        Box hboxFormulaType = Box.createHorizontalBox();
        hboxFormulaType.add(Box.createHorizontalGlue());

        hboxFormulaType.add(addRadioButton("Formula 1", 1, radioButtonsType.FORMULA));
        hboxFormulaType.add(addRadioButton("Formula 2", 2, radioButtonsType.FORMULA));
        radioButtons.setSelected(
                radioButtons.getElements().nextElement().getModel(), true);
        hboxFormulaType.add(Box.createHorizontalGlue());

        // Создать область с полями ввода для X и Y
        JLabel labelForX = new JLabel("X:");
        textFieldX = new JTextField("0", 10);
        textFieldX.setMaximumSize(textFieldX.getPreferredSize());

        JLabel labelForY = new JLabel("Y:");
        textFieldY = new JTextField("0", 10);
        textFieldY.setMaximumSize(textFieldY.getPreferredSize());

        JLabel labelForZ = new JLabel("Z:");
        textFieldZ = new JTextField("0", 10);
        textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());

        Box hboxVariables = Box.createHorizontalBox();

        hboxVariables.add(labelForX);
        hboxVariables.add(Box.createHorizontalStrut(2));
        hboxVariables.add(textFieldX);

      //  hboxVariables.add(Box.createHorizontalGlue());
        hboxVariables.add(labelForY);
        hboxVariables.add(Box.createHorizontalStrut(2));
        hboxVariables.add(textFieldY);

       // hboxVariables.add(Box.createHorizontalGlue());
        hboxVariables.add(labelForZ);
        hboxVariables.add(Box.createHorizontalStrut(2));
        hboxVariables.add(textFieldZ);


        //Память
        Box hBoxMem = Box.createVerticalBox();

        Box hBoxMemRadioButton = Box.createHorizontalBox();
        hBoxMemRadioButton.add(Box.createHorizontalGlue());

        hBoxMemRadioButton.add(Box.createHorizontalGlue());

        Box hBoxMemButton = Box.createHorizontalBox();

        JButton buttonMemClear = new JButton("MC");
        buttonMemClear.setBorder(new LineBorder(new Color(252, 252, 252), 2));
        buttonMemClear.setBackground(new Color(215, 116, 116));
        buttonMemClear.setForeground(Color.WHITE);
        buttonMemClear.setPreferredSize(new Dimension(80, 30));
        buttonMemClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                memCell[memoryId] = 0.0;
                labelForMemory.setText("0.0");
            }
        });

        JButton buttonMemAdd = new JButton("M+");

        buttonMemAdd.setBorder(new LineBorder(new Color(252, 252, 252), 2));
        buttonMemAdd.setBackground(new Color(215, 116, 116));
        buttonMemAdd.setForeground(Color.WHITE);
        buttonMemAdd.setPreferredSize(new Dimension(80, 30));
        buttonMemAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                double valueToAdd = Double.parseDouble(textFieldResult.getText());
                memCell[memoryId] += Double.parseDouble(textFieldResult.getText());
                sum += valueToAdd;
                labelForMemory.setText(Double.toString(memCell[memoryId]));
            }
        });



        hBoxMemButton.add(Box.createHorizontalGlue());
        hBoxMemButton.add(buttonMemClear);
        hBoxMemButton.add(Box.createRigidArea(new Dimension(10, 0)));
        hBoxMemButton.add(buttonMemAdd);
        hBoxMemButton.add(Box.createRigidArea(new Dimension(10, 0)));
        hBoxMemButton.add(labelForMemory);
        hBoxMemButton.add(Box.createHorizontalGlue());

       // hBoxMem.add(hBoxMemRadioButton);
        hBoxMem.add(hBoxMemButton);
        // Создать область для кнопок
        JButton buttonCalc = new JButton("Count");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double x = Double.parseDouble(textFieldX.getText());
                    Double y = Double.parseDouble(textFieldY.getText());
                    Double z = Double.parseDouble(textFieldZ.getText());
                    Double result;
                    if (formulaId == 1)
                        result = calculate1(x, y, z);
                    else
                        result = calculate2(x, y, z);
                    if(Double.isNaN(result))
                        throw(new ArithmeticException());
                    textFieldResult.setText(result.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,"Error in the format of floating point number entry", "Erroneous number format", JOptionPane.WARNING_MESSAGE);
                } catch (ArithmeticException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,"Breached area of determination", "Area of determination error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonReset = new JButton("Clear all");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldZ.setText("0");
                textFieldResult.setText("0");
            }

        });
        // Создать область для вывода результата
        JLabel labelForResult = new JLabel("Result:");
        //labelResult = new JLabel("0");
        textFieldResult = new JTextField("0", 12);
        textFieldResult.setMaximumSize( textFieldResult.getPreferredSize());

        Box hboxResult = Box.createHorizontalBox();
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.add(labelForResult);
        hboxResult.add(Box.createHorizontalStrut(10));
        hboxResult.add(textFieldResult);
        hboxResult.add(Box.createHorizontalGlue());


        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());

        // Связать области воедино в компоновке BoxLayout
        Box contentBox = Box.createVerticalBox(); contentBox.add(Box.createVerticalGlue());
        contentBox.add(hboxFormulaType);
        contentBox.add(hboxVariables);
        contentBox.add(hboxResult);
        contentBox.add(hboxButtons);
        contentBox.add(hBoxMem);
        contentBox.add(Box.createVerticalGlue());
        getContentPane().add(contentBox, BorderLayout.CENTER);
    }
}
