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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

/**
 * @author wubo
 */
public class FileCleanerTest {

  @Test
  public void test() {
    File file = new File("./" + SnowflakeIdWorker.defaultNextId());
    Path path = Paths.get(".", SnowflakeIdWorker.defaultNextId() + "");
    try (FileCleaner cleaner = new FileCleaner()) {
      FileUtils.touch(file);
      assertTrue(file.exists());
      cleaner.track(file);
      FileUtils.touch(path);
      assertTrue(path.toFile().exists());
      cleaner.track(path);
    }
    assertFalse(file.exists());
    assertFalse(path.toFile().exists());
  }
}