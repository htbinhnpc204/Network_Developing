package StringHelper;

public class StringHelper {

    public static String Reverse(String s) {
        s = s.trim();
        return new StringBuilder().append(s).reverse().toString();
    }

    public static String removeSpace(String s) {
        s = s.trim();
        String result = "" + s.charAt(0);
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == ' ' && s.charAt(i - 1) == ' ') {
                continue;
            }
            result += s.charAt(i);
        }
        return result;
    }

    public static String Mix(String s) {
        String result = "";
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result += Character.toLowerCase(c);
            } else {
                result += Character.toUpperCase(c);
            }
        }
        return result;
    }

    public static int getNumWords(String s) {
        s = s.trim();
        if (s.length() == 0)
            return 0;
        int count = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == ' ' && s.charAt(i - 1) != ' ') {
                count++;
            }
        }
        return count;
    }

    private static boolean isVowel(char c) {
        return c == 'u' || c == 'e' || c == 'o' || c == 'a' || c == 'i';
    }

    public static String getVowel(String s) {
        String vowel = "";
        for (char c : s.toCharArray()) {
            if (isVowel(Character.toLowerCase(c))) {
                vowel += c + " ";
            }
        }
        return vowel;
    }

    public static void wordPrinter(String s) {
        s = removeSpace(s);
        int old = 0;
        for (int index = s.indexOf(' '); index >= 0; index = s.indexOf(' ', index + 1)) {
            System.out.println(s.substring(old, index));
            if (index != s.length()) {
                old = index + 1;
            }
        }
        if (old != s.length()) {
            System.out.println(s.substring(old, s.length()));
        }
    }

    public static float getPercentage(long times, String s) {
        return (float) (times * 100.0 / s.length());
    }

    public static void percentageTable(String s) {
        String unique = "";
        for (char c : s.toCharArray()) {
            if (unique.indexOf(c) == -1) {
                unique += c;
            }
        }
        System.out.printf("%15s %15s %15s", "Character", "Apear times", "Percentage");
        System.out.printf("\n---------------------------------------------------------");
        for (char c : unique.toCharArray()) {
            long times = s.chars().filter(ch -> ch == c).count();
            System.out.printf("\n%15s %15d %15.1f%%", c == ' ' ? "[ ]" : c, times, getPercentage(times, s));
        }
    }
}
