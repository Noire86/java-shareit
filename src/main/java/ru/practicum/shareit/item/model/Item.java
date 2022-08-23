package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Value;

/**
 * // Базовая модель итема
 */
@Value
@Builder(toBuilder = true)
public class Item {

    Integer id;
    String name;
    String description;
    Boolean available;
    Integer owner;
    String request;


}
