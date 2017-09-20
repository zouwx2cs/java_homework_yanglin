
public class Math {
	private static int Add(int a, int b) {
		return a+b ;
	}
	public static String Add(String query_string) {
		String para[] = query_string.split("&") ;
		String a = para[0].split("=")[1] ;
		String b = para[1].split("=")[1] ;
		
		return a + " + " + b + " = " + 
			String.valueOf(Add(Integer.valueOf(a) ,Integer.valueOf(b))) ;
	}
	public static String Color(String query_string) {
		String color = query_string.split("=")[1] ;
		String buf = "" ;
	    
	    buf = "<!doctype HTML><HTML><head><meta charset=\"utf-8\"><TITLE>Not found</TITLE>"
	    		+ "<style type=\"text/css\">body {background-color:" + color + ";}</style>"
	    		+ "</head>\r\n" ;
	    buf += "<BODY><h3>欢迎访问我的页面</h3>\r\n" ;
	    buf += "<p>该网站基于jtinyhttpd<p>\r\n" ;
	    buf += "\r\n" ;
	    buf += "WxZou's jtinyhttpd webserver v_0.0.1" ;
	    buf += "</BODY></HTML>\r\n" ;
		return buf ;
	}
	
	
}
