package enums;

import lombok.Getter;

@Getter
public enum TaskPriority {
    LOWEST(1, "LOWEST"),
    LOW(2, "LOW"),
    MEDIUM(3, "MEDIUM"),
    HIGH(4, "HIGH"),
    HIGHEST(5, "HIGHEST");

    private final int level;
    private final String description;

    TaskPriority(int level, String description) {
        this.level = level;
        this.description = description;
    }
}