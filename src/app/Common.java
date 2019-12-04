package app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class Common {
	private static final boolean PRINT_EXCEPTION_TRACE = false;
	private static final int BUFFER_SIZE = 1024;
	private static final byte[] buf = new byte[BUFFER_SIZE];
	
	
	public static DatagramPacket receivePacketAtSocket(DatagramSocket socket) {
		zeroInputBuffer(buf);
		DatagramPacket packet=new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
		} catch (IOException e1) {
			System.out.println("B³¹d w recieve!");
			if(PRINT_EXCEPTION_TRACE)
				e1.printStackTrace();
			return null;
		}
		return packet;
	}
	
	public static boolean sendPacketFromSocket(DatagramPacket packet, DatagramSocket socket) {
		try {
			socket.send(packet);
		} catch (IOException e1) {
			System.out.println("B³¹d w send!");
			if(PRINT_EXCEPTION_TRACE)
				e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static void zeroInputBuffer(byte[] buf) {
		Arrays.fill(buf, (byte)'\0');
	}
}
