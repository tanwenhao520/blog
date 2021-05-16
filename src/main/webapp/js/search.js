var findMap = {
    "page": 1,
    "row": 2,
    "total":0,
    "contents": {
        "content": "",
        "createTime": "",
        "id": "",
        "issuer": "",
        "tag": "",
        "title": "",
    }
}



$(function () {
    indexImg();
   var URLsearch = window.location.search;
   var str = URLsearch.split("=");
   if(str[0] == "?search"){
        findMap.contents.title = str[1]
       findContentAll();
   }else{
       alert(str[1])
       findMap.contents.title = ""
       findContentAll();
   }


})


//首页查询方法
$("#header").click(function () {
    alert(findMap.total);
    findMap.page = 1;
    findContentAll();
})
//尾页查询方法
$("#lost").click(function () {
    alert(findMap.total);
    findMap.page = findMap.total;
    findContentAll();
})
//上一页查询方法
$("#up").click(function () {
    alert(findMap.total);
    if(findMap.page ==1  || findMap.page < 1 ){
        findContentAll();
    }else{
        findMap.page = findMap.page - 1
        findContentAll();
    }

})
//下一页查询方法
$("#down").click(function () {
    alert(findMap.total);
    if(findMap.page >= findMap.total){
        findMap.page = findMap.total;
        findContentAll();
    }else {
        findMap.page = findMap.page + 1;
        findContentAll();
    }
    })


//点击当前页查询方法
function selectPage(data){
    //点击页数不能为负数或者大于总页数
    if(data < 0 || data > findMap.total){
        findMap.total = 1;
    }
    findMap.page = data;
    findContentAll();
}
//查询所有内容
function findContentAll() {
        $.ajax({
            type: "post",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(findMap),
            url: "http://localhost:8081/blog/content/search",
            async: true,
            success: function success(data) {
                $(".contents").html("");
                $.each(data.resultList, function (index, obj) {
                    $(".contents").append("<article class=\"excerpt excerpt-1\"  style=\"padding-left: 40px;\" >" +
                        "<div class=\"media-left\"><img src=" + obj.titleImg + " class=\"media-object\" style=\"width:60px\"></div>" +
                        "<header> <a class=\"cat\" href=\"program\">" + obj.tag + "<i></i></a><h2><a href=\"article.html\" title=\"\">" + obj.title + "</a></h2></header>" +
                        "<p class=\"meta\"><time class=\"time\"><i class=\"glyphicon glyphicon-time\"></i> " + new Date(obj.createTime).toLocaleDateString() + "</time><span class=\"views\"><i class=\"glyphicon glyphicon-eye-open\"></i> 共" + obj.pv + "人围观</span></p>" +
                        "<p class=\"note contentFont\">" + obj.content + "</p>" + "</article>"
                    )
                    ;
                })
                $(".totalPage").html("共 "+data.totalPage+ "页");
                $(".itemli").remove();
                var down= $("#down");
                //如果总页数不大于5直接输出页数
                if(data.totalPage < 5){
                    for (var i = 1; data.totalPage >= i;i++){
                        down.before("<li class=\"page-item itemli\"><a class=\"page-link selectPage\" href='javascript:void(0)' onclick='selectPage("+ i +")'>" + i +"</a></li>");
                    }
                    //如果大于5则进行判断
                }else{
                    var num = 4;
                    //只输出5页
                    for (var i = 0; i <= 4; i++){
                        //如果当前页数大于或者等于总页数-5页，将总页数-5进行遍历
                        if(findMap.page >= data.totalPage - 5 ){
                            var num2 = 0;
                            num2 = data.totalPage -num;
                            down.before("<li class=\"page-item  itemli\"><a class=\"page-link selectPage\" href='javascript:void(0)' onclick='selectPage("+  num2 +")'>" + num2 +"</a></li>");
                            num--;
                            //如果当前页数小于总页数-5 则继续遍历输出
                        }else{
                            var num = 0;
                            num = num + i +findMap.page;
                            down.before("<li class=\"page-item  itemli\"><a class=\"page-link selectPage\" href='javascript:void(0)' onclick='selectPage("+  num +")'>" + num +"</a></li>");
                        }

                    }
                }
                findMap.total = data.totalPage;
            },
            error: function error() {
                $(".contents").html("<li>" + "出错啦(⊙﹏⊙)" + "</li>");
            }
        })
}

//页面加载找首页图片
function indexImg() {
    $.ajax({
        type: "get",
        dataType: "json",
        url: "http://localhost:8081/blog/index/indexImg",
        async: true,
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                if (i == 0) {
                    $(".indexImg ").append("<div class=\"item active\">" +
                        "<a href=\"#\" target=\"_blank\">" +
                        "<img src=\"" + data[i] + "\" alt=\"首页图片\" class=\"img-responsive\"></a>\n" +
                        " </div>")
                } else {
                    $(".indexImg").append("<div class=\"item\">" +
                        "<a href=\"#\" target=\"_blank\">" +
                        "<img src=\"" + data[i] + "\" alt=\"首页图片\" class=\"img-responsive\"></a>\n" +
                        " </div>")
                }
            }

        }
    })
}