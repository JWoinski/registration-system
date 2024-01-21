package com.school.registrationsystem.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    @NonNull
    private String name;
    @NonNull
    private String surname;
}
