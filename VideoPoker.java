package PJ4;
import java.util.*;

public class VideoPoker {

    // default constant values
    private static final int startingBalance=100;
    private static final int numberOfCards=5;

    // default constant payout value and playerHand types
    private static final int[] multipliers={1,2,3,5,6,10,25,50,1000};
    private static final String[] goodHandTypes={ 
	  "One Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	  "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

    // must use only one deck
    private final Decks oneDeck;

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;

    /** default constructor, set balance = startingBalance */
    public VideoPoker()
    {
	this(startingBalance);
    }

    /** constructor, set given balance */
    public VideoPoker(int balance)
    {
	this.playerBalance= balance;
        oneDeck = new Decks(1, false);
    }

    /** This display the payout table based on multipliers and goodHandTypes arrays */
    private void showPayoutTable()
    { 
	System.out.println("\n\n");
	System.out.println("Payout Table   	      Multiplier   ");
	System.out.println("=======================================");
	int size = multipliers.length;
	for (int i=size-1; i >= 0; i--) {
		System.out.println(goodHandTypes[i]+"\t|\t"+multipliers[i]);
	}
	System.out.println("\n\n");
    }

    /** Check current playerHand using multipliers and goodHandTypes arrays
     *  Must print yourHandType (default is "Sorry, you lost") at the end of function.
     *  This can be checked by testCheckHands() and main() method.
     */
    private void checkHands()
    {
    	
    	if (royalFlush()) 
    	{
			System.out.println("\nYou have a Royal Flush!");
			playerBalance += playerBet * 1000;
		} 
    	else if (straight() && flush()) 
    	{
			System.out.println("\nYou have a Straight Flush!");
			playerBalance += playerBet * 50;
		} 
    	else if (flush()) 
    	{
			System.out.println("\nYou have a Flush!");
			playerBalance += playerBet * 6;
		} 
    	else if (straight()) 
    	{
			System.out.println("\nYou have a Straight!");
			playerBalance += playerBet * 5;
		} 
    	else if (fourOfKind())
    	{
			System.out.println("\nYou have a four of a kind!");
			playerBalance += playerBet * 25;
		} else if (fullHouse()) 
		{
			System.out.println("\nYou have a Full House!");
			playerBalance += playerBet * 10;
		}
		else if (threeOfKind()) 
		{
			System.out.println("\nYou have three of a kind!");
			playerBalance += playerBet * 3;
		} 
		else if (twoPair()) 
		{
			System.out.println("\nYou have two pairs!");
			playerBalance += playerBet * 2;
		} 
		else if (pair())
		{
			System.out.println("\nYou have a pair!");
		} 
		else 
		{
			System.out.println("\nYou do not have any winning hands!");
			playerBalance = playerBalance - playerBet;
		}

    	
    }


    public void play() 
    {
   
    	playerBalance = startingBalance;
		showPayoutTable();
		Scanner betPlaced = new Scanner(System.in);
		Scanner chosen = new Scanner(System.in);
		Scanner response = new Scanner(System.in);
		List<Card> temp = new ArrayList<Card>();
		String replace;
		String answer;
		while (playerBalance > 0) {
			System.out.println("--------------------------------------------------");
			System.out.println("Balance: $" + playerBalance);
			System.out.print("Please enter an amount to bet: ");
			playerBet = betPlaced.nextInt();
			if (playerBet <= playerBalance) {
				try {
					oneDeck.reset();
					oneDeck.shuffle();
					playerHand = oneDeck.deal(numberOfCards);
					System.out.println(playerHand);
					System.out.print("Select positions of cards to replace (e.g. 1 4 5): ");
					replace = chosen.nextLine();
					Scanner lineScanner = new Scanner(replace);
					while (lineScanner.hasNextInt()) {
						temp = oneDeck.deal(1);
						playerHand.set(lineScanner.nextInt() - 1, temp.get(0));
					}
					lineScanner.close();
					System.out.print(playerHand);
					checkHands();
					if (playerBalance != 0) {
						System.out.print("\nYour balance: $" + playerBalance + ", one more game? (y or n): ");
						answer = response.next();
						if (answer.equals("n")) {
							System.out.println("\nBye");
							betPlaced.close();
							chosen.close();
							response.close();
							System.exit(0);
						} else {
							System.out.print("\nWould you like to see the payout table? (y or n): ");
							answer = response.next();
							if (answer.equals("y")) {
								showPayoutTable();
							}
							continue;
						}
					}
				} catch (PlayingCardException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("Insufficient funds.");
				continue;
			}

		}
		Scanner response2 = new Scanner(System.in);
		System.out.print("\nYour balance is 0. Would you like to play again? (y or n): ");
		String answer2 = response2.next();
		if (answer2.equals("y")) {
			play();
			response.close();
			betPlaced.close();
			chosen.close();
			response2.close();
		} else {
			System.out.println("\nBye");
			System.exit(0);
		}
		
    	
    	

    }

    /** testCheckHands() is used to test checkHands() method 
     *  checkHands() should print your current hand type
     */ 

    private boolean pair() {
		Card c1 = playerHand.get(0);
		Card c2 = playerHand.get(1);
		Card c3 = playerHand.get(2);
		Card c4 = playerHand.get(3);
		Card c5 = playerHand.get(4);
		boolean isPair = false;
		int x[] = { c1.getRank(), c2.getRank(), c3.getRank(), c4.getRank(), c5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[0] < x[2] && x[3] < x[4]) || (x[1] == x[2] && x[1] < x[3] && x[1] > x[0])
				|| (x[2] == x[3] && x[3] < x[4] && x[1] < x[2] && x[0] < x[1])
				|| (x[3] == x[4] && (x[2] != x[1] && x[1] != x[0]))) {
			isPair = true;
		}
		return isPair;
	}

	private boolean twoPair() {
		Card c1 = playerHand.get(0);
		Card c2 = playerHand.get(1);
		Card c3 = playerHand.get(2);
		Card c4 = playerHand.get(3);
		Card c5 = playerHand.get(4);
		boolean isTwoPair = false;
		int x[] = { c1.getRank(), c2.getRank(), c3.getRank(), c4.getRank(), c5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] != x[2] && x[2] == x[3])
				|| (x[1] == x[2] && x[2] != x[3] && x[3] == x[4] && x[1] != x[0])
				|| (x[0] == x[1] && x[1] != x[2] && x[3] == x[4] && x[2] < x[3])) {
			isTwoPair = true;
		}

