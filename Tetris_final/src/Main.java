import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

// это класс для всех форм и их поворотов
class Shape {
    Tetrominoe pieceShape; // форма фигуры
    int coords[][]; // координаты фигуры
    int[][][] coordsTable; // координаты фигуры во всех ее поворотах
    public Shape()
    {
        initShape(); // инициализировать форму
    }
    void initShape()
    {
        coords = new int[4][2]; // инициализировать размер куска
        setShape(Tetrominoe.NoShape); // установите форму на NoShape
    }

    // установить форму фигуры в заданную форму и установить координаты фигуры в координаты заданной формы
    protected void setShape(Tetrominoe shape) {

        coordsTable = new int[][][]{
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        for (int i = 0; i < 4 ; i++) { // для количества строк в форме

            for (int j = 0; j < 2; ++j) { // для количества столбцов в форме

                coords[i][j] = coordsTable[shape.ordinal()][i][j]; // установить координаты фигуры в координаты заданной формы
            }
        }

        pieceShape = shape;
    }

    void setX(int index, int x) { coords[index][0] = x; } // установить координату x куска
    void setY(int index, int y) { coords[index][1] = y; } // установить координату y куска
    public int x(int index) { return coords[index][0]; } // получить координату x куска
    public int y(int index) { return coords[index][1]; } // получить координату y куска
    public Tetrominoe getShape()  { return pieceShape; } // получить форму куска

    // установить форму куска в случайную форму каждый раз, когда создается новый кусок
    public void setRandomShape() {
        Random r = new Random(); // создать случайный объект
        int x = Math.abs(r.nextInt()) % 7 + 1; // получить случайное число от 1 до 7
        Tetrominoe[] values = Tetrominoe.values();  // получить все формы
        setShape(values[x]); // установить форму куска в случайную форму
    }

    // получить минимальную координату x куска
    public int minX() {

        int m = coords[0][0];

        for (int i=0; i < 4; i++) { // для количества строк в форме

            m = Math.min(m, coords[i][0]); // получить минимальную координату x куска
        }

        return m;
    }


    // получить минимальную координату у куска
    public int minY() {

        int m = coords[0][1];

        for (int i=0; i < 4; i++) {

            m = Math.min(m, coords[i][1]);
        }
        return m; //
    }
    // повернуть фигуру влево
    public Shape rotateLeft() {
        if (pieceShape == Tetrominoe.SquareShape) // если фигура квадратная
            return this; // вернуть фигуру без поворота
        Shape result = new Shape(); // создать новую фигуру
        result.pieceShape = pieceShape; // установите форму новой фигуры в форму старой
        for (int i = 0; i < 4; ++i) { // для количества строк в форме

            result.setX(i, y(i)); // устанавливает координату x новой формы на координату y части
            result.setY(i, -x(i)); //устанавливает координату y новой формы на отрицательную координату x части
        }

        return result;
    }
    // повернуть фигуру вправо
    public Shape rotateRight() {

        if (pieceShape == Tetrominoe.SquareShape)
            return this;

        Shape result = new Shape(); // создаем новую форму
        result.pieceShape = pieceShape; // устанавливаем форму новой формы в соответствии с формой фигуры

        for (int i = 0; i < 4; ++i) {

            result.setX(i, -y(i)); // устанавоивает координату x новой формы на отрицательную координату y части
            result.setY(i, x(i)); //устанавливает координату y новой формы на координату x части
        }

        return result;
    }
}

enum Tetrominoe { NoShape, ZShape, SShape, LineShape,
    TShape, SquareShape, LShape, MirroredLShape };

// это класс доски
class Board extends JPanel {

    static final long serialVersionUID = 1L;
    final int BOARD_WIDTH = 10; // ширина
    final int BOARD_HEIGHT = 22; // высота
    final int INITIAL_DELAY = 100; // начальная задержка таймера
    final int PERIOD_INTERVAL = 300; // интервал таймера

    Timer timer;
    boolean isFallingFinished = false; // проверить, закончил ли падение кусок
    boolean isStarted = false;
    boolean isPaused = false;
    int numLinesRemoved = 0; // количество удаленных строк
    int curX = 0; // текущая координата x
    int curY = 0; // текущая координата у
    JLabel statusbar;
    Shape curPiece;
    Tetrominoe[] board;

    public Board(Tetris parent) {

        initBoard(parent);
    }

    // инициализация доски
    void initBoard(Tetris parent) {
        setFocusable(true);
        setBorder(BorderFactory.createLineBorder(Color.pink, 4));
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(),
                INITIAL_DELAY, PERIOD_INTERVAL);
        curPiece = new Shape();
        statusbar = parent.getStatusBar();
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
        addKeyListener(new TAdapter());
        clearBoard();
    }

    int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    Tetrominoe shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    public void start() {

        isStarted = true;
        clearBoard();
        newPiece();
    }

    void pause() {

        if (!isStarted) {
            return;
        }

        isPaused = !isPaused;

        if (isPaused) {

            statusbar.setText("Paused");
        } else {

            statusbar.setText(String.valueOf(numLinesRemoved));
        }
    }

