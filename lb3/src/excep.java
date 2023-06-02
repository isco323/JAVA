import javax.swing.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class excep extends Exception{
public excep()
{
    showMessageDialog(null,"Неккоректные значения", "Ошибка", JOptionPane.ERROR_MESSAGE);

}
}
