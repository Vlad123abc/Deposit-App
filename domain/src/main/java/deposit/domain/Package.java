package deposit.domain;

import jakarta.persistence.Table;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Packages")
public class Package extends MyEntity<Long>{
    private String name;
    private String p_from;
    private String p_to;
    private String description;
    private Float weight;
    private Boolean fragile;

    public Package() {
        this.name = "";
        this.p_from = "";
        this.p_to = "";
        this.description = "";
        this.weight = 0F;
        this.fragile = false;
    }

    public Package(String name, String p_from, String p_to, String description, Float weight, Boolean fragile) {
        this.name = name;
        this.p_from = p_from;
        this.p_to = p_to;
        this.description = description;
        this.weight = weight;
        this.fragile = fragile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getP_from() {
        return p_from;
    }

    public void setP_from(String p_from) {
        this.p_from = p_from;
    }

    public String getP_to() {
        return p_to;
    }

    public void setP_to(String to) {
        this.p_to = to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean getFragile() {
        return fragile;
    }

    public void setFragile(Boolean fragile) {
        this.fragile = fragile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Package aPackage = (Package) o;
        return Objects.equals(name, aPackage.name) && Objects.equals(p_from, aPackage.p_from) && Objects.equals(p_to, aPackage.p_to) && Objects.equals(description, aPackage.description) && Objects.equals(weight, aPackage.weight) && Objects.equals(fragile, aPackage.fragile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, p_from, p_to, description, weight, fragile);
    }

    @Override
    public String toString() {
        return "Package{" +
                "name='" + name + '\'' +
                ", from='" + p_from + '\'' +
                ", to='" + p_to + '\'' +
                ", description='" + description + '\'' +
                ", weight=" + weight +
                ", fragile=" + fragile +
                ", id=" + id +
                '}';
    }
}
