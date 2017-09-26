package blackjack;

import java.util.Iterator;

public class Player {

	Hand[] hand;
	CardCount cardCount;
	int money;
	int currentHand;
	int ante;
	int numberOfHands;
	boolean hasAHand;
	int[] handInfo;
	double[][] weightNN;
	double[] weightMin;
	double[] weightRange;

	public Player(int paramInt)
	{
		this.hand = new Hand[4];
		this.hand[0] = new Hand();
		this.cardCount = new CardCount(paramInt);
		this.money = 400;
		this.currentHand = 0;
		this.ante = 10;
		this.numberOfHands = 1;
		this.hasAHand = false;
		this.handInfo = new int[4];
		this.handInfo[0] = 0;
		this.handInfo[1] = 0;
		this.handInfo[2] = 0;
		this.handInfo[3] = 0;
	}

	public void newHand(CardValueType paramCard1, CardValueType paramCard2)
	{
		observeValue(paramCard1.getValue());
		observeValue(paramCard2.getValue());
		this.hand[0] = new Hand(paramCard1, paramCard2);
		this.hand[0].setBet(this.ante);
		this.currentHand = 0;
		this.numberOfHands = 1;
		this.hasAHand = false;
	}

	public Hand splitHand(CardValueType paramCard1, CardValueType paramCard2)
	{
		observeValue(paramCard1.getValue());
		observeValue(paramCard2.getValue());

		Iterator<CardValueType> localIterator = this.hand[this.currentHand].cardValueTypes.iterator();
		CardValueType localCard1 = (CardValueType)localIterator.next();
		CardValueType localCard2 = (CardValueType)localIterator.next();

		this.hand[this.currentHand] = new Hand(localCard1, paramCard1);
		this.hand[this.numberOfHands] = new Hand(localCard2, paramCard2);
		this.hand[this.currentHand].changeBet(this.ante);
		this.hand[this.numberOfHands].changeBet(this.ante);
		this.numberOfHands += 1;

		return this.hand[(this.numberOfHands - 1)];
	}

	public boolean nextHand()
	{
		if ((this.hand[this.currentHand].getValue() <= 21) && (!this.hand[this.currentHand].isBlackjack())) {
			this.hasAHand = true;
		}
		if (this.currentHand + 1 == this.numberOfHands) {
			return false;
		}
		this.currentHand += 1;

		return true;
	}

	public Hand getHand()
	{
		return this.hand[this.currentHand];
	}

	public boolean hasHand()
	{
		return this.hasAHand;
	}

	public int getValue()
	{
		return this.hand[this.currentHand].getValue();
	}

	public boolean hasBlackjack()
	{
		return this.hand[this.currentHand].isBlackjack();
	}

	public int isSoft()
	{
		return this.hand[this.currentHand].isSoft();
	}

	public boolean canSplit()
	{
		return (this.hand[this.currentHand].isSplitable()) && (this.numberOfHands < 4);
	}

	public void addCard(CardValueType paramCard)
	{
		observeValue(paramCard.getValue());
		this.hand[this.currentHand].addCard(paramCard);
	}

	public void observeValue(int paramInt)
	{
		this.cardCount.observeValue(paramInt);
	}

	public void resetCount()
	{
		this.cardCount.resetCount();
	}

	public String advice(int paramInt)
	{
//		System.out.println(this.cardCount.getCardsLeft());
		return playerDecision(this.hand[this.currentHand].getValue(), this.hand[this.currentHand].isSoft(), paramInt, this.hand[this.currentHand].isTwoCards(), (this.hand[this.currentHand].isSplitable()) && (this.numberOfHands != 4), this.cardCount);
	}

	public String playerDecision(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, CardCount paramCardCount)
	{
		int i = 0;
		if (paramInt3 == 11) {
			i++;
		}
		double d1 = -1.0D;
		if (paramInt1 > 11) {
			d1 = dealerRecursive(paramInt1, paramInt2, paramInt3, i, true, paramCardCount);
		}
		double d2 = 0.0D;
		for (int j = 2; j < 11; j++)
		{
			paramCardCount.observeValue(j);
			d2 += paramCardCount.chance(j) * playerRecursive(paramInt1 + j, paramInt2, paramInt3, i, paramCardCount);

			paramCardCount.unobserveValue(j);
		}
		paramCardCount.observeValue(11);
		d2 += paramCardCount.chance(11) * playerRecursive(paramInt1 + 11, paramInt2 + 1, paramInt3, i, paramCardCount);

		paramCardCount.unobserveValue(11);

		double d3 = -1.0D;
		if (paramBoolean1)
		{
			d3 = 0.0D;
			for (int k = 2; k < 11; k++)
			{
				paramCardCount.observeValue(k);
				d3 += paramCardCount.chance(k) * dealerRecursive(paramInt1 + k, paramInt2, paramInt3, i, true, paramCardCount);

				paramCardCount.unobserveValue(k);
			}
			paramCardCount.observeValue(11);
			d2 += paramCardCount.chance(11) * dealerRecursive(paramInt1 + 11, paramInt2 + 1, paramInt3, i, true, paramCardCount);

			paramCardCount.unobserveValue(11);
			d3 *= 2.0D;
		}
		double d4 = -1.0D;
		if (paramBoolean2)
		{
			int m = paramInt1 / 2;
			if (paramInt2 == 1) {
				m = 11;
			}
			d4 = projectedValSplit(m, paramInt3);
			d4 *= 2.0D;
		}
		if ((d1 > d2) && (d1 > d3) && (d1 > d4)) {
			return new String("Stand");
		}
		if ((d2 > d1) && (d2 > d3) && (d2 > d4)) {
			return new String("Hit");
		}
		if ((d4 > d2) && (d4 > d3) && (d4 > d1)) {
			return new String("Split");
		}
		return new String("Double Down");
	}

