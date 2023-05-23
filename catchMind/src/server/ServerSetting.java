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
	private String[][] wordarr = {{"과일","동물","전자기기","패스트푸드","과자"},
			{"파인애플","고릴라","식기세척기","햄버거","새우깡"}};
	private int tema = -1;
	ServerSetting(){
		init();
	}

	private void init() {
		try {
			server = new ServerSocket();
			server.bind(new InetSocketAddress("192.168.0.25",7777));
			while(true) {
				toClient = server.accept(); // 사용자 입장 대기
				sc = new ServerSC(toClient, this); // 사용자 정보 + 소켓 받아서 저장 + 현재 객체정보 갱신
				serverList.add(sc);
				if(serverList.size()>2) { // 3인용으로 설정
					sendTurn();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//서버의 역할부여
	public void sendTurn() { // 2명 이상부터 역할부여 가능
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
	// 좌표 전송
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
	// 채팅 전송
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
	// 채팅에 정답이 있는 경우 regameFlag 전송
	public void sendFlag(String msg, String nickname) {
		if(msg.contains("**")) {
			if(msg.contains(wordarr[1][tema])) {
				String answer = "$"+nickname+" 정답입니다.";
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
