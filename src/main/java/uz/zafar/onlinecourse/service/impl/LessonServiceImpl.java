package uz.zafar.onlinecourse.service.impl;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.zafar.onlinecourse.db.domain.*;
import uz.zafar.onlinecourse.db.repository.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.ResponseDtoNotData;
import uz.zafar.onlinecourse.dto.lesson_dto.req.AddLessonDto;
import uz.zafar.onlinecourse.dto.lesson_dto.req.AddLessonFileDto;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonDto;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonFileAndTypeDto;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonFileDto;
import uz.zafar.onlinecourse.dto.lesson_file_dto.res.DownloadLessonFileDto;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.AuthService;
import uz.zafar.onlinecourse.service.LessonService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final AuthService authService;
    private final TypeRepository typeRepository;
    private final LessonFileRepository lessonFileRepository;
    private final ServletContext servletContext;
    private final GradeRepository gradeRepository;
    @Value("${lesson.video.base.url}")
    private String lessonFileBaseUrl;
    @Value("${homework.video.base.url}")
    private String homeworkFileBaseUrl;

    @Override
    public ResponseDto<LessonDto> findById(UUID lessonId) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(lessonId);
            if (lOp.isEmpty()) throw new Exception("Not found lesson id " + lessonId);
            LessonDto res = LessonDto.builder().title(lOp.get().getTitle()).description(lOp.get().getDescription()).pkey(lOp.get().getId()).files(lessonFileRepository.getFilesFromLesson(lessonId)).groupId(lOp.get().getGroup().getId()).created(lessonRepository.getLessonCreatedDate(lessonId)).updated(lessonRepository.getLessonUpdatedDate(lessonId)).build();
            return new ResponseDto<>(true, "success", res);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<LessonDto> addLesson(AddLessonDto lessonDto, HttpServletRequest request) {
        try {
            Optional<Group> gOp = groupRepository.findById(lessonDto.getGroupId());
            if (gOp.isEmpty()) return new ResponseDto<>(false, "Group not found");
            Group group = gOp.get();
            Optional<Type> checkType = typeRepository.findById(lessonDto.getFiles().get(0).getTypeId());
            if (checkType.isEmpty()) {
                throw new Exception("Not found type");
            }
            if (!checkType.get().getActive()) {
                throw new Exception("Not found type");
            }
            String s = Objects.requireNonNull(lessonDto.getFiles().get(0).getFile().getOriginalFilename()).toLowerCase();
            if (!checkType.get().getName().equals(s.substring(s.length() - 3)))
                throw new Exception("not equals file and file type");
            Lesson lesson = new Lesson();
            lesson.setGroup(group);
            lesson.setTitle(lessonDto.getTitle());
            lesson.setDescription(lessonDto.getDescription());
            lesson.setCreated(TimeUtil.currentTashkentTime());
            Lesson save = lessonRepository.save(lesson);

            // 2. Fayllar saqlanadigan papka yo'lini tayyorlaymiz
            String uploadDir = System.getProperty("user.dir") + "/uploads" + lessonFileBaseUrl;
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                boolean created = uploadPath.mkdirs();
                if (!created) {
                    return new ResponseDto<>(false, "Upload directory could not be created.");
                }
            }

            // 3. Fayllarni saqlaymiz
            for (LessonFileDto fileDto : lessonDto.getFiles()) {
                MultipartFile multipartFile = fileDto.getFile();
                if (multipartFile != null && !multipartFile.isEmpty()) {
                    String originalFileName = multipartFile.getOriginalFilename();

                    // Fayl nomidan xavfli belgilarni olib tashlaymiz
                    if (originalFileName == null) throw new Exception("not found original file name");
                    String cleanFileName = originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

                    // Fayl kengaytmasi
                    String extension = cleanFileName.substring(cleanFileName.lastIndexOf('.'));
                    String baseName = cleanFileName.substring(0, cleanFileName.lastIndexOf('.'));

                    // Timestampni qo‘shamiz
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(TimeUtil.currentTashkentTime());
                    String safeFileName = baseName + "_" + timestamp + extension;

                    String filePath = uploadDir + "/" + safeFileName;

                    // Faylni static papkaga saqlaymiz
                    multipartFile.transferTo(new File(filePath));

                    // Fayl URL
                    String fileUrl = lessonFileBaseUrl + "/" + safeFileName;

                    // Type ni topamiz
                    Optional<Type> typeOp = typeRepository.findById(fileDto.getTypeId());
                    if (typeOp.isEmpty()) continue;
                    LessonFile lessonFile = new LessonFile();
                    lessonFile.setFileUrl(authService.getHostUrl(request) + fileUrl);
                    lessonFile.setFileName(originalFileName);
                    lessonFile.setLesson(lesson);
                    lessonFile.setType(typeOp.get());
                    lessonFile.setCreated(TimeUtil.currentTashkentTime());
                    lessonFile.setActive(true);

                    lessonFileRepository.save(lessonFile);
                }
            }
            return new ResponseDto<>(true, "Lesson and files saved successfully", findById(save.getId()).getData());
        } catch (Exception e) {
            log.error("Error in addLesson", e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }


    @Override
    public ResponseDto<LessonDto> addFileToLesson(AddLessonFileDto dto, HttpServletRequest request) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(dto.getLessonId());
            if (lOp.isEmpty()) throw new Exception("Lesson not found");
            Lesson lesson = lOp.get();
            List<LessonFile> files = lesson.getFiles();
            Optional<Type> tOp = typeRepository.findById(dto.getTypeId());
            if (tOp.isEmpty()) throw new Exception("Not found type id: " + dto.getTypeId());
            if (!tOp.get().getActive()) throw new Exception("Not found type id: " + dto.getTypeId());
            Type type = tOp.get();
            if (!dto.getFile().getOriginalFilename().substring(dto.getFile().getOriginalFilename().length() - 3).equals(type.getName())) {
                throw new Exception("not equals file and file type");
            }

            MultipartFile file = dto.getFile();
            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                if (originalFileName == null) throw new Exception("not found original file name");
                String cleanFileName = originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
                String extension = cleanFileName.substring(cleanFileName.lastIndexOf('.'));
                String baseName = cleanFileName.substring(0, cleanFileName.lastIndexOf('.'));
                // Timestampni qo‘shamiz
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(TimeUtil.currentTashkentTime());
                String safeFileName = baseName + "_" + timestamp + extension;

                String uploadDir = getLessonUploadDir();
                String filePath = uploadDir + "/" + safeFileName;

                // Faylni static papkaga saqlaymiz
                file.transferTo(new File(filePath));

                // Fayl URL
                String fileUrl = lessonFileBaseUrl + "/" + safeFileName;
                files.add(LessonFile.builder().fileUrl(authService.getHostUrl(request) + fileUrl).fileName(originalFileName).created(TimeUtil.currentTashkentTime()).type(type).lesson(lesson).active(true).build());
                lesson.setFiles(files);
                Lesson save = lessonRepository.save(lesson);
                return new ResponseDto<>(true, "Lesson saved successfully", findById(save.getId()).getData());
            } else throw new Exception("Not found file");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    private String getLessonUploadDir() {
        return System.getProperty("user.dir") + "/uploads" + lessonFileBaseUrl;
    }

    @Override
    public ResponseDto<Page<LessonDto>> findAllByGroupId(UUID groupId, int page, int size) {
        try {
            if (groupRepository.findById(groupId).isEmpty()) throw new Exception("Group not found");
            Page<Lesson> lessons = lessonRepository.getLessonsByGroupId(groupId, PageRequest.of(page, size));
            List<LessonDto> contents = new ArrayList<>();
            for (Lesson lesson : lessons.getContent()) {
                List<LessonFileAndTypeDto> list = lessonFileRepository.getFilesFromLesson(lesson.getId());
                contents.add(new LessonDto(lesson.getId(), lesson.getTitle(), lesson.getDescription(), lessonRepository.getLessonCreatedDate(lesson.getId()), lessonRepository.getLessonUpdatedDate(lesson.getId()), groupId, list));
            }
            return new ResponseDto<>(true, "success", new PageImpl<>(contents, PageRequest.of(page, size), lessons.getTotalElements()));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> downloadLessonFile(UUID lessonFileId) {
        try {
            Optional<LessonFile> lfOp = lessonFileRepository.findById(lessonFileId);
            if (lfOp.isEmpty()) throw new Exception("Lesson file not found");
            LessonFile lessonFile = lfOp.get();
            if (!lessonFile.getActive())
                throw new Exception("Not found lesson file id");
            return authService.downloadFile(lessonFile.getFileUrl(), lessonFile.getFileName());
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.ok(new ResponseDtoNotData(false, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> downloadAllLessonFiles(UUID lessonId) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(lessonId);
            if (lOp.isEmpty()) throw new Exception("Lesson not found");
            List<LessonFile> lessonFiles = lessonFileRepository.getAllLessonFilesByLessonId(lessonId);
            if (lessonFiles.isEmpty()) return ResponseEntity.ok(new ResponseDtoNotData(false, "Lesson files is empty"));
            return authService.downloadMultipleFiles(
                    lessonFiles.stream()
                            .map(file -> DownloadLessonFileDto
                                    .builder()
                                    .fileUrl(file.getFileUrl())
                                    .fileName(file.getFileName())
                                    .build())
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.ok(new ResponseDtoNotData(false, e.getMessage()));
        }
    }


}
