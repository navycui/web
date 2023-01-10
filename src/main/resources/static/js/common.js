/* 헤더 레이아웃 */
$(document).ready(function(){
	view();
    
    // 아코디언
    accordion();
    $('.accordion_wrap .list').eq(0).addClass('active').find('.conts').css('display','block'); //list 첫번째 열기
    
    //pop
    pop_show();
    pop_hide();

	//tree
	tree_menu_move();

	//code 유형 트리
	$('.btn-code-save').hide();

	var $btn_code_edit = $('.btn-code-edit');
	var $btn_code_save = $('.btn-code-save');

	$btn_code_edit.on("click", function(){
		$(this).hide().next('.btn-code-save').show();
		$(this).parent().closest('li').toggleClass('on');
		$(this).parent().closest('li').find('input').attr("disabled", false);

		if($('.code-list li').hasClass('on')){
			$btn_code_save.on("click", function(){
				
				$(this).hide().prev('.btn-code-edit').show();
				$(this).parent().closest('li').removeClass('on');
				$(this).parent().closest('li').find('input').attr("disabled", true);
			})
		}
	})
});

// family box
var selectBoxToggle = function(val) {
	var delay = 200;
	var targetClass = (val == undefined && val != 'noti') ? '.list-select-box' : '.' + val;
	var targetBtn = (val == undefined && val != 'noti') ? targetClass + ' .btn-select' : targetClass + ' .name a';
	
	$(targetBtn).on('click', function(e) {
		e.preventDefault();
		$(targetClass).find('.list-select').stop().slideUp(delay, function() {
			$(this).parent().removeClass('on');
		});
		$(this).siblings().stop().slideToggle(delay);
	});

    // list select 메뉴들 클릭 시 select 닫기
    $(targetClass + ' .list-select span').on('click', function() {
        $(this).parents('.list-select').stop().slideUp(delay);
        $(this).parents(targetClass).removeClass('on');
    });

    // list select 바깥 클릭시 닫기
    $(document).on('click', function(e) {
        if($(targetClass).has(e.target).length === 0){
            $(targetClass).find('.list-select').stop().slideUp(delay);
            $(targetClass).removeClass('on');
        }
    });
}

$(document).ready(function(){
	//탭메뉴
	$('ul.tab-group li').click(function(){
        var tab_id = $(this).attr('data-tab');

        $('ul.tab-group li').removeClass('current');
        $('.tab-content').removeClass('current');

        $(this).addClass('current');
        $("#"+tab_id).addClass('current');
      })

	// 채팅창 댓글 등록 버튼 클릭 시 댓글창 열기
	$('.chat-list-item .btn-reply').on('click', function(e) {
		$('.reply-chat-form').show();
	});

	// 채팅창 댓글 열고/닫기
	$('.chat-list-item .btn-reply-show').on('click', function(e) {
		$(this).parents().find('li').children('.chat-reply-list').slideToggle(delay);
	});
	
});

function view(){
    $('.datepicker input[type="text"]').datepicker();
}

/* 캘린더 */
jQuery(document).ready(function($) {
	// 공통 모달창 셋팅
	$("#dialog").dialog({modal: true,autoOpen: false});
	function datepickerSet() {
		$.datepicker.regional['ko'] = {
			closeText: '닫기',
			prevText: '이전',
			nextText: '다음',
			currentText: '오늘',
			buttonText:'날짜선택',
			monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
			monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
			dayNames: ['일','월','화','수','목','금','토'],
			dayNamesShort: ['일','월','화','수','목','금','토'],
			dayNamesMin: ['일','월','화','수','목','금','토'],
			weekHeader: 'Wk',
			dateFormat: 'yy-mm-dd',
			firstDay: 0,
			isRTL: false,
			showMonthAfterYear: true,
			yearSuffix: ''};
		$.datepicker.setDefaults($.datepicker.regional['ko']);
	}

	$(document).ready(function () {	
		datepickerSet();
		
		$("input[class^='calendarDate']").datepicker({
			showOn: "both",
			buttonImage:'../../../static/images/common/calendar_day_btn.png',
			buttonImageOnly: true,
			buttonText:'날짜선택',
			changeMonth: true,
			dateFormat: 'yy-mm-dd',
			changeYear: true,
			yearRange: 'c-99:c+99',
			showButtonPanel: true
		});
		
	});		

	$("input[class^='calendarDate_start']").datepicker({
		showOn: "both",
		buttonImage:'../../../static/images/common/calendar_day_btn.png',
		buttonImageOnly: true,
		buttonText:'날짜선택',
		changeMonth: true,
		changeYear: true,
		yearRange: 'c-99:c+99',
		controlType: 'select',
		oneLine: true,
		dateFormat: 'yy-mm-dd',
		altFieldTimeOnly: false,
		showButtonPanel: true,
		onClose: function( selectedDate ) {
			$("input[class^='calendarDate_end']").datepicker( "option", "minDate", selectedDate );
		}
	});
	
	$("input[class^='calendarDate_end']").datepicker({
		showOn: "both",
		buttonImage:'../../../static/images/common/calendar_day_btn.png',
		buttonImageOnly: true,
		buttonText:'날짜선택',
		changeMonth: true,
		changeYear: true,
		yearRange: 'c-99:c+99',
		controlType: 'select',
		oneLine: true,
		dateFormat: 'yy-mm-dd',
		altFieldTimeOnly: false,
		showButtonPanel: true,
		onClose: function( selectedDate ) {
			$("input[class^='calendarDate_start']").datepicker( "option", "maxDate", selectedDate );
		}
	});
	
	try	{
		$("input[class^='calendarMonth']").MonthPicker({
			Button:'<img src="/images/common/calendar_btn.png" />'
		});
	} catch (exception) { }
	
	//tree menu click
	$('.tree-wrap ul li a').on("click", function(){
		$('.tree-wrap ul li').removeClass('on');
		$(this).parent('li').addClass('on');
	})
});

