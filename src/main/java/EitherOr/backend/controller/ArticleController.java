package EitherOr.backend.controller;

import EitherOr.backend.domain.Article;
import EitherOr.backend.dto.ArticleDto;
import EitherOr.backend.service.ArticleService;
import EitherOr.backend.service.ChatGptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ChatGptService chatGptService;
    private final ArticleService articleService;

    @ResponseBody
    @RequestMapping("/add")
    public Object getRecommendedText(ArticleForm articleForm) throws Exception {
        log.info("input = {}", articleForm.generateQuestion());
        log.info("inputCat = {} / {}", articleForm.getCategory().toString(), articleForm.getCategory().size());
        ArticleDto articleDto = new ArticleDto(articleForm);
        Long articleId = articleService.saveArticle(articleDto);
        chatGptService.sendRequest(articleId, articleDto.generateQuestion());
        return articleId;
    }

    @PutMapping("/hit/{articleId}")
    public void articleHitUp(@PathVariable("articleId") Long articleId) {
        articleService.hit(articleId);
    }

    @PutMapping("/unhit/{articleId}")
    public void articleHitDown(@PathVariable("articleId") Long articleId) {
        articleService.unHit(articleId);
    }

    @ResponseBody
    @GetMapping("/{articleId}")
    public Article articleSpecificSingle(@PathVariable("articleId") Long articleId) {
        return articleService.getArticle(articleId);
    }

//    @Async("threadPoolTaskExecutor")
//    public void claimChatGptComment(Long articleId, ArticleDto articleDto) throws JsonProcessingException {
//        String gptResponse = chatGptService.sendRequest(articleDto.generateQuestion()).getChoices().get(0).getMessage().get("content");
//        articleService.updateGptComment(articleId, gptResponse);
//    }


  /*  @ResponseBody
    @RequestMapping("/gpttest/v1")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<String,Object> getRecommendedAdText(ArticleForm articleForm) throws Exception {
        log.info("input = {}", articleForm.generateQuestion());
        Map<String,Object> result = new HashMap<String, Object>();
        Map<String,Object> Header = new HashMap<String, Object>();
        Header.put("Content-Type","application/json");
        Header.put("Authorization", "Bearer " + "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");

        String content = articleForm.generateQuestion();
        List<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
        Map<String,Object> tempMap = Map.of( "role" , "user", "content" , content );
        tempList.add(tempMap);

        Map<String,Object> apiParam = new HashMap<String , Object>();
        apiParam.put("model", "gpt-3.5-turbo");
        apiParam.put("messages", tempList);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(apiParam);


        log.info("request sent");
        String gptResult = sendApi("https://api.openai.com/v1/chat/completions", "POST", Header, json,300000);
        log.info("result={}",gptResult);
        if(gptResult != null) {
            result = mapper.readValue(gptResult,Map.class);
        }

        return result;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String sendApi(String sendUrl, String method ,Map<String, Object> Header ,String jsonValue , int ms) throws Exception  {
        String inputLine = null;
        StringBuffer outResult = new StringBuffer();
        BufferedReader in = null;
        HttpsURLConnection conn = null;

        try{
            URL url = new URL(sendUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            if(method.equals("POST")) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
            }else {
                conn.setDoInput(true);
                conn.setDoOutput(false);
            }

            Iterator<?> it = Header.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)it.next();
                conn.setRequestProperty(entry.getKey().toString(), entry.getValue().toString());
            }

            conn.setConnectTimeout(ms);
            conn.setReadTimeout(ms);

            if(method.equals("POST")) {
                OutputStream os = conn.getOutputStream();
                os.write(jsonValue.getBytes("UTF-8"));
                os.flush();
            }


            // 리턴된 결과 읽기
            int responseCode = conn.getResponseCode();
            log.info("request sent={}", responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) { // 정상 호출 200
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else { // 에러 발생
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }

            conn.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (in != null) {
                in.close();
            } if (conn != null) {
                conn.disconnect();
            }
        }

        return outResult.toString();
    }
*/

}
