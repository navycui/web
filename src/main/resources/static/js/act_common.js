/**
 * axios
 * @param method	=> 'get', 'post', 'put', 'delete', 'multi'
 * @param url		=> '/api/~/~'
 * @param param 	=> json or array or formData
 * @param callback 	=> function
 * @param etc 		=> 미사용
 * ex) axiosCall('get', '/api/getList', {key:value}, function() {});
 */
function axiosCall(method, url, param, callback, etc) {

	// params: method => get, delete
	var params = method == 'get' || method == 'delete' ? param : {};
	// data : method => post, put, multi
	var data = method == 'post' || method == 'put' ? JSON.stringify(param) : method == 'multi' ? param : {};
	
	var headers = {};
	if(method == 'post' || method == 'put') {
		headers = {			
			'Content-Type': 'application/json',
			'x-requested-with' : 'AJAX'
		};
		
	// headers : method => multi
	} else if(method == 'multi') {
		method = 'post';
		headers = {
			"Content-Type": "multipart/form-data",
			'x-requested-with' : 'AJAX'
		}
	} else {
		headers = {
			'x-requested-with' : 'AJAX'
		}
	}

	// loader open
	if (!etc) {
		showLoader();
	}

	// axios로 api 호출
	axios({
		method: method,
		url: url,
		headers: headers,
		params: params,
		data: data,
	}).then(callback).catch(function(error){
		if(error){
			if(error.response.data.message === 'ERR_AJAX'){
				location.href = "/user/login";
				return;
			}else{
				cmmAlert(error.response.data.message);
			}			
		}
	}).finally(function(){
		// loader close
		if (!etc) {
			closeLoader();
		}
	})
}

/**
 * axios
 * @param filename	=> 'get'
 * ex) axiosCallDownLoad(filename);
 */
function axiosCallDownLoad(filename,viewid) {
	if (!!!filename) {
		cmmAlert("파일명 없습니다.")
	}
	// 로딩 시작
	showLoader();
	axios({
		url: '/api/filedownload/' + (!!viewid ? viewid : 'chat') + '/' + filename, //your url
		method: 'GET',
		responseType: 'blob', // important
	}).then((res) => {
		// create file link in browser's memory "Content-Disposition"
		const href = URL.createObjectURL(res.data);
		const filename = res.headers["content-disposition"].split("=")[1]
		// create "a" HTML element with href to file & click
		const link = document.createElement('a');
		link.href = href;
		var ajaxName = decodeURIComponent(filename);
		link.setAttribute('download',decodeURIComponent(ajaxName.replace("/\+/g", " ") )); //or any other extension
		document.body.appendChild(link);
		link.click();
		// clean up "a" element & remove ObjectURL
		document.body.removeChild(link);
		URL.revokeObjectURL(href);
	}).catch((e)=>{
			cmmAlert(e.response.data.message)
	}).finally(function(){
			// 로딩 종료
			closeLoader()
	});
}

function getAttachId(){
	var ret
	syncCall('/view/common/attach/getAttachId', null, function(response){
		ret = response.data.atchId
	})
	return ret
}

/*
 * attachInfo1: 기존파일(수정시)
 * attachInfo2: 신규파일
 * atchId: 첨부파일 배번
 * commCd: 공통코드(100001:게시판, 100002:제언, 100003:메뉴얼.....)
 * type: 타입(게시판(100001:법령정보, 100002:식품안전뉴스....), 개선진행(impv:외식개선, mfcimpv:제조사업장개선.....))
 * */
function attachments(attachInfo1, attachInfo2, atchId, commCd, type){
	var _this = this;
	if(attachInfo1 != null && attachInfo1.length > 0){
		syncCall('/view/common/attach/deleteCaseAttach', JSON.stringify(attachInfo1), function(response1){
			
		})
	}
	
	if(attachInfo2 != null && attachInfo2.length > 0){
		var formData = new FormData();
		for(var i=0; i<attachInfo2.length; i++){
			formData.append("files", attachInfo2[i])
		}
		syncCallAttach('/view/common/attach/addAttach?atchId='+atchId+'&atchCd='+commCd+'&type='+type, formData, function(response2){
		
		})
	}
}

