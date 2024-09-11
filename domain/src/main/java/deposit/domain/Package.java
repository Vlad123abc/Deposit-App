package deposit.domain;

import jakarta.persistence.Table;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Packages")
public class Package extends MyEntity<Long>{
    private String name;
    private String from;
    private String to;
    private String description;
    private Float weight;
    private Boolean fragile;

    public Package() {
        this.name = "";
        this.from = "";
        this.to = "";
        this.description = "";
        this.weight = 0F;
        this.fragile = false;
    }

    public Package(String name, String from, String to, String description, Float weight, Boolean fragile) {
        this.name = name;
        this.from = from;
        this.to = to;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
        return Objects.equals(name, aPackage.name) && Objects.equals(from, aPackage.from) && Objects.equals(to, aPackage.to) && Objects.equals(description, aPackage.description) && Objects.equals(weight, aPackage.weight) && Objects.equals(fragile, aPackage.fragile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, from, to, description, weight, fragile);
    }

    @Override
    public String toString() {
        return "Package{" +
                "name='" + name + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", description='" + description + '\'' +
                ", weight=" + weight +
                ", fragile=" + fragile +
                ", id=" + id +
                '}';
    }
}
