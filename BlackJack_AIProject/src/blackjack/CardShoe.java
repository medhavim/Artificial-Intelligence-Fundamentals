package blackjack; 

import java.util.LinkedList; 
import java.util.Random; 

public class CardShoe {

	int numberOfDecks; 
	int cardsLeft; 
	LinkedList<CardValueType> cardValueTypes; 

	public CardShoe(int intParam)
	{
		this.numberOfDecks = intParam; 
		shuffleNew(); 
	}
	
	public void shuffleNew()
	{
		this.cardsLeft = (this.numberOfDecks * 52); 
		int[] arrayOfInt = new int[this.cardsLeft]; 
		Random localRandom = new Random(); 
		for (int i = 0;  i < this.cardsLeft;  i++) {
			arrayOfInt[i] = (i % 52); 
		}
		for (int j = 0;  j < this.cardsLeft;  j++)
		{
			int k = localRandom.nextInt(this.cardsLeft); 
			int m = arrayOfInt[k]; 
			arrayOfInt[k] = arrayOfInt[j]; 
			arrayOfInt[j] = m; 
		}
		this.cardValueTypes = new LinkedList<CardValueType>(); 
		for (int k = 0;  k < this.cardsLeft;  k++) {
			this.cardValueTypes.add(new CardValueType(arrayOfInt[k])); 
		}
	}

	public CardValueType dealCard()
	{
		this.cardsLeft = this.cardsLeft - 1; 
		return (CardValueType)this.cardValueTypes.removeFirst(); 
	}

	public boolean lowOnCards()
	{
		if (this.cardsLeft < 35) {
			return true; 
		}
		return false; 
	}

}
