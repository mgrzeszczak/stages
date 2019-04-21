package com.github.mgrzeszczak.stages.domain.repository;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.github.mgrzeszczak.stages.domain.BaseEntity;
import com.github.mgrzeszczak.stages.domain.Id;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Id> {

    default T findOrThrow(Id id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("entity not found: " + id.toString()));
    }

}
