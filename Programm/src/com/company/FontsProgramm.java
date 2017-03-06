package com.company;

import com.company.PaintPanel;
import javafx.scene.layout.BorderRepeat;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FontsProgramm {

    /*Input parameters of infile*/
    private static double x, y; // Begginner coordinates
    private static String text; // Text
    private static String ttfont; // Font
    private static double h;
    private static double k;

    private JFrame mainFrame;// Main frame
    private JPanel controlPanel;// Panel of components
    private JPanel firstPanel;// Major panel
    private JTabbedPane tabbedPane;
    private JLabel headerLabel;// Up label
    private JButton jButton;// Ok Button
    private static JTextArea jOutputTextArea; //Field for output text
    private PaintPanel panel2;
    private JLabel firstFileLabel;//
    private JLabel secondFileLabel;
    private JLabel templateLabel;
    private JLabel scaleChoose;
    private JLabel coefficient_Choose;
    private DefaultTableModel model;
    private JTable table;

    private JTextField firtsFileName;
    private JTextField secondFileName;
    private JTextField scaleCoeff;
    private JComboBox templatesCombo;
    private JComboBox scalesCombo;
    private JComboBox typeCombo;
    private JScrollPane templateListScrollPane;
    private JScrollPane scaleListScrollPane;
    private JScrollPane typeListScrollPane;

    private String inputFile;//Input filename
    private String outputFile;//Output filename

    private boolean DEBUG = false;
    public FontsProgramm(){
        prepareGUI();
    }

    public static void main(String[] args){
        FontsProgramm fontsProgramm = new FontsProgramm();
        fontsProgramm.showGridLayoutDemo();
    }

    private String[] columnNames = {"X",
            "Y",
            "ttfont",
            "h",
            "text"};
    private Object[][] data = {
            {}
    };
    private void prepareGUI(){
        mainFrame = new JFrame("Templates");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(new Dimension(400, 300));



        firtsFileName = new JTextField();
        secondFileName = new JTextField();
        firtsFileName.setSize(50, 10);
        secondFileName.setSize(50, 10);
        scaleCoeff = new JTextField();
        scaleCoeff.setSize(50, 10);
        scaleCoeff.setText(Double.toString(0.327));

        firstFileLabel = new JLabel("Input file: ");
        secondFileLabel = new JLabel("Output file: ");
        templateLabel = new JLabel("Choose template: ");
        scaleChoose = new JLabel("Choose scale: ");
        coefficient_Choose = new JLabel("Scale: ");

        firstPanel = new JPanel();
        firstPanel.setSize(300, 500);
        firstPanel.setLayout(new BorderLayout());


        final DefaultComboBoxModel templateNames = new DefaultComboBoxModel();

        templateNames.addElement("Template 1");
        templateNames.addElement("Template 2");

        templatesCombo = new JComboBox(templateNames);
        templatesCombo.setSelectedIndex(0);
        templateListScrollPane = new JScrollPane(templatesCombo);

        final DefaultComboBoxModel scaleNames = new DefaultComboBoxModel();

        scaleNames.addElement("110x110");
        scaleNames.addElement("180x180");

        scalesCombo = new JComboBox(scaleNames);
        scalesCombo.setSelectedIndex(0);

        scaleListScrollPane = new JScrollPane(scalesCombo);

        final DefaultComboBoxModel typesModel = new DefaultComboBoxModel();

        typesModel.addElement("First type");
        typesModel.addElement("Second type");

        typeCombo = new JComboBox(typesModel);
        typeCombo.setEnabled(false);

        typeListScrollPane = new JScrollPane(typeCombo);

        templatesCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (templatesCombo.getSelectedIndex() == 1)
                    typeCombo.setEnabled(true);
                else
                    typeCombo.setEnabled(false);
            }
        });
        headerLabel = new JLabel("Please choose necessary parameters: ",JLabel.CENTER );

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        jOutputTextArea = new JTextArea(text,5,34);
        jOutputTextArea.setSize(200, 200);
        jButton = new JButton("Ok");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (firtsFileName.getText().equals("") || secondFileName.getText().equals("")) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Please, write the input/output file name!");
                } else {
                    while(model.getRowCount()!=0)
                        model.removeRow(0);

                    inputFile = firtsFileName.getText().toString();
                    outputFile = secondFileName.getText().toString();
                    try {
                        readfile(inputFile);
                        jOutputTextArea.setText(null);
                        jOutputTextArea.setText(read(outputFile));
                    } catch (IOException exec) {
                        exec.printStackTrace();
                    }
                    tabbedPane.setEnabledAt(1, true);
                    tabbedPane.setEnabledAt(2, true);
                    tabbedPane.setEnabledAt(3, true);
                    if (scalesCombo.getSelectedIndex() == 0)
                        mainFrame.setSize(450, 500);
                    else
                        mainFrame.setSize(750, 750);
                    panel2.repaint();
                }
            }
        });

        firstPanel.add(headerLabel, BorderLayout.NORTH);
        firstPanel.add(controlPanel, BorderLayout.CENTER);
        firstPanel.add(jButton, BorderLayout.SOUTH);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Parameters", null, firstPanel);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);


        JComponent panel3 = new JPanel();
        panel3.setLayout(new BorderLayout());

        model = new DefaultTableModel(data,columnNames);
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        model.removeRow(0);

        JScrollPane jScrollArea = new JScrollPane(table);
        panel3.add(jScrollArea, BorderLayout.CENTER);
        JButton btn_SaveInput = new JButton("Save");
        btn_SaveInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveTableIntoFile(inputFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JButton btn_add = new JButton("Add");
        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addRow(new Object[]{"", "", "", "", "", "", ""});
            }
        });
        JButton btn_Delete = new JButton("Delete");
        btn_Delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.removeRow(model.getRowCount() - 1);
            }
        });
        JButton btn_Convert = new JButton("Convert");
        btn_Convert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jOutputTextArea.setText("");
                panel2.repaint();
                jOutputTextArea.setText(panel2.getTemplate());
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setSize(400,100);
        buttonsPanel.setLayout(new GridLayout(1, 3));
        buttonsPanel.add(btn_SaveInput);
        buttonsPanel.add(btn_add);
        buttonsPanel.add(btn_Delete);
        buttonsPanel.add(btn_Convert);
        panel3.add(buttonsPanel,BorderLayout.SOUTH);
        tabbedPane.addTab("Input", null, panel3);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        panel2 = new PaintPanel(templatesCombo.getSelectedIndex(),Double.parseDouble(scaleCoeff.getText().toString()));
        tabbedPane.addTab("Coordinates", null, panel2);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent panel4 = new JPanel();

        panel4.setLayout(new BorderLayout());
        JScrollPane jScrollArea2 = new JScrollPane(jOutputTextArea);
        panel4.add(jScrollArea2,BorderLayout.CENTER);
        JButton btn_SaveOutput = new JButton("Save");
        btn_SaveOutput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveFile(outputFile,jOutputTextArea, StandardCharsets.UTF_8);
                 //   saveDifferentFiles();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel4.add(btn_SaveOutput,BorderLayout.SOUTH);

        tabbedPane.addTab("Output", null, panel4);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);


        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(2, false);
        tabbedPane.setEnabledAt(3, false);

        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }


    class MyTableModel extends DefaultTableModel {
        private String[] columnNames = {"X",
                "Y",
                "ttfont",
                "point",
                "c",
                "scale",
                "text"};
        private Object[][] data = {
                {"X", "Y",
                        "TimesNewRoman", new Integer(5), new Double(0.35), new Double(1.2), "Hello,world!"}
        };

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }


        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 0) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                        + " to " + value
                        + " (an instance of "
                        + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    private void showGridLayoutDemo(){

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)

                        .addGroup(layout.createSequentialGroup()
                                        .addComponent(firstFileLabel)
                                        .addComponent(firtsFileName)
                        )
                        .addGroup(layout.createSequentialGroup()
                                        .addComponent(secondFileLabel)
                                        .addComponent(secondFileName)
                        )
                        .addGroup(layout.createSequentialGroup()
                                        .addComponent(templateLabel)
                                        .addComponent(templateListScrollPane)
                        )
                        .addGroup(layout.createSequentialGroup()
                                        .addComponent(scaleChoose)
                                        .addComponent(scaleListScrollPane)
                        )
                        .addComponent(typeListScrollPane)
                        .addGroup(layout.createSequentialGroup()
                                        .addComponent(coefficient_Choose)
                                        .addComponent(scaleCoeff)
                        )

        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(firstFileLabel)
                                        .addComponent(firtsFileName))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(secondFileLabel)
                                        .addComponent(secondFileName))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(templateLabel)
                                        .addComponent(templateListScrollPane))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(scaleChoose)
                                        .addComponent(scaleListScrollPane))
                                .addComponent(typeListScrollPane))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                .addComponent(coefficient_Choose)
                                                .addComponent(scaleCoeff)
                                )
        );
        panel.setLayout(layout);
        controlPanel.add(panel);
        mainFrame.setVisible(true);
    }


    private static final Pattern patternDouble = Pattern.compile("\\-?\\d+\\.?\\d*");
    private static final Pattern patternInt = Pattern.compile(" \\d+ ");
    //Read input file
    public void readfile(String filename) throws IOException {
        System.out.println(filename);
        File inputfile = new File(filename);
        if(!inputfile.exists()) {
            inputfile.createNewFile();
        }
        InputStream is = new FileInputStream(filename);
        String UTF8 = "UTF8";
        int BUFFER_SIZE = 8192;

        BufferedReader br = new BufferedReader(new InputStreamReader(is,
                UTF8), BUFFER_SIZE);String s;
        while((s = br.readLine()) != null) {
            Matcher matcher = patternDouble.matcher(s);
            matcher.find();
            x = Double.parseDouble(s.substring(matcher.start(), matcher.end()));
            matcher.find();
            y = Double.parseDouble(s.substring(matcher.start(), matcher.end()));
            int ttfont_left = matcher.end() + 1;
            matcher.find();
            h = Double.parseDouble(s.substring(matcher.start(), matcher.end()));
            int ttfont_right = matcher.start()-1;
            int text_left = matcher.end() + 1;
            System.out.println(x + " " + y + " " + h);
            ttfont = s.substring(ttfont_left, ttfont_right);
            text = s.substring(text_left);
            System.out.println(ttfont);
            System.out.println(text);
            model.addRow(new Object[]{x, y, ttfont, h, text});
            k = 0;
            panel2.addPolygon(templatesCombo.getSelectedIndex(),scalesCombo.getSelectedIndex(), typeCombo.getSelectedIndex(), x, y, text, h, ttfont, k);
            panel2.repaint();
        }

        br.close();
    }

    public void readfileOfSecondType(String filename) throws IOException {
        System.out.println(filename);
        File inputfile = new File(filename);
        if(!inputfile.exists()) {
            inputfile.createNewFile();
        }
        InputStream is = new FileInputStream(filename);
        String UTF8 = "UTF8";
        int BUFFER_SIZE = 8192;

        BufferedReader br = new BufferedReader(new InputStreamReader(is,
                UTF8), BUFFER_SIZE);String s;
        while((s = br.readLine()) != null) {
            Matcher matcher = patternDouble.matcher(s);
            matcher.find();
            h = Double.parseDouble(s.substring(matcher.start(), matcher.end()));
            int ttfont_right = matcher.start() - 1;
            int text_left = matcher.end()+1;
            matcher.find();
            k = Double.parseDouble(s.substring(matcher.start(), matcher.end()));
            int text_right = matcher.start() - 1;
            System.out.println(x + " " + y + " " + h);
            ttfont = s.substring(0, ttfont_right);
            text = s.substring(text_left,text_right);
            System.out.println(ttfont);
            System.out.println(text);
            model.addRow(new Object[]{ttfont, h, text, k});
            panel2.addPolygon(templatesCombo.getSelectedIndex(),scalesCombo.getSelectedIndex(), typeCombo.getSelectedIndex(), x, y, text, h, ttfont, k);
            panel2.repaint();
        }
        br.close();
    }



    public static String read(String filename) throws IOException {
        File inputfile = new File(filename);
        if(!inputfile.exists()) {
            inputfile.createNewFile();
        }
        BufferedReader in = new BufferedReader(new FileReader(inputfile));
        StringBuilder sb = new StringBuilder();
        String s;
        while((s = in.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        in.close();
        return sb.toString();
    }

    public static void saveFile(String filename, JTextArea area, Charset charset) throws IOException {
        PrintWriter writer = new PrintWriter(filename);
        writer.print("");
        writer.close();

        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), charset));
        for (String line : area.getText().split("\\n")) {
            out.write(line);
            out.write(System.getProperty("line.separator"));
        }
        out.close();
    }

    public static void saveDifferentFiles() throws IOException {
        saveFile("outputISO_8859_1.txt", jOutputTextArea,StandardCharsets.ISO_8859_1);
        saveFile("outputUS_ASCII.txt", jOutputTextArea,StandardCharsets.US_ASCII);
        saveFile("outputUTF_16.txt", jOutputTextArea,StandardCharsets.UTF_16);
        saveFile("outputUTF_16BE.txt", jOutputTextArea,StandardCharsets.UTF_16BE);
        saveFile("outputUTF_16LE.txt", jOutputTextArea,StandardCharsets.UTF_16LE);
        saveFile("outputUTF_8.txt", jOutputTextArea,StandardCharsets.UTF_8);
    }

    public void saveTableIntoFile(String filename) throws IOException {
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
        int row = 0;
        while(row != model.getRowCount()) {
            Object[] result = new Object[model.getColumnCount()];

            for (int i = 0; i < model.getColumnCount(); i++) {
                result[i] = model.getValueAt(row, i);
                if(i != model.getColumnCount()-1)
                    out.write(result[i]+" ");
                else
                    out.write(result[i]+"");
            }
            out.write(System.getProperty( "line.separator" ));
            row++;
        }
        out.close();
    }



}
