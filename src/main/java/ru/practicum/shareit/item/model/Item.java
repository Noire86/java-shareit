package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * // Базовая модель итема
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    @NotNull
    private Boolean available;

    @Column
    private Integer owner;

    @Column
    private Integer request;


}
