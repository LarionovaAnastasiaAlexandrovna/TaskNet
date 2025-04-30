package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    @JsonProperty("projectId")
    private Long projectId;

    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("startDate")
    private LocalDate startDate;

    @JsonProperty("endDate")
    private LocalDate endDate;

    @JsonProperty("status")
    private ProjectStatus projectStatus;
}
