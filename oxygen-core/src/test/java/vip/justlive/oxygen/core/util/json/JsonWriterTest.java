package vip.justlive.oxygen.core.util.json;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import vip.justlive.oxygen.core.util.MoreObjects;

/**
 * @author wubo
 */
public class JsonWriterTest {

  @Test
  public void toJson() {

    // Primitive
    Assert.assertEquals("1", Json.toJson(1));
    Assert.assertEquals("23.3", Json.toJson(23.3f));
    Assert.assertEquals("9024512353132414", Json.toJson(9024512353132414L));
    Assert.assertEquals("true", Json.toJson(true));

    Assert.assertEquals("null", Json.toJson(null));

    Date date = new Date();
    Assert.assertEquals(Long.toString(date.getTime()), Json.toJson(date));
    LocalDateTime time = LocalDateTime.now();
    Assert.assertEquals("\"" + time.toString() + "\"", Json.toJson(time));

    Bean bean = new Bean();
    bean.a = 1;
    bean.b = true;
    bean.c = time;

    Assert.assertEquals("{\"a\":1,\"b\":true,\"c\":\"" + time.toString() + "\",\"d\":null}",
        Json.toJson(bean));

    Map<String, Object> map = MoreObjects.mapOf("a", 1, "b", true, "c", time);
    Assert.assertEquals("{\"a\":1,\"b\":true,\"c\":\"" + time.toString() + "\"}", Json.toJson(map));

    List<Object> list = Arrays.asList(1, 2, 3, bean);
    Assert.assertEquals("[1,2,3,{\"a\":1,\"b\":true,\"c\":\"" + time.toString() + "\",\"d\":null}]",
        Json.toJson(list));

    int[] array = new int[]{3, 4, 5};
    Assert.assertEquals("[3,4,5]", Json.toJson(array));

    Assert.assertEquals("\"UTF-8\"", Json.toJson(StandardCharsets.UTF_8));
    Assert.assertEquals("\"Asia/Shanghai\"", Json.toJson(ZoneId.of("Asia/Shanghai")));
    Assert.assertEquals("1", Json.toJson(new AtomicReference<>(1)));
    Assert.assertEquals("[1,2]", Json.toJson(new AtomicIntegerArray(new int[]{1, 2})));

  }

  @Data
  public static class Bean {

    private int a;
    private boolean b;
    private LocalDateTime c;
    private Date d;
  }
}