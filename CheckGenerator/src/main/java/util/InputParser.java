package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class InputParser {
    private InputParser(){}
    private static final String VALID_ID_AMOUNT_REGEX = "^\\d+-\\d+$";
    private static final String VALID_CARD_NUMBER_REGEX = "^card-\\d+$";

    public static Map<Integer,Integer> getIdsAndAmount(String[] args)  {
        Map<Integer,Integer> result = new HashMap<>();
        for (String arg : args) {
            String stringToParse=arg.trim();
            if (Pattern.matches(VALID_ID_AMOUNT_REGEX,stringToParse)){
                String id = stringToParse.substring(0,stringToParse.indexOf("-"));
                String amount = stringToParse.substring(stringToParse.indexOf("-")+1);
                result.put(Integer.parseInt(id),Integer.parseInt(amount));
            }
        }
        if (result.isEmpty()) return null;
        return result;
    }

    public static Integer getCardNumber(String[] args){
        for (String arg : args) {
            String stringToParse=arg.trim();
            if (Pattern.matches(VALID_CARD_NUMBER_REGEX,stringToParse)){
                String cardNumber = stringToParse.substring(stringToParse.indexOf("-")+1);
                return Integer.parseInt(cardNumber);
            }
        }
        return null;
    }
}
