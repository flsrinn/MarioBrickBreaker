package gamePlayer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Block extends JLabel {
	private Image img;
	private int num = 0;
	private JLabel number;
	private int level = 0;

	public Block(int x, int y, int w, int h, ImageIcon icon) {
		this.setBounds(x, y, w, h);
		this.setIcon(icon);
		img = icon.getImage();
		num = 0;
	}

	public void setNum() {
		String blockType = getName();
		if(number != null)
			remove(number);
		number = new JLabel(Integer.toString(num));
		number.setForeground(Color.white);
		//1~5
		if(level == 0) {
			switch(blockType) {
			case "1":
				num = (int) (Math.random() * 2) + 1; // 1 ~ 2
				break;
			case "2":
				num = (int) (Math.random() * 2) + 2; // 2 ~ 3
				number.setForeground(Color.cyan);
				break;
			case "3":
				num = (int) (Math.random() * 2) + 3; // 3 ~ 4
				number.setForeground(Color.green);
				break;
			case "4":
				num = (int) (Math.random() * 2) + 4; // 4 ~ 5
				number.setForeground(Color.MAGENTA);
				break;
			case "5":
				num = 5;
				number.setForeground(Color.red);
				break;
			}
		}
		// 3~10
		else if(level == 1) {
			switch(blockType) {
			case "1":
				num = (int) (Math.random() * 2) + 3; // 3 ~ 4
				break;
			case "2":
				num = (int) (Math.random() * 3) + 4; // 4 ~ 6
				number.setForeground(Color.cyan);
				break;
			case "3":
				num = (int) (Math.random() * 2) + 6; // 6 ~ 7
				number.setForeground(Color.green);
				break;
			case "4":
				num = (int) (Math.random() * 3) + 8; // 8 ~ 10
				number.setForeground(Color.MAGENTA);
				break;
			case "5":
				num = 10;
				number.setForeground(Color.red);
				break;
			}
		}
		// 5~15
		else {
			switch(blockType) {
			case "1":
				num = (int) (Math.random() * 4) + 5; // 5 ~ 8
				break;
			case "2":
				num = (int) (Math.random() * 3) + 8; // 8 ~ 10
				number.setForeground(Color.cyan);
				break;
			case "3":
				num = (int) (Math.random() * 3) + 11; // 11 ~ 13
				number.setForeground(Color.green);
				break;
			case "4":
				num = (int) (Math.random() * 3) + 13; // 13 ~ 15
				number.setForeground(Color.MAGENTA);
				break;
			case "5":
				num = 15;
				number.setForeground(Color.red);
				break;
			}
		}
		revalidate();
		repaint();
	}
	
	public void changeLevel(int level) {
		if(this.level == level) {
			return;
		}
		this.level = level;
	}

	public int getNum() {
		return num;
	}

	public void decreaseNum() {
		num -= 1;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		if (number != null)
			remove(number);
		if (num > 0) {
			number.setText(Integer.toString(num));
			number.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
			number.setSize(25, 20);
			number.setLocation(this.getWidth() / 2-5, this.getHeight() / 2-10);
			add(number);
		}
	}

	public boolean isHitted(int ballX, int ballY) {
		if (ballX < getX() || getX() + getWidth() - 1 < ballX) {
			return false;
		}
		if (ballY < getY() || getY() + getHeight() - 1 < ballY) {
			return false;
		}
		return true;
	}
}