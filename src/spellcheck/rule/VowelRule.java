package spellcheck.rule;

public class VowelRule implements SpellcheckRule {
    @Override
    public String applyRule(String s) {
        return s.replaceAll("a|e|i|o|u", "*");
    }
}
