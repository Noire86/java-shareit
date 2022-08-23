package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * // TODO .
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
