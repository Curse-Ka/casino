/* TODO
 * Fix repeat of printHands after hit?
 * spacing, timing, sizing
 * Say outcome before quitting. 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class game {
	public static void main(String[] args) {
		
		// Initialize Things outside the loop
		Scanner sc = new Scanner(System.in);
		game g = new game();
		user player = new user();
		user dealer = new user();
		
		// Take Starting Information
		System.out.print("Welcome to the Blackjack table\nat the Krska Kasino!\n\nPress [enter] when you're ready to begin!");
		sc.nextLine();
		
		dealer.setName("Dealer");

		System.out.print("\nWhat is your name?\n\n-> ");
		player.setName(sc.nextLine());
		while (!(player.chips() > 0)) {
			try {
				System.out.print("\nHow many chips do you want to buy?\n\n-> ");
				player.setChips(Integer.parseInt(sc.nextLine()));
				if (player.chips() < 1) {
					System.out.print("\nOops that's not a valid amount.\n");
					g.pause();
				}
			} catch (Exception e) {
				System.out.print("\nOops that's not a valid amount.\n");
				g.pause();
			}
		}
		
		boolean outerLoop = true;
		while (outerLoop) {		
			// Initialize 
			deck d = new deck(new String[] {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"}, 
					new String[] {"Hearts", "Clubs", "Spades", "Diamonds"},
					new int[] {11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10});
			
			// Set ante and deal initial cards
			int bet = 0;
			while (!(bet > 0)) {
				try {
					System.out.print("\nWhat's your ante? Dealer matches initial bet.\nYou have " + player.chips() + " chips.\n\n-> ");
					bet = Integer.parseInt(sc.nextLine());
					if (bet < 1 || bet > player.chips()) {
						System.out.print("\nOops that's not a valid amount.\n");
						g.pause();
						bet = 0;
					}
				} catch (Exception e) {
					System.out.print("\nOops that's not a valid amount.\n");
					g.pause();
				}
			}
			player.bet(bet);
			dealer.bet(bet);
			
			System.out.print("\nDealing");
			g.dots();
			player.hand.add(d.deal());
			player.hand.add(d.deal());
			card temp = d.deal();
			temp.changeVis(false);
			dealer.hand.add(temp);
			dealer.hand.add(d.deal());
			
			// Game Play Loop			
			boolean innerLoop = (player.getSum(player.hand) == 21) ? false : true;
			int response = 0;
			while (innerLoop) {
				g.printHands(new ArrayList<user>(Arrays.asList(player, dealer)));
				System.out.println("Press [enter] to continue.");
				sc.nextLine();
				System.out.print("Would you like to...\n\n\t[1] Hit\n\t[2] Stand\n\t[3] Double Down" + ((player.hand.size() == 2) ? "\n\t[4] Surrender" : "") + "\n\n-> ");
				while (!(response > 0)) {
					try {
						response = Integer.parseInt(sc.nextLine());
						if (response < 1 || response > 4 || (response == 4 && player.hand.size() != 2)) {
							System.out.print("\nOops that's not a valid choice.\n");
							g.pause();
							response = 0;
						}
					} catch (Exception e) {
						System.out.print("\nOops that's not a valid choice.\n");
						g.pause();
					}
				}
				
				switch (response) {
				// Hit
				case 1:
					System.out.print("You chose to hit.\n\nDealing");
					g.dots();
					player.hand.add(d.deal());
					g.printHands(new ArrayList<user>(Arrays.asList(player, dealer)));
					if (player.getSum(player.hand) >= 21) 
						innerLoop = false;
					break;
				// Stand
				case 2:
					System.out.println("You chose to stand.\n\n");
					innerLoop = false;
					break;
				// 
				case 3:
					System.out.println("You chose to double down.\n");
					if (player.chips() == 0) {
						System.out.println("You have no more chips to bet.");
						break;
					}
					int raise = 0;
					while (!(raise > 0)) {
						try {
							System.out.print("\nWhat's your raise? Must be at most " + player.bet() + "\nYou have " + player.chips() + " chips.\n\n-> ");
							raise += Integer.parseInt(sc.nextLine());
							if (raise < 1 || (raise) > player.chips() || raise > player.bet()) {
								System.out.print("\nOops that's not a valid amount.\n");
								g.pause();
								raise = 0;
							}
						} catch (Exception e) {
							System.out.print("\nOops that's not a valid amount.\n");
							g.pause();
						}
					}
					player.bet(raise);
					System.out.print("Dealing");
					g.dots();
					player.hand.add(d.deal());
					g.printHands(new ArrayList<user>(Arrays.asList(player, dealer)));
					innerLoop = false;
					break;
				case 4:
					System.out.println("You chose to surrender. You were returned half your wager.\n\n");
					innerLoop = false;
					player.setChips(player.chips() + (int) 0.5 * player.bet());
					break;
				default:
					System.out.print("ERROR: Response invalid; will try again");
					g.dots();
					break;
				}
			} // End of Game Play Loop
			
			// Finish Dealer
			System.out.println("Hole Card was a " + dealer.hand.get(0).toString());
			dealer.hand.get(0).changeVis(true);
			
			g.pause();
			
			while (dealer.getSum(dealer.hand) < 17) {
				System.out.println("Dealing to Dealer...");
				dealer.hand.add(d.deal());
			}
			
			System.out.println("");
			
			if (response != 4) {
				g.pause();
				
				g.printHands(new ArrayList<user>(Arrays.asList(player, dealer)));
				
				System.out.println("Press [enter] to continue.");
				sc.nextLine();
				
				// Use compareTo to get lose/tie/win/BJ win
				int outcome = player.compareTo(dealer);
				switch (outcome) {
				case -1:
					System.out.println("Better luck next round :/");
					break;
				case 0:
					System.out.println("It was a tie. All wagers returned");
					player.setChips(player.bet() + player.chips());
					System.out.println("You have " + player.chips() + " chips");
					break;
				case 1:
					System.out.println("Well played. You have won " + player.bet() + " chips");
					player.setChips(2 * player.bet() + player.chips());
					System.out.println("You have " + player.chips() + " chips");
					break;
				case 2:
					System.out.println("Well played. You have won " + 1.5 * player.bet() + " chips with a Black Jack");
					player.setChips((int) 2.5 * player.bet() + player.chips());
					System.out.println("You have " + player.chips() + " chips");
					break;
				default:
					System.out.print("ERROR: Response invalid; will try again");
					g.dots();
					outcome = player.compareTo(dealer);
					break;
				}
			}
			if (player.chips() == 0) {
				System.out.println("You've run out of chips. Thank you for playing.");
				outerLoop = false;
			} else {
				System.out.print("Would you like to...\n\n\t[1] Play Again\n\t[2] Cash Out\n\n-> ");
				
				int decision = 0;
				while (!(decision > 0)) {
					try {
						decision = Integer.parseInt(sc.nextLine());
						if (decision > 2 || decision < 1) {
							System.out.print("\nOops that's not a valid choice.\n");
							decision = 0;
							g.pause();
						}
					} catch (Exception e) {
						System.out.print("\nOops that's not a valid choice.\n");
						g.pause();
					}
				}
				
				switch (decision) {
				case 1:
					System.out.println("\nYou've chosen to stay and play some more!\n");
					player.hand.clear();
					dealer.hand.clear();
					
					break;
				case 2: 
					System.out.println("You've got " + player.chips() + " chips.");
					System.out.println("\nByeee\n");
					outerLoop = false;
					break;
				default:
					System.out.print("ERROR: Response invalid");
					g.dots();
					break;
				}
			}
		}
	}
	
	private void printHands(ArrayList<user> users) {
		for (user u : users) {
			System.out.println(u.name() + "'s hand:");
			for (card c : u.hand) {
				if (c.visibility()) {
					System.out.println("\t-" + c.toString());
				} else {
					System.out.println("\t-Hole Card (Hidden)");
				}
			}
			System.out.println("Sum of hand: " + u.getSum(u.hand) + "\n");
		}
		System.out.println("");
	}
	
	private void dots() {
		try {
			for(int i = 0; i < 3; i++) {
				System.out.print(".");
				Thread.sleep(400);
			}
		} catch (Exception e) {
			System.out.println("Error: Interruption.");
		}
		System.out.println("\n");
	}
	
	private void pause() {
		try {
			Thread.sleep(700);
		} catch (Exception e) { }
	}
}