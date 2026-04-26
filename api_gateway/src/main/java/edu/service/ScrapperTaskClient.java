package edu.service;

import dto.comment.CommentDTO;
import dto.task.TaskDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapperTaskClient {

    private final WebClient scrapperWebClient;

    private static final Retry RETRY_POLICY = Retry
            .fixedDelay(3, Duration.ofMillis(300))
            .filter(throwable ->
                    throwable instanceof WebClientResponseException.ServiceUnavailable ||
                            throwable instanceof WebClientResponseException.InternalServerError)
            .onRetryExhaustedThrow((spec, signal) -> signal.failure());

    public TaskDTO createTask(String email, TaskDTO taskDTO) {
        log.info("Создание задачи в Scrapper для пользователя: {}", email);

        return scrapperWebClient.post()
                .uri("/task/create")
                .header("X-User-Email", email)
                .bodyValue(taskDTO)
                .retrieve()
                .bodyToMono(TaskDTO.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при создании задачи в Scrapper: {}", e.getMessage()))
                .block();
    }

    public TaskDTO getTaskById(Long taskId) {
        log.info("Получение задачи ID: {} из Scrapper", taskId);
        return scrapperWebClient.get()
                .uri("/task/{id}", taskId)
                .retrieve()
                .bodyToMono(TaskDTO.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при получении задачи из Scrapper: {}", e.getMessage()))
                .block();
    }

    public List<TaskDTO> getRecentTasks(String email) {
        log.info("Получение недавних задач из Scrapper для пользователя: {}", email);

        return scrapperWebClient.get()
                .uri("/task/recent")
                .header("X-User-Email", email)
                .retrieve()
                .bodyToFlux(TaskDTO.class)
                .collectList()
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при получении недавних задач из Scrapper: {}", e.getMessage()))
                .block();
    }

    public void updateLastView(Long taskId) {
        log.info("Обновление времени просмотра задачи ID: {} в Scrapper", taskId);

        scrapperWebClient.put()
                .uri("/task/{id}/view", taskId)
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при обновлении просмотра задачи в Scrapper: {}", e.getMessage()))
                .block();
    }

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        log.info("Обновление задачи ID: {} в Scrapper", taskId);

        return scrapperWebClient.put()
                .uri("/task/{id}/update", taskId)
                .bodyValue(taskDTO)
                .retrieve()
                .bodyToMono(TaskDTO.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при обновлении задачи в Scrapper: {}", e.getMessage()))
                .block();
    }

    public CommentDTO addComment(Long taskId, String email, CommentDTO commentDTO) {
        log.info("Добавление комментария к задаче ID: {} в Scrapper от пользователя: {}", taskId, email);

        return scrapperWebClient.post()
                .uri("/task/{id}/add-comment", taskId)
                .header("X-User-Email", email)
                .bodyValue(commentDTO)
                .retrieve()
                .bodyToMono(CommentDTO.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при добавлении комментария в Scrapper: {}", e.getMessage()))
                .block();
    }

    public List<CommentDTO> getCommentsByTask(Long taskId) {
        log.info("Получение комментариев к задаче ID: {} из Scrapper", taskId);

        return scrapperWebClient.get()
                .uri("/task/{id}/comments", taskId)
                .retrieve()
                .bodyToFlux(CommentDTO.class)
                .collectList()
                .timeout(Duration.ofSeconds(5))
                .retryWhen(RETRY_POLICY)
                .doOnError(e -> log.error("Ошибка при получении комментариев из Scrapper: {}", e.getMessage()))
                .block();
    }
}