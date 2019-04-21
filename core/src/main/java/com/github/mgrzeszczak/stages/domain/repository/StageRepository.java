package com.github.mgrzeszczak.stages.domain.repository;

import java.time.ZonedDateTime;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.mgrzeszczak.stages.domain.Stage;
import com.github.mgrzeszczak.stages.domain.StageStatus;

public interface StageRepository extends BaseRepository<Stage> {

    Page<Stage> findByStatusInAndScheduledDateBefore(Collection<StageStatus> statuses, ZonedDateTime scheduledDate, Pageable pageable);

}
