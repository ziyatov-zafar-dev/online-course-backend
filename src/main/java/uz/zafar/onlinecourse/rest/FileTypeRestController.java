package uz.zafar.onlinecourse.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.db.domain.Type;
import uz.zafar.onlinecourse.db.repository.TypeRepository;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.fileTypeDto.AddFileTypeRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class FileTypeRestController {
    private final TypeRepository typeRepository;

    public FileTypeRestController(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }


    @GetMapping("api/file-types/list")
    public ResponseEntity<ResponseDto<?>> findAll() {
        try {
            List<Type> types = typeRepository.findAllByActive(true);
            return ResponseEntity.ok(new ResponseDto<>(true, "Success", types));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto<>(false, "Error", e));
        }
    }

    @PostMapping("api/file-types/add-file-type")
    public ResponseEntity<ResponseDto<?>> addFileType(@RequestBody AddFileTypeRequestDto fileType) {
        try {
            Type type = typeRepository.findByNameAndActive(fileType.getFileType(), true);
            if (type != null) {
                throw new Exception("file type already exists");
            }
            type = new Type();
            type.setActive(true);
            type.setHomeworkFiles(new ArrayList<>());
            type.setLessonFiles(new ArrayList<>());
            type.setName(fileType.getFileType());
            Type save = typeRepository.save(type);
            return ResponseEntity.ok(new ResponseDto<>(true, "Success", save));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto<>(false, "Error", e.getMessage()));
        }
    }

    @DeleteMapping("api/file-types/delete-file-type/{typeId}")
    public ResponseEntity<ResponseDto<?>> deleteFileType(@PathVariable("typeId") Short id) {
        try {
            Optional<Type> tOp = typeRepository.findById(id);
            if (tOp.isEmpty())
                throw new Exception("file type not found");
            Type type = tOp.get();
            if (!type.getActive())
                throw new Exception("file type not found");
            type.setActive(false);
            type.setName(UUID.randomUUID() + "" + type.getId());
            Type save = typeRepository.save(type);
            return ResponseEntity.ok(new ResponseDto<>(true, "Successfully deleted"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto<>(false, "Error", e.getMessage()));
        }
    }

    @PutMapping("api/file-types/edit-file-type/{typeId}")
    public ResponseEntity<ResponseDto<?>> editFileType(@PathVariable("typeId") Short id, @RequestParam("type") String fileType) {
        try {
            Optional<Type> tOp = typeRepository.findById(id);
            if (tOp.isEmpty())
                throw new Exception("file type not found");
            Type type = tOp.get();
            if (!type.getActive())
                throw new Exception("file type not found");
            type.setName(fileType);
            Type save = typeRepository.save(type);
            return ResponseEntity.ok(new ResponseDto<>(true, "Successfully edited", save));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto<>(false, "Error", e.getMessage()));
        }
    }
}
