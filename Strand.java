import java.util.Random;

public class Strand{
	//-------Definition of simple organism is changed with these variables--------------------
	private boolean NumVnonNum = true;	//activates the changes.
	//-------Definition of simple organism is changed with these variables--------------------
	
	private Gene[] strand;
	private int strandLength = 4;
	private boolean fullNumeric = true;
	
	public Strand() {
		strand = new Gene[strandLength];
		for (int i = 0; i < strandLength; i++) {
			strand[i] = new Gene();
		}
	}
	
	public Strand(Strand s) {
		this(s.getStrand());
	}
	
	public Strand(int length) {
		strandLength = length;
		strand = new Gene[strandLength];
		for (int i = 0; i < strandLength; i++) {
			strand[i] = new Gene();
		}
	}
	
	public Strand(String s) {	//automatically takes the length of the given string as the length of the Strand.
		this(s.length());
		for (int i = 0; i < strandLength;  i++) {
			if ((!Character.isDigit(s.charAt(i))) && (NumVnonNum)) {
				fullNumeric = false;
				break;
			}
			/*else if ((int)s.charAt(i) > GreaterThanChar) {
				fullNumeric = false;
			}*/
		}
		this.setStrand(s);
	}
	
	public Strand(boolean full) {
		strand = new Gene[strandLength];
		fullNumeric = full;
		for (int i = 0; i < strandLength; i++) {
			strand[i] = new Gene();
		}
	}
	
	public Strand(int length, boolean full) {
		strandLength = length;
		fullNumeric = full;
		strand = new Gene[strandLength];
		for (int i = 0; i < strandLength; i++) {
			strand[i] = new Gene();
		}
	}
	
	//END OF CONSTRUCTORS--------------------------------------------------------------
	/*
	the following functions are of the Strand ADT
	*/
		
	public static Strand duplicate(Strand s) {
		return new Strand(s);
	}
		
	public static Strand generateRandomStrand(int length, boolean numeric) {
		Strand s = new Strand(length, numeric);
		
		if (!numeric) {
			for (int i = 0; i < length; i++) {
				s.setGeneAt(i, Gene.generateComplexGene());
			}
		}
		else {
			for (int i = 0; i < length; i++) {
				s.setGeneAt(i, Gene.generateSimpleGene());
			}
		}
		
		return s;
	}
	
	public static Strand generateRandomStrand(int length, int upperBound) {
		Strand s = new Strand(length, true);
		for (int i = 0; i < length; i++) {
			s.setGeneAt(i, Gene.generateSimpleGene(upperBound));
		}
		
		return s;
	}
	
	public int length() {
		return strandLength;
	}
	
	public boolean isNumeric() {
		return fullNumeric;
	}
	
	public void reset() {
		strand = new Gene[strandLength];
		for (int i = 0; i < strandLength; i++) {
			strand[i] = new Gene();
		}
	}
	
	public void setStrand(String s) throws invalidStrandException {
		Gene[] newGene = new Gene[strandLength];
		if (s.length() > strandLength) {
			throw new invalidStrandException("Strand passed of invalid length " + s.length() + ", expected length " + strandLength + ".");
		}
		for (int i = 0; i < strandLength; i++) {
			if ((!Character.isDigit(s.charAt(i))) && (fullNumeric)) {
				throw new invalidStrandException("strand passed " + s + " contains non-numeric being set to numeric strand.");
			}
			
			else {
				newGene[i] = new Gene(s.charAt(i));
			}
		}
		
		strand = newGene;
	}
	
	public String getStrand() {
		StringBuilder sb = new StringBuilder(strandLength);
		
		for (int i = 0; i < strandLength; i++) {
			sb.append(strand[i].getContents());
		}
		
		return sb.toString();
	}
	
	public String getGeneAt(int pos) throws invalidStrandException {
		if (pos >= strandLength) {
			throw new invalidStrandException("Invalid position " + pos + " requested from a strand of length " + strandLength);
		}
		
		return Character.toString(strand[pos].getContents());
	}
	
	public void setGeneAt(int pos, int newGene) throws geneException {
		if ((newGene > 9) || (newGene < 0)) {
			throw new geneException("number " + Integer.toString(newGene) + " given to a strand. strand's may only be of length 1, this has multiple digits.");
		}
		
		newGene = newGene + '0';
		
		char c = (char)newGene;
		
		strand[pos].setContents(c);
	}
	
