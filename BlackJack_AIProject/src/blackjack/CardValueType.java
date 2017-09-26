package blackjack; 

public class CardValueType {

	int value; 
	int place; 

	public CardValueType(int intParam)
	{
		this.place = intParam; 
		this.value = (intParam % 13 + 1); 
		if (this.value > 10) {
			this.value = 10; 
		}
		if (this.value == 1) {
			this.value = 11; 
		}
	}

	public int getPlace()
	{
		return this.place; 
	}
	
	public int getValue()
	{
		return this.value; 
	}

}
