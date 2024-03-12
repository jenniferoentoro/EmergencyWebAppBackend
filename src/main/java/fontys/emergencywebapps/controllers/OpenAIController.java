package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.controllers.dto.ChatGPTResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import fontys.emergencywebapps.business.MessageUseCases;
import fontys.emergencywebapps.controllers.dto.ChatGPTRequest;
import fontys.emergencywebapps.controllers.dto.MessageRequest;

@RestController
@RequestMapping("/openai")
@AllArgsConstructor
public class OpenAIController {

    private static final String MODEL = "gpt-3.5-turbo";

   private   static final String URL = "https://api.openai.com/v1/chat/completions";

    final MessageUseCases messageUseCases;


    private final RestTemplate template;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt, HttpServletRequest httpServletRequest) {
        ChatGPTRequest request = new ChatGPTRequest(MODEL, prompt);
        ChatGPTResponse response = template.postForObject(URL, request, ChatGPTResponse.class);


        assert response != null;
        String msgResponse = response.getChoices().get(0).getMessage().getContent();

        messageUseCases.openAIMessage(MessageRequest.builder().message(msgResponse).build(), httpServletRequest);

        return msgResponse;
    }
}
