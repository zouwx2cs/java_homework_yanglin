import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * by zwx@bnu 2017-06-08
 **/

public class Print {
	//2017-06-08 09:38:03: str
	/**
	 * 打印带有时间戳的字符串，并写入到日志文件中
	 * 输出样例 "2017-06-08 09:38:03: str"
	 * @param logName 日志文件名
	 * @param str 打印的字符串
	 */
	public static void i(String logName, String str) {
		String o = DateTime.GetNowDate() + str ;
		LogWrite(logName, o) ;
		System.out.println(o) ;
	}
	
	public static void w(String logName, String str) {
		String o = DateTime.GetNowDate() + str ;
		LogWrite(logName, o) ;
	}
	
	//2017-06-08 09:38:09: >>> str
	public static void send(String str) {
		System.out.println(DateTime.GetNowDate() + ">>> " + str) ;
	}
	
	//2017-06-08 09:38:09: <<< str
	public static void receive(String str) {
		System.out.println(DateTime.GetNowDate() + "<<< " + str) ;
	}
	
	//2017-06-08 09:38:09: Err str
	public static void e(String logName, String str) {
		String o = DateTime.GetNowDate() + "Err " + str ;
		LogWrite(logName, o) ;
		System.out.println(o) ;
	}
	
	/**仅在屏幕上显示str，不写入文件
	 * 2017-06-08 09:38:09: str
	 * @param str要显示的内容
	 */
	public static void sysOut(String str) {
		System.out.println(str) ;
	}
	
	/**
	 * 以增量的方式写入文件
	 * @param logName要写入文件的文件名
	 * @param content要写入的内容
	 */
	private static void LogWrite(String logName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			logName += ".log" ;
			
			RandomAccessFile randomFile = new RandomAccessFile(logName, "rw") ;
			// 文件长度，字节数
			long fileLength = randomFile.length() ;
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength) ;
			//RandomAccessFile是按字节访问的，而utf-8一个字符不止一个字节哦
			randomFile.write((content + "\r\n").getBytes("utf-8")) ;
			randomFile.close() ;
		} catch (IOException e) {
			e.printStackTrace() ;
		}
	}
}
