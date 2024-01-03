import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.ResultSet;

public class Quiz implements ActionListener {  //класс с именем Quiz, который реализует интерфейс ActionListener
    String[] questions = {  //массив содержит вопросы
            "Which company created JAVA ?",
            "Which year is JAVA?",
            "What was JAVA originally called?",
            "Who is credited was creating JAVA?"
    };

    String[][] options = {    //двумерный массив содержит варианты ответов
            { "Microsoft", "Starbuks", "Alphabet", "Sun Microsystems" },
            { "1989", "1995", "1990", "1986" },
            { "Oak", "Latte", "Koffing", "Sincro" },
            { "Jobs", "Gates", "Gosling", "Skrin" }
    };

    char[] answers = {  //массив содержит правильне  ответы на соответствующие вопросы
            'D',
            'B',
            'A',
            'C'
    };

    char answer; // используется для хранения выбора пользователя в ответ на вопрос викторины
    int index; //используется для отслеживания текущего вопроса в массивах questions, options, и answers
    int correct_guesses = 0;  //переменная кол-ва правильных ответов
    int total_questions = questions.length; //используется, чтобы отслеживать общее количество вопросов в
                                            // тесте. Например, в resetQuiz(), если индекc вопроса превышает
                                            // общее количество вопросов, это означает, что тест завершен,
    int result;
    int seconds = 10;

    String userInput = JOptionPane.showInputDialog("Write your name: "); //вызов окна, в котором вводим имя пользователя.
                                                                        // это имя помещаетс в переменную userInput
    JFrame frame = new JFrame(); //создание объекта класса JFrame. JFrame представляет собой основное окно графического интерфейса

    JTextField textfield = new JTextField(); //текстовое поле, в которое пользователь может вводить текст.
                                             //текстовое поле, вероятно, используется для отображения и,
                                             //возможно, ввода информации, такой как номер текущего вопроса.
    JTextArea textarea = new JTextArea(); //текстовое поле, используется для отображения вопросов в вашем приложении.

    JButton buttonA = new JButton();
    JButton buttonB = new JButton();
    JButton buttonC = new JButton();
    JButton buttonD = new JButton();

    JButton buttonRestart = new JButton();
    JButton buttonRecord = new JButton();

    JButton buttonHistory = new JButton();

    JLabel answer_labelA = new JLabel(); //используется для отображения варианта ответа
    JLabel answer_labelB = new JLabel();
    JLabel answer_labelC = new JLabel();
    JLabel answer_labelD = new JLabel();

    JLabel seconds_left = new JLabel(); //отображение оставшегося времени

    JTextField number_right = new JTextField(); //отображение кол-ва правильных ответов в конце
    JTextField percentage = new JTextField(); //отображение процента правильных ответов в конце

    Timer timer = new Timer(1000, new ActionListener() { //таймер который каждую секудну производит действие e
        @Override
        public void actionPerformed(ActionEvent e) {
            seconds--;
            seconds_left.setText(String.valueOf(seconds));

            if (seconds <= 0) {
                displayAnswer();
            }
        }
    });

    Connection connection; //объявление переменной connection типа Connection.
                           // В вашем коде эта переменная используется для установления соединения с базой данных
    Statement statement; //объявление переменной statement типа Statement
                         // Statement - это интерфейс, предназначенный для выполнения SQL-запросов к базе данных.
                         // вы объявляете переменную statement этого типа, но не создаете объект Statement. Это происходит в блоке try-catch

