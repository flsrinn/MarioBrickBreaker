package gamePlayer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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

public class GameClearPanel extends JPanel {
	private ImageIcon bgImg;
	private BlockGameFrame blockGameFrame;
	
	public GameClearPanel(BlockGameFrame blockGameFrame, Node gameClearPanelNode, ItemPanel itemPanel, int score) {
		setLayout(null);
		this.blockGameFrame = blockGameFrame;
		Node bgNode = XMLReader.getNode(gameClearPanelNode, XMLReader.E_BACKGROUND);
		Node bgimgNode = XMLReader.getNode(bgNode, XMLReader.E_BGIMG);
		bgImg = new ImageIcon(bgimgNode.getTextContent());
		Node bgMusicNode = XMLReader.getNode(bgNode, XMLReader.E_MUSIC);
		if(blockGameFrame.isMusicPlaying()) {
			blockGameFrame.stopAudio();
			blockGameFrame.loadAudio(bgMusicNode.getTextContent());
		}
		JLabel ending = new JLabel("Game CLEAR!");
		ending.setFont(new Font("DungGeunMo", Font.PLAIN, 60));
		ending.setSize(400, 60);
		ending.setLocation(230, 100);
		add(ending);
		
		Image ghostImg = new ImageIcon("item/ghost.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		JLabel ghostLabel = new JLabel(new ImageIcon(ghostImg));
		ghostLabel.setLocation(170, 180);
		ghostLabel.setSize(60, 60);
		add(ghostLabel);
		JLabel ghosts = new JLabel("x " + Integer.toString(itemPanel.getGhost()));
		ghosts.setSize(40, 20);
		ghosts.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		ghosts.setLocation(250, 200);
		add(ghosts);
		
		Image flowerImg = new ImageIcon("item/flower.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		JLabel flowerLabel = new JLabel(new ImageIcon(flowerImg));
		flowerLabel.setLocation(340, 180);
		flowerLabel.setSize(60, 60);
		add(flowerLabel);
		JLabel flowers = new JLabel("x " + Integer.toString(itemPanel.getFlower()));
		flowers.setSize(40, 20);
		flowers.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		flowers.setLocation(420, 200);
		add(flowers);
		
		Image starImg = new ImageIcon("item/star.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		JLabel starLabel = new JLabel(new ImageIcon(starImg));
		starLabel.setLocation(500, 180);
		starLabel.setSize(60, 60);
		add(starLabel);
		JLabel stars = new JLabel("x " + Integer.toString(itemPanel.getStar()));
		stars.setSize(40, 20);
		stars.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		stars.setLocation(580, 200);
		add(stars);
		
		score -= itemPanel.getGhost() * 50;
		score += itemPanel.getFlower() * 30;
		score += itemPanel.getStar() * 100;
		
		JLabel gameClearScore = new JLabel("SCORE " + Integer.toString(score));
		gameClearScore.setFont(new Font("DungGeunMo", Font.PLAIN, 40));
		gameClearScore.setSize(300, 40);
		gameClearScore.setLocation(290, 250);
		add(gameClearScore);
		
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