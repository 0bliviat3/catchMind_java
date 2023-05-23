package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import server.ServerSetting;

public class ClientGUI extends JFrame implements KeyListener,ActionListener,MouseMotionListener,MouseListener{
	
	private JPanel loginPanel = null;
	private JTextField nicknameF = null;
	private ClientSC csc = null;
	private JPanel gamePanel = null;
	private JButton penBtn = null;
	private JTextField answerF = null;
	private JLabel word = null;
	private GameCanvas gameArea = new GameCanvas();
	private JTextArea chatArea = new JTextArea();
	private String msg = null;
	
	ClientGUI(){
		login();
		this.setBounds(500,200,700,500);
		this.setVisible(true);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
	}
	
	private void login() {
		loginPanel = new JPanel();
		JLabel nickname = new JLabel("닉네임 입력",JLabel.CENTER);
		nicknameF = new JTextField(20);
		loginPanel.add(nickname);
		loginPanel.add(nicknameF);
		this.add("Center",loginPanel);
		nicknameF.addKeyListener(this);
	}
	private void game() { // 센터에 캔버스
		JPanel title = new JPanel();
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1,2));
		gamePanel = new JPanel();
		gamePanel.setLayout(new BorderLayout());
		penBtn = new JButton("pen");
		answerF = new JTextField(); // 채팅겸 정답을 받는 필드
		word = new JLabel("",JLabel.CENTER);
		
//		chatList = new ArrayList();
		chatArea.setEditable(false);
		JScrollPane chatScroll = new JScrollPane(chatArea);
//		chatScroll.setSize(100, HEIGHT);
		centerPanel.add(gameArea);
		centerPanel.add(chatScroll);
		title.add(word);
		title.add(penBtn);
		gamePanel.add("North",title);
		gamePanel.add("Center",centerPanel);
		gamePanel.add("South",answerF);
		answerF.addActionListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		String[][] wordarr = {{"과일","동물","전자기기","패스트푸드","과자"},
				{"파인애플","고릴라","식기세척기","햄버거","새우깡"}};
		int sec = 0;
		if(nicknameF.getText() != null) {
			if(e.getKeyCode() == e.VK_ENTER) {
				String nickname = nicknameF.getText();
				csc = new ClientSC(nickname,gameArea,chatArea,this); // 입장과 동시에 역할부여
				//main Thread가 더 빨라서 정보를 받지 못함..
				//main Thread sleep
				System.out.println("참가인원 대기중...");
				
				while(true) { // 역할을 부여받는 순간 break
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					sec++;
					System.out.println(sec+"초경과");
//					msg = csc.getTurn();
					if(msg != null) {
						break;
					}
				}
				game();
				for(int i=0;i<5;i++) {
					if(wordarr[0][i].equals(msg)) { // 맞추는 사람
						System.out.println("맞추는 사람");
						word.setText(msg);
					}else if(wordarr[1][i].equals(msg)){ // 그리는 사람 만 펜 버튼 클릭가능 + 제시어 받음
						System.out.println("그리는 사람");
						word.setText(msg);
						penBtn.addActionListener(this);
					}
				}
				csc.setTurn(null);
				this.remove(loginPanel);
				this.add("Center",gamePanel);
				this.setVisible(true);
			}
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(penBtn)) { // canvas 그리기 가능.. 소켓으로부터 입력주기 가능
			gameArea.addMouseMotionListener(this);
			gameArea.addMouseListener(this);
		}else if(e.getSource().equals(answerF)) { // 입력후 엔터치면 채팅 받기
			csc.sendMsg("**"+answerF.getText()); // 채팅 전송 채팅임을 알수있는 특문 추가해서 전송
			answerF.setText("");
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) { // 드래그할때 화면에 그리면서 동시에 좌표를 소켓으로 넘겨준다.
		// TODO Auto-generated method stub
		Point p = new Point(e.getX(),e.getY());
		System.out.println(p.getX()+"/"+p.getY());
		csc.sending(p);
		gameArea.setList(p);
		//repaint
		gameArea.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) { // 마우스 땟을때 리스트 초기화
		gameArea.newList();
		csc.sendList();
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