function accordion() {
	//아코디언
	$('.accordion_wrap').on('click', '.btn_accordion', function(e){
		var $this = $(this);
		var $content_accdr = $(this).parent().next('.conts');

		if($this.parent().parent().hasClass('active')){

			$content_accdr.slideUp(300);
			$('.accordion_wrap > .list').removeClass('active');

		}else{

			$('.accordion_wrap > .list > .conts').slideUp(300);
			$content_accdr.slideDown(300);
			
			$('.accordion_wrap > .list').removeClass('active');
			$this.parent().parent().addClass('active');

		}
	})
}

// popup
function pop_show(){
	var $popOpen = $('.js-pop-open');
    
    $popOpen.on('click', function(){
        $(".js-layer-pop").show();
    })
}

function pop_hide(){
    var $popClose = $('.js-pop-close');
    
    $popClose.on('click', function(){
        $(".js-layer-pop").hide();
    })
}

function tree_menu_move(){

	//tree menu move
	var $checked = $('.tree-pin input[type=checkbox]');
	var $btn_tree = $('.btn-tree-menu');

	$btn_tree.on("click", function(){
		$('.tree-wrap').toggleClass('active');

		if($('.pin-layout-wrap').hasClass('on')){
			$('.pin-layout-wrap').removeClass('on');
			$checked.prop('checked',false);
		}
	})

	$checked.on("click", function(){
		if($(this).is(':checked')){
			$('.pin-layout-wrap').addClass('on');
		}else{
			$('.pin-layout-wrap').removeClass('on');
		}
	})
}


// datePicker 생성
function drawDatePicker(self, parent) {
	//var self = this;
	$(self.$el).datepicker({
		dateFormat: "yy-mm-dd",
		onSelect: function(date){
			self.$emit(parent, date);
		},
		onClose: function( selectedDate ) {

			// 시작일(fromDate) datepicker가 닫힐때
			// 종료일(toDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
			if(dateValidationHyphen(selectedDate)) {

				if($(".hasDatepicker")[0].id == this.id) {
					var endDateInput = $("#"+ $(".hasDatepicker")[1].id);
					if(dateValidationHyphen(endDateInput.val())) { // 형식에 안 맞는 날짜 공백처리되는 현상 수정
						endDateInput.datepicker( "option", "minDate", selectedDate );
					}

				} else {
					var startDateInput = $("#"+ $(".hasDatepicker")[0].id);
					if(dateValidationHyphen(startDateInput.val())) { // 형식에 안 맞는 날짜 공백처리되는 현상 수정
						startDateInput.datepicker( "option", "maxDate", selectedDate );
					}
				}
			}
		},
//          ,minDate: new Date(),
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		showMonthAfterYear: true,
		yearSuffix: '',
		showOtherMonths: true,
		selectOtherMonths: true,
		closeText: '닫기',
		prevText: '이전',
		nextText: '다음',
		currentText: '이번달',
		isRTL: false,
		firstDay: 0,
		weekHeader: 'Wk',
		showOn: "both",
		buttonImageOnly:true,
		changeMonth: true,
		changeYear: true,
		yearRange: 'c-99:c+99',
		showButtonPanel: true,
		buttonImage:'/images/common/calendar_btn.png'
	});
}

