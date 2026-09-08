<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>解約申込書印刷完了（個人） | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/accounting.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/cancellation.css" />
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
          class="card content-card accounting-card accounting-card--complete"
          aria-labelledby="cancellation-complete-title"
        >
          <a class="accounting-breadcrumb" href="${pageContext.request.contextPath}/mockup/top">トップへ戻る</a>

          <div class="accounting-complete-content cancellation-complete-content">
            <div class="accounting-complete-mark" aria-hidden="true">✓</div>
            <h1 class="accounting-page-title" id="cancellation-complete-title">
              解約申込書の印刷が完了しました
            </h1>

            <dl class="accounting-complete-summary">
              <div>
                <dt>印刷連番</dt>
                <dd>${contract.insatsuRenban}</dd>
              </div>
              <div>
                <dt>証券番号</dt>
                <dd>${contract.polNo}</dd>
              </div>
              <div>
                <dt>契約者名</dt>
                <dd>${contract.nameKanji1}${contract.nameKanji2}</dd>
              </div>
            </dl>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>
