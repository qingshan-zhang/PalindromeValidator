import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LineValidatorTest {

    //region Test isValidLine

    @Test
    public void nullValue() {
        assertThat(LineValidator.isValidLine(null)).isFalse();
    }

    @Test
    public void emptyLine() {
        assertThat(LineValidator.isValidLine("")).isFalse();
    }

    @Test
    public void spaceLine() {
        assertThat(LineValidator.isValidLine(" ")).isFalse();
    }

    @Test
    public void shortLine() {
        assertThat(LineValidator.isValidLine("aba")).isFalse();
    }

    @Test
    public void caseDifference() {
        assertThat(LineValidator.isValidLine("ABba")).isFalse();
    }

    @Test
    public void sameCharacters() {
        assertThat(LineValidator.isValidLine("bbbb[ab]qrst")).isFalse();
    }

    @Test
    public void validLineContainSpace() {
        assertThat(LineValidator.isValidLine("ada baab")).isTrue();
    }

    @Test
    public void invalidLineContainSpace() {
        assertThat(LineValidator.isValidLine("ba ab")).isFalse();
    }

    @Test
    public void palindromeInBracket() {
        assertThat(LineValidator.isValidLine("irttr[baab]qrst")).isFalse();
    }

    @Test
    public void palindromeOutOfBracket() {
        assertThat(LineValidator.isValidLine("rttr[mnop]qrst")).isTrue();
    }

    @Test
    public void palindromeWithSpecialCharactersAndDigits() {
        assertThat(LineValidator.isValidLine("abc&bb&[ad]q34")).isTrue();
    }

    @Test
    public void allCharactersInBucket() {
        assertThat(LineValidator.isValidLine("[abcdaasdfaef]")).isFalse();
    }

    @Test
    public void shortStringOutSide() {
        assertThat(LineValidator.isValidLine("[asdf]baa[naba]bcd[dawe]")).isFalse();
    }

    @Test
    public void stringWithUnclosedBracket() {
        assertThat(LineValidator.isValidLine("[rttr[")).isTrue();
    }

    //endregion

    //region Test getGroupsToVerify

    @Test
    public void oneBracket() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("abcd[efgh]hi");
        assertThat(groupsToVerify.getInnerGroup()).containsExactlyInAnyOrder("efgh");
        assertThat(groupsToVerify.getOutterGroup()).containsExactlyInAnyOrder("abcd");
    }

    @Test
    public void bracketAtBegining() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("[efgh]hive");
        assertThat(groupsToVerify.getInnerGroup()).containsExactlyInAnyOrder("efgh");
        assertThat(groupsToVerify.getOutterGroup()).containsExactlyInAnyOrder("hive");
    }

    @Test
    public void bracketAtEnd() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("abcd[efgh]");
        assertThat(groupsToVerify.getInnerGroup()).containsExactlyInAnyOrder("efgh");
        assertThat(groupsToVerify.getOutterGroup()).containsExactlyInAnyOrder("abcd");
    }

    @Test
    public void noBracket() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("abcdefg");
        assertThat(groupsToVerify.getInnerGroup()).isEmpty();
        assertThat(groupsToVerify.getOutterGroup()).containsExactlyInAnyOrder("abcdefg");
    }

    @Test
    public void noOutterString() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("[something]");
        assertThat(groupsToVerify.getInnerGroup()).containsExactlyInAnyOrder("something");
        assertThat(groupsToVerify.getOutterGroup()).isEmpty();
    }

    @Test
    public void multipleBracket() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("abcd[efgh]h[ve][include]outside");
        assertThat(groupsToVerify.getInnerGroup()).containsExactlyInAnyOrder("efgh", "include");
        assertThat(groupsToVerify.getOutterGroup()).containsExactlyInAnyOrder("abcd", "outside");
    }

    @Test
    public void nestedBracket() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("abcd[[efgh]]outside");
        assertThat(groupsToVerify.getInnerGroup()).containsExactlyInAnyOrder("efgh");
        assertThat(groupsToVerify.getOutterGroup()).containsExactlyInAnyOrder("abcd", "outside");
    }

    @Test
    public void mixNestedBracket() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("[abcd[efgh]defg]outside");
        //TODO: to confirm the expected behaviour for mixed nested bracket
        assertThat(groupsToVerify.getInnerGroup()).containsExactlyInAnyOrder("efgh");
        assertThat(groupsToVerify.getOutterGroup()).containsExactlyInAnyOrder("abcd", "defg", "outside");
    }

    @Test
    public void nonClosingBracket() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("abcd]efgh]h[outside");
        assertThat(groupsToVerify.getInnerGroup()).isEmpty();
        //TODO: to confirm the expected behaviour for non closing bracket
        assertThat(groupsToVerify.getOutterGroup()).containsExactlyInAnyOrder("abcd","efgh", "outside");
    }

    @Test
    public void mixedBracket() {
        ValidatorGroup groupsToVerify = LineValidator.getGroupsToVerify("[abc[cdef]eff][ghij]h[outside");
        //TODO: to confirm the expected behaviour for non closing bracket
        assertThat(groupsToVerify.getInnerGroup()).containsExactlyInAnyOrder("cdef", "ghij");
        assertThat(groupsToVerify.getOutterGroup()).containsExactlyInAnyOrder("outside");
    }

    //endregion

    //region Test isValidGroup

    @Test
    public void emptyGroup() {
        assertThat(LineValidator.isValidGroup(new ValidatorGroup())).isFalse();
    }

    @Test
    public void outterStringEmpty() {
        Set<String> innerStrings = Set.of("abcd");
        ValidatorGroup group = new ValidatorGroup(new HashSet<>(), innerStrings);
        assertThat(LineValidator.isValidGroup(group)).isFalse();
    }

    @Test
    public void outterStringHasPalindrome() {
        Set<String> outterString = Set.of("abba", "abcd");
        Set<String> innerString = Set.of("asda", "bbbb");
        ValidatorGroup group = new ValidatorGroup(outterString, innerString);
        assertThat(LineValidator.isValidGroup(group)).isTrue();
    }

    @Test
    public void bracketContainsPalindrome() {
        Set<String> outterString = Set.of("abba", "abca");
        Set<String> innerString = Set.of("asdd", "baab");
        ValidatorGroup group = new ValidatorGroup(outterString, innerString);
        assertThat(LineValidator.isValidGroup(group)).isFalse();
    }

    @Test
    public void outterStringHasNoPalindrome() {
        Set<String> outterString = Set.of("abcd", "edge");
        Set<String> innerString = Set.of("asad", "abcd");
        ValidatorGroup group = new ValidatorGroup(outterString, innerString);
        assertThat(LineValidator.isValidGroup(group)).isFalse();
    }

    @Test
    public void bothHavePalindrome() {
        Set<String> outterString = Set.of("abba", "abcd");
        Set<String> innerString = Set.of("asda", "baab");
        ValidatorGroup group = new ValidatorGroup(outterString, innerString);
        assertThat(LineValidator.isValidGroup(group)).isFalse();
    }

    @Test
    public void noneHasPalindrome() {
        Set<String> outterString = Set.of("abac", "abcd");
        Set<String> innerString = Set.of("asda", "edgf");
        ValidatorGroup group = new ValidatorGroup(outterString, innerString);
        assertThat(LineValidator.isValidGroup(group)).isFalse();
    }

    //endregion

    //region Test containsPalindrome

    @Test
    public void shortString() {
        assertThat(LineValidator.containsPalindrome("aba")).isFalse();
    }

    @Test
    public void duplicateString() {
        assertThat(LineValidator.containsPalindrome("abccccd")).isFalse();
    }

    @Test
    public void allDuplicateString() {
        assertThat(LineValidator.containsPalindrome("aaaaaaaa")).isFalse();
    }

    @Test
    public void hadPalindrome() {
        assertThat(LineValidator.containsPalindrome("abcceffecd")).isTrue();
    }

    @Test
    public void palindromeAtBegining() {
        assertThat(LineValidator.containsPalindrome("abbacccd")).isTrue();
    }

    @Test
    public void palindromeAtEnd() {
        assertThat(LineValidator.containsPalindrome("abccccddc")).isTrue();
    }

    @Test
    public void palindromeAfterDuplicateStrings() {
        assertThat(LineValidator.containsPalindrome("aaaaaabba")).isTrue();
    }

    //endregion
}
