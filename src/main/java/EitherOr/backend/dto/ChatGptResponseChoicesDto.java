package EitherOr.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Getter
public class ChatGptResponseChoicesDto {
    private Map<String, String> message;
    private String finish_reason;
    private int index;

    @Builder
    ChatGptResponseChoicesDto(Map<String, String> message, String finish_reason, int index){
        this.message = message;
        this.finish_reason = finish_reason;
        this.index = index;
    }
}
