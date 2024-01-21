package com.school.registrationsystem.model.dto;

import com.school.registrationsystem.validator.date.DateValid;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CourseDto {
    @NonNull
    private String name;
    @NonNull
    @DateValid
    private LocalDate startDate;
    @NonNull
    @DateValid
    private LocalDate endDate;
    @NonNull
    private String description;
}