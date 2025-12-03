package edu.service;

import dto.user.ProfileResponseDTO;
import dto.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapperProfileClient {

    private final WebClient scrapperWebClient;

    private static final Retry RETRY_POLICY = Retry
            .fixedDelay(3, Duration.ofMillis(300))
            .filter(throwable ->
                    throwable instanceof WebClientResponseException.ServiceUnavailable ||
                            throwable instanceof WebClientResponseException.InternalServerError)
            .onRetryExhaustedThrow((spec, signal) -> signal.failure());

    public ProfileResponseDTO getUserProfile(String email) {
        log.info("Запрос профиля из Scrapper для пользователя: {}", email);

        return scrapperWebClient.get()
                .uri("/profile")
                .header("X-User-Email", email)
                .retrieve()
                .bodyToMono(ProfileResponseDTO.class)
                .timeout(Duration.ofSeconds(3))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при получении профиля из Scrapper: {}", e.getMessage()))
                .block();
    }

    public UserDTO updateUserProfile(UserDTO userDTO) {
        log.info("Обновление профиля в Scrapper для пользователя: {}", userDTO.getEmail());

        return scrapperWebClient.put()
                .uri("/profile/update")
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .timeout(Duration.ofSeconds(3))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при обновлении профиля в Scrapper: {}", e.getMessage()))
                .block();
    }
}