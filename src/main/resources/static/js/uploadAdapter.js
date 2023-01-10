export default class UploadAdapter {
    constructor(loader) {
        this.loader = loader;
    }

    upload() {
        return this.loader.file.then( file => new Promise(((resolve, reject) => {
            var formData = new FormData();
            formData.append('files', file);

            // 공지사항 저장
            axiosCall('multi', '/api/notice/setNotiImgUpload', formData, function(res) {
                resolve({
                    default: res.data[0].directory + res.data[0].serFileName,
                    alt: res.data[0].fileName,
                    // default: res.data[0].directory + '/' + res.data[0].filename
                    // default: 'https://localhost:8080/image/' + res.data[0].filename
                })
            });

            
            // this._initRequest();
            // this._initListeners( resolve, reject, file );
            // this._sendRequest( file );
        })))
    }

    // _initRequest() {
    //     const xhr = this.xhr = new XMLHttpRequest();
    //     xhr.open('POST', '/api/notice/setNotiImgUpload', true);
    //     xhr.responseType = 'json';
    // }

    // _initListeners(resolve, reject, file) {
    //     const xhr = this.xhr;
    //     const loader = this.loader;
    //     const genericErrorText = '파일을 업로드 할 수 없습니다.'

    //     xhr.addEventListener('error', () => {reject(genericErrorText)})
    //     xhr.addEventListener('abort', () => reject())
    //     xhr.addEventListener('load', () => {
    //         debugger
    //         const response = xhr.response
    //         if(!response || response.error) {
    //             return reject( response && response.error ? response.error.message : genericErrorText );
    //         }

    //         resolve({
    //             default: response.url //업로드된 파일 주소
    //         })
    //     })
    // }

    // _sendRequest(file) {
    //     const data = new FormData()
    //     data.append('files',file)
    //     this.xhr.send(data)
    // }
}