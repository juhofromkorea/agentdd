package agentdd.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContractTest {
    private Contract target;
    @BeforeEach
public void setUp() throws Exception {
    // テスト対象オブジェクトの取得
    target = new Contract();
}
/**
 * 支払方法「直接集金」のテスト。<br>
 * <p>
 * 概要：支払方法が「直接集金」として結果が返ってくることを確認する。
 * 条件：支払方法に1を設定
 * 結果：直接集金
 * </p>
 */
@Test
public void testPayMaster_直接集金() {
    // 期待値の定義
    String expected = "直接集金";

    // テスト対象メソッドの呼び出し
    target.setPaymentMethod(1);
    String actual = target.payMaster();

    // 検証
    assertEquals(expected, actual);
}
/**
 * 支払方法「口座振替」のテスト。<br>
 * <p>
 * 概要：支払方法が「口座振替」として結果が返ってくることを確認する。
 * 条件：支払方法に2を設定
 * 結果：口座振替
 * </p>
 */
@Test
public void testPayMaster_口座振替() {
    // 期待値の定義
    String expected = "口座振替";

    // テスト対象メソッドの呼び出し
    target.setPaymentMethod(2);
    String actual = target.payMaster();

    // 検証
    assertEquals(expected, actual);
}
/**
 * 支払方法「クレジットカード」のテスト。<br>
 * <p>
 * 概要：支払方法が「クレジットカード」として結果が返ってくることを確認する。
 * 条件：支払方法に3を設定
 * 結果：クレジットカード
 * </p>
 */
@Test
public void testPayMaster_クレジットカード() {
    // 期待値の定義
    String expected = "クレジットカード";

    // テスト対象メソッドの呼び出し
    target.setPaymentMethod(3);
    String actual = target.payMaster();

    // 検証
    assertEquals(expected, actual);
}


/**
 * 性別「男性」のテスト。<br>
 * <p>
 * 概要：性別が「男性」として結果が返ってくることを確認する。
 * 条件：男性に1を設定
 * 結果：男性
 * </p>
 */
@Test
public void testgenderMaster_男性() {
    // 期待値の定義
    String expected = "男性";

    // テスト対象メソッドの呼び出し
    target.setGender(1);
    String actual = target.genderMaster();

    // 検証
    assertEquals(expected, actual);
}
/**
 * 性別「女性」のテスト。<br>
 * <p>
 * 概要：性別が「女性」として結果が返ってくることを確認する。
 * 条件：女性に1を設定
 * 結果：女性
 * </p>
 */
@Test
public void testgenderMaster_女性() {
    // 期待値の定義
    String expected = "女性";

    // テスト対象メソッドの呼び出し
    target.setGender(2);
    String actual = target.genderMaster();

    // 検証
    assertEquals(expected, actual);
}
/**
 * 被保険者区分「個人」のテスト。<br>
 * <p>
 * 概要：被保険者区分が「個人」として結果が返ってくることを確認する。
 * 条件：被保険者区分に1を設定
 * 結果：個人
 * </p>
 */
@Test
public void testginsuredTypeMaster_個人() {
    // 期待値の定義
    String expected = "個人";

    // テスト対象メソッドの呼び出し
    target.setInsuredKbn(1);
    String actual = target.insuredTypeMaster();

    // 検証
    assertEquals(expected, actual);
}
/**
 * 被保険者区分「法人」のテスト。<br>
 * <p>
 * 概要：被保険者区分が「法人」として結果が返ってくることを確認する。
 * 条件：法人に2を設定
 * 結果：法人
 * </p>
 */
@Test
public void testinsuredTypeMaster_法人() {
    // 期待値の定義
    String expected = "法人";

    // テスト対象メソッドの呼び出し
    target.setInsuredKbn(2);
    String actual = target.insuredTypeMaster();

    // 検証
    assertEquals(expected, actual);
}
/**
 * 契約状態「計上済み」のテスト。<br>
 * <p>
 * 概要：契約状態が「計上済み」として結果が返ってくることを確認する。
 * 条件：契約状態に0を設定
 * 結果：計上済み
 * </p>
 */
@Test
public void testcontractStatusMaster_計上済み() {
    // 期待値の定義
    String expected = "計上済み";

    // テスト対象メソッドの呼び出し
    target.setStatusFlg(0);
    String actual = target.contractStatusMaster();

    // 検証
    assertEquals(expected, actual);
}

/**
 * 契約状態「計上処理待ち状態（新規）」のテスト。<br>
 * <p>
 * 概要：契約状態が「計上処理待ち状態（新規）」として結果が返ってくることを確認する。
 * 条件：契約状態に1を設定
 * 結果：計上処理待ち状態（新規）
 * </p>
 */
@Test
public void testcontractStatusMaster_計上処理待ち状態_新規() {
    // 期待値の定義
    String expected = "計上処理待ち状態（新規）";

    // テスト対象メソッドの呼び出し
    target.setStatusFlg(1);
    String actual = target.contractStatusMaster();

    // 検証
    assertEquals(expected, actual);
}
/**
 * 契約状態「計上処理待ち状態（変更）」のテスト。<br>
 * <p>
 * 概要：契約状態が「計上処理待ち状態（変更）」として結果が返ってくることを確認する。
 * 条件：契約状態に0を設定
 * 結果：計上処理待ち状態（変更）
 * </p>
 */
@Test
public void testcontractStatusMaster_計上処理待ち状態_変更() {
    // 期待値の定義
    String expected = "計上処理待ち状態（変更）";

    // テスト対象メソッドの呼び出し
    target.setStatusFlg(5);
    String actual = target.contractStatusMaster();

    // 検証
    assertEquals(expected, actual);
}
/**
 * 契約状態「計上処理待ち状態（解約）」のテスト。<br>
 * <p>
 * 概要：契約状態が「計上処理待ち状態（解約）」として結果が返ってくることを確認する。
 * 条件：契約状態に0を設定
 * 結果：計上処理待ち状態（解約）
 * </p>
 */
@Test
public void testcontractStatusMaster_計上処理待ち状態_解約() {
    // 期待値の定義
    String expected = "計上処理待ち状態（解約）";

    // テスト対象メソッドの呼び出し
    target.setStatusFlg(9);
    String actual = target.contractStatusMaster();

    // 検証
    assertEquals(expected, actual);
}

    
}
