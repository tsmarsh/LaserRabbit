package com.tailoredshapes.laser;

import com.tailoredshapes.stash.Stash;

public class GitRepository implements MetaRepository<String, String, Stash> {
    @Override
    public Repository<String, Stash> repositoryFor(String root) {
        return null;
    }
}
