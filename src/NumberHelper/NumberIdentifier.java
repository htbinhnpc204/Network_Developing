package NumberHelper;

public class NumberIdentifier {

    public static boolean isSNT(int x) {
        // SNT là số chỉ chia hết cho 1 và chính nó
        for (int i = 2; i < x / 2; i++) {
            if (x % i == 0) {
                // Nếu có số nào khác ngoài 1 và chính nó mà x % i == 0 thì return false
                return false;
            }
        }
        return true;
    }

    public static boolean isChinhPhuong(int x) {
        // Số chính phương là số bằng bình phương của 1 số tự nhiên
        int tmp = Math.round((float) Math.sqrt(x)); // Làm tròn căn bậc 2 của x sau đó bình phương và so sánh
        return tmp * tmp == x;
    }

    public static boolean isPerfect(int x) {
        // Số hoàn hảo là số mà tổng các ước (ngoại trừ chính nó) bằng nó
        int tmp = 0; // Tạo biến tmp để tính tổng các ước của x
        for (int i = 1; i <= x / 2; i++) { // Vòng lặp i - x /2 vì x / 2 là ước lớn nhất của x
            if (x % i == 0) {
                tmp += i;
            }
        }
        return tmp == x; // Kiểm tra tmp có bằng x hay không
    }

    public static boolean isAmstrong(int x) {
        int sum = 0, target = x;
        int digits = NumberHelper.getNumDigits(x);
        // System.out.println("Digits " + digits);
        while (x != 0) {
            int p = x % 10;
            x /= 10;
            sum += Math.pow(p, digits);
            // System.out.println(sum);
        }
        return sum == target;
    }
}
