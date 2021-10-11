package com.jungho.coding_test.repository;

import com.jungho.coding_test.domain.item.Book;
import com.jungho.coding_test.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
