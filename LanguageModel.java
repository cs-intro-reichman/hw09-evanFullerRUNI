import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		String frame = "";
        char c;
        In in = new In(fileName);
        List probs = new List();

        // Read in the first windowLength characters
        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) {
                c = in.readChar();
                frame += c;
            }
        }

        // Read in the rest of the file and update the model
        while (!in.isEmpty()) {
            c = in.readChar();

            if (this.CharDataMap.containsKey(frame)) {
                probs = this.CharDataMap.get(frame);
            } else {
                probs = new List();
            }

            probs.update(c);
            this.CharDataMap.put(frame, probs);
            frame = frame.substring(1) + c;
        }

        for (List prob : CharDataMap.values()) {
            calculateProbabilities(prob);
        }
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {				
		double total = 0;
        double p, cp = 0;
        Node curr = probs.getFirstNode();

        while (curr != null) {
            total += curr.cp.count;
            curr = curr.next;
        }

        curr = probs.getFirstNode();

        while (curr != null) {
            p = curr.cp.count / total;
            cp += p;
            curr.cp.p = p;
            curr.cp.cp = cp;
            curr = curr.next;
        }
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
		Node curr = probs.getFirstNode();
        double rand = randomGenerator.nextDouble();

        while (curr != null) {
            if (rand < curr.cp.cp) {
                return curr.cp.chr;
            }

            curr = curr.next;
        }

        return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength || !this.CharDataMap.containsKey(initialText.substring(initialText.length() - windowLength))) {
            return initialText;
        }

        String text = initialText;
        String frame = initialText;
        char c;

        for (int i = 0; i < textLength; i++) {
            c = this.getRandomChar(this.CharDataMap.get(frame));
            text += c;
            frame = frame.substring(1) + c;
        }

        return text;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    // creates a new LanguageModel object, trains it, and returns it
    public static void main(String[] args) {
		int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int textLength = Integer.parseInt(args[2]);
        Boolean seed = args[3].equals("random");
        String fileName = args[4];

        LanguageModel langModel;

        if (seed) {
            langModel = new LanguageModel(windowLength);
        } else {
            langModel = new LanguageModel(windowLength, 20);
        }

        langModel.train(fileName);
        System.out.println(langModel.generate(initialText, textLength));
    }
}
