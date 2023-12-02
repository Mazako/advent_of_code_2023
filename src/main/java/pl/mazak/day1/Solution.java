package pl.mazak.day1;

import pl.mazak.utils.StreamSupport;

import java.io.IOException;
import java.util.LinkedList;

class Solution {
    public static void main(String[] args) throws IOException {
        System.out.println(calibrationValuesSum());
    }

    static int calibrationValuesSum() throws IOException {
        try (var stream = StreamSupport.readFile(Solution.class.getResource("input").getFile())) {
            return stream.lines()
                    .map(Solution::getLineDigit)
                    .reduce(Integer::sum)
                    .orElseThrow(RuntimeException::new);
        }
    }

    private static int getLineDigit(String line) {
        int ptr = 0;
        LinkedList<String> list = new LinkedList<>();
        while (ptr < line.length()) {
            int ptrAtStart = ptr;
            int offset3 = ptr + 3;
            if (offset3 <= line.length()) {
                String substring = line.substring(ptr, offset3);
                if (substring.equals("one")) {
                    list.add("1");
                    ptr += 2;
                } else if (substring.equals("two")) {
                    list.add("2");
                    ptr += 2;
                } else if (substring.equals("six")) {
                    list.add("6");
                    ptr += 3;
                }
            }

            int offset4 = ptr + 4;
            if (offset4 <= line.length()) {
                String substring = line.substring(ptr, offset4);
                if (substring.equals("four")) {
                    list.add("4");
                    ptr += 4;
                } else if (substring.equals("five")) {
                    list.add("5");
                    ptr += 3;
                } else if (substring.equals("nine")) {
                    list.add("9");
                    ptr += 3;
                }
            }

            int offset5 = ptr + 5;
            if (offset5 <= line.length()) {
                String substring = line.substring(ptr, offset5);
                if (substring.equals("three")) {
                    list.add("3");
                    ptr += 4;
                } else if (substring.equals("seven")) {
                    list.add("7");
                    ptr += 4;
                } else if (substring.equals("eight")) {
                    ptr += 4;
                    list.add("8");
                }
            }

            if (ptr < line.length() && Character.isDigit(line.charAt(ptr))) {
                list.add(String.valueOf(line.charAt(ptr)));
                ptr++;
            }
            if (ptr == ptrAtStart) {
                ptr++;
            }
        }
        return Integer.parseInt(list.getFirst() + list.getLast());
    }
}
