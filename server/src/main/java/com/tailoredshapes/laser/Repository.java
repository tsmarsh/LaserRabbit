package com.tailoredshapes.laser;

import java.util.List;

public interface Repository<I, T> {

    T create(T form);

    T read(I id);

    List<T> read();

    T update(I id, T form);

    T delete(I id);
}
