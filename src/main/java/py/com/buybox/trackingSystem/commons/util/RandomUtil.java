package py.com.buybox.trackingSystem.commons.util;

import java.util.Random;

public class RandomUtil {

    public static Random random = new Random();

    public static String stringRandom(int length){
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_*-".toCharArray();
        return randStr(chars, length);
    }

    public static String stringRandomWithoutSymbols(int length){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        return randStr(chars, length);
    }

    private static String randStr(char[] chars, int length){
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
