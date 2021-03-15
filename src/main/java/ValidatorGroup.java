import java.util.HashSet;
import java.util.Set;

public class ValidatorGroup {
    private Set<String> outterGroup;
    private Set<String> innerGroup;

    public ValidatorGroup() {
        this.outterGroup = new HashSet<>();
        this.innerGroup = new HashSet<>();
    }

    public ValidatorGroup(Set<String> outter, Set<String> inner) {
        this.outterGroup = outter;
        this.innerGroup = inner;
    }

    public void addToInnerGroup(String str) {
        this.innerGroup.add(str);
    }

    public void addToOutterGroup(String str) {
        this.outterGroup.add(str);
    }

    public Set<String> getInnerGroup() {
        return innerGroup;
    }

    public Set<String> getOutterGroup() {
        return outterGroup;
    }
}
