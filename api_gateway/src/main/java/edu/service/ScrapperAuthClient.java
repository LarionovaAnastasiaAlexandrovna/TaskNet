package edu.service;

import dto.GeneraleResponseDTO;
import dto.user.LoginRequestDTO;
import dto.user.RegisterRequestDTO;
import dto.user.RegistrationResponseDTO;
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
public class ScrapperAuthClient {

    private final WebClient scrapperWebClient;

    private static final Retry RETRY_POLICY = Retry
            .fixedDelay(3, Duration.ofMillis(300))
            .filter(throwable ->
                    throwable instanceof WebClientResponseException.ServiceUnavailable ||
                            throwable instanceof WebClientResponseException.InternalServerError)
            .onRetryExhaustedThrow((spec, signal) ->
                    signal.failure()); // выбрасываем последнюю ошибку

    public RegistrationResponseDTO register(RegisterRequestDTO request) {
        log.info("Отправка регистрации в Scrapper: {}", request.getEmail());

        return scrapperWebClient.post()
                .uri("/auth/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(RegistrationResponseDTO.class)
                .timeout(Duration.ofSeconds(3)) // Защита от зависания
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при регистрации в Scrapper: {}", e.getMessage()))
                .block();
    }

    public void login(LoginRequestDTO request) {
        log.info("Отправка логина в Scrapper: {}", request.getEmail());

        scrapperWebClient.post()
                .uri("/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeneraleResponseDTO.class)
                .timeout(Duration.ofSeconds(3))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при логине в Scrapper: {}", e.getMessage()))
                .block();
    }

    public GeneraleResponseDTO verifyEmail(String email) {
        log.info("Отправка верификации email в Scrapper: {}", email);

        return scrapperWebClient.get()
                .uri(uri -> uri.path("/auth/verify")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(GeneraleResponseDTO.class)
                .timeout(Duration.ofSeconds(3))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при верификации email в Scrapper: {}", e.getMessage()))
                .block();
    }
}
