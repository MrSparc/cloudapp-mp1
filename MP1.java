import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        //TODO
        ArrayList<String> lines = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.inputFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        
        ArrayList<String> listStopWords = new ArrayList<String>(Arrays.asList(this.stopWordsArray));
        ArrayList<String> listWords = new ArrayList<String>();
        
        Map<String, Integer> wordCount = new HashMap<>();
        Integer n;
        
        for (Integer i : getIndexes()){
            // 1. Divide each sentence into a list of words using delimiters provided in the "delimiters" variable.
            StringTokenizer st = new StringTokenizer(lines.get(i), this.delimiters);
            while (st.hasMoreTokens()) {
                // 2. Make all the tokens lower-case and remove any tailing and leading spaces.
                String word = st.nextToken().toLowerCase().trim();
                     
                // 3. Ignore all common words provided in the "stopWordsArray" variable
                if (listStopWords.contains(word))
                    continue;
                     
                 n = wordCount.get(word);
                 n = (n == null) ? 1 : ++n;
                 wordCount.put(word, n);
            }
        }
        // Convert map to list of <String,Integer> entries
        List<Map.Entry<String, Integer>> list = 
            new ArrayList<Map.Entry<String, Integer>>(wordCount.entrySet());

        // Sort list by integer values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                // compare o2 to o1, instead of o1 to o2, to get descending freq. order
                int cmp = (o2.getValue()).compareTo(o1.getValue());
                if (cmp != 0)
                        return cmp;
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });

        // Populate the result into a list
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : list) {
                result.add(entry.getKey());
            //result.add( "(" + entry.getKey() + "," + entry.getValue() + ")");
        }
        
        for (int i=0; i < 20; ++i) {
                ret[i] = result.get(i);
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
