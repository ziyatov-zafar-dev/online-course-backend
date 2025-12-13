package uz.codebyz.onlinecoursebackend.telegram.util;

import java.util.List;

public class PaginationUtil {

    public static <T> List<T> page(List<T> list, int page, int size) {
        int from = page * size;
        int to = Math.min(from + size, list.size());

        if (from >= list.size()) {
            return List.of();
        }
        return list.subList(from, to);
    }
}

