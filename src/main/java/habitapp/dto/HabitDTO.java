package habitapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import habitapp.services.enums.HabitFrequency;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HabitDTO {
    private String name;

    private String description;

    private HabitFrequency frequenсy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @JsonProperty(defaultValue = "false")
    private boolean isComplited;

}
