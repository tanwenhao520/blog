window.onload = function () {
    findTag();
    findNews();
    findContentAll();
    loginCookie();
    indexImg();
    findRecommend();
    findDayWord();
    findHotContents();
};

//页面加载查询条件
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

//获取当前时间
var newdate = new Date();
var year = newdate.getFullYear();
var month = newdate.getMonth()+1;
var date = newdate.getDate();
var day = newdate.getDay();
var showday= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
//退出登录
$(".userQuit").click(function () {
    $.ajax({
        type:"get",
        dataType:"json",
        url: "http://localhost:8081/blog/login/qiut",
        async:true,
        success:function (data) {
            alert(data.message)
            window.location.replace("http://localhost:8081/blog/")
        }
    })
})
//页面加载时查看Cookie是否存在
function loginCookie() {
    $.ajax({
            type:"get",
            dataType: "json",
            //contentType: "application/json;charset=utf-8",
            url: "http://localhost:8081/blog/login/findLoginCookie",
            async: true,
            success: function success(data) {
                if(data.userName != null){
                    $(".login").html("");
                    $(".register").html("");
                    $(".myUser").html("欢迎您:"+ data.userName);
                    $(".userQuit").html("退出登录");
                }
            }
    })

}

<!--  分页JS方法-->
//首页查询方法
$("#header").click(function () {
    findMap.page = 1;
    findContentAll();
})
//尾页查询方法
$("#lost").click(function () {
    findMap.page = findMap.total;
    findContentAll();
})
//上一页查询方法
$("#up").click(function () {
    if(findMap.page ==1  || findMap.page < 1 ){
        findContentAll();
    }else{
        findMap.page = findMap.page - 1
        findContentAll();
    }

})
//下一页查询方法
$("#down").click(function () {
    if(findMap.page >= findMap.total){
        findMap.page = findMap.total;
        findContentAll();
    }else{
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
<!-- 分页JS方法-->




//登录按钮点击事件
$(".loginUser").click(function(){
    var userName= $("#userName").val();
    var passWord = $("#passWord").val();
    $(".uMessage").html("");
    $(".pMessage").html("");
    if(userName!= "" && passWord!=""){
        function findUser() {
            $.ajax({
                type:"get",
                dataType: "json",
                //contentType: "application/json;charset=utf-8",
                data:{"userName":userName,"passWord":passWord},
                url: "http://localhost:8081/blog/login/loginUser",
                async: true,
                success: function success(data) {
                    if (data.success == true) {
                        alert(data.message);
                        window.location.replace("http://localhost:8081/blog") ;
                    }else{
                        $(".eMessage").html(data.message);
                    }
                },
                error: function error(data) {
                    alert(data.message);
                }
            });
        }
        findUser();
    }else if (userName =="") {
        $(".uMessage").html("请输入用户名");
    }else if (passWord ==""){
        $(".pMessage").html("请输入密码");
    }
})
   /* function(){
    $.ajax({
        type: "post",
        dataType: "json",
        data: "",
        url: "http://localhost:8081/blog/user/loginUser",
        async: true,
        success: function success(data) {
            alert(data.message);
        },
        error: function error() {
            alert(data.m)
        }

    });
}*/

//查找标签
function findTag() {
    $.ajax({
        type: "get",
        dataType: "json",
        data: "",
        url: "http://localhost:8081/blog/tag/findAll",
        async: true,
        success: function success(data) {
            $.each(data, function (index, obj) {
                $(".tags").append("<li><a href=" + "#" + " title=" + obj.tagName + " id=" + obj.id + "'><span class=" + "thumbnail" + "></span><span class=" + "text" + ">" + obj.tagName + "</span></a></li>");
            })
        },
        error: function error() {
            $(".tags").html("\"<li><a href=\"+\"#\"+\" title='\"+error+\"'><span class=\"+\"thumbnail\"+\"></span><span class=\"+\"text\"+\">" + "抱歉,出现了异常(⊙﹏⊙)" + "</span></a></li>");
        }

    });

}

//查找新闻页面
function findNews() {
    $.ajax({
        type: "get",
        dataType: "json",
        data: "",
        url: "http://localhost:8081/blog/news/findAll",
        async: true,
        success: function success(data) {
            $(".news").html("");
            $.each(data, function (index, obj) {
                $(".news").append("<li><time datetime=" + obj.time + ">" + new Date(obj.time).toLocaleDateString() + "</time><a href=" + '#' + " target=" + "_blank" + ">" + obj.title + "</a> </li>");
            })
        },
        error: function error() {
            $(".news").html("<li><time datetime=" + "0-0-0 0:0:0" + ">+\"0-0-0 0:0:0\"+</time><a href=\"+'#'+\" target=\"+_blank+\">+" + obj.title + "+</a></li>");
        }

    });

}
//查询所有内容
function findContentAll() {
    $.ajax({
        type: "post",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(findMap),
        url: "http://localhost:8081/blog/content/findPage",
        async: true,
        success: function success(data) {
            $(".contents").html("");
            $.each(data.resultList, function (index, obj) {
                $(".contents").append("<article class=\"excerpt excerpt-1\"   style=\"padding-left: 40px;\" >" +
                    "<div class=\"media-left\"><img src=" + obj.titleImg + " class=\"media-object\" style=\"width:60px\"></div>" +
                    "<header> <a class=\"cat\" href=\"program\">" + obj.tag + "<i></i></a><h2><a href=\"article.html?id="+obj.id+"\" title=\"\">" + obj.title + "</a></h2></header>" +
                    "<p class=\"meta\"><time class=\"time\"><i class=\"glyphicon glyphicon-time\"></i> " + new Date(obj.createTime).toLocaleDateString() + "</time><span class=\"views\"><i class=\"glyphicon glyphicon-eye-open\"></i> 共" + obj.pv + "人围观</span></p>" +
                    "<p class=\"note contentFont\">" + obj.content.substring(0,50)+"......" + "</p></article>"
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
//查找solr数据
$(".searchkey").click(function () {
    var searchText = $(".searchText").val();
   window.open("search.html?search="+searchText);
})

//页面加载找首页图片
function indexImg() {
    $.ajax({
        type:"get",
        dataType:"json",
        url:"http://localhost:8081/blog/index/indexImg",
        async:true,
        success:function (data) {
            for (var i =0 ;i < data.length;i++){
                if(i == 0){
                    $(".indexImg ").append("<div class=\"item active\">" +
                        "<a href=\"#\" target=\"_blank\">" +
                        "<img src=\""+data[i]+"\" alt=\"首页图片\" class=\"img-responsive\"></a>\n" +
                        " </div>")
                }else{
                    $(".indexImg").append("<div class=\"item\">" +
                        "<a href=\"#\" target=\"_blank\">" +
                        "<img src=\""+data[i]+"\" alt=\"首页图片\" class=\"img-responsive\"></a>\n" +
                        " </div>")
                }
            }

        }
    })
}
//获取今日热点
function findRecommend() {
    $.ajax({
        type:"get",
        dataType:"json",
        url:"http://localhost:8081/blog/index/findRecommend",
        async:true,
        success:function (data) {
            $(".newsTitle").attr("href","https://xw.qq.com/"+data[0].id)
            $(".newsTitle").html(data[0].title.substring(0,30)+"......");
            $(".newsNote").html(data[0].content.substring(0,100)+"......");
        },
        error:function () {
            $(".newsNote").html("出错了┭┮﹏┭┮");
        }
    })
}

//查找每日一句
function findDayWord() {
    $.ajax({
        type:"get",
        dataType:"json",
        url:"http://localhost:8081/blog/index/findDayWord",
        async:true,
        success:function (data) {
            $(".dayDate").html(year+"年"+month+"月"+date+"日"+showday[day]);
            alert(data.id);
            alert(data.word);
            $(".dayWord1").html(data.word);
        },
        error:function () {
            $(".dayWord1").html("出错了，显示不了心灵鸡汤/(ㄒoㄒ)/~~");
        }
    })
}
//查找热门文章
function findHotContents() {
    $.ajax({
        type:"get",
        dataType:"json",
        url:"http://localhost:8081/blog/index/findHotContent",
        async:true,
        success:function (data) {
            alert(data)
            $(".hotContents").html("");
            for (var hot in data) {
                alert(data[hot].id);
                $(".hotContents").append("<li>" +
                    "<a href=\"\" class='"+data[hot].id+"'><span class=\"thumbnail\"><img class=\"thumb\" data-original=\""+data[hot].titleImg+"\" src=\""+data[hot].titleImg+"\" alt=\"\"></span><span class=\"text\">"+data[hot].title+"</span>" +
                    "<span class=\"muted\"><i class=\"glyphicon glyphicon-time\"></i>"+new Date(data[hot].createTime).toLocaleDateString()+"</span>" +
                    "<span class=\"muted\"><i class=\"glyphicon glyphicon-eye-open\"></i>"+data[hot].pv+"</span></a>" +
                    "</li>");
            }

        },
        error:function () {
            $(".hotContents").html("");
            $(".hotContents").append("<li>" +
                "<a href=\"\"><span class=\"thumbnail\"><img class=\"thumb\" data-original=\""+obj.titleImg+"\" src=\""+111+"\" alt=\"\"></span><span class=\"text\">"+服务器错误+"</span><span\n" +
                "class=\"muted\"><i class=\"glyphicon glyphicon-time\"></i>"+1999/11/21+"</span><span\n" +
                "class=\"muted\"><i class=\"glyphicon glyphicon-eye-open\"></i>"+0+"</span></a>\n" +
                "</li>")
        }
    })
}
