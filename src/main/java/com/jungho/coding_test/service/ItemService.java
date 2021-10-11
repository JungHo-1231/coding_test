package com.jungho.coding_test.service;

import com.jungho.coding_test.domain.item.Book;
import com.jungho.coding_test.domain.item.Item;
import com.jungho.coding_test.dto.item.CreateBookRequestDto;
import com.jungho.coding_test.dto.item.ItemDto;
import com.jungho.coding_test.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Long saveItem(CreateBookRequestDto createBookRequestDto) {
        Book book = modelMapper.map(createBookRequestDto, Book.class);
        Book save = itemRepository.save(book);
        return save.getId();
    }

    public List<ItemDto> findItems() {

        List<Item> items = itemRepository.findAll();
        List<ItemDto> itemDtos = items.stream()
                .map(i -> modelMapper.map(i, ItemDto.class))
                .collect(Collectors.toList());

        return itemDtos;
    }

    public ItemDto fineOne(Long id) {
        Item item = itemRepository.findById(id).
                orElseThrow(() -> new IllegalStateException("아이템을 찾을 수 없습니다"));

        ItemDto itemDto = modelMapper.map(item, ItemDto.class);
        return itemDto;
    }
}

