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
		JLabel nickname = new JLabel("�г��� �Է�",JLabel.CENTER);
		nicknameF = new JTextField(20);
		loginPanel.add(nickname);
		loginPanel.add(nicknameF);
		this.add("Center",loginPanel);
		nicknameF.addKeyListener(this);
	}
	private void game() { // ���Ϳ� ĵ����
		JPanel title = new JPanel();
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1,2));
		gamePanel = new JPanel();
		gamePanel.setLayout(new BorderLayout());
		penBtn = new JButton("pen");
		answerF = new JTextField(); // ä�ð� ������ �޴� �ʵ�
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
		String[][] wordarr = {{"����","����","���ڱ��","�н�ƮǪ��","����"},
				{"���ξ���","����","�ı⼼ô��","�ܹ���","�����"}};
		int sec = 0;
		if(nicknameF.getText() != null) {
			if(e.getKeyCode() == e.VK_ENTER) {
				String nickname = nicknameF.getText();
				csc = new ClientSC(nickname,gameArea,chatArea,this); // ����� ���ÿ� ���Һο�
				//main Thread�� �� ���� ������ ���� ����..
				//main Thread sleep
				System.out.println("�����ο� �����...");
				
				while(true) { // ������ �ο��޴� ���� break
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					sec++;
					System.out.println(sec+"�ʰ��");
//					msg = csc.getTurn();
					if(msg != null) {
						break;
					}
				}
				game();
				for(int i=0;i<5;i++) {
					if(wordarr[0][i].equals(msg)) { // ���ߴ� ���
						System.out.println("���ߴ� ���");
						word.setText(msg);
					}else if(wordarr[1][i].equals(msg)){ // �׸��� ��� �� �� ��ư Ŭ������ + ���þ� ����
						System.out.println("�׸��� ���");
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
		if(e.getSource().equals(penBtn)) { // canvas �׸��� ����.. �������κ��� �Է��ֱ� ����
			gameArea.addMouseMotionListener(this);
			gameArea.addMouseListener(this);
		}else if(e.getSource().equals(answerF)) { // �Է��� ����ġ�� ä�� �ޱ�
			csc.sendMsg("**"+answerF.getText()); // ä�� ���� ä������ �˼��ִ� Ư�� �߰��ؼ� ����
			answerF.setText("");
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) { // �巡���Ҷ� ȭ�鿡 �׸��鼭 ���ÿ� ��ǥ�� �������� �Ѱ��ش�.
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
	public void mouseReleased(MouseEvent e) { // ���콺 ������ ����Ʈ �ʱ�ȭ
		gameArea.newList();
		csc.sendList();
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