// datePicker 생성
function drawDatePickerSingle(self, parent) {
	//var self = this;
	$(self.$el).datepicker({
		dateFormat: "yy-mm-dd",
		onSelect: function(date){
			self.$emit(parent, date);
		},
		onClose: function( selectedDate ) {    
		},
//          ,minDate: new Date(),
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		showMonthAfterYear: true,
		yearSuffix: '',
		showOtherMonths: true,
		selectOtherMonths: true,
		closeText: '닫기',
		prevText: '이전',
		nextText: '다음',
		currentText: '이번달',
		isRTL: false,
		firstDay: 0,
		weekHeader: 'Wk',
		showOn: "both",
		buttonImageOnly:true,
		changeMonth: true,
		changeYear: true,
		yearRange: 'c-99:c+99',
		showButtonPanel: true,
		buttonImage:'/images/common/calendar_btn.png'
	});
}

function drawDatePickerMaxToday(self, parent) {
	//var self = this;
	$(self.$el).datepicker({
		dateFormat: "yyyy-mm-dd",
		onSelect: function(date){
			self.date = date;
			self.$emit(parent, date);
		},
		onClose: function( selectedDate ) {    
		},
//          ,minDate: new Date(),
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		showMonthAfterYear: true,
		yearSuffix: '',
		showOtherMonths: true,
		selectOtherMonths: true,
		closeText: '닫기',
		prevText: '이전',
		nextText: '다음',
		currentText: '이번달',
		isRTL: false,
		firstDay: 0,		
		maxDate: 0,
		weekHeader: 'Wk',
		showOn: "both",
		buttonImageOnly:true,
		changeMonth: true,
		changeYear: true,
		yearRange: 'c-99:c+99',
		showButtonPanel: true,
		buttonImage:'/images/common/calendar_btn.png'
	});
}

// datePicker 제거
function destroyDatePicker(self) {
	$(self.$el).datepicker('hide').datepicker('destroy')
}

// 조회기간 구하기
// val => 당월:currentMonth, 당해:currentYear, 1개월:oneMonth, 3개월:threeMonth, 6개월:sixMonth, 1년:oneYear
function getPeriod(val) {
	var now = new Date();
	var startDt = '';
	var endDt = '';
	if(val == 'currentMonth') {
		startDt =new Date(now.getFullYear(), now.getMonth(), 1);
		endDt = new Date(now.getFullYear(), now.getMonth() + 1, 0);
		
	} else if(val == 'currentYear') {
		startDt = new Date(now.getFullYear(), 0, 1);
		endDt = new Date(now.getFullYear(), 12, 0);
		
	} else if(val == 'oneMonth') {
		startDt = new Date(now.getFullYear(), now.getMonth() - 1, now.getDate());
		endDt = new Date();
		
	} else if(val == 'threeMonth') {
		startDt = new Date(now.getFullYear(), now.getMonth() - 3, now.getDate());
		endDt = new Date();
		
	} else if(val == 'sixMonth') {
		startDt = new Date(now.getFullYear(), now.getMonth() - 6, now.getDate());
		endDt = new Date();
		
	} else if(val == 'oneYear') {
		startDt = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate());
		endDt = new Date();

	} else if(val == 'all') {
		startDt = '';
		endDt = '';

		// 전체 시 비활성화
		// $('.calendar-wrap').addClass("all");

	} else {
		startDt = new Date();
		endDt = new Date();
	}

	var periodDate = {};
	periodDate.startDt = dateFormatCalendar(startDt);
	periodDate.endDt = dateFormatCalendar(endDt);
	
	return periodDate
}

// dateFormat yyyy-mm-dd 형식
function dateFormatCalendar(date) {
	if(date) {
		return new Date(+date + 3240 * 10000).toISOString().split("T")[0];
	}
	return date
}


