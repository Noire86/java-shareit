package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Value;

/**
 * // TODO .
 */
@Value
@Builder(toBuilder = true)
public class Item {

    Integer id;
    String name;
    String description;
    boolean available;
    Integer owner;
    String request;


}
