<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>Agent D.D</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/login.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/validation.css" />
    <script defer src="${pageContext.request.contextPath}/assets/js/dataCheck.js"></script>
    <script defer src="${pageContext.request.contextPath}/assets/js/formErrors.js"></script>
    <script defer src="${pageContext.request.contextPath}/assets/js/loginCheck.js"></script>
  </head>
  <body>
    <!-- CSSだけでテーマを切り替えるため、チェックボックスを画面全体より前に置く -->
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
          <span>ログイン</span>
        </div>

        <label class="theme-switch" for="theme-toggle">
          <span>ダークモード</span>
          <span class="theme-switch__track" aria-hidden="true">
            <span class="theme-switch__thumb"></span>
          </span>
        </label>
      </header>

      <main class="app-main">
        <section class="card login-card" aria-labelledby="login-title">
          <div class="login-card__brand" aria-hidden="true">
            <svg class="login-card__car" viewBox="0 0 48 28">
              <path
                d="M7 16 11 8c1-2 3-3 5-3h15c2 0 4 1 5 3l4 8h2c2 0 3 1 3 3v4H3v-4c0-2 1-3 3-3h1Z"
                fill="none"
                stroke="currentColor"
                stroke-width="2.5"
                stroke-linejoin="round"
              />
              <circle cx="13" cy="23" r="3.5" fill="currentColor" />
              <circle cx="36" cy="23" r="3.5" fill="currentColor" />
              <path d="M13 9h20l3 7H10l3-7Z" fill="currentColor" opacity=".2" />
            </svg>
          </div>

          <h1 class="login-card__title" id="login-title">
            AGENT D.D
          </h1>

          <form id="login-form" data-validation="login" class="login-form" action="${pageContext.request.contextPath}/login" method="post">
            <label class="sr-only" for="login-id">ID</label>
            <div class="form-field">
              <svg
                class="form-field__icon"
                viewBox="0 0 24 24"
                aria-hidden="true"
              >
                <circle cx="12" cy="8" r="4" fill="currentColor" />
                <path d="M4 21v-2c0-4 3-7 8-7s8 3 8 7v2H4Z" fill="currentColor" />
              </svg>
              <input
                class="form-field__input"
                id="login-id"
                name="userId"
                type="text"
                value="<c:out value='${requestScope.userId}' />"
                placeholder="ID"
                autocomplete="username"
                aria-describedby="login-error-message"
                aria-invalid="${not empty requestScope.error}"
                required
              />
            </div>

            <label class="sr-only" for="password">パスワード</label>
            <div class="form-field">
              <svg
                class="form-field__icon"
                viewBox="0 0 24 24"
                aria-hidden="true"
              >
                <path
                  d="M7 10V7a5 5 0 0 1 10 0v3h2v11H5V10h2Zm3 0h4V7a2 2 0 1 0-4 0v3Z"
                  fill="currentColor"
                />
                <circle cx="12" cy="15" r="1.5" fill="var(--color-input-bg)" />
              </svg>
              <input
                class="form-field__input"
                id="password"
                name="password"
                type="password"
                placeholder="パスワード"
                autocomplete="current-password"
                aria-describedby="login-error-message"
                aria-invalid="${not empty requestScope.error}"
                required
              />
            </div>

            <p
              class="login-form__error-space"
              ${empty requestScope.error ? 'hidden' : ''}
              aria-live="polite"
            >
              <c:out value="${requestScope.error}" />
            </p>

            <div class="login-form__actions">
              <button class="button button--primary" type="submit">
                ログイン
              </button>
              <button class="button button--secondary" type="reset">
                リセット
              </button>
            </div>
          </form>
        </section>
        <c:set var="validationFormId" value="login-form" />
        <%@ include file="../template/validation-errors.jspf" %>
      </main>
    </div>
  </body>
</html>
