<%@page import="com.clipsoft.clipreport.oof.connection.OOFConnectionMemo"%>
<%@page import="com.clipsoft.clipreport.oof.OOFFile"%>
<%@page import="com.clipsoft.clipreport.oof.OOFDocument"%>
<%@page import="java.io.File"%>
<%@page import="com.clipsoft.clipreport.server.service.ReportUtil"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String content0   = request.getParameter("content0");
String content1   = request.getParameter("content1");
String fileNm 	   = request.getParameter("fileNm");
String eformDir	   = request.getParameter("eformDir");
String returnUrl   = request.getParameter("returnUrl");
if(eformDir.equals("null") || eformDir==null || eformDir.equals("")){
	eformDir ="/file/eform/";
}
OOFDocument oof = OOFDocument.newOOF();
OOFFile file = oof.addFile("crfe.root", eformDir + "/fs_Bill_form.crfe");  //crfe 경로 변경해야함~~

String contents [] = {
	content0,  content1
};

for(int i = 0; i < contents.length; i++) {
	OOFConnectionMemo memo = file.addConnectionMemo("content" + i, contents[i]);
	memo.addContentParamJSON("*", "utf-8", "{%dataset.json.root%}");	
}

%><%@include file="Property.jsp"%><%
//세션을 활용하여 리포트키들을 관리하지 않는 옵션
//request.getSession().setAttribute("ClipReport-SessionList-Allow", false);
String resultKey =  ReportUtil.createEForm(request, oof, "false", "false", request.getRemoteAddr(), propertyPath);
//리포트의 특정 사용자 ID를 부여합니다.
//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
//clipreport5.properties 의 useuserid 옵션이 true 이고 기본 예제[String resultKey =  ReportUtil.createEForm(request, oof, "false", "false", request.getRemoteAddr(), propertyPath);] 사용 했을 때 세션ID가 userID로 사용 됩니다.
//String resultKey =  ReportUtil.createEForm(request, oof, "false", "false", request.getRemoteAddr(), propertyPath, "userID");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Haccp 컨설팅</title>
<meta name="viewport" content="width=800, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="./css/clipreport.css">
<link rel="stylesheet" type="text/css" href="./css/eform.css">
<link rel="stylesheet" type="text/css" href="./css/UserConfig.css">
<link rel="stylesheet" type="text/css" href="./css/font.css">
<script type='text/javascript' src='./js/jquery-1.11.1.js'></script>
<script type='text/javascript' src='./js/clipreport.js?ver=1.0'></script>
<script type='text/javascript' src='./js/UserConfig.js'></script>
<script type='text/javascript'>
$(document).ready(function () {
	var urlPath   = document.location.protocol + "//" + document.location.host;
	var eformkey  = "<%=resultKey%>";
	var fileNm    = "<%=fileNm%>";
	var returnUrl = "<%=returnUrl%>";

	var eform = createImportJSPEForm(urlPath + "/Clip.jsp", eformkey, document.getElementById('targetDiv1'));
	eform.setStyle('save_button'  , 'display:inline;left:550px');
	eform.setStyle('close_button' , 'display:none;');
	eform.setStyle('memo_button'  , 'display:none;');
	eform.setStyle('doodle_button', 'display:none;');
	eform.setStyle("print_button" , "display:inline;left:600px");
	eform.setStyle("pdf_button"   , "display:none");
	eform.setNecessaryEnabled(false);
	eform.setClientPaintView(true);
	eform.setEndReportEvent(function(){
	})
	eform.view();
	
	var isSaved = true;
	
	// 저장 후 iframe 밖으로 데이터 전달 fn.
	var saveBtnClickEvent = function (){
		var resultVal = {};
		isSaved = false;

		$('#loading').show();

		//조건에 따라서 저장
		//등록,수정,서명(컨설턴트) -> 조회,서명(고객) -> 출력
		$.ajax({
			type 	: "POST",
			url 	: "exportForPDF_hc.jsp", //같은경로에 위치
			data 	: {"report_key":eform.getReportKey(), "fileNm" : fileNm},
			async 	: false,
			cache 	: false,
			success : function(result){
				resultVal.pdf = JSON.parse(result.trim());
				isSaved = true;
				window.parent.postMessage({resultJson : resultVal}, returnUrl);
			}
		});
	}

	// 저장버튼 event set.
	var test = 0;
	eform.setEndSaveButtonEvent(function() {

		// 무한루프 방지.
		if(test > 0){
			return false
		}
		
		if(confirm('저장하시겠습니까?')) {
			//로컬저장 (pdf다운로드)
			eform.saveFileDownLoad(fileNm, 3, 1);
			test++;
		}

	});
	
	// iframe 밖에서 전달.
	window.addEventListener('message', function(event){

		// code 1 : 메일전송.
		if (event.data.code == 1) {
			if(confirm('메일 전송하시겠습니까?')) {
				saveBtnClickEvent();
			}
		}

	});
});

</script>
</head>
<body>
	<div id='targetDiv1' style='position:absolute;top:5px;left:5px;right:5px;bottom:5px;'></div>
</body>
</html>