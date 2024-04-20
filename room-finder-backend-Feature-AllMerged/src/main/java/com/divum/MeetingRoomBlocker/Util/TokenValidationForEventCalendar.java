package com.divum.MeetingRoomBlocker.Util;

import com.divum.MeetingRoomBlocker.Config.Oauth2Config;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.net.URLEncoder;

@Component
public class TokenValidationForEventCalendar {
private final Oauth2Config oauth2Config;
    @Autowired
    public TokenValidationForEventCalendar(Oauth2Config oauth2Config) {
        this.oauth2Config = oauth2Config;
    }
    public boolean isAccessTokenValid(String accessToken) {
    accessToken = accessToken.trim();
    String endPoint = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + accessToken;
    RestTemplate restTemplate = new RestTemplate();

    try {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(endPoint, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            JSONObject jsonResponse = (JSONObject) JSONValue.parse(responseBody);
            String expiresInString = jsonResponse.get("expires_in").toString();
            int expiresIn = Integer.parseInt(expiresInString);
            return expiresIn > 100;
        } else {
            return false;
        }
    } catch (HttpClientErrorException ex) {
        return false;
    } catch (Exception ex) {

        ex.printStackTrace();
        return false;
    }
}

    public String refreshAccessToken(String refreshToken) {
        try {
            String clientId=oauth2Config.getClientId();
            String clientSecret=oauth2Config.getClientSecret();

            String requestBody = "client_id="+clientId+
                    "&client_secret="+clientSecret+
                    "&grant_type=refresh_token"+
                    "&refresh_token=" + URLEncoder.encode(refreshToken, "UTF-8")
                    ;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token", requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                JSONObject jsonObject = (JSONObject) JSONValue.parse(responseBody);
                String accessToken = jsonObject.get("access_token").toString();
                return accessToken;
            } else {
                 throw new RuntimeException("The AccessToken Not Obtained");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("The Request is Invalid");
        }
    }
    }

