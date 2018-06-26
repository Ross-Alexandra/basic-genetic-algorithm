import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

public class Species {
   /*
	*	Attributes:
	*		species: Organism array to hold the organisms that make up the species.
	*		population: number of organisms in the species.
	*		isBounded: whether or not the species is bounded in binary.
	*/
	private Organism[] species;
	private int population;
	private int GenNumber = 0;
	private boolean isBounded;
	
	private static final int DEFAULT_POPULATION = 500;
	/*----------------END OF ATTRIBUTES.
	*
	*
	*/
	
	/*
	*	Constructors
	*/
	public Species() {
		species = new Organism[DEFAULT_POPULATION];
		population = DEFAULT_POPULATION;
		
		for (int i = 0; i < population; i++) {
			species[i] = new Organism();
		}
	}
	
	public Species(int pop) {
		species = new Organism[pop];
		population = pop;
		
		for (int i = 0; i < population; i++) {
			species[i] = new Organism();
		}
	}
	
	public Species(boolean isBouneded) {	//if true, bounded as binary, if false, species becomes complex.
		this.isBounded = isBounded;
		population = DEFAULT_POPULATION;
		species = new Organism[population];
		
		for (int i = 0; i < population; i++) {
			if (!isBounded) {
				species[i] = new Organism(Organism.DEFAULT_GENOME_SIZE, false);
			}
			else {
				species[i] = new Organism(true);
			}
		}
	}
	
	public Species(boolean Bounded, int pop) {	//if true, bounded as binary, if false, species becomes complex.
		isBounded = Bounded;
		population = pop;
		species = new Organism[population];
		
		for (int i = 0; i < population; i++) {
			if (!Bounded) {
				species[i] = new Organism(Organism.DEFAULT_GENOME_SIZE, false);
			}
			else {
				species[i] = new Organism(true);
			}
		}
	}
	
	public Species(boolean Bounded, int pop, int genomeSize) {	//if true, bounded as binary, if false, species becomes complex.
		isBounded = Bounded;
		population = pop;
		species = new Organism[population];
		
		for (int i = 0; i < population; i++) {
			if (!Bounded) {
				species[i] = new Organism(genomeSize, false, (float)0);
			}
			else {
				species[i] = new Organism(genomeSize, true, (float)0);
			}
		}
	}
	
	public Species(int pop, int genomeSize) {
		population = pop;
		species = new Organism[population];
		
		for (int i = 0; i < population; i++) {
			species[i] = new Organism(genomeSize);
		}
	}
	
	public Species(int pop, int genomeSize, int strandLength) {
		population = pop;
		species = new Organism[population];
		
		for (int i = 0; i < population; i++) {
			species[i] = new Organism(genomeSize, strandLength);
		}
	}
	
	public Species(int pop, int genomeSize, boolean simple, int geneNumber) {
		population = pop;
		species = new Organism[population];
		
		for (int i = 0; i < population; i++) {
			species[i] = new Organism(genomeSize, simple, geneNumber);
		}
	}
	
	/*----------------END OF CONSTRUCTORS.
	*
	* Species psudeo:
	*	cycle {	-- HANDLED BY doGeneration 
		
	*		test for fitness {	-- HANDLED BY TEST FITNESS AND USER CHANGEABLE CODE.
	*			decode strands	-- SEE ABOVE
	*			
	*		}
	
	
	*		sort by fitness -- HANDLED BY SPECIES SORT WITH A MERGE SORT.
	
	*		kill off worst +/- random {	--ALL FUNCTIONS HANDLED BY survival();
	*			create int array with size .5 population.
	*			evaluate chance that each position is eliminated then roll -- CHANCE_OF_DEATH: (ORGANISM POSITION / POPULATIONN)
	*			for each to be killed, add its index to array
	*			when array is full, replace Organisms at each index with offspring of 2 radnom high-performers.
	*		}
	
	
	*		report based on user input -- NO USER INPUT TAKEN, REPLACED WITH REPORTING WITHIN DO GENERATION
	*		
	*	}
	*/
	
