import java.util.Random;

public class Gene {
	
	private char element;
	public static final int DEFAULT_BOUND = 10;
	
	public Gene() {
		element = '0';
	}
	
	public Gene(char e) {
		element = e;
	}
	
	public char getContents() {
		return element;
	}
	
	public void setContents(char e) {
		element = e;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(1);
		
		sb.append(element);
		return sb.toString();
	}
	
	public static char generateSimpleGene() {
		Random rand = new Random();
		int generated;
		char genedCharacter;
		
		generated = rand.nextInt(DEFAULT_BOUND) + '0';	
		genedCharacter = (char) generated;
		return genedCharacter;
	}
	
	public static char generateSimpleGene(int boundary) {
		Random rand = new Random();
		int generated;
		char genedCharacter;
		
		generated = rand.nextInt(boundary) + '0';	
		genedCharacter = (char) generated;
		return genedCharacter;
	}
	
	public static char generateComplexGene() {
		Random rand = new Random();
		int generated;
		char genedCharacter;
		generated = rand.nextInt(36);

		if (generated > 9) {
			generated = generated + 87;
		}
		else {
			generated = generated + '0';
		}

		genedCharacter = (char) generated;
		
		return genedCharacter;
	}
}