public class card {
	
	private String suit;
	private String rank;
	private int pointValue;
	private boolean visibility;

	public card(String cardRank, String cardSuit, int cardPointValue, boolean cardVisibility) {
		//initializes a new card with the given rank, suit, and point value
		rank = cardRank;
		suit = cardSuit;
		pointValue = cardPointValue;
		visibility = cardVisibility;
	}

	public String suit() {
		return suit;
	}

	public String rank() {
		return rank;
	}

	public int pointValue() {
		return pointValue;
	}
	
	public boolean visibility() {
		return visibility;
	}
	
	public void changeVis(boolean vis) {
		visibility = vis;
	}

	public boolean matches(card otherCard) {
		return otherCard.suit().equals(this.suit())
			&& otherCard.rank().equals(this.rank())
			&& otherCard.pointValue() == this.pointValue();
	}
	
	@Override
	public String toString() {
		String result = rank + " of " + suit + " (point value = ";
		return result + ((rank.equals("Ace")) ? "1 or 11)" : pointValue + ")");
	}
}