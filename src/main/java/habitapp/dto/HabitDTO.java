package habitapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import habitapp.enums.HabitFrequency;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HabitDTO {
    private String name;
    private String description;
    private HabitFrequency frequency;
    @JsonProperty(defaultValue = "false")
    private boolean completed;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime = LocalDateTime.now();


}
