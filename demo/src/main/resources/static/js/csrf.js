$(document).ready(function() {
    // CSRF 토큰과 헤더 이름을 메타 태그에서 가져오기
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    // 모든 AJAX 요청에 CSRF 토큰을 포함시키기
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
