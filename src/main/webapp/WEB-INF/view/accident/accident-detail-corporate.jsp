<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
  <html lang="ja">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>事故受付入力（法人） | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/accounting.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/accident.css" />
  </head>

  <body>
    <input class="theme-controller" type="checkbox" id="theme-toggle" aria-label="ダークモードに切り替える" />

    <div class="app-shell">
      <header class="app-header">
        <div class="app-header__title">
          <svg class="app-header__home" viewBox="0 0 48 44" aria-hidden="true">
            <path d="M4 21 24 4l20 17M10 19v21h28V19M19 40V27h10v13" fill="none" stroke="currentColor" stroke-width="4"
              stroke-linecap="square" stroke-linejoin="miter" />
          </svg>
          <span>事故受付</span>
        </div>

        <label class="theme-switch" for="theme-toggle">
          <span>ダークモード</span>
          <span class="theme-switch__track" aria-hidden="true">
            <span class="theme-switch__thumb"></span>
          </span>
        </label>
      </header>

      <main class="app-main accounting-main">
        <section class="card content-card accounting-card accident-card" aria-labelledby="accident-detail-title">
          <h1 class="sr-only" id="accident-detail-title">法人契約の事故受付入力</h1>
          <a class="accounting-breadcrumb" href="${pageContext.request.contextPath}/top">トップへ戻る</a>

          <form class="accounting-workspace" action="${pageContext.request.contextPath}/accident/submit" method="post">

            <input type="hidden" name="claimNo" value="${fn:escapeXml(accident.claimNo)}" />
            <input type="hidden" name="polNo" value="${fn:escapeXml(accident.polNo)}" />

            <input class="accounting-controller" type="radio" name="accident-tab" id="accident-tab-reception" checked />
            <input class="accounting-controller" type="radio" name="accident-tab" id="accident-tab-contract" />
            <input class="accounting-controller" type="radio" name="accident-tab" id="accident-tab-coverage" />

            <c:if test="${not empty errorMessage}">
              <p role="alert"><c:out value="${errorMessage}" /></p>
            </c:if>

            <div class="accounting-tabs accident-tabs" aria-label="事故受付メニュー">
              <label class="accounting-tab" for="accident-tab-reception">事故受付</label>
              <label class="accounting-tab" for="accident-tab-contract">契約条件</label>
              <label class="accounting-tab" for="accident-tab-coverage">補償</label>
            </div>

            <div class="accounting-panels">
              <section class="accounting-panel accident-panel--reception">
                <h2 class="accounting-section-title">事故情報</h2>

                <dl class="accident-record-summary">
                  <div>
                    <dt>事故受付番号</dt>
                    <dd><c:out value='${empty accident.claimNo ? "保存時に採番" : accident.claimNo}' /></dd>
                  </div>
                  <div>
                    <dt>証券番号</dt>
                    <dd><c:out value='${contract.polNo}' /></dd>
                  </div>
                  <div>
                    <dt>会社名</dt>
                    <dd><c:out value='${contract.nameKanji1}' /></dd>
                  </div>
                </dl>

                <fieldset class="accident-form-section">
                  <legend>事故発生状況</legend>
                  <div class="accident-input-grid">
                    <label class="accident-field accident-field--wide">
                      <span>事故発生日</span>
                      <input class="accident-input" type="text" name="accidentDate" value="${fn:escapeXml(requestScope.accidentInput == null ? accident.accidentDate : accidentInput.accidentDate)}"
                        inputmode="numeric" maxlength="8" pattern="[0-9]{8}" placeholder="例：20260827" />
                    </label>
                    <label class="accident-field">
                      <span>事故現場住所1</span>
                      <input class="accident-input" type="text" name="accidentLocationKanji1"
                        value="${fn:escapeXml(requestScope.accidentInput == null ? accident.accidentLocationKanji1 : accidentInput.accidentLocationKanji1)}" maxlength="48" placeholder="例：東京都多摩市" />
                    </label>
                    <label class="accident-field">
                      <span>事故現場住所1（カナ）</span>
                      <input class="accident-input" type="text" name="accidentLocationKana1"
                        value="${fn:escapeXml(requestScope.accidentInput == null ? accident.accidentLocationKana1 : accidentInput.accidentLocationKana1)}" maxlength="48" placeholder="例：トウキョウトタマシ" />
                    </label>
                    <label class="accident-field">
                      <span>事故現場住所2</span>
                      <input class="accident-input" type="text" name="accidentLocationKanji2"
                        value="${fn:escapeXml(requestScope.accidentInput == null ? accident.accidentLocationKanji2 : accidentInput.accidentLocationKanji2)}" maxlength="48" placeholder="例：1-1-1 サンプル交差点付近" />
                    </label>
                    <label class="accident-field">
                      <span>事故現場住所2（カナ）</span>
                      <input class="accident-input" type="text" name="accidentLocationKana2"
                        value="${fn:escapeXml(requestScope.accidentInput == null ? accident.accidentLocationKana2 : accidentInput.accidentLocationKana2)}" maxlength="48" placeholder="例：1-1-1 サンプルコウサテンフキン" />
                    </label>
                    <label class="accident-field accident-field--wide">
                      <span>相手の状況</span>
                      <textarea class="accident-textarea" name="accidentSituation" maxlength="100"
                        placeholder="例：相手車両の状況や負傷者の有無を入力"><c:out value='${requestScope.accidentInput == null ? accident.accidentSituation : accidentInput.accidentSituation}' /></textarea>
                    </label>
                    <label class="accident-field">
                      <span>被保険者の過失割合</span>
                      <span class="accident-affixed-control">
                        <input class="accident-input" type="text" name="ratingBlameMyself"
                          value="${fn:escapeXml(requestScope.accidentInput == null ? accident.ratingBlameMyself : accidentInput.ratingBlameMyself)}" inputmode="numeric" maxlength="3"
                          pattern="([0-9]|[1-9][0-9]|100)" placeholder="例：30" />
                        <span class="accident-affix">%</span>
                      </span>
                    </label>
                    <label class="accident-field">
                      <span>相手方の過失割合</span>
                      <span class="accident-affixed-control">
                        <input class="accident-input" type="text" name="ratingBlameYourself"
                          value="${fn:escapeXml(requestScope.accidentInput == null ? accident.ratingBlameYourself : accidentInput.ratingBlameYourself)}" inputmode="numeric" maxlength="3"
                          pattern="([0-9]|[1-9][0-9]|100)" placeholder="例：70" />
                        <span class="accident-affix">%</span>
                      </span>
                    </label>
                    <p class="accident-ratio-note">※双方の過失割合の合計が100になるように入力します。</p>
                  </div>
                </fieldset>

                <fieldset class="accident-form-section">
                  <legend>損害情報</legend>
                  <div class="accident-damage-list">
                    <details class="accident-damage-section" open>
                      <summary>車両</summary>
                      <div class="accident-damage-content">
                        <label class="accident-field">
                          <span>車両損害額</span>
                          <span class="accident-affixed-control">
                            <input class="accident-input" type="text" name="damageCarPrice"
                              value="${fn:escapeXml(requestScope.accidentInput == null ? accident.damageCarPrice : accidentInput.damageCarPrice)}" inputmode="numeric" maxlength="18"
                              pattern="[0-9]{1,18}" placeholder="例：250000" />
                            <span class="accident-affix">円</span>
                          </span>
                        </label>
                        <label class="accident-field">
                          <span>車両損害状況</span>
                          <textarea class="accident-textarea" name="damageCarState" maxlength="48"
                            placeholder="例：右前方バンパーにへこみ"><c:out value='${requestScope.accidentInput == null ? accident.damageCarState : accidentInput.damageCarState}' /></textarea>
                        </label>
                      </div>
                    </details>
                    <details class="accident-damage-section">
                      <summary>対人</summary>
                      <div class="accident-damage-content">
                        <label class="accident-field">
                          <span>対人損害額</span>
                          <span class="accident-affixed-control">
                            <input class="accident-input" type="text" name="damageBodilyPrice"
                              value="${fn:escapeXml(requestScope.accidentInput == null ? accident.damageBodilyPrice : accidentInput.damageBodilyPrice)}" inputmode="numeric" maxlength="18"
                              pattern="[0-9]{1,18}" placeholder="例：100000" />
                            <span class="accident-affix">円</span>
                          </span>
                        </label>
                        <label class="accident-field">
                          <span>対人損害状況</span>
                          <textarea class="accident-textarea" name="damageBodilyState" maxlength="48"
                            placeholder="例：通院状況などを入力"><c:out value='${requestScope.accidentInput == null ? accident.damageBodilyState : accidentInput.damageBodilyState}' /></textarea>
                        </label>
                      </div>
                    </details>
                    <details class="accident-damage-section">
                      <summary>対物</summary>
                      <div class="accident-damage-content">
                        <label class="accident-field">
                          <span>対物損害額</span>
                          <span class="accident-affixed-control">
                            <input class="accident-input" type="text" name="damagePropertyPrice"
                              value="${fn:escapeXml(requestScope.accidentInput == null ? accident.damagePropertyPrice : accidentInput.damagePropertyPrice)}" inputmode="numeric" maxlength="18"
                              pattern="[0-9]{1,18}" placeholder="例：50000" />
                            <span class="accident-affix">円</span>
                          </span>
                        </label>
                        <label class="accident-field">
                          <span>対物被害状況</span>
                          <textarea class="accident-textarea" name="damagePropertyState" maxlength="48"
                            placeholder="例：ガードレールの破損"><c:out value='${requestScope.accidentInput == null ? accident.damagePropertyState : accidentInput.damagePropertyState}' /></textarea>
                        </label>
                      </div>
                    </details>
                    <details class="accident-damage-section">
                      <summary>傷害</summary>
                      <div class="accident-damage-content">
                        <label class="accident-field">
                          <span>傷害損害額</span>
                          <span class="accident-affixed-control">
                            <input class="accident-input" type="text" name="damageAccidentPrice"
                              value="${fn:escapeXml(requestScope.accidentInput == null ? accident.damageAccidentPrice : accidentInput.damageAccidentPrice)}" inputmode="numeric" maxlength="18"
                              pattern="[0-9]{1,18}" placeholder="例：30000" />
                            <span class="accident-affix">円</span>
                          </span>
                        </label>
                        <label class="accident-field">
                          <span>傷害損害状況</span>
                          <textarea class="accident-textarea" name="damageAccidentState" maxlength="48"
                            placeholder="例：搭乗者の負傷状況"><c:out value='${requestScope.accidentInput == null ? accident.damageAccidentState : accidentInput.damageAccidentState}' /></textarea>
                        </label>
                      </div>
                    </details>
                  </div>
                </fieldset>

                <div class="accident-panel-actions">
                  <button class="button button--primary" type="submit" name="action" value="updateStatus">状況更新</button>
                  <button class="button button--primary" type="submit" name="action" value="completeReceipt">事故受付完了</button>
                </div>
              </section>

              <section class="accounting-panel accident-panel--contract">
                <h2 class="accounting-section-title">契約情報</h2>
                <dl class="accounting-data-list">
                  <div class="accounting-data-row">
                    <dt>保険期間</dt>
                    <dd><c:out value='${contract.inceptionDate}' /> <c:out value='${contract.inceptionTime}' /> ～ <c:out value='${contract.conclusionDate}' />
                      <c:out value='${contract.conclusionTime}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>証券番号</dt>
                    <dd><c:out value='${contract.polNo}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>印刷連番</dt>
                    <dd><c:out value='${contract.insatsuRenban}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>被保険者区分</dt>
                    <dd><c:out value='${contract.insuredStr}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>払込方法</dt>
                    <dd><c:out value='${contract.paymentStr}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>払込回数</dt>
                    <dd><c:out value='${contract.installment}' />回</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>会社名（漢字）</dt>
                    <dd><c:out value='${contract.nameKanji1}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>会社名（カナ）</dt>
                    <dd><c:out value='${contract.nameKana1}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>住所（漢字）</dt>
                    <dd><c:out value='${contract.addressKanji1}' /> <c:out value='${contract.addressKanji2}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>住所（カナ）</dt>
                    <dd><c:out value='${contract.addressKana1}' /> <c:out value='${contract.addressKana2}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>郵便番号</dt>
                    <dd><c:out value='${contract.postcode}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>電話番号</dt>
                    <dd><c:out value='${contract.telephoneNo}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>携帯電話番号</dt>
                    <dd><c:out value='${contract.mobilephoneNo}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>FAX番号</dt>
                    <dd><c:out value='${contract.faxNo}' /></dd>
                  </div>
                </dl>
                <div class="accident-panel-actions">
                  <button class="button button--primary" type="submit" name="action" value="updateStatus">状況更新</button>
                  <button class="button button--primary" type="submit" name="action" value="completeReceipt">事故受付完了</button>
                </div>
              </section>

              <section class="accounting-panel accident-panel--coverage">
                <div class="accounting-coverage-header">
                  <h2 class="accounting-section-title">自動車保険試算結果</h2>
                  <div class="accounting-premium-summary">
                    <span>総額保険料</span>
                    <strong><c:out value='${claim.premiumAmount}' />円</strong>
                  </div>
                </div>
                <dl class="accounting-data-list accounting-data-list--coverage">
                  <div class="accounting-data-row">
                    <dt>一回分保険料</dt>
                    <dd><c:out value='${claim.premiumInstallment}' />円</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>メーカー</dt>
                    <dd><c:out value='${claim.maker}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>車名</dt>
                    <dd><c:out value='${claim.carName}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>車のナンバー</dt>
                    <dd><c:out value='${claim.licenseNo}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>車両保険金額</dt>
                    <dd><c:out value='${claim.vehiclePrice}' />円</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>免許証の色</dt>
                    <dd><c:out value='${claim.licenseColor}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>車両料率</dt>
                    <dd><c:out value='${claim.vehicleRates}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>対人料率</dt>
                    <dd><c:out value='${claim.bodilyRates}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>対物料率</dt>
                    <dd><c:out value='${claim.propertyDamageRates}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>傷害料率</dt>
                    <dd><c:out value='${claim.accidentRates}' /></dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>年齢条件</dt>
                    <dd><c:out value='${claim.ageLimit}' />歳以上</dd>
                  </div>
                </dl>
                <div class="accident-panel-actions">
                  <button class="button button--primary" type="submit" name="action" value="updateStatus">状況更新</button>
                  <button class="button button--primary" type="submit" name="action" value="completeReceipt">事故受付完了</button>
                </div>
              </section>
            </div>
          </form>
        </section>
      </main>
    </div>
  </body>

  </html>