    Quiz() {
        //настройка окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //операцию закрытия окна по умолчанию.
                                                              // JFrame.EXIT_ON_CLOSE указывает, что
                                                                //при закрытии окна приложение должно остановиться.
        frame.setSize(650, 650);
        frame.getContentPane().setBackground(new Color(50, 50, 50)); // цвет окна
        frame.setLayout(null); //Устанавливает менеджер компоновки (layout manager) для вашего JFrame в null.
                               // Это означает, что вы будете самостоятельно управлять расположением и размерами
                                //компонентов в окне.

        frame.setResizable(false); // если не хотим, что бы люди меняли размер окна

        //панель с номером вопроса
        textfield.setBounds(0, 0, 650, 50); //настройка размещения и размеров окна textfield
        textfield.setBackground(new Color(25, 25, 25)); // цвет фона панели
//        textfield.setForeground(new Color(25, 255, 0));
        textfield.setForeground(new Color(255, 255, 0)); //цвет шрифта номера вопроса
        textfield.setFont(new Font("Ink Free", Font.BOLD, 30)); // установка шрифта тексат в окне вопроса
        textfield.setBorder(BorderFactory.createBevelBorder(1)); //создает граничный эффект в виде фаски
        textfield.setHorizontalAlignment(JTextField.CENTER); //горизонтальная ориентация
        textfield.setEditable(false);
        textfield.setText("testing");

        //панель c вопросом
        textarea.setBounds(0, 50, 650, 50);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setBackground(new Color(25, 25, 25)); // цвет панели сверху
//        textarea.setForeground(new Color(25, 255, 0));
        textarea.setForeground(new Color(255, 255, 0)); //цвет вопроса
        textarea.setFont(new Font("MV Boli", Font.BOLD, 25)); // шрифт в панели
        textarea.setBorder(BorderFactory.createBevelBorder(1));
        textarea.setEditable(false);
        textarea.setText("String1");

        // первая кнопка
        buttonA.setBounds(0, 100, 100, 100);
        buttonA.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonA.setFocusable(false);
        buttonA.addActionListener(this);
        buttonA.setText("A");

        // вторая кнопка
        buttonB.setBounds(0, 200, 100, 100);
        buttonB.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonB.setFocusable(false);
        buttonB.addActionListener(this);
        buttonB.setText("B");

        // третья кнопка
        buttonC.setBounds(0, 300, 100, 100);
        buttonC.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonC.setFocusable(false);
        buttonC.addActionListener(this);
        buttonC.setText("C");

        // четвертая кнопка
        buttonD.setBounds(0, 400, 100, 100);
        buttonD.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonD.setFocusable(false);
        buttonD.addActionListener(this);
        buttonD.setText("D");

        // кнопка рестарта
        buttonRestart.setBounds(175, 425, 300, 100);
        buttonRestart.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonRestart.setFocusable(false);
        buttonRestart.addActionListener(this);
        buttonRestart.setText("RESTART");

        // кнопка history
        buttonHistory.setBounds(175, 525, 300, 100);
        buttonHistory.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonHistory.setFocusable(false);
        buttonHistory.addActionListener(this);
        buttonHistory.setText("HISTORY");

        // кнопка записи
        buttonRecord.setBounds(175, 125, 300, 100);
        buttonRecord.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonRecord.setFocusable(false);
        buttonRecord.addActionListener(this);
        buttonRecord.setText("RECORD");

        answer_labelA.setBounds(125, 100, 500, 100);
        answer_labelA.setBackground(new Color(50, 50, 50));
        answer_labelA.setForeground(new Color(25, 255, 0));
        answer_labelA.setFont(new Font("MV Boli", Font.PLAIN, 35));
        answer_labelA.setText("Testing label 1");

        answer_labelB.setBounds(125, 200, 500, 100);
        answer_labelB.setBackground(new Color(50, 50, 50));
        answer_labelB.setForeground(new Color(25, 255, 0));
        answer_labelB.setFont(new Font("MV Boli", Font.PLAIN, 35));
        answer_labelB.setText("Testing label 2");

        answer_labelC.setBounds(125, 300, 500, 100);
        answer_labelC.setBackground(new Color(50, 50, 50));
        answer_labelC.setForeground(new Color(25, 255, 0));
        answer_labelC.setFont(new Font("MV Boli", Font.PLAIN, 35));
        answer_labelC.setText("Testing label 3");

        answer_labelD.setBounds(125, 400, 500, 100);
        answer_labelD.setBackground(new Color(50, 50, 50));
        answer_labelD.setForeground(new Color(25, 255, 0));
        answer_labelD.setFont(new Font("MV Boli", Font.PLAIN, 35));
        answer_labelD.setText("Testing label 4");

        // таймер справа внизу (10 в квадратике)
        seconds_left.setBounds(535, 510, 100, 100);
        seconds_left.setBackground(new Color(25, 25, 25));
        seconds_left.setForeground(new Color(255, 0, 0));
        seconds_left.setFont(new Font("Ink Free", Font.BOLD, 60));
        seconds_left.setBorder(BorderFactory.createBevelBorder(1));
        seconds_left.setOpaque(true);
        seconds_left.setHorizontalAlignment(JTextField.CENTER);
        seconds_left.setText(String.valueOf(seconds));

        // верхнее окошко
        number_right.setBounds(225, 225, 200, 100);
        number_right.setBackground(new Color(25, 25, 25));
        number_right.setForeground(new Color(25, 255, 0));
        number_right.setFont(new Font("Ink Free", Font.BOLD, 50));
        number_right.setBorder(BorderFactory.createBevelBorder(1));
        number_right.setHorizontalAlignment(JTextField.CENTER);
        number_right.setEditable(false);


        // нижнее окошко
        percentage.setBounds(225, 325, 200, 100);
        percentage.setBackground(new Color(25, 25, 25));
        percentage.setForeground(new Color(25, 255, 0));
        percentage.setFont(new Font("Ink Free", Font.BOLD, 50));
        percentage.setBorder(BorderFactory.createBevelBorder(1));
        percentage.setHorizontalAlignment(JTextField.CENTER);
        percentage.setEditable(false);

        frame.add(answer_labelD);
        frame.add(answer_labelC);
        frame.add(answer_labelB);
        frame.add(answer_labelA);
        frame.add(buttonD); // четвертая кнопка
        frame.add(buttonC); // третья кнопка
        frame.add(buttonB); // вторая кнопка
        frame.add(buttonA); // первая кнопка
        frame.add(textarea);
        frame.add(textfield); // панель сверху
        frame.setVisible(true);
        timer.stop();
        nextQuestion();

        // Database setup
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:quiz_database.db");
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS quiz_results (id INTEGER PRIMARY KEY AUTOINCREMENT, username NVARCHAR(20), score INTEGER)");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

//    History() {
//        //настройка окна
//        frameHistory.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frameHistory.setSize(650, 650);
//        frameHistory.getContentPane().setBackground(new Color(50, 50, 50)); // цвет окна
//        frameHistory.setLayout(null);
//        frameHistory.setResizable(false);
//
//        //окно истории
//        history.setBounds(0, 0, 640, 550);
//        history.setBackground(new Color(25, 25, 25));
//        history.setForeground(new Color(25, 255, 0));
//        history.setFont(new Font("Ink Free", Font.BOLD, 50));
//        history.setBorder(BorderFactory.createBevelBorder(1));
//        history.setHorizontalAlignment(JTextField.CENTER);
//        history.setEditable(false);
//
//        // кнопка Cance
//        buttonHistory.setBounds(175, 600, 300, 100);
//        buttonHistory.setFont(new Font("MV Boli", Font.BOLD, 35));
//        buttonHistory.setFocusable(false);
//        buttonHistory.addActionListener(this);
//        buttonHistory.setText("Cance");
//
//        frame.add(answer_labelD);
//        frame.add(answer_labelC);
//        frame.add(answer_labelB);
//        frame.add(answer_labelA);
//        frame.add(buttonD); // четвертая кнопка
//        frame.add(buttonC); // третья кнопка
//        frame.add(buttonB); // вторая кнопка
//        frame.add(buttonA); // первая кнопка
//        frame.add(textarea);
//        frame.add(textfield); // панель сверху
//        frame.setVisible(true);
//        timer.stop();
//        nextQuestion();

    // метод добавления вопроса (текста) и ответов(текста) на окно
    public void nextQuestion() {

        System.out.println("Current index: " + index);

        if (index >= total_questions) {
            results();
        } else {
            frame.add(seconds_left);
            textfield.setText("Question" + (index + 1));
            textarea.setText(questions[index]);
            answer_labelA.setText(options[index][0]);
            answer_labelB.setText(options[index][1]);
            answer_labelC.setText(options[index][2]);
            answer_labelD.setText(options[index][3]);
            timer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);

        if (e.getSource() == buttonRecord) {
            saveResultsToDatabase();
        }

        if (e.getSource() == buttonRestart) {
            timer.stop();
            resetQuiz();
        }

        if (e.getSource() == buttonHistory){
            showHistoryResult();
        }

        if (e.getSource() == buttonA) {  //e.getSource() - это метод в Java, который возвращает объект, источник события
            answer = 'A';
            if (answer == answers[index]) {
                correct_guesses++;
            }
        }
        if (e.getSource() == buttonB) {
            answer = 'B';
            if (answer == answers[index]) {
                correct_guesses++;
            }
        }
        if (e.getSource() == buttonC) {
            answer = 'C';
            if (answer == answers[index]) {
                correct_guesses++;
            }
        }
        if (e.getSource() == buttonD) {
            answer = 'D';
            if (answer == answers[index]) {
                correct_guesses++;
            }
        }
        displayAnswer();
    }

    // метод рестарта
    public void resetQuiz() {
        Timer pause = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                userInput = JOptionPane.showInputDialog("Write your name: ");

                frame.add(buttonA);
                frame.add(buttonB);
                frame.add(buttonC);
                frame.add(buttonD);

                frame.remove(number_right); // удаление верхнего окошка результата
                frame.remove(percentage); // удаление нижнего окошка результата
                frame.remove(buttonRestart); // удаление кнопки рестарта
                frame.remove(buttonRecord);
                frame.remove(seconds_left);
//                frame.remove(buttonHistory);

                frame.repaint(); // восстановление цвета рамки после нажатия кнопки RESTART

                index = 0;
                correct_guesses = 0;
                result = 0;
                total_questions = questions.length;
                seconds = 10;

                seconds_left.setText(String.valueOf(seconds));

                buttonA.setEnabled(true);
                buttonB.setEnabled(true);
                buttonC.setEnabled(true);
                buttonD.setEnabled(true);

                answer_labelA.setForeground(new Color(25, 255, 0));
                answer_labelB.setForeground(new Color(25, 255, 0));
                answer_labelC.setForeground(new Color(25, 255, 0));

                answer_labelA.setText("");
                answer_labelB.setText("");
                answer_labelC.setText("");
                answer_labelD.setText("");

                frame.add(seconds_left);

                nextQuestion();
            }
        });
        pause.setRepeats(false);
        pause.start();
    }

    public void displayAnswer() {

        timer.stop();

        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);

        if (index < answers.length){ //добавили этот if т.к возникала ошибка (если его убрать, то будет ошибка)
//            Исключение ArrayIndexOutOfBoundsException возникает, когда вы обращаетесь к элементу массива по индексу, который находится за пределами диапазона этого массива. В вашем коде ошибка происходит в методе
//            Если index достигает максимального значения, то ваши условия проверки индексов приведут к выходу за пределы массива answers.
//                    Для предотвращения этой ошибки вам нужно добавить проверку, чтобы убедиться, что index находится в пределах допустимых значений.
            if (answers[index] != 'A') {
                answer_labelA.setForeground(new Color(255, 0, 0));
            }
            if (answers[index] != 'B') {
                answer_labelB.setForeground(new Color(255, 0, 0));
            }
            if (answers[index] != 'C') {
                answer_labelC.setForeground(new Color(255, 0, 0));
            }
            if (answers[index] != 'D') {
                answer_labelD.setForeground(new Color(255, 0, 0));
            }
            Timer pause = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    answer_labelA.setForeground(new Color(25, 255, 0));
                    answer_labelB.setForeground(new Color(25, 255, 0));
                    answer_labelC.setForeground(new Color(25, 255, 0));
                    answer_labelD.setForeground(new Color(25, 255, 0));

                    answer = ' ';
                    seconds = 10;
                    seconds_left.setText(String.valueOf(seconds));
                    buttonA.setEnabled(true);
                    buttonB.setEnabled(true);
                    buttonC.setEnabled(true);
                    buttonD.setEnabled(true);
                    index++;
                    nextQuestion();

                }
            });
            pause.setRepeats(false);
            pause.start();
        }
    }

    public void results() {

        frame.add(buttonRecord);
        frame.add(buttonRestart);
        frame.add(number_right);
        frame.add(percentage);
        frame.add(buttonHistory);

        frame.remove(buttonA);
        frame.remove(buttonB);
        frame.remove(buttonC);
        frame.remove(buttonD);

        frame.repaint();

        result = (int) ((correct_guesses / (double) total_questions) * 100);

        textfield.setText("RESULTS!");
        textarea.setText("");
        answer_labelA.setText("");
        answer_labelB.setText("");
        answer_labelC.setText("");
        answer_labelD.setText("");

        number_right.setText("(" + correct_guesses + "/" + total_questions + ")");
        percentage.setText(result + "%");

        System.out.println("Result: " + result + "%");
        System.out.println("Correct Answers: " + correct_guesses + "/" + total_questions);
    }

    private void saveResultsToDatabase() {

        System.out.println("Zapisano");

        try {
            String insertQuery = "INSERT INTO quiz_results (username, score) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, userInput);
                preparedStatement.setInt(2, result);
                preparedStatement.executeUpdate();

                frame.remove(buttonRecord);
//              frame.repaint();     //если откомментировать, то после нажатия на кнопку Record, на удаляется с экрана.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showHistoryResult() {

        try {
            String selectQuery = "SELECT * FROM quiz_results";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                System.out.println("Quiz Results History:");
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    int score = resultSet.getInt("score");

                    System.out.println("ID: " + id + ", Username: " + username + ", Score: " + score);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new History();
    }
}



