package src.domain;

/**
 * Created by Vladyslav Dovhopol on 3/2/17.
 */
public class Office {

    private final String name;

    public Office(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Office.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Office other = (Office) obj;
        return this.name == other.name || this.name != null && this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return 28 + (this.name != null ? this.name.hashCode() : 0);
    }
}