function profileUpload(attachInfo1, attachInfo2, atchId, commCd, type){
	var _this = this;
//	if(attachInfo1 != null && attachInfo1.length > 0){
	if(attachInfo1 != null && attachInfo1.atchId != null){
		syncCall('/view/common/attach/deleteAttach', JSON.stringify(attachInfo1), function(response1){
			
		})
	}
	
	if(attachInfo2 != null && attachInfo2.length > 0){
		var formData = new FormData();
		for(var i=0; i<attachInfo2.length; i++){
			formData.append("files", attachInfo2[i])
		}
		syncCallAttach('/view/common/attach/addAttach?atchId='+atchId+'&atchCd='+commCd+(type!=null?'&type='+type:''), formData, function(response2){
		
		})
	}
}

function download(atchUrl, atchOrgNm){
	window.location = '/view/common/attach/download?atchUrl=' + atchUrl + '&atchOrgNm=' + encodeURIComponent(atchOrgNm)
}

function SHA256(s){
    
    var chrsz   = 8;
    var hexcase = 0;
  
    function safe_add (x, y) {
        var lsw = (x & 0xFFFF) + (y & 0xFFFF);
        var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
        return (msw << 16) | (lsw & 0xFFFF);
    }
  
    function S (X, n) { return ( X >>> n ) | (X << (32 - n)); }
    function R (X, n) { return ( X >>> n ); }
    function Ch(x, y, z) { return ((x & y) ^ ((~x) & z)); }
    function Maj(x, y, z) { return ((x & y) ^ (x & z) ^ (y & z)); }
    function Sigma0256(x) { return (S(x, 2) ^ S(x, 13) ^ S(x, 22)); }
    function Sigma1256(x) { return (S(x, 6) ^ S(x, 11) ^ S(x, 25)); }
    function Gamma0256(x) { return (S(x, 7) ^ S(x, 18) ^ R(x, 3)); }
    function Gamma1256(x) { return (S(x, 17) ^ S(x, 19) ^ R(x, 10)); }
  
    function core_sha256 (m, l) {
         
        var K = new Array(0x428A2F98, 0x71374491, 0xB5C0FBCF, 0xE9B5DBA5, 0x3956C25B, 0x59F111F1,
            0x923F82A4, 0xAB1C5ED5, 0xD807AA98, 0x12835B01, 0x243185BE, 0x550C7DC3,
            0x72BE5D74, 0x80DEB1FE, 0x9BDC06A7, 0xC19BF174, 0xE49B69C1, 0xEFBE4786,
            0xFC19DC6, 0x240CA1CC, 0x2DE92C6F, 0x4A7484AA, 0x5CB0A9DC, 0x76F988DA,
            0x983E5152, 0xA831C66D, 0xB00327C8, 0xBF597FC7, 0xC6E00BF3, 0xD5A79147,
            0x6CA6351, 0x14292967, 0x27B70A85, 0x2E1B2138, 0x4D2C6DFC, 0x53380D13,
            0x650A7354, 0x766A0ABB, 0x81C2C92E, 0x92722C85, 0xA2BFE8A1, 0xA81A664B,
            0xC24B8B70, 0xC76C51A3, 0xD192E819, 0xD6990624, 0xF40E3585, 0x106AA070,
            0x19A4C116, 0x1E376C08, 0x2748774C, 0x34B0BCB5, 0x391C0CB3, 0x4ED8AA4A,
            0x5B9CCA4F, 0x682E6FF3, 0x748F82EE, 0x78A5636F, 0x84C87814, 0x8CC70208,
            0x90BEFFFA, 0xA4506CEB, 0xBEF9A3F7, 0xC67178F2);

        var HASH = new Array(0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A, 0x510E527F, 0x9B05688C, 0x1F83D9AB, 0x5BE0CD19);

        var W = new Array(64);
        var a, b, c, d, e, f, g, h, i, j;
        var T1, T2;
  
        m[l >> 5] |= 0x80 << (24 - l % 32);
        m[((l + 64 >> 9) << 4) + 15] = l;
  
        for ( var i = 0; i<m.length; i+=16 ) {
            a = HASH[0];
            b = HASH[1];
            c = HASH[2];
            d = HASH[3];
            e = HASH[4];
            f = HASH[5];
            g = HASH[6];
            h = HASH[7];
  
            for ( var j = 0; j<64; j++) {
                if (j < 16) W[j] = m[j + i];
                else W[j] = safe_add(safe_add(safe_add(Gamma1256(W[j - 2]), W[j - 7]), Gamma0256(W[j - 15])), W[j - 16]);
  
                T1 = safe_add(safe_add(safe_add(safe_add(h, Sigma1256(e)), Ch(e, f, g)), K[j]), W[j]);
                T2 = safe_add(Sigma0256(a), Maj(a, b, c));
  
                h = g;
                g = f;
                f = e;
                e = safe_add(d, T1);
                d = c;
                c = b;
                b = a;
                a = safe_add(T1, T2);
            }
  
            HASH[0] = safe_add(a, HASH[0]);
            HASH[1] = safe_add(b, HASH[1]);
            HASH[2] = safe_add(c, HASH[2]);
            HASH[3] = safe_add(d, HASH[3]);
            HASH[4] = safe_add(e, HASH[4]);
            HASH[5] = safe_add(f, HASH[5]);
            HASH[6] = safe_add(g, HASH[6]);
            HASH[7] = safe_add(h, HASH[7]);
        }
        return HASH;
    }
  
    function str2binb (str) {
        var bin = Array();
        var mask = (1 << chrsz) - 1;
        for(var i = 0; i < str.length * chrsz; i += chrsz) {
            bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (24 - i%32);
        }
        return bin;
    }
  
    function Utf8Encode(string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
  
        for (var n = 0; n < string.length; n++) {
  
            var c = string.charCodeAt(n);
  
            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
  
        }
  
        return utftext;
    }
  
    function binb2hex (binarray) {
        var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
        var str = "";
        for(var i = 0; i < binarray.length * 4; i++) {
            str += hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8+4)) & 0xF) +
            hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8  )) & 0xF);
        }
        return str;
    }
  
    s = Utf8Encode(s);
    return binb2hex(core_sha256(str2binb(s), s.length * chrsz));
}

