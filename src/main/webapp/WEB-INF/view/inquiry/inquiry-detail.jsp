<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>契約内容照会確認（個人） | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/accounting.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/inquiry.css" />
    <script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
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
          <span>契約内容照会</span>
        </div>

        <label class="theme-switch" for="theme-toggle">
          <span>ダークモード</span>
          <span class="theme-switch__track" aria-hidden="true">
            <span class="theme-switch__thumb"></span>
          </span>
        </label>
      </header>

      <main class="app-main accounting-main">
        <section
          class="card content-card accounting-card inquiry-result-card"
          aria-labelledby="inquiry-detail-title"
        >
          <h1 class="sr-only" id="inquiry-detail-title">
            個人契約の契約内容照会確認
          </h1>
          <a class="accounting-breadcrumb" href="${pageContext.request.contextPath}/top">トップへ戻る</a>

          <div class="accounting-workspace">
            <input
              class="accounting-controller"
              type="radio"
              name="inquiry-tab"
              id="accounting-tab-contract"
              checked
            />
            <input
              class="accounting-controller"
              type="radio"
              name="inquiry-tab"
              id="accounting-tab-coverage"
            />

            <div class="accounting-tabs" aria-label="契約内容照会メニュー">
              <label class="accounting-tab" for="accounting-tab-contract">
                契約条件
              </label>
              <label class="accounting-tab" for="accounting-tab-coverage">
                補償
              </label>
            </div>

            <div class="accounting-panels">
              <section class="accounting-panel accounting-panel--contract">
                <h2 class="accounting-section-title">契約情報</h2>

                <dl class="accounting-data-list">
                  <div class="accounting-data-row">
                    <dt>印刷連番</dt>
                    <dd>${contract.insatsuRenban}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>証券番号</dt>
                    <dd>${contract.polNo}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>契約状態</dt>
                    <dd>
                      <c:choose>
                        <c:when test="${contract.cancelFlg}">
                          解約済み
                        </c:when>
                        <c:when test="${contract.statusFlg eq 0}">
                          契約済み
                        </c:when>
                        <c:otherwise>
                          <c:out value="${contract.statusStr}" />
                        </c:otherwise>
                      </c:choose>
                    </dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>被保険者区分</dt>
                    <dd>${contract.insuredStr}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>姓（漢字）</dt>
                    <dd>${contract.nameKanji1}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>名（漢字）</dt>
                    <dd>${contract.nameKanji2}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>姓（カナ）</dt>
                    <dd>${contract.nameKana1}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>名（カナ）</dt>
                    <dd>${contract.nameKana2}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>性別</dt>
                    <dd>${contract.genderStr}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>生年月日</dt>
                    <dd>${contract.formattedBirthday}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>郵便番号</dt>
                    <dd>${contract.formattedPostcode}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>住所1（漢字）</dt>
                    <dd>${contract.addressKanji1}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>住所2（漢字）</dt>
                    <dd>${contract.addressKanji2}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>住所1（カナ）</dt>
                    <dd>${contract.addressKana1}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>住所2（カナ）</dt>
                    <dd>${contract.addressKana2}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>電話番号</dt>
                    <dd>${contract.formattedTelephoneNo}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>携帯電話番号</dt>
                    <dd>${contract.formattedMobilephoneNo}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>FAX番号</dt>
                    <dd>${contract.formattedFaxNo}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>保険期間始期日</dt>
                    <dd>${contract.formattedInceptionDate}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>保険期間始期時刻</dt>
                    <dd>${contract.formattedInceptionTime}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>保険期間満期日</dt>
                    <dd>${contract.formattedConclusionDate}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>保険期間満期時刻</dt>
                    <dd>${contract.formattedConclusionTime}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>払込方法</dt>
                    <dd>${contract.paymentStr}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>払込回数</dt>
                    <dd>${contract.installment}</dd>
                  </div>
                </dl>
              </section>

              <section class="accounting-panel accounting-panel--coverage">
                <div class="accounting-coverage-header">
                  <h2 class="accounting-section-title">自動車保険試算結果</h2>
                  <div class="accounting-premium-summary">
                    <span>総額保険料</span>
                    <strong>${claim.premiumAmount}円</strong>
                  </div>
                </div>

                <dl class="accounting-data-list accounting-data-list--coverage">
                  <div class="accounting-data-row">
                    <dt>メーカー</dt>
                    <dd>${claim.maker}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>車名</dt>
                    <dd>${claim.carName}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>車のナンバー</dt>
                    <dd>${claim.licenseNo}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>車両保険金額</dt>
                    <dd>${claim.vehiclePrice}万円</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>免許証の色</dt>
                    <dd>${claim.licenseColorStr}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>車両料率</dt>
                    <dd>${claim.vehicleRateValue}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>対人料率</dt>
                    <dd>${claim.bodilyRateValue}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>対物料率</dt>
                    <dd>${claim.propertyDamageRateValue}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>傷害料率</dt>
                    <dd>${claim.accidentRateValue}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>年齢条件</dt>
                    <dd>${claim.ageLimitStr}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>一回分保険料</dt>
                    <dd>${claim.premiumInstallment}円</dd>
                  </div>
                </dl>
              </section>
            </div>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>
