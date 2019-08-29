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
package vip.justlive.oxygen.web.servlet;

import javax.servlet.ServletContext;

/**
 * web application 启动初始接口
 *
 * @author wubo
 */
public interface WebAppInitializer extends Comparable<WebAppInitializer> {

  /**
   * 启动执行
   *
   * @param context servlet上下文
   */
  void onStartup(ServletContext context);

  /**
   * 加载顺序，默认0，越小越先加载
   *
   * @return order
   */
  default int order() {
    return 0;
  }

  /**
   * sort compare
   *
   * @param o compare object
   * @return result
   */
  @Override
  default int compareTo(WebAppInitializer o) {
    return Integer.compare(order(), o.order());
  }
}
