package EitherOr.backend;

import org.springframework.beans.factory.annotation.Value;

public class ChatGptConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String API_KEY = "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";
    public static final String MODEL = "gpt-3.5-turbo";
    public static final Double TEMPERATURE = 0.0;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String URL = "https://api.openai.com/v1/chat/completions";
}
