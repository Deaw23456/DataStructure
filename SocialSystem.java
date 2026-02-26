import java.util.*;

// 1. โครงสร้างโหนดสำหรับเก็บข้อมูลคนในกราฟ
class Person {
    String name;
    String age;
    List<Person> groupNeighbors = new ArrayList<>();  // เส้นเชื่อมเพื่อนในกลุ่ม
    List<Person> randomNeighbors = new ArrayList<>(); // เส้นเชื่อมเพื่อนจากการสุ่ม
    // คอนสตรัคเตอร์สำหรับสร้างโหนดใหม่
    public Person(String name, String age) {
        this.name = name;
        this.age = age;
    }
    // เมธอดสำหรับเชื่อมเพื่อนในกลุ่ม
    public void addGroupFriend(Person p) {
        if (!groupNeighbors.contains(p)) {
            groupNeighbors.add(p);
            p.groupNeighbors.add(this);
        }
    }
    // เมธอดสำหรับเชื่อมเพื่อนที่นิกเหนือจากในกลุ่ม
    public void addRandomFriend(Person p) {
        if (!randomNeighbors.contains(p)) {
            randomNeighbors.add(p);
            p.randomNeighbors.add(this);
        }
    }
}

public class SocialSystem {
    private static Map<String, Person> allNodes = new HashMap<>();
    private static List<Person> groupList = new ArrayList<>();
    private static List<Person> randomList = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);
    // เมนูหลักของระบบ
    public static void main(String[] args) {

        while (true) {
            System.out.println("\n========= 🌐 CLASSROOM FRIEND-FINDING SYSTEM =========");
            System.out.println("1. NEW GROUP");
            System.out.println("2. ADD OTHER FRIENDS");
            System.out.println("3. GROUP MEMBERS");
            System.out.println("4. THIS PERSON IS A FRIEND OF");
            System.out.println("0. EXIT");
            System.out.print("👉 ENTER_MENU: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addGroupMember();
                case 2 -> addRandomConnection();
                case 3 -> displayTable(groupList, "GROUP MEMBERS");
                case 4 -> displayTable(randomList, "OUT GROUP FRIENDS");
                case 0 -> {
                    System.out.println("EXIT_PROGRAM...");
                    return;
                }
            }
        }
    }

    // Thai language Tabel
    public static String formatThai(String str, int length) {
        int thaiExtra = 0;
        if (str == null) return "";
        for (char c : str.toCharArray()) {
            if ((c >= 0x0E31 && c <= 0x0E3A) || (c >= 0x0E47 && c <= 0x0E4E)) {
                thaiExtra++;
            }
        }
        return String.format("%-" + (length + thaiExtra) + "s", str);
    }

    // สร้างตาราง SQL Style 
    private static void displayTable(List<Person> list, String title) {
        System.out.println("\n-- " + title + " --");
    
        String line = "+-----------------+------------+------------------------------------------+";
        System.out.println(line);
        System.out.printf("| %-15s | %-10s | %-40s |\n", "NAME", "AGE", "FRIENDS (CONNECTED)");
        System.out.println(line);

        for (Person p : list) {
            StringBuilder friends = new StringBuilder();
            for(Person f : p.groupNeighbors) friends.append(f.name).append(",");
            for(Person f : p.randomNeighbors) friends.append(f.name).append(",");
            
            String friendStr = friends.length() > 0 ? friends.substring(0, friends.length()-1) : "None";
            System.out.printf("| %s | %s | %s |\n", formatThai(p.name, 15), formatThai(p.age, 10), formatThai(friendStr, 40));
        }
        System.out.println(line);
    }
    // เพิ่มสมาชิกใหม่เข้าสู่กลุ่มและเชื่อมกัน
    private static void addGroupMember() {
    System.out.print("ADD MEMBER NAME: "); String name = sc.nextLine();
    System.out.print("AGE: "); String age = sc.nextLine();
    
    Person n = new Person(name, age);
    allNodes.put(name.toLowerCase(), n);

    // เชื่อมสมาชิกทุกคนในกลุ่ม
    for (Person existingMember : groupList) {
        n.addGroupFriend(existingMember); 
    }
    // เพิ่มสมาชิกใหม่เข้าสู่กลุ่ม
    groupList.add(n);
    System.out.println("✅ ADD " + name + "SUCCESSFULLY!!");
}
    // เพิ่มเพื่อนแบบสุ่มและเชื่อมกับสมาชิกทีคนใดคนหนึ่งในกลุ่ม
    private static void addRandomConnection() {
        System.out.print("ADD FRIENDS: "); String name = sc.nextLine();
        System.out.print("AGE: "); String age = sc.nextLine();
        System.out.print("ADD BY: "); String voucher = sc.nextLine().toLowerCase();

        if (allNodes.containsKey(voucher)) {
            Person randomP = new Person(name, age);
            Person member = allNodes.get(voucher);
            randomP.addRandomFriend(member);
            allNodes.put(name.toLowerCase(), randomP);
            randomList.add(randomP);
            System.out.println("✅ ADD " + name + " (CERTIFIED BY: " + member.name + ")");
        } else {
            System.out.println("❌ NO MEMBER WERE FOUND IN THE SPECIFIED GROUP.!");
        }
    }
}