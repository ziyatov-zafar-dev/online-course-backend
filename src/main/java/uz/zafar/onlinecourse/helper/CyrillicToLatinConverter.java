package uz.zafar.onlinecourse.helper;

import java.util.Map;

public class CyrillicToLatinConverter {
    private static final Map<Character, String> cyrillicToLatinMap = Map.<Character, String>ofEntries(
            Map.entry('А', "A"), Map.entry('Б', "B"), Map.entry('В', "V"),
            Map.entry('Г', "G"), Map.entry('Д', "D"), Map.entry('Е', "E"),
            Map.entry('Ё', "Yo"), Map.entry('Ж', "J"), Map.entry('З', "Z"),
            Map.entry('И', "I"), Map.entry('Й', "Y"), Map.entry('К', "K"),
            Map.entry('Қ', "Q"), Map.entry('Л', "L"), Map.entry('М', "M"),
            Map.entry('Н', "N"), Map.entry('О', "O"), Map.entry('П', "P"),
            Map.entry('Р', "R"), Map.entry('С', "S"), Map.entry('Т', "T"),
            Map.entry('У', "U"), Map.entry('Ф', "F"), Map.entry('Х', "X"),
            Map.entry('Ҳ', "H"), Map.entry('Ц', "Ts"), Map.entry('Ч', "Ch"),
            Map.entry('Ш', "Sh"), Map.entry('Щ', "Shch"), Map.entry('Ъ', ""),
            Map.entry('Ы', "Y"), Map.entry('Ь', ""), Map.entry('Э', "E"),
            Map.entry('Ю', "Yu"), Map.entry('Я', "Ya"), Map.entry('а', "a"),
            Map.entry('б', "b"), Map.entry('в', "v"), Map.entry('г', "g"),
            Map.entry('д', "d"), Map.entry('е', "e"), Map.entry('ё', "yo"),
            Map.entry('ж', "j"), Map.entry('з', "z"), Map.entry('и', "i"),
            Map.entry('й', "y"), Map.entry('к', "k"), Map.entry('қ', "q"),
            Map.entry('л', "l"), Map.entry('м', "m"), Map.entry('н', "n"),
            Map.entry('о', "o"), Map.entry('п', "p"), Map.entry('р', "r"),
            Map.entry('с', "s"), Map.entry('т', "t"), Map.entry('у', "u"),
            Map.entry('ф', "f"), Map.entry('х', "x"), Map.entry('ҳ', "h"),
            Map.entry('ц', "ts"), Map.entry('ч', "ch"), Map.entry('ш', "sh"),
            Map.entry('щ', "shch"), Map.entry('ъ', ""), Map.entry('ы', "y"),
            Map.entry('ь', ""), Map.entry('э', "e"), Map.entry('ю', "yu"),
            Map.entry('я', "ya")
    );

    public static String toLatin(String text) {
        StringBuilder sb = new StringBuilder();
        for (char ch : text.toCharArray()) {
            sb.append(cyrillicToLatinMap.getOrDefault(ch, String.valueOf(ch)));
        }
        return sb.toString();
    }
}

