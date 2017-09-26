package blackjack; 

public class Dealer {

	Hand hand; 
	CardValueType hiddenCard; 
	boolean blackjack; 

	public Dealer()
	{
		this.hand = new Hand(); 
	}
	
	public int getValue()
	{
		return this.hand.getValue(); 
	}

	public Hand getHand()
	{
		return this.hand; 
	}

	public int isSoft()
	{
		return this.hand.isSoft(); 
	}

	public boolean hasBlackjack()
	{
		return this.blackjack; 
	}

	public int newHand(CardValueType cardParam1, CardValueType cardParam2)
	{
		this.hiddenCard = cardParam1; 
		this.hand = new Hand(cardParam2); 
		if (cardParam1.getValue() + cardParam2.getValue() == 21) {
			this.blackjack = true; 
		} else {
			this.blackjack = false; 
		}
		return cardParam2.getValue(); 
	}
	
	public int addCard(CardValueType cardParam)
	{
		this.hand.addCard(cardParam); 
		return cardParam.getValue(); 
	}

	public int showFirstCard()
	{
		this.hand.addFirstCard(this.hiddenCard); 
		return this.hiddenCard.getValue(); 
	}

}
