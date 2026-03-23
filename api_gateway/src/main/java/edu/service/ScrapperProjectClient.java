package edu.service;

import dto.project.ProjectDTO;
import dto.project.UserInProjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapperProjectClient {

    private final WebClient scrapperWebClient;

    private static final Retry RETRY_POLICY = Retry
            .fixedDelay(3, Duration.ofMillis(300))
            .filter(throwable ->
                    throwable instanceof WebClientResponseException.ServiceUnavailable ||
                            throwable instanceof WebClientResponseException.InternalServerError)
            .onRetryExhaustedThrow((spec, signal) -> signal.failure());

    public ProjectDTO createProject(String email, ProjectDTO projectDTO) {
        log.info("Создание проекта в Scrapper для пользователя: {}", email);

        return scrapperWebClient.post()
                .uri("/project/create")
                .header("X-User-Email", email)
                .bodyValue(projectDTO)
                .retrieve()
                .bodyToMono(ProjectDTO.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при создании проекта в Scrapper: {}", e.getMessage()))
                .block();
    }

    public List<ProjectDTO> getAllProjects(String email) {
        log.info("Получение всех проектов из Scrapper для пользователя: {}", email);

        return scrapperWebClient.get()
                .uri("/project/all")
                .header("X-User-Email", email)
                .retrieve()
                .bodyToFlux(ProjectDTO.class)
                .collectList()
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при получении всех проектов из Scrapper: {}", e.getMessage()))
                .block();
    }

    public List<UserInProjectDTO> getAllProjectUsers(Long projectId) {
        log.info("Получение пользователей проекта ID: {} из Scrapper", projectId);

        return scrapperWebClient.get()
                .uri("/project/{id}/all-users", projectId)
                .retrieve()
                .bodyToFlux(UserInProjectDTO.class)
                .collectList()
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при получении пользователей проекта из Scrapper: {}", e.getMessage()))
                .block();
    }

    public void addUserToProject(Long projectId, String email) {
        log.info("Добавление пользователя с email: {} в проект ID: {} в Scrapper", email, projectId);

        var emailRequest = Map.of("email", email);

        scrapperWebClient.post()
                .uri("/project/{id}/add-user", projectId)
                .bodyValue(emailRequest)
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при добавлении пользователя в проект в Scrapper: {}", e.getMessage()))
                .block();
    }
}