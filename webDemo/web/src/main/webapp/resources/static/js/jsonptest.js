/**
 * Created by chengxiang on 16/4/21.
 */
function testJsonp(){
    alert("test");

    $.ajax({
        type:"GET",
        data:{},
        url:"http://localhost:8080/jsonpTest?jsonpCallback=?",
        dataType:"jsonp",
        jsonp:"callback",
        jsonpCallback:"callbackTest",
        success:function(data){
            alert(data);
            alert(data.id+",code:"+data.code);
        },
        error:function(data){
            alert("error");
        }


    });

}