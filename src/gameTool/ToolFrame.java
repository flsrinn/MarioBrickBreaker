package gameTool;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;

import gamePlayer.XMLReader;

public class ToolFrame extends JFrame {
	private GamePanel gamePanel;
	private ToolPanel toolPanel;
	private JSplitPane hPane = new JSplitPane();
	private int width=800;
	private int height=600;
	private JLabel[] musicLabel = new JLabel[4];
	private String filePath, fileName="";
	private XMLReader xml;
	
	public ToolFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("게임 저작 도구");
		gamePanel = new GamePanel(this);
		toolPanel = new ToolPanel(gamePanel);
		init();
		menu();
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		String[] musics = {"start.wav", "themesong.wav", "gameover.wav", "gameclear.wav"};
		
		for(int i=0; i<musics.length; i++) {
			musicLabel[i] = new JLabel(musics[i]);
		}
		
	}
	
	private void init() {
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
		hPane.setDividerLocation(width-200);
		hPane.setLeftComponent(gamePanel);
		hPane.setRightComponent(toolPanel);
		hPane.setDividerSize(0);
		add(hPane);
		setSize(width, height);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private void loadFile() {
		xml = new XMLReader(filePath);
		Node blockGameNode = xml.getBlockGameElement();
		Node sizeNode = XMLReader.getNode(blockGameNode, XMLReader.E_SCREENSIZE);
		width = Integer.parseInt(XMLReader.getAttr(sizeNode, "w"));
		height = Integer.parseInt(XMLReader.getAttr(sizeNode, "h"));
		init();
		gamePanel.loadGamePanel(xml.getGamePanelElement());
		toolPanel.loadToolPanel(xml.getGamePanelElement(), xml.getScorePanelElement());
	}
	
	private void menu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gamePanel.block.size()==0 || gamePanel.paddle.size()==0 || gamePanel.shooter.size()==0) {
					JOptionPane.showMessageDialog(null, "블록, 패들, 대포를 다 설치해야 저장이 가능합니다.", "저장 실패", JOptionPane.WARNING_MESSAGE);
					return;
				}
				saveFile("save");
			}
		});
		fileMenu.add(saveItem);
		JMenuItem saveAsItem = new JMenuItem("Save As");
		saveAsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gamePanel.block.size()==0 || gamePanel.paddle.size()==0 || gamePanel.shooter.size()==0) {
					JOptionPane.showMessageDialog(null, "블록, 패들, 대포를 다 설치해야 저장이 가능합니다.", "저장 실패", JOptionPane.WARNING_MESSAGE);
					return;
				}
				saveFile("save as");
			}
		});
		fileMenu.add(saveAsItem);
		JMenuItem loadItem = new JMenuItem("Load");
		loadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser("./map");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
				fileChooser.setFileFilter(filter);
				int ret = fileChooser.showOpenDialog(null);
				if (ret != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
					return;
				}
				filePath = fileChooser.getSelectedFile().getPath();
				fileName = fileChooser.getSelectedFile().getName();
				loadFile();
			}
		});
		fileMenu.add(loadItem);
		menuBar.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		JMenuItem undoItem = new JMenuItem("Undo");
		undoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!gamePanel.items.isEmpty()) {
		            JLabel lastItem = gamePanel.items.lastElement();
		            gamePanel.remove(lastItem);
		            gamePanel.items.remove(lastItem);
		            if(lastItem.getName().equals("shooter")) {
		            	gamePanel.shooter.remove(gamePanel.shooter.size()-1);
		            }
		            else if(lastItem.getName().equals("paddle")) {
		            	gamePanel.paddle.remove(gamePanel.paddle.size()-1);
		            }
		            else {
		            	gamePanel.block.remove(gamePanel.block.size()-1);
		            }
		            gamePanel.repaint();
		            gamePanel.revalidate();
		        }
			}
		});
		JMenuItem resizeItem = new JMenuItem("Resize");
		resizeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while(true) {
					try {
						width = Integer.parseInt(JOptionPane.showInputDialog("가로 길이를 입력해주세요. (800 이상)"));
						height = Integer.parseInt(JOptionPane.showInputDialog("세로 길이를 입력해주세요. (600 이상)"));
						if(width >= 800 && height >= 600) {
							break;
						} else {
							JOptionPane.showMessageDialog(null, "입력된 가로 혹은 세로 길이가 너무 작습니다. 다시 입력해주세요.");
						}
					} catch (NumberFormatException error) {
						JOptionPane.showMessageDialog(null, "잘못된 입력입니다. 숫자를 입력해주세요.");
					}
				}
				init();
			}
		});
		JMenuItem resetItem = new JMenuItem("Reset");
		resetItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.removeAll();
				gamePanel.repaint();
				gamePanel.revalidate();
				gamePanel.shooter.removeAllElements();
				gamePanel.paddle.removeAllElements();
				gamePanel.block.removeAllElements();
				gamePanel.items.removeAllElements();
			}
		});
		editMenu.add(undoItem);
		editMenu.add(resizeItem);
		editMenu.addSeparator();
		editMenu.add(resetItem);
		menuBar.add(editMenu);
		
		JMenu musicMenu = new JMenu("Music");
		JMenuItem settingMusic = new JMenuItem("Setting");
		settingMusic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				musicSetting();
			}
		});
		musicMenu.add(settingMusic);
		menuBar.add(musicMenu);
		
		JMenu imageMenu = new JMenu("Image");
		JMenuItem changeImage = new JMenuItem("Change");
		changeImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeImage();
			}
		});
		imageMenu.add(changeImage);
		menuBar.add(imageMenu);
		setJMenuBar(menuBar);
	}
	
	private void saveFile(String saveWay) {
		FileWriter fout = null;
		try {
			File file;
			int i = 1;
			if(saveWay.equals("save")) {
				if(fileName.equals("")) {
					file = new File("..\\XMLReaderForBlockGame\\map\\block" + i + ".xml");
					while(file.exists()) {
						i++;
						file = new File("..\\XMLReaderForBlockGame\\map\\block" + i + ".xml");
					}
				}
				else {
					file = new File("..\\XMLReaderForBlockGame\\map\\" + fileName);
					fout = new FileWriter(file);
				}
			}
			
			else if(saveWay.equals("save as")) {
				JFileChooser fileChooser = new JFileChooser("..\\XMLReaderForBlockGame\\map");
	            if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	                file = fileChooser.getSelectedFile();
	                fout = new FileWriter(file);
	            }
			}
			fout.write("<?xml version=\"1.0\" encoding=\"euc-kr\" ?> \r\n"
					+ "<BlockBreakGame>\r\n"
					+ "<GameScreen>\r\n");
			fout.write("\t<ScreenSize w=\"" + width + "\" h=\"" + height + "\"/>\r\n"
					+ "</GameScreen>\r\n");
			fout.write("<StartPanel>\r\n");
			fout.write("\t<Background>\r\n");
			fout.write("\t\t<BgImg>" + gamePanel.getBackImgName() + "</BgImg>\r\n");
			fout.write("\t\t<Music>audio/" + musicLabel[0].getText() + "</Music>\r\n");
			fout.write("\t</Background>\r\n");
			fout.write("</StartPanel>\r\n");
			fout.write("<GamePanel>\r\n");
			fout.write("\t<Background>\r\n");
			fout.write("\t\t<BgImg>" + gamePanel.getBackImgName() + "</BgImg>\r\n");
			fout.write("\t\t<Music>audio/" + musicLabel[1].getText() + "</Music>\r\n");
			fout.write("\t</Background>\r\n");
			fout.write("\t<Block>\r\n");
			Vector<String> blockVector = gamePanel.getVector("block");
			for(String blockObj : blockVector) {
				fout.write("\t\t" + blockObj + "\r\n");
			}
			fout.write("\t</Block>\r\n\t<Player>\r\n\t\t<Ball>\r\n\t\t\t");
			fout.write(toolPanel.getBall() + "\r\n\t\t</Ball>\r\n");
			fout.write("\t\t<Paddle>\r\n");
			Vector<String> paddleVector = gamePanel.getVector("paddle");
			for(String paddleObj : paddleVector) {
				fout.write("\t\t\t" + paddleObj + "\r\n");
			}
			fout.write("\t\t</Paddle>\r\n");
			fout.write("\t\t<Shooter>\r\n");
			Vector<String> shooterVector = gamePanel.getVector("shooter");
			for(String shooterObj : shooterVector) {
				fout.write("\t\t\t" + shooterObj + "\r\n");
			}
			fout.write("\t\t</Shooter>\r\n");
			fout.write("\t\t<Bullet>\r\n");
			fout.write("\t\t\t<Obj w=\"60\" h=\"40\" img=\"player/bullet.png\"/>\r\n");
			fout.write("\t\t</Bullet>\r\n");
			fout.write("\t</Player>\r\n</GamePanel>\r\n<ScorePanel>\r\n");
			fout.write("\t<Life>\r\n\t\t<Music>audio/");
			fout.write("</Music>\r\n");
			fout.write("\t\t<Heart>\r\n");
			fout.write("\t\t\t" + toolPanel.getLife() + "\r\n");
			fout.write("\t\t</Heart>\r\n\t</Life>\r\n</ScorePanel>\r\n");
			fout.write("<ItemPanel>\r\n");
			fout.write("\t<Background>\r\n");
			fout.write("\t\t<BgImg>background/animalBackground.png" + "</BgImg>\r\n");
			fout.write("\t</Background>\r\n");
			fout.write("</ItemPanel>\r\n<GameOverPanel>\r\n\t<Background>\r\n");
			fout.write("\t\t<BgImg>" + gamePanel.getBackImgName() + "</BgImg>\r\n");
			fout.write("\t\t<Music>audio/" + musicLabel[2].getText() +  "</Music>\r\n");
			fout.write("\t</Background>\r\n</GameOverPanel>\r\n<GameClearPanel>\r\n");
			
			fout.write("\t<Background>\r\n");
			fout.write("\t\t<BgImg>" + gamePanel.getBackImgName() + "</BgImg>\r\n");
			fout.write("\t\t<Music>audio/" + musicLabel[3].getText() + "</Music>\r\n");
			fout.write("\t</Background>\r\n");
			fout.write("</GameClearPanel>\r\n");
			fout.write("</BlockBreakGame>");
			fout.close();
		} catch(IOException e) {
			System.out.println("입력 오류");
		}
	}
	
	private void musicSetting() {
		JDialog settingsDialog = new JDialog(this, "Music Settings", true);
		settingsDialog.setLayout(null);
		settingsDialog.setSize(400, 300);
		
		settingsDialog.setResizable(false);
		String[] panels = {"시작 화면", "게임 배경음", "게임 오버", "게임 클리어"};
		
		for(int i=0; i<panels.length; i++) {
			musicLabel[i].setSize(150, 30);
			musicLabel[i].setLocation(200, 10 + (i*40));
			settingsDialog.add(musicLabel[i]);
			
			JButton panelButton = new JButton(panels[i]);
			panelButton.setSize(150, 30);
			panelButton.setLocation(40, 10 + (i*40));
			panelButton.addActionListener(new MusicSettingListener(musicLabel[i]));
			panelButton.setFocusable(false);
			settingsDialog.add(panelButton);
		}
		settingsDialog.setLocationRelativeTo(null);
		settingsDialog.setVisible(true);
	}
	
	class MusicSettingListener implements ActionListener {
		private JLabel musicLabel;
		
		public MusicSettingListener(JLabel musicLabel) {
			this.musicLabel = musicLabel;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser("./audio");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio Files", "wav");
			fileChooser.setFileFilter(filter);
			
			int ret = fileChooser.showOpenDialog(null);
			if (ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String filePath = fileChooser.getSelectedFile().getPath();
			musicLabel.setText(fileChooser.getSelectedFile().getName());
			revalidate();repaint(); 
		}
	}

	private void changeImage() {
		JDialog setImageDialog = new JDialog(this, "Image Settings", true);
		setImageDialog.setLayout(null);
		setImageDialog.setSize(500, 400);
		
		setImageDialog.setResizable(false);
		setImageDialog.setLocationRelativeTo(null);
		String[] panels = {"일반 블록", "미스터리 블록", "장애물 블록", "패들", "배경 화면"};
		
		JLabel buttonImg[] = new JLabel[5];
		for(int i=0; i<panels.length; i++) {
			JButton itemButton = new JButton(panels[i]);
			itemButton.setSize(140, 30);
			itemButton.setLocation(120, 30 + (i*60));
			itemButton.setFocusable(false);
			
			setImageDialog.add(itemButton);
			buttonImg[i] = new JLabel();
			Image buttonImage = null;
			String itemInfo;
			if(i == 0) {
				itemInfo = toolPanel.getItemIcon("block");
				
				buttonImage = new ImageIcon(itemInfo).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				buttonImg[i].setName(itemInfo);
			}
			else if(i == 1) {
				itemInfo = toolPanel.getItemIcon("mystery");
				buttonImage = new ImageIcon(itemInfo).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				buttonImg[i].setName(itemInfo);
			}
			else if(i == 2) {
				itemInfo = toolPanel.getItemIcon("obstacle");
				buttonImage = new ImageIcon(itemInfo).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				buttonImg[i].setName(itemInfo);
			}
			else if(i == 3) {
				itemInfo = toolPanel.getItemIcon("paddle");
				buttonImage = new ImageIcon(itemInfo).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				buttonImg[i].setName(itemInfo);
			}
			else {
				if(gamePanel.getBackImg() != null) {
					buttonImage = gamePanel.getBackImg().getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
					buttonImg[i].setName(gamePanel.getBackImgName());
				}
				else {
					buttonImage = null;
				}
			}
			if(buttonImage != null) {
				buttonImg[i].setIcon(new ImageIcon(buttonImage));
			}
			buttonImg[i].setSize(50, 50);
			buttonImg[i].setLocation(310, 20 + (i * 60));
			setImageDialog.add(buttonImg[i]);
			itemButton.addActionListener(new ImageSettingListener(buttonImg[i]));
		}
		
		JButton applyButton = new JButton("Apply");
		applyButton.setSize(80, 20);
		applyButton.setLocation(210, 320);
		applyButton.setFocusable(false);
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.setBackImg(buttonImg[4].getName());
				toolPanel.changeButtonImg(buttonImg[0].getName(), buttonImg[1].getName(), buttonImg[2].getName(), buttonImg[3].getName());
				JOptionPane.showMessageDialog(null, "적용되었습니다", "완료", JOptionPane.DEFAULT_OPTION);
				setImageDialog.dispose(); 
			}
		});
		setImageDialog.add(applyButton);
		setImageDialog.setVisible(true);
	}
	
	class ImageSettingListener implements ActionListener {
		private JLabel imageLabel;
		
		public ImageSettingListener(JLabel imageLabel) {
			this.imageLabel = imageLabel;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser("./");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("img Files", "png", "jpg", "jpeg", "gif");
			fileChooser.setFileFilter(filter);
			
			int ret = fileChooser.showOpenDialog(null);
			if(ret != JFileChooser.APPROVE_OPTION) { 
				JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.");
				return;
			}
			String filePath = fileChooser.getSelectedFile().getPath();
			String fileName = fileChooser.getSelectedFile().getName();
			imageLabel.setIcon(new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
			imageLabel.setName(fileName);
			repaint();
		}
	}
}
