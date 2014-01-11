package spellcheck.rule;

public class CaseRule implements SpellcheckRule {

    @Override
    public String applyRule(String s) {
        return s.toLowerCase();
    }
}
