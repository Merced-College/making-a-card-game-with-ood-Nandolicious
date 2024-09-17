// Fernando Gonzalez

// One of the things I fixed in the code appeared to be the shuffledeck as for some reason it was in comments because because before it would just work its way up the ladder by giving the player a pair of 2s. Now when you star a game it is random what cards you get.

// I also added a feature that implments player betting. Every new game the player has a starting currency of 500 and can bet every round with a minimum of a 25 bet. If the player loses all his currency, he can no longer play.

//If he wins his bet is doubled.

//Comments were implemented below

import java.util.Random;
import java.util.Scanner;

public class BlackJack {

    private static Card[] cards = new Card[52];
    private static int currentCardIndex = 0;
    private static int suitIndex = 0;
    private static int playerCurrency = 500;  // Player starts with 500 currency

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean turn = true;
        String playerDecision = "";

        // added a for loop so the game can be played multiple times
        while (turn && playerCurrency >= 25) {  // Ensure player has enough to bet
            initializeDeck();
            shuffleDeck(); 
            
            //this shows how much currency the player has
            System.out.println("You have " + playerCurrency + " currency.");
            int betAmount = getBetAmount(scanner);  // Get bet amount from player

            int playerTotal = dealInitialPlayerCards();
            // fix dealInitialDealerCards
            int dealerTotal = dealInitialDealerCards();

            // fix playerTurn
            playerTotal = playerTurn(scanner, playerTotal);
            if (playerTotal > 21) {
                System.out.println("You busted! Dealer wins.");
                playerCurrency -= betAmount;  // Deduct the bet from player's currency
                checkCurrency();  // Check if player has enough to continue
                continue;  // Move to the next game loop
            }

            // fix dealerTurn
            dealerTotal = dealerTurn(dealerTotal);

            determineWinner(playerTotal, dealerTotal, betAmount);
            
            // added
            // asks player if they want to play again
            System.out.println("Would you like to play another hand?");
            playerDecision = scanner.nextLine().toLowerCase();
            
            while (!(playerDecision.equals("no") || (playerDecision.equals("yes")))) {
                System.out.println("Invalid action. Please type 'yes' or 'no'.");
                playerDecision = scanner.nextLine().toLowerCase();
            }
            if (playerDecision.equals("no"))
                turn = false;
        }
        
        //If the player has less than 25 it is gameover
        if (playerCurrency < 25) {
            System.out.println("You don't have enough currency to place the minimum bet. Game over!");
        } else {
            System.out.println("Thanks for playing!");
        }
    }

    // algorithm to create deck
    private static void initializeDeck() {
        String[] SUITS = { "Hearts", "Diamonds", "Clubs", "Spades" };
        String[] RANKS = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace" };
        int suitsIndex = 0, rankIndex = 0;
        for (int i = 0; i < cards.length; i++) {
            int val = 10;
            if (rankIndex < 9)
                val = Integer.parseInt(RANKS[rankIndex]);
            else if (rankIndex == 12)  // Ace
                val = 11;

            cards[i] = new Card(val, SUITS[suitsIndex], RANKS[rankIndex]);
            suitsIndex++;
            if (suitsIndex == 4) {
                suitsIndex = 0;
                rankIndex++;
            }
        }
    }

    // algorithm to shuffle deck
    private static void shuffleDeck() {
        Random random = new Random();
        for (int i = 0; i < cards.length; i++) {
            int index = random.nextInt(cards.length);
            Card temp = cards[i];
            cards[i] = cards[index];
            cards[index] = temp;
        }
    }

    // Function to get the player's bet amount, minimum bet being 25
    private static int getBetAmount(Scanner scanner) {
        int betAmount;
        do {
            System.out.println("Enter your bet amount (minimum bet is 25): ");
            betAmount = scanner.nextInt();
            scanner.nextLine();  // Consume newline left after nextInt
            if (betAmount < 25 || betAmount > playerCurrency) {
                System.out.println("Invalid bet amount. Bet must be between 25 and your current currency (" + playerCurrency + ").");
            }
        } while (betAmount < 25 || betAmount > playerCurrency);

        return betAmount;
    }

    // Function to check if player has enough currency to continue
    private static void checkCurrency() {
        if (playerCurrency < 25) {
            System.out.println("You don't have enough currency to place the minimum bet. Game over!");
            System.exit(0);  // End the game if player is out of currency
        }
    }

    // algorithm to deal initial player cards
    private static int dealInitialPlayerCards() {
        Card card1 = dealCard();
        Card card2 = dealCard();

        // System.out.println("Your cards: " + RANKS[card1] + " of " + SUITS[card1 / 13] + " and " + RANKS[card2] + " of " + SUITS[card2 / 13]);
        System.out.println("Your cards: " + card1.getRank() + " of " + card1.getSuit() + " and " + card2.getRank() + " of " + card2.getSuit());

        return card1.getValue() + card2.getValue();
    }

    // algorithm to deal initial dealer cards
    private static int dealInitialDealerCards() {
        Card card1 = dealCard();
        System.out.println("Dealer's card: " + card1);
        return card1.getValue();
    }

    private static int playerTurn(Scanner scanner, int playerTotal) {
        while (true) {
            System.out.println("Your total is " + playerTotal + ". Do you want to hit or stand?");
            String action = scanner.nextLine().toLowerCase();
            if (action.equals("hit")) {
                Card newCard = dealCard();
                playerTotal += newCard.getValue();
                System.out.println("You drew a " + newCard);
                if (playerTotal > 21) {
                    // added
                    // resets playerTotal so the game can be played multiple times
                    System.out.println("You busted! Dealer wins.");
                    return playerTotal;
                }
            } else if (action.equals("stand")) {
                break;
            } else {
                System.out.println("Invalid action. Please type 'hit' or 'stand'.");
            }
        }
        return playerTotal;
    }

    // algorithm for dealer's turn
    private static int dealerTurn(int dealerTotal) {
        while (dealerTotal < 17) {
            Card newCard = dealCard();
            dealerTotal += newCard.getValue();
        }
        System.out.println("Dealer's total is " + dealerTotal);
        return dealerTotal;
    }

    // algorithm to determine the winner
    private static void determineWinner(int playerTotal, int dealerTotal, int betAmount) {
        if (dealerTotal > 21 || playerTotal > dealerTotal) {
            System.out.println("You win!");
            playerCurrency += betAmount;  // Add bet to player's currency
        } else if (dealerTotal == playerTotal) {
            System.out.println("It's a tie!");
        } else {
            System.out.println("Dealer wins!");
            playerCurrency -= betAmount;  // Deduct bet from player's currency
        }
        // Check if player has enough to continue
        checkCurrency();
    }

    // algorithm to deal a card
    private static Card dealCard() {
        return cards[currentCardIndex++];
    }
}

// Card class
class Card {
    private int value;
    private String suit;
    private String rank;

    public Card(int value, String suit, String rank) {
        this.value = value;
        this.suit = suit;
        this.rank = rank;
    }

    public int getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

