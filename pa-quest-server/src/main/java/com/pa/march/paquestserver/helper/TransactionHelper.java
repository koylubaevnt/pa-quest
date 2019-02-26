package com.pa.march.paquestserver.helper;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Supplier;

@Service
public class TransactionHelper {

    @Transactional
    public <T> T withTrabsaction(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional
    public void  withTrabsaction(Runnable runnable) {
        runnable.run();
    }
}
