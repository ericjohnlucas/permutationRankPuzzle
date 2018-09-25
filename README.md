# Ranking permutations lexicographically

## Problem:
Consider a "word" as any sequence of capital letters A-Z (not limited to just "dictionary words"). For any word with at least two different letters, there are other words composed of the same letters but in a different order (for instance, STATIONARILY/ANTIROYALIST, which happen to both be dictionary words; for our purposes "AAIILNORSTTY" is also a "word" composed of the same letters as these two).

We can then assign a number to every word, based on where it falls in an alphabetically sorted list of all words made up of the same set of letters. One way to do this would be to generate the entire list of words and find the desired one, but this would be slow if the word is long.

Write a program which takes a word as a command line argument and prints to standard output its number. Do not use the method above of generating the entire list. Your program should be able to accept any word 25 letters or less in length (possibly with some letters repeated), and should use no more than 1 GB of memory and take no more than 500 milliseconds to run. Any answer we check will fit in a 64-bit integer.

Sample words, with their rank:
ABAB = 2
AAAB = 1
BAAA = 4
QUESTION = 24572
BOOKKEEPER = 10743

## My solution:
My algorithm is based on my observation of the following fact: Suppose we have a list of all
permutations of an input string in alphabetical order. Starting with the first word, we can systematically
"skip" through multiple words in this list(without enumerating them) until we find the input string. If we
count the number of strings that we skipped and add 1, we have our "rank" for the input word. This "rank"
where it falls in an alphabetically sorted list of all words made up of the same set of letters(our answer).

  - Here are the details of my algorithm:

      1. We create a HashMap and iterate through the input string. Each character found in the string is
      mapped to its number of occurrences.
      See method buildIndex(String s) in my source code, where this is implemented.

      2. We create a variable called "rank" which is initially set to 1.

      3. For each letter 'L' in the input string, we iterate though the letters in the HashMap in alphabetical
      order until we find L. For each letter 'C' we iterate through in HashMap which is NOT equal to L,
      we a) decrement the occurrence count of C in the HashMap, b) compute the number of distinct
      permutations of the remainder the the letters in the HashMap by computing the multinomial
      coefficient(from probability & combinatorics), c) add the result to the "rank" variable, and d) reincrement
      the character count of C in the HashMap.
      See the method computeRank() in my source code, where this algorithm is implemented.

      4. The final value of the variable "rank" is our answer.

        - My implementation relies on the computation of multinomial coefficients, which I implemented in the
        method computeMultinomial(int topTerm, List<Integer> bottomTerms)
        - I used the Java class java.math.BigInteger because some computations involve factorials which
        are too large to fit in a 64-bit integer.
        - My implementation of this problem does not involve factorials larger then 30! , thus I included a lookup
        table for factorials of all numbers between 0 and 30. This way these factorials can be retrieved in constant
        time complexity, which is much faster then actually computing them during runtime