	public double playerRecursive(int paramInt1, int paramInt2, int paramInt3, int paramInt4, CardCount paramCardCount)
	{
		if (paramInt1 > 21)
		{
			if (paramInt2 == 0) {
				return -1.0D;
			}
			paramInt1 -= 10;
			paramInt2--;
		}
		double d1 = -1.0D;
		if (paramInt1 > 11) {
			d1 = dealerRecursive(paramInt1, paramInt2, paramInt3, paramInt4, true, paramCardCount);
		}
		double d2 = 0.0D;
		for (int i = 2; i < 11; i++) {
			if (paramCardCount.chance(i) > 0.0D)
			{
				paramCardCount.observeValue(i);
				d2 += paramCardCount.chance(i) * playerRecursive(paramInt1 + i, paramInt2, paramInt3, paramInt4, paramCardCount);

				paramCardCount.unobserveValue(i);
			}
		}
		if (paramCardCount.chance(11) > 0.0D)
		{
			paramCardCount.observeValue(11);
			d2 += paramCardCount.chance(11) * playerRecursive(paramInt1 + 11, paramInt2 + 1, paramInt3, paramInt4, paramCardCount);

			paramCardCount.unobserveValue(11);
		}
		if (d1 > d2) {
			return d1;
		}
		return d2;
	}

	public double dealerRecursive(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, CardCount paramCardCount)
	{
		if (paramInt1 > 21)
		{
			if (paramInt2 == 0) {
				return -1.0D;
			}
			paramInt1 -= 10;
			paramInt2--;
		}
		if (paramInt3 > 21)
		{
			if (paramInt4 == 0) {
				return 1.0D;
			}
			paramInt3 -= 10;
			paramInt4--;
		}
		if (paramInt3 >= 17)
		{
			if (paramInt3 > paramInt1) {
				return -1.0D;
			}
			if (paramInt3 < paramInt1) {
				return 1.0D;
			}
			if (paramInt3 == paramInt1) {
				return 0.0D;
			}
		}
		double d = 0.0D;
		int i;
		if ((paramBoolean) && (paramInt3 == 10) && (paramInt3 == 11))
		{
			for (i = 2; i < 11; i++) {
				if (paramCardCount.chance(i) > 0.0D)
				{
					paramCardCount.observeValue(i);
					d += paramCardCount.chanceDealerDown(i, paramInt3) * dealerRecursive(paramInt1, paramInt2, paramInt3 + i, paramInt4, false, paramCardCount);

					paramCardCount.unobserveValue(i);
				}
			}
			if (paramCardCount.chance(11) > 0.0D)
			{
				paramCardCount.observeValue(11);
				d += paramCardCount.chanceDealerDown(11, paramInt3) * dealerRecursive(paramInt1, paramInt2, paramInt3 + 11, paramInt4 + 1, false, paramCardCount);

				paramCardCount.unobserveValue(11);
			}
		}
		else
		{
			for (i = 2; i < 11; i++) {
				if (paramCardCount.chance(i) > 0.0D)
				{
					paramCardCount.observeValue(i);
					d += paramCardCount.chance(i) * dealerRecursive(paramInt1, paramInt2, paramInt3 + i, paramInt4, false, paramCardCount);

					paramCardCount.unobserveValue(i);
				}
			}
			if (paramCardCount.chance(11) > 0.0D)
			{
				paramCardCount.observeValue(11);
				d += paramCardCount.chance(11) * dealerRecursive(paramInt1, paramInt2, paramInt3 + 11, paramInt4 + 1, false, paramCardCount);

				paramCardCount.unobserveValue(11);
			}
		}
		return d;
	}

	public int getMoney()
	{
		return this.money;
	}

