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
  <script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/validation.css" />
  <script defer src="${pageContext.request.contextPath}/assets/js/dataCheck.js"></script>
  <script defer src="${pageContext.request.contextPath}/assets/js/formErrors.js"></script>
  <script defer src="${pageContext.request.contextPath}/assets/js/estimate.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
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
        <c:if test="${not empty message}">
          <p class="estimate-notice estimate-notice--success" role="status">
            <c:out value="${message}" />
          </p>
        </c:if>
        <c:if test="${not empty errorMessage}">
          <p class="estimate-notice estimate-notice--error" role="alert">
            <c:out value="${errorMessage}" />
          </p>
        </c:if>

        <div class="estimate-workspace">
          <input class="estimate-tab-controller" type="radio" name="estimate-tab" id="estimate-tab-contract"
            <c:if test="${not openSaved and not calculated}">checked</c:if>
          />
          <input class="estimate-tab-controller" type="radio" name="estimate-tab" id="estimate-tab-coverage"
            <c:if test="${not openSaved and calculated}">checked</c:if>
          />
          <input class="estimate-tab-controller" type="radio" name="estimate-tab" id="estimate-tab-saved"
            <c:if test="${openSaved}">checked</c:if>
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
                ${fn:length(tempSaveList)}
                / 5
              </span>
            </label>
          </div>

          <div class="estimate-panels">
            <section class="estimate-panel estimate-panel--contract">
              <div class="estimate-form">
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
                        value="${fn:escapeXml(contract.nameKanji1)}" form="coverage-form" placeholder="例：東京" />
                    </label>
                    <label class="estimate-field estimate-field--personal">
                      <span class="estimate-field__label">名（漢字）</span>
                      <input class="estimate-control" type="text" name="nameKanji2"
                        value="${fn:escapeXml(contract.nameKanji2)}" form="coverage-form" placeholder="例：太郎" />
                    </label>

                    <label class="estimate-field estimate-field--personal">
                      <span class="estimate-field__label">姓（カナ）</span>
                      <input class="estimate-control" type="text" name="nameKana1" value="${fn:escapeXml(contract.nameKana1)}"
                        form="coverage-form" placeholder="例：トウキョウ" />
                    </label>
                    <label class="estimate-field estimate-field--personal">
                      <span class="estimate-field__label">名（カナ）</span>
                      <input class="estimate-control" type="text" name="nameKana2" value="${fn:escapeXml(contract.nameKana2)}"
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
                        test="${not empty contract.birthday and fn:length(contract.birthday) == 8}">value="${fn:escapeXml(fn:substring(contract.birthday,
                      0, 4))}-${fn:escapeXml(fn:substring(contract.birthday, 4, 6))}-${fn:escapeXml(fn:substring(contract.birthday, 6, 8))}"
                      </c:if>
                      />
                    </label>

                    <label class="estimate-field estimate-field--corporation">
                      <span class="estimate-field__label">会社名（漢字）</span>
                      <input class="estimate-control" type="text" name="nameKanji1"
                        value="${fn:escapeXml(contract.nameKanji1)}" form="coverage-form" placeholder="例：株式会社サンプル商事" />
                    </label>
                    <label class="estimate-field estimate-field--corporation">
                      <span class="estimate-field__label">会社名（カナ）</span>
                      <input class="estimate-control" type="text" name="nameKana1" value="${fn:escapeXml(contract.nameKana1)}"
                        form="coverage-form" placeholder="例：カブシキガイシャサンプルショウジ" />
                    </label>

                    <label class="estimate-field estimate-field--wide">
                      <span class="estimate-field__label">郵便番号</span>
                      <input class="estimate-control estimate-control--half" type="text" name="postcode"
                        value="${fn:escapeXml(contract.postcode)}" form="coverage-form" inputmode="numeric"
                        placeholder="例：111-1111" />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">住所1（漢字）</span>
                      <input class="estimate-control" type="text" name="addressKanji1"
                        value="${fn:escapeXml(contract.addressKanji1)}" form="coverage-form" placeholder="例：東京都多摩市" />
                    </label>
                    <label class="estimate-field">
                      <span class="estimate-field__label">住所2（漢字）</span>
                      <input class="estimate-control" type="text" name="addressKanji2"
                        value="${fn:escapeXml(contract.addressKanji2)}" form="coverage-form" placeholder="例：1-1-1" />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">住所1（カタカナ）</span>
                      <input class="estimate-control" type="text" name="addressKana1"
                        value="${fn:escapeXml(contract.addressKana1)}" form="coverage-form" placeholder="例：トウキョウトタマシ" />
                    </label>
                    <label class="estimate-field">
                      <span class="estimate-field__label">住所2（カタカナ）</span>
                      <input class="estimate-control" type="text" name="addressKana2"
                        value="${fn:escapeXml(contract.addressKana2)}" form="coverage-form" placeholder="例：1-1-1" />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">電話番号</span>
                      <input class="estimate-control" type="tel" name="telephoneNo"
                        value="${fn:escapeXml(contract.telephoneNo)}" form="coverage-form" placeholder="例：00-0000-0000" />
                    </label>
                    <label class="estimate-field">
                      <span class="estimate-field__label">携帯電話番号</span>
                      <input class="estimate-control" type="tel" name="mobilephoneNo"
                        value="${fn:escapeXml(contract.mobilephoneNo)}" form="coverage-form" placeholder="例：000-0000-0000" />
                    </label>

                    <label class="estimate-field estimate-field--wide">
                      <span class="estimate-field__label">FAX番号</span>
                      <input class="estimate-control estimate-control--half" type="tel" name="faxNo"
                        value="${fn:escapeXml(contract.faxNo)}" form="coverage-form" placeholder="例：00-0000-0000" />
                    </label>

                    <label class="estimate-field">
                      <span class="estimate-field__label">保険期間開始日</span>
                      <input class="estimate-control" type="date" name="inceptionDate" form="coverage-form"
                        <c:if
                        test="${not empty contract.inceptionDate and fn:length(contract.inceptionDate) == 8}">value="${fn:escapeXml(fn:substring(contract.inceptionDate,
                        0, 4))}-${fn:escapeXml(fn:substring(contract.inceptionDate, 4,
                        6))}-${fn:escapeXml(fn:substring(contract.inceptionDate, 6, 8))}"</c:if>
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
                        test="${not empty contract.conclusionDate and fn:length(contract.conclusionDate) == 8}">value="${fn:escapeXml(fn:substring(contract.conclusionDate,
                        0, 4))}-${fn:escapeXml(fn:substring(contract.conclusionDate, 4,
                        6))}-${fn:escapeXml(fn:substring(contract.conclusionDate, 6, 8))}"</c:if>
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
                          >直接集金</option>
                        <option value="2" <c:if test="${contract.paymentMethod == 2}">selected</c:if>>口座振替
                        </option>
                        <option value="3" <c:if test="${contract.paymentMethod == 3}">selected</c:if>>クレジットカード
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
                    <button class="button button--secondary estimate-save-button" type="submit"
                      form="coverage-form" formnovalidate
                      formaction="${pageContext.request.contextPath}/tempSave"
                      formmethod="post">一時保存</button>
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
              </div>
            </section>

            <section class="estimate-panel estimate-panel--coverage">
              <form id="coverage-form" data-validation="estimate" data-calculated="${calculated eq true}" class="estimate-form"
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
                    <select class="estimate-control" name="maker" id="makerSelect" data-initial="${fn:escapeXml(claim.maker)}"
                      required>
                      <option value="">選択してください</option>
                    </select>
                  </label>

                  <!-- 車名の入力欄をプルダウンに変更 -->
                  <label class="estimate-field">
                    <span class="estimate-field__label">車名</span>
                    <select class="estimate-control" name="carName" id="carNameSelect"
                      data-initial="${fn:escapeXml(claim.carName)}" required>
                      <option value="">選択してください</option>
                      <c:forEach var="vehicle" items="${vehicles}">
                        <option value="${fn:escapeXml(vehicle.name)}" data-maker="${fn:escapeXml(vehicle.maker)}"><c:out value="${vehicle.name}" /></option>
                      </c:forEach>
                    </select>
                  </label>


                  <label class="estimate-field">
                    <span class="estimate-field__label">車のナンバー</span>
                    <input class="estimate-control" type="text" name="licenseNo" value="${fn:escapeXml(claim.licenseNo)}"
                      required placeholder="例：品川300あ00-0000" />
                  </label>

                  <label class="estimate-field">
                    <span class="estimate-field__label">免許証の色</span>
                    <select class="estimate-control" name="licenseColor" required>
                      <option value="" <c:if test="${empty claim.licenseColor or claim.licenseColor == 0}">selected</c:if> disabled>例：ブルー</option>
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
                    <button class="button button--secondary estimate-save-button" type="submit"
                      formnovalidate formaction="${pageContext.request.contextPath}/tempSave"
                      formmethod="post">一時保存</button>
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
                  ${fn:length(tempSaveList)}
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
                    <c:choose>
                      <c:when test="${empty tempSaveList}">
                        <tr>
                          <td colspan="6">一時保存データはありません。</td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="tempSave" items="${tempSaveList}">
                          <tr>
                            <td data-label="保存日時">
                              <c:out value="${tempSave.createdAtText}" />
                            </td>
                            <td data-label="契約者名">
                              <c:out value="${tempSave.contract.nameKanji1}" />
                              <c:if test="${not empty tempSave.contract.nameKanji2}"> </c:if>
                              <c:out value="${tempSave.contract.nameKanji2}" />
                            </td>
                            <td data-label="郵便番号">
                              <c:out value="${tempSave.contract.postcode}" />
                            </td>
                            <td data-label="住所">
                              <c:out value="${tempSave.contract.addressKanji1}" />
                              <c:if test="${not empty tempSave.contract.addressKanji2}"> </c:if>
                              <c:out value="${tempSave.contract.addressKanji2}" />
                            </td>
                            <td data-label="連絡先">
                              <c:out value="${tempSave.contract.telephoneNo}" />
                            </td>
                            <td data-label="操作" class="estimate-table__actions">
                              <form action="${pageContext.request.contextPath}/tempSaveResume"
                                method="post">
                                <input type="hidden" name="tempSaveId"
                                  value="${tempSave.tempSaveId}" />
                                <button class="button estimate-row-button estimate-row-button--resume"
                                  type="submit">再開</button>
                              </form>
                              <form action="${pageContext.request.contextPath}/tempSaveDelete"
                                method="post"
                                data-confirm-delete>
                                <input type="hidden" name="tempSaveId"
                                  value="${tempSave.tempSaveId}" />
                                <button class="button estimate-row-button estimate-row-button--delete"
                                  type="submit">削除</button>
                              </form>
                            </td>
                          </tr>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>
                  </tbody>
                </table>
              </div>
            </section>
          </div>
        </div>
      </section>
      <c:set var="validationFormId" value="coverage-form" />
      <%@ include file="../template/validation-errors.jspf" %>
    </main>
  </div>
</body>
</html>
