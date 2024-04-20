package com.divum.MeetingRoomBlocker.Implementation;

import com.divum.MeetingRoomBlocker.DTO.OAuthDetailsDTO;
import com.divum.MeetingRoomBlocker.Exception.InvalidDataException;
import com.divum.MeetingRoomBlocker.Service.GoogleOAuthServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class GoogleOAuthServiceImplementation implements GoogleOAuthServices {

    private final ObjectMapper objectMapper;
    @Override
    public String extractEmail(String accessToken) {
        OAuthDetailsDTO oAuthDetailsDTO = getDetails(accessToken);
        if(oAuthDetailsDTO == null) throw new InvalidDataException("Invalid Access Token");
        return oAuthDetailsDTO.getEmail();
    }

    @Override
    public OAuthDetailsDTO extractUser(String accessToken){
        OAuthDetailsDTO oAuthDetailsDTO = getDetails(accessToken);
        if(oAuthDetailsDTO == null) throw new InvalidDataException("Invalid Access Token");
        return oAuthDetailsDTO;
    }

    @Override
    public boolean validateUser(String accessToken, UserDetails userDetails){
        return extractEmail(accessToken).equals(userDetails.getUsername());
    }


    private OAuthDetailsDTO getDetails(String accessToken){
        String tokenInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;

        try{
            URL url = new URL(tokenInfoUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return objectMapper.readValue(response.toString(),OAuthDetailsDTO.class);

            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }
}
