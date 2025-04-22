package dto;

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

    private String projectName;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ProjectStatus projectStatus;
}
