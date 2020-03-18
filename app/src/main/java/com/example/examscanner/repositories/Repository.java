package com.example.examscanner.repositories;

import java.util.List;
import java.util.function.Predicate;

public interface Repository<T> {
    public int getId();
    public T get(int id);
    public List<T> get(Predicate<T> criteria);
    public void create(T t);
    public void update(T t);
    public void delete(int id);
}
