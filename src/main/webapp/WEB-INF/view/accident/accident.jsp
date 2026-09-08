<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="color-scheme" content="light dark" />
    <title>事故受付開始 | Agent d.d</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tokens.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/accounting.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/accident.css" />
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
          class="card content-card accounting-card accounting-card--start accident-card"
          aria-labelledby="accident-start-title"
        >
          <!-- 修正①：トップページへのリンクパスを修正 -->
          <a class="accounting-breadcrumb" href="${pageContext.request.contextPath}/top">トップへ戻る</a>

          <div class="accounting-start-content accident-start-content">
            <h1 class="accounting-page-title" id="accident-start-title">
              事故受付開始
            </h1>

            <!-- 修正②：formのactionを本番Controllerのパス (/accident/detail) に修正 -->
            <form
              class="accounting-start-form"
              action="${pageContext.request.contextPath}/accident/detail"
              method="get"
            >
              <div class="accident-start-fields">
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
                    aria-describedby="accident-start-note accident-start-message"
                  />
                </label>

                <label class="accounting-start-field">
                  <span>事故受付番号</span>
                  <input
                    class="accounting-control"
                    type="text"
                    name="claimNo"
                    inputmode="text"
                    maxlength="8"
                    placeholder="例：C0000001"
                    pattern="[A-Za-z0-9]{1,8}"
                    aria-describedby="accident-start-note accident-start-message"
                  />
                </label>
              </div>

              <p class="accident-start-note" id="accident-start-note">
                新規受付は証券番号、受付済み事故の更新は事故受付番号を入力してください。
              </p>
              
              <!-- 修正③：Controllerから返されたエラーメッセージを表示できるように変更 -->
              <p class="accounting-error-space" id="accident-start-message">
                ${errorMessage}
              </p>

              <button class="button button--primary accounting-submit" type="submit">
                受付開始
              </button>
            </form>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>