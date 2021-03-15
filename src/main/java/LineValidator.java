import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineValidator {

    private static final Pattern pattern = Pattern.compile("([^\\[\\]]*)(\\[[^\\[\\]]*\\])*");

    public static boolean isValidLine(String str) {
        if (str == null || str.trim().length() < 4) {
            return false;
        }
        ValidatorGroup bucket = getGroupsToVerify(str);
        return isValidGroup(bucket);
    }

    protected static boolean isValidGroup(ValidatorGroup bucket) {
        if (bucket.getOutterGroup().isEmpty()) {
            return false;
        }
        if (bucket.getInnerGroup().stream()
                .anyMatch(LineValidator::containsPalindrome)) {
            return false;
        }
        if (bucket.getOutterGroup().stream()
                .anyMatch(LineValidator::containsPalindrome)) {
            return true;
        }
        return false;
    }

    protected static ValidatorGroup getGroupsToVerify(String str) {
        Matcher matcher = pattern.matcher(str);
        ValidatorGroup bucket = new ValidatorGroup();
        while(matcher.find()) {
            addString(matcher.group(1), bucket);
            addString(matcher.group(2), bucket);
        }
        return bucket;
    }

    protected static boolean containsPalindrome(String str) {
        int i = 1;
        int len = str.length();
        while (i <= len - 3) {
            while (i <= len - 3 && str.charAt(i) == str.charAt(i - 1)) {
                i++;
            }
            if (i > len - 3) {
                return false;
            }
            if (str.charAt(i) == str.charAt(i + 1) && str.charAt(i - 1) == str.charAt(i + 2)) {
                return true;
            }
            i++;
        }
        return false;
    }

    private static void addString(String str, ValidatorGroup bucket) {
        if (str != null && str.trim().length() >= 4) {
            if (str.startsWith("[")) {
                bucket.addToInnerGroup(str.substring(1, str.length() - 1));
            } else {
                bucket.addToOutterGroup(str);
            }
        }
    }
}
