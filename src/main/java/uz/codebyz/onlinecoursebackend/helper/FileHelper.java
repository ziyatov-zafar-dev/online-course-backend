package uz.codebyz.onlinecoursebackend.helper;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    public static String getFileSize(MultipartFile file) {
        return FileHelper.getFileSize(file.getSize());
    }

    public static String getFileSize(Long size) {
        if (size == null || size <= 0) return "0";
        double result;
        String value;
        String unit;

        if (size < 1024) {
            return String.valueOf(size).concat(" B");
        } else if (size < 1024L * 1024) {
            result = (double) size / 1024;
            unit = " KB";

        } else if (size < 1024L * 1024 * 1024) {
            result = (double) size / (1024 * 1024);
            unit = " MB";

        } else {
            result = (double) size / (1024L * 1024 * 1024);
            unit = " GB";
        }

        // Avval ikki kasr bilan formatlaymiz
        if (result >= 100) {
            value = String.format(java.util.Locale.US, "%.2f", result);
        } else {
            value = String.format(java.util.Locale.US, "%.0f", result);
        }

        // Ortiqcha nol va nuqtalarni o'chirish
        while (value.contains(".") && (value.endsWith("0") || value.endsWith("."))) {
            value = value.substring(0, value.length() - 1);
        }

        return value.concat(unit);
    }

    public static ResponseDto<UploadFileResponseDto> uploadFile(MultipartFile file, String folder, boolean mgb) {

        try {
            if (mgb) file = FileHelper.compressVideoAndReturn(file);
            if (file.isEmpty()) {
                return ResponseDto.error("Fayl topilmadi!");
            }
            if (!new File(folder).exists()) {
                new File(folder).mkdirs();
            }
            String originalName = file.getOriginalFilename();
            String newFileName = (CurrentTime.currentTime() + "_" + originalName).replaceAll("[^a-zA-Z0-9_.]", "_");
            Path filePath = Paths.get(folder + newFileName);
            Files.write(filePath, file.getBytes());
            return new ResponseDto<>(true, "Ok", new UploadFileResponseDto(
                    originalName,
                    folder + newFileName,
                    file.getSize()
            ));
        } catch (Exception e) {
            return new ResponseDto<>(false, "Kutilmagan xatolik" + e.getMessage());
        }
    }

    public static boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                Files.delete(path);
                return false;
            } else {
                System.out.println("❗ File not found: " + filePath);
                return true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    public static MultipartFile compressVideoAndReturn(MultipartFile originalFile) {
        try {

            // --- 1. TEMP papka yaratamiz ---
            Path tempDir = Files.createTempDirectory("compress_tmp_");

            // Kirgan videoni vaqtinchalik saqlaymiz
            File tempInput = tempDir.resolve("input_" + originalFile.getOriginalFilename()).toFile();
            originalFile.transferTo(tempInput);

            // Compressdan keyin chiqadigan vaqtinchalik fayl
            File tempOutput = tempDir.resolve("output_" + originalFile.getOriginalFilename()).toFile();

            // --- 2. FFmpeg resource fayllarini to'g'ri ko'rsatamiz ---
            String ffmpegPath = "src/main/resources/ffmpeg/ffmpeg.exe";
            String ffprobePath = "src/main/resources/ffmpeg/ffprobe.exe";

            FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
            FFprobe ffprobe = new FFprobe(ffprobePath);

            // --- 3. Compress parametrlari ---
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(tempInput.getAbsolutePath())
                    .overrideOutputFiles(true)
                    .addOutput(tempOutput.getAbsolutePath())
                    .setVideoBitRate(1_200_000) // 1.2 Mbps
                    .setVideoFrameRate(30)
                    .setVideoResolution(1280, 720)
                    .setAudioBitRate(128_000)
                    .setAudioChannels(2)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();

            // --- 4. Compress bo‘lgan videoni byte[] ga o‘qiymiz ---
            byte[] compressedBytes = Files.readAllBytes(tempOutput.toPath());

            // --- 5. MultipartFile obyektini qayta yasaymiz ---
            MultipartFile result = new MultipartFile() {
                @Override
                public String getName() {
                    return originalFile.getName();
                }

                @Override
                public String getOriginalFilename() {
                    return "compressed_" + originalFile.getOriginalFilename();
                }

                @Override
                public String getContentType() {
                    return originalFile.getContentType();
                }

                @Override
                public boolean isEmpty() {
                    return compressedBytes.length == 0;
                }

                @Override
                public long getSize() {
                    return compressedBytes.length;
                }

                @Override
                public byte[] getBytes() {
                    return compressedBytes;
                }

                @Override
                public InputStream getInputStream() {
                    return new ByteArrayInputStream(compressedBytes);
                }

                @Override
                public void transferTo(File dest) throws IOException {
                    Files.write(dest.toPath(), compressedBytes);
                }
            };

            // --- 6. Temp fayllarni o‘chiramiz ---
            tempInput.delete();
            tempOutput.delete();
            Files.deleteIfExists(tempDir);

            // --- 7. Natija sifatida compressed MultipartFile qaytadi ---
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Video compress qilishda xato: " + e.getMessage(), e);
        }
    }


}