	public void doGeneration(int cycles, boolean quick) {
		for (int i = 0; i < cycles; i++) {
			//System.out.println("Testing Fitness.");
			if ( i == 0) {
				for (int k = 0; k < population; k++) {
					testFitness(species[k]);
				}
				
				Arrays.sort(species);	//implement your own damn merge sort here one day.
			}
			//System.out.println("\n\n" + this.toString());
			
			//System.out.println("Eliminating worst preformers & breeding their replacements");
			this.survival();
			
			int numNew = 0;
			for (int j = 0; j < population; j++) {
				if (species[j].getFitness() == Float.NEGATIVE_INFINITY) {
					numNew++;
				}
			}
			
			
			for (int k = 0; k < population; k++) {
				testFitness(species[k]);
			}
				
			Arrays.sort(species);
			
			StringBuilder sb = new StringBuilder(species[0].getGenomeSize() * 2);
			StringBuilder median = new StringBuilder(species[population/2].getGenomeSize() * 2);
		
		
			for (int j = 0; j < species[0].getGenomeSize(); j++) {
				sb.append(decode(species[0].getStrandAt(j).getStrand()));
				 if (j + 1 < species[0].getGenomeSize()) sb.append(",");
			}
			
			for (int j = 0; j < species[population/2].getGenomeSize(); j++) {
				median.append(decode(species[population/2].getStrandAt(j).getStrand()));
				 if (j + 1 <species[population/2].getGenomeSize()) median.append(",");
			}
			
			GenNumber++;
			
			try {
				if (!quick) Thread.sleep(14);
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			}
			catch (IOException | InterruptedException e) {
				System.err.println(e);
			}
			
			System.out.println("Current Generation: " + GenNumber);
			System.out.println("Numer of new Organisms: " + numNew);
			System.out.println();
			System.out.println("Current Best Organism: " + species[0].getGenome() + "\nIts decoded results are: " + sb.toString());
			System.out.println("Best final fitness is: " + species[0].getFitness());
			System.out.println();
			System.out.println("Current Median Organism: " + species[population/2].getGenome() + "\nIts decoded results are: " + median.toString());
			System.out.println("Median final fitness is: " + species[population/2].getFitness());
			
		}
	}
	
	public int getGeneration() {
		return GenNumber;
	}
	
	private void survival() {
		Random rand = new Random();
		int weakest = 0;
		int selected = 0;
		float found = 0;
		if (population % 2 == 0) {
			weakest = population /2;
		}
		else {
			weakest = (population - 1) / 2;
		}
		
		for (int i = population -1; i >= 0; i--) {
			found = rand.nextFloat();
			
			if ((((float)i/((float)population -1)) >= found) && (selected <= weakest)){
				
				species[i] = null;
				
				selected++;
				//System.out.println("This ran");
				
				/*if (species[i].bounded) {
					//species[i] = new Organism(true);
				}
				else {
					//species[i] = new Organism(species[i].getGenomeSize(), species[i].getOrganismComplexity(), species[i].getGeneComplexity());
				}*/
			}
		}
		
		for (int i = 0; i < population; i++) {
			if (species[i] == null) {
				species[i] = breedMostFit(this);
				//species[i] = new Organism(true);
				//System.out.println("New Breed: " + species[i].toString());
			}
		}
		
	}
	
	private static Organism breedMostFit(Species s) {
		//using breed(mater, matee, Organism.boundedAt;
		//or using breed(mater, matee); if no bound is present.
		Random rand = new Random();
		float pop = s.getPopulation();
		float coef = (float)s.getPopulation() - 1;
		float draw;
		int[] breeding = {-1, -1};
		int selected = 0;
		
		while (breeding[1] == -1) {
			for (int i = 0; i < s.getPopulation(); i++, coef--) {
				draw = rand.nextFloat();
				
				//System.out.println("-----------------------------------------------" + coef/pop);
			
				if ((draw <= (coef / pop) / 3) && (selected < 2) && (s.getOrganismAt(i) != null)) {
					breeding[selected] = i;
					selected++;
				}
				
				if (coef == 0) {
					coef = (float) pop -1;
				}
			}
		}
		//System.out.println("-----------------------------------------------" + breeding[0]);
		//System.out.println("-----------------------------------------------" + breeding[1]);
		
		if (s.getOrganismAt(0).bounded) {
			return Organism.breed(s.getOrganismAt(breeding[0]), s.getOrganismAt(breeding[1]), s.getOrganismAt(0).boundedAt);
		}
		else {
			return Organism.breed(s.getOrganismAt(breeding[0]), s.getOrganismAt(breeding[1]));
		}
		
	}
	
	private void testFitness(Organism org) {
		StringBuilder sb = new StringBuilder(org.getGenomeSize());
		
		for (int i = 0; i < org.getGenomeSize(); i++) {
			sb.append(decode(org.getStrandAt(i).getStrand()));
		}
		
		org.setFitness(assignFitness(sb.toString()));
	}
	
	public Organism getOrganismAt(int pos) throws SpeciesException {
		if ((pos < 0) || (pos >= population)) {
			throw new SpeciesException("Tried to find organism at location: " + pos + " in a species with a population of " + population);
		}
		
		return species[pos];
	}
	
	public Organism setOrganismAt(int pos) throws SpeciesException {
		if ((pos < 0) || (pos >= population)) {
			throw new SpeciesException("Tried to replace organism at location: " + pos + " in a species with a population of " + population);
		}
		
		return species[pos];
	}
	
