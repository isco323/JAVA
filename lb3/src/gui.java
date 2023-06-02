import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

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
            if(a >= b)
                throw new excep();
        check = true;
        model.addRow( new Object[]{textFieldlow.getText(), textFieldup.getText(), textFieldstep.getText()});
        recintegral.setAllField(Double.valueOf(textFieldlow.getText()),Double.valueOf(textFieldup.getText()),Double.valueOf(textFieldstep.getText()));
        digits.add(recintegral);
        str++;
        }
        catch (excep ex)
        {
            throw new RuntimeException(ex);
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

    private void calc() throws excep{
         table.setValueAt(counted(), str, 3);
         textFieldlow.setText("");
         textFieldup.setText("");
         textFieldstep.setText("");
    }

    public double counted() throws excep
    {
        double a = Double.parseDouble(textFieldlow.getText());
        double b = Double.parseDouble(textFieldup.getText());
        double h = Double.parseDouble(textFieldstep.getText());
        return integral(a, b, h);
    }
    public double integral(double a, double b, double h)
    {
    double area = 0;
    if (h == 0) return area;

    for(int i = 0; i < (b-a)/h; i++){
        area +=f(a + i*h);
    }
    area += (f(a)+f(b))/2;
    area *= h;
    return area;
    }

    public double f(double x){
       return ((Math.pow(Math.E, x))/x);
    }

    public static void main(String[] args) {
        gui dialog = new gui();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}