		return isTwoPair;
	}

	private boolean threeOfKind() {
		Card c1 = playerHand.get(0);
		Card c2 = playerHand.get(1);
		Card c3 = playerHand.get(2);
		Card c4 = playerHand.get(3);
		Card c5 = playerHand.get(4);
		boolean isThreeKind = false;
		int x[] = { c1.getRank(), c2.getRank(), c3.getRank(), c4.getRank(), c5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] == x[2] && x[0] < x[3] && x[3] != x[4])
				|| (x[1] == x[2] && x[2] == x[3] && x[1] < x[4] && x[1] > x[0])
				|| (x[2] == x[3] && x[3] == x[4] && x[2] > x[1] && x[1] != x[0])) {
			isThreeKind = true;
		}

		return isThreeKind;
	}

	private boolean fullHouse() {
		Card c1 = playerHand.get(0);
		Card c2 = playerHand.get(1);
		Card c3 = playerHand.get(2);
		Card c4 = playerHand.get(3);
		Card c5 = playerHand.get(4);
		boolean isFullHouse = false;
		int x[] = { c1.getRank(), c2.getRank(), c3.getRank(), c4.getRank(), c5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] == x[2] && x[3] == x[4]) || (x[0] == x[1] && x[2] == x[3] && x[3] == x[4])) {
			isFullHouse = true;
		}

		return isFullHouse;
	}

	private boolean fourOfKind() {
		Card c1 = playerHand.get(0);
		Card c2 = playerHand.get(1);
		Card c3 = playerHand.get(2);
		Card c4 = playerHand.get(3);
		Card c5 = playerHand.get(4);
		boolean isFour = false;
		int x[] = { c1.getRank(), c2.getRank(), c3.getRank(), c4.getRank(), c5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] == x[2] && x[2] == x[3] && x[3] != x[4])
				|| (x[0] != x[1] && x[1] == x[2] && x[2] == x[3] && x[3] == x[4])) {
			isFour = true;
		}

		return isFour;
	}

	private boolean flush() {
		Card c1 = playerHand.get(0);
		Card c2 = playerHand.get(1);
		Card c3 = playerHand.get(2);
		Card c4 = playerHand.get(3);
		Card c5 = playerHand.get(4);
		boolean isFlush = false;
		int x[] = { c1.getSuit(), c2.getSuit(), c3.getSuit(), c4.getSuit(), c5.getSuit() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] == x[2] && x[2] == x[3] && x[3] == x[4])) {
			isFlush = true;
		}

		return isFlush;
	}

	private boolean straight() {
		Card c1 = playerHand.get(0);
		Card c2 = playerHand.get(1);
		Card c3 = playerHand.get(2);
		Card c4 = playerHand.get(3);
		Card c5 = playerHand.get(4);
		boolean isStraight = false;
		int x[] = { c1.getRank(), c2.getRank(), c3.getRank(), c4.getRank(), c5.getRank() };
		Arrays.sort(x);
		if ((x[1] == (x[0] + 1)) && (x[2] == (x[1] + 1)) && (x[3] == (x[2] + 1)) && (x[4] == (x[3] + 1))
				|| ((x[0] == 1) && (x[1] == 10) && (x[2] == 11) && (x[3] == 12) && (x[4] == 13))) {
			isStraight = true;
		}

		return isStraight;
	}

	private boolean royalFlush() {
		Card c1 = playerHand.get(0);
		Card c2 = playerHand.get(1);
		Card c3 = playerHand.get(2);
		Card c4 = playerHand.get(3);
		Card c5 = playerHand.get(4);
		boolean isRoyalFlush = false;
		int x[] = { c1.getRank(), c2.getRank(), c3.getRank(), c4.getRank(), c5.getRank() };
		Arrays.sort(x);
		if (flush() && ((x[0] == 1) && (x[1] == 10) && (x[2] == 11) && (x[3] == 12) && (x[4] == 13))) {
			isRoyalFlush = true;
		}

		return isRoyalFlush;
	}
    
    public void testCheckHands()
    {
      	try {
    		playerHand = new ArrayList<Card>();

		// set Royal Flush
		playerHand.add(new Card(3,1));
		playerHand.add(new Card(3,10));
		playerHand.add(new Card(3,12));
		playerHand.add(new Card(3,11));
		playerHand.add(new Card(3,13));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight Flush
		playerHand.set(0,new Card(3,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight
		playerHand.set(4, new Card(1,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Flush 
		playerHand.set(4, new Card(3,5));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	 	// "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

		// set Four of a Kind
		playerHand.clear();
		playerHand.add(new Card(4,8));
		playerHand.add(new Card(1,8));
		playerHand.add(new Card(4,12));
		playerHand.add(new Card(2,8));
		playerHand.add(new Card(3,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Three of a Kind
		playerHand.set(4, new Card(4,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Full House
		playerHand.set(2, new Card(2,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Two Pairs
		playerHand.set(1, new Card(2,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set One Pair
		playerHand.set(0, new Card(2,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set One Pair
		playerHand.set(2, new Card(4,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set no Pair
		playerHand.set(2, new Card(4,6));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");
      	}
      	catch (Exception e)
      	{
		System.out.println(e.getMessage());
      	}
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) 
    {
	VideoPoker pokergame = new VideoPoker();
	pokergame.testCheckHands();
    }
}
