package blackjack;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainDisplayBox extends JApplet {

	private static final long serialVersionUID = 1L;

	SubDialogBox subDialogBox;
	JFrame f;
	Player player;
	Dealer dealer;
	CardShoe cardShoe;
	ImagePanel imagePanel;
	Random randomGenerator;
	JButton raiseButton;
	JButton lowerButton;
	JButton dealButton;
	JButton hitButton;
	JButton standButton;
	JButton doubleDownButton;
	JButton splitButton;
	JButton exitButton;
	JLabel adviceLabel;
	JLabel moneyLabel;
	JPanel buttons;
	JMenuBar menuBar;
	JMenu menu;
	JMenu submenu;
	JMenuItem menuItem;
	JCheckBoxMenuItem standardMenuItem;
	JCheckBoxMenuItem adviceMenuItem;
	JMenuItem autoMenuItem;
	int gameCount;
	boolean adviceMode;
	boolean autoMode;
	boolean gameOverFlag;

	public void start()
	{
		init();
		buildWindow();
	}

	public void init()
	{
		this.cardShoe = new CardShoe(4);
		this.player = new Player(4);
		this.dealer = new Dealer();
		this.adviceMode = false;
		this.autoMode = false;
		this.gameOverFlag = true;
	}

	public void buildWindow()
	{
		this.imagePanel = new ImagePanel(this.player.getHand(), this.dealer.getHand());
		this.f = new JFrame("A.I. Blackjack with MDP");
		this.buttons = new JPanel();
		this.buttons.setLayout(new BoxLayout(this.buttons, 0));

		this.f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent paramAnonymousWindowEvent)
			{
				System.exit(0);
			}
		});
		this.menuBar = new JMenuBar();
		this.f.setJMenuBar(this.menuBar);

		this.menu = new JMenu("File");
		this.menu.setMnemonic(70);
		this.menuBar.add(this.menu);

		this.menuItem = new JMenuItem("Quit", 81);
		this.menu.add(this.menuItem);

		this.menu = new JMenu("Mode");
		this.menu.setMnemonic(77);
		this.menuBar.add(this.menu);

		this.standardMenuItem = new JCheckBoxMenuItem("Standard");
		this.standardMenuItem.setMnemonic(83);
		this.menu.add(this.standardMenuItem);
		this.standardMenuItem.setState(true);
		this.adviceMenuItem = new JCheckBoxMenuItem("Advice");
		this.adviceMenuItem.setMnemonic(86);
		this.menu.add(this.adviceMenuItem);
		this.autoMenuItem = new JMenuItem("Automatic");
		this.autoMenuItem.setMnemonic(65);
		this.menu.add(this.autoMenuItem);

		this.standardMenuItem.addActionListener(new MainDisplayBox.StandardMenuListener(this));
		this.adviceMenuItem.addActionListener(new MainDisplayBox.AdviceMenuListener(this));
		this.autoMenuItem.addActionListener(new MainDisplayBox.AutoMenuListener(this));

		this.raiseButton = new JButton("Raise Bet");
		this.lowerButton = new JButton("Lower Bet");
		this.dealButton = new JButton("Deal");
		this.hitButton = new JButton("Hit");
		this.standButton = new JButton("Stand");
		this.doubleDownButton = new JButton("Double Down");
		this.splitButton = new JButton("Split");
		this.exitButton = new JButton("Exit");
		this.adviceLabel = new JLabel(" ");
		this.moneyLabel = new JLabel("Money: $400");

		this.hitButton.setEnabled(false);
		this.standButton.setEnabled(false);
		this.splitButton.setEnabled(false);
		this.doubleDownButton.setEnabled(false);

		this.raiseButton.addActionListener(new MainDisplayBox.RaiseButtonListener(this));
		this.lowerButton.addActionListener(new MainDisplayBox.LowerButtonListener(this));
		this.dealButton.addActionListener(new MainDisplayBox.DealButtonListener(this));
		this.hitButton.addActionListener(new MainDisplayBox.HitButtonListener(this));
		this.standButton.addActionListener(new MainDisplayBox.StandButtonListener(this));
		this.doubleDownButton.addActionListener(new MainDisplayBox.DoubleDownButtonListener(this));
		this.splitButton.addActionListener(new MainDisplayBox.SplitButtonListener(this));
		this.exitButton.addActionListener(new MainDisplayBox.ExitButtonListener());

		this.buttons.add(this.raiseButton);
		this.buttons.add(this.lowerButton);
		this.buttons.add(this.dealButton);
		this.buttons.add(this.hitButton);
		this.buttons.add(this.standButton);
		this.buttons.add(this.doubleDownButton);
		this.buttons.add(this.splitButton);
		this.buttons.add(Box.createRigidArea(new Dimension(20, 0)));
		this.buttons.add(this.exitButton);
		this.buttons.add(Box.createRigidArea(new Dimension(20, 0)));
		this.buttons.add(this.moneyLabel);
		this.buttons.add(Box.createRigidArea(new Dimension(20, 0)));
		this.buttons.add(this.adviceLabel);

		this.f.getContentPane().add(this.imagePanel, "Center");
		this.f.getContentPane().add(this.buttons, "South");
		this.f.setSize(new Dimension(800, 600));
		this.f.setVisible(true);

		this.subDialogBox = new SubDialogBox(this.f, this);
		this.subDialogBox.pack();
	}

	public class StandardMenuListener
	implements ActionListener
	{
		MainDisplayBox display;

		StandardMenuListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			this.display.setMode(1);
		}
	}

	public class AdviceMenuListener
	implements ActionListener
	{
		MainDisplayBox display;

		AdviceMenuListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			this.display.setMode(2);
		}
	}

	public class AutoMenuListener
	implements ActionListener
	{
		MainDisplayBox display;

		AutoMenuListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			MainDisplayBox.this.subDialogBox.setLocationRelativeTo(MainDisplayBox.this.f);
			MainDisplayBox.this.subDialogBox.initilizePDC(MainDisplayBox.this.player, MainDisplayBox.this.dealer, MainDisplayBox.this.cardShoe);
			MainDisplayBox.this.subDialogBox.setVisible(true);
		}
	}

	public class RaiseButtonListener
	implements ActionListener
	{
		MainDisplayBox display;

		RaiseButtonListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			this.display.changeBet(10);
		}
	}

	public class LowerButtonListener
	implements ActionListener
	{
		MainDisplayBox display;

		LowerButtonListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			this.display.changeBet(-10);
		}
	}

	public class DealButtonListener
	implements ActionListener
	{
		MainDisplayBox display;

		DealButtonListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			this.display.dealHand();
		}
	}
	
	public class HitButtonListener
	implements ActionListener
	{
		MainDisplayBox display;

		HitButtonListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			this.display.hitit(false);
		}
	}

	public class StandButtonListener
	implements ActionListener
	{
		MainDisplayBox display;

		StandButtonListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			this.display.playerStands();
		}
	}

	public class DoubleDownButtonListener
	implements ActionListener
	{
		MainDisplayBox display;

		DoubleDownButtonListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			this.display.doubleBet();
			this.display.hitit(true);
		}
	}

	public class SplitButtonListener
	implements ActionListener
	{
		MainDisplayBox display;

		SplitButtonListener(MainDisplayBox paramImageDisplayer)
		{
			this.display = paramImageDisplayer;
		}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			this.display.splitHand();
		}
	}

	public class ExitButtonListener
	implements ActionListener
	{
		public ExitButtonListener() {}

		public void actionPerformed(ActionEvent paramActionEvent)
		{
			System.exit(0);
		}
	}

	public void setMode(int paramInt)
	{
		this.standardMenuItem.setState(paramInt == 1);
		this.adviceMenuItem.setState(paramInt == 2);
		this.adviceMode = this.adviceMenuItem.getState();
		if (this.adviceMode) {
			try
			{
				if (this.gameOverFlag) {
					this.adviceLabel.setText(this.player.bettingAdvice());
				} else {
					this.adviceLabel.setText(this.player.advice(this.dealer.getValue()));
				}
			}
			catch (Exception localException)
			{
				System.out.println("Error in advice");
			}
		} else {
			this.adviceLabel.setText("");
		}
	}

	public void changeBet(int paramInt)
	{
		int i = this.player.getBet();
		if (i + paramInt > 50) {
			return;
		}
		if (i + paramInt < 10) {
			return;
		}
		this.imagePanel.setBet(this.player.changeBet(paramInt) / 10);
	}

	public void doubleBet()
	{
		this.player.doubleBet();
		this.imagePanel.doubleBet();
	}

	public void dealHand()
	{
		this.gameOverFlag = false;
		if (this.cardShoe.lowOnCards())
		{
			this.cardShoe.shuffleNew();
			this.player.resetCount();
		}
		this.player.newHand(this.cardShoe.dealCard(), this.cardShoe.dealCard());
		this.player.observeValue(this.dealer.newHand(this.cardShoe.dealCard(), this.cardShoe.dealCard()));
		if (this.dealer.hasBlackjack())
		{
			this.player.observeValue(this.dealer.showFirstCard());
			gameOver();
			return;
		}
		this.imagePanel.newGame();
		this.moneyLabel.setText("money: $" + this.player.getMoney());
		this.imagePanel.addPicture(this.player.getHand(), this.dealer.getHand());
		this.dealButton.setEnabled(false);
		this.raiseButton.setEnabled(false);
		this.lowerButton.setEnabled(false);
		this.autoMenuItem.setEnabled(false);
		this.hitButton.setEnabled(true);
		this.standButton.setEnabled(true);
		this.doubleDownButton.setEnabled(true);
		this.splitButton.setEnabled(this.player.canSplit());

		playersTurn();
	}

	public void hitit(boolean paramBoolean)
	{
		if (this.adviceMode) {
			this.adviceLabel.setText("");
		}
		this.player.addCard(this.cardShoe.dealCard());

		this.imagePanel.addPicture(this.player.getHand(), this.dealer.getHand());
		this.doubleDownButton.setEnabled(false);
		this.splitButton.setEnabled(false);
		if (paramBoolean) {
			playerStands();
		} else {
			playersTurn();
		}
	}

	public void splitHand()
	{
		Hand localHand1 = this.player.splitHand(this.cardShoe.dealCard(), this.cardShoe.dealCard());
		Hand localHand2 = this.player.getHand();

		this.imagePanel.splitHand(localHand2, localHand1);
		this.splitButton.setEnabled(this.player.canSplit());

		playersTurn();
	}

	public void playerStands()
	{
		if (this.player.nextHand())
		{
			this.imagePanel.nextHand();
			this.imagePanel.addPicture(this.player.getHand(), this.dealer.getHand());
			this.doubleDownButton.setEnabled(true);
			this.splitButton.setEnabled(this.player.canSplit());
			playersTurn();
		}
		else
		{
			dealersTurn();
		}
	}

	public void dealersTurn()
	{
		this.adviceLabel.setText("");
		this.standButton.setEnabled(false);
		this.hitButton.setEnabled(false);
		this.doubleDownButton.setEnabled(false);
		this.splitButton.setEnabled(false);

		this.player.observeValue(this.dealer.showFirstCard());
		if (this.player.hasHand()) {
			while (this.dealer.getValue() < 17)
			{
				this.player.observeValue(this.dealer.addCard(this.cardShoe.dealCard()));
				this.imagePanel.addPicture(this.player.getHand(), this.dealer.getHand());
			}
		}
		gameOver();
	}

	public void playersTurn()
	{
		if ((this.player.getValue() > 21) || (this.player.hasBlackjack()))
		{
			if (this.player.nextHand())
			{
				this.imagePanel.nextHand();
				this.doubleDownButton.setEnabled(true);
				this.splitButton.setEnabled(this.player.canSplit());
				try
				{
					if (this.adviceMode) {
						this.adviceLabel.setText(this.player.advice(this.dealer.getValue()));
					}
				}
				catch (Exception localException1)
				{
					System.out.println("Error in advice");
				}
			}
			else
			{
				dealersTurn();
			}
		}
		else {
			try
			{
				if (this.adviceMode) {
					this.adviceLabel.setText(this.player.advice(this.dealer.getValue()));
				}
			}
			catch (Exception localException2)
			{
				System.out.println("Error in advice");
			}
		}
	}

	public void gameOver()
	{
		this.gameOverFlag = true;
		this.player.settleBets(this.dealer.getValue());

		this.imagePanel.addPicture(this.player.getHand(), this.dealer.getHand());
		this.imagePanel.gameOver();
		this.moneyLabel.setText("money: $" + this.player.getMoney());
		try
		{
			if (this.adviceMode) {
				this.adviceLabel.setText(this.player.bettingAdvice());
			}
		}
		catch (Exception localException)
		{
			System.out.println("Error in advice");
		}
		this.dealButton.setEnabled(true);
		this.raiseButton.setEnabled(true);
		this.lowerButton.setEnabled(true);
		this.hitButton.setEnabled(false);
		this.standButton.setEnabled(false);
		this.autoMenuItem.setEnabled(true);
	}
}
