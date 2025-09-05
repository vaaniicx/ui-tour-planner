package at.fhtw.tourplanner.ui.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Difficulty {
    EASY(3),
    MEDIUM(2),
    HARD(1);

    private final int score;

    Difficulty(int score) {
        this.score = score;
    }

    public static int maxScore() {
        return Arrays.stream(values())
                .mapToInt(Difficulty::getScore)
                .max()
                .orElse(0);
    }
}
