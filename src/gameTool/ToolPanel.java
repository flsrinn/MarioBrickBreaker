package gameTool;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gamePlayer.XMLReader;

public class ToolPanel extends JPanel {
	private GamePanel gamePanel;
	private JRadioButton block, mysteryBlock, obstacleBlock, paddle, shooter;
	private ImageIcon blockIcon, mysteryBlockIcon, obstacleBlockIcon, paddleIcon, shooterIcon;
	private JLabel check = new JLabel("V");
	private int blockW=50, blockH=50, mysteryW=50, mysteryH=50, unW=50, unH=50, paddleW=50, paddleH=50, shooterW=50, shooterH=50;
	private int ballSize=1, ballSpeed=1, balls=1, lives=3;
	private JLabel ballSizeLabel, ballSpeedLabel, ballsLabel, lifeLabel;
	private String ballImagePath = "player/shell.png";
	private String ball, life;
	private JLabel ballLabel;
	
	public ToolPanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		setLayout(null);
		setBackground(Color.DARK_GRAY);
		ButtonGroup g = new ButtonGroup();
		blockIcon = new ImageIcon("block/block.png");
		Image blockImg = blockIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		blockIcon = new ImageIcon(blockImg);
		block = new JRadioButton(blockIcon, true);
		block.setBorderPainted(true);
		block.setOpaque(false);
		block.setBounds(60, 10, 60, 60);
		String[] blockType = { "1", "2", "3", "4" };
		JComboBox<String> blockTypeComboBox = new JComboBox<String>(blockType);
		blockTypeComboBox.setSize(40, 20);
		blockTypeComboBox.setLocation(130, 30);
		blockIcon.setDescription(Integer.toString(blockTypeComboBox.getSelectedIndex()+1));
		blockTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				blockIcon.setDescription(Integer.toString(blockTypeComboBox.getSelectedIndex()+1));
			}
		});
		add(blockTypeComboBox);
		
		mysteryBlockIcon = new ImageIcon("block/mysteryBlock.png");
		Image mysteryBlockImg = mysteryBlockIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		mysteryBlockIcon = new ImageIcon(mysteryBlockImg);
		mysteryBlockIcon.setDescription("5");
		mysteryBlock = new JRadioButton(mysteryBlockIcon);
		mysteryBlock.setBorderPainted(true);
		mysteryBlock.setOpaque(false);
		mysteryBlock.setBounds(60, 90, 60, 60);
		
		obstacleBlockIcon = new ImageIcon("block/unBlock.png");
		Image obstacleBlockImg = obstacleBlockIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		obstacleBlockIcon = new ImageIcon(obstacleBlockImg);
		obstacleBlockIcon.setDescription("6");
		obstacleBlock = new JRadioButton(obstacleBlockIcon);
		obstacleBlock.setBorderPainted(true);
		obstacleBlock.setOpaque(false);
		obstacleBlock.setBounds(60, 170, 60, 60);
		
		paddleIcon = new ImageIcon("player/paddle.png");
		Image paddleImg = paddleIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		paddleIcon = new ImageIcon(paddleImg);
		paddleIcon.setDescription("paddle");
		paddle = new JRadioButton("");
		paddle.setIcon(paddleIcon);
		paddle.setBorderPainted(true);
		paddle.setOpaque(false);
		paddle.setBounds(60, 250, 60, 60);
		
		shooterIcon = new ImageIcon("player/shooter.png");
		Image shooterImg = shooterIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		shooterIcon = new ImageIcon(shooterImg);
		shooter = new JRadioButton(shooterIcon);
		shooterIcon.setDescription("shooter");
		shooter.setBorderPainted(true);
		shooter.setOpaque(false);
		shooter.setBounds(60, 350, 60, 60);
		
		g.add(block);
		g.add(mysteryBlock);
		g.add(obstacleBlock);
		g.add(paddle);
		g.add(shooter);
		
		add(block);
		add(mysteryBlock);
		add(obstacleBlock);
		add(paddle);
		add(shooter);
		
		block.setActionCommand("block/block.png");
		mysteryBlock.setActionCommand("block/mysteryBlock.png");
		obstacleBlock.setActionCommand("block/unBlock.png");
		paddle.setActionCommand("player/paddle.png");
		shooter.setActionCommand("player/shooter.png");
		
		RadioButtonListener listener = new RadioButtonListener();
		block.addItemListener(listener);
		mysteryBlock.addItemListener(listener);
		obstacleBlock.addItemListener(listener);
		paddle.addItemListener(listener);
		shooter.addItemListener(listener);
		
		JButton resizeOption = new JButton("RESIZE");
		resizeOption.setFont(new Font("DungGeunMo", Font.PLAIN, 15));
		resizeOption.setSize(80, 20);
		resizeOption.setLocation(50, 320);
		resizeOption.setFocusable(false);
		resizeOption.setOpaque(false);
		resizeOption.setForeground(Color.white);
		resizeOption.setBackground(new Color(0,0,0));
		resizeOption.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(shooter.isSelected()) return;
				ImageIcon selectedIcon = null;
				int width, height;
				while(true) {
					String w = JOptionPane.showInputDialog("가로폭 사이즈를 입력하세요. (20 이상 100 이하)");
					String h = JOptionPane.showInputDialog("세로폭 사이즈를 입력하세요. (30 이상 70 이하)");
					if(w == null || h == null) {
						JOptionPane.showMessageDialog(null, "사이즈를 입력하지 않았습니다.", "경고", JOptionPane.ERROR_MESSAGE);
					}
					else if(w.equals("") || h.equals("")) {
						JOptionPane.showMessageDialog(null, "사이즈를 입력하지 않았습니다.", "경고", JOptionPane.ERROR_MESSAGE);
					}
					else if((Integer.parseInt(w) < 20 || Integer.parseInt(w) > 100) || (Integer.parseInt(h) < 30 || Integer.parseInt(h) > 70)) {
						JOptionPane.showMessageDialog(null, "가로 20 이상 100 이하, 세로 30 이상 50 이하로 입력하셔야 합니다.", "경고", JOptionPane.ERROR_MESSAGE);
					}
					else {
						width = Integer.parseInt(w);
						height = Integer.parseInt(h);
						break;
					}
				}
				
				if (block.isSelected()) {
					blockW = width;
					blockH = height;
					gamePanel.getIconInfo(blockIcon, blockW, blockH, block.getActionCommand());
				}
			}
		});
		add(resizeOption);
		
		check.setFont(new Font("DungGeunMo", Font.BOLD, 30));
		check.setLocation(30, block.getY() + 10);
		check.setSize(20, 30);
		check.setForeground(Color.WHITE);
		add(check);
		
		gamePanel.getIconInfo(blockIcon, blockW, blockH, block.getActionCommand());
		
		ImageIcon ballIcon = new ImageIcon(new ImageIcon(ballImagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		ballLabel = new JLabel(ballIcon);
		ballLabel.setSize(50, 50);
		ballLabel.setLocation(30, 420);
		add(ballLabel);
		
		JButton ballButton = new JButton("BALL");
		ballButton.setSize(70, 20);
		ballButton.setFont(new Font("DungGeunMo", Font.PLAIN, 15));
		ballButton.setLocation(90, 433);
		ballButton.setFocusable(false);
		ballButton.setOpaque(false);
		ballButton.setForeground(Color.white);
		ballButton.setBackground(new Color(0,0,0));
		ballButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ballSettingDialog();
			}
		});
		add(ballButton);
		
		ballSizeLabel = new JLabel("크기: " + ballSize);
		ballSizeLabel.setSize(70, 12);
		ballSizeLabel.setLocation(10, 480);
		ballSizeLabel.setFont(new Font("DungGeunMo", Font.PLAIN, 12));
		ballSizeLabel.setForeground(Color.white);
		add(ballSizeLabel);
		
		ballSpeedLabel = new JLabel("속도: " + ballSpeed);
		ballSpeedLabel.setSize(70, 12);
		ballSpeedLabel.setLocation(65, 480);
		ballSpeedLabel.setFont(new Font("DungGeunMo", Font.PLAIN, 12));
		ballSpeedLabel.setForeground(Color.white);
		add(ballSpeedLabel);
		
		ballsLabel = new JLabel("개수: " + balls);
		ballsLabel.setSize(70, 12);
		ballsLabel.setLocation(120, 480);
		ballsLabel.setFont(new Font("DungGeunMo", Font.PLAIN, 12));
		ballsLabel.setForeground(Color.white);
		add(ballsLabel);
		
		ball = "<Obj size=\"" + Integer.toString(ballSize) + "\" balls=\"" +
				balls + "\" speed=\"" + Integer.toString(ballSpeed) + "\" img=\"" + ballImagePath + "\"/>";
	
		JButton lifeButton = new JButton("LIFE");
		lifeButton.setSize(70, 20);
		lifeButton.setFont(new Font("DungGeunMo", Font.PLAIN, 15));
		lifeButton.setLocation(90, 505);
		lifeButton.setFocusable(false);
		lifeButton.setOpaque(false);
		lifeButton.setForeground(Color.white);
		lifeButton.setBackground(new Color(0,0,0));
		lifeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while(true) {
					String lifeInput = null;
					lifeInput = JOptionPane.showInputDialog("생명 개수를 입력해주세요. (1 이상 5 이하)");
					if(lifeInput == null) {
				        return;
				    }
					else if(lifeInput.equals("")) {
						JOptionPane.showMessageDialog(null, "입력하지 않았습니다.", "경고", JOptionPane.ERROR_MESSAGE);
						continue;
					}
					try {
				        lives = Integer.parseInt(lifeInput);
				        if(lives >= 1 && lives <= 5) {
				            break;
				        }
				        JOptionPane.showMessageDialog(null, "1 이상 5 이하의 수를 입력해야 합니다.", "경고", JOptionPane.ERROR_MESSAGE);
				    } catch (NumberFormatException error) {
				        // 숫자가 아닌 값이 입력되었을 경우
				        JOptionPane.showMessageDialog(null, "숫자를 입력해야 합니다.", "경고", JOptionPane.ERROR_MESSAGE);
				    }
				}
				lifeLabel.setText("생명: " + lives);
				life = "<Obj life=\"" + Integer.toString(lives) + "\" img=\"item/heart.png\"/>";
				revalidate();
				repaint();
			}
		});
		add(lifeButton);
		
		lifeLabel = new JLabel("생명: " + lives);
		lifeLabel.setSize(70, 12);
		lifeLabel.setLocation(30, 509);
		lifeLabel.setFont(new Font("DungGeunMo", Font.PLAIN, 12));
		lifeLabel.setForeground(Color.white);
		add(lifeLabel);
		
		life = "<Obj life=\"" + Integer.toString(lives) + "\" img=\"item/heart.png\"/>";
	}
	
	public void loadToolPanel(Node toolPanelNode, Node scorePanelNode) {
		Node ballNode = XMLReader.getNode(toolPanelNode, XMLReader.E_BALL);
		NodeList ballNodeList = ballNode.getChildNodes();
		ballNode = ballNodeList.item(1);
		if(ballNode.getNodeName().equals(XMLReader.E_OBJ)) {
			ballSpeed = Integer.parseInt(XMLReader.getAttr(ballNode, "speed"));
			balls = Integer.parseInt(XMLReader.getAttr(ballNode, "balls"));
			ballSize = Integer.parseInt(XMLReader.getAttr(ballNode, "size"));
			ballImagePath = XMLReader.getAttr(ballNode, "img");
			
			ballSizeLabel.setText("크기: " + ballSize);
	        ballSpeedLabel.setText("속도: " + ballSpeed);
	        ballsLabel.setText("개수: " + balls);
	        
	        ball = "<Obj size=\"" + Integer.toString(ballSize) + "\" balls=\"" +
					balls + "\" speed=\"" + Integer.toString(ballSpeed) + "\" img=\"" + ballImagePath + "\"/>";
	        ballLabel.setIcon(new ImageIcon(new ImageIcon(ballImagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
		}
		
		Node lifeNode = XMLReader.getNode(scorePanelNode, XMLReader.E_HEART);
		NodeList lifeNodeList = lifeNode.getChildNodes();
		lifeNode = lifeNodeList.item(1);
		if(lifeNode.getNodeName().equals(XMLReader.E_OBJ)) {
			lives = Integer.parseInt(XMLReader.getAttr(lifeNode, "life"));
			
			lifeLabel.setText("생명: " + lives);
	        
			life = "<Obj life=\"" + Integer.toString(lives) + "\" img=\"item/heart.png\"/>";
		}
	}
	
	public String getBall() {
		return ball;
	}
	
	public String getLife() {
		return life;
	}
	
	public String getItemIcon(String item) {
		String info = "";
		switch(item) {
		case "block":
			info = block.getActionCommand();
			break;
		case "mystery":
			info = mysteryBlock.getActionCommand();
			break;
		case "obstacle":
			info = obstacleBlock.getActionCommand();
			break;
		case "paddle":
			info = paddle.getActionCommand();
			break;
		}
		return info;
	}
	
	private void ballSettingDialog() {
		// 팝업창 띄우기
		JDialog settingsDialog = new JDialog();
		settingsDialog.setLayout(null);
		settingsDialog.setSize(400, 300);
		settingsDialog.setTitle("Ball Settings");
		settingsDialog.setModal(true);
		JLabel sizeLabel = new JLabel("크기");
		sizeLabel.setSize(50, 20);
		sizeLabel.setLocation(120, 15);
		settingsDialog.add(sizeLabel);
		String[] ballSizes = { "1", "2", "3" };
		JComboBox<String> ballSizeComboBox = new JComboBox<String>(ballSizes);
		ballSizeComboBox.setSelectedIndex(ballSize-1);
		ballSizeComboBox.setSize(100, 30);
		ballSizeComboBox.setLocation(170, 10);
		settingsDialog.add(ballSizeComboBox);
		
		JLabel speedLabel = new JLabel("속도");
		speedLabel.setSize(50, 20);
		speedLabel.setLocation(120, 65);
		settingsDialog.add(speedLabel);
		String[] ballSpeeds = { "1", "2", "3", "4" };
		JComboBox<String> ballSpeedComboBox = new JComboBox<String>(ballSpeeds);
		ballSpeedComboBox.setSelectedIndex(ballSpeed-1);
		ballSpeedComboBox.setSize(100, 30);
		ballSpeedComboBox.setLocation(170, 60);
		settingsDialog.add(ballSpeedComboBox);
		
		JLabel ballCountLabel = new JLabel("개수");
		ballCountLabel.setSize(50, 20);
		ballCountLabel.setLocation(120, 115);
		settingsDialog.add(ballCountLabel);
		JTextField ballsInput = new JTextField(Integer.toString(balls));
		ballsInput.setSize(100, 30);
		ballsInput.setLocation(170, 110);
		settingsDialog.add(ballsInput);
		
		JLabel ballImage = new JLabel(new ImageIcon(new ImageIcon(ballImagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
		ballImage.setBounds(210, 160, 50, 50);
		settingsDialog.add(ballImage);
		JButton ballImageButton = new JButton("이미지");
		ballImageButton.setSize(75, 20);
		ballImageButton.setLocation(100, 170);
		ballImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser("./player");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("img Files", "png", "jpg", "jpeg", "gif");
				fileChooser.setFileFilter(filter);
				
				int ret = fileChooser.showOpenDialog(null);
				if(ret != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.");
					return;
				}
				ballImagePath = "player/" + fileChooser.getSelectedFile().getName();
				ballImage.setIcon(new ImageIcon(new ImageIcon(ballImagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
				repaint();
			}
		});
		settingsDialog.add(ballImageButton);
		
		JButton applyButton = new JButton("Apply");
		applyButton.setLocation(155, 230);
		applyButton.setSize(80, 20);
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ballSize = ballSizeComboBox.getSelectedIndex() + 1;
				ballSpeed = ballSpeedComboBox.getSelectedIndex()+1;
				balls = Integer.parseInt(ballsInput.getText());
				
				ballSizeLabel.setText("크기: " + ballSize);
		        ballSpeedLabel.setText("속도: " + ballSpeed);
		        ballsLabel.setText("개수: " + balls);
		        ballLabel.setIcon(new ImageIcon(new ImageIcon(ballImagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
		        ball = "<Obj size=\"" + Integer.toString(ballSize) + "\" balls=\"" +
						balls + "\" speed=\"" + Integer.toString(ballSpeed) + "\" img=\"" + ballImagePath + "\"/>";
		        
		        JOptionPane.showMessageDialog(null, "적용되었습니다", "완료", JOptionPane.DEFAULT_OPTION);
				settingsDialog.dispose(); 
				revalidate();
				repaint();
			}
		});
		settingsDialog.add(applyButton);
		settingsDialog.setLocationRelativeTo(null);
		settingsDialog.setResizable(false);
		settingsDialog.setVisible(true);
	}
	
	class RadioButtonListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(block.isSelected()) {
				gamePanel.getIconInfo(blockIcon, blockW, blockH, block.getActionCommand());
				check.setLocation(30, block.getY() + 10);
			}
			else if(mysteryBlock.isSelected()) {
				gamePanel.getIconInfo(mysteryBlockIcon, mysteryW, mysteryH, mysteryBlock.getActionCommand());
				check.setLocation(30, mysteryBlock.getY() + 10);
			}
			else if(obstacleBlock.isSelected()) {
				gamePanel.getIconInfo(obstacleBlockIcon, unW, unH, obstacleBlock.getActionCommand());
				check.setLocation(30, obstacleBlock.getY() + 10);
			}
			else if(paddle.isSelected()) {
				gamePanel.getIconInfo(paddleIcon, paddleW, paddleH, paddle.getActionCommand());
				check.setLocation(30, paddle.getY() + 10);
			}
			else if(shooter.isSelected()) {
				gamePanel.getIconInfo(shooterIcon, shooterW, shooterH, shooter.getActionCommand());
				check.setLocation(30, shooter.getY() + 10);
			}
			add(check);
		}
		
	}

	public void changeButtonImg(String blockName, String mysteryBlockName, String unBlockName, String paddleName) {
		if(!block.getActionCommand().equals(blockName)) {
			blockIcon.setImage(new ImageIcon("block/" + blockName).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			block.setIcon(blockIcon);
			block.setActionCommand("block/" + blockName);
			block.repaint();
			if(block.isSelected()) {
				gamePanel.getIconInfo(blockIcon, blockW, blockH, block.getActionCommand());
			}
		}
		if(!mysteryBlock.getActionCommand().equals(mysteryBlockName)) {
			mysteryBlockIcon.setImage(new ImageIcon("block/" + mysteryBlockName).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			mysteryBlock.setIcon(mysteryBlockIcon);
			mysteryBlock.setActionCommand("block/" + mysteryBlockName);
			mysteryBlock.repaint();
			if(mysteryBlock.isSelected()) {
				gamePanel.getIconInfo(blockIcon, blockW, blockH, block.getActionCommand());
			}
		}
		
		if(!obstacleBlock.getActionCommand().equals(unBlockName)) {
			obstacleBlockIcon.setImage(new ImageIcon("block/" + unBlockName).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			obstacleBlock.setIcon(obstacleBlockIcon);
			obstacleBlock.setActionCommand("block/" + unBlockName);
			obstacleBlock.repaint();
			if(obstacleBlock.isSelected()) {
				gamePanel.getIconInfo(blockIcon, blockW, blockH, block.getActionCommand());
			}
		}
		if(!paddle.getActionCommand().equals(paddleName)) {
			paddleIcon.setImage(new ImageIcon("player/" + paddleName).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			paddle.setIcon(paddleIcon);
			paddle.setActionCommand("player/" + paddleName);
			paddle.repaint();
			if(paddle.isSelected()) {
				gamePanel.getIconInfo(blockIcon, blockW, blockH, block.getActionCommand());
			}
		}
	}
}
