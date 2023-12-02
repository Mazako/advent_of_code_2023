package pl.mazak.day2;

import pl.mazak.utils.StreamSupport;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class Solution {
    public static void main(String[] args) throws IOException {
        System.out.println(findTotalPowerOfMinimumColorsInGame("input"));
    }

    //PART ONE
    static int countPossibleGamesSum(String fileName) throws IOException {
        try (var stream = StreamSupport.readFile(Solution.class.getResource(fileName).getFile())) {
            return stream.lines()
                    .map(Solution::isPossible)
                    .filter(PossibleDTO::isPossible)
                    .reduce(0, (a, b) -> a + b.id, Integer::sum);
        }
    }

    private static PossibleDTO isPossible(String line) {
        AtomicBoolean isPossible = new AtomicBoolean(true);
        String gameNumber = line.substring(0, line.indexOf(":")).split(" ")[1];
        Arrays.stream(line.substring(line.indexOf(": ") + 2)
                        .split("; "))
                .flatMap(game -> Arrays.stream(game.split(", ")))
                .forEach(result -> {
                    String[] countColor = result.split(" ");
                    if ((countColor[1].equals("red") && Integer.parseInt(countColor[0]) > 12)
                            || countColor[1].equals("green") && Integer.parseInt(countColor[0]) > 13
                            || countColor[1].equals("blue") && Integer.parseInt(countColor[0]) > 14) {
                        isPossible.set(false);
                    }
                });
        return new PossibleDTO(Integer.parseInt(gameNumber), isPossible.get());
    }

    record PossibleDTO(int id, boolean isPossible){}

    //PART TWO
    static int findTotalPowerOfMinimumColorsInGame(String fileName) throws IOException {
        try (var stream = StreamSupport.readFile(Solution.class.getResource(fileName).getFile())) {
            return stream.lines()
                    .map(Solution::powerOfMaxes)
                    .reduce(Integer::sum)
                    .orElseThrow(RuntimeException::new);
        }
    }

    private static int powerOfMaxes(String line) {
        AtomicInteger maxRed = new AtomicInteger(0);
        AtomicInteger maxGreen = new AtomicInteger(0);
        AtomicInteger maxBlue = new AtomicInteger(0);

        Arrays.stream(line.substring(line.indexOf(": ") + 2)
                        .split("; "))
                .flatMap(game -> Arrays.stream(game.split(", ")))
                .forEach(result -> {
                    String[] countColor = result.split(" ");
                    switch (countColor[1]) {
                        case "red" -> maxRed.set(Math.max(Integer.parseInt(countColor[0]), maxRed.get()));
                        case "green" -> maxGreen.set(Math.max(Integer.parseInt(countColor[0]), maxGreen.get()));
                        case "blue" -> maxBlue.set(Math.max(Integer.parseInt(countColor[0]), maxBlue.get()));
                    }
                });
        return maxRed.get() * maxGreen.get() * maxBlue.get();
    }
}
