package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerSC {
	
	String nickname = null;
	InputStream receiveStream = null;
	OutputStream sendStream = null;
	Socket nowSocket = null;
	byte[] msgBuffer = new byte[100];
	ServerSetting server = null;
	
	ServerSC(Socket ClientSocket , ServerSetting server){
		this.nowSocket = ClientSocket;
		this.server = server;
		init();
		receive();
	}
	private void init() {
		try {
			receiveStream = nowSocket.getInputStream();
			sendStream = nowSocket.getOutputStream();
			receiveStream.read(msgBuffer);
			nickname = new String(msgBuffer).trim();
			System.out.println(nickname+" 입장");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void receive() { // 객체 생성될때마다 thread 생성
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						msgBuffer = new byte[100];
						receiveStream.read(msgBuffer);
						String info = new String(msgBuffer).trim();
						server.sendPoint(info); // 좌표 전송
						server.sendMsg(info,nickname); // 채팅 전송
						server.sendFlag(info,nickname); // regameFlag 전송
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break; // 루프방지
					}
				}
				
			}
		}).start();
	}
	
}
