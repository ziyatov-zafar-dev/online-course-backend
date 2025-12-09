package uz.codebyz.onlinecoursebackend.userDevice.service;

import com.google.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.userDevice.dt.IpLocationDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Service
public class IpLocationService {

    @Value("${location.api}")
    private String apiUrl;

    private final Gson gson = new Gson();

    public IpLocationDto getLocation(String ip) {
        try {
            String url = apiUrl + ip;

            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );

            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            reader.close();

            // 🔥 JSON → IpLocationDto
            return gson.fromJson(jsonBuilder.toString(), IpLocationDto.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

