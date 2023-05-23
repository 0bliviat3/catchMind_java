package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ServerSetting { 
	
	private ServerSocket server;
	private Socket toClient;
	private ServerSC sc = null;
	private ArrayList<ServerSC> serverList = new ArrayList<>();
	private int turn = -1;
	private String[][] wordarr = {{"����","����","���ڱ��","�н�ƮǪ��","����"},
			{"���ξ���","����","�ı⼼ô��","�ܹ���","�����"}};
	private int tema = -1;
	ServerSetting(){
		init();
	}

	private void init() {
		try {
			server = new ServerSocket();
			server.bind(new InetSocketAddress("192.168.0.25",7777));
			while(true) {
				toClient = server.accept(); // ����� ���� ���
				sc = new ServerSC(toClient, this); // ����� ���� + ���� �޾Ƽ� ���� + ���� ��ü���� ����
				serverList.add(sc);
				if(serverList.size()>2) { // 3�ο����� ����
					sendTurn();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//������ ���Һο�
	public void sendTurn() { // 2�� �̻���� ���Һο� ����
		Random r = new Random();
		
		turn = r.nextInt(serverList.size());
		tema = r.nextInt(5);
		System.out.println("t"+turn);
		try {
			for(int i=0;i<serverList.size();i++) {
				if(turn == i) {
					serverList.get(i).sendStream.write(wordarr[1][tema].getBytes());
				}else {
					serverList.get(i).sendStream.write(wordarr[0][tema].getBytes());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	// ��ǥ ����
	public void sendPoint(String point) {
		for(int i=0;i<serverList.size();i++) {
			if(i != turn) {
				if((point.contains("!"))||(point.contains("@"))) {
					System.out.println(20000+serverList.get(i).nickname);
					try {
						serverList.get(i).sendStream.write(point.getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	// ä�� ����
	public void sendMsg(String msg,String nickname) {
		if(msg.contains("**")) {
			msg = nickname + msg;
			for(int i=0;i<serverList.size();i++) {
				try {
					serverList.get(i).sendStream.write(msg.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	// ä�ÿ� ������ �ִ� ��� regameFlag ����
	public void sendFlag(String msg, String nickname) {
		if(msg.contains("**")) {
			if(msg.contains(wordarr[1][tema])) {
				String answer = "$"+nickname+" �����Դϴ�.";
				for(int i=0;i<serverList.size();i++) {
					try {
						serverList.get(i).sendStream.write(answer.getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public ArrayList<ServerSC> getServerList() {
		return serverList;
	}
	
}
