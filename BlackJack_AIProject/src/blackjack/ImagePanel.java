package blackjack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Iterator;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	Image[] cardImage;
	Image[] chipImage;
	Image bustedImage;
	Image loserImage;
	Image winnerImage;
	Image pushImage;
	Image fingerImage;
	Point[] coord;
	int[] picOnTable;
	boolean gameOver;
	int betAmount;
	boolean[] doubledBet;
	Hand[] playersHand;
	Hand dealersHand;
	int numberOfHands;
	int currentHand;
	MediaTracker tracker;

	public ImagePanel(Hand paramHand1, Hand paramHand2)
	{
		this.gameOver = false;
		this.doubledBet = new boolean[4];
		for (int i = 0; i < 4; i++) {
			this.doubledBet[i] = false;
		}
		this.playersHand = new Hand[4];
		this.playersHand[0] = paramHand1;
		this.dealersHand = paramHand2;
		this.betAmount = 1;
		this.numberOfHands = 1;
		this.currentHand = 0;
		init();
	}

	public void init()
	{
		this.cardImage = new Image[53];
		this.chipImage = new Image[5];
		this.coord = new Point[12];
		this.picOnTable = new int[12];
		this.tracker = new MediaTracker(this);
		for (int i = 0; i < 12; i++) {
			this.picOnTable[i] = -1;
		}
		for (int j = 0; j < 13; j++)
		{
			this.cardImage[j] = Toolkit.getDefaultToolkit().getImage("images/S" + (j + 1) + ".png");
			this.cardImage[(j + 13)] = Toolkit.getDefaultToolkit().getImage("images/C" + (j + 1) + ".png");
			this.cardImage[(j + 26)] = Toolkit.getDefaultToolkit().getImage("images/D" + (j + 1) + ".png");
			this.cardImage[(j + 39)] = Toolkit.getDefaultToolkit().getImage("images/H" + (j + 1) + ".png");
			this.tracker.addImage(this.cardImage[j], 0);
			this.tracker.addImage(this.cardImage[(j + 13)], 0);
			this.tracker.addImage(this.cardImage[(j + 26)], 0);
			this.tracker.addImage(this.cardImage[(j + 39)], 0);
		}
		for (int k = 0; k < 5; k++)
		{
			this.chipImage[k] = Toolkit.getDefaultToolkit().getImage("images/" + (k + 1) + "Chip.gif");
			this.tracker.addImage(this.chipImage[k], 0);
		}
		this.cardImage[52] = Toolkit.getDefaultToolkit().getImage("images/cardback.jpeg");
		this.tracker.addImage(this.cardImage[52], 0);
		this.bustedImage = Toolkit.getDefaultToolkit().getImage("images/Busted.gif");
		this.tracker.addImage(this.bustedImage, 0);
		this.loserImage = Toolkit.getDefaultToolkit().getImage("images/Loser.gif");
		this.tracker.addImage(this.loserImage, 0);
		this.winnerImage = Toolkit.getDefaultToolkit().getImage("images/Winner.gif");
		this.tracker.addImage(this.winnerImage, 0);
		this.pushImage = Toolkit.getDefaultToolkit().getImage("images/Push.gif");
		this.tracker.addImage(this.pushImage, 0);
		this.fingerImage = Toolkit.getDefaultToolkit().getImage("images/Finger.gif");
		this.tracker.addImage(this.fingerImage, 0);

		this.coord[0] = new Point(150, 10);
		this.coord[1] = new Point(150, 300);
		try
		{
			this.tracker.waitForAll();
		}
		catch (InterruptedException localInterruptedException) {}
	}

	public void newGame()
	{
		this.gameOver = false;
		this.numberOfHands = 1;
		this.currentHand = 0;
	}

	public void setBet(int paramInt)
	{
		this.betAmount = paramInt;
		repaint();
	}

	public void doubleBet()
	{
		this.doubledBet[this.currentHand] = true;
	}

	public void nextHand()
	{
		this.currentHand += 1;
	}

	public void paintComponent(Graphics paramGraphics)
	{
		super.paintComponent(paramGraphics);

		paramGraphics.setColor(Color.GRAY.darker().darker());
		paramGraphics.fillRect(0, 0, getWidth(), getHeight());

		int i = 0;
		Iterator<CardValueType> localIterator = this.dealersHand.cardValueTypes.iterator();
		while (localIterator.hasNext())
		{
			CardValueType localCard1 = (CardValueType)localIterator.next();
			paramGraphics.drawImage(this.cardImage[localCard1.getPlace()], this.coord[0].x + i, this.coord[0].y, this);
			i += 75;
		}
		for (int j = 0; j < this.numberOfHands; j++)
		{
			i = 0;
			localIterator = this.playersHand[j].cardValueTypes.iterator();
			while (localIterator.hasNext())
			{
				CardValueType localCard2 = (CardValueType)localIterator.next();
				paramGraphics.drawImage(this.cardImage[localCard2.getPlace()], this.coord[1].x + 560 * j / this.numberOfHands + i, this.coord[1].y, this);

				i += 75 / this.numberOfHands;
			}
			if ((!this.gameOver) || (j == 0)) {
				paramGraphics.drawImage(this.chipImage[(this.betAmount - 1)], this.coord[1].x + 560 * j / this.numberOfHands + 10, this.coord[1].y + 90, this);
			}
			if (this.doubledBet[j]) {
				paramGraphics.drawImage(this.chipImage[(this.betAmount - 1)], this.coord[1].x + 560 * j / this.numberOfHands + 35, this.coord[1].y + 100, this);
			}
			if (this.playersHand[j].busted()) {
				paramGraphics.drawImage(this.bustedImage, this.coord[1].x + 560 * j / this.numberOfHands + 5, this.coord[1].y + 45, this);
			} else if (this.dealersHand.busted()) {
				paramGraphics.drawImage(this.winnerImage, this.coord[1].x + 560 * j / this.numberOfHands + 5, this.coord[1].y + 45, this);
			} else if ((this.dealersHand.getValue() > this.playersHand[j].getValue()) && (this.gameOver)) {
				paramGraphics.drawImage(this.loserImage, this.coord[1].x + 560 * j / this.numberOfHands + 5, this.coord[1].y + 45, this);
			} else if ((this.dealersHand.getValue() < this.playersHand[j].getValue()) && (this.gameOver)) {
				paramGraphics.drawImage(this.winnerImage, this.coord[1].x + 560 * j / this.numberOfHands + 5, this.coord[1].y + 45, this);
			} else if (this.gameOver) {
				paramGraphics.drawImage(this.pushImage, this.coord[1].x + 560 * j / this.numberOfHands + 5, this.coord[1].y + 45, this);
			}
		}
		if ((this.numberOfHands > 1) && (!this.gameOver)) {
			paramGraphics.drawImage(this.fingerImage, this.coord[1].x + 560 * this.currentHand / this.numberOfHands + 30, this.coord[1].y - 45, this);
		}
	}

	public void addPicture(Hand paramHand1, Hand paramHand2)
	{
		this.playersHand[this.currentHand] = paramHand1;
		this.dealersHand = paramHand2;
		repaint();
	}

	public void splitHand(Hand paramHand1, Hand paramHand2)
	{
		this.playersHand[this.currentHand] = paramHand1;
		this.playersHand[this.numberOfHands] = paramHand2;
		this.numberOfHands += 1;
		repaint();
	}

	public void gameOver()
	{
		this.gameOver = true;
		this.doubledBet[0] = false;
		this.doubledBet[1] = false;
		this.doubledBet[2] = false;
		this.doubledBet[3] = false;
		this.betAmount = 1;
	}
}
