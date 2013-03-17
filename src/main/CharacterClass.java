package main;

/**
 * A class that represents a character class 
 *
 */
public class CharacterClass 
{

	private String regex;
	private CharacterClass superClass;
	
	public CharacterClass()
	{
	}
	public CharacterClass(String r, CharacterClass s)
	{
		regex = r;
		superClass = s;
	
	}
	
	public String stringRepresentation()
	{
		if (superClass == null)
		{
			return regex;
		}
		else
		{
			return "(?="+regex+")"+superClass.stringRepresentation();
		}
	}
	
	public boolean matches(String other)
	{
		return other.matches(regex);
	}
	
	
}
