package blackjack;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;

public class SubDialogBox extends JDialog {

	private static final long serialVersionUID = 1L;

	private Timer timer;
	private LongTask task;
	private int testSize = 0;
	private Player player;
	private Dealer dealer;
	private CardShoe cardShoe;
	JLabel noLinesLabel;
	JTextField textField;
	JButton startButton;
	JButton closeButton;
	JProgressBar progressBar;
	JLabel handsWonValue;
	JLabel handsLostValue;
	JLabel handsPushedValue;
	JLabel moneyChangedValue;
	JLabel handsWonPercent;
	JLabel handsLostPercent;
	JLabel handsPushedPercent;
	JLabel moneyChangedPercent;

	public SubDialogBox(Frame paramFrame, MainDisplayBox paramImageDisplayer)
	{
		super(paramFrame, true);
		@SuppressWarnings("unused")
		MainDisplayBox localImageDisplayer = paramImageDisplayer;
		JPanel localJPanel1 = new JPanel();
		//	    this.player = this.player;
		//	    this.dealer = this.dealer;
		//	    this.cardShoe = this.cardShoe;
		this.task = new LongTask();
		localJPanel1.setLayout(new BoxLayout(localJPanel1, 1));

		setTitle("Automated Player");

		JPanel localJPanel2 = new JPanel();
		localJPanel2.setLayout(new BoxLayout(localJPanel2, 0));

		this.timer = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent paramAnonymousActionEvent)
			{
				SubDialogBox.this.progressBar.setValue(SubDialogBox.this.task.getCurrent() - 1);
				SubDialogBox.this.moneyChangedValue.setText(String.valueOf(SubDialogBox.this.task.getMoneyTotal()));
				int[] arrayOfInt = SubDialogBox.this.task.getHandTotals();
				int i = arrayOfInt[3];
				if (i == 0) {
					i++;
				}
				SubDialogBox.this.handsWonValue.setText(String.valueOf(arrayOfInt[0]));
				SubDialogBox.this.handsWonPercent.setText(arrayOfInt[0] * 100 / i + " %");
				SubDialogBox.this.handsLostValue.setText(String.valueOf(arrayOfInt[1]));
				SubDialogBox.this.handsLostPercent.setText(arrayOfInt[1] * 100 / i + " %");
				SubDialogBox.this.handsPushedValue.setText(String.valueOf(arrayOfInt[2]));
				SubDialogBox.this.handsPushedPercent.setText(arrayOfInt[2] * 100 / i + " %");
				SubDialogBox.this.moneyChangedPercent.setText(String.valueOf((SubDialogBox.this.task.getMoneyTotal() - 400) / i * 10));
				if (SubDialogBox.this.task.done())
				{
					SubDialogBox.this.timer.stop();
					SubDialogBox.this.progressBar.setValue(SubDialogBox.this.testSize);
					SubDialogBox.this.startButton.setEnabled(true);
					SubDialogBox.this.closeButton.setEnabled(true);
				}
			}
		});
		this.noLinesLabel = new JLabel("Number of hands to play: ");
		this.textField = new JTextField(5);
		this.noLinesLabel.setLabelFor(this.textField);
		this.startButton = new JButton(" Start ");

		localJPanel2.add(this.noLinesLabel);
		localJPanel2.add(this.textField);
		localJPanel2.add(Box.createRigidArea(new Dimension(10, 0)));
		localJPanel2.add(this.startButton);

		localJPanel1.add(localJPanel2);

		this.progressBar = new JProgressBar();
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);

		this.startButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent paramAnonymousActionEvent)
			{
				SubDialogBox.this.testSize = Integer.parseInt(SubDialogBox.this.textField.getText());
				SubDialogBox.this.progressBar.setMaximum(SubDialogBox.this.testSize);
				SubDialogBox.this.startButton.setEnabled(false);
				SubDialogBox.this.closeButton.setEnabled(false);
				SubDialogBox.this.task.go(SubDialogBox.this.player, SubDialogBox.this.dealer, SubDialogBox.this.cardShoe, SubDialogBox.this.testSize);
				SubDialogBox.this.timer.start();
			}
		});
		localJPanel1.add(this.progressBar);

		JPanel localJPanel3 = new JPanel();
		localJPanel3.setLayout(new GridLayout(0, 3));

		JLabel localJLabel1 = new JLabel("Hands Won:");
		this.handsWonValue = new JLabel("0");
		this.handsWonPercent = new JLabel("0 %");
		localJPanel3.add(localJLabel1);
		localJPanel3.add(this.handsWonValue);
		localJPanel3.add(this.handsWonPercent);

		JLabel localJLabel2 = new JLabel("Hands Lost:");
		this.handsLostValue = new JLabel("0");
		this.handsLostPercent = new JLabel("0 %");
		localJPanel3.add(localJLabel2);
		localJPanel3.add(this.handsLostValue);
		localJPanel3.add(this.handsLostPercent);

		JLabel localJLabel3 = new JLabel("Hands Pushed:      ");
		this.handsPushedValue = new JLabel("0");
		this.handsPushedPercent = new JLabel("0 %");
		localJPanel3.add(localJLabel3);
		localJPanel3.add(this.handsPushedValue);
		localJPanel3.add(this.handsPushedPercent);

		JLabel localJLabel4 = new JLabel("Money:");
		this.moneyChangedValue = new JLabel("0");
		this.moneyChangedPercent = new JLabel("0.00");
		localJPanel3.add(localJLabel4);
		localJPanel3.add(this.moneyChangedValue);
		localJPanel3.add(this.moneyChangedPercent);

		localJPanel3.setBorder(BorderFactory.createTitledBorder("Game Information"));

		localJPanel1.add(localJPanel3);

		this.closeButton = new JButton("Close");

		this.closeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent paramAnonymousActionEvent)
			{
				SubDialogBox.this.setVisible(false);
			}
		});
		localJPanel1.add(this.closeButton);

		getContentPane().add(localJPanel1, "East");
	}
	
	public void initilizePDC(Player paramPlayer, Dealer paramDealer, CardShoe paramCardShoe)
	{
		this.player = paramPlayer;
		this.dealer = paramDealer;
		this.cardShoe = paramCardShoe;
		this.moneyChangedValue.setText(String.valueOf(paramPlayer.getMoney()));
		int[] arrayOfInt = paramPlayer.getHandInfo();
		int i = arrayOfInt[3];
		if (i == 0) {
			i++;
		}
		this.handsWonValue.setText(String.valueOf(arrayOfInt[0]));
		this.handsWonPercent.setText(arrayOfInt[0] * 100 / i + " %");
		this.handsLostValue.setText(String.valueOf(arrayOfInt[1]));
		this.handsLostPercent.setText(arrayOfInt[1] * 100 / i + " %");
		this.handsPushedValue.setText(String.valueOf(arrayOfInt[2]));
		this.handsPushedPercent.setText(arrayOfInt[2] * 100 / i + " %");
	}
}
