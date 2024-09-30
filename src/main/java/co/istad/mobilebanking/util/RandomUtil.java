package co.istad.mobilebanking.util;

import java.util.Random;

public class RandomUtil {
    public static String generateCode(){
        Random random=new Random();
        int sixDigitNumber= random.nextInt(999999);
        return String.format("%06d",sixDigitNumber);
    }
}
