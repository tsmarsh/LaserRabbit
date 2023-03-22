package com.tailoredshapes.laser;

public interface MetaRepository<T, I, U> {
    Repository<I, U> repositoryFor(T root);
}
