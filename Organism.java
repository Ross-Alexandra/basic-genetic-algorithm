import java.util.Random;

public class Organism implements Comparable {
	//attributes-----------------------------------------------------------------------------------------------------
	private Strand[] genome;
	
	private int genomeSize = 4;
	private int geneComplexity = 4;
	private boolean simpleOrganism = true;
	public boolean bounded;
	public static final int boundedAt = 2;
	
	private float fitness = Float.NEGATIVE_INFINITY;
	private static final double mutationRate = .001;
	public static final int DEFAULT_GENOME_SIZE = 4;
	public static final int DEFAULT_GENE_COMPLEXITY = 4;
	public static final boolean DEFAULT_SIMPLE_ORGANISM = true;
	//attributes-----------------------------------------------------------------------------------------------------
	
	//constructors---------------------------------------------------------------------------------------------------
	public Organism() {	//Defaults
		genome = new Strand[genomeSize];
		for (int i = 0; i < genomeSize; i++) {
			genome[i] = new Strand(geneComplexity, simpleOrganism);
			genome[i] = Strand.generateRandomStrand(geneComplexity, simpleOrganism);
		}
	}
	
	public Organism(int size) { //adjust genomeSize
		genomeSize = size;
		genome = new Strand[genomeSize];
		for (int i = 0; i < genomeSize; i++) {
			genome[i] = new Strand(geneComplexity, simpleOrganism);
			genome[i] = Strand.generateRandomStrand(geneComplexity, simpleOrganism);
		}
	}
	
	public Organism(int size, int complexity) {
		genomeSize = size;
		geneComplexity = complexity;
		genome = new Strand[genomeSize];
		for (int i = 0; i < genomeSize; i++) {
			genome[i] = new Strand(geneComplexity, simpleOrganism);
			genome[i] = Strand.generateRandomStrand(geneComplexity, simpleOrganism);
		}
	}
	
	public Organism(int size, boolean isNotComplex) { //adjust genomeSize and simpleOrganism.
		genomeSize = size;
		simpleOrganism = isNotComplex;
		genome = new Strand[genomeSize];
		for (int i = 0; i < genomeSize; i++) {
			genome[i] = new Strand(geneComplexity, simpleOrganism);
			genome[i] = Strand.generateRandomStrand(geneComplexity, simpleOrganism);
		}
	}
	
	public Organism(int size, boolean isComplex, int complexity) {	//adjust genomeSize, simpleOrganism, and geneComplexity
		genomeSize = size;
		simpleOrganism = isComplex;
		geneComplexity = complexity;
		genome = new Strand[genomeSize];
		for (int i = 0; i < genomeSize; i++) {
			genome[i] = new Strand(geneComplexity, simpleOrganism);
			genome[i] = Strand.generateRandomStrand(geneComplexity, simpleOrganism);
		}
	}
	
	public Organism(boolean isComplex, int complexity) {//adjust simpleOrganism and geneComplexity
		simpleOrganism = isComplex;
		geneComplexity = complexity;
		genome = new Strand[genomeSize];
		for (int i = 0; i < genomeSize; i++) {
			genome[i] = new Strand(geneComplexity, simpleOrganism);
			genome[i] = Strand.generateRandomStrand(geneComplexity, simpleOrganism);
		}
	} 
	
	public Organism(int size, boolean isBounded, float bin) {	//adjust simpleOrganism	-- NOTE BIN WILL NEVER BE USED.
		simpleOrganism = isBounded;
		bounded = isBounded;
		genomeSize = size;
		genome = new Strand[genomeSize];
		for (int i = 0; i < genomeSize; i++) {
			genome[i] = new Strand(geneComplexity, simpleOrganism);
			if (isBounded) genome[i] = Strand.generateRandomStrand(geneComplexity, boundedAt);
			else {
				genome[i] = Strand.generateRandomStrand(geneComplexity, simpleOrganism);
			}
		}
	}
	
	public Organism(boolean isBounded) {	//adjust simpleOrganism
		simpleOrganism = isBounded;
		bounded = isBounded;
		genome = new Strand[genomeSize];
		for (int i = 0; i < genomeSize; i++) {
			genome[i] = new Strand(geneComplexity, simpleOrganism);
			if (isBounded) genome[i] = Strand.generateRandomStrand(geneComplexity, boundedAt);
			else {
				genome[i] = Strand.generateRandomStrand(geneComplexity, simpleOrganism);
			}
		}
	}
	//constructors---------------------------------------------------------------------------------------------------
	
