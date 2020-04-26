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
package vip.justlive.oxygen.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import vip.justlive.oxygen.aop.annotation.Aspect;
import vip.justlive.oxygen.aop.annotation.Aspect.TYPE;
import vip.justlive.oxygen.aop.interceptor.AnnotationInterceptor;
import vip.justlive.oxygen.aop.interceptor.MethodInterceptor;
import vip.justlive.oxygen.aop.proxy.ProxyStore;
import vip.justlive.oxygen.core.Plugin;
import vip.justlive.oxygen.core.exception.Exceptions;
import vip.justlive.oxygen.core.util.ClassUtils;
import vip.justlive.oxygen.ioc.IocPlugin;

/**
 * aop插件
 *
 * @author wubo
 */
public class AopPlugin implements Plugin {

  @Override
  public int order() {
    return Integer.MIN_VALUE + 100;
  }

  @Override
  public void start() {
    IocPlugin.beanStore().getBeans().forEach(
        bean -> ClassUtils.getMethodsAnnotatedWith(bean.getClass(), Aspect.class)
            .forEach(method -> handleMethod(method, bean)));
    ProxyStore.init();
  }

  @Override
  public void stop() {
    ProxyStore.clean();
  }

  private void handleMethod(Method method, Object bean) {
    Aspect aspect = method.getAnnotation(Aspect.class);
    AopWrapper wrapper;
    try {
      wrapper = new AopWrapper(bean,
          bean.getClass().getMethod(method.getName(), method.getParameterTypes()), aspect.order());
    } catch (NoSuchMethodException e) {
      throw Exceptions.wrap(e);
    }
    for (TYPE type : aspect.type()) {
      if (aspect.annotation() != Annotation.class) {
        ProxyStore.addInterceptor(type, new AnnotationInterceptor(aspect.annotation(), wrapper));
      }
      if (aspect.method().length() > 0) {
        ProxyStore.addInterceptor(type, new MethodInterceptor(aspect.method(), wrapper));
      }
    }
  }

}
