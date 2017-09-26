package blackjack;

public class CardCount {

	int numberOfDecks; 
	int cardsLeft; 
	int[] cardType; 

	public CardCount(int intParam1, int intParam2, int intParam3, int intParam4, int intParam5, int intParam6, int intParam7, int intParam8, int intParam9, int intParam10, int intParam11, int intParam12)
	{
		this.cardType = new int[12]; 
		this.numberOfDecks = intParam11; 
		this.cardsLeft = intParam12; 
		this.cardType[2] = intParam2; 
		this.cardType[3] = intParam3; 
		this.cardType[4] = intParam4; 
		this.cardType[5] = intParam5; 
		this.cardType[6] = intParam6; 
		this.cardType[7] = intParam7; 
		this.cardType[8] = intParam8; 
		this.cardType[9] = intParam9; 
		this.cardType[10] = intParam10; 
		this.cardType[11] = intParam1; 
	}

	public CardCount(int intParam)
	{
		this.numberOfDecks = intParam; 
		resetCount(); 
	}

	public double chanceDealerDown(int intParam1, int intParam2)
	{
		if (intParam2 == 10)
		{
			if (intParam1 == 11) {
				return 0.0D; 
			}
			return this.cardType[intParam1] / (this.cardsLeft - this.cardType[11]); 
		}
		if (intParam1 == 10) {
			return 0.0D; 
		}
		return this.cardType[intParam1] / (this.cardsLeft - this.cardType[10]); 
	}
	
	public int getCardsLeft()
	{
		return this.cardsLeft; 
	}

	public double chance(int intParam)
	{
		return this.cardType[intParam] / this.cardsLeft; 
	}
	

	public void resetCount()
	{
		this.cardsLeft = (this.numberOfDecks * 52); 
		this.cardType = new int[12]; 
		for (int i = 2; i < 12; i++) {
			this.cardType[i] = (4 * this.numberOfDecks); 
		}
		this.cardType[10] = (16 * this.numberOfDecks); 
	}

	public CardCount spanNew(int intParam)
	{
		this.cardType[intParam] = this.cardType[intParam] - 1; 
		CardCount localCardCount = new CardCount(this.cardType[2], this.cardType[3], this.cardType[4], this.cardType[5], this.cardType[6], this.cardType[7], this.cardType[8], this.cardType[9], this.cardType[10], this.cardType[11], this.numberOfDecks, this.cardsLeft - 1); 

		this.cardType[intParam] = this.cardType[intParam] + 1; 
		return localCardCount; 
	}

	public void observeValue(int intParam)
	{
		this.cardType[intParam] = this.cardType[intParam] - 1; 
		this.cardsLeft = this.cardsLeft - 1; 
	}

	public void unobserveValue(int intParam)
	{
		this.cardType[intParam] = this.cardType[intParam] + 1; 
		this.cardsLeft = this.cardsLeft + 1; 
	}
	
	public double[] getInputs()
	{
		double[] arrayOfDouble = new double[12]; 
		for (int i = 2; i < 12; i++) {
			arrayOfDouble[(i - 2)] = (this.cardType[i] * 13.0D / this.cardsLeft - 1.0D); 
		}
		arrayOfDouble[8] = (this.cardType[10] * 13.0D / this.cardsLeft / 4.0D - 1.0D); 

		return arrayOfDouble; 
	}

}
