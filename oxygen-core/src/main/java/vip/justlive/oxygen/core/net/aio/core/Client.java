/*
 * Copyright (C) 2019 justlive1
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License
 *  is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing permissions and limitations under
 *  the License.
 */

package vip.justlive.oxygen.core.net.aio.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.TimeUnit;
import lombok.Data;

/**
 * aio 客户端
 *
 * @author wubo
 */
@Data
public class Client {

  private final GroupContext groupContext;
  private final BeatProcessor beatProcessor;
  private final RetryProcessor retryProcessor;
  private ChannelContext channelContext;

  public Client(GroupContext groupContext) {
    this.groupContext = groupContext;
    this.beatProcessor = new BeatProcessor(this);
    this.retryProcessor = new RetryProcessor(this);
  }

  /**
   * 连接服务端
   *
   * @param host 主机
   * @param port 端口
   * @throws IOException io异常时抛出
   */
  public void connect(String host, int port) throws IOException {
    connect(new InetSocketAddress(host, port));
  }

  /**
   * 连接服务端
   *
   * @param address 地址
   * @throws IOException io异常时抛出
   */
  public void connect(InetSocketAddress address) throws IOException {
    groupContext.setServerAddress(address);
    AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup
        .withThreadPool(groupContext.getGroupExecutor());
    groupContext.setChannelGroup(channelGroup);
    AsynchronousSocketChannel channel = Utils.create(groupContext);
    channelContext = new ChannelContext(groupContext, channel, false);
    channel.connect(address, channelContext, ConnectHandler.INSTANCE);

    if (groupContext.getAioHandler().beat(channelContext) != null) {
      groupContext.getScheduledExecutor()
          .schedule(beatProcessor, groupContext.getBeatInterval(), TimeUnit.MILLISECONDS);
    }
    if (groupContext.isRetryEnabled()) {
      groupContext.getScheduledExecutor()
          .schedule(retryProcessor, groupContext.getRetryInterval(), TimeUnit.MILLISECONDS);
    }
  }

  /**
   * 关闭客户端
   */
  public void close() {
    if (channelContext != null) {
      channelContext.close();
      groupContext.close();
    }
  }

  /**
   * 向服务端写入数据
   *
   * @param data 数据
   */
  public void write(Object data) {
    if (channelContext != null) {
      channelContext.write(data);
    }
  }
}
