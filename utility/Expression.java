package utility;

import java.util.HashMap;

public class Expression {

	String expression;
	int position = -1, ch;
	String processedExpression;
	HashMap<String, Double> letters;
	
	public Expression(String str, HashMap<String, Double> letters) {
		expression = str;
		this.letters = letters;
	}

	/**
	 * Sets the character to the next character in the expression string (or -1
	 * if there is no next character) and increases <code>position</code> by 1.
	 */
	private void nextChar() {
		if (++position < expression.length())
			ch = expression.charAt(position);
		else
			ch = -1;
		// System.out.println("new character: " + (char) ch);
	}
	/**
	 * Compares the given character with the current character. The current
	 * character is consumed if it's the same.
	 * 
	 * @param chToCheck
	 *            The character to compare the current character to.
	 * @return <code>true</code> if the characters match, otherwise
	 *         <code>false</code>.
	 */
	private boolean consume(int chToCheck) {
		while (ch == ' ')
			nextChar();
		if (ch == chToCheck) {
			nextChar(); // Consume the character by moving to the next one
			return true;
		}
		return false;
	}

	// expression = term. or expression '+' term, or expression '-' term
	private double processExpression() {
		double d = processTerm();
		while (true) {
			if (consume('+'))
				d += processTerm();
			else if (consume('-'))
				d -= processTerm();
			else
				return d;
		}
	}

	// term = factor, or term '*' factor, or term '/' factor.
	private double processTerm() {
		double d = processFactor();
		while (true) {
			if (consume('*'))
				d *= processFactor();
			else if (consume('/'))
				d /= processFactor();
			else if(position < expression.length() && Character.isLetter(ch))
				d *= processFactor();
			else
				return d;
		}
	}

	// factor = '+' factor (positive) or '-' factor (negative), or '('
	// expression ')', or number (double), or factor^factor, or function(factor)
	// (e.g. sine, cosine, etc.)
	private double processFactor() {
		if (consume('+'))
			return processFactor();
		if (consume('-'))
			return -processFactor();
		
		double d = 0;
		int pos = position;
		
		if (consume('(')) { // new expression within the parentheses
			d = processExpression();
			consume(')');
		} else if(consume('|')) {
			d = Math.abs(processExpression());
			consume('|');
		} else if ((ch >= '0' && ch <= '9') || ch == '.') {// number
			while ((ch >= '0' && ch <= '9') || ch == '.')
				nextChar();
			d = Double.parseDouble(expression.substring(pos, position));
			if(consume('E')) {
				d *= Math.pow(10, processFactor());
			}
			//Letter, which is part of a variable or function
		} else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_') {
			while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_' || ch>='0' && ch<='9') nextChar();
			String letters = expression.substring(pos, position);
			if (this.letters.containsKey(letters)) {
				d = this.letters.get(letters).doubleValue();
			}
			else if(letters.equals("e"))
				d = Math.E;
			else if(letters.equals("pi"))
				d = Math.PI;
			else {
				d = processFactor(); // Gets the factor inside the function
				switch (letters) {
				case "sin":	
					d = Math.sin(d);
					break;
				case "sind":
					d = Math.sin(Math.toRadians(d));
					break;
				case "cos":
					d = Math.cos(d);
					break;
				case "cosd":
					d = Math.cos(Math.toRadians(d));
					break;
				case "tan":
					d = Math.tan(d);
					break;
				case "tand":
					d = Math.tan(Math.toRadians(d));
					break;
				case "sqrt":
					d = Math.sqrt(d);
					break;
				case "ln":
					d = Math.log(d);
					break;
				case "log":
					d = Math.log10(d);
					break;
				case "abs":
					d = Math.abs(d);
					break;
				}
			}
		}

		// If the factor has to be risen to a power
		if (consume('^'))
			d = Math.pow(d, processFactor());
		if (consume('%'))
			d %= processFactor();
		return d;
	}

	/**
	 * Processes the whole expression.
	 * 
	 * @return the arithmetic solution of the expression.
	 * @throws Exception 
	 */
	public double evaluateExpression() {
		nextChar();
		double val = processExpression();
		return val;
	}
}
