import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ProjectMain extends JPanel {

    public static void main(String args[]){
        JFrame frame=new JFrame("Project INBETWEEN");//creating frame
        ProjectMdain content=new ProjectMain();//creating constructor
        frame.setContentPane(content);
        frame.setSize(900,500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public ProjectMain(){
        setBackground(new Color(130,50,40));
        setLayout(new BorderLayout(3,3));

        //creating a constructor of cardpanel
        CardPanel board=new CardPanel();
        add(board,BorderLayout.CENTER);

        //setting color of button panel
        JPanel buttonPanel=new JPanel();
        buttonPanel.setBackground(new Color(220,200,180));
        add(buttonPanel,BorderLayout.SOUTH);

        //creating button
        JButton draw=new JButton("DRAW CARD");
        draw.addActionListener(board);
        buttonPanel.add(draw);

        JButton bid=new JButton("BID");
        bid.addActionListener(board);
        bid.setEnabled(false);
        buttonPanel.add(bid);

        JButton pack=new JButton("PACK");
        pack.addActionListener(board);
        pack.setEnabled(false);
        buttonPanel.add(pack);

        JButton newGame=new JButton("New Game");
        newGame.addActionListener(board);
        newGame.setEnabled(false);
        buttonPanel.add(newGame);

        setBorder(BorderFactory.createLineBorder(new Color(130,70,40)));

        //making the button disable
        //for draw
        draw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.setEnabled(false);
                bid.setEnabled(true);
                pack.setEnabled(true);
                repaint();
            }
        });
        //for bid
        bid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bid.setEnabled(false);
                pack.setEnabled(false);
                newGame.setEnabled(true);
            }
        });
        //for pack
        pack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.setEnabled(false);
                pack.setEnabled(false);
                bid.setEnabled(false);
                newGame.setEnabled(true);
            }
        });
        //for newGame
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.setEnabled(true);
                pack.setEnabled(false);
                newGame.setEnabled(false);

            }
        });

    }
    private class CardPanel extends JPanel implements ActionListener{

        Deck deck;  //deck of card to used in the game
        Hand hand;  //the card that has to be delivered
        String message; //convey message in game

        boolean gameInProgress; //set true when game begins and false when game ends

        Font bigFont; //Font that is used to display message

        Image cardImages; //contain the image of 52 cards.

        //connecting database
        DatabaseCon d=new DatabaseCon();
        int value=d.retrive(); //retrive the initaial value exist in database
        int point= value; //assigning the value in point

        /**
         * Constructor creates fonts, sets the foreground and background
         * colors and starts the first game.
         */
        CardPanel(){
            loadImage();
            setBackground(new Color(0,120,0));
            setForeground(Color.GREEN);//color of message and foreground object.
            bigFont=new Font("serif",Font.BOLD,15);//setting the font of message
            doNewGame();
        }//end of CardPanel
        /**
         * Respond when the user clicks on a button by calling the appropriate
         * method.  Note: that the buttons are created and listening is set
         * up in the constructor of the HighLowPanel class.
         */
        public void actionPerformed(ActionEvent evt){
            String command=evt.getActionCommand();
            if(command.equals("DRAW CARD")){
                doDraw();
            }
            else if(command.equals("BID"))
                doBid();
            else if(command.equals("PACK"))
                doPack();
            else if(command.equals("New Game"))
                doNewGame();
        }
        void doDraw(){
            if(gameInProgress==false){
                message="click\"New Game\"to Start a new Game!";
                return;
            }
            hand.addCard(deck.dealCard());
            int Cardct=hand.getCardCount();
            repaint();
        }
        void doBid(){
            if(gameInProgress==false){
                message="Click \"New Game\" to start a new game!";
                repaint();
                return;
            }
            hand.addCard(deck.dealCard());
            int Cardct=hand.getCardCount();
            Card thisCard=hand.getCard(Cardct-1);
            Card prevCard=hand.getCard(Cardct-2);
            Card prevCard2=hand.getCard(Cardct-3);
            if ( thisCard.getValue() < prevCard.getValue()&&thisCard.getValue()>prevCard2.getValue()) {
                gameInProgress = false;
                message = "You win the 1 point ";
                repaint();
                point++;
                d.save(point);
            }
            else if ( thisCard.getValue() > prevCard.getValue()&&thisCard.getValue()< prevCard2.getValue()) {
                gameInProgress = false;
                message = "You win the 1 point ";
                repaint();
                point++;
                d.save(point);

            }
            else {
                message = "You loose 2 point";
                gameInProgress=false;
                point=point-2;
                d.save(point);//saving the point in database
                repaint();
            }
            repaint();
        }
        void doPack(){
            message="Card Packed in deck!! You have lost the board amount which is 1 point";
            point--;
            d.save(point);
            gameInProgress=false;
            repaint();
//            doNewGame();
        }
        void doNewGame(){
           if(gameInProgress){
               message="You still have to Finish this game";
               repaint();
               return;
           }
           deck=new Deck();
           hand =new Hand();
           deck.shuffle();
           hand.addCard(deck.dealCard());
           message="Draw a card / Bid or Pack / New Game";
           gameInProgress=true;
           repaint();
        }
        /**
         * This method draws the message at the bottom of the
         * panel, and it draws all of the dealt cards spread out
         * across the canvas.  If the game is in progress, an extra
         * card is drawn face down representing the card to be dealt next.
         */
 public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (cardImages == null) {
                g.drawString("Error: Can't get card images!", 10,30);
                return;
            }
            g.setFont(bigFont);
            g.drawString(message,15,168);
            g.drawRect(700,20,120,50);
            int cardCt = hand.getCardCount();
            for (int i = 0; i < 3; i++)
                drawCard(g, hand.getCard(i), 15 + i * (15+79), 15);
//            if (gameInProgress)
//                drawCard(g, null, 15 + cardCt * (15+79), 15);
            g.drawString("Total Point: "+point,710,40);
        } // end paintComponent()
        /**
         * Draws a card in a 79x123 pixel rectangle with its
         * upper left corner at a specified point (x,y).  Drawing the card
         * requires the image file "cards.png".
         * @param g The non-null graphics context used for drawing the card.  If g is
         * null, a NullPointerException will be thrown.
         * @param card The card that is to be drawn.  If the value is null, then a
         * face-down card is drawn.
         * @param x the x-coord of the upper left corner of the card
         * @param y the y-coord of the upper left corner of the card
         */

        public void drawCard(Graphics g, Card card, int x, int y) {
            int cx;    // x-coord of card inside cardsImage
            int cy;    // y-coord of card inside cardsImage
            if (card == null) {
                cy = 4*123;//width   // coords for a face-down card.
                cx = 2*79;//height
            }
            else {
                cx = (card.getValue()-1)*79;
                switch (card.getSuit()) {
                    case Card.CLUBS:
                        cy = 0;
                        break;
                    case Card.DIAMONDS:
                        cy = 123;
                        break;
                    case Card.HEARTS:
                        cy = 2*123;
                        break;
                    default:  // spades
                        cy = 3*123;
                        break;
                }
            }
            g.drawImage(cardImages,x,y,x+79,y+123,cx,cy,cx+79,cy+123,this);
        }
        private void loadImage() {
            ClassLoader cl = ProjectMain.class.getClassLoader();
            URL imageURL = cl.getResource("cards.png");
            if (imageURL != null)
                cardImages = Toolkit.getDefaultToolkit().createImage(imageURL);
        }

    }
}//end of main class ProjectMain