	public int changeBet(int paramInt)
	{
		this.ante += paramInt;
		return this.ante;
	}

	public int doubleBet()
	{
		return this.hand[this.currentHand].changeBet(this.ante);
	}

	public int getBet()
	{
		return this.ante;
	}

	public void settleBets(int paramInt)
	{
		for (int i = 0; i < this.numberOfHands; i++) {
			if (this.hand[i].isBlackjack())
			{
				this.money = ((int)(this.money + this.hand[i].getBet() * 1.5D));
				this.handInfo[0] += 1;
			}
			else if (this.hand[i].busted())
			{
				this.money -= this.hand[i].getBet();
				this.handInfo[1] += 1;
			}
			else if (paramInt > 21)
			{
				this.money += this.hand[i].getBet();
				this.handInfo[0] += 1;
			}
			else if (paramInt > this.hand[i].getValue())
			{
				this.money -= this.hand[i].getBet();
				this.handInfo[1] += 1;
			}
			else if (paramInt < this.hand[i].getValue())
			{
				this.money += this.hand[i].getBet();
				this.handInfo[0] += 1;
			}
			else
			{
				this.handInfo[2] += 1;
			}
		}
		this.handInfo[3] += this.numberOfHands;
		this.ante = 10;
	}

	public int[] getHandInfo()
	{
		return this.handInfo;
	}

	private double squish(double paramDouble)
	{
		return 1.0D / (1.0D + Math.exp(-paramDouble));
	}