	/*
	An organism must be able to:
		breed.
		mutate.
		Tell us what it's genome is.
		Convert itself to a string.
		read and write each strand.
		read and write it's fitness score.
		
	*/
	
	public static Organism duplicate(Organism d) {
		Organism r = new Organism(d.getGenomeSize(), d.getOrganismComplexity(), d.getGeneComplexity());
		for (int i = 0; i < d.getGenomeSize(); i++) {
			r.setStrandAt(i, Strand.duplicate(d.getStrandAt(i)));
		}
		
		return r;
	}
	
	
	public static Organism breed(Organism mater, Organism matee) {
		int swap = crossoverPos(mater);
		if (swap == -1) {
			swap = mater.getGenomeSize() - 1;
			//System.out.println("----------: " + swap);
		}
		Organism offSpring = Organism.duplicate(matee);
		
		for (int i = 0; i < offSpring.genomeSize; i++) {
			for (int j = 0; j < offSpring.geneComplexity; j++, swap--) {
				offSpring.getStrandAt(i).setGeneAt(j, mater.getStrandAt(i).getGeneAt(j));
				if (swap == 0) {
					i = offSpring.genomeSize;
					j = offSpring.geneComplexity;
				}
			}
		}
	
		offSpring.mutate(Gene.DEFAULT_BOUND);
		
		offSpring.setFitness(Float.NEGATIVE_INFINITY);
		
		return offSpring;
	}
	
	public static Organism breed(Organism mater, Organism matee, int Bound) {
		if (!mater.getOrganismComplexity()) {
			return breed(mater, matee);
		}
		else {
			int swap = crossoverPos(mater);
			
			if (swap == -1) {
				swap = mater.getGenomeSize() - 1;
			}
			
			//System.out.println("----------: " + swap);
			
			Organism offSpring = Organism.duplicate(matee);
			
			for (int i = 0; i < swap; i++) {
				for (int j = 0; j < offSpring.geneComplexity; j++) {
					offSpring.getStrandAt(i).setGeneAt(j, mater.getStrandAt(i).getGeneAt(j));
				}
			}
			for (int i = swap; i < offSpring.genomeSize; i++) {
				for (int j = 0; j < offSpring.geneComplexity; j++) {
					offSpring.getStrandAt(i).setGeneAt(j, matee.getStrandAt(i).getGeneAt(j));
				}
			}
			
			offSpring.mutate(Bound);
		
			offSpring.setFitness(Float.NEGATIVE_INFINITY);
		
			return offSpring;
		}
	}
	
	private static int crossoverPos(Organism mater) {
		Random rand = new Random();
		int draw;
		int crossoverPosition= -1;
		double eachPercent = (1/((double)mater.genomeSize)) * 100;
		int testAgainst = 100 - (int)eachPercent;
		int loops = 0;
		
		for (int i = 0; i < (mater.genomeSize); i++) {
			draw = rand.nextInt(101);
			if (draw >= testAgainst) {
				crossoverPosition = i;
				break;
			}
			loops++;
		}
		
		//System.out.println("--------------CROSSOVER POSITION: " + crossoverPosition + " ----------- testAgainst: " + testAgainst + " -------------eachPercent: " + eachPercent);
		//System.out.println("--------------NUMBER OF LOOPS TO OBTAIN ABOVE: " + loops);

		return crossoverPosition;
	}
	
	public int compareTo(Object other) throws ClassCastException
	{
		if (!(other instanceof Organism)) {
			throw new ClassCastException("A type of Organism was expected.");
		}
		
		if (this.getFitness() > ((Organism)other).getFitness()) {
			return -1;
		}
		else if (this.getFitness() == ((Organism)other).getFitness()) {
			return 0;
		}
		else {
			return 1;
		}
	}
	
	public void mutate(int bound) {
		if (bound == Gene.DEFAULT_BOUND) {
			mutate();
		}
		else { 
			Random rand = new Random();
			int selector;
			int sig = rand.nextInt(1001);
			
			if (sig == 520) {
				this.geneAddition(bound);
			}
			
			else {
				for (int i = 0; i < genomeSize; i++) {
					for (int j = 0; j < geneComplexity; j++) {
						selector = rand.nextInt(1001);
						if (selector == 520) {
							this.geneSubstitution(bound, i, j);
						}
					}
				}
			}
		}
	}
	
