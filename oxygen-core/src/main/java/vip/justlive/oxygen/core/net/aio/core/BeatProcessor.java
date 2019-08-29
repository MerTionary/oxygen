/*
 * Copyright (C) 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package vip.justlive.oxygen.core.net.aio.core;

import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;

/**
 * 心跳任务
 *
 * @author wubo
 */
@AllArgsConstructor
public class BeatProcessor implements Runnable {

  private final Client client;

  @Override
  public void run() {
    if (client.getGroupContext().isStopped()) {
      return;
    }
    try {
      if (client.getChannelContext() == null || client.getChannelContext().isClosed()) {
        return;
      }

      long lastActiveAt = Math.max(client.getChannelContext().getLastReceivedAt(),
          client.getChannelContext().getLastSentAt());
      if (System.currentTimeMillis() - lastActiveAt >= client.getGroupContext().getBeatInterval()) {
        Object beat = client.getGroupContext().getAioHandler().beat(client.getChannelContext());
        if (beat == null) {
          return;
        }
        client.getChannelContext().write(beat);
      }
    } finally {
      client.getGroupContext().getScheduledExecutor()
          .schedule(this, client.getGroupContext().getBeatInterval(), TimeUnit.MILLISECONDS);
    }
  }
}
