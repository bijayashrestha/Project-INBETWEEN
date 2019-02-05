//import java.util.ArrayList;
//
//public class Hand {
//    private ArrayList hand;//the card in the hand
//    public Hand(){
//        hand=new ArrayList();//creating hand initally empty
//    }
//    public void clear(){
//        hand.clear();
//    }
//    public void addCard(Card c){
//        if(c==null)
//            throw new NullPointerException("Can't add a null card to a hand");
//        hand.add(c);
//    }
//    public int getCardCount(){
//        return hand.size();
//    }
//    public Card getCard(int position){
//        if(position<0||position>=hand.size())
//            throw new IllegalArgumentException("position doesnot exist in hand"+position);
//        return(Card)hand.get(position);
//    }
//}
/*
    An object of type Hand represents a hand of cards.  The maximum number of
    cards in the hand can be specified in the constructor, but by default
    is 5.  A utility function is provided for computing the value of the
    hand in the game of Blackjack.
*/

import java.util.Vector;

public class Hand {

    private Vector hand;   // The cards in the hand.

    public Hand(){
        // Create a Hand object that is initially empty.
        hand = new Vector();
    }

    public void addCard(Card c) {
        // Add the card c to the hand.  c should be non-null.  (If c is
        // null, nothing is added to the hand.)
        if (c != null)
            hand.addElement(c);
    }


    public int getCardCount() {
        // Return the number of cards in the hand.
        return hand.size();
    }

    public Card getCard(int position) {
        // Get the card from the hand in given position, where positions
        // are numbered starting from 0.  If the specified position is
        // not the position number of a card in the hand, then null
        // is returned.
        if (position >= 0 && position < hand.size())
            return (Card)hand.elementAt(position);
        else
            return null;
    }

}
