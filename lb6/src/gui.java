import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class gui extends JDialog {
    private JPanel contentPane;
    private JTextField textFieldlow;
    private JTextField textFieldup;
    private JTextField textFieldstep;
    private JButton buttonadd;
    private JButton buttondelete;
    private JButton buttoncalc;
    private JTable table;
    private JButton fill;
    private JButton clear;
    private JButton saveButton;
    private JButton savebinButton;
    private JButton loadButton;
    private JButton loadbinButton;
    private JFileChooser fileChooser = new JFileChooser(("d:"));
    private File file;
    private Recintegral recintegral = new Recintegral();
    private ArrayList<Recintegral> digits = new ArrayList();
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    boolean check;
    public gui()
    {
        setContentPane(contentPane);
        setModal(true);
        clear.setEnabled(false);
        fill.setEnabled(false);
        buttoncalc.setEnabled(false);
        buttondelete.setEnabled(false);
        createtable();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int choice = fileChooser.showSaveDialog(fileChooser);
                //if (choice != JFileChooser.APPROVE_OPTION) return;
                file = fileChooser.getSelectedFile();
                try {
                    saveFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        loadbinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Data List Object Type", "dlot");
                fileChooser.setFileFilter(filter);
                int choice = fileChooser.showOpenDialog(fileChooser);
                if (choice != JFileChooser.APPROVE_OPTION) return;
                file = fileChooser.getSelectedFile();
                loadbinFile();
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("text", "txt");
                fileChooser.setFileFilter(filter);
                int choice = fileChooser.showOpenDialog(fileChooser);
                if (choice != JFileChooser.APPROVE_OPTION) return;
                file = fileChooser.getSelectedFile();
                try {
                    loadFile();
                } catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                } catch (NumberFormatException ex)
                {
                    throw new RuntimeException(ex);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        savebinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Data List Object Type", "dlot");
                fileChooser.setFileFilter(filter);
                int choice = fileChooser.showSaveDialog(fileChooser);
                //if (choice != JFileChooser.APPROVE_OPTION) return;
                file = fileChooser.getSelectedFile();
                saveBinFile();
            }
        });
        buttonadd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });


        buttondelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });

        buttoncalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    calc();
                } catch (excep ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        fill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                tabfill();
            }
        });
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                tabclear();
            }
        });
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void createtable()
    {
        model.addColumn("Верхняя граница интегрирования");
        model.addColumn("Нижняя граница интегрирования");
        model.addColumn("Шаг интегрирования");
        model.addColumn("Результат");
    }

    public void saveBinFile(){

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(digits);
            System.out.println("File has been written");
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }


    }
    public void loadbinFile() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            digits=((ArrayList<Recintegral>)ois.readObject());
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        AddInCollection();
    }
    public void saveFile() throws IOException {
        int countData = digits.size();

        FileWriter myfile = new FileWriter(file);

        for(int i=0;i<countData;i++) {
            myfile.write(digits.get(i).toString() + "\n");
        }
        myfile.flush();
        myfile.close();
    }
    public void loadFile() throws IOException, NumberFormatException, InterruptedException {
        FileReader myfile = new FileReader(file);
        BufferedReader reader = new BufferedReader(myfile);
        int i=1;
        digits.add(0, new Recintegral());
        String line = reader.readLine();
        String[] dblArray = line.split(",");
        digits.get(0).setMin(Double.valueOf(dblArray[0]));
        digits.get(0).setMax(Double.valueOf(dblArray[1]));
        digits.get(0).setStep(Double.valueOf(dblArray[2]));
        digits.get(0).setResult(Double.valueOf(dblArray[3]));
            //i++;
            //line = reader.readLine();
            //if(line==null)break;
           // dblArray = line.split(",");
            //digits.add(i, new Recintegral());
           // digits.get(i).setMin(Double.valueOf(dblArray[0]));
           // digits.get(i).setMax(Double.valueOf(dblArray[1]));
           // digits.get(i).setStep(Double.valueOf(dblArray[2]));
 //           digits.get(i).setResult(Double.valueOf(dblArray[3]));

        // for(int k=0;i< dataNumbers.size();k++) {
        //  modelData.addRow(dataNumbers.get(k).addMod());
        // }
        AddInCollection();

    }
    private void AddInCollection()
    {
        for(int k=0;k< digits.size();k++) {
            model.addRow(digits.get(k).addMod());
        }
    }
    private void tabfill(){
         model.addRow(digits.get(0).addMod());
         str++;
    }
    private void tabclear(){
        model.setRowCount(0);
        str=-1;
        clear.setEnabled(false);
        fill.setEnabled(false);
        buttoncalc.setEnabled(false);
        buttondelete.setEnabled(false);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    int str=-1;
    private void add(){
        try
        {
            if(textFieldup.getText().equals("")) {throw new excep();}
            if(textFieldlow.getText().equals("")){throw new excep();}
            if(textFieldstep.getText().equals("")){throw new excep();}
            double a = Double.parseDouble(textFieldlow.getText());
            double b = Double.parseDouble(textFieldup.getText());
            double h = Double.parseDouble(textFieldstep.getText());
            if(a <= 0.000001 || a>=1000000)
                throw new excep();
            if(b <= 0.000001 || b>=1000000)
                throw new excep();
            if(h <= 0.000001 || h>=1000000)
                throw new excep();
        check = true;
        model.addRow( new Object[]{textFieldlow.getText(), textFieldup.getText(), textFieldstep.getText()});
        recintegral.setAllField(Double.valueOf(textFieldlow.getText()),Double.valueOf(textFieldup.getText()),Double.valueOf(textFieldstep.getText()));
        recintegral.runServer();
        digits.add(recintegral);
        str++;
        }
        catch (excep ex)
        {
            throw new RuntimeException(ex);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (check)
        {
            clear.setEnabled(true);
            fill.setEnabled(true);
            buttoncalc.setEnabled(true);
            buttondelete.setEnabled(true);
        }
    }

    private void delete(){
        if(str == 0 && check)
        {
            clear.setEnabled(false);
            fill.setEnabled(false);
            buttoncalc.setEnabled(false);
            buttondelete.setEnabled(false);
        }
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.removeRow(table.getSelectedRow());
        str--;
    }

    private void calc() throws excep, InterruptedException {
         table.setValueAt(counted(), str, 3);
         textFieldlow.setText("");
         textFieldup.setText("");
         textFieldstep.setText("");
    }

    public double counted() throws excep, InterruptedException {
        double a = Double.parseDouble(textFieldlow.getText());
        double b = Double.parseDouble(textFieldup.getText());
        double h = Double.parseDouble(textFieldstep.getText());
        return recintegral.Trap(a, b, h);
    }
    public static void main(String[] args) {
        gui dialog = new gui();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}





