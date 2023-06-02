import javax.swing.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class excep extends Exception{
public excep()
{
    showMessageDialog(null,"Ошибка","Неккоректные значения", JOptionPane.ERROR_MESSAGE);
}
}