	public int getPopulation() {
		return population;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < population; i++) {
			sb.append((i + 1) + ": " + species[i].getFitness() + "\n");
		}
		
		return sb.toString();
	}
	
	public String toStringComplex() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < population; i++) {
			sb.append("Organism ");
			sb.append((i+1));
			sb.append(": ");
			sb.append(species[i].getGenome());
			sb.append(", just bred: ");
			sb.append(species[i].getFitness() == Float.NEGATIVE_INFINITY);
			sb.append("\n");
			
		}
		
		return sb.toString();
	}
	
	/*
			ATTENTION:	---------------------------------------------------------------------------------------------------------------------------------------------------
			The methods that exist below this line are those than MUST be changed across each 
			successive test. 
			
			The decode method is responsibe for the interpretation of the 
			strands of genes within each organism. 
			
			assignFitness is
			the code that will return a number to be given as the fitness for each 
			Organism within the population. This is based on a string that represents
			a decoded (per YOUR decode code) version of each organism's genome.
			
			NOTE: EACH STRAND IN THE FINAL DECODED GENOME WILL BE COMMA SEPERATED.
			
			EXAMPLE:
			if 00 = 1, 01 = 2, 10 = 3, 11 = 4
			
			and the organism being looked at has the genome 001011 then:
				the decode method will take run 3 times, once for 00, once for 10 and once for 11
				if the decode method works correctly, it will return first 1, then 3 then 4.
				
				FROM HERE, the assignFitness method will be given the string "1,3,4" and must interprate
				this data however it will in order to return a fitness score.
	*/
	
	private static String decode(String inputStrand) {	//decodes the String containing a strand, this strand will be part of the currently looked at organism.
		String[][] codex = 	{{"0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101"},
							 {"1"	, "2"	, "3"	, "4"	, "5"	, "6"	, "7"	, "8"	, "9"	, "+"	, "-"	,	"*"	, "/"}};
		
		for (int i = 0; i < codex[0].length; i++) {
			if (codex[0][i].equals(inputStrand)) {
				//System.out.println(codex[1][i] + "----------aaaaaaaaa");
				return codex[1][i];
			}
		}
		
		//System.out.println(inputStrand + "----------------------------------");
		
		return "F";
	}
	
	private static float assignFitness(String decodedGenome) {	//assigns a fitness to the currently looked at Organism, based on its decoded genome.
		int value = 0;
		int i;
		float fitness = Float.POSITIVE_INFINITY;
		
		boolean currentlySymbol = true;
		int nextSymbol = -1; //-1 means none, 0 is +, 1 is -, 2 is *, 3 is 
		int nextNumber = -1;
		
		for (i = 0; ((i < decodedGenome.length()) && (!Character.isDigit(decodedGenome.charAt(i)))); i++) {
			//do nothing
		}
		if (decodedGenome.length() == i) {
			return fitness;
		}
		value = Character.getNumericValue(decodedGenome.charAt(i));
		
		while (i < decodedGenome.length()) {
			
			while (decodedGenome.charAt(i) == 'F') {
				i++;
				if (i >= decodedGenome.length()) break;
			}
			
			if (i >= decodedGenome.length()) break;
			
			if (currentlySymbol) {
				if (decodedGenome.charAt(i) == '+') {
					nextSymbol = 0;
				}
				else if (decodedGenome.charAt(i) == '-') {
					nextSymbol = 1;
				}
				else if (decodedGenome.charAt(i) == '*') {
					nextSymbol = 2;
				}
				else if (decodedGenome.charAt(i) == '/') {
					nextSymbol = 3;
				}
			}
			else {
				if (Character.isDigit(decodedGenome.charAt(i))) {
					nextNumber = Character.getNumericValue(decodedGenome.charAt(i));
				}
			}
			
			if ((nextSymbol != -1) && (currentlySymbol)){
				currentlySymbol = false;
			}
			else if (nextNumber != -1) {
				
				if (nextSymbol == 0) {
					value += nextNumber;
				}
				else if (nextSymbol == 1) {
					value -= nextNumber;
				}
				else if (nextSymbol == 2) {
					value *= nextNumber;
				}
				else if (nextSymbol == 3) {
					value /= nextNumber;
				}
				
				nextNumber = -1;
				currentlySymbol = true;
				nextSymbol = -1;
			}
			
			i++;
		}
		
		return Math.abs(91 - value) * -1;
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int option = 1;
		Species myS = new Species(true, 100, 10);

		while (option != -1) {
			
			if (option == -2) {
				myS = new Species(true, 100, 10);
				option = 1;
			}
			myS.doGeneration(option, false);
			
			System.out.println("\nPlease enter the number of generations to simulate, or -1 to exit, or -2 to restart.");
			option = in.nextInt(); 
		}
		
	}

}