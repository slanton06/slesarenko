package bsu.rfe.java.group10.lab3.Slesarenko.VarA4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Desktop;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    // Константы с исходным размером окна приложения
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    // Массив коэффициентов многочлена
    private Double[] coefficients;
    // Объект диалогового окна для выбора файлов
    // Компонент не создаѐтся изначально, т.к. может и не понадобиться
    // пользователю если тот не собирается сохранять данные в файл
    private JFileChooser fileChooser = null;
    // Элементы меню вынесены в поля данных класса, так как ими необходимо
    // манипулировать из разных мест
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    // Поля ввода для считывания значений переменных
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    private JLabel aboutPhotoLabel;
    private JLabel aboutNameTF;
    private JMenuItem aboutMenuItem;
    private JButton githubLinkButton;
    // Визуализатор ячеек таблицы
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    // Модель данных с результатами вычислений
    private GornerTableModel data;

    public MainFrame(Double[] coefficients) {
        // Обязательный вызов конструктора предка
        super("Табулирование многочлена на отрезке по схеме Горнера");
        // Запомнить во внутреннем поле переданные коэффициенты
        this.coefficients = coefficients;
        // Установить размеры окна
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);
        // Создать меню
        JMenuBar menuBar = new JMenuBar();
        // Установить меню в качестве главного меню приложения
        setJMenuBar(menuBar);
        // Добавить в меню пункт меню "Файл"

        JMenu aboutMenu = new JMenu("Сравка");
        menuBar.add(aboutMenu);
        JMenu fileMenu = new JMenu("Файл");
        // Добавить его в главное меню
        menuBar.add(fileMenu);
        // Создать пункт меню "Таблица"
        JMenu tableMenu = new JMenu("Таблица");

        // Добавить его в главное меню
        menuBar.add(tableMenu);

        Action aboutAction = new AbstractAction("Автор") {
            public void actionPerformed(ActionEvent event) {
                JDialog dialog = new JDialog(MainFrame.this, "Автор", true);
                dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dialog.setSize(360, 360);

                aboutNameTF = new JLabel("Слесаренко Антон, группа 10");

                Box box = Box.createVerticalBox();
                //box.add(aboutPhotoLabel);
                box.add(aboutNameTF);

                Box hbox = Box.createHorizontalBox();
                hbox.add(Box.createHorizontalStrut(20));
                hbox.add(box);

                dialog.getContentPane().add(hbox);

                dialog.setVisible(true);
            }
        };

        // Создать новое "действие" по сохранению в текстовый файл
        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    // Если экземпляр диалогового окна "Открыть файл" ещѐ не создан,
                    // то создать его
                    fileChooser = new JFileChooser();
                    // и инициализировать текущей директорией
                    fileChooser.setCurrentDirectory(new File(".txt"));
                }
                // Показать диалоговое окно
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    // Если результат его показа успешный,
                    // сохранить данные в текстовый файл
                    saveToTextFile(fileChooser.getSelectedFile());

            }
        };
        aboutMenuItem = aboutMenu.add(aboutAction);
        // Добавить соответствующий пункт подменю в меню "Файл"
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        // По умолчанию пункт меню является недоступным (данных ещѐ нет)
        saveToTextMenuItem.setEnabled(false);
        // Создать новое "действие" по сохранению в текстовый файл
        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    // Если экземпляр диалогового окна
                    // "Открыть файл" ещѐ не создан,
                    // то создать его
                    fileChooser = new JFileChooser();
                    // и инициализировать текущей директорией
                    fileChooser.setCurrentDirectory(new File("."));
                }
                // Показать диалоговое окно
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    ;
                // Если результат его показа успешный,
                // сохранить данные в двоичный файл
                saveToGraphicsFile(fileChooser.getSelectedFile());

            }
        };
        // Добавить соответствующий пункт подменю в меню "Файл"
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        // По умолчанию пункт меню является недоступным(данных ещѐ нет)
        saveToGraphicsMenuItem.setEnabled(false);
        // Создать новое действие по поиску значений многочлена
        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            public void actionPerformed(ActionEvent event) {
                // Запросить пользователя ввести искомую строку
                String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска",
                        "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                // Установить введенное значение в качестве иголки
                renderer.setNeedle(value);
                // Обновить таблицу
                getContentPane().repaint();
            }
        };
        // Добавить действие в меню "Таблица"
        searchValueMenuItem = tableMenu.add(searchValueAction);
        // По умолчанию пункт меню является недоступным (данных ещѐ нет)
        searchValueMenuItem.setEnabled(false);
        // Создать область с полями ввода для границ отрезка и шага
        // Создать подпись для ввода левой границы отрезка
        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
        // Создать текстовое поле для ввода значения длиной в 10 символов
        // со значением по умолчанию 0.0
        textFieldFrom = new JTextField("0.0", 10);
        // Установить максимальный размер равный предпочтительному, чтобы
        // предотвратить увеличение размера поля ввода
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        // Создать подпись для ввода левой границы отрезка
        JLabel labelForTo = new JLabel("до:");
        // Создать текстовое поле для ввода значения длиной в 10 символов
        // со значением по умолчанию 1.0
        textFieldTo = new JTextField("1.0", 10);
        // Установить максимальный размер равный предпочтительному, чтобы
        // предотвратить увеличение размера поля ввода
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        // Создать подпись для ввода шага табулирования
        JLabel labelForStep = new JLabel("с шагом:");
        // Создать текстовое поле для ввода значения длиной в 10 символов
        // со значением по умолчанию 1.0
        textFieldStep = new JTextField("0.1", 10);
        // Установить максимальный размер равный предпочтительному, чтобы
        // предотвратить увеличение размера поля ввода
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        // Создать контейнер 1 типа "коробка с горизонтальной укладкой"
        Box hboxRange = Box.createHorizontalBox();
        // Задать для контейнера тип рамки "объѐмная"
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        // Добавить "клей" C1-H1
        hboxRange.add(Box.createHorizontalGlue());
        // Добавить подпись "От"
        hboxRange.add(labelForFrom);
        // Добавить "распорку" C1-H2
        hboxRange.add(Box.createHorizontalStrut(10));
        // Добавить поле ввода "От"
        hboxRange.add(textFieldFrom);
        // Добавить "распорку" C1-H3
        hboxRange.add(Box.createHorizontalStrut(20));
        // Добавить подпись "До"
        hboxRange.add(labelForTo);
        // Добавить "распорку" C1-H4
        hboxRange.add(Box.createHorizontalStrut(10));
        // Добавить поле ввода "До"
        hboxRange.add(textFieldTo);
        // Добавить "распорку" C1-H5
        hboxRange.add(Box.createHorizontalStrut(20));
        // Добавить подпись "с шагом"
        hboxRange.add(labelForStep);
        // Добавить "распорку" C1-H6
        hboxRange.add(Box.createHorizontalStrut(10));
        // Добавить поле для ввода шага табулирования
        hboxRange.add(textFieldStep);
        // Добавить "клей" C1-H7
        hboxRange.add(Box.createHorizontalGlue());
        // Установить предпочтительный размер области равным удвоенному
        // минимальному, чтобы при компоновке область совсем не сдавили
        hboxRange.setPreferredSize(new Dimension(
                new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
                new Double(hboxRange.getMinimumSize().getHeight()).intValue() * 2));
        // Установить область в верхнюю (северную) часть компоновки
        getContentPane().add(hboxRange, BorderLayout.NORTH);
        // Создать кнопку "Вычислить"
        JButton buttonCalc = new JButton("Вычислить");
        // Задать действие на нажатие "Вычислить" и привязать к кнопке
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    // Считать значения начала и конца отрезка, шага
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    // На основе считанных данных создать новый экземпляр модели таблицы
                    data = new GornerTableModel(from, to, step,
                            MainFrame.this.coefficients);
                    // Создать новый экземпляр таблицы
                    JTable table = new JTable(data);
                    // Установить в качестве визуализатора ячеек для класса Double разработанный
                    // визуализатор
                    table.setDefaultRenderer(Double.class,
                            renderer);
                    // Установить размер строки таблицы в 30пикселов
                    table.setRowHeight(30);
                    // Удалить все вложенные элементы из контейнераhBoxResult
                    hBoxResult.removeAll();
                    // Добавить в hBoxResult таблицу, "обѐрнутую" впанель с полосами прокрутки
                    hBoxResult.add(new JScrollPane(table));
                    // Обновить область содержания главного окна
                    getContentPane().validate();
                    // Пометить ряд элементов меню как доступных
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                } catch (NumberFormatException ex) {
                    // В случае ошибки преобразования чисел показатьсообщение об ошибке
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        // Создать кнопку "Очистить поля"
        JButton buttonReset = new JButton("Очистить поля");
        // Задать действие на нажатие "Очистить поля" и привязать к кнопке
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // Установить в полях ввода значения по умолчанию
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                // Удалить все вложенные элементы контейнера
                hBoxResult.removeAll();
                // Добавить в контейнер пустую панель
                hBoxResult.add(new JPanel());
                // Пометить элементы меню как недоступные
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                // Обновить область содержания главного окна
                getContentPane().validate();
            }
        });
        // Поместить созданные кнопки в контейнер
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        // Установить предпочтительный размер области равным удвоенномуминимальному,
        // чтобы при
        // компоновке окна область совсем не сдавили
        hboxButtons.setPreferredSize(new Dimension(new Double(hboxButtons.getMaximumSize().getWidth()).intValue(),
                new Double(hboxButtons.getMinimumSize().getHeight()).intValue() * 2));
        // Разместить контейнер с кнопками в нижней (южной) областиграничной компоновки
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
        // Область для вывода результата пока что пустая
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        // Установить контейнер hBoxResult в главной (центральной) областиграничной
        // компоновки
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }
    protected void saveToGraphicsFile(File selectedFile) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile))) {
            for (int i = 0; i < data.getRowCount(); i++) {
                double x = (Double) data.getValueAt(i, 0);
                double value = (Double) data.getValueAt(i, 1);
                out.writeDouble(x);
                out.writeDouble(value);
            }
            System.out.println("File was saved");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    protected void saveToTextFile(File selectedFile) {
        try (PrintWriter out = new PrintWriter(selectedFile)) {
            out.println("X\tValue");
            for (int i = 0; i < data.getRowCount(); i++) {
                double x = (Double) data.getValueAt(i, 0);
                double value = (Double) data.getValueAt(i, 1);
                out.println(x + "\t" + value);
            }
            System.out.println("File was saved");
        } catch (FileNotFoundException e) {
            System.err.println("File wasn't find: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        Double[] a = {1., 5., 4., 3., 2., 1.};
        MainFrame frame = new MainFrame(a);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
