package NumberHelper;

public class NumberHelper {
    public static int getNumDigits(int x) {
        int counter = 0;
        while (x != 0) {
            counter++;
            x /= 10;
        }
        return counter;
    }

    public static int getSumDigits(int x) {
        int Sum = 0;
        while (x != 0) {
            Sum += x % 10;
            x /= 10;
        }
        return Sum;
    }

    public static int getMultipleDigits(int x) {
        int Result = 1;
        while (x != 0) {
            Result *= x % 10;
            x /= 10;
        }
        return Result;
    }

    public static int getUCLN(int a, int b) {
        int Max = 0;
        for (int i = 1; i <= (a > b ? b : a); i++) {
            if (a % i == 0 && b % i == 0) {
                Max = i;
            }
        }
        return Max;
    }

    public static int getBCNN(int a, int b) {
        return (a * b) / getUCLN(a, b);
    }
}
