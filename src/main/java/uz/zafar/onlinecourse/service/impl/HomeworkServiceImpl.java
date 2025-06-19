package uz.zafar.onlinecourse.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.zafar.onlinecourse.db.domain.*;
import uz.zafar.onlinecourse.db.repository.HomeworkFileRepository;
import uz.zafar.onlinecourse.db.repository.HomeworkRepository;
import uz.zafar.onlinecourse.db.repository.LessonRepository;
import uz.zafar.onlinecourse.db.repository.TypeRepository;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.AddHomeworkDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.AddHomeworkFileDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.EditHomeworkDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.RemoveHomeworkFile;
import uz.zafar.onlinecourse.dto.homework_dto.res.HomeworkDto;
import uz.zafar.onlinecourse.dto.homework_dto.res.HomeworkFileDto;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonDto;
import uz.zafar.onlinecourse.helper.CyrillicToLatinConverter;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.AuthService;
import uz.zafar.onlinecourse.service.HomeworkService;
import uz.zafar.onlinecourse.service.LessonService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Log4j2
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final LessonService lessonService;
    private final TypeRepository typeRepository;
    private final AuthService authService;
    private final LessonRepository lessonRepository;
    private final HomeworkFileRepository homeworkFileRepository;

    private List<HomeworkFileDto> getHomeworkFiles(Homework h) {
        List<HomeworkFileDto> files = new ArrayList<>();
        List<HomeworkFile> homeworkFiles = homeworkRepository.getHomeworkFiles(h.getId());
        for (HomeworkFile homeworkFile : homeworkFiles) {
            HomeworkFileDto file = HomeworkFileDto
                    .builder()
                    .homeworkId(homeworkFile.getId())
                    .fileName(homeworkFile.getFileName())
                    .fileUrl(homeworkFile.getFileUrl())
                    .typeId(homeworkFile.getType().getId())
                    .homeworkFileId(homeworkFile.getId())
                    .build();
            files.add(file);
        }
        return files;
    }

    @Override
    public ResponseDto<HomeworkDto> findById(UUID pkey) {
        try {
            Optional<Homework> hOp = homeworkRepository.findById(pkey);
            if (hOp.isPresent()) {
                Homework h = hOp.get();
                if (!h.getActive())
                    throw new Exception("Not found homework");
                ResponseDto<LessonDto> checkLesson = lessonService.findById(h.getLesson().getId());
                if (!checkLesson.isSuccess())
                    throw new Exception(checkLesson.getMessage());
                return new ResponseDto<>(true, "Ok", HomeworkDto.builder()
                        .pkey(pkey)
                        .title(h.getTitle())
                        .description(h.getDescription())
                        .created(h.getCreated())
                        .deadline(h.getDeadline())
                        .updated(h.getUpdated())
                        .files(getHomeworkFiles(h))
                        .lesson(checkLesson.getData())
                        .build());
            } else throw new Exception("Not found homework");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Page<HomeworkDto>> findAllByLessonId(UUID lessonId, int page, int size) {
        try {
            Page<Homework> homeworks = homeworkRepository.findAllByLessonId(PageRequest.of(page, size), lessonId);
            List<HomeworkDto> contents = new ArrayList<>();
            for (Homework homework : homeworks.getContent()) {
                ResponseDto<LessonDto> checkLesson = lessonService.findById(homework.getLesson().getId());
                if (!checkLesson.isSuccess()) continue;
                contents.add(HomeworkDto.builder()
                        .pkey(homework.getId())
                        .title(homework.getTitle())
                        .description(homework.getDescription())
                        .created(homework.getCreated())
                        .deadline(homework.getDeadline())
                        .updated(homework.getUpdated())
                        .files(getHomeworkFiles(homework))
                        .lesson(checkLesson.getData())
                        .build());
            }
            return new ResponseDto<>(true, "Success",
                    new PageImpl<>(contents, PageRequest.of(page, size), homeworks.getTotalElements()));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<?> addFileToHomework(AddHomeworkFileDto dto, HttpServletRequest req) {
        try {
            UUID homeworkId = dto.getHomeworkId();
            Optional<Homework> hOp = homeworkRepository.findById(homeworkId);
            if (hOp.isEmpty()) throw new Exception("Not found homework");
            Homework homework = hOp.get();
            HomeworkFile homeworkFile = new HomeworkFile();
            MultipartFile file = dto.getFile();
            Optional<Type> tOp = typeRepository.findById(dto.getTypeId());
            if (tOp.isEmpty()) throw new Exception("Not found type");
            Type type = tOp.get();
            if (type.getActive() == false) throw new Exception("Not found type");
            if (!Objects.requireNonNull(Objects.requireNonNull(file.getOriginalFilename()).toLowerCase()).contains(type.getName().toLowerCase()))
                throw new Exception("File name not equals to original filename");
            String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - 4) + "_" + TimeUtil.currentTashkentTime() + "." + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 3);
            fileName = fileName.replaceAll("[\\\\/:*?\"<>|\\s]", "_");
            ResponseDto<String> saveFile = authService.saveFile(file, fileName, req);
            if (saveFile.isSuccess()) {
                homeworkFile.setType(type);
                homeworkFile.setFileUrl(saveFile.getData());
                homeworkFile.setFileName(fileName);
                homeworkFile.setCreated(TimeUtil.currentTashkentTime());
                homeworkFile.setHomework(homework);
                homeworkFile.setActive(true);
                homeworkFileRepository.save(homeworkFile);
                return new ResponseDto<>(true, "Ok", findById(homework.getId()).getData());
            }
            throw new Exception("Not found homework");

        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<?> addHomework(AddHomeworkDto dto, HttpServletRequest request) {
        try {
            Optional<Lesson> lessonOptional = lessonRepository.findById(dto.getLessonId());
            if (lessonOptional.isEmpty()) throw new Exception("Not found lesson id: " + dto.getLessonId());
            MultipartFile file = dto.getFile();
            Short typeId = dto.getTypeId();
            Optional<Type> tOp = typeRepository.findById(typeId);
            if (tOp.isEmpty()) throw new Exception("Not found type");
            if (!tOp.get().getActive()) throw new Exception("Not found type");
            Type type = tOp.get();
            if (!Objects.requireNonNull(Objects.requireNonNull(file.getOriginalFilename()).toLowerCase()).contains(type.getName().toLowerCase()))
                throw new Exception("File name not equals to original filename");
            String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - 4) + "_" + TimeUtil.currentTashkentTime() + "." + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 3);
            fileName = fileName.replaceAll("[\\\\/:*?\"<>|\\s]", "_");
//            fileName = s + fileName.substring(fileName.length() - 4);
            ResponseDto<String> saveFile = authService.saveFile(file, fileName, request);
            if (saveFile.isSuccess()) {
                Homework homework = new Homework();
                homework.setCreated(TimeUtil.currentTashkentTime());
                homework.setDeadline(dto.getHomework().getDeadline());
                homework.setActive(true);
                homework.setTitle(dto.getHomework().getTitle());
                homework.setDescription(dto.getHomework().getDescription());
                homework.setUpdated(TimeUtil.currentTashkentTime());
                homework.setLesson(lessonOptional.get());


                HomeworkFile hf = new HomeworkFile();
                hf.setFileUrl(saveFile.getData());
                hf.setFileName(fileName);
                hf.setType(type);
                hf.setCreated(TimeUtil.currentTashkentTime());
                hf.setActive(true);
                Homework save = homeworkRepository.save(homework);
                hf.setHomework(save);
                homeworkFileRepository.save(hf);
                return new ResponseDto<>(true, "Ok", findById(save.getId()).getData());
            } else throw new Exception(saveFile.getMessage());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<HomeworkDto> removeFile(RemoveHomeworkFile remove) {
        try {
            Optional<Homework> hOp = homeworkRepository.findById(remove.getHomeworkId());
            if (hOp.isEmpty()) throw new Exception("Not found homework");
            Homework homework = hOp.get();
            if (homeworkRepository.getHomeworkFiles(homework.getId()).size() == 1)
                throw new Exception("This homework assignment cannot be deleted because there is only 1 file left.");
            Optional<HomeworkFile> hfOp = homeworkFileRepository.findById(remove.getHomeworkFileId());
            if (hfOp.isEmpty()) throw new Exception("Not found homework");
            HomeworkFile homeworkFile = hfOp.get();
            homeworkFile.setActive(false);
            homeworkFileRepository.save(homeworkFile);
            return new ResponseDto<>(true, "Ok", findById(hOp.get().getId()).getData());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<HomeworkDto> edit(UUID homeworkId, EditHomeworkDto dto) {
        try {
            Optional<Homework> hOp = homeworkRepository.findById(homeworkId);
            if (hOp.isEmpty())
                throw new Exception("Not found homework");
            Homework homework = hOp.get();
            homework.setTitle(dto.getTitle());
            homework.setDescription(dto.getDescription());
            homework.setDeadline(dto.getDeadline());
            homework.setUpdated(TimeUtil.currentTashkentTime());
            homeworkRepository.save(homework);
            return new ResponseDto<>(true, "Ok", findById(hOp.get().getId()).getData());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> downloadLessonHomeworks(UUID lessonId) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(lessonId);
            if (lOp.isEmpty()) throw new Exception("Not found lesson");
            List<Homework> homeworks = homeworkRepository.getAllHomeworksByLessonId(lessonId);
            if (homeworks.isEmpty()) {
                throw new Exception("Uyga vazifa topilmadi yoki muddati tugagan");
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
            for (Homework homework : homeworks) {
                String folderName = CyrillicToLatinConverter.toLatin(
                        homework.getTitle().replaceAll("[^a-zA-Z0-9]", "_")) + "/";
                List<HomeworkFile> homeworkFiles = homeworkFileRepository.findAllByHomeworkId(homework.getId());
                for (HomeworkFile file : homeworkFiles) {
                    try {
                        String originalFileName = Paths.get(new URI(file.getFileUrl()).getPath()).getFileName().toString();
                        String fullPath = "uploads/files/homeworks/" + originalFileName;

                        File localFile = new File(fullPath);
                        if (!localFile.exists()) {
                            log.warn("Fayl topilmadi: " + fullPath);
                            continue;
                        }
                        ZipEntry zipEntry = new ZipEntry(folderName + originalFileName);
                        zipOutputStream.putNextEntry(zipEntry);
                        try (InputStream inputStream = new FileInputStream(localFile)) {
                            inputStream.transferTo(zipOutputStream);
                        }
                        zipOutputStream.closeEntry();

                    } catch (Exception e) {
                        log.error("Fayl qo‘shishda xatolik: {}", file.getFileUrl(), e);
                    }
                }
            }

            zipOutputStream.close();
            byte[] zipBytes = byteArrayOutputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(zipBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=homeworks_" + lOp.get().getTitle() + ".zip");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(zipBytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            log.error("Xatolik: ", e);
            return ResponseEntity.ok(new ResponseDto<>(false, e.getMessage()));
        }
    }

}
