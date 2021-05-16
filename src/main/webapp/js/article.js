

var searchURL = window.location.search;
var searchId = searchURL.split("=");


$(document).ready(function () {
    searchContent();
})
function searchContent() {
        $.ajax({
                type: "get",
                dataType: "json",
                url: "http://localhost:8081/blog/content/findContent?id="+searchId[1],
                async: true,
                success: function (data) {
                    $(".article-title").html(data.title);
                    $(".create-time").html(new Date(data.createTime).toLocaleDateString());
                    $(".tag").html(data.tag);
                    $(".pv").html("共"+data.pv+"围观");
                    $(".article-content").html(data.content);
                    $(".tag2").html(data.tag);
                },
                error: function () {
                    alert("请不要乱搜索╰（‵□′）╯")
                }
            }
        )
    }
