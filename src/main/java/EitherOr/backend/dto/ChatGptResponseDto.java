package EitherOr.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ChatGptResponseDto {
    private String id;
    private String object;
    private LocalDate created;
    private String model;
    private List<ChatGptResponseChoicesDto> choices;

    @Builder
    public ChatGptResponseDto(String id, String object, LocalDate created, String model, List<ChatGptResponseChoicesDto> choices){
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choices = choices;
    }

}