	public void mutate() {
		Random rand = new Random();
		int selector;
		int sig = rand.nextInt(1001);
			
		if (sig == 520) {
			this.geneAddition(Gene.DEFAULT_BOUND);
		}
		
		for (int i = 0; i < genomeSize; i++) {
			for (int j = 0; j < geneComplexity; j++) {
				selector = rand.nextInt(1001);
				if (selector == 520) {
					this.geneSubstitution(Gene.DEFAULT_BOUND, i, j);
				}
			
				
			}
		}
	}
	
	private void geneSubstitution(int bound, int i, int j) {
		//System.out.println("Substitution.");
		if (simpleOrganism) {
			genome[i].setGeneAt(j, Gene.generateSimpleGene(bound));
		}
		else {
			genome[i].setGeneAt(j, Gene.generateComplexGene());
		}
	}
	
	private void geneAddition(int bound) {
		//System.out.println("Addition");
		Random selector = new Random();
		int initPos = selector.nextInt(genomeSize - 1);
		String org = this.getGenome();
		int dist = initPos + 2;
		
		if (simpleOrganism) genome[initPos].setGeneAt(0, Gene.generateSimpleGene(bound));
		else {
			genome[initPos].setGeneAt(0, Gene.generateComplexGene());
		}
		
		for (int i = initPos+1; i < genomeSize; i++) {
			for (int j = 0; j < geneComplexity; j++, dist++) {
				if (dist == org.length()) {
					break;
				}
				genome[i].setGeneAt(j, org.charAt(dist));
				
			}
			if (dist == org.length()) {
					break;
			}
		}
		
		//genome[genomeSize-1].setGeneAt(geneComplexity - 1, 0);
	}
	
	public String getGenome() {
		StringBuilder sb = new StringBuilder(genomeSize * geneComplexity);
		for (int i = 0; i < genomeSize; i++) {
			sb.append(genome[i].getStrand());
		}
		
		return sb.toString();
	}
	
	public Strand getStrandAt(int pos) throws OrganismException {
		if (pos >= genomeSize) {
			throw new OrganismException("Searching for Strand at position " + pos + " where Organism's genome is of size " + genomeSize);
		}
		
		return genome[pos];
	}
	
	private void setStrandAt(int pos, Strand setter) throws OrganismException {
		if (pos >= genomeSize) {
			throw new OrganismException("Attempt to set strand at position " + pos + " where Organism's genome is of size " + genomeSize);
		}
		
		if (setter.length() != geneComplexity) {
			throw new OrganismException("given strand has a greater gene length (" + setter.length() + ") than this organism allows (" + geneComplexity + ")");
		}
		
		if (setter.isNumeric() != simpleOrganism) {
			throw new OrganismException("given strand has the opposite complexity (" + setter.isNumeric() + ") than this organism (" + simpleOrganism + ")");
		}
		
		genome[pos] = setter;
	}
	
	public float getFitness() {
		return fitness;
	}
	
	public void setFitness(float score) {
		fitness = score;
	}
	
	public int getGenomeSize() {
		return genomeSize;
	}
	
	public int getGeneComplexity() {
		return geneComplexity;
	}

