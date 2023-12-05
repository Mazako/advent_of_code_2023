package pl.mazak.day3;

import pl.mazak.utils.StreamSupport;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

class Solution {

    public static void main(String[] args) throws IOException {
        System.out.println(sum(partTwo()));
    }

    private static BiFunction<List<List<Character>>, LinkedList<Number>, Integer> partOne() {
        return (charMap, numbers) -> numbers.stream()
                .map(number -> findNumberValue(number.x, number.y, charMap))
                .reduce(Integer::sum)
                .orElse(0);
    }

    private static BiFunction<List<List<Character>>, LinkedList<Number>, Integer> partTwo() {
        Map<Engine, Set<Number>> engineMap = new HashMap<>();
        BiConsumer<List<List<Character>>, LinkedList<Number>> fun = (charMap, numbers) -> numbers
                .forEach(number -> findEngineAndValue(number, charMap, engineMap));
        return (charMap, numbers) -> {
            fun.accept(charMap, numbers);
            return engineMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().size() == 2)
                    .reduce(0, (total, e) -> total + e.getValue().stream().reduce(1, (a, b) -> a * b.value, Integer::sum), Integer::sum);
        };
    }

    private static void findEngineAndValue(Number number, List<List<Character>> charMap, Map<Engine, Set<Number>> engineMap) {
        int maxY = charMap.size();
        int maxX = charMap.get(0).size();
        int x = number.x;
        int y = number.y;
        Set<Engine> engines = new HashSet<>();
        StringBuilder builder = new StringBuilder();
        builder.append(charMap.get(number.y).get(x));
        while (x < maxX - 1) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int y1 = y + i;
                    int x1 = x + j;
                    if ((y1 > 0 && y1 < maxY) && (x1 > 0 && x1 < maxX)) {
                        char c = charMap.get(y1).get(x1);
                        if (c == '*') {
                            engines.add(new Engine(x1, y1));
                        }
                    }
                }
            }
            x++;
            char c = charMap.get(y).get(x);
            if (Character.isDigit(c)) {
                builder.append(c);
            } else {
                break;
            }
        }
        int value = Integer.parseInt(builder.toString());
        Number newNumber = new Number(number.x, number.y, value);
        engines.forEach(engine -> {
            if (engineMap.containsKey(engine)) {
                engineMap.get(engine).add(newNumber);
            } else {
                HashSet<Number> numbers = new HashSet<>();
                numbers.add(newNumber);
                engineMap.put(engine, numbers);
            }
        });
    }

    private static int sum(BiFunction<List<List<Character>>, LinkedList<Number>, Integer> func) throws IOException {
        List<List<Character>> charMap = new ArrayList<>();
        LinkedList<Number> numbers = new LinkedList<>();
        try (var stream = StreamSupport.readFile(Solution.class.getResource("input").getFile())) {
            char previous = '.';
            List<String> list = stream.lines().toList();
            for (int i = 0; i < list.size(); i++) {
                charMap.add(new ArrayList<>());
                char[] chars = list.get(i).toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    if (!Character.isDigit(previous) && Character.isDigit(chars[j])) {
                        numbers.add(new Number(j, i));
                    }
                    charMap.get(i).add(chars[j]);
                    previous = chars[j];
                }
            }
        }

        return func.apply(charMap, numbers);
    }

    private static int findNumberValue(int x, int y, List<List<Character>> charMap) {
        int maxY = charMap.size();
        int maxX = charMap.get(0).size();
        boolean adjacentToSymbol = false;
        StringBuilder builder = new StringBuilder();
        builder.append(charMap.get(y).get(x));
        while (x < maxX - 1) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int y1 = y + i;
                    int x1 = x + j;
                    if ((y1 > 0 && y1 < maxY) && (x1 > 0 && x1 < maxX)) {
                        char c = charMap.get(y1).get(x1);
                        if (!Character.isDigit(c) && c != '.') {
                            adjacentToSymbol = true;
                        }
                    }
                }
            }
            x++;
            char c = charMap.get(y).get(x);
            if (Character.isDigit(c)) {
                builder.append(c);
            } else {
                break;
            }
        }
        if (!adjacentToSymbol) {
            return 0;
        }
        return Integer.parseInt(builder.toString());
    }

    record Number(int x, int y, int value) {
        Number(int x, int y) {
            this(x, y, 0);
        }
    }

    record Engine(int x, int y) {
    }
}
