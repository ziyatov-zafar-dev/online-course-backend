package uz.codebyz.onlinecoursebackend.telegram.dto;

public class ButtonDto {

    private String text;
    private ButtonType type;
    private String value; // callback_data yoki url

    public ButtonDto(String text, ButtonType type) {
        this.text = text;
        this.type = type;
    }

    public ButtonDto(String text, ButtonType type, String value) {
        this.text = text;
        this.type = type;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public ButtonType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(ButtonType type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
