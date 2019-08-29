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
package vip.justlive.oxygen.aop.proxy;

import org.junit.Assert;
import org.junit.Test;
import vip.justlive.oxygen.aop.annotation.Aspect.TYPE;

/**
 * @author wubo
 */
public class ProxyStoreTest {

  @Test
  public void test() {
    NoLogService service = CglibProxy.proxy(NoLogService.class);
    ProxyStore.addInterceptor(TYPE.BEFORE, new NoLogInterceptor());
    ProxyStore.addInterceptor(TYPE.AFTER, new NoLogInterceptor());
    service.log();
    Assert.assertEquals(((1 * 10 + 2) * 100 + 1) * 10 + 2, NoLogService.ato.get());
  }
}
