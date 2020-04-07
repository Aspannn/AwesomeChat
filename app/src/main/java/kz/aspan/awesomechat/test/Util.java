package kz.aspan.awesomechat.test;

import com.google.firebase.auth.FirebaseAuth;

public class Util {
    public static final String SENDER = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

    public static final String RECEIVER = getReceiverNumber();

    private static String getReceiverNumber() {
        if (SENDER.equals("+77073098583")) {
            return "+77074807466";
        } else {
            return "+77073098583";
        }
    }

//    public static final String SENDER = "+77073098583";
//    public static final String RECEIVER = "+77074807466";

//    public static final String SENDER = "+77074807466";
//    public static final String RECEIVER = "+77073098583";


}
// todo  sign up ka auystyryp koru  osy kottardy