package spellcheck.rule;

public class RepetitionRule implements SpellcheckRule {
    @Override
    public String applyRule(String s) {
        StringBuilder transformedString = new StringBuilder();
        int windowStart = 0;
        int windowEnd = 0;
        while (windowStart < s.length()) {
            windowEnd = windowStart;
            while (windowEnd < s.length() && s.charAt(windowEnd) == s.charAt(windowStart)) {
                windowEnd++;
            }

            int numOccurrences = windowEnd - windowStart;
            transformedString.append(s.charAt(windowStart));
            if (numOccurrences > 1) {
                transformedString.append("+").append(numOccurrences).append("+");
            }

            windowStart = windowEnd;
        }
        return transformedString.toString();
    }
}
