import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


class Student {

    // 学生成绩统计属性
    private double averageScore;
    private int rank;

    /**
     * 获取班级名称。
     *
     * @return 班级名称
     */
    public String getClassName() {
        return className;
    }

    /**
     * 获取语文成绩。
     *
     * @return 语文成绩
     */
    public int getChineseScore() {
        return chineseScore;
    }

    /**
     * 获取数学成绩。
     *
     * @return 数学成绩
     */
    public int getMathScore() {
        return mathScore;
    }

    /**
     * 获取英语成绩。
     *
     * @return 英语成绩
     */
    public int getEnglishScore() {
        return englishScore;
    }

    /**
     * 计算并设置平均分。
     */
    public void calculateAverageScore() {
        this.averageScore = (double) (chineseScore + mathScore + englishScore) / 3.0;
    }

    /**
     * 设置平均分。
     *
     * @param averageScore 平均分
     */
    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    /**
     * 获取平均分。
     *
     * @return 平均分
     */
    public double getAverageScore() {
        return averageScore;
    }

    /**
     * 设置排名。
     *
     * @param rank 排名
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * 获取排名。
     *
     * @return 排名
     */
    public int getRank() {
        return rank;
    }

    /**
     * 计算卓越率（分数大于等于90分的科目数量占总科目数量的比例）。
     *
     * @return 卓越率
     */
    public double calculateExcellentRate() {
        int excellentCount = (chineseScore >= 90 ? 1 : 0) + (mathScore >= 90 ? 1 : 0) + (englishScore >= 90 ? 1 : 0);
        return (double) excellentCount / 3.0;
    }

    /**
     * 计算不及格率（分数小于60分的科目数量占总科目数量的比例）。
     *
     * @return 不及格率
     */
    public double calculateFailRate() {
        int failCount = (chineseScore < 60 ? 1 : 0) + (mathScore < 60 ? 1 : 0) + (englishScore < 60 ? 1 : 0);
        return (double) failCount / 3.0;
    }

    /**
     * 设置英语成绩。
     *
     * @param englishScore 英语成绩
     */
    public void setEnglishScore(int englishScore) {
        this.englishScore = englishScore;
    }

    /**
     * 设置数学成绩。
     *
     * @param mathScore 数学成绩
     */
    public void setMathScore(int mathScore) {
        this.mathScore = mathScore;
    }

    /**
     * 设置语文成绩。
     *
     * @param chineseScore 语文成绩
     */
    public void setChineseScore(int chineseScore) {
        this.chineseScore = chineseScore;
    }

    /**
     * 设置班级名称。
     *
     * @param className 班级名称
     */
    public void setClassName(String className) {
        this.className = className;
    }
    String studentId;
    String name;
    String className;
    int chineseScore;
    int mathScore;
    int englishScore;
    public String getStudentId() {
        return studentId;
    }


    // 构造函数、获取器和设置器

    /**
     * 设置学生学号。
     *
     * @param studentId 学生学号
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * 设置学生姓名。
     *
     * @param name 学生姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取学生姓名。
     *
     * @return 学生姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 返回学生信息的字符串表示形式。
     * 用于调试目的，输出学生的学号、姓名、班级、语文、数学、英语成绩。
     *
     * @return 学生信息的字符串表示形式
     */
    @Override
    public String toString() {
        System.out.println("Debug: " + studentId + "," + name + "," + className + "," + chineseScore + "," + mathScore + "," + englishScore);
        return studentId + "," + name + "," + className + "," + chineseScore + "," + mathScore + "," + englishScore;
    }


}

public class StudentDatabase {

    /**
     * 学生列表。
     * 存储学生信息的列表。
     */
    private List<Student> students;

    /**
     * 撤销栈。
     * 用于存储学生列表的历史记录，支持撤销操作。
     */
    private Stack<List<Student>> undoStack;


    public StudentDatabase() {
        this.students = new ArrayList<>();
        this.undoStack = new Stack<>();
        // 从 .txt 文件加载数据（如果有的话）
        loadDataFromFile();
        // 初始状态入栈
        saveToUndoStack();
    }

    private void saveToUndoStack() {
        undoStack.push(new ArrayList<>(students));
    }

    public void undo() {
        if (undoStack.size() > 1) {
            undoStack.pop(); // 移除当前状态
            students = new ArrayList<>(undoStack.peek()); // 恢复到上一个状态
        }
    }

