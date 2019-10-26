package app.server;

import java.io.IOException;

public class EchoServer {
	private static final int DEFAULT_SERVER_PORT=8765;
	
	public static void main(String[] args) throws IOException {
        new EchoServerThread((args.length>0)?Integer.valueOf(args[0]):DEFAULT_SERVER_PORT).start();
    }
}
