
function getBlanket(windowname){
    blanket_size(windowname);
    window_pos(windowname);
    toggle('blanket');
    toggle(windowname);
}
function getBlanket1(windowname){
    blanket_size(windowname);
    window_pos1(windowname);
    toggle('blanket');
    toggle(windowname);
}

function toggle(div_id) {    
    var el = document.getElementById(div_id);
    if ( el.style.display == 'none' ){
        el.style.display = 'block';
    } else {
        el.style.display = 'none';
    }
}
function blanket_size(popUpDivVar){
    if (typeof window.innerWidth != 'undefined') {
        viewportheight = window.innerHeight;
    } else {
        viewportheight = document.documentElement.clientHeight;
    }
    if ((viewportheight > document.body.parentNode.scrollHeight) && (viewportheight > document.body.parentNode.clientHeight)){
        blanket_height = viewportheight;
    } else {
        if (document.body.parentNode.clientHeight > document.body.parentNode.scrollHeight) {
            blanket_height = document.body.parentNode.clientHeight;
        } else {
            blanket_height = document.body.parentNode.scrollHeight;
        }
    }
    var blanket = document.getElementById('blanket');
    blanket.style.height = blanket_height + 'px';
    var popUpDiv_js = document.getElementById(popUpDivVar);
    popUpDiv_height=blanket_height/2-150;     //150 is half popup's height
    popUpDiv_js.style.top = popUpDiv_height + 'px';
}

function window_pos(popUpDivVar) {
    if (typeof window.innerWidth != 'undefined') {
        viewportwidth = window.innerHeight;
    } else {
        viewportwidth = document.documentElement.clientHeight;
    }
    if ((viewportwidth > document.body.parentNode.scrollWidth) && (viewportwidth > document.body.parentNode.clientWidth)){
        window_width = viewportwidth;
    } else {
        if (document.body.parentNode.clientWidth > document.body.parentNode.scrollWidth) {
            window_width = document.body.parentNode.clientWidth;
        } else {
            window_width = document.body.parentNode.scrollWidth;
        }
    }
    var popUpDiv = document.getElementById(popUpDivVar);
    window_width=window_width/2-150;//150 is half popup's width
    popUpDiv.style.left = window_width + 'px';
}
function window_pos1(popUpDivVar){
    if (typeof window.innerWidth != 'undefined') {
        viewportwidth = window.innerHeight;
    } else {
        viewportwidth = document.documentElement.clientHeight;
    }
    if ((viewportwidth > document.body.parentNode.scrollWidth) && (viewportwidth > document.body.parentNode.clientWidth)){
        window_width = viewportwidth;
    } else {
        if (document.body.parentNode.clientWidth > document.body.parentNode.scrollWidth) {
            window_width = document.body.parentNode.clientWidth;
        } else {
            window_width = document.body.parentNode.scrollWidth;
        }
    }
    var popUpDiv = document.getElementById(popUpDivVar);
    window_width=window_width/2-150;//150 is half popup's width
    popUpDiv.style.left = window_width + 'px' ;
    //    popUpDiv.style.top = '0px' ;

    var popUpDiv_js = document.getElementById(popUpDivVar);
    popUpDiv_height=blanket_height/2-150;     //150 is half popup's height
    popUpDiv_js.style.top = popUpDiv_height + 'px';

}

