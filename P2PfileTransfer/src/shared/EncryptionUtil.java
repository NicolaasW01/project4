package shared;

public class EncryptionUtil {
    static int key = 6;
    public static String encrypt(String plainText) {
        char[] chars = plainText.toCharArray();
        StringBuilder result = new StringBuilder();

        for (char c : chars) {
            c += key;
            result.append(c);
        }
        //System.out.println(result.toString());
        return result.toString();
        
    }

    public static String decrypt(String cipherText) {
        char[] chars = cipherText.toCharArray();
        StringBuilder result = new StringBuilder();

        for (char c : chars) {
            c -= key;
            result.append(c);
        }
     //   System.out.println(result.toString());
        return result.toString();
    }

    public static void main(String[] args) {

        decrypt("");

    }
}
