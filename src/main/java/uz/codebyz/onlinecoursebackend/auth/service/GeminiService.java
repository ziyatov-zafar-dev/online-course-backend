package uz.codebyz.onlinecoursebackend.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.Map;@Service
public class GeminiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.gemini.api-key}")
    private String apiKey;

    @Value("${google.gemini.model}")
    private String model;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

    public ResponseDto<?> generateCourseDescription(String courseName) {

        try {
            String url = String.format(GEMINI_URL, model, apiKey);

            String prompt = """
                    Kurs uchun qisqa tavsif yozing
                    title: '%s'
                    
                    title ni umuman o'zgartirmang
                    
                    Faqat JSON formatida javob qaytaring:
                    Matn uzunligi 300-500 ta harf bo'lsin
                    {
                      "title": "Kurs nomi",
                      "desc": "AI javobi"
                    }
                    Javobga ```json yoki ``` qo‘shmang.
                    """.formatted(courseName);

            Map<String, Object> requestBody = Map.of(
                    "contents", new Object[]{
                            Map.of("parts", new Object[]{
                                    Map.of("text", prompt)
                            })
                    }
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            Map body = response.getBody();
            if (body == null) {
                return ResponseDto.error("AI javobi bo‘sh qaytdi");
            }

            List candidates = (List) body.get("candidates");
            Map firstCandidate = (Map) candidates.get(0);

            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);

            String aiText = (String) firstPart.get("text");

            // ==== ❗ CODE BLOCK TOZALASH =====
            String cleaned = aiText
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            // ==== JSON PARSE =====
            Map json = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(cleaned, Map.class);

            return ResponseDto.ok("Ok", json);

        } catch (Exception ex) {
            return ResponseDto.error("AI bilan bog‘lanishda xatolik: " + ex.getMessage());
        }
    }
}

