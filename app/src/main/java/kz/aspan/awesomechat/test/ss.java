package kz.aspan.awesomechat.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ss {

    public int getNumber(String str) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return m.group().length();
        } else
            return 0;
    }
}
