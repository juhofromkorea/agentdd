<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>トップページ | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/top.css" />
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
          <span>トップページ</span>
        </div>

        <div class="top-header__actions">
          <a
            class="button button--secondary top-header__logout"
            href="${pageContext.request.contextPath}/logout"
          >
            ログアウト
          </a>
          <label class="theme-switch" for="theme-toggle">
            <span>ダークモード</span>
            <span class="theme-switch__track" aria-hidden="true">
              <span class="theme-switch__thumb"></span>
            </span>
          </label>
        </div>
      </header>

      <main class="app-main">
        <section class="card top-menu-card" aria-labelledby="top-menu-title">
          <div class="top-menu__list" role="group" aria-label="業務メニュー">
            <form action="${pageContext.request.contextPath}/mockup/estimate" method="get">
              <button
                class="button button--primary top-menu__button"
                type="submit"
              >
                新規試算
              </button>
            </form>
            <form action="${pageContext.request.contextPath}/mockup/accounting" method="get">
              <button
                class="button button--primary top-menu__button"
                type="submit"
              >
                計上
              </button>
            </form>
            <form action="${pageContext.request.contextPath}/mockup/inquiry" method="get">
              <button
                class="button button--primary top-menu__button"
                type="submit"
              >
                契約内容照会
              </button>
            </form>
            <form action="${pageContext.request.contextPath}/mockup/cancellation" method="get">
              <button
                class="button button--primary top-menu__button"
                type="submit"
              >
                解約
              </button>
            </form>
            <form action="${pageContext.request.contextPath}/mockup/accident" method="get">
              <button
                class="button button--primary top-menu__button"
                type="submit"
              >
                事故受付
              </button>
            </form>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>
