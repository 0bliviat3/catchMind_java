package client;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JTextArea;

public class ClientSC {
	
	private Socket toServer;
	private InputStream receiveStream = null;
	private OutputStream sendStream = null;
	private byte[] msgBuffer = new byte[100];
	private String turn = null;
	private GameCanvas gameArea = null;
	private JTextArea chatArea = null;
	private ClientGUI gui = null;

	ClientSC(String id,GameCanvas gameArea,JTextArea chatArea,ClientGUI gui){
		this.gui = gui;
		this.gameArea = gameArea;
		this.chatArea = chatArea;
		init(id);
		receive();
	}
	
	private void init(String id) {
		try {
			toServer = new Socket("192.168.0.25",7777);
			sendStream = toServer.getOutputStream();
			sendStream.write(id.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 좌표보내는 method
	 * @param p : 인자값으로 Point객체를 받습니다.
	 */
	public void sending(Point p) {
		try {
			String x = String.valueOf(p.getX());
			String y = String.valueOf(p.getY());
			x = x.substring(0,x.indexOf("."));
			y = y.substring(0,y.indexOf("."));
			String point = x+"!"+y+"!";
			sendStream.write(point.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * receive Thread method
	 */
	public void receive() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						msgBuffer = new byte[100];
						receiveStream = toServer.getInputStream();
						receiveStream.read(msgBuffer);
						turn = new String(msgBuffer).trim();
						System.out.println(turn);
						if(turn.contains("!")) {
							castingPoint(turn);
						}else if(turn.equals("@")) {
							newList(turn);
						}else if(turn.contains("**")) { // 채팅일때
							System.out.println(turn);
							castingChat(turn);
						}else if(turn.contains("$")) { // gameset
							gameSet(turn);
						}else { // 역할부여
							gui.setMsg(turn);
						}
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
				
			}
		}).start();
	}
	/**
	 * Point로 형변환후 gameCanvas 객체로 넘겨주는 method
	 * @param point : x좌표!y좌표의 형식으로 오는 문자열을 인자값으로 받습니다.
	 */
	private void castingPoint(String point) {
		StringTokenizer stz = new StringTokenizer(point,"!@");
		while(stz.hasMoreTokens()) {
			int x = Integer.valueOf(stz.nextToken());
			int y = Integer.valueOf(stz.nextToken());
			System.out.println(x+"/"+y);
			gameArea.setList(new Point(x,y));
			//repaint
			gameArea.repaint();
			
		}
	}
	
	/**
	 * gameCanvas 객체에서 새로운 리스트를 정의해주는 method
	 * @param sign : flag를 받습니다
	 */
	private void newList(String sign) {
		gameArea.newList();
	}
	
	/**
	 * 선을 끊은 포인트에서 flag역할을하는 method
	 */
	public void sendList() {
		String point = "@";
		try {
			sendStream.write(point.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 메세지를 전송하는 method
	 * @param msg : 사용자의 메세지를 인자값으로 받습니다.
	 */
	public void sendMsg(String msg) {
		try {
			sendStream.write(msg.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 채팅과 아이디를 구분하고 메세지형태로 변환후 gui객체의 jtextarea에 저장하는 method
	 * @param msg : 전송받는 채팅을 인자값으로 받습니다
	 */
	private void castingChat(String msg) {
		String text = null;
		StringTokenizer stz = new StringTokenizer(msg,"*");
		while(stz.hasMoreTokens()) {
			text = "["+stz.nextToken()+"] ";
			text = text + stz.nextToken();
		}
		chatArea.append(text);
		chatArea.append("\n"); // 줄바꿈
	}
	
	private void gameSet(String gameFlag) {
		chatArea.append(gameFlag);
		chatArea.append("\n"); // 줄바꿈
		for(int i=0;i<5;i++) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			chatArea.append("게임종료 "+(5-i)+"초전");
			chatArea.append("\n"); // 줄바꿈
		}
		// 모든 메세지 출력된 시점.. 메세지박스GUI객체 생성
		new MsgBoxGUI();
	}
	
	public String getTurn() {
		return turn;
	}

	public void setTurn(String turn) {
		this.turn = turn;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
