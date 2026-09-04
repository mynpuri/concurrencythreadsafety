/**
 * การโอนเงินระหว่างสองบัญชี — ไฟล์ที่นิสิตต้องแก้ (ส่วนที่ 2)
 *
 * การโอนต้องล็อกสองใบพร้อมกัน เพราะระหว่างที่หักจากบัญชีต้นทาง
 * แล้วยังไม่ทันเพิ่มให้ปลายทาง เงินจะ "หายไปจากระบบ" ชั่วขณะ
 * ถ้ามีใครมาอ่านยอดรวมตอนนั้นจะได้ตัวเลขที่ผิด
 *
 * ปัญหาคือ การถือล็อกสองใบพร้อมกัน คือสูตรสำเร็จของ deadlock
 */
public class Bank {

    private Bank() {
        // utility class — ห้ามสร้าง object
    }

    /**
     * โอนเงินจากบัญชีหนึ่งไปอีกบัญชีหนึ่ง
     *
     * @return true ถ้าโอนสำเร็จ, false ถ้าเงินต้นทางไม่พอ
     * @throws IllegalArgumentException ถ้า argument ไม่ถูกต้อง หรือโอนเข้าบัญชีตัวเอง
     */
    public static boolean transfer(Account from, Account to, int amount) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("accounts must not be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (from == to) {
            throw new IllegalArgumentException("cannot transfer to the same account");
        }

        // ---------------------------------------------------------------
        // TODO 2  ลำดับการล็อก
        //
        // ตอนนี้ลำดับล็อกขึ้นกับว่าใครโอนให้ใคร:
        //     transfer(A, B, ...) จะล็อก A ก่อน แล้วค่อย B
        //     transfer(B, A, ...) จะล็อก B ก่อน แล้วค่อย A
        //
        // ถ้าสองอย่างนี้เกิดพร้อมกัน ต่างฝ่ายต่างถือสิ่งที่อีกฝ่ายรอ
        // ไม่มี error ไม่มี exception โปรแกรมแค่ค้างเงียบ ๆ
        //
        // งานของคุณ: ทำให้ทุกเธรดขอล็อกใน "ลำดับเดียวกันเสมอ"
        //            ไม่ว่าจะโอนไปทางไหน โดยใช้ from.id() และ to.id()
        //
        // ห้ามแก้ด้วยการเอาล็อกใบใดใบหนึ่งออก — ยอดรวมจะเพี้ยน
        // ---------------------------------------------------------------
        Account first = from ;
        Account second = to ;
        if(from.id()>to.id()){
            first = to ;
            second = from ;
        }

        synchronized (first) {
            synchronized (second) {
                if (!from.withdraw(amount)) {
                    return false;
                }
                to.deposit(amount);
                return true;
            }
        }
    }
}
