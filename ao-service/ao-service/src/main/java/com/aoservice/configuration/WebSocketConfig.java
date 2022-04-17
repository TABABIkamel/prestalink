package com.aoservice.configuration;

import org.apache.catalina.Context;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Bean
    public TomcatServletWebServerFactory tomcatContainerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();;
        factory.setTomcatContextCustomizers(Collections.singletonList(tomcatContextCustomizer()));
        return factory;
    }

    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.addServletContainerInitializer(new WsSci(), null);
            }
        };
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/notification");
        registry.setApplicationDestinationPrefixes("/swns");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/notifications")
                .setAllowedOrigins("http://localhost:4200", "http://127.0.0.1:4200")
                .withSockJS();
    }

}
