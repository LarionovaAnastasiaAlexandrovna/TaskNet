package dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import enums.ProjectStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectDTO {
    @JsonProperty("projectId")
    Long projectId;

    @JsonProperty("projectName")
    String projectName;

    @JsonProperty("description")
    String description;

    @JsonProperty("startDate")
    LocalDate startDate;

    @JsonProperty("endDate")
    LocalDate endDate;

    @JsonProperty("status")
    ProjectStatus projectStatus;
}
