package app.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import app.Common;

public class UDPClientThread extends Thread {
	private static final boolean PRINT_EXCEPTION_TRACE = false;

	private static final int BUFFER_SIZE = 1024;
	private char[] buf = new char[BUFFER_SIZE];
	private BufferedReader input;

	private DatagramSocket clientSocket;

	private InetAddress serverAddr;
	private int serverPort;

	public UDPClientThread(String serverAddr, int serverPort) {
		super("UDP Client");
		try {
			this.serverPort = serverPort;
			this.serverAddr = InetAddress.getByName(serverAddr);
			clientSocket = new DatagramSocket(); // odpowiada bind + socket
			System.out.println("KLIENT UDP: dzia³a na porcie: " + clientSocket.getLocalPort() + ", adresie IP: " + clientSocket.getLocalAddress());
			input = new BufferedReader(new InputStreamReader(System.in));
		} catch (SocketException e) {
			System.out.println("B³¹d przy tworzeniu gniazda!");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("B³¹d: z³y adres IP serwera!");
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println(getName() + " started!");
		
		boolean run = true;

		while (run) {
			zeroInputBuffer(buf);
			System.out.print("KLIENT UDP: wprowadz dane lub zakoñcz (komenda: kill):");
			readDataFromInputStream(input, buf);
			String request = new String(buf);

			DatagramPacket toSendPacket = new DatagramPacket(request.getBytes(), request.length(), serverAddr, serverPort);
			run = Common.sendPacketFromSocket(toSendPacket, clientSocket);

			if(request.startsWith("kill"))
				break;

			DatagramPacket receivedPacket = Common.receivePacketAtSocket(clientSocket);
			if(receivedPacket==null)
				break;
			System.out.println(new String(receivedPacket.getData())); //print server response
		}

		clientSocket.close();
		System.out.println(getName() + " terminated!");
	}

	private static void zeroInputBuffer(char[] buf) {
		Arrays.fill(buf, '\0');
	}

	private boolean readDataFromInputStream(BufferedReader input, char[] buf) {
		try {
			input.read(buf);
		} catch (IOException e) {
			System.out.println("B³¹d w read input!");
			if(PRINT_EXCEPTION_TRACE)
				e.printStackTrace();
			return false;
		}
		return true;
	}
}
