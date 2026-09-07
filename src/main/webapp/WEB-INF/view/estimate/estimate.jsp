<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>新規試算 | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/estimate.css" />
  </head>
  <body>
    <input
      class="theme-controller"
      type="checkbox"
      id="theme-toggle"
      aria-label="ダークモードに切り替える"
    />

    <div class="app-shell">
      <header class="app-header">
        <div class="app-header__title">
          <svg
            class="app-header__home"
            viewBox="0 0 48 44"
            aria-hidden="true"
          >
            <path
              d="M4 21 24 4l20 17M10 19v21h28V19M19 40V27h10v13"
              fill="none"
              stroke="currentColor"
              stroke-width="4"
              stroke-linecap="square"
              stroke-linejoin="miter"
            />
          </svg>
          <span>新規試算</span>
        </div>

        <label class="theme-switch" for="theme-toggle">
          <span>ダークモード</span>
          <span class="theme-switch__track" aria-hidden="true">
            <span class="theme-switch__thumb"></span>
          </span>
        </label>
      </header>

      <main class="app-main estimate-main">
        <section class="card estimate-card" aria-labelledby="estimate-title">
          <h1 class="sr-only" id="estimate-title">新規試算</h1>

          <a class="estimate-breadcrumb" href="${pageContext.request.contextPath}/top">トップへ戻る</a>

          <div class="estimate-workspace">
            <!-- CSS-only demo: 一時保存 moves 3 -> 4 -> 5 items. -->
            <input
              class="estimate-state-controller"
              type="radio"
              name="estimate-save-count"
              id="estimate-save-count-3"
              checked
            />
            <input
              class="estimate-state-controller"
              type="radio"
              name="estimate-save-count"
              id="estimate-save-count-4"
            />
            <input
              class="estimate-state-controller"
              type="radio"
              name="estimate-save-count"
              id="estimate-save-count-5"
            />

            <input
              class="estimate-tab-controller"
              type="radio"
              name="estimate-tab"
              id="estimate-tab-contract"
              checked
            />
            <input
              class="estimate-tab-controller"
              type="radio"
              name="estimate-tab"
              id="estimate-tab-coverage"
            />
            <input
              class="estimate-tab-controller"
              type="radio"
              name="estimate-tab"
              id="estimate-tab-saved"
            />

            <div class="estimate-tabs" aria-label="新規試算メニュー">
              <label class="estimate-tab" for="estimate-tab-contract">
                契約条件
              </label>
              <label class="estimate-tab" for="estimate-tab-coverage">
                補償
              </label>
              <label class="estimate-tab" for="estimate-tab-saved">
                <span>一時保存一覧</span>
                <span class="estimate-tab__count" aria-live="polite">
                  <span class="estimate-count-value estimate-count-value--3">3</span>
                  <span class="estimate-count-value estimate-count-value--4">4</span>
                  <span class="estimate-count-value estimate-count-value--5">5</span>
                  / 5
                </span>
              </label>
            </div>

            <div class="estimate-panels">
              <section class="estimate-panel estimate-panel--contract">
                <form class="estimate-form" action="#" method="get">
                  <fieldset class="estimate-section">
                    <legend>お客様情報</legend>

                    <div class="estimate-form-grid">
                      <div
                        class="estimate-field estimate-field--wide estimate-party-selector"
                        role="group"
                        aria-labelledby="policyholder-type-label"
                      >
                        <span
                          class="estimate-field__label"
                          id="policyholder-type-label"
                        >
                          保険契約者区分
                        </span>
                        <div class="estimate-radio-group">
                          <label>
                            <input
                              type="radio"
                              name="policyholder-type"
                              id="policyholder-individual"
                              value="individual"
                              checked
                            />
                            個人
                          </label>
                          <label>
                            <input
                              type="radio"
                              name="policyholder-type"
                              id="policyholder-corporation"
                              value="corporation"
                            />
                            法人
                          </label>
                        </div>
                      </div>

                      <label class="estimate-field estimate-field--personal">
                        <span class="estimate-field__label">姓（漢字）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：東京"
                        />
                      </label>
                      <label class="estimate-field estimate-field--personal">
                        <span class="estimate-field__label">名（漢字）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：太郎"
                        />
                      </label>

                      <label class="estimate-field estimate-field--personal">
                        <span class="estimate-field__label">姓（カナ）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：トウキョウ"
                        />
                      </label>
                      <label class="estimate-field estimate-field--personal">
                        <span class="estimate-field__label">名（カナ）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：タロウ"
                        />
                      </label>

                      <div
                        class="estimate-field estimate-field--personal"
                        role="group"
                        aria-labelledby="gender-label"
                      >
                        <span class="estimate-field__label" id="gender-label">
                          性別
                        </span>
                        <div class="estimate-radio-group estimate-radio-group--control">
                          <label>
                            <input type="radio" name="gender" value="male" checked />
                            男
                          </label>
                          <label>
                            <input type="radio" name="gender" value="female" />
                            女
                          </label>
                        </div>
                      </div>
                      <label class="estimate-field estimate-field--personal">
                        <span class="estimate-field__label">生年月日</span>
                        <input
                          class="estimate-control"
                          type="text"
                          inputmode="numeric"
                          placeholder="例：1998/02/19"
                        />
                      </label>

                      <label class="estimate-field estimate-field--corporation">
                        <span class="estimate-field__label">会社名（漢字）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：株式会社サンプル商事"
                        />
                      </label>
                      <label class="estimate-field estimate-field--corporation">
                        <span class="estimate-field__label">会社名（カナ）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：カブシキガイシャサンプルショウジ"
                        />
                      </label>

                      <label class="estimate-field estimate-field--wide">
                        <span class="estimate-field__label">郵便番号</span>
                        <input
                          class="estimate-control estimate-control--half"
                          type="text"
                          inputmode="numeric"
                          placeholder="例：111-1111"
                        />
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">住所1（漢字）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：東京都多摩市"
                        />
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">住所2（漢字）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：1-1-1"
                        />
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">住所1（カタカナ）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：トウキョウトタマシ"
                        />
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">住所2（カタカナ）</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：1-1-1"
                        />
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">電話番号</span>
                        <input
                          class="estimate-control"
                          type="tel"
                          placeholder="例：00-0000-0000"
                        />
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">携帯電話番号</span>
                        <input
                          class="estimate-control"
                          type="tel"
                          placeholder="例：000-0000-0000"
                        />
                      </label>

                      <label class="estimate-field estimate-field--wide">
                        <span class="estimate-field__label">FAX番号</span>
                        <input
                          class="estimate-control estimate-control--half"
                          type="tel"
                          placeholder="例：00-0000-0000"
                        />
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">保険期間開始日</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：令和8年01月01日"
                        />
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">保険期間開始時刻</span>
                        <input
                          class="estimate-control"
                          type="text"
                          value="午後6時"
                          disabled
                        />
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">保険期間満期日</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：令和9年01月01日"
                        />
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">保険期間満期時刻</span>
                        <input
                          class="estimate-control"
                          type="text"
                          value="午後6時"
                          disabled
                        />
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">払込方法</span>
                        <select class="estimate-control">
                          <option>クレジットカード</option>
                          <option>口座振替</option>
                          <option>払込票</option>
                        </select>
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">払込回数</span>
                        <select class="estimate-control">
                          <option>1</option>
                          <option>6</option>
                          <option>12</option>
                        </select>
                      </label>
                    </div>
                  </fieldset>

                  <div class="estimate-actions">
                    <div class="estimate-save-actions">
                      <label
                        class="button button--secondary estimate-save-action estimate-save-action--3"
                        for="estimate-save-count-4"
                      >一時保存</label>
                      <label
                        class="button button--secondary estimate-save-action estimate-save-action--4"
                        for="estimate-save-count-5"
                      >一時保存</label>
                      <button
                        class="button button--secondary estimate-save-action estimate-save-action--5"
                        type="button"
                        popovertarget="save-limit-dialog"
                      >一時保存</button>
                    </div>
                    <div class="estimate-primary-actions">
                      <button class="button button--primary" type="button">
                        保険料試算
                      </button>
                      <a
                        class="button button--primary estimate-button-link"
                        href="${pageContext.request.contextPath}/application-print"
                      >申込書印刷</a>
                    </div>
                  </div>
                </form>
              </section>

              <section class="estimate-panel estimate-panel--coverage">
                <form class="estimate-form" action="#" method="get">
                  <fieldset class="estimate-section">
                    <legend>試算結果</legend>

                    <div class="estimate-premium-summary">
                      <div class="estimate-premium">
                        <span>総額保険料</span>
                        <strong>10,000,000円</strong>
                      </div>
                      <div class="estimate-premium">
                        <span>一回分保険料</span>
                        <strong>10,000円</strong>
                      </div>
                    </div>

                    <div class="estimate-form-grid estimate-form-grid--coverage">
                      <label class="estimate-field">
                        <span class="estimate-field__label">メーカー</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：TOYOTA"
                        />
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">車名</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：キューブ"
                        />
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">車のナンバー</span>
                        <input
                          class="estimate-control"
                          type="text"
                          placeholder="例：品川300あ00-0000"
                        />
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">車両保険金額</span>
                        <span class="estimate-control-with-suffix">
                          <input
                            class="estimate-control"
                            type="text"
                            inputmode="numeric"
                            placeholder="例：10,000,000"
                          />
                          <span>円</span>
                        </span>
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">免許証の色</span>
                        <select class="estimate-control" required>
                          <option value="" selected disabled>例：ブルー</option>
                          <option>ブルー</option>
                          <option>ゴールド</option>
                          <option>グリーン</option>
                        </select>
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">年齢条件</span>
                        <select class="estimate-control" required>
                          <option value="" selected disabled>例：無制限</option>
                          <option>無制限</option>
                          <option>21歳以上</option>
                          <option>26歳以上</option>
                          <option>35歳以上</option>
                        </select>
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">車両料率</span>
                        <select class="estimate-control" required>
                          <option value="" selected disabled>例：1</option>
                          <option>1</option>
                          <option>2</option>
                          <option>3</option>
                        </select>
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">対人料率</span>
                        <select class="estimate-control" required>
                          <option value="" selected disabled>例：1</option>
                          <option>1</option>
                          <option>2</option>
                          <option>3</option>
                        </select>
                      </label>

                      <label class="estimate-field">
                        <span class="estimate-field__label">対物料率</span>
                        <select class="estimate-control" required>
                          <option value="" selected disabled>例：1</option>
                          <option>1</option>
                          <option>2</option>
                          <option>3</option>
                        </select>
                      </label>
                      <label class="estimate-field">
                        <span class="estimate-field__label">傷害料率</span>
                        <select class="estimate-control" required>
                          <option value="" selected disabled>例：1</option>
                          <option>1</option>
                          <option>2</option>
                          <option>3</option>
                        </select>
                      </label>
                    </div>
                  </fieldset>

                  <div class="estimate-actions">
                    <div class="estimate-save-actions">
                      <label
                        class="button button--secondary estimate-save-action estimate-save-action--3"
                        for="estimate-save-count-4"
                      >一時保存</label>
                      <label
                        class="button button--secondary estimate-save-action estimate-save-action--4"
                        for="estimate-save-count-5"
                      >一時保存</label>
                      <button
                        class="button button--secondary estimate-save-action estimate-save-action--5"
                        type="button"
                        popovertarget="save-limit-dialog"
                      >一時保存</button>
                    </div>
                    <div class="estimate-primary-actions">
                      <button class="button button--primary" type="button">
                        保険料試算
                      </button>
                      <a
                        class="button button--primary estimate-button-link"
                        href="${pageContext.request.contextPath}/application-print"
                      >申込書印刷</a>
                    </div>
                  </div>
                </form>
              </section>

              <section class="estimate-panel estimate-panel--saved">
                <div class="estimate-saved-header">
                  <div>
                    <h2>一時保存一覧</h2>
                  </div>
                  <strong class="estimate-saved-total" aria-live="polite">
                    全
                    <span class="estimate-count-value estimate-count-value--3">3</span>
                    <span class="estimate-count-value estimate-count-value--4">4</span>
                    <span class="estimate-count-value estimate-count-value--5">5</span>
                    件表示
                  </strong>
                </div>

                <div class="estimate-table-wrap">
                  <table class="estimate-table">
                    <thead>
                      <tr>
                        <th>保存日時</th>
                        <th>契約者名</th>
                        <th>郵便番号</th>
                        <th>住所</th>
                        <th>連絡先</th>
                        <th>操作</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td data-label="保存日時">2026/08/05</td>
                        <td data-label="契約者名">株式会社サンプル商事</td>
                        <td data-label="郵便番号">100-0001</td>
                        <td data-label="住所">東京都千代田区1-1-1 サンプルビル10階</td>
                        <td data-label="連絡先">03-1234-5678</td>
                        <td data-label="操作" class="estimate-table__actions">
                          <label
                            class="button estimate-row-button estimate-row-button--resume"
                            for="estimate-tab-contract"
                          >再開</label>
                          <button
                            class="button estimate-row-button estimate-row-button--delete"
                            type="button"
                            popovertarget="delete-dialog"
                          >削除</button>
                        </td>
                      </tr>
                      <tr>
                        <td data-label="保存日時">2026/08/09</td>
                        <td data-label="契約者名">サンプル株式会社</td>
                        <td data-label="郵便番号">150-0002</td>
                        <td data-label="住所">東京都八王子市2-2-2 サンプラザ5階</td>
                        <td data-label="連絡先">03-2345-6789</td>
                        <td data-label="操作" class="estimate-table__actions">
                          <label
                            class="button estimate-row-button estimate-row-button--resume"
                            for="estimate-tab-contract"
                          >再開</label>
                          <button
                            class="button estimate-row-button estimate-row-button--delete"
                            type="button"
                            popovertarget="delete-dialog"
                          >削除</button>
                        </td>
                      </tr>
                      <tr>
                        <td data-label="保存日時">2026/08/13</td>
                        <td data-label="契約者名">海上 太郎</td>
                        <td data-label="郵便番号">530-0001</td>
                        <td data-label="住所">東京都多摩市3-3-3 コーポサンプル101</td>
                        <td data-label="連絡先">080-0000-0000</td>
                        <td data-label="操作" class="estimate-table__actions">
                          <label
                            class="button estimate-row-button estimate-row-button--resume"
                            for="estimate-tab-contract"
                          >再開</label>
                          <button
                            class="button estimate-row-button estimate-row-button--delete"
                            type="button"
                            popovertarget="delete-dialog"
                          >削除</button>
                        </td>
                      </tr>
                      <tr class="estimate-saved-row estimate-saved-row--4">
                        <td data-label="保存日時">2026/08/19</td>
                        <td data-label="契約者名">山田 優子</td>
                        <td data-label="郵便番号">460-0001</td>
                        <td data-label="住所">東京都新宿区4-4-4 サンプルメゾン301</td>
                        <td data-label="連絡先">090-1111-1111</td>
                        <td data-label="操作" class="estimate-table__actions">
                          <label
                            class="button estimate-row-button estimate-row-button--resume"
                            for="estimate-tab-contract"
                          >再開</label>
                          <button
                            class="button estimate-row-button estimate-row-button--delete"
                            type="button"
                            popovertarget="delete-dialog"
                          >削除</button>
                        </td>
                      </tr>
                      <tr class="estimate-saved-row estimate-saved-row--5">
                        <td data-label="保存日時">2026/08/26</td>
                        <td data-label="契約者名">サンプルホールディングス</td>
                        <td data-label="郵便番号">260-0001</td>
                        <td data-label="住所">神奈川県横浜市5-5-5 サンプルタワー8階</td>
                        <td data-label="連絡先">06-3456-7890</td>
                        <td data-label="操作" class="estimate-table__actions">
                          <label
                            class="button estimate-row-button estimate-row-button--resume"
                            for="estimate-tab-contract"
                          >再開</label>
                          <button
                            class="button estimate-row-button estimate-row-button--delete"
                            type="button"
                            popovertarget="delete-dialog"
                          >削除</button>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </section>
            </div>

            <div
              class="estimate-dialog"
              id="save-limit-dialog"
              popover
              role="dialog"
              aria-labelledby="save-limit-title"
            >
              <div class="estimate-dialog__body">
                <h2 id="save-limit-title">一時保存の上限に達しました</h2>
                <p>
                  一時保存できるのは最大5件です。不要な保存データを削除してから、
                  もう一度お試しください。
                </p>
              </div>
              <div class="estimate-dialog__actions">
                <button
                  class="button button--primary"
                  type="button"
                  popovertarget="save-limit-dialog"
                  popovertargetaction="hide"
                >閉じる</button>
              </div>
            </div>

            <div
              class="estimate-dialog"
              id="delete-dialog"
              popover
              role="dialog"
              aria-labelledby="delete-dialog-title"
            >
              <div class="estimate-dialog__body estimate-dialog__body--center">
                <h2 id="delete-dialog-title">削除してよろしいでしょうか</h2>
              </div>
              <div class="estimate-dialog__actions estimate-dialog__actions--split">
                <button
                  class="button button--secondary"
                  type="button"
                  popovertarget="delete-dialog"
                  popovertargetaction="hide"
                >キャンセル</button>
                <button
                  class="button estimate-dialog__delete"
                  type="button"
                  popovertarget="delete-dialog"
                  popovertargetaction="hide"
                >削除</button>
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>
