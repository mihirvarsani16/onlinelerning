const toggleSidebar = () => {

    if ($('.sidebar').is(":visible")) {
        //true
        //bandh karna he
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    } else {
        //false
        //show karna he
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }

};