package spellcheck;

import spellcheck.adt.SpellcheckTrie;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(args[0]));
        SpellcheckTrie trie = new SpellcheckTrie();

        String word;
        while ((word = fileReader.readLine()) != null) {
            trie.insert(word);
        }
        fileReader.close();

        BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            String misspelledWord = stdinReader.readLine();
            if (misspelledWord == null) {
                break;
            }

            List<String> correctWords = trie.findMatches(misspelledWord);
            String result = findBestWord(correctWords, misspelledWord);
            System.out.println(result);
        }

    }

    private static String findBestWord(List<String> candidateWords, String misspelledWord) {
        // We can implement a distance-checking algorithm here to detect which of the words is "closest" to the misspelled word.
        // For now, just choose the first one
        // Also, this is not the correct place to put this function :)

        if (candidateWords.isEmpty()) {
            return "NO SUGGESTIONS";
        } else {
            return candidateWords.get(0);
        }
    }
}
