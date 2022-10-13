package ru.practicum.shareit.requests.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestDAO extends JpaRepository<ItemRequest, Integer> {

    @Query("select ir from ItemRequest as ir where ir.requestor.id = ?1 order by ir.created desc ")
    List<ItemRequest> getItemRequestsByRequestor(Integer requestorId);

    @Query("select ir from ItemRequest as ir order by ir.created desc")
    List<ItemRequest> getAllItemRequests(Pageable pageable);
}
