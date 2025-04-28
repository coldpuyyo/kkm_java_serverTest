'use strict';

const regNoticeBoard = document.regNoticeBoard,
    regBtn = regNoticeBoard.regBtn,
    cancel = regNoticeBoard.cancel;
   
const param = document.querySelector('.hidden'),
       pageNum = param.dataset.pagenum,
       searchField = param.dataset.searchfield,
       searchWord = param.dataset.searchword,
      limitMsg = document.querySelector('.limitMsg');

const queryString = `?pageNum=${pageNum}&searchField=${searchField}&searchWord=${searchWord}`;
   
async function regNotice() {
   const maxFileSize = 1024 * 1024 * 5,
   
      /* file 형식인 input 요소가 다중 요소로 구성되어 있으므로 files 속성은 파일 요소들을 배열 형태로 반환. */
      uploadFiles = regNoticeBoard.files; 
      
   const errMsg = "파일은 최대 5M 까지만 업로드 가능합니다!!";
      
   if(!regNoticeBoard.title.value){
      alert('제목은 반드시 입력되어야 합니다.!!');
      
      return;
   }   
   
   if(!regNoticeBoard.content.value){
      alert('내용은 반드시 입력되어야 합니다.!!');
      
      return;
   }   
   
   /*
      uploadFiles 가 배열이므로 루프를 통해 개별 파일 input 요소들을 얻고 다시 filse 속성을
      인덱싱을 통해야만 실제 첨부 파일의 참조를 얻을 수 있음에 주의.
   */
   for(const uploadFile of uploadFiles) {
      if( uploadFile.files[0] && uploadFile.files[0].size > maxFileSize) {
         limitMsg.textContent = errMsg;
         
         return;
      }
   }
   
    try {
        await axios.post('reg' + queryString, new FormData(regNoticeBoard));
      
        location.href = '/admin/notice/list' + queryString;
    } catch (err) {
      limitMsg.textContent = errMsg;
    }
}

function cancelReg() {
   location.href = '/admin/notice/list' + queryString;
}

regBtn.addEventListener('click', regNotice);
cancel.addEventListener('click', cancelReg);