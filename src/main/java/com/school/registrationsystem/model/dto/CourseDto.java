package com.school.registrationsystem.model.dto;

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
    private LocalDate startDate;
    @NonNull
    private LocalDate endDate;
    @NonNull
    private String description;
}