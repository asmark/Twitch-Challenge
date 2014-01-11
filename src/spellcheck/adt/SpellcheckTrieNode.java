package spellcheck.adt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Asmar on 1/10/14.
 */
public class SpellcheckTrieNode {

    private static final char START_CHAR = '^';
    private static final char TERMINAL_CHAR = '$';

    private final char letter;
    private final SpellcheckTrieNode parent;
    private final Map<Character, SpellcheckTrieNode> children;

    // We don't really care about storing the word at each node. We only store this at the leaves, since the tree is full of gibberish anyways
    private String word;

    private SpellcheckTrieNode(SpellcheckTrieNode parent, char letter) {
        this.children = new HashMap<Character, SpellcheckTrieNode>();
        this.letter = letter;
        this.parent = parent;
        this.word = null;
    }

    public SpellcheckTrieNode addLetter(char letter) {
        SpellcheckTrieNode letterNode = children.get(letter);
        if (letterNode == null) {
            letterNode = new SpellcheckTrieNode(this, letter);
            children.put(letter, letterNode);
        }
        return letterNode;
    }

    public SpellcheckTrieNode terminate(String word) {
        SpellcheckTrieNode terminalNode = addLetter(TERMINAL_CHAR);
        terminalNode.word = word;
        return terminalNode;
    }

    public static SpellcheckTrieNode newRootNode() {
        return new SpellcheckTrieNode(null, START_CHAR);
    }

    public SpellcheckTrieNode follow(char c) {
        return children.get(c);
    }

    public String getWord() {
        // Follow a terminal character first, and then return the word if it exists
        SpellcheckTrieNode node = children.get(TERMINAL_CHAR);
        if (node != null) {
            return node.word;
        }
        return null;
    }
}
