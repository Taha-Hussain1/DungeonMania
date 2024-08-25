package dungeonmania.entities.logical;

public class LogicalRuleFactory {
    public static LogicalRule getRule(String ruleType) {
        switch (ruleType.toUpperCase()) {
            case "AND":
                return new AndRule();
            case "OR":
                return new OrRule();
            case "XOR":
                return new XorRule();
            case "CO_AND":
                return new CoAndRule();
            default:
            throw new IllegalArgumentException("Invalid rule type: " + ruleType);
        }
    }


}