    void doDrawing(Graphics g) {

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();
        for (int i = 0; i < BOARD_HEIGHT; ++i)
        {
            for (int j = 0; j < BOARD_WIDTH; ++j)
            {
                Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Tetrominoe.NoShape) {
                    drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (curPiece.getShape() != Tetrominoe.NoShape) {

            for (int i = 0; i < 4; ++i) {

                int x = curX + curPiece.x(i); // получить координату x куска
                int y = curY - curPiece.y(i); // получить координату y куска
                drawSquare(g, x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        curPiece.getShape()); // нарисовать фигуру на доске
            }
        }
    }

    //квадрат
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    //новые координаты куска при падении
    void dropDown() throws IOException, InterruptedException {

        int newY = curY; // установить новую координату y на текущую координату y

        while (newY > 0) {

            if (!tryMove(curPiece, curX, newY - 1)) { // если фигура не может двигаться

                break; // break the loop
            }

            --newY;
        }

        pieceDropped(); // вызываем метод падения куска
    }

    void oneLineDown() throws IOException, InterruptedException {

        if (!tryMove(curPiece, curX, curY - 1)) {

            pieceDropped();
        }
    }

    void clearBoard() {

        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; ++i) {
            board[i] = Tetrominoe.NoShape;
        }
    }

    void pieceDropped() throws IOException, InterruptedException {

        for (int i = 0; i < 4; ++i) {

            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }

        removeFullLines(); // удалить строку

        if (!isFallingFinished) { // если фигура не закончила падение
            newPiece(); // создать новый
        }
    }
    void newPiece() {

        curPiece.setRandomShape(); // установить в случайную форму
        curX = BOARD_WIDTH / 2 + 1; // устанавливает координату x фигуры в середину доски
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) { // если фигура не может двигаться в новые координаты
            curPiece.setShape(Tetrominoe.NoShape); //устанавливает форму куска на no shape
            timer.cancel();
            isStarted = false;
            statusbar.setText("GAME OVER!");
        }
    }

    // проверить, может ли фигура двигаться
    boolean tryMove(Shape newPiece, int newX, int newY) {

        for (int i = 0; i < 4; ++i) {

            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) { // если координаты за пределами доски
                return false;
            }

            if (shapeAt(x, y) != Tetrominoe.NoShape) {
                return false;
            }
        }

        curPiece = newPiece; // установить текущую инф на новую инф
        curX = newX;
        curY = newY;

        repaint(); // перекрасить доску с новыми координатами фигуры

        return true;
    }
    void removeFullLines() throws IOException, InterruptedException {

        int numFullLines = 0; // устанавливаем количество полных строк равным 0

        for (int i = BOARD_HEIGHT - 1; i >= 0; --i) {
            boolean lineIsFull = true; // установить строку заполнена в true

            for (int j = 0; j < BOARD_WIDTH; ++j) {

                if (shapeAt(j, i) == Tetrominoe.NoShape) {

                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BOARD_HEIGHT - 1; ++k) {
                    for (int j = 0; j < BOARD_WIDTH; ++j) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1); // установить форму по координатам на форму по координатам ниже
                    }
                }
            }
        }

        if (numFullLines > 0) { // если количество полных строк больше 0
            connect connect = new connect();
            connect.runServer(numFullLines);
            numLinesRemoved += connect.getResult();
            //numLinesRemoved += numFullLines; // увеличить количество удаленных строк на количество полных строк
            statusbar.setText("Score: "+String.valueOf(numLinesRemoved)); // установить очки на количество удаленных строк
            isFallingFinished = true;
            curPiece.setShape(Tetrominoe.NoShape);
            repaint();

        }
    }

    void drawSquare(Graphics g, int x, int y,
                    Tetrominoe shape) {

        Color colors[] = {
                new Color(0, 0, 0), new Color(200, 100, 100),
                new Color(100, 200, 100), new Color(100, 100, 200),
                new Color(200, 200, 100), new Color(200, 100, 200),
                new Color(100, 200, 200), new Color(200, 100, 0),

        };
        Color color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);

    }

    void doGameCycle() throws IOException, InterruptedException {
        update();
        repaint();
    }

    void update() throws IOException, InterruptedException {

        if (isPaused) {
            return;
        }

        if (isFallingFinished) {

            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown(); // переместить фигуру вниз на одну строку
        }
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (!isStarted || curPiece.getShape() == Tetrominoe.NoShape) { //если игра не началась или фигура не имеет формы
                return; // return
            }

            int keycode = e.getKeyCode();
            if (keycode == KeyEvent.VK_ENTER) {
                pause();
                return;
            }

            if (isPaused) {
                return;
            }

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    tryMove(curPiece, curX - 1, curY);
                    break;

                case KeyEvent.VK_RIGHT:
                    tryMove(curPiece, curX + 1, curY);
                    break;

                case KeyEvent.VK_DOWN:
                    tryMove(curPiece.rotateRight(), curX, curY);
                    break;

                case KeyEvent.VK_UP:
                    tryMove(curPiece.rotateLeft(), curX, curY);
                    break;

                case KeyEvent.VK_SPACE:
                    try {
                        dropDown();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;

                case KeyEvent.VK_D:
                    try {
                        oneLineDown();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
            }
        }
    }

    class ScheduleTask extends TimerTask {

        @Override
        public void run() {
            try {
                doGameCycle();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

// main
class Tetris extends JFrame {

    //static final long serialVersionUID = 1L;
    JLabel statusbar;

    public Tetris() {
        initUI();
    }

    void initUI() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0XF5EBE0));
        statusbar = new JLabel("Score: 0");
        statusbar.setFont(new Font("MV Boli", Font.ROMAN_BASELINE, 30));
        panel.add(statusbar, BorderLayout.NORTH);

        Board board = new Board(this);
        add(panel, BorderLayout.NORTH);
        add(board);
        board.setBackground(new Color(0Xf0e2d3));
        board.start();

        setTitle("Тетрис");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public JLabel getStatusBar() {
        return statusbar;
    }

    // run
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {

            Tetris game = new Tetris();
            game.setVisible(true);
        });
    }
}