	public boolean getOrganismComplexity() {
		return simpleOrganism;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nNumber of strands in genome: ");
		sb.append(genomeSize);
		sb.append("\nTotal genes: ");
		sb.append(geneComplexity * genomeSize);
		sb.append("\nSimple Organism: ");
		sb.append(simpleOrganism);
		sb.append("\n\n");
		
		for (int i = 0; i < genomeSize; i++) {
			sb.append("Strand " + i + ": \n");
			sb.append(genome[i].toString());
			sb.append("\n\n");
		}
		
		sb.append("\nFull genome: ");
		
		for (int i = 0; i < genomeSize; i++) {
			sb.append(genome[i].getStrand());
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		/*
		needs testing:
			breed.
			mutate.
			getGenome.
			getStrand.
			setStrand.
		*/
		Organism myOrg = new Organism();
		Organism Org = new Organism();
		
		System.out.println("Priniting initial organism, along with test info");
		System.out.println(myOrg);
		System.out.println("Expected Genome: " + myOrg.getGenome());
		System.out.println("Expected Strand 0: " + myOrg.getStrandAt(0).getStrand());
		
		System.out.println("Changing Strand 0 to 1111");
		myOrg.setStrandAt(0, new Strand("1111"));
		System.out.println("Expect 1111: " + myOrg.getStrandAt(0).getStrand());
		
		System.out.println("\n\nNow testing throws");
		
		try {
			System.out.println("Testing set out of bounds:");
			myOrg.setStrandAt(500, new Strand("1111"));
		} 
		catch (OrganismException e) {
			System.err.println(e);
		}
		System.out.println();
		
		try {
			System.out.println("Testing get out of bounds: ");
			myOrg.getStrandAt(500);
		}
		catch (OrganismException e) {
			System.err.println(e);
		}
		System.out.println();
		
		try {
			System.out.println("Testing set has different complexity");
			myOrg.setStrandAt(0, new Strand("aaaa"));
		}
		catch (OrganismException e) {
			System.err.println(e);
		}
		System.out.println();
		
		try {
			System.out.println("Testing set has different length");
			myOrg.setStrandAt(0, new Strand("11111"));
		}
		catch (OrganismException e) {
			System.err.println(e);
		}
		System.out.println();
		
		System.out.println("\n all testing of basic functions complete.");
		
		Organism boundOrg = new Organism(true);
		
		System.out.println(boundOrg);
		
		boundOrg.setStrandAt(0, Strand.generateRandomStrand(boundOrg.geneComplexity, 2));
		
		System.out.println(boundOrg);
		
		System.out.println("------------Starting breeding Tests---------------");
		
		Organism alpha = new Organism(5, 10);
		Organism beta = new Organism(5, 10);
		Organism gamma = Organism.breed(alpha, beta);
		Organism hold = Organism.duplicate(gamma);
		System.out.println(gamma.toString());
		System.out.println(hold.toString());
		System.out.println(hold.toString().equals(gamma.toString()));
		int i = 0;
		
		//System.out.println(beta.toString().equals(hold.toString()));
		
		while (hold.toString().equals(gamma.toString())) {
			i++;
			
			gamma.mutate();
			System.out.println("mutate attempt: " + i);
			if (i == 10000) {
				System.out.println("Aborting mutate attempts");
				break;
			}
		}
		
		System.out.println(gamma.toString());
		/*
		Random genomes generated followed by them with single mutation
		
		 9805885254 1885601409 5847568422 4024749770 8921746048
		 9805885254 5885601409 5847568422 4024749770 8921746048
		 
		 7500633608 7064900156 6657075639 7507313052 9342114722
		 7500633608 7064900156 6657075639 7507313052 9142114722
		*/
		
		alpha = new Organism(5, 10);
		beta = new Organism(5, 10);
		gamma = Organism.breed(alpha, beta);
		
		System.out.println(alpha.getGenome());
		System.out.println(beta.getGenome());
		System.out.println(gamma.getGenome());
		
		/*
		
		a: 50319674546306116190015162684541531975730544421160
		g: 50319674546306116190015162684556855300986131853430
		
		b: 49570739112905303443827013566656855300986131853430
		g: 50319674546306116190015162684556855300986131853430
	
		   aaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbb
		g: 50319674546306116190015162684556855300986131853430
		*/
		
		alpha = new Organism(5, false, 10);
		beta = new Organism(5, false, 10);
		gamma = Organism.breed(alpha, beta);
		
		System.out.println(alpha.getGenome());
		System.out.println(beta.getGenome());
		System.out.println(gamma.getGenome());
		
		/*
		
		** a = father (gene), b = mother (gene), g = child, s = sequence, M = mutated gene
		a: 7r2br4xn9zlbtnrrx4h1oe6ybrg2xgho35t08o2kodv6jxnuv0
		g: 7r2br4xn9zlbtnrr215ikl7go5gbb8rnanmbx90gqowqs6jqrh
		
		b: 19j3ahjyb1c6sffx2s5ikl7go5gbb8rnanmbx90gqowqs6jqrh
		g: 7r2br4xn9zlbtnrr215ikl7go5gbb8rnanmbx90gqowqs6jqrh
		
		s: aaaaaaaaaaaaaaaabMbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb
		g: 7r2br4xn9zlbtnrr215ikl7go5gbb8rnanmbx90gqowqs6jqrh
		*/
		
		System.out.println("Attempting to find case of addition");
		
		gamma = new Organism();
		hold = Organism.duplicate(gamma);
		
		System.out.println("intial: " + gamma.getGenome());
		
		for (int r = 0; r < 1000; r++) {
			
			while (hold.toString().equals(gamma.toString())) {
				i++;
			
				gamma.mutate();
				//System.out.println("mutate attempt: " + i);
				if (i == 10000) {
					System.out.println("Aborting mutate attempts");
					break;
				}
			}
			
			System.out.println(gamma.getGenome());
			hold = Organism.duplicate(gamma);
		}		
	}
} 