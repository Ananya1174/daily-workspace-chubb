package com.chubb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.chubb.request.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {}