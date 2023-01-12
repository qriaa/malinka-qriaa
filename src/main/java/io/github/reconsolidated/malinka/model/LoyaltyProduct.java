package io.github.reconsolidated.malinka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyProduct {
    private Long id;
    private String name = "default";
    private String fileName = "default";
    private String category = "default";

    private int points = 1;

    public LoyaltyProduct(String name, String fileName, String category, int points) {
        this.name = name;
        this.fileName = fileName;
        this.category = category;
        this.points = points;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        LoyaltyProduct other = (LoyaltyProduct) obj;
        return Objects.equals(this.id, other.id);
    }
}
