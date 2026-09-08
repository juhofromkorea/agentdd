<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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

    <script>
      if (localStorage.getItem("appTheme") === "dark") {
          document.documentElement.classList.add("dark-mode");
      }
    </script>
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
          <a class="print-breadcrumb" href="${pageContext.request.contextPath}/mockup/top">トップへ戻る</a>

          <div class="print-content">
            <h1 class="print-title" id="print-title">申込書印刷確認</h1>

            <dl class="print-summary">
              <div>
                <dt>一回分保険料</dt>
                <!-- fmt:formatNumber で金額にカンマをつける！ -->
                <dd><fmt:formatNumber value="${claim.premiumInstallment}" />円</dd>
              </div>
              <div>
                <dt>総額保険料</dt>
                <dd><fmt:formatNumber value="${claim.premiumAmount}" />円</dd>
              </div>
              <div>
                <dt>契約者</dt>
                <!-- c:out を使って安全に名前を表示する！ -->
                <dd><c:out value="${contract.nameKanji1} ${contract.nameKanji2}" />様</dd>
              </div>
              <div>
                <dt>払込方法</dt>
                <!-- DBの数字(int)に合わせて文字を切り替える！ -->
                <dd>
                  <c:choose>
                    <c:when test="${contract.paymentMethod == 1}">クレジットカード</c:when>
                    <c:when test="${contract.paymentMethod == 2}">口座振替</c:when>
                    <c:when test="${contract.paymentMethod == 3}">払込票</c:when>
                    <c:otherwise>未設定</c:otherwise>
                  </c:choose>
                </dd>
              </div>
              <div>
                <dt>払込回数</dt>
                <dd><c:out value="${contract.installment}" />回</dd>
              </div>
            </dl>

            <div class="print-actions">
              <a
                class="button button--secondary print-button-link"
                href="${pageContext.request.contextPath}/estimateprint"
              >戻る</a>
              <form
                action="${pageContext.request.contextPath}/estimateprint"
                method="post"
              >
                <button
                  class="button button--primary print-button-link"
                  type="submit"
                >印刷</button>
              </form>
            </div>
          </div>
        </section>
      </main>
    </div>
  </body>
</html>
