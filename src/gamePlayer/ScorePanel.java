package gamePlayer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class ScorePanel extends JPanel {
	private BlockGameFrame blockGameFrame;
	private int score = 0;
	private int balance = 0;
	private int bullet = 0;
	private Block life[];
	private ImageIcon lifeIcon;
	private int maxLife;
	private Vector<Block> lifeList = new Vector<Block>();
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	private JLabel moneyLabel = new JLabel(Integer.toString(balance));
	private JLabel bulletLabel = new JLabel("x " + Integer.toString(bullet));
	private Node musicNode;
	
	public ScorePanel(Node scorePanelNode, BlockGameFrame blockGameFrame) {
		setLayout(null);
		this.blockGameFrame = blockGameFrame;
		setBackground(new Color(115, 202, 229));
		JLabel score = new JLabel("SCORE : ");
		score.setLocation(40, 10);
		score.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		score.setSize(100, 20);
		score.setForeground(Color.white);
		add(score);
		scoreLabel.setLocation(125, 10);
		scoreLabel.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		scoreLabel.setSize(50, 20);
		scoreLabel.setForeground(Color.white);
		add(scoreLabel);
		
		Node lifeNode = XMLReader.getNode(scorePanelNode, XMLReader.E_LIFE);
		Node heartNode = XMLReader.getNode(lifeNode, XMLReader.E_HEART);
		Node objNode = XMLReader.getNode(heartNode, XMLReader.E_OBJ);
		maxLife = Integer.parseInt(XMLReader.getAttr(objNode, "life"));
		lifeIcon = new ImageIcon(XMLReader.getAttr(objNode, "img"));
		life = new Block[maxLife];
		int x = 0;
		switch(maxLife) {
		case 1:
			x = 90;
			break;
		case 2:
			x = 70;
			break;
		case 3:
			x = 60;
			break;
		case 4:
			x = 40;
			break;
		case 5:
			x = 30;
			break;
		}
		for(int i=0; i<maxLife; i++) {
			life[i] = new Block(x + (30 * i), 50, 30, 30, lifeIcon);
			add(life[i]);
			lifeList.add(life[i]);
		}
		musicNode = XMLReader.getNode(lifeNode, XMLReader.E_MUSIC);
		
		JLabel money = new JLabel("MONEY : ");
		money.setLocation(40, 90);
		money.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		money.setSize(100, 20);
		money.setForeground(Color.white);
		add(money);
		
		moneyLabel.setLocation(125, 90);
		moneyLabel.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		moneyLabel.setSize(70, 20);
		moneyLabel.setForeground(Color.white);
		add(moneyLabel);
		
		Image bulletImage = new ImageIcon("item/bulletIcon.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		JButton bulletButton = new JButton(new ImageIcon(bulletImage));
		bulletButton.setSize(40, 40);
		bulletButton.setLocation(40, 125);
		bulletButton.setOpaque(false);
		bulletButton.setBackground(new Color(0,0,0));
		bulletButton.setFocusable(false);
		bulletButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(balance >= 1000) {
					balance -= 1000;
					bullet++;
					moneyLabel.setText(Integer.toString(balance));
					bulletLabel.setText("x " + Integer.toString(bullet));
				}
			}
		});
		add(bulletButton);
		
		bulletLabel.setLocation(47, 165);
		bulletLabel.setFont(new Font("DungGeunMo", Font.PLAIN, 15));
		bulletLabel.setSize(40, 15);
		bulletLabel.setForeground(Color.white);
		add(bulletLabel);
		
		JLabel bulletPrice = new JLabel("1000원");
		bulletPrice.setLocation(100, 135);
		bulletPrice.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		bulletPrice.setSize(100, 20);
		bulletPrice.setForeground(Color.white);
		add(bulletPrice);
		
		Image heartImage = new ImageIcon("item/heart.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		JButton heartButton = new JButton(new ImageIcon(heartImage));
		heartButton.setSize(40, 40);
		heartButton.setLocation(40, 180);
		heartButton.setOpaque(false);
		heartButton.setBackground(new Color(0,0,0));
		heartButton.setFocusable(false);
		heartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(balance >= 2000 && lifeList.size() != maxLife) {
					balance -= 2000;
					moneyLabel.setText(Integer.toString(balance));
					
					Block newLife = new Block(lifeList.get(0).getX(), lifeList.get(0).getY(), lifeList.get(0).getWidth(), lifeList.get(0).getHeight(), lifeIcon);
		            lifeList.add(newLife);
		            Block lifeNode = lifeList.get(lifeList.size()-1);
		            
		            lifeNode.setVisible(true);
		            
		            lifeNode.setLocation((lifeList.get(0).getX()) + ((lifeList.size() - 1) * 30), lifeList.get(0).getY());
		            
		            add(lifeNode);
		            
		            revalidate();
		            repaint();
				}
			}
		});
		add(heartButton);
		
		JLabel heartPrice = new JLabel("2000원");
		heartPrice.setLocation(100, 190);
		heartPrice.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		heartPrice.setSize(100, 20);
		heartPrice.setForeground(Color.white);
		add(heartPrice);
	}

	public int getBullet() {
		return bullet;
	}
	
	public int getScore() {
		return score;
	}
	
	public void increaseScore(int score) {
		this.score += score;
		scoreLabel.setText(Integer.toString(this.score));
	}
	
	public void increaseMoney() {
		balance += 100;
		moneyLabel.setText(Integer.toString(balance));
	}
	
	public void decrease() {
		loadAudio(musicNode.getTextContent());
		Block lifeNode = lifeList.get(lifeList.size()-1);
		lifeNode.setVisible(false);
		lifeList.remove(lifeList.size()-1);
		
		if (lifeList.size() == 0) {
			JOptionPane.showConfirmDialog(null, "게임 오버", "게임 오버", JOptionPane.DEFAULT_OPTION);
			blockGameFrame.gameOver(score);
		}
		revalidate();
		repaint();
	}
	
	public void decreaseBullet() {
		bullet--;
		bulletLabel.setText("x " + Integer.toString(bullet));
		bulletLabel.repaint();
	}
	
	public void loadAudio(String fileName) {
		if(!blockGameFrame.isMusicPlaying()) {
			return;
		}
		try {
		    Clip clip = AudioSystem.getClip();
		    File audioFile = new File(fileName);
		    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		    clip.open(audioStream);
		    clip.start();
		} catch (LineUnavailableException e) {
		    e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
}