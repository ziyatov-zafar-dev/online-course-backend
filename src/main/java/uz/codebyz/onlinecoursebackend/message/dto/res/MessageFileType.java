package uz.codebyz.onlinecoursebackend.message.dto.res;

import java.util.List;

public class MessageFileType {
    private String name;
    private String description;
    private List<String> mimeTypes;

    public String getName() {
        return name;
    }

    public MessageFileType() {
    }

    public MessageFileType(String name, String description, List<String> mimeTypes) {
        this.name = name;
        this.description = description;
        this.mimeTypes = mimeTypes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(List<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }
}
