<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>エラー | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/error.css" />
    <script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
  </head>
  <body>
    <input
      class="theme-controller"
      type="checkbox"
      id="theme-toggle"
      aria-label="ダークモードに切り替える"
    />

    <c:url var="backUrl"
      value="${empty requestScope.errorBackUrl ? '/login' : requestScope.errorBackUrl}" />
    <c:set var="backLabel"
      value="${empty requestScope.errorBackLabel ? 'ログイン画面へ戻る' : requestScope.errorBackLabel}" />

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
          <span>エラー</span>
        </div>

        <label class="theme-switch" for="theme-toggle">
          <span>ダークモード</span>
          <span class="theme-switch__track" aria-hidden="true">
            <span class="theme-switch__thumb"></span>
          </span>
        </label>
      </header>

      <main class="app-main error-main">
        <section class="card content-card error-card" aria-labelledby="error-title">
          <div class="error-content">
            <h1 class="error-message" id="error-title">
              <c:out
                value="${empty requestScope.error ? '予期せぬエラーが発生しました。' : requestScope.error}" />
            </h1>
            <a class="button button--primary error-back-button" href="${backUrl}">
              <c:out value="${backLabel}" />
            </a>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>
