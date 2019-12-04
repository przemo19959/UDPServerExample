package app.server;

import java.io.IOException;
import java.util.regex.Pattern;

public class UDPServer {
	private static final int DEFAULT_SERVER_PORT=8765;
	private static final Pattern PORT_PATTERN=Pattern.compile("[3-9]|[1-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9][0-9][0-9]|[1-6][0-5][0-5][0-3][0-5]"); 
	
	public static void main(String[] args) throws IOException {
		//1 - mo�liwo�� podania portu na kt�rym serwer nas�chuje, domy�lnie 8765
		if(args.length==1 && PORT_PATTERN.matcher(args[0]).matches())
			new UDPServerThread(Integer.valueOf(args[0])).start();
		else
			new UDPServerThread(DEFAULT_SERVER_PORT).start();			
    }
}
