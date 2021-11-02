import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * @author starblet
 * @create 2021-08-19 22:45
 */

public class AssertTest {

    @Test
    public void test() {
        Object o = null;
        if (o == null) {
            throw new IllegalArgumentException("参数不合法");
        }
    }

    @Test
    public void test2() {
        Object o = null;
        // 使用断言来替代if
        Assert.notNull(o,"参数不合法");
    }
}