    private void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("student_database.txt"))) {
            for (Student student : students) {
                writer.println(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件加载学生数据的方法。
     * 该方法从名为 "student_database.txt" 的文件中读取学生信息，并将其存储到学生列表中。
     */
    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("student_database.txt"))) {
            String line;

            // 逐行读取文件内容
            while ((line = reader.readLine()) != null) {
                // 使用逗号分割每行数据
                String[] parts = line.split(",");

                // 创建学生对象并设置属性值
                Student student = new Student();
                student.studentId = parts[0];
                student.name = parts[1];
                student.className = parts[2];
                student.chineseScore = Integer.parseInt(parts[3]);
                student.mathScore = Integer.parseInt(parts[4]);
                student.englishScore = Integer.parseInt(parts[5]);

                // 将学生对象添加到学生列表中
                students.add(student);
            }
        } catch (IOException e) {
            // 捕获文件读取异常并打印堆栈跟踪信息
            e.printStackTrace();
        }
    }


    /**
     * 根据关键字查询学生信息的方法。
     * 查询可根据学生姓名、学号、班级或课程名称进行，查询结果以列表形式返回。
     *
     * @param keyword 查询关键字
     * @param field   查询字段，可选值包括："姓名"、"学号"、"班级"、"课程"
     * @return 包含查询结果的学生列表
     */
    public List<Student> queryStudents(String keyword, String field) {
        return students.stream()
                .filter(student -> field.equalsIgnoreCase("姓名") && student.name.contains(keyword)
                        || field.equalsIgnoreCase("学号") && student.studentId.contains(keyword)
                        || field.equalsIgnoreCase("班级") && student.className.contains(keyword)
                        || field.equalsIgnoreCase("课程") && (student.chineseScore >= 0 || student.mathScore >= 0 || student.englishScore >= 0))
                .collect(Collectors.toList());
    }


    // 根据条件排序学生信息（单科成绩、总成绩、平均成绩、学号）
    public List<Student> sortStudents(String criteria) {
        Comparator<Student> comparator = null;
        switch (criteria) {
            case "单科成绩", "总成绩":
                comparator = Comparator.comparingInt(student -> student.chineseScore + student.mathScore + student.englishScore);
                break;
            case "平均成绩":
                comparator = Comparator.comparingDouble(student -> (student.chineseScore + student.mathScore + student.englishScore) / 3.0);
                break;
            case "学号":
                comparator = Comparator.comparing(Student::getStudentId);
                break;
            case "优秀率":
                comparator = Comparator.comparingDouble(Student::calculateExcellentRate);
                break;
            case "不及格率":
                comparator = Comparator.comparingDouble(Student::calculateFailRate);
                break;
        }
        return students.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * 插入新学生信息的方法。
     * 将新的学生信息添加到学生列表中，并保存更新后的数据到文件，同时将操作记录保存到撤销栈。
     *
     * @param student 要插入的新学生对象
     */
    public void insertStudent(Student student) {
        // 将新学生信息添加到学生列表
        students.add(student);

        // 保存更新后的数据到文件
        saveDataToFile();

        // 将操作记录保存到撤销栈
        saveToUndoStack();
    }


    /**
     * 删除学生信息的方法。
     * 根据学生学号删除相应学生的信息，并保存更新后的数据到文件，同时将操作记录保存到撤销栈。
     *
     * @param studentId 要删除的学生的学号
     * @return 删除是否成功，始终返回 false
     */
    public boolean deleteStudent(String studentId) {
        // 根据学号删除相应学生的信息
        students.removeIf(student -> student.getStudentId().equals(studentId));

        // 保存更新后的数据到文件
        saveDataToFile();

        // 将操作记录保存到撤销栈
        saveToUndoStack();

        // 始终返回 false，表示删除操作不返回具体结果
        return false;
    }


    /**
     * 更新学生信息的方法。
     * 根据更新后的学生信息，先删除对应学生的旧信息，然后插入更新后的学生信息。
     *
     * @param updatedStudent 包含更新信息的学生对象
     */
    public void updateStudent(Student updatedStudent) {
        // 根据学号删除相应学生的旧信息
        deleteStudent(updatedStudent.getStudentId());

        // 插入更新后的学生信息
        insertStudent(updatedStudent);
    }


    // 主方法用于交互
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentDatabase database = new StudentDatabase();

        // 进入主循环，接受用户输入并执行相应操作
        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 查询学生信息");
            System.out.println("2. 排序学生信息");
            System.out.println("3. 插入新学生");
            System.out.println("4. 删除学生信息");
            System.out.println("5. 更新学生信息");
            System.out.println("0. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消费换行符

            switch (choice) {
                case 1:
                    // 查询学生信息
                    System.out.println("请输入关键字:");
                    String keyword = scanner.nextLine();
                    System.out.println("请输入查询字段:");
                    String field = scanner.nextLine();
                    List<Student> result = database.queryStudents(keyword, field);
                    System.out.println("查询结果：" + result);
                    break;

                case 2:
                    // 排序学生信息
                    System.out.println("请输入排序条件:");
                    String criteria = scanner.nextLine();
                    List<Student> sortedStudents = database.sortStudents(criteria);
                    System.out.println("排序后的学生：" + sortedStudents);
                    break;

                case 3:
                    // 插入新学生
                    System.out.println("请输入新学生信息（学号,姓名,班级,语文成绩,数学成绩,英语成绩）:");
                    String[] studentInfo = scanner.nextLine().split(",");
                    Student newStudent = new Student();
                    newStudent.setStudentId(studentInfo[0]);
                    newStudent.setName(studentInfo[1]);
                    newStudent.setClassName(studentInfo[2]);
                    newStudent.setChineseScore(Integer.parseInt(studentInfo[3]));
                    newStudent.setMathScore(Integer.parseInt(studentInfo[4]));
                    newStudent.setEnglishScore(Integer.parseInt(studentInfo[5]));
                    database.insertStudent(newStudent);
                    System.out.println("新学生插入成功！");
                    break;

                case 4:
                    // 删除学生信息
                    System.out.println("请输入要删除的学生学号:");
                    String studentIdToDelete = scanner.nextLine();
                    database.deleteStudent(studentIdToDelete);
                    System.out.println("学生信息删除成功！");
                    break;

                case 5:
                    // 更新学生信息
                    System.out.println("请输入更新后的学生信息（学号,姓名,班级,语文成绩,数学成绩,英语成绩）:");
                    String[] updatedStudentInfo = scanner.nextLine().split(",");
                    Student updatedStudent = new Student();
                    updatedStudent.setStudentId(updatedStudentInfo[0]);
                    updatedStudent.setName(updatedStudentInfo[1]);
                    updatedStudent.setClassName(updatedStudentInfo[2]);
                    updatedStudent.setChineseScore(Integer.parseInt(updatedStudentInfo[3]));
                    updatedStudent.setMathScore(Integer.parseInt(updatedStudentInfo[4]));
                    updatedStudent.setEnglishScore(Integer.parseInt(updatedStudentInfo[5]));
                    database.updateStudent(updatedStudent);
                    System.out.println("学生信息更新成功！");
                    break;

                case 0:
                    // 退出程序
                    scanner.close();
                    database.saveDataToFile();
                    System.exit(0);
                    break;

                default:
                    System.out.println("无效选择，请重新输入。");
            }
        }
    }

    /**
     * 插入新学生信息的方法。
     * 将新的学生信息列表添加到当前学生列表中，并保存更新后的数据到文件。
     *
     * @param newStudents 包含新学生信息的学生列表
     */
    public void insertStudents(List<Student> newStudents) {
        // 将新学生信息列表添加到当前学生列表
        students.addAll(newStudents);

        // 保存更新后的数据到文件
        saveDataToFile();
    }


    /**
     * 获取当前学生列表的方法。
     * 返回包含当前学生信息的学生列表。
     *
     * @return 当前学生列表
     */
    public List<Student> getStudents() {
        return this.students;
    }


    /**
     * 根据学生姓名查询学生信息的方法。
     * 返回包含符合姓名条件的学生信息的学生列表。
     *
     * @param name 要查询的学生姓名
     * @return 符合条件的学生列表
     */
    public List<Student> queryStudentsByName(String name) {
        List<Student> result = new ArrayList<>();

        // 遍历学生列表，筛选符合姓名条件的学生信息
        for (Student student : students) {
            if (student.getName().equals(name)) {
                result.add(student);
            }
        }

        return result;
    }



    /**
     * 查询最高分学生的方法。
     * 根据指定的科目，返回取得该科目最高分的学生列表。
     *
     * @param subject 要查询的科目
     * @return 取得最高分的学生列表
     */
    public List<Student> queryMaxScoreStudents(String subject) {
        // 使用比较器获取指定科目的降序比较器
        Comparator<Student> comparator = getSubjectComparator(subject, Comparator.reverseOrder());

        // 获取最高分数
        int maxScore = students.stream().max(comparator).map(student -> getSubjectScore(student, subject)).orElse(-1);

        // 过滤得到取得最高分的学生列表，同时计算平均分和设置排名
        return students.stream()
                .filter(student -> getSubjectScore(student, subject) == maxScore)
                .map(student -> {
                    student.calculateAverageScore(); // 计算平均分
                    student.setRank(students.indexOf(student) + 1); // 设置排名
                    return student;
                })
                .collect(Collectors.toList());
    }


    /**
     * 查询最低分学生的方法。
     * 根据指定的科目，返回取得该科目最低分的学生列表。
     *
     * @param subject 要查询的科目
     * @return 取得最低分的学生列表
     */
    public List<Student> queryMinScoreStudents(String subject) {
        // 使用比较器获取指定科目的升序比较器
        Comparator<Student> comparator = getSubjectComparator(subject, Comparator.naturalOrder());

        // 获取最低分数
        int minScore = students.stream().min(comparator).map(student -> getSubjectScore(student, subject)).orElse(-1);

        // 过滤得到取得最低分的学生列表，同时计算平均分和设置排名
        return students.stream()
                .filter(student -> getSubjectScore(student, subject) == minScore)
                .map(student -> {
                    student.calculateAverageScore(); // 计算平均分
                    student.setRank(students.indexOf(student) + 1); // 设置排名
                    return student;
                })
                .collect(Collectors.toList());
    }


    /**
     * 获取学生的指定科目分数的私有方法。
     * 根据指定的科目返回相应学生的分数。
     *
     * @param student 学生对象
     * @param subject 要获取分数的科目
     * @return 学生在指定科目的分数
     * @throws IllegalArgumentException 如果提供的科目无效
     */
    private int getSubjectScore(Student student, String subject) {
        switch (subject) {
            case "语文":
                return student.getChineseScore();
            case "数学":
                return student.getMathScore();
            case "英语":
                return student.getEnglishScore();
            default:
                throw new IllegalArgumentException("Invalid subject: " + subject);
        }
    }


    // 获取科目比较器
    /**
     * 获取指定科目的学生比较器的私有方法。
     * 根据指定的科目和分数比较器返回相应学生的比较器。
     *
     * @param subject         要比较的科目
     * @param scoreComparator 分数的比较器
     * @return 学生比较器
     * @throws IllegalArgumentException 如果提供的科目无效
     */
    private Comparator<Student> getSubjectComparator(String subject, Comparator<Integer> scoreComparator) {
        Comparator<Student> comparator;
        switch (subject) {
            case "语文":
                comparator = Comparator.comparingInt(Student::getChineseScore);
                break;
            case "数学":
                comparator = Comparator.comparingInt(Student::getMathScore);
                break;
            case "英语":
                comparator = Comparator.comparingInt(Student::getEnglishScore);
                break;
            default:
                throw new IllegalArgumentException("Invalid subject: " + subject);
        }

        return comparator.thenComparing(Comparator.comparingInt(student -> getSubjectScore(student, subject)));
    }


    /**
     * 计算指定学生在整个学生列表中的排名的方法。
     * 排名按照学生的平均分从高到低排序。
     *
     * @param student 要计算排名的学生对象
     * @return 学生在整个学生列表中的排名
     */
    public int calculateRank(Student student) {
        List<Student> sortedStudents = students.stream()
                .sorted(Comparator.comparingDouble(s -> ((double) s.getChineseScore() + s.getMathScore() + s.getEnglishScore()) / 3.0))
                .collect(Collectors.toList());

        int rank = 1;

        // 遍历排序后的学生列表，找到指定学生并返回其排名
        for (Student s : sortedStudents) {
            if (s == student) {
                break;
            }
            rank++;
        }

        return rank;
    }

}


