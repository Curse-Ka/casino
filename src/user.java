import java.util.ArrayList;

public class user implements Comparable<user> {
	// Yeah it's unsafe programming but it's easier so
	
	private String name;
	private int chips;
	public ArrayList<card> hand = new ArrayList<card>();
	private int bet;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setChips(int chips) {
		this.chips = chips;
	}
	
	public String name() {
		return this.name;
	}
	
	public int chips() {
		return this.chips;
	}
	
	public int bet(int bet) {
		chips -= bet;
		this.bet += bet;
		return this.bet;
	}
	
	public int bet() {
		return bet;
	}
	
	public int getSum(ArrayList<card> cards) {
		int sum = 0;
		int aces = 0;
		for (card c : cards) {
			if (c.visibility()) {
				if (c.rank().equals("Ace")) {
					aces++;
				}
				sum += c.pointValue();
			}
		}
		while (aces > 0 && sum > 21) {
			sum -= 10;
			aces--;
		}
		return sum;
	}
	
	public int outcome(user c) {
		int userHand = c.getSum(c.hand);
		int score = 0;
		if (userHand > 21) {
			System.out.println(c.name() + " busted");
			score = -1;
		}
		if (userHand == 21) {
			score = 1;
			if (c.hand.size() == 2) {
				System.out.println(c.name() + " got a Blackjack");
				score++;
			}
		}
		return score;
	}
	
	public int outcome() {
		int userHand = getSum(hand);
		int score = 0;
		if (userHand > 21) {
			System.out.println(name + " busted");
			score = -1;
		}
		if (userHand == 21) {
			score = 1;
			if (hand.size() == 2) {
				System.out.println(name + " got a Blackjack");
				score++;
			}
		}
		return score;
	}
	
	public int compareTo(user c) {
		// -1 = bust, 0 = stand, 1 = 21, 2 = Blackjack
		
		// Player
		int sum = getSum(hand);
		int outcome = outcome();
		
		// Other player
		int otherSum = c.getSum(c.hand);
		int otherOutcome = outcome(c);
		
		if (outcome == 0 && otherOutcome == 0 && sum != otherSum) {
			return (sum > otherSum) ? 1 : -1;
		} else if (outcome > otherOutcome) {
			return (outcome == 2) ? 2 : 1;
		} else if (outcome < otherOutcome) {
			return -1;
		} else {
			return 0;
		}
	}
}