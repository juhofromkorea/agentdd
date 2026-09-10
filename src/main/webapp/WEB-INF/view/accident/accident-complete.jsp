<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>事故受付完了 | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/accounting.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/accident.css" />
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
          <svg class="app-header__home" viewBox="0 0 48 44" aria-hidden="true">
            <path
              d="M4 21 24 4l20 17M10 19v21h28V19M19 40V27h10v13"
              fill="none"
              stroke="currentColor"
              stroke-width="4"
              stroke-linecap="square"
              stroke-linejoin="miter"
            />
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
        <section
          class="card content-card accounting-card accounting-card--complete accident-card"
          aria-labelledby="accident-complete-title"
        >
          <a class="accounting-breadcrumb" href="${pageContext.request.contextPath}/top">トップへ戻る</a>

          <div class="accounting-complete-content accident-complete-content">
            <div class="accounting-complete-mark" aria-hidden="true">✓</div>
            <h1 class="accounting-page-title" id="accident-complete-title">
              <c:out value="${completeMessage}" />
            </h1>

            <dl class="accounting-complete-summary accident-complete-summary">
              <div>
                <dt>事故受付番号</dt>
                <dd><c:out value="${claimNo}" /></dd>
              </div>
              <div>
                <dt>証券番号</dt>
                <dd><c:out value="${polNo}" /></dd>
              </div>
              <div>
                <dt>契約者名</dt>
                <dd><c:out value="${contractorName}" /></dd>
              </div>
              <div>
                <dt>支払金額</dt>
                <dd><fmt:formatNumber value="${paymentAmount}" pattern="#,##0" />円</dd>
              </div>
            </dl>

          </div>
        </section>
      </main>
    </div>
  </body>
</html>