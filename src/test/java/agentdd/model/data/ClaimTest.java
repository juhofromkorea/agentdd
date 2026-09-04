package agentdd.model.data;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClaimTest {
       private Claim target;

    @BeforeEach
    public void setUp() throws Exception {
       
        target = new Claim();
    }
   @Test
public void testRatesMaster_料率0() {
    // 期待値の定義
    String expected = "0.0倍";

    // テスト対象メソッドの呼び出し
    String actual = target.ratesMaster(0);

    // 検証
    assertEquals(expected, actual);
}

@Test
public void testRatesMaster_料率1() {
    // 期待値の定義
    String expected = "1.0倍";

    // テスト対象メソッドの呼び出し
    String actual = target.ratesMaster(1);

    // 検証
    assertEquals(expected, actual);
}

@Test
public void testRatesMaster_料率2() {
    // 期待値の定義
    String expected = "1.2倍";

    // テスト対象メソッドの呼び出し
    String actual = target.ratesMaster(2);

    // 検証
    assertEquals(expected, actual);
}

@Test
public void testRatesMaster_料率3() {
    // 期待値の定義
    String expected = "1.4倍";

    // テスト対象メソッドの呼び出し
    String actual = target.ratesMaster(3);

    // 検証
    assertEquals(expected, actual);
}

@Test
public void testRatesMaster_料率4() {
    // 期待値の定義
    String expected = "1.6倍";

    // テスト対象メソッドの呼び出し
    String actual = target.ratesMaster(4);

    // 検証
    assertEquals(expected, actual);
}

@Test
public void testRatesMaster_料率5() {
    // 期待値の定義
    String expected = "1.9倍";

    // テスト対象メソッドの呼び出し
    String actual = target.ratesMaster(5);

    // 検証
    assertEquals(expected, actual);
}
    @Test
public void testAgeMaster_年齢条件1() {
    // 期待値の定義
    String expected = "無制限";

    // テスト対象メソッドの呼び出し
    String actual = target.ageMaster(1);

    // 検証
    assertEquals(expected, actual);
}

@Test
public void testAgeMaster_年齢条件2() {
    // 期待値の定義
    String expected = "21歳以上";

    // テスト対象メソッドの呼び出し
    String actual = target.ageMaster(2);

    // 検証
    assertEquals(expected, actual);
}

@Test
public void testAgeMaster_年齢条件3() {
    // 期待値の定義
    String expected = "26歳以上";

    // テスト対象メソッドの呼び出し
    String actual = target.ageMaster(3);

    // 検証
    assertEquals(expected, actual);
}
@Test
public void testLicenseColorMaster_免許証色1() {
    // 期待値の定義
    String expected = "ブルー";

    // テスト対象メソッドの呼び出し
    String actual = target.licenseColorMaster(1);

    // 検証
    assertEquals(expected, actual);
}

@Test
public void testLicenseColorMaster_免許証色2() {
    // 期待値の定義
    String expected = "グリーン";

    // テスト対象メソッドの呼び出し
    String actual = target.licenseColorMaster(2);

    // 検証
    assertEquals(expected, actual);
}

@Test
public void testLicenseColorMaster_免許証色3() {
    // 期待値の定義
    String expected = "ゴールド";

    // テスト対象メソッドの呼び出し
    String actual = target.licenseColorMaster(3);

    // 検証
    assertEquals(expected, actual);
}
}
