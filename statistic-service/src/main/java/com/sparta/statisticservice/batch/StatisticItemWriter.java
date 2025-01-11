package com.sparta.statisticservice.batch;

import com.sparta.statisticservice.entity.Statistic;
import com.sparta.statisticservice.repository.StatisticRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticItemWriter implements ItemWriter<Statistic> {

    private final StatisticRepository statisticRepository;

    public StatisticItemWriter(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void write(Chunk<? extends Statistic> chunk) throws Exception {
        List<? extends Statistic> statistics = chunk.getItems();
        statisticRepository.saveAll(statistics);
        System.out.println("Saved Statistics: " + statistics.size() + " records.");
    }
}