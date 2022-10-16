package ru.practicum.shareit.requests.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemResponseDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;

}
