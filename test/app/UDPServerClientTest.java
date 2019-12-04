package app;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

import app.client.UDPClientThread;
import app.server.UDPServerThread;

class UDPServerClientTest {

	@Test
	void test() {
		new UDPServerThread(8765).start();
		new UDPClientThread("127.0.0.1", 8765).start();
		
		String data = "My-input-data";
		System.setIn(new ByteArrayInputStream(data.getBytes()));
		
		
	}

}