// 1. codeList : 바로 array 명을 넣어주어 bind
// param => sys_cd, cmm_cd, arr_nm : array
// result => code list array
function getCodeListForArrNm(codeParamList, self, callback) {

	let params = [];
	
	// 날짜 체크
	$.each(codeParamList, function(idx, obj) {

		var cmmCd = obj.cmm_cd;
		var startDate = obj.p_parm4;
		var endDate = obj.p_parm5;

		if(cmmCd == 'cst' || cmmCd == 'mng') {
			// 검색일자 체크
			if(startDate != undefined && !dateValidationHyphen(startDate)) {
				return true;
			}

			// 검색일자 체크
			if(endDate != undefined && !dateValidationHyphen(endDate)) {
				return true;
			}
		}

		params.push(obj);
	});

	if(params.length > 0) {
		axiosCall('post', '/api/commDtlCodeAllList', params, function(res) {
			
			var codeList = [];
	
			if(res.data.length == 0) {
				console.log("공통코드가 존재하지 않습니다.");
	
			} else {
				$.each(res.data, function(idx, obj) {
					var codeDtlList = [];
	
					if(obj.dtlCodeList) {
						codeDtlList = obj.dtlCodeList;
	
					} else if(obj.cstList) {
						codeDtlList = obj.cstList;
	
					} else if(obj.mngList) {
						codeDtlList = obj.mngList;
	
					} else if(obj.deptList) {
						codeDtlList = obj.deptList;
					}
	
					// 전체 or 선택
					var comboType = params[idx].comboType;
					if(comboType != undefined && comboType != '') {
						if(comboType == 'all') {
							codeDtlList.unshift({cmm_dtl_cd: '', cmm_dtl_nm: '전체'});
	
						} else if(comboType == 'select') {
							codeDtlList.unshift({cmm_dtl_cd: '', cmm_dtl_nm: '선택'});
						}
					}
	
					self[params[idx].arr_nm] = codeDtlList;
					codeList[obj.cmm_cd] = codeDtlList;
				});
			}
	
			// callback존재시 실행
			if(callback) {
				if(callback instanceof Function) {
					callback(codeList);
				} else {
					console.log("The callback is not a function.");
				}
			}
	
		}, 'exist');
	}
}

// 2. codeAdapterList : 바로 array 명을 넣어주어 bind
// param: sys_cd, cmm_cd, arr_nm : array
// return: code list를 jqxgrid에서 사용하는 adapter로 변경하여 만든 array
function getCodeAdapterListForArrNm(codeParamList, self, callback) {
    
	axiosCall('post', '/api/commDtlCodeAllList', codeParamList, function(res) {

		var codeAdapterList = [];

		if(res.data.length == 0){
			console.log("공통코드가 존재하지 않습니다.");

		}else{
			$.each(res.data, function(idx, obj) {
				var codeSource = {
					datatype: "array",
					datafields: [
						{ name: 'cmm_dtl_nm', type: 'string' },
						{ name: 'cmm_dtl_cd', type: 'string' }
					],
					localdata: obj.dtlCodeList
				};

				var codeAdapter = new $.jqx.dataAdapter(codeSource, {autoBind: true});
				self[codeParamList[idx].arr_nm] = codeAdapter;
				codeAdapterList[obj.cmm_cd] = codeAdapter;
			});

			// callback존재시 실행
			if(callback) {
				if(callback instanceof Function) {
					callback(codeAdapterList);
				} else {
					console.log("The callback is not a function.");
				}
			}
		}
	}, true);
}

// 공통팝업
function openCmmPopup(val, id, param) {
	
	let url = "";
	if(val == "addContactPopup") {
		url = '/user/addContactPopup';
	} else if(val == "customerSearchPopup") {
		url = '/commPopup/customerSearchPopup';
	} else if(val == "personSearchPopup") {
		url = '/commPopup/personSearchPopup';
	} else if(val == "hcComplete") {
		url = '/consulting/consult/hcComplete';
	} else if(val == "SalesActivityPop") {
		url = '/main/main_hc/getActivity';
	} else if(val == "SalesUnActivityPop") {
		url = '/main/main_hc/getUnActivity';	
	} else {
		url = val;
	}
	
	// 팝업불러오기
	let popupDiv = $("#" + id);
	axiosCall('get', url, param, function(res){
		if(res.data.length != 0){
			popupDiv.html(res.data);
			popupDiv.show();
			console.log("popup open");
		}else{
			console.log("데이터가 존재하지 않습니다.");
		}
	}, "");
}

// 팝업닫기
function closeCmmPopup(id) {
	if(id != '' || id == undefined) {
		$("#" + id).parent().html("");
	}
}

// 공통 alert
function cmmAlert(val) {
	// alert창 띄우기
	$("#alertPopup").show();
	$("#alertPopup cancel").hide(); // 취소버튼 비활성화
	$("#alertPopup .pop-header-title").html("알림");

	// 버튼 : 기존 함수 호출시 기존 css 관련 모든 버튼을 바꾸는 경우가 있어 ID 추가함
	$("#alertPopup .pop-bottom .pop-btn-box").html('<a href="#a" class="btn-pop-bottom js-pop-close" id="alertClose">확인</a>');

	// 내용
	val = (val == undefined ? '관리자에게 문의하세요.' : val)
	$("#alertPopup .cont").html(val);

	// 닫기, 확인 버튼
	$('#alertPopup .js-pop-close').click(function(e){
		var id = $(this).attr('id');
		$("#alertPopup").hide();
	});
}

