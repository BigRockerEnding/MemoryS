package org.wintresstech.seh.memorys;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.wintrisstech.cards.Card;

@SuppressWarnings("serial")
public class CardButton extends JButton {
	private Card card;
	private boolean faceUp = false;
	private ImageIcon face, back;

	public CardButton(Card c) {
		this.card = c;
		this.face = new ImageIcon(card.getFaceImage());
		this.back = new ImageIcon(card.getBackImage());
		this.setIcon(back);
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	public void flip() {
		faceUp = !faceUp;
		setIcon(faceUp ? face : back);
	}

	public void setCard(Card c) {
		card = c;
		face = new ImageIcon(card.getFaceImage());
		back = new ImageIcon(card.getBackImage());
		setIcon(back);
		faceUp = false;
	}

	public Card getCard() {
		return card;
	}
}
