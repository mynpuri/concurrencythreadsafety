/**
 * บัญชีธนาคาร — ไฟล์ที่นิสิตต้องแก้ (ส่วนที่ 1)
 *
 * ตอนนี้คลาสนี้ ยังไม่ปลอดภัยต่อเธรด
 * balance คือ shared mutable state: หลายเธรดมองเห็นและเขียนทับกันได้
 *
 * อย่าเพิ่งแก้อะไรจนกว่าจะทำ Stage 1 ในไฟล์ README.md เสร็จ
 * ต้องเห็นมันพังด้วยตาตัวเองก่อน แล้วค่อยแก้
 */
public class Account {

    private final int id;

    /** ยอดเงินคงเหลือ — จุดที่เธรดหลายตัวแย่งกันเขียน */
    private int balance;

    /**
     * @param id            เลขบัญชี ต้องไม่ซ้ำกันในระบบเดียวกัน
     * @param initialBalance ยอดตั้งต้น ต้องไม่ติดลบ
     */
    public Account(int id, int initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("initialBalance must not be negative");
        }
        this.id = id;
        this.balance = initialBalance;
    }

    /** เลขบัญชี — ค่านี้ไม่เปลี่ยนตลอดอายุ object จึงไม่ต้องคุ้มครอง */
    public int id() {
        return id;
    }

    // ---------------------------------------------------------------
    // TODO 1.3  อ่านอย่างเดียวก็ต้องคุ้มครอง
    //
    // เมธอดนี้ไม่ได้เขียนอะไรเลย แล้วทำไมยังต้องล็อก?
    // คำใบ้: ถ้าเธรด A เพิ่งเขียน balance ลงไป เธรด B ที่อ่านตอนนี้
    //        รับประกันได้หรือไม่ว่าจะเห็นค่าใหม่ ไม่ใช่ค่าเก่าที่ค้างในแคชของ CPU
    // ---------------------------------------------------------------
    public synchronized int balance() {
        return balance;
    }

    // ---------------------------------------------------------------
    // TODO 1.1  read-modify-write
    //
    // บรรทัด balance = balance + amount; ดูเหมือนคำสั่งเดียว
    // แต่จริง ๆ คือ อ่าน → บวก → เขียน สามจังหวะที่ถูกแทรกกลางคันได้
    // ---------------------------------------------------------------
    public synchronized void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        balance = balance + amount;
    }

    // ---------------------------------------------------------------
    // TODO 1.2  check-then-act
    //
    // ตรงนี้อันตรายกว่า deposit เพราะมีการ "ตรวจก่อนแล้วค่อยทำ"
    // ระหว่าง if (balance >= amount) กับบรรทัดถัดไป เธรดอื่นถอนไปแล้วได้
    // ผลคือยอดติดลบ ทั้งที่โค้ดมี if ป้องกันอยู่ชัด ๆ
    //
    // @return true ถ้าถอนสำเร็จ, false ถ้าเงินไม่พอ
    // ---------------------------------------------------------------
    public synchronized boolean withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (balance >= amount) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account#" + id + "(" + balance() + ")";
    }
}
