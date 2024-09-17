//Fernando Gonzalez

//this is the a seperate file for the Card class with comments listed below

public class Card {
    private int value;  // Value of the cards
    private String suit;  // Hearts, Diamonds, Clubs, Spades
    private String rank;  // 2-10, Jack, Queen, King, Ace

    // Constructor
    public Card(int value, String suit, String rank) {
        this.value = value;
        this.suit = suit;
        this.rank = rank;
    }

    // Accessor
    public int getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    // Mutator methods (setters)
    public void setValue(int value) {
        this.value = value;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    // toString method for printing card details
    @Override
    public String toString() {
        return rank + " of " + suit + " (Value: " + value + ")";
    }
}
