
function bindmodel(listid,subid,data,currpage,pagecount){
    if (data){
        Pagination(listid,1,subid,currpage,pagecount);
        jQuery('#'+listid+'').html(data);
    }
}

function Paginationclick(isajax,subid,currpage,pagecount){
    if (currpage>=pagecount){
        currpage=pagecount;
    }
    if (currpage<=1){
        currpage=1;
    }
    if (isajax==1){
        jQuery('#currpage').val(currpage);
        jQuery('#'+subid+'').click();
    }else {
        if (window.location.search.indexOf("currpage")>0){
            var href=window.location.href;
            var query = location.search.substring(1);
            var params=query.split("&");
            for (var i=0;i<params.length;i++){
                if (params[i].substr(0,params[i].indexOf("=")).toLowerCase()=="currpage".toLowerCase()){
                    params[i]=params[i].substr(0,params[i].indexOf("="))+"="+currpage;
                }
                var newhref=href.substr(0,href.indexOf("?")+1)+params.join("&");
                window.location.href=newhref;
            }
        }else {
            window.location.href=window.location.href+"?currpage="+currpage;
        }
    }
}

function Pagination(listid,isajax,subid,currpage,pagecount){
    var pagehtml="<ul class=\"pagination\" style=\"float:right\">";
    if (parseInt(pagecount)<6){
        pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+parseInt(parseInt(currpage)-1)+"')\"><a href=\"javascript:void();\">&laquo;</a></li>";
        for (var i=1;i<=pagecount;i++){
            if (currpage==i){
                pagehtml+="<li class=\"active\"><a href=\"javascript:void();\">"+i+"</a></li>";
            }else {
                pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+i+"')\"><a href=\"javascript:void();\">"+i+"</a></li>";
            }
        }
        pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+parseInt(parseInt(currpage)+1)+"')\"><a href=\"javascript:void();\">&raquo;</a></li>";
    }else {
        pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+parseInt(parseInt(currpage)-1)+"')\"><a href=\"javascript:void();\">&laquo;</a></li>";
        if (parseInt(currpage)==1){
            pagehtml+="<li class=\"active\"><a href=\"javascript:void();\">1</a></li>";
            pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','2')\"><a href=\"javascript:void();\">2</a></li>";
            pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','3')\"><a href=\"javascript:void();\">3</a></li>";
            pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','4')\"><a href=\"javascript:void();\">...</a></li>";
            pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+parseInt(pagecount)+"')\"><a href=\"javascript:void();\">"+pagecount+"</a></li>";
        }else {
            if (parseInt(currpage)!=1){
                pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','1')\"><a href=\"javascript:void();\">1</a></li>";
            }
            if (parseInt(parseInt(currpage)-2)>1){
                pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+parseInt(parseInt(currpage)-2)+"')\"><a href=\"javascript:void();\">...</a></li>";
            }
            if (parseInt(parseInt(currpage)-1)>1){
                pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+parseInt(parseInt(currpage)-1)+"')\"><a href=\"javascript:void();\">"+parseInt(parseInt(currpage)-1)+"</a></li>";
            }
            pagehtml+="<li class=\"active\"><a href=\"javascript:void();\">"+currpage+"</a></li>";
            if (parseInt(parseInt(currpage)+1)<pagecount){
                pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+parseInt(parseInt(currpage)+1)+"')\"><a href=\"javascript:void();\">"+parseInt(parseInt(currpage)+1)+"</a></li>";
            }
            if (parseInt(parseInt(currpage)+2)<pagecount){
                pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+parseInt(parseInt(currpage)+2)+"')\"><a href=\"javascript:void();\">...</a></li>";
            }
            if (parseInt(currpage)!=parseInt(pagecount)){
                pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+pagecount+"')\"><a href=\"javascript:void();\">"+pagecount+"</a></li>";
            }
        }
        pagehtml+="<li onclick=\"Paginationclick('"+isajax+"','"+subid+"','"+parseInt(parseInt(currpage)+1)+"','"+pagecount+"')\"><a href=\"javascript:void();\">&raquo;</a></li>";
    }
    pagehtml+="</ul>";
    jQuery('#pagination').html('');
    jQuery('#pagination').html(pagehtml);
}

;(function ($){
    $.Pagebindmodel=function(listid,subid,data,currpage,pagecount){
        bindmodel(listid,subid,data,currpage,pagecount);
    };
    $.Pagination=function(listid,isajax,subid,currpage,pagecount){
        Pagination(listid,isajax,subid,currpage,pagecount);
    };
})(jQuery)