package uz.codebyz.onlinecoursebackend.telegram.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class Util {

    public static <T> List<T> page(List<T> list, int page, int size) {
        int from = page * size;
        int to = Math.min(from + size, list.size());

        if (from >= list.size()) {
            return List.of();
        }
        return list.subList(from, to);
    }

    public static String getBaseUrl(HttpServletRequest request) {
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null || host.isBlank()) {
            host = request.getServerName();
        }
        // agar proxy bir nechta host yuborgan bo‘lsa
        if (host.contains(",")) {
            host = host.split(",")[0].trim();
        }
        return "https://" + host;
    }

}

