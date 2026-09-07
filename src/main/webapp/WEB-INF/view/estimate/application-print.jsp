<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>申込書印刷確認 | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/application-print.css" />
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

      <main class="app-main print-main">
        <section class="card print-card" aria-labelledby="print-title">
          <a class="print-breadcrumb" href="${pageContext.request.contextPath}/top">トップへ戻る</a>

          <div class="print-content">
            <h1 class="print-title" id="print-title">申込書印刷確認</h1>

            <dl class="print-summary">
              <div>
                <dt>一回分保険料</dt>
                <dd>10,000円</dd>
              </div>
              <div>
                <dt>総額保険料</dt>
                <dd>10,000,000円</dd>
              </div>
              <div>
                <dt>契約者</dt>
                <dd>東京 太郎様</dd>
              </div>
              <div>
                <dt>払込方法</dt>
                <dd>クレジットカード</dd>
              </div>
              <div>
                <dt>払込回数</dt>
                <dd>1</dd>
              </div>
            </dl>

            <div class="print-actions">
              <a
                class="button button--secondary print-button-link"
                href="${pageContext.request.contextPath}/estimate"
              >戻る</a>
              <a
                class="button button--primary print-button-link"
                href="${pageContext.request.contextPath}/application-print-complete"
              >印刷</a>
            </div>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>
