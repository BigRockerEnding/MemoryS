package org.wintresstech.seh.memorys;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.wintrisstech.cards.Card;
import org.wintrisstech.cards.Deck;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable, ActionListener {
	// All the variables this program needs to USE
	// Form the grid
	private static final int ROWS = 4;
	private static final int COLS = 5;
	// Get random cards
	private static final Random RNG = new Random();
	private Deck sDeck;
	// track what cards are selected and the number
	private int state = 0;
	private CardButton selected1 = null;
	private CardButton selected2 = null;
	// You have enough matches?
	private int matches = 0;
	private int matchTarget = (ROWS * COLS) / 2;
	// Holds the buttons
	private CardButton[] buttons = new CardButton[ROWS * COLS];
	// Automatically flip
	private ActionListener flipBack = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (state == 2) {
				selected1.flip();
				selected2.flip();
				state = 0;
			}

		}
	};
	private Timer tmrFlipBack = new Timer(500, flipBack);

	// End variables

	// The method that starts it all
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}

	/**
	 * This method is used to set up the GUI by adding the buttons and giving
	 * them their cards and their ActionListeners
	 */
	@Override
	public void run() {
		JFrame frame = new JFrame("MemoryS (A Memory Game)");
		this.setLayout(new GridLayout(ROWS, COLS));
		Color[] choiceColors = new Color[] { Color.BLUE, Color.RED };
		String[] choices = new String[] { "BLUE", "RED" };
		int choice = JOptionPane.showOptionDialog(this,
				"Do you want to play with RED or BLUE?", "RED or BLUE?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				choices, null);
		sDeck = new Deck(choiceColors[choice]);
		Card[] cards = selectCards();
		for (int i = 0; i < cards.length; i++) {
			CardButton button = new CardButton(cards[i]);
			buttons[i] = button;
			add(button);
			button.addActionListener(this);
		}
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		tmrFlipBack.setRepeats(false);
	}

	/**
	 * Selects the cards to use and orders them randomly
	 * 
	 * @return An array containing a random order of cards
	 */
	public Card[] selectCards() {
		Card[] cards = new Card[ROWS * COLS];
		if (sDeck.getCount() < ROWS * COLS / 2)
			sDeck.shuffle();
		Card currentCard = sDeck.getCard();
		cards[0] = currentCard;
		cards[1] = currentCard;
		for (int i = 2; i < cards.length; i++) {
			if (i % 2 == 0)
				currentCard = sDeck.getCard();
			int rand = RNG.nextInt(i + 1);
			cards[i] = cards[rand];
			cards[rand] = currentCard;
		}
		return cards;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CardButton clicked = (CardButton) e.getSource();
		switch (state) {
		case 0:
			if (!clicked.isFaceUp()) {
				clicked.flip();
				selected1 = clicked;
				state = 1;
			}
			// Does nothing if face up.
			break;

		case 1:
			if (!clicked.isFaceUp()) {
				clicked.flip();
				selected2 = clicked;
				if (selected1.getCard() == selected2.getCard()) {
					state = 0;
					matches++;
					endGameCheck();
				} else {
					state = 2;
					tmrFlipBack.start();
				}
			}
			// Does nothing if face up.
			break;

		case 2:
			if (!clicked.isFaceUp()) {
				selected1.flip();
				selected2.flip();
				clicked.flip();
				selected1 = clicked;
				state = 1;
			} else {
				selected1.flip();
				selected2.flip();
				state = 0;
			}
			tmrFlipBack.stop();
			break;

		default:
			break;
		}

	}

	private void endGameCheck() {
		if (matches == matchTarget) {
			int playAgain = JOptionPane.showConfirmDialog(this,
					"YOU WON MemoryS!\nDo you want to play again?",
					"End Game, Play Again?", JOptionPane.YES_NO_OPTION);
			if (playAgain == JOptionPane.YES_OPTION) {
				replay();
			} else {
				System.exit(0);
			}
		}
	}

	private void replay() {
		Color[] choiceColors = new Color[] { Color.BLUE, Color.RED };
		String[] choices = new String[] { "BLUE", "RED" };
		int choice = JOptionPane.showOptionDialog(this,
				"Do you want to play with RED or BLUE?", "RED or BLUE?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				choices, null);
		sDeck = new Deck(choiceColors[choice]);
		Card[] cards = selectCards();
		for (int i = 0; i < cards.length; i++) {
			buttons[i].setCard(cards[i]);
		}
		selected1 = null;
		selected2 = null;
		matches = 0;
	}

}
