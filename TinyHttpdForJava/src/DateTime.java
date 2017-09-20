import java.text.SimpleDateFormat ;
import java.util.Date ;
/**
 * 获取当前日期时间
 * by zwx@bnu 2017-06-08
 **/
public class DateTime {
	public static String  GetNowDate() {     
        String temp_str = "" ;     
        Date dt = new Date() ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss: ") ;     
        temp_str = sdf.format(dt) ;     
        return temp_str ;     
    } 
}
