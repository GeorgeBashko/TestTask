package com.rest.example.Controller;

import com.rest.example.DTO.AuthDTO;
import com.rest.example.Entities.Patient;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Setter
public class AuthController {
    @Value("${client-id}")
    private String clientId;
    @Value("${resource-url}")
    private String resourceServerUrl;
    @Value("${grant-type}")
    private String grantType;
    @Operation(
            summary = "Authorize user",
            description = "This method receives an AuthDTO object contains login and password. Returns access token."
    )
    @PostMapping("/auth")
    public String auth(@RequestBody AuthDTO authDTO) {
        System.out.println("authDTO: " + authDTO);
        var headers = new HttpHeaders();
        //"&client_secret=x15bJC4AoxawZXOD4MaqkyGsDCsViOJw"
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body = "client_id="+clientId+ "&username="+authDTO.login()+"&password="+authDTO.password() + "&grant_type="+grantType;
        var request = new HttpEntity<Object>(body, headers);
        var restTemplate = new RestTemplate();
        var response = restTemplate.exchange(resourceServerUrl,HttpMethod.POST,request, String.class);
        if (response.getStatusCode().value() == 200){
            return response.getBody();
        }
        return null;
    }

}
