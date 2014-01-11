package spellcheck;

import spellcheck.adt.SpellcheckTrie;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String dictFile = args.length <= 0 || args[0] == null ? "words.txt" : args[0];
        SpellcheckTrie trie = constructTrie(dictFile);

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

    private static SpellcheckTrie constructTrie(String dictFile) throws IOException {
        BufferedReader dictFileReader = new BufferedReader(new FileReader(dictFile));
        SpellcheckTrie trie = new SpellcheckTrie();

        String word;
        while ((word = dictFileReader.readLine()) != null) {
            trie.insert(word);
        }
        dictFileReader.close();
        return trie;
    }

    private static String findBestWord(List<String> candidateWords, String misspelledWord) {
        // We can implement a distance-checking algorithm here to detect which of the words is "closest" to the misspelled word.
        // For now, just choose the first one
        // Also, this is not the correct place to put this function :)

        if (candidateWords.isEmpty()) {
            return "NO SUGGESTION";
        } else {
            return candidateWords.get(0);
        }
    }
}
