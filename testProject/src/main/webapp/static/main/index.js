'use strict';

const clientForm = document.clientForm,
    submitBtn = clientForm.submitBtn;

async function addClient() {
    try {
        await axios.post('/postJson', Object.fromEntries(new FormData(clientForm)));
      
      /*
         URL 에 요청 페이지에 대한 경로와 확장자가 그대로 표시되는 문제점을
         해결하기 위해 PostJson 서블릿으로 Get 요청. 
      */
        location.href = "/postJson";
    } catch (err) {
        console.log('데이터를 전송중 오류 발생');
        console.log(err.message);
    }
}

submitBtn.addEventListener('click', addClient);