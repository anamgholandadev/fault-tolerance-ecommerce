package com.ufrn.faulttolerance.ecommerce.services;

import com.ufrn.faulttolerance.ecommerce.model.dto.BonusRequestDTO;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class RequestQueueService {

    private final BlockingQueue<BonusRequestDTO> queue = new LinkedBlockingQueue<>();

    public  BonusRequestDTO peek() {
        return queue.peek();
    }

    public void enqueue(BonusRequestDTO bonusRequestDTO) {
        queue.add(bonusRequestDTO);
    }

    public BonusRequestDTO dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void clear() {
        queue.clear();
    }

    public int size() {
        return queue.size();
    }

}
