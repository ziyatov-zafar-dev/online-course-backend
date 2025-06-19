package uz.zafar.onlinecourse.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.zafar.onlinecourse.db.repository.UserRepository;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.ResponseDtoNotData;
import uz.zafar.onlinecourse.dto.lesson_file_dto.res.DownloadLessonFileDto;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
//import uz.farobiy.lesson_11_backend.db.repository.UserRepository;


@Service
public class AuthService implements UserDetailsService {
    @Value("${homework.video.base.url}")
    private String homeworkVideoBaseUrl;


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public String getHostUrl(HttpServletRequest request) {
        String scheme = request.getScheme();             // http yoki https
        String serverName = request.getServerName();     // masalan, localhost
        int serverPort = request.getServerPort();        // masalan, 8080
        String contextPath = request.getContextPath();   // masalan, /api

        return scheme + "://" + serverName +
                (serverPort == 80 || serverPort == 443 ? "" : ":" + serverPort) +
                contextPath;
    }

    public ResponseEntity<?> downloadFile(String fileUrl,String filename) {
        try {
            URL url = new URL(fileUrl);
            UrlResource resource = new UrlResource(url);
            if (!resource.exists() || !resource.isReadable()) {
                throw new FileNotFoundException("Fayl topilmadi yoki o‘qib bo‘lmaydi");
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.ok(new ResponseDtoNotData(false, e.getMessage()));
        }
    }
    public ResponseEntity<?> downloadMultipleFiles(List<DownloadLessonFileDto> data) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

            int i = 0 ;
            for (DownloadLessonFileDto fileData : data) {
                i ++ ;
                URL url = new URL(fileData.getFileUrl());
                InputStream inputStream = url.openStream();
                zipOut.putNextEntry(new ZipEntry("%d".formatted(i)+fileData.getFileName()));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    zipOut.write(buffer, 0, len);
                }
                zipOut.closeEntry();
                inputStream.close();
            }

            zipOut.close();

            byte[] zipBytes = byteArrayOutputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(zipBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(zipBytes.length)
                    .body(resource);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDtoNotData(false, "Fayllarni yuklashda xatolik: " + e.getMessage()));
        }
    }
    /*public ResponseDto<String> saveFile(MultipartFile file, String fileName, String fileBaseUrl,HttpServletRequest request) {
        try {
            Path directoryPath = Paths.get(fileBaseUrl);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Path filePath = directoryPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            return ResponseDto.<String>builder()
                    .success(true)
                    .message("Fayl muvaffaqiyatli saqlandi")
                    .data(this.getHostUrl(request)  + homeworkVideoBaseUrl + "/" + fileName)
                    .build();

        } catch (Exception e) {
            return ResponseDto.<String>builder()
                    .success(false)
                    .message("Faylni saqlashda xatolik: " + e.getMessage())
                    .build();
        }
    }*/
    public ResponseDto<String> saveFile(MultipartFile file, String fileName, HttpServletRequest request) {
        String fileBaseUrl = "uploads/files/homeworks";
        try {
            Path directoryPath = Paths.get(fileBaseUrl);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Path filePath = directoryPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            return ResponseDto.<String>builder()
                    .success(true)
                    .message("Fayl muvaffaqiyatli saqlandi")
                    .data(this.getHostUrl(request) + "/files/homeworks/" + fileName)
                    .build();

        } catch (IOException e) {
            return ResponseDto.<String>builder()
                    .success(false)
                    .message("Faylni saqlashda xatolik: " + e.getMessage())
                    .build();
        }
    }

    public ResponseEntity<?> showFile(String type, String fileName) {
        try {
            if (fileName.contains("..")) {
                return ResponseEntity.badRequest().build();
            }

            File file = new File("uploads/files/%s/".formatted(type) + fileName);

            if (!file.exists() || file.isDirectory()) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());

            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));
            headers.setContentLength(fileContent.length);
            headers.setContentDisposition(ContentDisposition.inline().filename(file.getName()).build());
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDtoNotData(false, e.getMessage()));
        }
    }
}
