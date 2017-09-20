import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

	/**
     *  WxZou's simple web server base on java 
     *  Created September 2017 by Zou Wuxing 
     *  20150915 (Java Programing), Prof. Yanglin
     *  Beijing Normal University
     *  
     *  This program compiles for Fedora 26 Workstation 64bit & java 1.8.0_144. 
     *  
     *  @author Zou Wuxing
     *  @version v0.0.1
     **/  
public class Httpd {
	//服务端header名称
	public static final String SERVERNAME = "Server: jtinyhttp/0.0.1\r\n" ;
	//本地回路
	public static final String LOCAL_HOST = "127.0.0.1" ;
	//服务端端口
	public static final int SERVER_PORT = 12345 ;
	//默认页文件名
	public static final String DEFAULT_PAGE = "index.html" ;
	//web文件地址
	public static final String WWW_PATH = "htmlwww" ;
	//日志文件名称,不用加.log后缀名
	public static final String logFileName = "Server" ;  
	//服务端socket
	private static ServerSocket SERVER_SOCKET = null ;
	
	private static ServerSocket startServer(int iPort) {
		ServerSocket SOCKET = null ;
    	try {
    		Print.i(logFileName, "[ Starting server... ]") ;
			SOCKET = new ServerSocket(iPort) ;
		} catch (Exception e) {
			Print.e(logFileName, "Server start failed: " + e) ;
			Print.e(logFileName, "[ Server exited. ]") ;
			System.exit(-1) ;
			e.printStackTrace() ;
		}
    	return SOCKET ;
    }
	
	public static void main(String[] args) {
		//开启服务端端口监听
		SERVER_SOCKET = startServer(SERVER_PORT) ;
		
		Socket socket = null ;
		try {
            while (true) {
                //循环监听客户端的连接（阻塞式）
                socket = SERVER_SOCKET.accept() ;
                //新建一个线程WebServerThread，并开启
                new WebServerThread(socket).start() ;
            }
        } catch (Exception e) {
        	Print.e(logFileName, e.toString()) ;
            e.printStackTrace() ;
        } finally {
	        try {
				socket.close() ;
				SERVER_SOCKET.close() ;
			} catch (IOException e) {
				e.printStackTrace() ;
				return ;
			}
        }
	}
}
