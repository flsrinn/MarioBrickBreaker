package gamePlayer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Node;

public class GameOverPanel extends JPanel {
	private ImageIcon bgImg;
	private BlockGameFrame blockGameFrame;
	
	public GameOverPanel(BlockGameFrame blockGameFrame, Node gameOverPanelNode, int score) {
		setLayout(null);
		this.blockGameFrame = blockGameFrame;
		Node bgNode = XMLReader.getNode(gameOverPanelNode, XMLReader.E_BACKGROUND);
		Node bgimgNode = XMLReader.getNode(bgNode, XMLReader.E_BGIMG);
		bgImg = new ImageIcon(bgimgNode.getTextContent());
		
		Node bgMusicNode = XMLReader.getNode(bgNode, XMLReader.E_MUSIC);
		if(blockGameFrame.isMusicPlaying()) {
			blockGameFrame.stopAudio();
			blockGameFrame.loadAudio(bgMusicNode.getTextContent());
		}
		
		JLabel ending = new JLabel("Game Over!");
		ending.setFont(new Font("DungGeunMo", Font.PLAIN, 60));
		ending.setSize(400, 60);
		ending.setLocation(240, 100);
		add(ending);
		
		JLabel gameOverScore = new JLabel("SCORE " + Integer.toString(score));
		gameOverScore.setFont(new Font("DungGeunMo", Font.PLAIN, 40));
		gameOverScore.setSize(300, 40);
		gameOverScore.setLocation(290, 200);
		add(gameOverScore);
		
		// 게임 재시작 버튼
		JButton restartButton = new JButton("RESTART");
		restartButton.setLocation(290, 320);
		restartButton.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		restartButton.setOpaque(false);
		restartButton.setBackground(new Color(0, 0, 0));
		restartButton.setSize(200, 40);
		restartButton.setFocusable(false);
		restartButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	blockGameFrame.getContentPane().removeAll();
		    	blockGameFrame.toStartPanel();
		    }
		});
		add(restartButton);
		
		// 게임 종료 버튼
		JButton exitButton = new JButton("EXIT");
		exitButton.setLocation(290, 380);
		exitButton.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
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
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}