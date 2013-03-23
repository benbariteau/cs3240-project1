package main;

/**
 * A class that represents a character class
 */
public class CharacterClass {

	private String regex;
	private CharacterClass superClass;

	/**
	 * Default constructor
	 */
	public CharacterClass() {
	}

	/**
	 * Constructor that only accepts a regular expression
	 * 
	 * @param r		The regular expression that represents this character class
	 */
	public CharacterClass(String r) {
		regex = r;
		superClass = null;
	}

	/**
	 * Constructor that accepts a regular expression and
	 * 
	 * @param r		The regular expression that represents this character class
	 * @param s		The CharacterClass that represents this character class' superclass
	 */
	public CharacterClass(String r, CharacterClass s) {
		regex = r;
		superClass = s;

	}

	/**
	 * Returns the regular expression representing this character class.
	 * 
	 * @return regex the regular expression representing this character class.
	 */
	public String toString() {
		if (superClass == null) {
			return regex;
		} else {
			return "(?=" + regex + ")" + superClass.toString();
		}
	}

	/**
	 * Returns whether the parameter String matches this character class.
	 * 
	 * @param other		The parameter String to be matched to this character class
	 * @return matches 	A boolean that represents whether other fits in this character Class.
	 */
	public boolean matches(String other) {
		return other.matches(regex);
	}

}
