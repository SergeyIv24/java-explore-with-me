package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.statistic.model.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {


}