// 공통 confirm
// opt: 로그인 페이지 서비스 구분선택 추가 최해군 과장
// 공통 confirm
function cmmConfirm(val, callback, viewType) {
	// alert창 띄우기
	$("#alertPopup").show();
	$("#alertPopup cancel").show(); // 취소버튼 활성화
	$("#alertPopup .pop-header-title").html("확인");

	if (!!viewType) {
		// 버튼
		$("#alertPopup .pop-bottom .pop-btn-box").html(
			'<a href="#a" class="btn-pop-bottom js-pop-close" id="comfirmCancel">일지확인</a>'
			+'<a href="#a" class="btn-pop-bottom js-pop-close" id="confirmClose">확인</a>');
	} else {
		// 버튼
		$("#alertPopup .pop-bottom .pop-btn-box").html(
			'<a href="#a" class="btn-pop-bottom js-pop-close" id="comfirmCancel">취소</a>'
			+'<a href="#a" class="btn-pop-bottom js-pop-close" id="confirmClose">확인</a>');
	}

	// 내용	
	val = (val == undefined ? '관리자에게 문의하세요.' : val)
	$("#alertPopup .cont").html(val);

	// 닫기, 취소, 확인
	$('#alertPopup .js-pop-close').click(function(e){
		var id = $(this).attr('id');

		$("#alertPopup").hide();

		// 확인 callback function
		if(id == 'confirmClose') {
			if(callback != 'undefined' && typeof callback == 'function'){
				callback('chat');
			}
		};
		// 취소 callback function
		if(id == 'comfirmCancel' && !!viewType) {
			if(callback != 'undefined' && typeof callback == 'function'){
				callback();
			}
		};
	});
}

// 공통 tooltip
var cmmTooltip = function () {
	setTimeout(function () {
		$("div i.ico-tooltip").mouseover(function(){
			var tooltipContent = "<div>"+ $(this).next('.tooltip-txt').html() +"</div>";         // 툴팁내용
			
			// 툴팁 적용
			$(this).jqxTooltip({ position: 'right', content: tooltipContent});
			$(this).jqxTooltip('open');
		});

		$(".ico-tooltip").mouseout(function(){
			$(this).jqxTooltip('destroy');
		});
		
	}, 200);
}

// show loading
function showLoader() {
	$("#cmmLoading").show();
}

// hide loading
function closeLoader() {
	$("#cmmLoading").hide();
}


// amount format: 숫자를 제외한 모든 문자를 ''로 변경하여 숫자 + 콤마 생성(금액).
function addCommaToAmount(val) {
	 return Number(val.toString().replace(/[^0-9]/g,'')).toLocaleString('ko-KR');
}

// date format: 숫자를 제외한 모든 문자를 ''로 변경하여 하이픈 추가 (yyyy-mm-dd형식)
function addHyphenToDate(val) {
	return val.toString().replace(/[^0-9]/g,'').replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
}

// phone format: 숫자를 제외한 모든 문자를 ''로 변경하여 하이픈 추가 (000-0000-0000형식)
function addHyphenToPhone(val) {
	val = val.replaceAll(/[^0-9]/g, "");
	let length = val.length;
    let result = "";

    if(length === 8 || (!val.startsWith("02") && length === 9)) {
        result = val.replace(/(\d{4})(\d{4})/, '$1-$2');
    } else if(val.startsWith("02") && (length === 9 || length === 10)) {
        result = val.replace(/(\d{2})(\d{3,4})(\d{4})/, '$1-$2-$3');
    } else if(!val.startsWith("02") && (length === 10 || length === 11)) {
        result = val.replace(/(\d{3})(\d{3,4})(\d{4})/, '$1-$2-$3');
    } else {
		result = val;
	}

    return result;
}

// date validation check
function dateValidation(val) {
	if(!val.replaceAll('-', '').length == 8) {
		return false;
	}

	return Date.parse(val);
}

// date validation yyyy-mm-dd형식 체크
function dateValidationHyphen(val) {

	if(!dateValidation(val)) {
		return false
	}

	var datatimeRegexp = /([0-9]{4}-[0-9]{2}-[0-9]{2})/;
	return datatimeRegexp.test(val);
}

// date validation yyyy-mm-dd형식 (alert포함.)
function dateValidationAndAlert(val) {

	if(!dateValidationHyphen(val)) {
		cmmAlert("유효한 날짜형식이 아닙니다.");
		return false;
	}

	return true;
}

// phone validation 000-000-0000 or 000-0000-0000형식
function phoneValidation(val) {
	var phoneRegxp = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
	return phoneRegxp.test(val);
}


