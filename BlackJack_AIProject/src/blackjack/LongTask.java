package blackjack;

public class LongTask {

	Player player;
	Dealer dealer;
	CardShoe cardShoe;
	int gameCount;
	int size;
	int gamesWon;
	int gamesLost;
	int gamesTied;

	LongTask()
	{
		this.gameCount = 0;
		this.size = 0;
		this.gamesWon = 0;
		this.gamesLost = 0;
		this.gamesTied = 0;
	}

	void go(Player paramPlayer, Dealer paramDealer, CardShoe paramCardShoe, int paramInt)
	{
		this.player = paramPlayer;
		this.dealer = paramDealer;
		this.cardShoe = paramCardShoe;
		this.size = paramInt;
		this.gameCount = 0;

		SwingWorker local1 = new SwingWorker()
		{
			public Object construct()
			{
				return new LongTask.BlackJackGame(LongTask.this);
			}
		};
		local1.start();
	}

	int getLengthOfTask()
	{
		return this.size;
	}

	int getCurrent()
	{
		return this.gameCount;
	}

	int getMoneyTotal()
	{
		return this.player.getMoney();
	}

	int[] getHandTotals()
	{
		return this.player.getHandInfo();
	}

	void stop()
	{
		this.gameCount = this.size;
	}

	boolean done()
	{
		if (this.gameCount >= this.size) {
			return true;
		}
		return false;
	}

	class BlackJackGame
	{
		BlackJackGame(LongTask longTask)
		{
			while (LongTask.this.gameCount <= LongTask.this.size) {
				dealHand();
			}
		}

		public void hitit(boolean paramBoolean)
		{
			LongTask.this.player.addCard(LongTask.this.cardShoe.dealCard());
			if (paramBoolean) {
				playerStands();
			} else {
				playersTurn();
			}
		}

		public void playerStands()
		{
			System.out.println("player stands");
			if (LongTask.this.player.nextHand()) {
				playersTurn();
			} else {
				dealersTurn();
			}
		}

		public void dealersTurn()
		{
			LongTask.this.player.observeValue(LongTask.this.dealer.showFirstCard());
			if (LongTask.this.player.hasHand()) {
				while (LongTask.this.dealer.getValue() < 17) {
					LongTask.this.player.observeValue(LongTask.this.dealer.addCard(LongTask.this.cardShoe.dealCard()));
				}
			}
			System.out.println("dealer finishes");
			gameOver();
		}

		public void playersTurn()
		{
			String str;
			if ((LongTask.this.player.getValue() > 21) || (LongTask.this.player.hasBlackjack()))
			{
				if (LongTask.this.player.nextHand())
				{
					str = LongTask.this.player.advice(LongTask.this.dealer.getValue());
					if (str.equals("Hit"))
					{
						hitit(false);
					}
					else if (str.equals("Double Down"))
					{
						LongTask.this.player.doubleBet();
						hitit(true);
					}
					else if (str.equals("Split"))
					{
						splitHand();
					}
					else
					{
						playerStands();
					}
				}
				else
				{
					dealersTurn();
				}
			}
			else
			{
				str = LongTask.this.player.advice(LongTask.this.dealer.getValue());
				if (str.equals("Hit"))
				{
					hitit(false);
				}
				else if (str.equals("Double Down"))
				{
					LongTask.this.player.doubleBet();
					hitit(true);
				}
				else if (str.equals("Split"))
				{
					splitHand();
				}
				else
				{
					playerStands();
				}
			}
		}

		public void gameOver()
		{
			LongTask.this.player.settleBets(LongTask.this.dealer.getValue());
			System.out.println("money $" + LongTask.this.player.getMoney());
		}

		public void dealHand()
		{
			LongTask.this.gameCount += 1;
			System.out.println("Game: " + LongTask.this.gameCount);

			String str = LongTask.this.player.bettingAdvice();
			if (str.equals("Bet High")) {
				LongTask.this.player.changeBet(90);
			}
			if (LongTask.this.cardShoe.lowOnCards())
			{
				LongTask.this.cardShoe.shuffleNew();
				LongTask.this.player.resetCount();
			}
			LongTask.this.player.newHand(LongTask.this.cardShoe.dealCard(), LongTask.this.cardShoe.dealCard());
			LongTask.this.player.observeValue(LongTask.this.dealer.newHand(LongTask.this.cardShoe.dealCard(), LongTask.this.cardShoe.dealCard()));
			if (LongTask.this.dealer.hasBlackjack())
			{
				LongTask.this.player.observeValue(LongTask.this.dealer.showFirstCard());
				gameOver();
			}
			else
			{
				playersTurn();
			}
		}

		public void splitHand()
		{
			@SuppressWarnings("unused")
			Hand localHand1 = LongTask.this.player.splitHand(LongTask.this.cardShoe.dealCard(), LongTask.this.cardShoe.dealCard());
			@SuppressWarnings("unused")
			Hand localHand2 = LongTask.this.player.getHand();
			playersTurn();
		}
	}
}
