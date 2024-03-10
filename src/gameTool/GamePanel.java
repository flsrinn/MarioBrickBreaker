package gameTool;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gamePlayer.Block;
import gamePlayer.XMLReader;

public class GamePanel extends JPanel {
	private ToolFrame toolFrame;
	private ImageIcon bgImg = null;
	private String imgFileName = "";
	private ImageIcon itemIcon = null;
	private int itemWidth, itemHeight;
	private String itemImgPath = "";
	public Vector<String> block = new Vector<String>();
	public Vector<String> paddle = new Vector<String>();
	public Vector<String> shooter = new Vector<String>();
	public Vector<JLabel> items = new Vector<JLabel>();
	
	public GamePanel(ToolFrame toolFrame) {
		this.toolFrame = toolFrame;
		setLayout(null);
		GamePanelMouseListener gamePanelMouseListener = new GamePanelMouseListener();
		addMouseListener(gamePanelMouseListener);
		addMouseMotionListener(gamePanelMouseListener);
		setFocusable(true);
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection emptySelection = new StringSelection("");
		clipboard.setContents(emptySelection, null);
	}
	
	public void loadGamePanel(Node gamePanelNode) {
		removeAll();
		block.removeAllElements();
		paddle.removeAllElements();
		shooter.removeAllElements();
		items.removeAllElements();
		
		Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BACKGROUND);
		Node bgimgNode = XMLReader.getNode(bgNode, XMLReader.E_BGIMG);
		bgImg = new ImageIcon(bgimgNode.getTextContent());
		imgFileName = bgimgNode.getTextContent();
		
		Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
		NodeList blockNodeList = blockNode.getChildNodes();
		for (int i = 0; i < blockNodeList.getLength(); i++) {
			Node node = blockNodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				String type = XMLReader.getAttr(node, "type");
				String imgSrc = XMLReader.getAttr(node, "img");
				ImageIcon icon = new ImageIcon(imgSrc);
				
				JLabel blockLabel = new JLabel();
				blockLabel.setBounds(x, y, w, h);
				icon = new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
				icon.setDescription(type);
				blockLabel.setIcon(icon);
				
				block.add("<Obj x=\"" + x + "\" y=\"" + y +
						"\" w=\"" + w + "\" h=\"" + h + 
						"\" type=\"" + type + "\" img=\"" + imgSrc + "\"/>");
				System.out.println(block.size());
				blockLabel.setName("block");
				add(blockLabel);
				
				ItemListener itemListener = new ItemListener(blockLabel, block.size()-1, icon, imgSrc);
				blockLabel.addMouseListener(itemListener);
				blockLabel.addMouseMotionListener(itemListener);
				items.add(blockLabel);
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
			String imgSrc = XMLReader.getAttr(paddleNode, "img");
			ImageIcon icon = new ImageIcon(imgSrc);
			JLabel paddleLabel = new JLabel();
			paddleLabel.setBounds(x, y, w, h);
			icon = new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
			paddleLabel.setIcon(icon);
			paddle.add("<Obj x=\"" + x + "\" y=\"" + y +
					"\" w=\"" + w + "\" h=\"" + h + 
					"\" img=\"" + imgSrc + "\"/>");
			paddleLabel.setName("paddle");
			add(paddleLabel);
			
			ItemListener itemListener = new ItemListener(paddleLabel, paddle.size()-1, icon, imgSrc);
			paddleLabel.addMouseListener(itemListener);
			paddleLabel.addMouseMotionListener(itemListener);
			items.add(paddleLabel);
		}
		Node shooterNode = XMLReader.getNode(gamePanelNode, XMLReader.E_SHOOTER);
		NodeList shooterNodeList = shooterNode.getChildNodes();
		shooterNode = shooterNodeList.item(1);
		if (shooterNode.getNodeName().equals(XMLReader.E_OBJ)) {
			int x = Integer.parseInt(XMLReader.getAttr(shooterNode, "x"));
			int y = Integer.parseInt(XMLReader.getAttr(shooterNode, "y"));
			int w = Integer.parseInt(XMLReader.getAttr(shooterNode, "w"));
			int h = Integer.parseInt(XMLReader.getAttr(shooterNode, "h"));
			String imgSrc = XMLReader.getAttr(shooterNode, "img");
			ImageIcon icon = new ImageIcon(imgSrc);
			JLabel shooterLabel = new JLabel();
			shooterLabel.setBounds(x, y, w, h);
			icon = new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
			shooterLabel.setIcon(icon);
			shooter.add("<Obj x=\"" + x + "\" y=\"" + y +
					"\" w=\"" + w + "\" h=\"" + h + 
					"\" img=\"" + imgSrc + "\"/>");
			shooterLabel.setName("shooter");
			add(shooterLabel);
			
			ItemListener itemListener = new ItemListener(shooterLabel, shooter.size()-1, icon, imgSrc);
			shooterLabel.addMouseListener(itemListener);
			shooterLabel.addMouseMotionListener(itemListener);
			items.add(shooterLabel);
		}
	}
	
	class GamePanelMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				JLabel item = new JLabel(new ImageIcon(itemIcon.getImage().getScaledInstance(itemWidth, itemHeight, Image.SCALE_SMOOTH)));
				item.setSize(itemWidth, itemHeight);
				int x = e.getX();
				int y = e.getY();
				if(x + item.getWidth() > toolFrame.getWidth()-250) {
					x = toolFrame.getWidth() - 250;
				}
				else if(y + item.getHeight() > toolFrame.getHeight() - 240) {
					y = toolFrame.getHeight() - 240;
				}
				item.setLocation(x, y);
				ItemListener itemListener;
				
				if(itemIcon.getDescription().equals("paddle")) {
					if(paddle.size() == 0) {
						paddle.add("<Obj x=\"" + x + "\" y=\"" + (getHeight()-100) +
								"\" w=\"" + itemWidth + "\" h=\"" + itemHeight + 
								"\" img=\"" + itemImgPath + "\"/>");
						item.setName("paddle");
						item.setLocation(x, getHeight()-100);
						itemListener = new ItemListener(item, paddle.size()-1, itemIcon, itemImgPath);
					}
					else {
						JOptionPane.showMessageDialog(null, "패들은 하나만 추가 가능합니다.", "경고", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				else if(itemIcon.getDescription().equals("shooter")) {
					if(shooter.size() == 0) {
						shooter.add("<Obj x=\"" + x + "\" y=\"" + (getHeight()-50) +
								"\" w=\"" + itemWidth + "\" h=\"" + itemHeight + 
								"\" img=\"" + itemImgPath + "\"/>");
						item.setName("shooter");
						item.setLocation(x, getHeight()-50);
						itemListener = new ItemListener(item, shooter.size()-1, itemIcon, itemImgPath);
					}
					else {
						JOptionPane.showMessageDialog(null, "대포는 하나만 추가 가능합니다.", "경고", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				else {
					for(int i=0; i<items.size(); i++) {
			    		JLabel itemVector = items.get(i);
			    		if(itemVector.getName().equals("paddle") || itemVector.getName().equals("shooter"))
			    			continue;
			    		if((itemVector.getX() <= x + item.getWidth() && itemVector.getX()+itemVector.getWidth() >= x)
			    				&& (itemVector.getY() <= y + item.getHeight() && itemVector.getY() + itemVector.getHeight() >= y)) 
			    			return;
			    	}
					block.add("<Obj x=\"" + x + "\" y=\"" + y +
							"\" w=\"" + itemWidth + "\" h=\"" + itemHeight + 
							"\" type=\"" + itemIcon.getDescription() + "\" img=\"" + itemImgPath + "\"/>");
					item.setName("block");
					itemListener = new ItemListener(item, block.size()-1, itemIcon, itemImgPath);
				}
				
				add(item);
				item.addMouseListener(itemListener);
				item.addMouseMotionListener(itemListener);
				items.add(item);
				repaint();
			}
			
			else if(e.getButton() == MouseEvent.BUTTON3) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			    try {
			        String pasteText = (String)clipboard.getData(DataFlavor.stringFlavor);
			        StringTokenizer st = new StringTokenizer(pasteText, ",");
			        int width = Integer.parseInt(st.nextToken());
			        int height = Integer.parseInt(st.nextToken());
			        String type = st.nextToken();
			        String filePath = st.nextToken();
			        ImageIcon itemIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
			        JLabel item = new JLabel(itemIcon);
			        item.setSize(width, height);
			        item.setLocation(e.getX(), e.getY());
			        ItemListener itemListener;
			        if(type.equals("paddle")) {
						if(paddle.size() == 0) {
							paddle.add("<Obj x=\"" + e.getX() + "\" y=\"" + (getHeight()-100) +
									"\" w=\"" + width + "\" h=\"" + height + 
									"\" img=\"" + filePath + "\"/>");
							item.setName("paddle");
							item.setLocation(e.getX(), getHeight()-100);
							itemListener = new ItemListener(item, paddle.size()-1, itemIcon, itemImgPath);
						}
						else {
							JOptionPane.showMessageDialog(null, "패들은 하나만 추가 가능합니다.", "경고", JOptionPane.WARNING_MESSAGE);
							return;
						}
					}
					else if(type.equals("shooter")) {
						if(shooter.size() == 0) {
							shooter.add("<Obj x=\"" + e.getX() + "\" y=\"" + (getHeight()-50) +
									"\" w=\"" + width + "\" h=\"" + height + 
									"\" img=\"" + filePath + "\"/>");
							item.setName("shooter");
							item.setLocation(e.getX(), getHeight()-50);
							itemListener = new ItemListener(item, shooter.size()-1, itemIcon, itemImgPath);
						}
						else {
							JOptionPane.showMessageDialog(null, "대포는 하나만 추가 가능합니다.", "경고", JOptionPane.WARNING_MESSAGE);
							return;
						}
					}
					else {
						block.add("<Obj x=\"" + e.getX() + "\" y=\"" + e.getY() +
								"\" w=\"" + width + "\" h=\"" + height + 
								"\" type=\"" + type + "\" img=\"" + filePath + "\"/>");
						item.setName("block");
						itemIcon.setDescription(type);
						itemListener = new ItemListener(item, block.size()-1, itemIcon, itemImgPath);
					}
			        item.addMouseListener(itemListener);
			        item.addMouseMotionListener(itemListener);
			        items.add(item);
			        add(item);
			       
			        item.repaint();
			    } catch (UnsupportedFlavorException | IOException | NumberFormatException | NoSuchElementException ex) {
			        return;
			    }
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			toolFrame.setTitle("게임 저작 도구 " + "(" + e.getX() + "," + e.getY() + ")");
		}
	}
	
	class ItemListener extends MouseAdapter {
		private JLabel item;
		private int count = 0;
		private int index;
		private ImageIcon img;
		private String imgPath;
		
		public ItemListener(JLabel item, int index, ImageIcon img, String imgPath) {
			this.item = item;
			this.index = index;
			this.img = img;
			this.imgPath = imgPath;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON3) {
				count++;
				if(count % 2 == 0) {
					JPopupMenu itemSettingMenu = new JPopupMenu();
					itemSettingMenu.setLayout(null);
					itemSettingMenu.setSize(200, 150);
					
					if(item.getName().equals("block")) {
						JMenuItem typeItem = new JMenuItem("Type");
						typeItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JDialog setTypeDialog = new JDialog();
								setTypeDialog.setLayout(null);
								setTypeDialog.setSize(150, 150);
								setTypeDialog.setModal(true);
								String[] blockType = { "1", "2", "3", "4", "미스터리 블록", "장애물 블록" };
								JComboBox<String> blockTypeComboBox = new JComboBox<String>(blockType);
								blockTypeComboBox.setSize(80, 20);
								blockTypeComboBox.setLocation(30, 30);
								blockTypeComboBox.setSelectedIndex(Integer.parseInt(img.getDescription())-1);
								blockTypeComboBox.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										img.setDescription(Integer.toString(blockTypeComboBox.getSelectedIndex()+1));
									}
								});
								setTypeDialog.add(blockTypeComboBox);
								
								JButton applyButton = new JButton("Apply");
								applyButton.setSize(70, 20);
								applyButton.setLocation(35, 80);
								applyButton.setFocusable(false);
								applyButton.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										block.remove(index);
										System.out.println(imgPath);
								        block.add(index, "<Obj x=\"" + item.getX() + "\" y=\"" + item.getY() +
												"\" w=\"" + item.getWidth() + "\" h=\"" + item.getHeight() + 
												"\" type=\"" + img.getDescription() + "\" img=\"" + imgPath + "\"/>");
										setTypeDialog.dispose();
									}
								});
								setTypeDialog.add(applyButton);
								
								setTypeDialog.setLocationRelativeTo(item);
								setTypeDialog.setVisible(true);
								
							}
						});
						itemSettingMenu.add(typeItem);
						
						JMenuItem copyItem = new JMenuItem("Copy");
						copyItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String itemInfo = item.getWidth() + "," + item.getHeight() + "," + img.getDescription() + "," + imgPath;
								Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						        StringSelection stringSelection = new StringSelection(itemInfo);
						        clipboard.setContents(stringSelection, null);
							}
						});
						itemSettingMenu.add(copyItem);
					}
					
					JMenuItem changeImageItem = new JMenuItem("Image");
					changeImageItem.addActionListener(new ActionListener() {
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
							item.setIcon(new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(item.getWidth(), item.getHeight(), Image.SCALE_SMOOTH)));
							item.setName(fileName);
							repaint();
							block.remove(index);
					        block.add(index, "<Obj x=\"" + item.getX() + "\" y=\"" + item.getY() +
									"\" w=\"" + item.getWidth() + "\" h=\"" + item.getHeight() + 
									"\" type=\"" + img.getDescription() + "\" img=\"block/" + fileName + "\"/>");
						}
					});
					itemSettingMenu.add(changeImageItem);
					
					JMenuItem deleteItem = new JMenuItem("Delete");
					deleteItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							remove(item);
							if(item.getName().equals("block")) {
								block.remove(index);
							}
							else if(item.getName().equals("paddle")) {
								paddle.remove(0);
							}
							if(item.getName().equals("shooter")) {
								shooter.remove(0);
							}
							repaint();
							//itemResize.dispose();
						}
					});
					itemSettingMenu.add(deleteItem);
					itemSettingMenu.show(e.getComponent(), e.getX(), e.getY());
					/*itemResize.setLocationRelativeTo(item);
					itemSettingMenu.setLocation(item.getLocationOnScreen().x-100, item.getLocationOnScreen().y+item.getHeight());
					itemSettingMenu.setVisible(true);*/
				}
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			if(item.getName().equals("block") && e.getX() > item.getWidth() - 10 && e.getY() > item.getHeight() - 10) {
				item.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
			}
			else if(item.getName().equals("paddle") && e.getX() > item.getWidth() - 10) {
				item.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
			}
			else {
				item.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getXOnScreen() - item.getWidth() / 2;
		    int y = e.getYOnScreen() - item.getHeight() / 2;

		    toolFrame.setTitle("게임 저작 도구 " + "(" + Integer.toString(x - getLocationOnScreen().x) + "," 
				    + Integer.toString(y - getLocationOnScreen().y) + ")"); 
		    
		    if (item.getCursor().getType() == Cursor.SE_RESIZE_CURSOR) {
		    	int newWidth = Math.max(20, Math.min(100, e.getX())); 
		        int newHeight = Math.max(30, Math.min(70, e.getY()));
		    	item.setIcon(new ImageIcon(img.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)));
		    	item.setSize(newWidth, newHeight);
		    	item.revalidate(); 
		    	block.remove(index);
		        block.add(index, "<Obj x=\"" + item.getX() + "\" y=\"" + item.getY() +
						"\" w=\"" + item.getWidth() + "\" h=\"" + item.getHeight() + 
						"\" type=\"" + img.getDescription() + "\" img=\"" + imgPath + "\"/>");
	        }
		    
		    else if(item.getCursor().getType() == Cursor.W_RESIZE_CURSOR) {
		    	int newWidth = Math.max(20, Math.min(100, e.getX()));
		    	item.setIcon(new ImageIcon(img.getImage().getScaledInstance(newWidth, item.getHeight(), Image.SCALE_SMOOTH)));
		    	item.setSize(newWidth, item.getHeight());
		    	item.revalidate(); 
		    	paddle.remove(index);
		        paddle.add(index, "<Obj x=\"" + item.getX() + "\" y=\"" + item.getY() +
						"\" w=\"" + item.getWidth() + "\" h=\"" + item.getHeight() + 
						"\" img=\"" + imgPath + "\"/>");
		    }
		    
		    else if(!item.getName().equals("shooter") && (x - getLocationOnScreen().x >= 0 && item.getWidth() + x - getLocationOnScreen().x <= toolFrame.getWidth()-200) 
		    		&& (y - getLocationOnScreen().y >= 0 && item.getHeight() + y - getLocationOnScreen().y  <= toolFrame.getHeight()-60)) {
		    	if(item.getName().equals("paddle")) {
			    	paddle.remove(index);
			        paddle.add(index, "<Obj x=\"" + Integer.toString(x - getLocationOnScreen().x) + "\" y=\"" + (getHeight()-100) +
							"\" w=\"" + item.getWidth() + "\" h=\"" + item.getHeight() + 
							"\" img=\"" + imgPath + "\"/>");
			        item.setLocation(x - getLocationOnScreen().x, getHeight()-100);
			    } else if(item.getName().equals("block")){
			    	for(int i=0; i<items.size(); i++) {
			    		if(i == index) {
			    			continue;
			    		}
			    		JLabel itemVector = items.get(i);
			    		if(itemVector.getName().equals("paddle") || itemVector.getName().equals("shooter"))
			    			continue;
			    		if((itemVector.getX() <= x - getLocationOnScreen().x + item.getWidth() && itemVector.getX()+itemVector.getWidth() >= x - getLocationOnScreen().x)
			    				&& (itemVector.getY() <= y - getLocationOnScreen().y + item.getHeight() && itemVector.getY() + itemVector.getHeight() >= y - getLocationOnScreen().y)) 
			    			return;
			    	}
			        block.remove(index);
			        block.add(index, "<Obj x=\"" + Integer.toString(x - getLocationOnScreen().x) + "\" y=\"" + Integer.toString(y - getLocationOnScreen().y) +
							"\" w=\"" + item.getWidth() + "\" h=\"" + item.getHeight() + 
							"\" type=\"" + img.getDescription() + "\" img=\"" + imgPath + "\"/>");
			        if(y - getLocationOnScreen().y + item.getHeight() < getHeight()-100)
			        	item.setLocation(x - getLocationOnScreen().x, y - getLocationOnScreen().y);
			    }
		    }
		    
		    else if(item.getName().equals("shooter") && (x - getLocationOnScreen().x >= 0 && item.getWidth() + x - getLocationOnScreen().x <= toolFrame.getWidth()-200) 
		    		&& (y - getLocationOnScreen().y >= 0 && item.getHeight() + y - getLocationOnScreen().y  <= toolFrame.getHeight()-50)) {
		    	item.setLocation(x - getLocationOnScreen().x, getHeight()-50);
		    	if(item.getName().equals("shooter")) {
			        shooter.remove(index);
			        shooter.add(index, "<Obj x=\"" + Integer.toString(x - getLocationOnScreen().x) + "\" y=\"" + (getHeight()-50) +
							"\" w=\"" + item.getWidth() + "\" h=\"" + item.getHeight() + 
							"\" img=\"" + imgPath + "\"/>");
			    }
		    }
		    
		    
		}
	}
	
	public void setBackImg(String filePath) {
		if(!imgFileName.equals(filePath)) {
			bgImg = new ImageIcon("background/" + filePath);
			imgFileName = "background/" + filePath;
			repaint();
		}
	}
	
	public String getBackImgName() {
		return imgFileName;
	}
	
	public ImageIcon getBackImg() {
		if(bgImg == null) return null;
		return bgImg;
	}
	
	public Vector<String> getVector(String info) {
		if(info.equals("block"))
			return block;
		else if(info.equals("paddle"))
			return paddle;
		else 
			return shooter;
	}
	
	public void getIconInfo(ImageIcon itemIcon, int itemWidth, int itemHeight, String itemImgPath) {
		this.itemIcon = itemIcon;
		this.itemWidth = itemWidth;
		this.itemHeight = itemHeight;
		this.itemImgPath = itemImgPath;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bgImg != null)
			g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
