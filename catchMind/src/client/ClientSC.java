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
	 * ��ǥ������ method
	 * @param p : ���ڰ����� Point��ü�� �޽��ϴ�.
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
						}else if(turn.contains("**")) { // ä���϶�
							System.out.println(turn);
							castingChat(turn);
						}else if(turn.contains("$")) { // gameset
							gameSet(turn);
						}else { // ���Һο�
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
	 * Point�� ����ȯ�� gameCanvas ��ü�� �Ѱ��ִ� method
	 * @param point : x��ǥ!y��ǥ�� �������� ���� ���ڿ��� ���ڰ����� �޽��ϴ�.
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
	 * gameCanvas ��ü���� ���ο� ����Ʈ�� �������ִ� method
	 * @param sign : flag�� �޽��ϴ�
	 */
	private void newList(String sign) {
		gameArea.newList();
	}
	
	/**
	 * ���� ���� ����Ʈ���� flag�������ϴ� method
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
	 * �޼����� �����ϴ� method
	 * @param msg : ������� �޼����� ���ڰ����� �޽��ϴ�.
	 */
	public void sendMsg(String msg) {
		try {
			sendStream.write(msg.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ä�ð� ���̵� �����ϰ� �޼������·� ��ȯ�� gui��ü�� jtextarea�� �����ϴ� method
	 * @param msg : ���۹޴� ä���� ���ڰ����� �޽��ϴ�
	 */
	private void castingChat(String msg) {
		String text = null;
		StringTokenizer stz = new StringTokenizer(msg,"*");
		while(stz.hasMoreTokens()) {
			text = "["+stz.nextToken()+"] ";
			text = text + stz.nextToken();
		}
		chatArea.append(text);
		chatArea.append("\n"); // �ٹٲ�
	}
	
	private void gameSet(String gameFlag) {
		chatArea.append(gameFlag);
		chatArea.append("\n"); // �ٹٲ�
		for(int i=0;i<5;i++) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			chatArea.append("�������� "+(5-i)+"����");
			chatArea.append("\n"); // �ٹٲ�
		}
		// ��� �޼��� ��µ� ����.. �޼����ڽ�GUI��ü ����
		new MsgBoxGUI();
	}
	
	public String getTurn() {
		return turn;
	}

	public void setTurn(String turn) {
		this.turn = turn;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