	public String bettingAdvice()
	{
		this.weightNN = new double[3][10];
		this.weightMin = new double[10];
		this.weightRange = new double[10];

		this.weightNN[0][0] = -0.12920576D;
		this.weightNN[0][1] = -0.06412242D;
		this.weightNN[0][2] = -0.17444408D;
		this.weightNN[0][3] = -0.01934681D;
		this.weightNN[0][4] = 0.18213964D;
		this.weightNN[0][5] = -0.1992395D;
		this.weightNN[0][6] = -0.23679555D;
		this.weightNN[0][7] = 0.12794927D;
		this.weightNN[0][8] = -0.03082812D;
		this.weightNN[0][9] = -0.00958003D;
		this.weightNN[1][0] = 0.17491729D;
		this.weightNN[1][1] = -0.0467863D;
		this.weightNN[1][2] = 0.17462811D;
		this.weightNN[1][3] = 0.03037175D;
		this.weightNN[1][4] = -0.20012138D;
		this.weightNN[1][5] = 0.11618808D;
		this.weightNN[1][6] = 0.04476362D;
		this.weightNN[1][7] = -0.27435917D;
		this.weightNN[1][8] = -0.34074678D;
		this.weightNN[1][9] = 0.04785409D;
		this.weightNN[2][0] = 0.37539159D;
		this.weightNN[2][1] = 0.25006792D;
		this.weightNN[2][2] = 0.51257027D;
		this.weightNN[2][3] = 0.68247989D;
		this.weightNN[2][4] = 0.36550626D;
		this.weightNN[2][5] = 0.21091794D;
		this.weightNN[2][6] = -0.17030578D;
		this.weightNN[2][7] = -0.06858553D;
		this.weightNN[2][8] = -0.8879999D;
		this.weightNN[2][9] = -0.40723251D;

		this.weightMin[0] = -0.711111111D;
		this.weightMin[1] = -0.682926829D;
		this.weightMin[2] = -0.704545455D;
		this.weightMin[3] = -0.74D;
		this.weightMin[4] = -0.682926829D;
		this.weightMin[5] = -0.745098039D;
		this.weightMin[6] = -1.0D;
		this.weightMin[7] = -0.779661017D;
		this.weightMin[8] = -0.368055556D;
		this.weightMin[9] = -0.711111111D;

		this.weightRange[0] = 2.238888889D;
		this.weightRange[1] = 1.360346184D;
		this.weightRange[2] = 1.510101011D;
		this.weightRange[3] = 1.597142857D;
		this.weightRange[4] = 1.488482385D;
		this.weightRange[5] = 1.853206147D;
		this.weightRange[6] = 1.762711864D;
		this.weightRange[7] = 1.351089588D;
		this.weightRange[8] = 0.715616532D;
		this.weightRange[9] = 1.421637427D;

		double[] arrayOfDouble1 = new double[3];

		arrayOfDouble1[0] = 0.10667967D;
		arrayOfDouble1[1] = 0.15053564D;
		arrayOfDouble1[2] = 0.98879035D;

		double[] arrayOfDouble2 = this.cardCount.getInputs();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 10; j++) {
				arrayOfDouble1[i] += this.weightNN[i][j] * (arrayOfDouble2[j] - this.weightMin[j]) / this.weightRange[j];
			}
		}
		for (int k = 0; k < 3; k++) {
			arrayOfDouble1[k] = squish(arrayOfDouble1[k]);
		}
		double d = squish(0.43549004D * arrayOfDouble1[0] + 0.24037475D * arrayOfDouble1[1] + -0.79204378D * arrayOfDouble1[2] + 0.27736448D);
		if (d > 0.5D) {
			return "Bet High";
		}
		return "Bet Low";
	}

	public double projectedValSplit(int paramInt1, int paramInt2)
	{
		this.weightNN = new double[3][12];
		this.weightMin = new double[12];
		this.weightRange = new double[12];

		this.weightNN[0][0] = -1.21651929D;
		this.weightNN[0][1] = -1.75106837D;
		this.weightNN[0][2] = -1.5230685D;
		this.weightNN[0][3] = -2.1164724D;
		this.weightNN[0][4] = -2.26898277D;
		this.weightNN[0][5] = -1.56448317D;
		this.weightNN[0][6] = -0.56488825D;
		this.weightNN[0][7] = 0.89017783D;
		this.weightNN[0][8] = 1.66608082D;
		this.weightNN[0][9] = 0.02400712D;
		this.weightNN[0][10] = -0.24058565D;
		this.weightNN[0][11] = 8.81926644D;
		this.weightNN[1][0] = 1.22057476D;
		this.weightNN[1][1] = 1.09101458D;
		this.weightNN[1][2] = 1.10072435D;
		this.weightNN[1][3] = 1.22400125D;
		this.weightNN[1][4] = 1.65969363D;
		this.weightNN[1][5] = 1.85113163D;
		this.weightNN[1][6] = 1.4028604D;
		this.weightNN[1][7] = 1.4958162D;
		this.weightNN[1][8] = 0.57825291D;
		this.weightNN[1][9] = 1.72673181D;
		this.weightNN[1][10] = -9.38866057D;
		this.weightNN[1][11] = -0.27153814D;
		this.weightNN[2][0] = 0.90780594D;
		this.weightNN[2][1] = 1.51781949D;
		this.weightNN[2][2] = 1.11716148D;
		this.weightNN[2][3] = 2.02920127D;
		this.weightNN[2][4] = 2.83394159D;
		this.weightNN[2][5] = 2.23718097D;
		this.weightNN[2][6] = 0.62584858D;
		this.weightNN[2][7] = -0.46508107D;
		this.weightNN[2][8] = -0.29303796D;
		this.weightNN[2][9] = 0.23615821D;
		this.weightNN[2][10] = 0.23876952D;
		this.weightNN[2][11] = -10.39379175D;

		this.weightMin[0] = -1.0D;
		this.weightMin[1] = -0.796875D;
		this.weightMin[2] = -1.0D;
		this.weightMin[3] = -0.75D;
		this.weightMin[4] = -1.0D;
		this.weightMin[5] = -0.796875D;
		this.weightMin[6] = -1.0D;
		this.weightMin[7] = -1.371428571D;
		this.weightMin[8] = -0.290909091D;
		this.weightMin[9] = -1.351351351D;
		this.weightMin[10] = 2.0D;
		this.weightMin[11] = 2.0D;

		this.weightRange[0] = 2.052631579D;
		this.weightRange[1] = 1.699314024D;
		this.weightRange[2] = 1.733333333D;
		this.weightRange[3] = 1.71969697D;
		this.weightRange[4] = 2.022222222D;
		this.weightRange[5] = 2.160511364D;
		this.weightRange[6] = 1.936170213D;
		this.weightRange[7] = 2.273867595D;
		this.weightRange[8] = 0.820320856D;
		this.weightRange[9] = 2.518018018D;
		this.weightRange[10] = 9.0D;
		this.weightRange[11] = 9.0D;

		double[] arrayOfDouble1 = new double[3];

		arrayOfDouble1[0] = 0.15847939D;
		arrayOfDouble1[1] = 3.21499955D;
		arrayOfDouble1[2] = 0.58619056D;

		double[] arrayOfDouble2 = this.cardCount.getInputs();
		arrayOfDouble2[10] = paramInt1;
		arrayOfDouble2[11] = paramInt2;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 12; j++) {
				arrayOfDouble1[i] += this.weightNN[i][j] * (arrayOfDouble2[j] - this.weightMin[j]) / this.weightRange[j];
			}
		}
		for (int k = 0; k < 3; k++) {
			arrayOfDouble1[k] = squish(arrayOfDouble1[k]);
		}
		double d = squish(2.0134737D * arrayOfDouble1[0] + -3.46032026D * arrayOfDouble1[1] + 2.44507157D * arrayOfDouble1[2] + 0.87420299D);
		d = d * 2.0D - 1.0D;

		return d;
	}
}
