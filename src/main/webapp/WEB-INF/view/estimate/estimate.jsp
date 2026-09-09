<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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

  <script>
    // 画面が描画される前に最速で判定してクラスをつける！
    if (localStorage.getItem("appTheme") === "dark") {
      document.documentElement.classList.add("dark-mode"); // ※あなたのクラス名に合わせてね
    }
    // 2. ページ読み込み完了後にトグルスイッチの同期とイベントを設定
    document.addEventListener("DOMContentLoaded", function () {
      const toggle = document.getElementById("theme-toggle");
      if (!toggle) return;

      // 保存されている設定に合わせてスイッチのチェック状態を同期
      if (localStorage.getItem("appTheme") === "dark") {
        toggle.checked = true;
      }

      // スイッチが切り替わったときに保存とクラスの付け外しを行う
      toggle.addEventListener("change", function () {
        if (toggle.checked) {
          document.documentElement.classList.add("dark-mode");
          localStorage.setItem("appTheme", "dark");
        } else {
          document.documentElement.classList.remove("dark-mode");
          localStorage.setItem("appTheme", "light");
        }
      });
    });
  </script>
</head>

<body>
  <input class="theme-controller" type="checkbox" id="theme-toggle" aria-label="ダークモードに切り替える" />

  <div class="app-shell">
    <header class="app-header">
      <div class="app-header__title">
        <svg class="app-header__home" viewBox="0 0 48 44" aria-hidden="true">
          <path d="M4 21 24 4l20 17M10 19v21h28V19M19 40V27h10v13" fill="none" stroke="currentColor"
            stroke-width="4" stroke-linecap="square" stroke-linejoin="miter" />
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
          <input class="estimate-state-controller" type="radio" name="estimate-save-count"
            id="estimate-save-count-3" checked />
          <input class="estimate-state-controller" type="radio" name="estimate-save-count"
            id="estimate-save-count-4" />
          <input class="estimate-state-controller" type="radio" name="estimate-save-count"
            id="estimate-save-count-5" />

          <input class="estimate-tab-controller" type="radio" name="estimate-tab" id="estimate-tab-contract"
            <c:if test="${not calculated}">checked</c:if>
          />
          <input class="estimate-tab-controller" type="radio" name="estimate-tab" id="estimate-tab-coverage"
            <c:if test="${calculated}">checked</c:if>
          />
          <input class="estimate-tab-controller" type="radio" name="estimate-tab" id="estimate-tab-saved" />

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
                    <div class="estimate-field estimate-field--wide estimate-party-selector" role="group"
                      aria-labelledby="policyholder-type-label">
                      <span class="estimate-field__label" id="policyholder-type-label">
                        保険契約者区分
                      </span>
                      <div class="estimate-radio-group">
                        <label>
                          <input type="radio" name="insuredKbn" id="policyholder-individual" value="1" <c:if
                            test="${empty contract or contract.insuredKbn == 0 or contract.insuredKbn == 1}">checked
                          </c:if>
                          form="coverage-form"
                          />
                          個人
                        </label>
                        <label>
                          <input type="radio" name="insuredKbn" id="policyholder-corporation" value="2" <c:if
                            test="${contract.insuredKbn == 2}">checked</c:if>
                          form="coverage-form"
                          />
                          法人
                        </label>
                      </div>
                    </div>

                    <label class="estimate-field estimate-field--personal">
                      <span class="estimate-field__label">姓（漢字）</span>
                      <input class="estimate-control" type="text" name="nameKanji1"
                        value="${contract.nameKanji1}" form="coverage-form" placeholder="例：東京" />
                    </label>
                    <label class="estimate-field estimate-field--personal">
                      <span class="estimate-field__label">名（漢字）</span>
                      <input class="estimate-control" type="text" name="nameKanji2"
                        value="${contract.nameKanji2}" form="coverage-form" placeholder="例：太郎" />
                    </label>

                    <label class="estimate-field estimate-field--personal">
                      <span class="estimate-field__label">姓（カナ）</span>
                      <input class="estimate-control" type="text" name="nameKana1" value="${contract.nameKana1}"
                        form="coverage-form" placeholder="例：トウキョウ" />
                    </label>
                    <label class="estimate-field estimate-field--personal">
                      <span class="estimate-field__label">名（カナ）</span>
                      <input class="estimate-control" type="text" name="nameKana2" value="${contract.nameKana2}"
                        form="coverage-form" placeholder="例：タロウ" />
                    </label>

                    <div class="estimate-field estimate-field--personal" role="group"
                      aria-labelledby="gender-label">
                      <span class="estimate-field__label" id="gender-label">
                        性別
                      </span>
                      <div class="estimate-radio-group estimate-radio-group--control">
                        <label>
                          <input type="radio" name="gender" value="1" form="coverage-form" <c:if
                            test="${empty contract or contract.gender == 0 or contract.gender == 1}">checked
                          </c:if> />
                          男
                        </label>
                        <label>
                          <input type="radio" name="gender" value="2" form="coverage-form" <c:if
                            test="${contract.gender == 2}">checked</c:if> />
                          女
                        </label>
                      </div>
                    </div>
                    <label class="estimate-field estimate-field--personal">
                      <span class="estimate-field__label">生年月日</span>
                      <input class="estimate-control" type="date" name="birthday" form="coverage-form" <c:if
                        test="${not empty contract.birthday and fn:length(contract.birthday) == 8}">value="${fn:substring(contract.birthday,
                      0, 4)}-${fn:substring(contract.birthday, 4, 6)}-${fn:substring(contract.birthday, 6, 8)}"
                      </c:if>
                      />
                    </label>

                    <label class="estimate-field estimate-field--corporation">
                      <span class="estimate-field__label">会社名（漢字）</span>
                      <input class="estimate-control" type="text" name="nameKanji1"
                        value="${contract.nameKanji1}" form="coverage-form" placeholder="例：株式会社サンプル商事" />
                    </label>
                    <label class="estimate-field estimate-field--corporation">
                      <span class="estimate-field__label">会社名（カナ）</span>
                      <input class="estimate-control" type="text" name="nameKana1" value="${contract.nameKana1}"
                        form="coverage-form" placeholder="例：カブシキガイシャサンプルショウジ" />
                    </label>

                    <label class="estimate-field estimate-field--wide">
                      <span class="estimate-field__label">郵便番号</span>
                      <input class="estimate-control estimate-control--half" type="text" name="postcode"
                        value="${contract.postcode}" form="coverage-form" inputmode="numeric"
                        placeholder="例：111-1111" />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">住所1（漢字）</span>
                      <input class="estimate-control" type="text" name="addressKanji1"
                        value="${contract.addressKanji1}" form="coverage-form" placeholder="例：東京都多摩市" />
                    </label>
                    <label class="estimate-field">
                      <span class="estimate-field__label">住所2（漢字）</span>
                      <input class="estimate-control" type="text" name="addressKanji2"
                        value="${contract.addressKanji2}" form="coverage-form" placeholder="例：1-1-1" />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">住所1（カタカナ）</span>
                      <input class="estimate-control" type="text" name="addressKana1"
                        value="${contract.addressKana1}" form="coverage-form" placeholder="例：トウキョウトタマシ" />
                    </label>
                    <label class="estimate-field">
                      <span class="estimate-field__label">住所2（カタカナ）</span>
                      <input class="estimate-control" type="text" name="addressKana2"
                        value="${contract.addressKana2}" form="coverage-form" placeholder="例：1-1-1" />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">電話番号</span>
                      <input class="estimate-control" type="tel" name="telephoneNo"
                        value="${contract.telephoneNo}" form="coverage-form" placeholder="例：00-0000-0000" />
                    </label>
                    <label class="estimate-field">
                      <span class="estimate-field__label">携帯電話番号</span>
                      <input class="estimate-control" type="tel" name="mobilephoneNo"
                        value="${contract.mobilephoneNo}" form="coverage-form" placeholder="例：000-0000-0000" />
                    </label>

                    <label class="estimate-field estimate-field--wide">
                      <span class="estimate-field__label">FAX番号</span>
                      <input class="estimate-control estimate-control--half" type="tel" name="faxNo"
                        value="${contract.faxNo}" form="coverage-form" placeholder="例：00-0000-0000" />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">保険期間開始日</span>
                      <input class="estimate-control" type="date" name="inceptionDate" form="coverage-form"
                        <c:if
                        test="${not empty contract.inceptionDate and fn:length(contract.inceptionDate) == 8}">value="${fn:substring(contract.inceptionDate,
                      0, 4)}-${fn:substring(contract.inceptionDate, 4,
                      6)}-${fn:substring(contract.inceptionDate, 6, 8)}"</c:if>
                      />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">保険期間開始時刻</span>
                      <select class="estimate-control" name="inceptionTime" form="coverage-form">
                        <option value="" ${empty contract.inceptionTime ? 'selected' : '' }>選択してください</option>
                        <option value="09" ${contract.inceptionTime=='09' ? 'selected' : '' }>午前9時</option>
                        <option value="10" ${contract.inceptionTime=='10' ? 'selected' : '' }>午前10時</option>
                        <option value="11" ${contract.inceptionTime=='11' ? 'selected' : '' }>午前11時</option>
                        <option value="12" ${contract.inceptionTime=='12' ? 'selected' : '' }>午後0時</option>
                        <option value="13" ${contract.inceptionTime=='13' ? 'selected' : '' }>午後1時</option>
                        <option value="14" ${contract.inceptionTime=='14' ? 'selected' : '' }>午後2時</option>
                        <option value="15" ${contract.inceptionTime=='15' ? 'selected' : '' }>午後3時</option>
                        <option value="16" ${contract.inceptionTime=='16' ? 'selected' : '' }>午後4時</option>
                        <option value="17" ${contract.inceptionTime=='17' ? 'selected' : '' }>午後5時</option>
                        <option value="18" ${contract.inceptionTime=='18' ? 'selected' : '' }>午後6時</option>
                      </select>
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">保険期間満期日</span>
                      <input class="estimate-control" type="date" name="conclusionDate" form="coverage-form"
                        <c:if
                        test="${not empty contract.conclusionDate and fn:length(contract.conclusionDate) == 8}">value="${fn:substring(contract.conclusionDate,
                      0, 4)}-${fn:substring(contract.conclusionDate, 4,
                      6)}-${fn:substring(contract.conclusionDate, 6, 8)}"</c:if>
                      />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">保険期間満期時刻</span>
                      <select class="estimate-control" name="conclusionTime" form="coverage-form">
                        <option value="" ${empty contract.conclusionTime ? 'selected' : '' }>選択してください</option>
                        <option value="09" ${contract.conclusionTime=='09' ? 'selected' : '' }>午前9時</option>
                        <option value="10" ${contract.conclusionTime=='10' ? 'selected' : '' }>午前10時</option>
                        <option value="11" ${contract.conclusionTime=='11' ? 'selected' : '' }>午前11時</option>
                        <option value="12" ${contract.conclusionTime=='12' ? 'selected' : '' }>午後0時</option>
                        <option value="13" ${contract.conclusionTime=='13' ? 'selected' : '' }>午後1時</option>
                        <option value="14" ${contract.conclusionTime=='14' ? 'selected' : '' }>午後2時</option>
                        <option value="15" ${contract.conclusionTime=='15' ? 'selected' : '' }>午後3時</option>
                        <option value="16" ${contract.conclusionTime=='16' ? 'selected' : '' }>午後4時</option>
                        <option value="17" ${contract.conclusionTime=='17' ? 'selected' : '' }>午後5時</option>
                        <option value="18" ${contract.conclusionTime=='18' ? 'selected' : '' }>午後6時</option>
                      </select>
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">払込方法</span>
                      <select class="estimate-control" name="paymentMethod" form="coverage-form">
                        <option value="1" <c:if
                          test="${contract.paymentMethod == 0 or contract.paymentMethod == 1}">selected</c:if>
                          >クレジットカード</option>
                        <option value="2" <c:if test="${contract.paymentMethod == 2}">selected</c:if>>口座振替
                        </option>
                        <option value="3" <c:if test="${contract.paymentMethod == 3}">selected</c:if>>払込票
                        </option>
                      </select>
                    </label>
                    <label class="estimate-field">
                      <span class="estimate-field__label">払込回数</span>
                      <select class="estimate-control" name="installment" form="coverage-form">
                        <option value="1" <c:if
                          test="${contract.installment == 0 or contract.installment == 1}">selected</c:if>>1
                        </option>
                        <option value="6" <c:if test="${contract.installment == 6}">selected</c:if>>6</option>
                        <option value="12" <c:if test="${contract.installment == 12}">selected</c:if>>12
                        </option>
                      </select>
                    </label>
                  </div>
                </fieldset>

                <div class="estimate-actions">
                  <div class="estimate-save-actions">
                    <label class="button button--secondary estimate-save-action estimate-save-action--3"
                      for="estimate-save-count-4">一時保存</label>
                    <label class="button button--secondary estimate-save-action estimate-save-action--4"
                      for="estimate-save-count-5">一時保存</label>
                    <button class="button button--secondary estimate-save-action estimate-save-action--5"
                      type="button" popovertarget="save-limit-dialog">一時保存</button>
                  </div>
                  <div class="estimate-primary-actions">
                    <button class="button button--primary" type="submit" form="coverage-form">
                      保険料試算
                    </button>
                    <button class="button button--primary estimate-button-link" type="submit"
                      form="coverage-form" formnovalidate
                      formaction="${pageContext.request.contextPath}/estimatestatus" formmethod="post">
                      申込書印刷
                    </button>
                  </div>
                </div>
              </form>
            </section>

            <section class="estimate-panel estimate-panel--coverage">
              <form id="coverage-form" class="estimate-form"
                action="${pageContext.request.contextPath}/estimatecalc" method="post">
                <fieldset class="estimate-section">
                  <legend>試算結果</legend>

                  <div class="estimate-premium-summary">
                    <div class="estimate-premium">
                      <span>総額保険料</span>
                      <strong>
                        <c:choose>
                          <c:when test="${calculated}">
                            <fmt:formatNumber value="${claim.premiumAmount}" type="number" />円
                          </c:when>
                          <c:otherwise>未計算</c:otherwise>
                        </c:choose>
                      </strong>
                    </div>
                    <div class="estimate-premium">
                      <span>一回分保険料</span>
                      <strong>
                        <c:choose>
                          <c:when test="${calculated}">
                            <fmt:formatNumber value="${claim.premiumInstallment}" type="number" />円
                          </c:when>
                          <c:otherwise>未計算</c:otherwise>
                        </c:choose>
                      </strong>
                    </div>
                  </div>
                  <!-- メーカーの入力欄をプルダウンに変更 -->
                  <label class="estimate-field">
                    <span class="estimate-field__label">メーカー</span>
                    <select class="estimate-control" name="maker" id="makerSelect" onchange="onMakerChange()"
                      required>
                      <option value="">選択してください</option>
                    </select>
                  </label>

                  <!-- 車名の入力欄をプルダウンに変更 -->
                  <label class="estimate-field">
                    <span class="estimate-field__label">車名</span>
                    <select class="estimate-control" name="carName" id="carNameSelect"
                      onchange="onCarNameChange()" required>
                      <option value="">選択してください</option>
                      <c:forEach var="vehicle" items="${vehicles}">
                        <option value="${vehicle.name}" data-maker="${vehicle.maker}">${vehicle.name}</option>
                      </c:forEach>
                    </select>
                  </label>


                  <label class="estimate-field">
                    <span class="estimate-field__label">車のナンバー</span>
                    <input class="estimate-control" type="text" name="licenseNo" value="${claim.licenseNo}"
                      required placeholder="例：品川300あ00-0000" />
                  </label>

                  <label class="estimate-field">
                    <span class="estimate-field__label">免許証の色</span>
                    <select class="estimate-control" name="licenseColor" required>
                      <option value="" <c:if test="${not calculated}">selected</c:if> disabled>例：ブルー</option>
                      <option value="1" <c:if test="${claim.licenseColor == '1'}">selected</c:if>>ブルー</option>
                      <option value="2" <c:if test="${claim.licenseColor == '2'}">selected</c:if>>グリーン</option>
                      <option value="3" <c:if test="${claim.licenseColor == '3'}">selected</c:if>>ゴールド</option>
                    </select>
                  </label>
                  <label class="estimate-field">
                    <span class="estimate-field__label">年齢条件</span>
                    <select class="estimate-control" name="ageLimit" required>
                      <option value="" <c:if test="${not calculated}">selected</c:if> disabled>例：無制限</option>
                      <option value="1" <c:if test="${claim.ageLimit == 1}">selected</c:if>>無制限</option>
                      <option value="2" <c:if test="${claim.ageLimit == 2}">selected</c:if>>21歳以上</option>
                      <option value="3" <c:if test="${claim.ageLimit == 3}">selected</c:if>>26歳以上</option>
                    </select>
                  </label>

                  <label class="estimate-field">
                    <span class="estimate-field__label">車両保険金額</span>
                    <output class="estimate-control estimate-control--readonly">
                      <c:if test="${calculated}">
                        <fmt:formatNumber value="${claim.vehiclePrice}" type="number" />円
                      </c:if>
                    </output>
                  </label>

                  <label class="estimate-field">
                    <span class="estimate-field__label">車両料率</span>
                    <output class="estimate-control estimate-control--readonly">
                      <fmt:formatNumber value="${claim.vehicleRates}" maxFractionDigits="2" />
                    </output>
                  </label>
                  <label class="estimate-field">
                    <span class="estimate-field__label">対人料率</span>
                    <output class="estimate-control estimate-control--readonly">
                      <fmt:formatNumber value="${claim.bodilyRates}" maxFractionDigits="2" />
                    </output>
                  </label>
                  <label class="estimate-field">
                    <span class="estimate-field__label">対物料率</span>
                    <output class="estimate-control estimate-control--readonly">
                      <fmt:formatNumber value="${claim.propertyDamageRates}" maxFractionDigits="2" />
                    </output>
                  </label>
                  <label class="estimate-field">
                    <span class="estimate-field__label">傷害料率</span>
                    <output class="estimate-control estimate-control--readonly">
                      <fmt:formatNumber value="${claim.accidentRates}" maxFractionDigits="2" />
                    </output>
                  </label>

                </fieldset>

                <div class="estimate-actions">
                  <div class="estimate-save-actions">
                    <label class="button button--secondary estimate-save-action estimate-save-action--3"
                      for="estimate-save-count-4">一時保存</label>
                    <label class="button button--secondary estimate-save-action estimate-save-action--4"
                      for="estimate-save-count-5">一時保存</label>
                    <button class="button button--secondary estimate-save-action estimate-save-action--5"
                      type="button" popovertarget="save-limit-dialog">一時保存</button>
                  </div>
                  <div class="estimate-primary-actions">
                    <button class="button button--primary" type="submit">
                      保険料試算
                    </button>
                    <button class="button button--primary estimate-button-link" type="submit"
                      formaction="${pageContext.request.contextPath}/estimatestatus" formmethod="post">
                      申込書印刷
                    </button>
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
                        <label class="button estimate-row-button estimate-row-button--resume"
                          for="estimate-tab-contract">再開</label>
                        <button class="button estimate-row-button estimate-row-button--delete" type="button"
                          popovertarget="delete-dialog">削除</button>
                      </td>
                    </tr>
                    <tr>
                      <td data-label="保存日時">2026/08/09</td>
                      <td data-label="契約者名">サンプル株式会社</td>
                      <td data-label="郵便番号">150-0002</td>
                      <td data-label="住所">東京都八王子市2-2-2 サンプラザ5階</td>
                      <td data-label="連絡先">03-2345-6789</td>
                      <td data-label="操作" class="estimate-table__actions">
                        <label class="button estimate-row-button estimate-row-button--resume"
                          for="estimate-tab-contract">再開</label>
                        <button class="button estimate-row-button estimate-row-button--delete" type="button"
                          popovertarget="delete-dialog">削除</button>
                      </td>
                    </tr>
                    <tr>
                      <td data-label="保存日時">2026/08/13</td>
                      <td data-label="契約者名">海上 太郎</td>
                      <td data-label="郵便番号">530-0001</td>
                      <td data-label="住所">東京都多摩市3-3-3 コーポサンプル101</td>
                      <td data-label="連絡先">080-0000-0000</td>
                      <td data-label="操作" class="estimate-table__actions">
                        <label class="button estimate-row-button estimate-row-button--resume"
                          for="estimate-tab-contract">再開</label>
                        <button class="button estimate-row-button estimate-row-button--delete" type="button"
                          popovertarget="delete-dialog">削除</button>
                      </td>
                    </tr>
                    <tr class="estimate-saved-row estimate-saved-row--4">
                      <td data-label="保存日時">2026/08/19</td>
                      <td data-label="契約者名">山田 優子</td>
                      <td data-label="郵便番号">460-0001</td>
                      <td data-label="住所">東京都新宿区4-4-4 サンプルメゾン301</td>
                      <td data-label="連絡先">090-1111-1111</td>
                      <td data-label="操作" class="estimate-table__actions">
                        <label class="button estimate-row-button estimate-row-button--resume"
                          for="estimate-tab-contract">再開</label>
                        <button class="button estimate-row-button estimate-row-button--delete" type="button"
                          popovertarget="delete-dialog">削除</button>
                      </td>
                    </tr>
                    <tr class="estimate-saved-row estimate-saved-row--5">
                      <td data-label="保存日時">2026/08/26</td>
                      <td data-label="契約者名">サンプルホールディングス</td>
                      <td data-label="郵便番号">260-0001</td>
                      <td data-label="住所">神奈川県横浜市5-5-5 サンプルタワー8階</td>
                      <td data-label="連絡先">06-3456-7890</td>
                      <td data-label="操作" class="estimate-table__actions">
                        <label class="button estimate-row-button estimate-row-button--resume"
                          for="estimate-tab-contract">再開</label>
                        <button class="button estimate-row-button estimate-row-button--delete" type="button"
                          popovertarget="delete-dialog">削除</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </section>
          </div>

          <div class="estimate-dialog" id="save-limit-dialog" popover role="dialog"
            aria-labelledby="save-limit-title">
            <div class="estimate-dialog__body">
              <h2 id="save-limit-title">一時保存の上限に達しました</h2>
              <p>
                一時保存できるのは最大5件です。不要な保存データを削除してから、
                もう一度お試しください。
              </p>
            </div>
            <div class="estimate-dialog__actions">
              <button class="button button--primary" type="button" popovertarget="save-limit-dialog"
                popovertargetaction="hide">閉じる</button>
            </div>
          </div>

          <div class="estimate-dialog" id="delete-dialog" popover role="dialog"
            aria-labelledby="delete-dialog-title">
            <div class="estimate-dialog__body estimate-dialog__body--center">
              <h2 id="delete-dialog-title">削除してよろしいでしょうか</h2>
            </div>
            <div class="estimate-dialog__actions estimate-dialog__actions--split">
              <button class="button button--secondary" type="button" popovertarget="delete-dialog"
                popovertargetaction="hide">キャンセル</button>
              <button class="button estimate-dialog__delete" type="button" popovertarget="delete-dialog"
                popovertargetaction="hide">削除</button>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
  <script>

    // 車両一覧はControllerがM_CARS_TBLから生成したoptionを利用する。
    const carDatabase = Array.from(document.querySelectorAll("#carNameSelect option[data-maker]"))
      .map(option => ({
        maker: option.dataset.maker,
        name: option.textContent,
        dbName: option.value
      }));

    // --- 2. 画面が開いた時の初期設定 ---
    document.addEventListener("DOMContentLoaded", function () {

      // ----------------------------------------
      // 【A】ダークモードのスイッチ連動処理
      // ----------------------------------------
      const themeToggle = document.getElementById("theme-toggle");

      // すでに<head>のスクリプトでダークモードになっている場合、スイッチの見た目だけを合わせる
      if (localStorage.getItem("appTheme") === "dark") {
        if (themeToggle) {
          themeToggle.checked = true;
        }
      }

      // スイッチを切り替えた時の処理はそのまま！
      if (themeToggle) {
        themeToggle.addEventListener("change", function () {
          if (this.checked) {
            document.documentElement.classList.add("dark-mode");
            localStorage.setItem("appTheme", "dark");
          } else {
            document.documentElement.classList.remove("dark-mode");
            localStorage.setItem("appTheme", "light");
          }
        });
      }

      // 重複のないメーカーの一覧を作成して、メーカープルダウンにセット
      const makerSelect = document.getElementById("makerSelect");
      const uniqueMakers = [...new Set(carDatabase.map(car => car.maker))];
      uniqueMakers.forEach(maker => {
        const option = document.createElement("option");
        option.value = maker;
        option.textContent = maker;
        makerSelect.appendChild(option);
      });

      // 初期状態では全車名を車名プルダウンにセット
      updateCarSelect();

      // ★もし試算ボタンを押した後で、Java(Controller)から値が戻ってきていたらセットする！
      const initialMaker = "${claim.maker}";
      const initialCarName = "${claim.carName}";

      if (initialMaker) {
        makerSelect.value = initialMaker;
        updateCarSelect(); // メーカーに合わせて車名を絞り込む
      }

      if (initialCarName) {
        const initialCar = carDatabase.find(car =>
          car.name === initialCarName || getDbCarName(car) === initialCarName);
        if (initialCar) {
          document.getElementById("carNameSelect").value = getDbCarName(initialCar);
        }
      }

    });

    // --- 3. イベント処理 (双方向連動の魔法) ---

    function getDbCarName(car) {
      return car.dbName || car.name;
    }

    // パターンA：メーカーが選ばれたら → 車名を絞り込む
    function onMakerChange() {
      updateCarSelect();
    }

    // パターンB：車名が選ばれたら → メーカーを逆引きして自動セットする
    function onCarNameChange() {
      const selectedCarName = document.getElementById("carNameSelect").value;
      const makerSelect = document.getElementById("makerSelect");

      if (selectedCarName) {
        // 選ばれた車名から、該当する車データを検索
        const foundCar = carDatabase.find(car => getDbCarName(car) === selectedCarName);
        if (foundCar) {
          // 見つかったメーカーをセット
          makerSelect.value = foundCar.maker;
          // 絞り込みを実行して他のメーカーの車を隠す
          updateCarSelect();
          // 絞り込み直後だと車名の選択が外れちゃうから、もう一度セット！
          document.getElementById("carNameSelect").value = selectedCarName;
        }
      }
    }

    // --- 4. 車名プルダウンの更新ロジック ---
    function updateCarSelect() {
      const selectedMaker = document.getElementById("makerSelect").value;
      const carSelect = document.getElementById("carNameSelect");

      // 現在選択されている車名を記憶しておく
      const currentCarValue = carSelect.value;

      // 中身をリセット
      carSelect.innerHTML = '<option value="">選択してください</option>';

      let filteredCars = carDatabase;
      // メーカーが選ばれていたら、そのメーカーの車だけに絞り込む
      if (selectedMaker) {
        filteredCars = carDatabase.filter(car => car.maker === selectedMaker);
      }

      // 絞り込んだ結果をプルダウンに追加
      filteredCars.forEach(car => {
        const option = document.createElement("option");
        option.value = getDbCarName(car);
        option.textContent = car.name;
        carSelect.appendChild(option);
      });

      // リセット前に選んでいた車名が、絞り込み後のリストに残っていれば再選択状態にする
      if (filteredCars.some(car => getDbCarName(car) === currentCarValue)) {
        carSelect.value = currentCarValue;
      }
    }
  </script>
</body>
</html>