package gamePlayer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class BlockGameFrame extends JFrame {
	private String filePath;
	private boolean isPlaying = false;
	private JSplitPane hPane = new JSplitPane();
	private JSplitPane vPane = new JSplitPane();
	public GamePanel gamePanel;
	private ScorePanel scorePanel;
	private StartPanel startPanel;
	private ItemPanel itemPanel;
	private String width;
	private String height;
	private int count = 0;
	private XMLReader xml;
	private Clip clip;
	private boolean isMusicPlaying=true;
	
	BlockGameFrame() {
		filePath = "map/block4.xml";
		xml = new XMLReader(filePath);
		Node blockGameNode = xml.getBlockGameElement();
		setTitle("블록 깨기 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Node sizeNode = XMLReader.getNode(blockGameNode, XMLReader.E_SCREENSIZE);
		width = XMLReader.getAttr(sizeNode, "w");
		height = XMLReader.getAttr(sizeNode, "h");
		setSize(Integer.parseInt(width), Integer.parseInt(height));
		startPanel = new StartPanel(this, xml.getStartPanelElement());
		add(startPanel);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	public void init() {
		xml = new XMLReader(filePath);
		remove(startPanel);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		scorePanel = new ScorePanel(xml.getScorePanelElement(), this);
		
		itemPanel = new ItemPanel(xml.getItemPanelElement());
		gamePanel = new GamePanel(this, xml.getGamePanelElement(), scorePanel, itemPanel);
		hPane.setDividerLocation(580);
		hPane.setLeftComponent(gamePanel);
		hPane.setRightComponent(vPane);
		hPane.setDividerSize(0);
		add(hPane);
		
		vPane.setDividerLocation(230);
		vPane.setTopComponent(scorePanel);
		vPane.setBottomComponent(itemPanel);
		vPane.setDividerSize(0);
	}
	
	public void loadAudio(String fileName) {
		try {
			isMusicPlaying=true;
			if ((clip != null && clip.isRunning()) || !isMusicPlaying) {
	            return;
	        }
		    clip = AudioSystem.getClip();
		    File audioFile = new File(fileName);
		    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		    clip.open(audioStream);
		    if(fileName.equals("audio/themesong.wav")) {
		    	clip.loop(Clip.LOOP_CONTINUOUSLY);
		    }
		    else {
		    	clip.start();
		    }
		} catch (LineUnavailableException e) {
		    e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
	
	public void stopAudio() {
		if (clip != null && clip.isRunning()) {
			clip.stop();
        }
		isMusicPlaying = false;
	}
	
	public void resumeAudio(String fileName) {
		if(fileName.equals("audio/themesong.wav")) {
	    	clip.loop(Clip.LOOP_CONTINUOUSLY);
	    }
	    else {
	    	clip.start();
	    }
		isMusicPlaying = true;
	}
	
	public boolean isMusicPlaying() {
		return isMusicPlaying;
	}

	public void menu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(115, 202, 229));
		JMenu fileMenu = new JMenu("File");
		fileMenu.setForeground(Color.white);
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isPlaying) {
					JOptionPane.showMessageDialog(null, "플레이 중에는 변경할 수 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				}
		        JFileChooser fileChooser = new JFileChooser("./map");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
				fileChooser.setFileFilter(filter);
				int ret = fileChooser.showOpenDialog(null);
				if (ret != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
					return;
				}
				filePath = fileChooser.getSelectedFile().getPath();
				init();
			}
		});
		fileMenu.add(openItem);
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setForeground(Color.white);
		JMenuItem playItem = new JMenuItem("Play");
		playItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isPlaying = true;
			}
		});
		JMenuItem settingItem = new JMenuItem("Setting");
		settingItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isPlaying) {
					gamePanel.settingDialog();
				}
				else {
					JOptionPane.showMessageDialog(null, "게임 플레이 중에는 변경할 수 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		JMenuItem pauseItem = new JMenuItem("Pause");
		pauseItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!gamePanel.isBallMoving() && !gamePanel.isBulletMoving()) {
					JOptionPane.showMessageDialog(null, "공이나 총알이 움직일 때만 멈출 수 있습니다.", "경고", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(count % 2 == 0) {
					if(gamePanel.isBallMoving()) {
						for(int i=0; i<gamePanel.ballThread.length; i++) {
							gamePanel.ballThread[i].stopBall();
						}
					}
					if(gamePanel.isBulletMoving()) {
						gamePanel.bulletThread.stopBullet();
					}
				}
				else {
					if(gamePanel.isBallMoving()) {
						for(int i=0; i<gamePanel.ballThread.length; i++) {
							gamePanel.ballThread[i].resumeBall();
						}
					}
					if(gamePanel.isBulletMoving()) {
						gamePanel.bulletThread.resumeBullet();
					}
				}
				count++;
			}
		});
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		gameMenu.add(playItem);
		gameMenu.add(settingItem);
		gameMenu.add(pauseItem);
		gameMenu.addSeparator(); 
		gameMenu.add(exitItem);
		
		JMenu musicMenu = new JMenu("Music");
		musicMenu.setForeground(Color.white);
		JMenuItem musicItem = new JMenuItem("On");
		musicItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resumeAudio("audio/themesong.wav");
			}
		});
		JMenuItem muteItem = new JMenuItem("Mute");
		muteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopAudio();
			}
		});
		musicMenu.add(musicItem);
		musicMenu.add(muteItem);
		menuBar.add(fileMenu);
		menuBar.add(gameMenu);
		menuBar.add(musicMenu);
		setJMenuBar(menuBar);
	}

	public boolean checkPlaying() {
		if (isPlaying)
			return true;
		else
			return false;
	}
	
	public int getWidth() { return Integer.parseInt(width); }

	public int getHeight() { return Integer.parseInt(height); }

	 // StartPanel을 보이게 하는 메소드
	public void toStartPanel() {
		startPanel = new StartPanel(this, xml.getStartPanelElement());
		getContentPane().add(startPanel);
		revalidate();
		repaint();
    }
	
	public void gameOver(int score) {
		remove(hPane);
    	remove(vPane);
    	setJMenuBar(null);
    	GameOverPanel gameOverPanel = new GameOverPanel(this, xml.getGameOverPanelElement(), score);
    	add(gameOverPanel);
    	revalidate();
		repaint();
	}
	
	public void gameClear(int score) {
		remove(hPane);
    	remove(vPane);
    	setJMenuBar(null);
    	GameClearPanel gameClearPanel = new GameClearPanel(this, xml.getGameClearPanelElement(), itemPanel, score);
    	add(gameClearPanel);
    	revalidate();
		repaint();
	}
}







