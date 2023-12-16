import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


class Student {

    double averageScore;
    int rank;
    public String getClassName() {
        return className;
    }
    public int getChineseScore() {
        return chineseScore;
    }
    public int getMathScore() {
        return mathScore;
    }
    public int getEnglishScore() {
        return englishScore;
    }

    public void calculateAverageScore() {
        this.averageScore = (double) (chineseScore + mathScore + englishScore) / 3.0;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
    // 在 Student 类中添加两个方法
    public double calculateExcellentRate() {
        int excellentCount = (chineseScore >= 90 ? 1 : 0) + (mathScore >= 90 ? 1 : 0) + (englishScore >= 90 ? 1 : 0);
        return (double) excellentCount / 3.0;
    }

    public double calculateFailRate() {
        int failCount = (chineseScore < 60 ? 1 : 0) + (mathScore < 60 ? 1 : 0) + (englishScore < 60 ? 1 : 0);
        return (double) failCount / 3.0;
    }
    public void setEnglishScore(int englishScore) {
        this.englishScore = englishScore;
    }
    public void setMathScore(int mathScore) {
        this.mathScore = mathScore;
    }
    public void setChineseScore(int chineseScore) {
        this.chineseScore = chineseScore;
    }
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

    // 省略其他属性和方法...

    // 添加以下方法
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        System.out.println("Debug: " + studentId + "," + name + "," + className + "," + chineseScore + "," + mathScore + "," + englishScore);
        return studentId + "," + name + "," + className + "," + chineseScore + "," + mathScore + "," + englishScore;
    }
    public String getName() {
        return name;
    }

}

public class StudentDatabase {

    private List<Student> students;
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

    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("student_database.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Student student = new Student();
                student.studentId = parts[0];
                student.name = parts[1];
                student.className = parts[2];
                student.chineseScore = Integer.parseInt(parts[3]);
                student.mathScore = Integer.parseInt(parts[4]);
                student.englishScore = Integer.parseInt(parts[5]);
                students.add(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 根据关键字查询学生信息（学生姓名、学号、班级、课程名称）
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

    // 插入新学生信息
    public void insertStudent(Student student) {
        students.add(student);
        saveDataToFile();
        saveToUndoStack();
    }

    // 删除学生信息
    public boolean deleteStudent(String studentId) {
        students.removeIf(student -> student.getStudentId().equals(studentId));
        saveDataToFile();
        saveToUndoStack();
        return false;
    }

    // 更新学生信息
    public void updateStudent(Student updatedStudent) {
        deleteStudent(updatedStudent.getStudentId());
        insertStudent(updatedStudent);
    }

    // 主方法用于交互
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentDatabase database = new StudentDatabase();

        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 查询学生信息");
            System.out.println("2. 排序学生信息");
            System.out.println("3. 插入新学生");
            System.out.println("4. 删除学生信息");
            System.out.println("5. 更新学生信息");
            System.out.println("0. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (choice) {
                case 1:
                    System.out.println("请输入关键字:");
                    String keyword = scanner.nextLine();
                    System.out.println("请输入查询字段:");
                    String field = scanner.nextLine();
                    List<Student> result = database.queryStudents(keyword, field);
                    System.out.println("查询结果：" + result);
                    break;

                case 2:
                    System.out.println("请输入排序条件:");
                    String criteria = scanner.nextLine();
                    List<Student> sortedStudents = database.sortStudents(criteria);
                    System.out.println("排序后的学生：" + sortedStudents);
                    break;

                case 3:
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
                    System.out.println("请输入要删除的学生学号:");
                    String studentIdToDelete = scanner.nextLine();
                    database.deleteStudent(studentIdToDelete);
                    System.out.println("学生信息删除成功！");
                    break;

                case 5:
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
    public void insertStudents(List<Student> newStudents) {
        students.addAll(newStudents);
        saveDataToFile();
    }

    public List<Student> getStudents() {
        return this.students;
    }

    // 在 StudentDatabase 类中添加以下两个方法
    public List<Student> queryStudentsByName(String name) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().equals(name)) {
                result.add(student);
            }
        }
        return result;
    }
    // 查询最高分学生
    public List<Student> queryMaxScoreStudents(String subject) {
        Comparator<Student> comparator = getSubjectComparator(subject, Comparator.reverseOrder());
        int maxScore = students.stream().max(comparator).map(student -> getSubjectScore(student, subject)).orElse(-1);

        return students.stream()
                .filter(student -> getSubjectScore(student, subject) == maxScore)
                .map(student -> {
                    student.calculateAverageScore(); // 计算平均分
                    student.setRank(students.indexOf(student) + 1); // 设置排名
                    return student;
                })
                .collect(Collectors.toList());
    }

    // 查询最低分学生
    public List<Student> queryMinScoreStudents(String subject) {
        Comparator<Student> comparator = getSubjectComparator(subject, Comparator.naturalOrder());
        int minScore = students.stream().min(comparator).map(student -> getSubjectScore(student, subject)).orElse(-1);

        return students.stream()
                .filter(student -> getSubjectScore(student, subject) == minScore)
                .map(student -> {
                    student.calculateAverageScore(); // 计算平均分
                    student.setRank(students.indexOf(student) + 1); // 设置排名
                    return student;
                })
                .collect(Collectors.toList());
    }

    // 获取学生的科目分数
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


    public int calculateRank(Student student) {
        List<Student> sortedStudents = students.stream()
                .sorted(Comparator.comparingDouble((Student s) -> ((double) s.getChineseScore() + s.getMathScore() + s.getEnglishScore()) / 3.0))

                .collect(Collectors.toList());

        int rank = 1;
        for (Student s : sortedStudents) {
            if (s == student) {
                break;
            }
            rank++;
        }
        return rank;
    }

}


