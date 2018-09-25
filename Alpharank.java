import java.util.*;
import java.io.*;
import java.math.BigInteger;

public class Alpharank{
    
    //This hashmap will be used to keep track of which characters are contained in the input string, along with their number of occurences
    private HashMap<Character, Integer> index;
    
    //A lookup table containing factorials between 0! and 30!. I chose to store them as Strings because factorials greater then 20! exceed the limit for a signed 64-bit integer
    public static final String factorialLookup[]= {"1", "1", "2", "6", "24", "120", "720", "5040", "40320", "362880", "3628800", "39916800", "479001600", "6227020800", "87178291200", "1307674368000", "20922789888000", "355687428096000", "6402373705728000", "121645100408832000", "2432902008176640000", "51090942171709440000", "1124000727777607680000", "25852016738884976640000", "620448401733239439360000", "15511210043330985984000000", "403291461126605635584000000", "10888869450418352160768000000", "304888344611713860501504000000", "8841761993739701954543616000000", "265252859812191058636308480000000"};
    
    //This method retrives the factorial of a given integer between 0 and 30 using the lookup table above
    public static String factorial(int n){
        if (n>30){
            throw new RuntimeException("The number input for factorial computation must be between 0 and 30");
        }
        return factorialLookup[n];
    }
    
    //This method computes a multinomial coefficent given the top term and the list of bottom terms.
    //This is computed by taking the factorial of the top term and dividing the result by the factorial of each bottom term
    public static BigInteger computeMultinomial(int topTerm, List<Integer> bottomTerms){
        BigInteger result = new BigInteger(factorial(topTerm));
        for (Integer r : bottomTerms){
            result = result.divide(new BigInteger(factorial(r)));
        }
        return result;
    }
    
    //This method initializes a HashMap, iterates through the characters in the given string, and maps each character found to its number of occurences
    private void buildIndex(String s){
        index = new HashMap<Character, Integer>();
        for (int i=0; i<s.length(); i++){
            if (s.charAt(i)<'A' || s.charAt(i)>'Z'){
                throw new RuntimeException("The input string can only contain capital letters('A'-'Z')");
            }
            if (index.containsKey(s.charAt(i))){
                // if the character exists in the hashMap, we increment the number of occurences
                index.put(s.charAt(i), index.get(s.charAt(i))+1);
            } else {
                // if the character does not exist in the hashMap, we add it and set the number of occurences to one
                index.put(s.charAt(i), 1);
            }
        }
    }
    
    //This method returns the number of unique string permutations of the characters in the HashMap "index"
    private BigInteger numUniqueWords(){
        int totalNumberCharacters=0;
        ArrayList<Integer> duplicateCharQuantities = new ArrayList<Integer>();
        for (Map.Entry<Character,Integer> c : index.entrySet()){
            totalNumberCharacters += c.getValue();
            if (c.getValue()>1){
                duplicateCharQuantities.add(c.getValue());
            }
        }
        return computeMultinomial(totalNumberCharacters,duplicateCharQuantities);
    }
    
    //This method computes the number for the given word. My algorithm is explained in further detail in the readme
    private BigInteger computeRank(String word){
        BigInteger rank= new BigInteger("1");
        for (int i=0; i < word.length()-1; i++){
            for (char c='A';c<'Z'; c++){
                if (index.containsKey(c)) {
                    int count = index.get(c);
                    if (c == word.charAt(i)){
                        if (count >1){
                            index.put(c, count-1);
                        }
                        else if (count == 1){
                            index.remove(c);
                        }
                        break;
                    }
                    if (count >1){
                        index.put(c, count-1);
                    }
                    else if (count == 1){
                        index.remove(c);
                    }
                    rank = rank.add(numUniqueWords());
                    index.put(c, count);
                }
            }
        }
        return rank;
    }
    
    public static void main(String args[]){
        //Error handling
        if (args.length<1){
            System.out.println("\nUSAGE:\njava Alpharank <INPUT STRING>\n");
            throw new RuntimeException("An input string is required as an argument");
            
        }
        if (args[0].length() > 30){
            System.out.println("ERROR: The length of the input string must be less then or equal to 30 characters");
        }
        
        //Create an instance of the class
        Alpharank instance = new Alpharank();

        //Build an index of the characters in the given string, mapped to the number of occurences.
        instance.buildIndex(args[0]);
        
        //Compute the number for the given word, and print it
        System.out.println(instance.computeRank(args[0]));
    }
}
