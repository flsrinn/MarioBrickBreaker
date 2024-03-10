package gamePlayer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ItemPanel extends JPanel {
	private Node itemPanelNode, itemNode;
	private ImageIcon bgImg;
	private JLabel ghost;
	private Vector<JLabel> ghostsVector = new Vector<JLabel>();
	private Vector<JLabel> flowersVector = new Vector<JLabel>();
	private Vector<JLabel> starsVector = new Vector<JLabel>();
	private Image flowerImg = new ImageIcon("item/flower.png").getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
	private Image ghostImg = new ImageIcon("item/ghost.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	private Image starImg = new ImageIcon("item/star.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	private JLabel item;
	
	public ItemPanel(Node itemPanelNode) {
		setLayout(null);
		this.itemPanelNode = itemPanelNode;
		Node bgNode = XMLReader.getNode(itemPanelNode, XMLReader.E_BACKGROUND);
		Node bgimgNode = XMLReader.getNode(bgNode, XMLReader.E_BGIMG);
		bgImg = new ImageIcon(bgimgNode.getTextContent());
	}

	public void addGhost() {
		ghost = new JLabel(new ImageIcon(ghostImg));
		ghostsVector.add(ghost);
		ghost.setSize(40, 40);
		ghost.setLocation(0, 10);
		add(ghost);
		repaint();
		GhostThread ghostThread = new GhostThread(ghost, ghostImg);
		ghostThread.start();
	}

	public void addFlower() {
		item = new JLabel(new ImageIcon(flowerImg));
		flowersVector.add(item);
		item.setSize(35, 35);
		if(flowersVector.size() == 1) {
			item.setLocation(0, 125);
		}
		else {
			item.setLocation(flowersVector.get(flowersVector.size()-2).getX() + 45, 125);
		}
		add(item);
		repaint();
	}

	public void addStar() {
		item = new JLabel(new ImageIcon(starImg));
		starsVector.add(item);
		item.setSize(30, 30);
		int x = (int) (Math.random() * 161);
		int y = (int) (Math.random() * 20) + 60;
		item.setLocation(x, y);
		add(item);
		repaint();
	}

	public int getGhost() {
		return ghostsVector.size();
	}
	
	public int getFlower() {
		return flowersVector.size();
	}
	
	public int getStar() {
		return starsVector.size();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(new Color(14, 13, 89));
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}

	class GhostThread extends Thread {
		private String direction = "RIGHTUP";
		private JLabel ghost;
		private Image ghostImg;

		public GhostThread(JLabel ghost, Image ghostImg) {
			this.ghost = ghost;
			this.ghostImg = ghostImg;
		}
		
		private BufferedImage toBufferedImage(Image img) {
			if(img instanceof BufferedImage)
				return (BufferedImage) img;
			BufferedImage buffered = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics2D bgr = buffered.createGraphics();
			bgr.drawImage(img, 0, 0, null);
			bgr.dispose();
			return buffered;
		}
		
		private void flipImage() {
			BufferedImage bufferedImage = toBufferedImage(ghostImg);
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-bufferedImage.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			bufferedImage = op.filter(bufferedImage, null);
			ImageIcon flippedIcon = new ImageIcon(bufferedImage);
			ghostImg = flippedIcon.getImage();
			ghost.setIcon(flippedIcon);
			repaint();
		}
		
		@Override
		public void run() {
			while (true) {
				switch (direction) {
				case "RIGHTUP":
					if (ghost.getY() <= 0) {
						direction = "RIGHTDOWN";
					}
					if (ghost.getX() == 160) {
						direction = "LEFTUP";
						ghost.setLocation(160, 10);
						flipImage();
					}
					ghost.setLocation(ghost.getX() + 1, ghost.getY() - 1);
					break;
				case "LEFTUP":
					if (ghost.getX() == 0) {
						direction = "RIGHTUP";
						ghost.setLocation(0, 10);
						flipImage();
					}
					if (ghost.getY() <= 0) {
						direction = "LEFTDOWN";
					}
					ghost.setLocation(ghost.getX() - 1, ghost.getY() - 1);
					break;
				case "LEFTDOWN":
					if (ghost.getX() == 0) {
						direction = "RIGHTDOWN";
						ghost.setLocation(0, 10);
						flipImage();
					}
					if (ghost.getY() >= 20) {
						direction = "LEFTUP";
					}
					ghost.setLocation(ghost.getX() - 1, ghost.getY() + 1);
					break;
				case "RIGHTDOWN":
					if (ghost.getY() >= 20) {
						direction = "RIGHTUP";
					}
					if (ghost.getX() == 160) {
						direction = "LEFTDOWN";
						ghost.setLocation(160, 10);
						flipImage();
					}
					ghost.setLocation(ghost.getX() + 1, ghost.getY() + 1);
					break;
				}
				try {
					sleep(17);
				} catch (InterruptedException e) {
				}
			}

		}
	}
}
