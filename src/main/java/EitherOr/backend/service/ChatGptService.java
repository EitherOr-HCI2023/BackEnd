package EitherOr.backend.service;

import EitherOr.backend.ChatGptConfig;
import EitherOr.backend.dto.ChatGptRequestDto;
import EitherOr.backend.dto.ChatGptResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatGptService {


    private static RestTemplate restTemplate = new RestTemplate();
    private final ArticleService articleService;




    @Async("threadPoolTaskExecutor")
    public void sendRequest(Long articleId, String question) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + ChatGptConfig.API_KEY);
        HttpEntity<ChatGptRequestDto> httpEntity = new HttpEntity<>(new ChatGptRequestDto(question), headers);

        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(ChatGptConfig.URL, httpEntity, ChatGptResponseDto.class);
        log.info("\n======================\n{} 에 대한 response 도착! \n======================\n{}", question, responseEntity.getBody().getChoices().get(0).getMessage().get("content"));


        String gptResponse = responseEntity.getBody().getChoices().get(0).getMessage().get("content");;
        articleService.updateGptComment(articleId, gptResponse);
    }
}
