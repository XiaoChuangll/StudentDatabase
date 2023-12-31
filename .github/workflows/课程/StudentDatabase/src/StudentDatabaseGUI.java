import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StudentDatabaseGUI extends JFrame {
    public class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        // 构造函数，接收背景图像路径作为参数
        public BackgroundPanel(String imagePath) {
            // 通过图像路径创建 ImageIcon，并获取其 Image 对象
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        // 覆盖父类的 paintComponent 方法以绘制背景图像
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 将背景图像绘制在面板上，填满整个面板
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private StudentDatabase database;
    private JTextField keywordField;
    private JTextField fieldField;
    private JTextArea JTable;
    private JTable outputTable;
    private DefaultTableModel tableModel;

    public StudentDatabaseGUI() {

        this.database = new StudentDatabase();

        // 设置窗口标题为"学生数据库管理系统"
        setTitle("学生数据库管理系统");

        // 设置窗口大小为570x450像素
        setSize(570, 450);

        // 设置窗口关闭操作为退出应用程序
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // 创建组件
        keywordField = new JTextField();
        fieldField = new JTextField();
        JTable = new JTextArea();

        // 在构造函数或初始化方法中
        JTable.setEditable(false);
        JButton queryButton = new JButton("查询信息");
        JButton sortButton = new JButton("排序学生信息");
        JButton addButton = new JButton("添加新学生");
        JButton batchAddButton = new JButton("批量添加新学生");
        JButton deleteButton = new JButton("删除学生");
        JButton updateButton = new JButton("更新学生信息");
        JButton clearButton = new JButton("清屏");
        JButton undoButton = new JButton("撤销");

        // 设置布局
        setLayout(new BorderLayout());

        BackgroundPanel inputPanel = new BackgroundPanel("background_IMG_048.PNG");
        inputPanel.setLayout(new GridLayout(2, 2));
        inputPanel.add(new JLabel("关键字:"));
        inputPanel.add(keywordField);
        JComboBox<String> fieldComboBox = new JComboBox<>(new String[]{"姓名", "学号", "班级", "课程"});
        inputPanel.add(new JLabel("查询字段:"));
        inputPanel.add(fieldComboBox);

        // 将inputPanel添加到布局的北部（上方）
        add(inputPanel, BorderLayout.NORTH);
        // 创建一个JTable，并将其放入JScrollPane中，然后将JScrollPane添加到布局的中央
        add(new JScrollPane(new JTable()), BorderLayout.CENTER);


        // 创建一个面板用于存放按钮
        JPanel buttonPanel = new JPanel();
        // 将面板的布局设置为2行2列的网格布局
        buttonPanel.setLayout(new GridLayout(2, 2));

        buttonPanel.add(queryButton); // 查询按钮，用于执行学生信息查询操作
        buttonPanel.add(sortButton);  // 排序按钮，用于执行学生信息排序操作
        buttonPanel.add(addButton);   // 添加按钮，用于添加新学生信息
        buttonPanel.add(batchAddButton); // 批量添加按钮，用于从文件批量添加新学生信息
        buttonPanel.add(deleteButton);    // 删除按钮，用于删除学生信息
        buttonPanel.add(updateButton);    // 更新按钮，用于更新学生信息
        buttonPanel.add(clearButton);     // 清屏按钮，用于清空显示区域的内容
        buttonPanel.add(undoButton);      // 撤销按钮，用于撤销最近的操作
        add(buttonPanel, BorderLayout.SOUTH);
        tableModel = new DefaultTableModel();
        outputTable = new JTable(tableModel);
        outputTable.setAutoCreateRowSorter(true); // 允许行排序

        // 设置表头
        String[] columnNames = {"学号", "姓名", "班级", "语文", "数学", "英语", "平均分"};
        tableModel.setColumnIdentifiers(columnNames);

        // 使用滚动窗格包装表格，以便显示滚动条
        add(new JScrollPane(outputTable), BorderLayout.CENTER);

        // 添加事件监听器

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示更新学生信息对话框，并添加查询功能
                showUpdateStudentDialogWithQuery();
            }
            private void showUpdateStudentDialogWithQuery() {

            }
        });

        // 为查询按钮添加事件监听器

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = keywordField.getText();
                String field = (String) fieldComboBox.getSelectedItem(); // 获取下拉菜单选项
                List<Student> result = database.queryStudents(keyword, field);
                updateOutput(result);
            }
        });

        // 撤销按钮事件监听器

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.undo();
                updateOutput(database.getStudents());
            }
        });

        // 为删除按钮添加事件监听器

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 清空JTextArea
                JTable.setText("");
                // 清空JTable的内容
                tableModel.setRowCount(0);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示删除学生对话框
                showDeleteStudentDialog();
            }
        });

        // 为更新按钮添加事件监听器
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示更新学生信息对话框
            }
        });

        // 为查询按钮添加动作监听器
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取关键字和字段输入框的文本内容
                String keyword = keywordField.getText();
                String field = fieldField.getText();

                // 调用数据库查询方法，获取查询结果
                List<Student> result = database.queryStudents(keyword, field);

                // 更新输出区域显示查询结果
                updateOutput(result);
            }
        });


        //排序功能
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建排序条件的菜单
                String[] sortingOptions = {"单科成绩", "总成绩", "平均成绩", "学号", "优秀率", "不及格率", "查询最高分", "查询最低分"};
                String selectedOption = (String) JOptionPane.showInputDialog(null,
                        "请选择排序条件:", "选择排序条件", JOptionPane.PLAIN_MESSAGE, null, sortingOptions, sortingOptions[0]);

                if (selectedOption != null && !selectedOption.isEmpty()) {
                    if (selectedOption.equals("查询最高分") || selectedOption.equals("查询最低分")) {
                        // 如果选择的是查询最高分或最低分，弹出科目选择菜单
                        String[] subjects = {"语文", "数学", "英语"};
                        String selectedSubject = (String) JOptionPane.showInputDialog(null,
                                "请选择科目:", selectedOption, JOptionPane.PLAIN_MESSAGE, null, subjects, subjects[0]);

                        if (selectedSubject != null && !selectedSubject.isEmpty()) {
                            // 根据用户选择的条件进行查询
                            List<Student> result = selectedOption.equals("查询最高分")
                                    ? database.queryMaxScoreStudents(selectedSubject)
                                    : database.queryMinScoreStudents(selectedSubject);

                            updateOutput(result);
                        } else {
                            JOptionPane.showMessageDialog(null, "请选择有效的科目。");
                        }
                    } else {
                        // 如果选择的是其他排序条件，执行相应的排序操作
                        List<Student> sortedStudents = database.sortStudents(selectedOption);
                        updateOutput(sortedStudents);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请选择有效的排序条件。");
                }
            }
        });

        // 为添加按钮添加动作监听器
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 执行动作：显示添加新学生的对话框
                showAddStudentDialog();
            }
        });


        // 为批量添加按钮添加动作监听器
        batchAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 执行动作：执行批量添加新学生的操作
                batchAddStudents();
            }
        });


        // 为更新按钮添加动作监听器
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 执行动作：显示查询学生信息对话框
                showQueryStudentDialog();
            }
        });

    }


    //查询即将要更新学生的方法
    private void showQueryStudentDialog() {
        JTextField queryNameField = new JTextField();

        JPanel queryPanel = new JPanel(new GridLayout(1, 2));
        queryPanel.add(new JLabel("查询姓名:"));
        queryPanel.add(queryNameField);

        int queryResult = JOptionPane.showConfirmDialog(null, queryPanel, "查询学生信息", JOptionPane.OK_CANCEL_OPTION);
        if (queryResult == JOptionPane.OK_OPTION) {
            // 获取用户输入的查询姓名
            String queryName = queryNameField.getText();

            // 执行查询
            List<Student> resultStudents = database.queryStudentsByName(queryName);

            if (!resultStudents.isEmpty()) {
                // 如果查询结果非空，则弹出更新学生信息对话框
                showUpdateStudentDialog(resultStudents.get(0));
            } else {
                JOptionPane.showMessageDialog(null, "未找到匹配的学生信息。");
            }
        }
    }


    //更新学生的方法
    private void showUpdateStudentDialog(Student studentToUpdate) {
        // 使用 studentToUpdate 对象初始化更新学生信息的输入框

        JTextField studentIdField = new JTextField(studentToUpdate.getStudentId());
        JTextField nameField = new JTextField(studentToUpdate.getName());
        JTextField classNameField = new JTextField(studentToUpdate.getClassName());
        JTextField chineseScoreField = new JTextField(String.valueOf(studentToUpdate.getChineseScore()));
        JTextField mathScoreField = new JTextField(String.valueOf(studentToUpdate.getMathScore()));
        JTextField englishScoreField = new JTextField(String.valueOf(studentToUpdate.getEnglishScore()));

        JPanel panel = new JPanel(new GridLayout(9, 2));
        panel.add(new JLabel("学号:"));
        panel.add(studentIdField);
        panel.add(new JLabel("姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("班级:"));
        panel.add(classNameField);
        panel.add(new JLabel("语文成绩:"));
        panel.add(chineseScoreField);
        panel.add(new JLabel("数学成绩:"));
        panel.add(mathScoreField);
        panel.add(new JLabel("英语成绩:"));
        panel.add(englishScoreField);

        //添加平均分和排名字段
        panel.add(new JLabel("平均分:"));
        JTextField averageScoreField = new JTextField();
        averageScoreField.setEditable(false);
        panel.add(averageScoreField);

        //不想用这个功能，所以注释掉
        /*
        panel.add(new JLabel("排名:"));
        JTextField rankField = new JTextField();
        rankField.setEditable(false);
        panel.add(rankField);
        */

        int updateResult = JOptionPane.showConfirmDialog(null, panel, "更新学生信息", JOptionPane.OK_CANCEL_OPTION);
        if (updateResult == JOptionPane.OK_OPTION) {
            // 获取用户输入的信息
            String studentId = studentIdField.getText();
            String name = nameField.getText();
            String className = classNameField.getText();
            int chineseScore = Integer.parseInt(chineseScoreField.getText());
            int mathScore = Integer.parseInt(mathScoreField.getText());
            int englishScore = Integer.parseInt(englishScoreField.getText());

            // 创建更新后的学生对象
            Student updatedStudent = new Student();
            updatedStudent.setStudentId(studentId);
            updatedStudent.setName(name);
            updatedStudent.setClassName(className);
            updatedStudent.setChineseScore(chineseScore);
            updatedStudent.setMathScore(mathScore);
            updatedStudent.setEnglishScore(englishScore);

            // 更新对话框中的平均分和排名字段
            double averageScore = (updatedStudent.getChineseScore() + updatedStudent.getMathScore() + updatedStudent.getEnglishScore()) / 3.0;
            int rank = database.calculateRank(updatedStudent);


            //不想用这个功能，所以注释掉
            //averageScoreField.setText(String.format("%.2f", averageScore));
            //rankField.setText(String.valueOf(rank));

            // 更新学生信息
            database.updateStudent(updatedStudent);

            // 更新显示
            updateOutput(database.getStudents());
        }
    }



    //删除学生的方法
    private void showDeleteStudentDialog() {
        JTextField studentIdField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("学号:"));
        panel.add(studentIdField);

        int result = JOptionPane.showConfirmDialog(null, panel, "删除学生", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // 获取用户输入的学生学号
            String studentIdToDelete = studentIdField.getText();

            // 尝试删除学生
            boolean deletionSuccessful = database.deleteStudent(studentIdToDelete);

            if (deletionSuccessful) {
                // 更新显示
                updateOutput(database.getStudents());
            } else {
                // 删除失败，显示提示对话框
                JOptionPane.showMessageDialog(null, "删除学生失败，可能是学号不存在或其他原因。请检查输入并重试。", "删除失败", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    //添加新学生
    private void showAddStudentDialog() {
        JTextField studentIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField classNameField = new JTextField();
        JTextField chineseScoreField = new JTextField();
        JTextField mathScoreField = new JTextField();
        JTextField englishScoreField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("学号:"));
        panel.add(studentIdField);
        panel.add(new JLabel("姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("班级:"));
        panel.add(classNameField);
        panel.add(new JLabel("语文成绩:"));
        panel.add(chineseScoreField);
        panel.add(new JLabel("数学成绩:"));
        panel.add(mathScoreField);
        panel.add(new JLabel("英语成绩:"));
        panel.add(englishScoreField);

        int result = JOptionPane.showConfirmDialog(null, panel, "添加新学生", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // 验证用户输入
            if (isEmpty(studentIdField) || isEmpty(nameField) || isEmpty(classNameField)
                    || isEmpty(chineseScoreField) || isEmpty(mathScoreField) || isEmpty(englishScoreField)) {
                JOptionPane.showMessageDialog(null, "请填写所有信息！", "输入不完整", JOptionPane.ERROR_MESSAGE);
                return; // 不完整时退出方法，不添加学生
            }

            // 获取用户输入
            String studentId = studentIdField.getText();
            String name = nameField.getText();
            String className = classNameField.getText();
            int chineseScore = Integer.parseInt(chineseScoreField.getText());
            int mathScore = Integer.parseInt(mathScoreField.getText());
            int englishScore = Integer.parseInt(englishScoreField.getText());

            // 创建新学生对象
            Student newStudent = new Student();
            newStudent.setStudentId(studentId);
            newStudent.setName(name);
            newStudent.setClassName(className);
            newStudent.setChineseScore(chineseScore);
            newStudent.setMathScore(mathScore);
            newStudent.setEnglishScore(englishScore);

            // 将新学生插入到数据库
            database.insertStudent(newStudent);

            // 弹出提示框，表示添加成功
            JOptionPane.showMessageDialog(null, "新学生添加成功！");
        }
    }

    // 辅助方法，检查文本字段是否为空
    private boolean isEmpty(JTextField textField) {
        return textField.getText().trim().isEmpty();
    }

    //批量添加学生数据的方法
    private void batchAddStudents() {
        // 创建文件选择器
        JFileChooser fileChooser = new JFileChooser();

        // 显示文件选择对话框，并获取用户的选择结果
        int result = fileChooser.showOpenDialog(null);

        // 如果用户选择了文件
        if (result == JFileChooser.APPROVE_OPTION) {
            // 获取用户选择的文件
            File selectedFile = fileChooser.getSelectedFile();

            try {
                // 从文件中读取学生信息列表
                List<Student> students = readStudentsFromFile(selectedFile);

                // 将学生信息插入到数据库中
                database.insertStudents(students);

                // 更新输出，传递一个空列表
                updateOutput(Collections.emptyList());
            } catch (IOException ex) {
                // 捕捉并显示读取文件时出现的错误
                JOptionPane.showMessageDialog(null, "读取文件时出错：" + ex.getMessage());
            }
        }
    }

    private List<Student> readStudentsFromFile(File file) throws IOException {
        // 创建一个列表以存储学生对象
        List<Student> students = new ArrayList<>();

        // 使用try-with-resources自动关闭BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // 从文件中读取每一行
            while ((line = reader.readLine()) != null) {
                // 使用逗号作为分隔符将行分割成部分
                String[] parts = line.split(",");

                // 创建一个新的Student对象
                Student student = new Student();

                // 使用从行中提取的部分设置学生的属性
                student.setStudentId(parts[0]);
                student.setName(parts[1]);
                student.setClassName(parts[2]);
                student.setChineseScore(Integer.parseInt(parts[3]));
                student.setMathScore(Integer.parseInt(parts[4]));
                student.setEnglishScore(Integer.parseInt(parts[5]));

                // 将Student对象添加到列表中
                students.add(student);
            }
        }
        // 返回学生对象的列表
        return students;
    }


    private void updateOutput(List<Student> students) {
    // 清空表格数据
    tableModel.setRowCount(0);

    // 更新表格数据
    for (int i = 0; i < students.size(); i++) {
        Student student = students.get(i);
        student.calculateAverageScore(); // 计算平均分

        Object[] rowData = {
                student.getStudentId(),
                student.getName(),
                student.getClassName(),
                student.getChineseScore(),
                student.getMathScore(),
                student.getEnglishScore(),
                String.format("%.2f", student.getAverageScore()) // 格式化为两位小数的平均分
        };
        tableModel.addRow(rowData);
    }

    // 设置表格单元格不可编辑
    for (int i = 0; i < outputTable.getColumnCount(); i++) {
        Class<?> columnClass = outputTable.getColumnClass(i);
        outputTable.setDefaultEditor(columnClass, null);
    }
}

    public static void main(String[] args) {
        try {
            // 设置外观为FlatDarkLaf
            UIManager.setLookAndFeel(new FlatDarkLaf());

            // Windows上模拟MacOS系统的外观
            UIManager.getLookAndFeelDefaults().put("Button.showMnemonics", Boolean.TRUE);
            UIManager.getLookAndFeelDefaults().put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
            UIManager.getLookAndFeelDefaults().put("Component.focusWidth", 2);
            UIManager.getLookAndFeelDefaults().put("Component.focusHeight", 2);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // 创建学生数据库GUI对象
            StudentDatabaseGUI gui = new StudentDatabaseGUI();
            // 设置GUI可见
            gui.setVisible(true);
        });
    }
}
