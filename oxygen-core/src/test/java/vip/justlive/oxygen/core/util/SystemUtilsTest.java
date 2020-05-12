/*
 * Copyright (C) 2020 the original author or authors.
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
package vip.justlive.oxygen.core.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author wubo
 */
public class SystemUtilsTest {

  @Test
  public void test() {
    Assert.assertTrue(SystemUtils.isValidPort(10000));
    Assert.assertFalse(SystemUtils.isValidPort(-2));
    Assert.assertFalse(SystemUtils.isValidPort(65536));

    SystemUtils.findAvailablePort();
    InetAddress address = SystemUtils.getLocalAddress();

    Assert.assertTrue(SystemUtils.pid() > -1);

    Assert.assertEquals(new InetSocketAddress(address.getHostAddress(), 8080),
        SystemUtils.parseAddress(address.getHostAddress() + ":8080"));

  }
}