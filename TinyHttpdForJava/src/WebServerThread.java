import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * web服务端 请求响应线程
 * @author stars
 *
 */
public class WebServerThread extends Thread {
	//日志文件名
	private String logFileName ;
	//服务端套接字
    private Socket socket ;

    /**
     * 构建一个客户端链接
     * @param socket 客户端套接字
     */
    public WebServerThread(Socket socket) {
        this.socket = socket ;
        this.logFileName = Httpd.logFileName ;
        
        try {
        	//设置超时时间(ms)
			this.socket.setSoTimeout(10*1000) ;
		} catch (SocketException e) { 
			e.printStackTrace() ;
		}
        
//        互斥访问临界资源       
//        synchronized(MultiServer.numConnection)  // this指的是调用这个方法的对象
//        {
//        	MultiServer.numConnection.addCnt() ;
//            Print.i(logFileName, String.valueOf(MultiServer.numConnection.getCnt()) + " threads in running +.") ;
//        }
        
        Print.i(logFileName, socket.toString() + "connected.") ;
    }
    
    private void unimplemented(int client)
    {
        ;
    }

   /**
    * 请求响应
    * @param is TCP输入流
    */
    private void acceptRequest(BufferedReader is) {
    	String buf ;
        String method ;
        String url ;
        String path ;
        //get，post方法标记
        boolean flgCgi = false ;
        String query_string = "" ;
        String tmpSplit[] ;

        //得到请求的第一行 
        try {
			if ((buf = is.readLine()) == null) 
				return ;
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
        //获取客户端的请求方法
        tmpSplit = buf.split(" ") ;
        method = tmpSplit[0] ;
        //url获取
        url = tmpSplit[1] ;
        //String httpVersion = tmpSplit[2] ;
        
        //如果既不是 GET 又不是 POST 则无法处理
        if (!method.equalsIgnoreCase("GET") 
        	&& !method.equalsIgnoreCase("POST"))
        {
            unimplemented(00);
            return;
        }
        
        //post标记
        if (method.equalsIgnoreCase("POST"))
        	flgCgi = true;

        //处理GET方法
        if (method.equalsIgnoreCase("GET"))
        {
            //获取get参数字符串
        	int indexQ = url.indexOf('?') ;
        	if (indexQ >= 0) {
        		query_string = url.substring(indexQ+1) ;
        		//开启 cgi
        		url = url.substring(0, indexQ) ;
        		flgCgi = true;
        	}
        }

        //确定资源文件地址
        path = Httpd.WWW_PATH + url ;
        //默认页为 index.html
        if (path.charAt(path.length()-1) == '/')
            path += Httpd.DEFAULT_PAGE ;
        //根据路径找到对应文件
        File file = new File(path) ;
        //如果文件不存在抛出Not Found页面
        if (!file.exists()) {
            not_found(socket);
        }
        else
        {
        	//如果是个目录，则默认使用该目录下 index.html
            if (file.isDirectory()) {
                path += Httpd.DEFAULT_PAGE ;
            }
            
            //不是动态页面
            if (!flgCgi) {
            	serve_file(socket, path) ;
            } else {
            	//执行动态页面处理
            	cgiJava(socket, path, query_string) ;
            }
        }
    }
    
    /**
     * 处理、执行动态资源
     * @param socket 客户端套接字
     * @param path 资源文件位置
     * @param query_string get、post参数
     */
    private void cgiJava(Socket socket, String path, String query_string) {
    	String buf = "" ;
		switch (path) {
			case "htmlwww" + "/add" :
			{
				buf = Math.Add(query_string) ;
				break ;
			}
			case "htmlwww/color" :
			{
				buf = Math.Color(query_string) ;
				break ;
			}
		}
		
		PrintWriter os = null ;

    	// 必须先关闭输入流才能获取下面的输出流
        try {
			socket.shutdownInput() ;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        // 获取输出流
        try {
			os = new PrintWriter(socket.getOutputStream()) ;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        //写 HTTP header
        headers(socket, os) ;
        //传送文件
        os.print(buf) ;
        os.flush() ;
	}

    /**
     * 向客户端发送一个资源文件
     * @param socket 客户端套接字
     * @param path 资源文件路径
     */
    private void serve_file(Socket socket, String path) {
    	//根据路径找到对应文件
        File fileResource = new File(path) ;
        if (!fileResource.exists()) {
            //找不到资源
            not_found(socket) ;
        }
        else
        {
        	//String buf ;
        	PrintWriter os = null ;

        	// 必须先关闭输入流才能获取下面的输出流
            try {
    			socket.shutdownInput() ;
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
            // 获取输出流
            try {
    			os = new PrintWriter(socket.getOutputStream()) ;
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
            
            //写 HTTP header
            headers(socket, os) ;
            //传送页面
            cat(os, fileResource) ;
            
            os.flush() ;
        }
	}

    /**
     * 向客户端发送一个html资源文件
     * @param os 客户端输出的输出流
     * @param fileResource 资源文件
     */
    void cat(PrintWriter os, File fileResource)
    {
	    BufferedReader reader = null ;
	    try {
	        reader = new BufferedReader(new FileReader(fileResource)) ;
	        String tempString = null ;
	        // 一次读入一行，直到读入null为文件结束
	        while ((tempString = reader.readLine()) != null) {
	            os.print(tempString) ;
	        }
	        os.flush() ;
	        reader.close() ;
	    } catch (IOException e) {
	        e.printStackTrace() ;
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close() ;
	            } catch (IOException e1) {
	            	e1.printStackTrace() ;
	            }
	        }
	    }
    }
    
    /**
     * 向客户端提供http消息头
     * @param socket 客户端套接字
     * @param os 向客户端传输的输出流
     */
    private void headers(Socket socket, PrintWriter os)
    {
        String buf ;
        /*正常的 HTTP header */
        buf = "HTTP/1.0 200 OK\r\n" ;
        os.print(buf) ;
        /*服务器信息*/
        os.print(Httpd.SERVERNAME) ;
        os.print("Content-Type: text/html\r\n");
        os.print("\r\n");
        os.flush() ;
    }

    /**
     * 向客户端发出一个404 Not Found提示页
     * @param socket 客户端的套接字
     */
    private void not_found(Socket socket) {
    	String buf ;
    	PrintWriter os = null ;

    	// 必须先关闭输入流才能获取下面的输出流
        try {
			socket.shutdownInput() ;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        // 获取输出流
        try {
			os = new PrintWriter(socket.getOutputStream()) ;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
	    // 404 页面
	    buf = "HTTP/1.0 404 NOT FOUND\r\n" ;
	    os.print(buf) ;
	    os.print(Httpd.SERVERNAME) ;
	    os.print("Content-Type: text/html\r\n") ;
	    os.print("\r\n") ;
	    
	    os.print("<!doctype HTML><HTML><head><meta charset=\"utf-8\"><TITLE>Not found</TITLE></head>\r\n") ;
	    os.print("<BODY><h3>该页无法访问</h3>\r\n") ;
	    os.print("<p>您所访问的页面它不存在啊，我也没办法啊！<p>\r\n") ;
	    os.print("\r\n") ;
	    os.print("WxZou's jtinyhttpd webserver v_0.0.1") ;
	    os.print("</BODY></HTML>\r\n") ;
	    os.flush() ;
	}

	@Override
    public void run() 
    {
        BufferedReader is = null ;
        //PrintWriter os = null ;
         
        //如果不是指定IP连接，则关闭服务
//		String strIpSrc = socket.getInetAddress().getHostAddress() ;
//		if (!strIpSrc.equals(CONF.localhost) && !strIpSrc.equals("192.168.0.0"))
//		{
//			try {
//				socket.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return ;
//		}
        
        //为了加快字符串拼接的速度，采用StringBuilder而不是String
        //StringBuilder strBuilder = new StringBuilder() ;
        try {
        	//输入流
            is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")) ;
            //响应请求
            acceptRequest(is) ;
            socket.shutdownOutput() ;
        } catch (Exception e) {
        	Print.e(logFileName, "" + e) ;
            e.printStackTrace() ;
        } finally {
            try {
				socket.close() ;
				Print.i(logFileName, socket.toString() + "disconnected.") ;
			} catch (IOException e) {
				Print.e(logFileName, "" + e) ;
				e.printStackTrace();
			}
        }
    }
}
