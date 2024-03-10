package gamePlayer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class GamePanel extends JPanel {
	private Node gamePanelNode;
	private BlockGameFrame blockGameFrame;
	private ImageIcon bgImg;
	private ScorePanel scorePanel;
	private ItemPanel itemPanel;
	private Vector<Block> blockList = new Vector<Block>();
	private JLabel ball[];
	private Block paddle;
	private Block shooter;
	private Block bullet;
	public BallThread ballThread[];
	public BulletThread bulletThread;
	private int ballSpeed, balls, ballCount = 0, tries = 0, ballSize = 0;
	private boolean isBallMoving = false;
	private boolean isBulletMoving = false;
	private int level = 0;
	private Image flowerImg = new ImageIcon("item/flower.png").getImage().getScaledInstance(60, 30, Image.SCALE_SMOOTH);
	private Image ghostImg = new ImageIcon("item/ghost.png").getImage().getScaledInstance(60, 30, Image.SCALE_SMOOTH);
	private Image starImg = new ImageIcon("item/star.png").getImage().getScaledInstance(60, 30, Image.SCALE_SMOOTH);
	private JLabel item;
	private int count = 0;
	private int unBlock = 0;
	private Node bgMusicNode;
	private Clip clip;

	public GamePanel(BlockGameFrame blockGameFrame, Node gamePanelNode, ScorePanel scorePanel, ItemPanel itemPanel) {
		setLayout(null);
		this.blockGameFrame = blockGameFrame;
		this.gamePanelNode = gamePanelNode;
		this.scorePanel = scorePanel;
		this.itemPanel = itemPanel;
		Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BACKGROUND);
		Node bgimgNode = XMLReader.getNode(bgNode, XMLReader.E_BGIMG);
		bgImg = new ImageIcon(bgimgNode.getTextContent());
		
		bgMusicNode = XMLReader.getNode(bgNode, XMLReader.E_MUSIC);
		if(blockGameFrame.isMusicPlaying()) {
			blockGameFrame.stopAudio();
			blockGameFrame.loadAudio(bgMusicNode.getTextContent());
		}
		// read <Fish><Obj>s from the XML parse tree, make Food objects, and add them to
		// the FishBowl panel.
		Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
		NodeList blockNodeList = blockNode.getChildNodes();
		for (int i = 0; i < blockNodeList.getLength(); i++) {
			Node node = blockNodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if (node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				String type = XMLReader.getAttr(node, "type");
				String imgSrc = XMLReader.getAttr(node, "img");
				ImageIcon icon = new ImageIcon(imgSrc);
				Block block = new Block(x, y, w, h, icon);
				if (type.equals("6")) {
					addUnBlock(blockList.size() - 1);
				}
				block.setName(type);
				block.setNum();
				add(block);
				blockList.add(block);
			}
		}

		Node paddleNode = XMLReader.getNode(gamePanelNode, XMLReader.E_PADDLE);
		NodeList paddleNodeList = paddleNode.getChildNodes();
		paddleNode = paddleNodeList.item(1);
		if (paddleNode.getNodeName().equals(XMLReader.E_OBJ)) {
			int x = Integer.parseInt(XMLReader.getAttr(paddleNode, "x"));
			int y = Integer.parseInt(XMLReader.getAttr(paddleNode, "y"));
			int w = Integer.parseInt(XMLReader.getAttr(paddleNode, "w"));
			int h = Integer.parseInt(XMLReader.getAttr(paddleNode, "h"));

			ImageIcon icon = new ImageIcon(XMLReader.getAttr(paddleNode, "img"));
			paddle = new Block(x, y, w, h, icon);
			add(paddle);
		}

		Node shooterNode = XMLReader.getNode(gamePanelNode, XMLReader.E_SHOOTER);
		NodeList shooterNodeList = shooterNode.getChildNodes();
		shooterNode = shooterNodeList.item(1);
		if (shooterNode.getNodeName().equals(XMLReader.E_OBJ)) {
			int x = Integer.parseInt(XMLReader.getAttr(shooterNode, "x"));
			int y = Integer.parseInt(XMLReader.getAttr(shooterNode, "y"));
			int w = Integer.parseInt(XMLReader.getAttr(shooterNode, "w"));
			int h = Integer.parseInt(XMLReader.getAttr(shooterNode, "h"));

			ImageIcon icon = new ImageIcon(XMLReader.getAttr(shooterNode, "img"));
			shooter = new Block(x, y, w, h, icon);
			add(shooter);
		}

		Node bulletNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BULLET);
		NodeList bulletNodeList = bulletNode.getChildNodes();
		bulletNode = bulletNodeList.item(1);
		if (bulletNode.getNodeName().equals(XMLReader.E_OBJ)) {
			int w = Integer.parseInt(XMLReader.getAttr(bulletNode, "w"));
			int h = Integer.parseInt(XMLReader.getAttr(bulletNode, "h"));

			ImageIcon icon = new ImageIcon(XMLReader.getAttr(bulletNode, "img"));
			bullet = new Block(shooter.getX() - 2, shooter.getY() + 40, w, h, icon);
			bullet.setVisible(false);
			add(bullet);
		}
		initGame();
		setFocusable(true);
		requestFocus();
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if (blockGameFrame.checkPlaying()) {
					switch (code) {
					case KeyEvent.VK_A:
						if (!isBallMoving) {
							ballThread[ballCount] = new BallThread(ball[ballCount], ballSize, "LEFTUP", ballSpeed);
							ballThread[ballCount].start();
							ballCount++;
							if (ballCount == balls) {
								isBallMoving = true;
							}
						} else {
							shooter.setLocation(shooter.getX() - 10, shooter.getY());
							if (!isBulletMoving()) {
								bullet.setLocation(bullet.getX() - 10, bullet.getY());
							}
						}
						break;
					case KeyEvent.VK_D:
						if (!isBallMoving) {
							ballThread[ballCount] = new BallThread(ball[ballCount], ballSize, "RIGHTUP", ballSpeed);
							ballThread[ballCount].start();
							ballCount++;
							if (ballCount == balls) {
								isBallMoving = true;
							}
						} else {
							shooter.setLocation(shooter.getX() + 10, shooter.getY());
							if (!isBulletMoving()) {
								bullet.setLocation(bullet.getX() + 10, bullet.getY());
							}
						}
						break;
					case KeyEvent.VK_SPACE:
						if (scorePanel.getBullet() > 0 && !isBulletMoving && isBallMoving) {
							isBulletMoving = true;
							bullet.setVisible(true);
							scorePanel.decreaseBullet();
							bulletThread = new BulletThread(bullet.getY());
							bulletThread.start();
						}
						break;
					}
				}
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (blockGameFrame.checkPlaying() && isBallMoving) {
					int x = e.getX();
					if (x == 0 || x + paddle.getWidth() - 5 >= getWidth() + getX() - 1)
						return;
					paddle.setLocation(x, paddle.getY());
				}
			}
		});
	}

	public void initGame() {
		if (ball != null) {
			for (int i = 0; i < ball.length; i++) {
				remove(ball[i]);
			}
		}
		tries = 0;
		ballCount = 0;
		isBallMoving = false;
		Node ballNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BALL);
		NodeList ballNodeList = ballNode.getChildNodes();
		ballNode = ballNodeList.item(1);
		ball = new Block[ballNodeList.getLength()];
		if (ballNode.getNodeName().equals(XMLReader.E_OBJ)) {
			ballSpeed = Integer.parseInt(XMLReader.getAttr(ballNode, "speed"));
			balls = Integer.parseInt(XMLReader.getAttr(ballNode, "balls"));
			String size = XMLReader.getAttr(ballNode, "size");
			int y = 0, w = 0, h = 0;
			switch (size) {
			case "1":
				ballSize = 7;
				w = 20;
				h = 20;
				y = paddle.getY() - (h / 2) + ballSize;
				break;
			case "2":
				ballSize = 3;
				w = 30;
				h = 30;
				y = paddle.getY() - (h / 2) + ballSize;
				break;
			case "3":
				ballSize = 1;
				w = 40;
				h = 40;
				y = paddle.getY() - (h / 2) + ballSize;
				break;
			}
			switch (ballSpeed) {
			case 1:
				ballSpeed = 7;
				break;
			case 2:
				ballSpeed = 6;
				break;
			case 3:
				ballSpeed = 5;
				break;
			case 4:
				ballSpeed = 4;
				break;
			}
			ball = new Block[balls];
			ImageIcon icon = new ImageIcon(XMLReader.getAttr(ballNode, "img"));
			for (int i = 0; i < balls; i++) {
				ball[i] = new Block(paddle.getX() + paddle.getWidth() / 2 - 15, y, w, h, icon);
				add(ball[i]);
			}
		}
		ballThread = new BallThread[balls];
	}

	public void loadAudio(String fileName) {
		if (!blockGameFrame.isMusicPlaying()) {
			return;
		}
		try {
			if (clip != null && clip.isRunning()) {
				return;
			}
			clip = AudioSystem.getClip();
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

	public void paintComponent(Graphics g) {
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public void settingDialog() {
		// 팝업창 띄우기
		JDialog settingsDialog = new JDialog();
		settingsDialog.setLayout(null);
		settingsDialog.setTitle("Settings");
		settingsDialog.setSize(400, 300);
		settingsDialog.setModal(true);

		// 난이도 설정을 위한 콤보박스
		JLabel levelLabel = new JLabel("난이도");
		levelLabel.setSize(50, 20);
		levelLabel.setLocation(120, 15);
		add(levelLabel);
		settingsDialog.add(levelLabel);
		String[] difficultyLevels = { "Easy", "Medium", "Hard" };
		JComboBox<String> difficultyComboBox = new JComboBox<String>(difficultyLevels);
		settingsDialog.add(difficultyComboBox);
		difficultyComboBox.setSize(100, 30);
		difficultyComboBox.setLocation(170, 10);
		difficultyComboBox.setSelectedIndex(level); // 기본 선택값 설정
		JLabel levelInfo;
		if (difficultyComboBox.getSelectedIndex() == 0) {
			levelInfo = new JLabel("1~7 사이의 숫자가 나옵니다.");
		}

		else if (difficultyComboBox.getSelectedIndex() == 1) {
			levelInfo = new JLabel("3~10 사이의 숫자가 나옵니다.");
		}

		else {
			levelInfo = new JLabel("5~15 사이의 숫자가 나옵니다.");
		}
		difficultyComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (difficultyComboBox.getSelectedIndex() == 0) {
					levelInfo.setText("1~7 사이의 숫자가 나옵니다.");
				}

				else if (difficultyComboBox.getSelectedIndex() == 1) {
					levelInfo.setText("3~10 사이의 숫자가 나옵니다.");
				}

				else {
					levelInfo.setText("5~15 사이의 숫자가 나옵니다.");
				}
			}
		});

		levelInfo.setSize(200, 40);
		levelInfo.setLocation(120, 40);

		JButton applyButton = new JButton("Apply");
		applyButton.setLocation(155, 200);
		applyButton.setSize(80, 20);
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsDialog.setVisible(false);
				for (int i = 0; i < blockList.size(); i++) {
					level = difficultyComboBox.getSelectedIndex();
					blockList.get(i).changeLevel(level);
					blockList.get(i).setNum();
				}
			}
		});
		settingsDialog.add(applyButton);
		settingsDialog.add(levelInfo);
		settingsDialog.setVisible(true);
	}

	public boolean isBallMoving() {
		return isBallMoving;
	}

	public boolean isBulletMoving() {
		return isBulletMoving;
	}

	private void addUnBlock(int num) {
		unBlock++;
	}

	private int getUnBlock() {
		return unBlock;
	}

	private void showItem(int x, int y) {
		int r = (int) (Math.random() * 100);
		if (r < 60) {
			item = new JLabel(new ImageIcon(flowerImg));
			itemPanel.addFlower();
		} else if (r < 90) {
			item = new JLabel(new ImageIcon(ghostImg));
			itemPanel.addGhost();
		} else {
			item = new JLabel(new ImageIcon(starImg));
			itemPanel.addStar();
		}
		item.setSize(60, 30);
		item.setLocation(x, y);
		add(item);

		IconThread iconThread = new IconThread(item);
		iconThread.start();
	}

	class BallThread extends Thread {
		private String direction;
		private boolean stopFlag;
		private int ballSpeed;
		private JLabel ball;
		private int size;

		public BallThread(JLabel ball, int size, String direction, int ballSpeed) {
			this.ball = ball;
			this.size = size;
			this.direction = direction;
			this.ballSpeed = ballSpeed;
		}

		public void stopBall() {
			stopFlag = true;
		}

		synchronized public void resumeBall() {
			stopFlag = false;
			notify();
		}

		synchronized private void checkWait() {
			if (stopFlag) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}

		@Override
		public void run() {
			while (true) {
				checkWait();
				if (blockList.size() - getUnBlock() == count) {
					stopBall();
					blockGameFrame.gameClear(blockList.size() * 10);
				}
				if (!stopFlag) {
					if (ball.getY() == blockGameFrame.getHeight()-10) {
						ball.setVisible(false);
						tries++;
						if (tries < balls || !isBallMoving) {
							return;
						} else {
							scorePanel.decrease();
							initGame();
						}
						revalidate();
						repaint();
						break;
					}

					// 패들과 부딪혔을 때
					else if (ball.getY() + ball.getHeight() / 2 - size == paddle.getY()
							&& (ball.getX() + ball.getWidth() >= paddle.getX()
									&& ball.getX() <= paddle.getX() + paddle.getWidth())) {
						switch (direction) {
						case "LEFTDOWN":
							direction = "LEFTUP";
							break;
						case "RIGHTDOWN":
							direction = "RIGHTUP";
							break;
						}
					}
					// 블록과 부딪혔는지 판단
					for (int i = 0; i < blockList.size(); i++) {
		                  Block block = blockList.get(i);
		                  if (block.isVisible() && (block.isHitted(ball.getX() + ball.getWidth() - 8, ball.getY() + 1) // 22
		                        || block.isHitted(ball.getX() + ball.getWidth() - 8,
		                              ball.getY() + ball.getHeight() - 2)
		                        || block.isHitted(ball.getX() + 1, ball.getY() + 1) || block.isHitted(ball.getX() + 1,
		                              ball.getY() + ball.getHeight() - 2))) {
		                     if (!block.getName().equals("6")) {
		                        scorePanel.increaseMoney();
		                     }
		                     if (block.getNum() == 1) {
		                        switch (block.getName()) {
		                        case "1":
		                           loadAudio("audio/block.wav");
		                           scorePanel.increaseScore(10);
		                           break;
		                        case "2":
		                           loadAudio("audio/block.wav");
		                           scorePanel.increaseScore(20);
		                           break;
		                        case "3":
		                           loadAudio("audio/block.wav");
		                           scorePanel.increaseScore(30);
		                           break;
		                        case "4":
		                           loadAudio("audio/block.wav");
		                           scorePanel.increaseScore(40);
		                           break;
		                        case "5":
		                           loadAudio("audio/mysteryBlock.wav");
		                           showItem(block.getX(), block.getY());
		                           break;
		                        }
		                        count++;
		                        block.setVisible(false);
		                     } else {
		                        block.decreaseNum();
		                     }
		                     // 블럭의 왼편에 맞았을 때 block.getX() > ball.getX() + ball.getWidth()/2
		                     // map[ball.getX()+1][ball.getY()] == 1
		                     if (block.getX() >= ball.getX() + ball.getWidth() - 8) {
		                        switch (direction) {
		                        case "RIGHTUP":
		                           direction = "LEFTUP";
		                           break;
		                        case "RIGHTDOWN":
		                           direction = "LEFTDOWN";
		                           break;
		                        }
		                     }
		                     // 블럭의 오른편에 맞았을 때 block.getX() + block.getWidth() < ball.getX() +
		                     // ball.getWidth()/2 map[ball.getX()-1][ball.getY()] == 1
		                     else if (block.getX() + block.getWidth() - 1 <= ball.getX() + 1) {
		                        switch (direction) {
		                        case "LEFTUP":
		                           direction = "RIGHTUP";
		                           break;
		                        case "LEFTDOWN":
		                           direction = "RIGHTDOWN";
		                           break;
		                        }
		                     }
		                     // 블럭의 아래에 맞았을 때 map[ball.getX()][ball.getY()-1] == 1
		                     else if (block.getY() + block.getHeight() - 1 <= ball.getY() + 1) {
		                        switch (direction) {
		                        case "RIGHTUP":
		                           direction = "RIGHTDOWN";
		                           break;
		                        case "LEFTUP":
		                           direction = "LEFTDOWN";
		                           break;
		                        }
		                     }
		                     // 블럭의 위에 맞았을 때 && ball.getY() <= block.getY() + block.getHeight()
		                     // map[ball.getX()][ball.getY()+1] == 1
		                     else if (block.getY() >= ball.getY() + ball.getHeight() - 2) {
		                        switch (direction) {
		                        case "RIGHTDOWN":
		                           direction = "RIGHTUP";
		                           break;
		                        case "LEFTDOWN":
		                           direction = "LEFTUP";
		                           break;
		                        }
		                     }
		                     //repaint();
		                  }
		               }
					if (direction.equals("LEFTUP")) {
						if (ball.getX() <= 0)
							direction = "RIGHTUP";
						if (ball.getY() <= 10)
							direction = "LEFTDOWN";
						ball.setLocation(ball.getX() - 1, ball.getY() - 1);
					} else if (direction.equals("RIGHTUP")) {
						if (ball.getY() <= 10)
							direction = "RIGHTDOWN";
						if (ball.getX() >= 550)
							direction = "LEFTUP";
						ball.setLocation(ball.getX() + 1, ball.getY() - 1);
					} else if (direction.equals("RIGHTDOWN")) {
						if (ball.getX() >= 550)
							direction = "LEFTDOWN";
						ball.setLocation(ball.getX() + 1, ball.getY() + 1);
					} else if (direction.equals("LEFTDOWN")) {
						if (ball.getX() <= 0)
							direction = "RIGHTDOWN";
						ball.setLocation(ball.getX() - 1, ball.getY() + 1);
					}
					try {
						sleep(ballSpeed);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	class BulletThread extends Thread {
		private int y;
		private Image exImg = new ImageIcon("explosion.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		private JLabel exIcon = new JLabel(new ImageIcon(exImg));
		private boolean stopFlag = false;

		public BulletThread(int y) {
			this.y = y;
		}

		public void stopBullet() {
			stopFlag = true;
		}

		synchronized public void resumeBullet() {
			stopFlag = false;
			notify();
		}

		synchronized private void checkWait() {
			if (stopFlag) {
				try {
					wait();
				} catch (InterruptedException e) {
					return;
				}
			}
		}

		@Override
		public void run() {
			while (true) {
				checkWait();
				for (int i = 0; i < blockList.size(); i++) {
					Block block = blockList.get(i);
					if (block.isVisible() && (block.isHitted(bullet.getX() + 32, bullet.getY() - 1)
							|| block.isHitted(bullet.getX() - 1, bullet.getY() - 1)
							|| block.isHitted(bullet.getX() + 32, bullet.getY() + 25)
							|| block.isHitted(bullet.getX() - 1, bullet.getY() + 25))) {
						scorePanel.increaseMoney();
						if (block.getNum() == 1) {
							switch (block.getName()) {
							case "1":
								loadAudio("audio/block.wav");
								scorePanel.increaseScore(10);
								break;
							case "2":
								loadAudio("audio/block.wav");
								scorePanel.increaseScore(20);
								break;
							case "3":
								loadAudio("audio/block.wav");
								scorePanel.increaseScore(30);
								break;
							case "4":
								loadAudio("audio/block.wav");
								scorePanel.increaseScore(40);
								break;
							case "5":
								loadAudio("audio/mysteryBlock.wav");
								showItem(block.getX(), block.getY());
								break;
							}
							count++;
							block.setVisible(false);
						} else {
							block.decreaseNum();
							repaint();
						}

						exIcon.setSize(100, 100);
						exIcon.setOpaque(false);
						exIcon.setBackground(new Color(0, 0, 0));
						exIcon.setLocation(bullet.getX() - 15, bullet.getY() - 40);
						add(exIcon);
						bullet.setLocation(bullet.getX(), y);
						bullet.setVisible(false);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
						}
						exIcon.setVisible(false);
						isBulletMoving = false;
						return;
					} else if (bullet.getY() == 0) {
						exIcon.setSize(100, 100);
						exIcon.setOpaque(false);
						exIcon.setBackground(new Color(0, 0, 0));
						exIcon.setLocation(bullet.getX() - 15, bullet.getY() - 35);
						add(exIcon);
						bullet.setLocation(bullet.getX(), y);
						bullet.setVisible(false);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
						}
						exIcon.setVisible(false);
						isBulletMoving = false;
					}
				}
				bullet.setLocation(bullet.getX(), bullet.getY() - 1);
				try {
					sleep(5);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	class IconThread extends Thread {
		private JLabel item;

		public IconThread(JLabel item) {
			this.item = item;
		}

		@Override
		public void run() {
			try {
				sleep(2000);
			} catch (InterruptedException e) {
			}
			remove(item);
			revalidate();
			repaint();
		}
	}
}
