package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MsgBoxGUI extends JFrame implements ActionListener{
	
	private JButton yes = new JButton("yes");
	private JButton no = new JButton("no");
	
	MsgBoxGUI(){
		JLabel title = new JLabel("����Ͻðڽ��ϱ�?",JLabel.CENTER);
		JPanel btnPanel = new JPanel();
		btnPanel.add(yes);
		btnPanel.add(no);
		yes.addActionListener(this);
		no.addActionListener(this);
		this.add("North",title);
		this.add("Center",btnPanel);
		this.setBounds(500, 300, 200, 100);
		this.setVisible(true);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(yes)) { // �����
			
		}else if(e.getSource().equals(no)) { // ����
			System.exit(0);
		}
	}
}