/*function pwdCheck(string) {
	var pwdExPattern = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$/;
	
	if(pwdExPattern.test(string)) {
		return true;
	}else {
//		alert('비밀번호는 영문자 대/소, 숫자, 특수문자의 조합이여야 합니다.');
		return false;
	}
}*/

function pwdCheck(string){
	var num = string.search(/[0-9]/g);
	var eng = string.search(/[a-z]/ig)
	var spe = string.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

	if(string.length < 8 || (num<0 && eng<0) || (eng<0 && spe<0) || (spe<0 && num<0)){
		return false;
	}else{
		return true;
	}

}


function emailCheck(string){
	
var emailExPattern = /^[0-9a-zA-Z_.]([-]?[0-9a-zA-Z_.])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	
	if(emailExPattern.test(string)) {
		return true;
	}else {
//		alert('e-mail 형식이 아닙니다.');
		return false;
	}
}

function cpCheck(string){
	var regExp = /^(?:(010-?\d{4})|(01[1|6|7|8|9]-?\d{3,4}))-?\d{4}$/;
	if(regExp.test(string)) {
		return true;
	}else {
//		alert('e-mail 형식이 아닙니다.');
		return false;
	}
}

function lossVal(val) {
	var result
	
	if(val <= 2000) {
		result = 5;
	}else if (val > 2000 && val <= 3000) {
		result = 8;
	}else if (val > 3000 && val <= 5000) {
		result = 10;
	}else if (val > 5000 && val <= 10000) {
		result  = 13;
	}else if (val > 10000 && val <= 15000) {
		result  = 16;
	}else if (val > 15000 && val <= 21000) {
		result  = 23;
	}else if (val > 21000 && val <= 27000) {
		result = 31;
	}else if (val > 27000 && val <= 33000) {
		result = 39;
	}else if (val > 33000 && val <= 40000) {
		result = 47;
	}else if (val > 40000 && val <= 47000) {
		result = 56;
	}else if (val > 47000 && val <= 55000) {
		result = 66;
	}else if (val > 55000 && val <= 65000) {
		result = 78;
	}else if (val > 65000 && val <= 75000) {
		result = 88;
	}else if (val > 75000 && val <= 85000) {
		result = 94;
	}else if (val > 85000 && val <= 100000) {
		result = 100;
	}else if (val > 100000 && val <= 120000) {
		result = 106;
	}else if (val > 120000 && val <= 150000) {
		result = 112;
	}else if (val > 150000 && val <= 200000) {
		result = 118;
	}else if (val > 200000 && val <= 250000) {
		result = 124;
	}else if (val > 250000 && val <= 300000) {
		result = 130;
	}else if (val > 300000 && val <= 400000) {
		result = 136;
	}else if (val > 400000 && val <= 500000) {
		result = 165;
	}else if (val > 500000 && val <= 650000) {
		result = 211;
	}else if (val > 650000 && val <= 800000) {
		result = 266;
	}else if (val > 800000 && val <= 1000000) {
		result = 330;
	}else if (val > 1000000) {
		result = 367;
	}
	
	return result;
}
