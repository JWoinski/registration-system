package com.school.registrationsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
    private String name;
    private int courseIndex;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}