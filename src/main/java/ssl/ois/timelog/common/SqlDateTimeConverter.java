package ssl.ois.timelog.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlDateTimeConverter {
    public static String convert(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}