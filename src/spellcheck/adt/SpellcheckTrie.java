package spellcheck.adt;

import spellcheck.rule.CaseRule;
import spellcheck.rule.RepetitionRule;
import spellcheck.rule.SpellcheckRule;
import spellcheck.rule.VowelRule;

import java.util.*;

public class SpellcheckTrie {

    private static final List<SpellcheckRule> PRE_PROCESSING_RULES = Arrays.asList(new CaseRule(), new VowelRule());
    private static final List<SpellcheckRule> MATCHING_RULES = Arrays.asList(new CaseRule(), new VowelRule(), new RepetitionRule());

    private final SpellcheckTrieNode root;

    public SpellcheckTrie() {
        root = SpellcheckTrieNode.newRootNode();
    }

    public SpellcheckTrieNode insert(String word) {
        String processedWord = applyRules(PRE_PROCESSING_RULES, word);

        SpellcheckTrieNode node = root;
        for (int i = 0; i < processedWord.length(); i++) {
            node = node.addLetter(processedWord.charAt(i));
        }
        return node.terminate(word);
    }

    public List<String> findMatches(String word) {
        String processedWord = applyRules(MATCHING_RULES, word);

        Set<SpellcheckTrieNode> nodesToExplore = new HashSet<SpellcheckTrieNode>(Arrays.asList(root));
        for (int i = 0; i < processedWord.length(); i++) {

            char nextChar = processedWord.charAt(i);
            int repetitions = getNumCharacterRepetitions(processedWord, i);

            nodesToExplore = descendTrie(nodesToExplore, nextChar);

            // Add all potential repeated states to our discovery list.
            // For example, if we are trying to match "sheep", then we must explore
            // she* as well as shee* since the "e" may have been incorrectly repeated.
            // As a result, this trie acts more like an NFA - we must keep track of multiple states
            Set<SpellcheckTrieNode> repeatedNodes = nodesToExplore;
            if (repetitions > 1) {
                for (int j = 1; j < repetitions; j++) {
                    repeatedNodes = descendTrie(repeatedNodes, nextChar);
                    nodesToExplore.addAll(repeatedNodes);
                }
                i += (2 + String.valueOf(repetitions).length());
            }
        }

        List<String> matchedWords = new ArrayList<String>();
        for (SpellcheckTrieNode node : nodesToExplore) {
            if (node.getWord() != null) {
                matchedWords.add(node.getWord());
            }
        }
        return matchedWords;

    }

    private String applyRules(List<SpellcheckRule> rules, String word) {
        String processedWord = word;
        for (SpellcheckRule rule : rules) {
            processedWord = rule.applyRule(processedWord);
        }
        return processedWord;
    }

    private Set<SpellcheckTrieNode> descendTrie(Set<SpellcheckTrieNode> nodesToExplore, char c) {
        Set<SpellcheckTrieNode> newNodesToExplore = new HashSet<SpellcheckTrieNode>();
        for (SpellcheckTrieNode node : nodesToExplore) {
            SpellcheckTrieNode followedNode = node.follow(c);
            if (followedNode != null) {
                newNodesToExplore.add(followedNode);
            }
        }
        return newNodesToExplore;
    }

    private int getNumCharacterRepetitions(String processedWord, int index) {
        int repetitions = 1;
        if (index + 1 < processedWord.length() && Character.valueOf('+').
                compareTo(Character.valueOf(processedWord.charAt(index + 1))) == 0) {
            int startIndex = index + 2;
            int endIndex = index + 3;
            while (Character.valueOf('+').compareTo(Character.valueOf(processedWord.charAt(endIndex))) != 0) {
                endIndex++;
            }
            repetitions = Integer.parseInt(processedWord.substring(startIndex, endIndex));
        }
        return repetitions;
    }
}
