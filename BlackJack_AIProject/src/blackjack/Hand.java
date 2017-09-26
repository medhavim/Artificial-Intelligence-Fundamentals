package blackjack; 

import java.util.Iterator; 
import java.util.LinkedList; 

public class Hand {

	public LinkedList<CardValueType> cardValueTypes; 
	boolean splitable; 
	boolean twoCards; 
	int handValue; 
	int bet; 
	boolean soft; 

	public Hand()
	{
		this.handValue = 0; 
		this.bet = 10; 
		this.soft = false; 
		this.twoCards = false; 
		this.cardValueTypes = new LinkedList<CardValueType>(); 
		this.splitable = false; 
	}
	
	public Hand(CardValueType cardParam)
	{
		this.handValue = 0; 
		this.twoCards = false; 
		this.cardValueTypes = new LinkedList<CardValueType>(); 
		this.cardValueTypes.add(new CardValueType(52)); 
		this.cardValueTypes.add(cardParam); 
		addValue(cardParam.getValue()); 
		this.splitable = false; 
		if (cardParam.getValue() == 11) {
			this.soft = true; 
		} else {
			this.soft = false; 
		}
	}
	
	public Hand(CardValueType cardParam1, CardValueType cardParam2)
	{
		this.handValue = 0; 
		this.twoCards = true; 
		this.cardValueTypes = new LinkedList<CardValueType>(); 
		
		this.cardValueTypes.add(cardParam1); 
		addValue(cardParam1.getValue()); 
		
		this.cardValueTypes.add(cardParam2); 
		addValue(cardParam2.getValue()); 
		
		if (cardParam1.getValue() == cardParam2.getValue()) {
			this.splitable = true; 
		} else {
			this.splitable = false; 
		}
		if ((cardParam1.getValue() == 11) || (cardParam2.getValue() == 11)) {
			this.soft = true; 
		} else {
			this.soft = false; 
		}
	}

	private void addValue(int intParam)
	{
		@SuppressWarnings("unused")
		Iterator<CardValueType> localIterator = this.cardValueTypes.iterator(); 

		this.handValue = this.handValue + intParam; 
		if (intParam == 11) {
			if (this.handValue > 21) {
				this.handValue = this.handValue - 10; 
			} else {
				this.soft = true; 
			}
		}
		if ((this.handValue > 21) && (this.soft == true))
		{
			this.handValue = this.handValue - 10; 
			this.soft = false; 
		}
	}
	
	public int setBet(int intParam)
	{
		this.bet = intParam; 
		return this.bet; 
	}

	public int getBet()
	{
		return this.bet; 
	}

	public int getValue()
	{
		return this.handValue; 
	}
	
	public boolean isBlackjack()
	{
		return (this.twoCards) && (this.handValue == 21); 
	}

	public int isSoft()
	{
		if (this.soft == true) {
			return 1; 
		}
		return 0; 
	}

	public boolean isSplitable()
	{
		return this.splitable; 
	}

	public boolean isTwoCards()
	{
		return this.twoCards; 
	}

	public boolean busted()
	{
		return this.handValue > 21; 
	}

	public void addCard(CardValueType cardParam)
	{
		this.cardValueTypes.add(cardParam); 
		this.splitable = false; 
		this.twoCards = false; 
		addValue(cardParam.getValue()); 
	}

	public void addFirstCard(CardValueType cardParam)
	{
		@SuppressWarnings("unused")
		CardValueType localCard = (CardValueType)this.cardValueTypes.removeFirst(); 
		this.cardValueTypes.addFirst(cardParam); 
		addValue(cardParam.getValue()); 
	}

	public int changeBet(int intParam)
	{
		this.bet = this.bet + intParam; 
		return this.bet; 
	}

}