	public void setGeneAt(int pos, String newGene) throws geneException {
		if ((!Character.isDigit(newGene.charAt(0))) && (fullNumeric)) {
			throw new geneException("Gene passed " + newGene + " is non-numeric and is being set to numeric Strand");
		}
		
		if (newGene.length() != 1) {
			throw new geneException("String of length " + newGene.length() + "given to a strand. strand's may only be of length 1.");
		}
		
		char internal = newGene.charAt(0);
		
		strand[pos].setContents(internal);
	}
	
	public void setGeneAt(int pos, char newGene) throws geneException {
		if ((!Character.isDigit(newGene)) && (fullNumeric)) {
			throw new geneException("Gene passed " + newGene + " is non-numeric and is being set to numeric Strand");
		}
		
		strand[pos].setContents(newGene);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(2*strandLength);
		
		sb.append("Strand Length: ");
		sb.append(strandLength);
		sb.append("\nStrand is numeric: ");
		sb.append(fullNumeric);
		
		for (int i = 0; i < strandLength; i++) {
			sb.append("\nGene number ");
			sb.append(i);
			sb.append("'s contents: ");
			sb.append(strand[i].getContents());
		}
		
		sb.append("\nCompiled Strand: ");
		sb.append(this.getStrand());
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Strand tester = new Strand("aaaa");
		System.out.println(tester.isNumeric());
		tester = new Strand("1111");
		System.out.println(tester.isNumeric());
		System.out.println("------------------------------------------------------------");
		Strand myStrand = new Strand();	//default length = 4;
		Strand testStrand = generateRandomStrand(6, false);
		
		System.out.println("Initial Test: ");
		System.out.println(myStrand.toString());
		System.out.println(myStrand.length());
		System.out.println();
		
		myStrand.setStrand("1111");
		System.out.println("Setting Strand to 1111");
		System.out.println(myStrand.toString());
		System.out.println();
		
		myStrand.reset();
		System.out.println("Resetting Strand");
		System.out.println(myStrand.toString());
		System.out.println();
		
		try {
			System.out.println("Oversized Strand test");
			myStrand.setStrand("11111");
		}
		catch (invalidStrandException e) {
			System.err.println(e);
		}
		System.out.println();
		
		try{
			System.out.println("Non-numeric Strand test");
			myStrand.setStrand("a101");
		}
		catch (invalidStrandException e) {
			System.err.println(e);
		}
		System.out.println();
		
		Strand Complex = new Strand(false);
		
		try {
			System.out.println("Non-numeric Strand test on non-numeric Strand");
			Complex.setStrand("zzzz");
		}
		catch (invalidStrandException e) {
			System.err.println(e);
		}
		System.out.println();
		
		System.out.println("Printing complex Strand");
		System.out.println(Complex.toString());
		
		System.out.println("Testing Strand constructors. *note, Strand() and Strand(boolean) already tested above.");
		
		Strand newStrand0 = new Strand(5);
		Strand newStrand1 = new Strand("10200");
		Strand newStrand2 = new Strand(6);
		Strand newStrand3 = new Strand("1030b");
		
		System.out.println("Expecting length of 5.");
		System.out.println(newStrand0.toString());
		System.out.println();
		
		System.out.println("Expecting Strand 10200.");
		System.out.println(newStrand1.toString());
		System.out.println();
		
		System.out.println("Expecting length of 6 AND non-numeric.");
		System.out.println(newStrand2.toString());
		System.out.println();	
		
		System.out.println("Expecting Strand 1030b.");
		System.out.println(newStrand3.toString());
		System.out.println();
		
		System.out.println("------Strand testing is done------");
		
		System.out.println("------Beggining strand testing------");
		myStrand.reset();
		
		System.out.println("Testing will be done of strand 0000");
		System.out.println();
		System.out.println("Getting at pos 0");
		System.out.println(myStrand.getGeneAt(0));
		System.out.println();
		System.out.println("Printing strand after setting at pos 0");
		myStrand.setGeneAt(0, 1);
		System.out.println(myStrand.toString());
		System.out.println();
		System.out.println("Setting strand to 1111 via setting of single strands.");
		for (int i = 0; i < 4; i++) {
			myStrand.setGeneAt(i, '1');
		}
		
		System.out.println(myStrand.toString());
		
		System.out.println("------ Beggining strand done ------");
		
		System.out.println(testStrand.toString());
		
		tester = new Strand("aaaa");
		System.out.println(tester.isNumeric());
	}
}