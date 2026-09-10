<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>解約確認（個人） | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/accounting.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/cancellation.css" />
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
          <span>解約</span>
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
          class="card content-card accounting-card cancellation-result-card"
          aria-labelledby="cancellation-detail-title"
        >
          <h1 class="sr-only" id="cancellation-detail-title">
            個人契約の解約確認
          </h1>
          <a class="accounting-breadcrumb" href="${pageContext.request.contextPath}/top">トップへ戻る</a>

          <div class="accounting-workspace">
            <input
              class="accounting-controller"
              type="radio"
              name="cancellation-tab"
              id="accounting-tab-contract"
              checked
            />
            <input
              class="accounting-controller"
              type="radio"
              name="cancellation-tab"
              id="accounting-tab-coverage"
            />

            <div class="accounting-tabs" aria-label="解約確認メニュー">
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
                    <dd>${contract.birthday}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>郵便番号</dt>
                    <dd>${contract.postcode}</dd>
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
                    <dt>住所1（カタカナ）</dt>
                    <dd>${contract.addressKana1}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>住所2（カタカナ）</dt>
                    <dd>${contract.addressKana2}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>電話番号</dt>
                    <dd>${contract.telephoneNo}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>携帯電話番号</dt>
                    <dd>${contract.mobilephoneNo}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>FAX番号</dt>
                    <dd>${contract.faxNo}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>保険期間開始日</dt>
                    <dd>${contract.inceptionDate}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>保険期間開始時刻</dt>
                    <dd>${contract.inceptionTime}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>保険期間満期日</dt>
                    <dd>${contract.conclusionDate}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>保険期間満期時刻</dt>
                    <dd>${contract.conclusionTime}</dd>
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

                <div class="accounting-actions">
                  <form action="${pageContext.request.contextPath}/cancel/complete" method="post">
                    <input type="hidden" name="polNo" value="${contract.polNo}" />
                    <button class="button button--primary" type="submit">解約申込書印刷</button>
                  </form>
                </div>
              </section>

              <section class="accounting-panel accounting-panel--coverage">
                <div class="accounting-coverage-header">
                  <h2 class="accounting-section-title">自動車保険試算結果</h2>
                  <div class="accounting-premium-summary">
                    <span>保険料</span>
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
                    <dd>${claim.vehiclePrice}円</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>免許証の色</dt>
                    <dd>${claim.licenseColorStr}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>車両料率</dt>
                    <dd>${claim.vehicleRates}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>対人料率</dt>
                    <dd>${claim.bodilyRates}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>対物料率</dt>
                    <dd>${claim.propertyDamageRates}</dd>
                  </div>
                  <div class="accounting-data-row">
                    <dt>傷害料率</dt>
                    <dd>${claim.accidentRates}</dd>
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

                <div class="accounting-actions">
                  <form action="${pageContext.request.contextPath}/cancel/complete" method="post">
                    <input type="hidden" name="polNo" value="${contract.polNo}" />
                    <button class="button button--primary" type="submit">解約申込書印刷</button>
                  </form>
                </div>
              </section>
            </div>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>
