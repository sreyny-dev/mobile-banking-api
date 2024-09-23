package co.istad.mobilebanking.util;

public class ValidatePhoneNumberUtil {
     public static boolean phoneNumNonChar(String phoneNumber){
        for(char c:phoneNumber.toCharArray()){
            if(!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }
}
