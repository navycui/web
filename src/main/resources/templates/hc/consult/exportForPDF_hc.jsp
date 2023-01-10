<%@page import="com.clipsoft.clipreport.server.service.ExportInfo"%>
<%@page import="com.clipsoft.clipreport.export.option.PDFOption"%>
<%@page import="com.clipsoft.clipreport.server.service.ClipReportExport"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileOutputStream"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@include file="../../Property.jsp"%> --%>
<%@include file="Property.jsp"%>
<%
// out.clear(); // where out is a JspWriter
// out = pageContext.pushBody();
//리포트 키를 받아서 처리 합니다. (report_key 파라미터 이름은 변경하여도 상관 없습니다)
String report_key = request.getParameter("report_key");
String fileNm	  = request.getParameter("fileNm");

System.out.println("------------------------------------------------------------");
System.out.println(fileNm);
System.out.println("------------------------------------------------------------");

String fileLoc	  = "/ecm/TMP/";
if(null != report_key){
	//서버에 파일로 저장 할 때
	
	System.out.println("------------------------------------------------------------");
	System.out.println(fileLoc + fileNm + ".pdf");
	System.out.println("------------------------------------------------------------");
	
	
	File localFileSave = new File(fileLoc + fileNm + ".pdf");
	OutputStream fileStream = new FileOutputStream(localFileSave);
	
	//클라이언트로 파일을 내릴 때
	//String fileName = "report.pdf";
	//response.setContentType("application/octet-stream; charset=UTF-8");
	//response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");
	//OutputStream fileStream = response.getOutputStream();
	
	//클라이언트 브라우져에서 바로 보는 방법(헤더 변경)
	//response.setContentType("application/pdf");
	//OutputStream fileStream = response.getOutputStream();
	
	PDFOption option = new PDFOption();
	/*
	option = new PDFOption();
	option.setUserPassword("사용자(읽기) 암호");
	option.setOwnerPassword("저자(쓰기) 암호");
	option.setTextToImage(true); // 글자를 이미지로 처리 - unicode 처리시 사용
	option.setImportOriginImage(true); // 원본 이미지 삽입
	option.setNotAllowCopy(true); //text복사 방지 (1.0.0.84 version 부터 적용)
	option.setEditable(false); // 편집 허용 여부 (1.0.0.173 version 부터 적용)
	*/
	
	//전자서식에서 서버에 저장되어 있다면 형광펜을 export 할 때 추가할지 여부
	//option.setDoodlePen(true);
	
	
	ExportInfo exportInfo = ClipReportExport.createExportForPartPDF(request, report_key, fileStream, option, propertyPath);
	//1페이지 부터 2페이지까지 pdf 생성 합니다. 
	//만약 5페이지의 리포트가 있을 때 2부터 6까지 설정한다면 2페이지부터 5페이지까지 pdf가 생성됩니다.
	//int startNum = 1;
	//int endNum = 2; 
	//ExportInfo exportInfo = ClipReportExport.createExportForPartPDF(request, report_key, fileStream, option, startNum, endNum, propertyPath);
	
	//지정한 페이지를 배열에 설정하면 배열 순으로 pdf가 생성됩니다.
	//지정한 페이지가 잘못되었을 경우 pdf에 제외됩니다. (5페이지 리포트이지만 6페이지를 설정하였을 경우)
	//아래의 sample은 3페이지의 리포트를 페이지 역순으로 pdf 저장합니다.    
	//int[] pageNumList = new int[3];
    //pageNumList[0] = 3;   
    //pageNumList[1] = 2;
    //pageNumList[2] = 1;
    //ExportInfo exportInfo = ClipReportExport.createExportForPartPDF(request, report_key, fileStream, option, pageNumList, propertyPath);
	
	int errorCode = exportInfo.getErrorCode();
	//errorCode == 0 정상
	//errorCode == 1 세션안에 리포트정보가 없을 때 오류 
	//errorCode == 2 리포트 서버가 설치가 안되어 있을 때 오류 
	//errorCode == 3 결과물(document) 파일을 찾지 못할 때 발생하는 오류
	//errorCode == 4 PDF 파일 생성 오류
	//errorCode == 5 리포트의 페이지 0 일 경우 오류
	
    /*
	response.setContentType("application/json");
    JSONObject obj = new JSONObject();
    obj.put("errorCode",errorCode);
    response.getWriter().write(obj.toString());
    */
    response.getWriter().write("{ \"errorCode\" : \"" + String.valueOf(errorCode) + "\",");
    response.getWriter().write( "\"originalFilePath\" : \"" + fileNm + "_TSA.pdf\",");
    response.getWriter().write( "\"counterpartFilePath\" : \"" + fileNm + ".pdf" + "\",");
    response.getWriter().write("\"fileLoc\" : \"" + fileLoc +"\"}");
    response.getWriter().flush();
    
}
%>