package com.LiveConnect.service.Configuration;

import com.LiveConnect.repository.UserRepository;
import com.LiveConnect.service.security.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtWebSocketHandlerInterceptor implements ChannelInterceptor {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        Object tokenObj = message.getHeaders().get("nativeHeaders");
        if (tokenObj instanceof Map) {
            Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) tokenObj;
            List<String> authHeader = nativeHeaders.get("Authorization");

            if (authHeader != null && !authHeader.isEmpty()) {
                String token = authHeader.get(0).replace("Bearer ", "");
                String subject = validateToken(token);

                if (!token.isEmpty() && !subject.isEmpty()) {
                    var userAuthentication = userRepository.findByUsername(subject).get();
                    var authentication = new UsernamePasswordAuthenticationToken(userAuthentication, null, userAuthentication.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return message;
                }
            }
        }
        throw new AccessDeniedException("Token is not valid");
    }

    private String validateToken(String token) {
        try {
            return tokenService.getSubject(token);
        } catch (Exception e) {
            return "";
        }
    }

}