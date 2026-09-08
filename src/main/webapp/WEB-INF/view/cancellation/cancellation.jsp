<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>解約開始 | Agent d.d</title>
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
          class="card content-card accounting-card accounting-card--start"
          aria-labelledby="cancellation-start-title"
        >
          <a class="accounting-breadcrumb" href="${pageContext.request.contextPath}/top">トップへ戻る</a>

          <div class="accounting-start-content">
            <h1 class="accounting-page-title" id="cancellation-start-title">
              解約開始
            </h1>

            <form
              class="accounting-start-form"
              action="${pageContext.request.contextPath}/cancellation-detail"
              method="get"
            >
              <label class="accounting-start-field">
                <span>証券番号</span>
                <input
                  class="accounting-control"
                  type="text"
                  name="polNo"
                  inputmode="text"
                  maxlength="10"
                  placeholder="例：B00000001"
                  pattern="[A-Za-z0-9]{1,10}"
                  aria-describedby="cancellation-start-message"
                  required
                  value="${sessionScope.constract.polNo }"
                />
              </label>

              <p class="accounting-error-space" id="cancellation-start-message">
                ※入力内容に誤りがある場合、ここにメッセージを表示します。
              </p>

              <button class="button button--primary accounting-submit" type="submit">
                次へ
              </button>
            </form>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>
