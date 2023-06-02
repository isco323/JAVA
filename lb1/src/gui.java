import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class gui extends JDialog {
    private JPanel contentPane;
    private JTextField textFieldlow;
    private JTextField textFieldup;
    private JTextField textFieldstep;
    private JButton buttonadd;
    private JButton buttondelete;
    private JButton buttoncalc;
    private JTable table;

    final private Object[][] rowdata = new Object[0][4];
    final private Object[] columnsHeader = new String[]{
            "Верхняя граница интегрирования",
            "Нижняя граница интегрирования",
            "Шаг интегрирования",
            "Результат" };
    Object[][] fdata = new Object[4][4];
    public gui() {
        setContentPane(contentPane);
        setModal(true);


        table.setModel(new DefaultTableModel(rowdata, columnsHeader)
        {
            public boolean isCellEditable(int row, int column) {
            return !(column == 3);
        }});
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
                calc();
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

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    int str=-1;
    private void add(){
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.addRow( new Object[]{textFieldlow.getText(), textFieldup.getText(), textFieldstep.getText()});
        str++;
    }
    private void delete(){
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.removeRow(table.getSelectedRow());
        str--;
    }
    private void calc(){
        table.setValueAt(counted(), str, 3);
    }
    public double counted()
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

