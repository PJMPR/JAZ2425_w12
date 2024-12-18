package com.users.updater;

import org.springframework.scheduling.annotation.Async;

public interface IUpdate {

    @Async
    void update(int size);
}
