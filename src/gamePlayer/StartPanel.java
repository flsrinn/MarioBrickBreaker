package gamePlayer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class StartPanel extends JPanel {
	private ImageIcon bgImg;
	private ImageIcon explosion;
	private Image exImg;

	public StartPanel(BlockGameFrame blockGameFrame, Node startPanelNode) {
		setLayout(null);
		Node bgNode = XMLReader.getNode(startPanelNode, XMLReader.E_BACKGROUND);
		Node bgImgNode = XMLReader.getNode(bgNode, XMLReader.E_BGIMG);
		bgImg = new ImageIcon(bgImgNode.getTextContent());
		Node bgMusicNode = XMLReader.getNode(bgNode, XMLReader.E_MUSIC);
		blockGameFrame.loadAudio(bgMusicNode.getTextContent());
		JLabel title = new JLabel("BLOCK BREAKER");
		title.setFont(new Font("DungGeunMo", Font.PLAIN, 50));
		title.setSize(400, 50);
		title.setLocation(230, 100);
		add(title);
		
		ImageIcon soundIcon = new ImageIcon("sound.png");
		Image soundImg = soundIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon muteIcon = new ImageIcon("mute.png");
        Image muteImg = muteIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		JButton soundButton = new JButton(new ImageIcon(soundImg));
		soundButton.setSize(50, 50);
		soundButton.setOpaque(false);
		soundButton.setBackground(new Color(0,0,0));
		soundButton.setFocusable(false);
		soundButton.setLocation(20, 10);
		soundButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(blockGameFrame.isMusicPlaying()) {
					soundButton.setIcon(new ImageIcon(muteImg));
					blockGameFrame.stopAudio();
				}
				else {
					soundButton.setIcon(new ImageIcon(soundImg));
					blockGameFrame.resumeAudio(bgMusicNode.getTextContent());
				}
			}
			
		});
		add(soundButton);
		
		ImageIcon yoshiIcon = new ImageIcon("yoshi.png");
		Image yoshiImg = yoshiIcon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
		JLabel yoshi = new JLabel(new ImageIcon(yoshiImg));
		yoshi.setSize(200, 300);
		yoshi.setLocation(10, 100);
		add(yoshi);
		
		ImageIcon marioIcon = new ImageIcon("mario.png");
		Image marioImg = marioIcon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
		JLabel mario = new JLabel(new ImageIcon(marioImg));
		mario.setSize(200, 300);
		mario.setLocation(550, 100);
		add(mario);
		
		// 게임 시작 버튼
		JButton startButton = new JButton("START");
		startButton.setFont(new Font("DungGeunMo", Font.PLAIN, 25));
		startButton.setSize(200, 40);
		startButton.setOpaque(false);
		startButton.setBackground(new Color(0, 0, 0));
		startButton.setLocation(290, 210);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				blockGameFrame.init();
				blockGameFrame.menu();
				blockGameFrame.gamePanel.requestFocus();
			}
		});
		startButton.setFocusable(false);
		add(startButton);
		
		/*JButton infoButton = new JButton("INFO");
		infoButton.setFont(new Font("DungGeunMo", Font.PLAIN, 25));
		infoButton.setSize(200, 40);
		infoButton.setOpaque(false);
		infoButton.setBackground(new Color(0, 0, 0));
		infoButton.setLocation(290, 260);
		infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameInfo();
			}
		});
		infoButton.setFocusable(false);
		add(infoButton);*/

		// 게임 종료 버튼
		JButton exitButton = new JButton("EXIT");
		exitButton.setLocation(290, 280);
		exitButton.setFont(new Font("DungGeunMo", Font.PLAIN, 25));
		exitButton.setOpaque(false);
		exitButton.setBackground(new Color(0, 0, 0));
		exitButton.setSize(200, 40);
		exitButton.setFocusable(false);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		add(exitButton);
		setVisible(true);
	}
	
	private void gameInfo() {
		// 팝업창 띄우기
		JDialog infoDialog = new JDialog();
		infoDialog.setLayout(null);
		infoDialog.getContentPane().setBackground(new Color(179, 221, 243));
		infoDialog.setTitle("게임 설명");
		infoDialog.setSize(400, 400);
		infoDialog.setVisible(true);
		
		JLabel info = new JLabel("블럭에 쓰여있는 숫자만큼 공으로 맞추는 게임입니다.");
		info.setFont(new Font("DungGeunMo", Font.PLAIN, 10));
		info.setLocation(10, 10);
		info.setSize(300, 10);
		add(info);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}