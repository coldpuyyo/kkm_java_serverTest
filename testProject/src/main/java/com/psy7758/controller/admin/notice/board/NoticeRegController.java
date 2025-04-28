package com.psy7758.controller.admin.notice.board;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.psy7758.dto.Notice;
import com.psy7758.service.imp.AdminService;

@MultipartConfig(
      fileSizeThreshold = 1024 * 1024,      //  1 Mbyte
      maxFileSize = 1024 * 1024 * 5,         //  5 Mbyte
      maxRequestSize = 1024 * 1024 * 5 * 3   // 15 Mbyte - 3 개의 업로드 파일 가정.
)
@WebServlet("/admin/notice/board/reg")
public class NoticeRegController extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private final AdminService service = new AdminService();
   
   @Override
   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String pageNum = request.getParameter("pageNum");
      String searchField = request.getParameter("searchField");
      String searchWord = request.getParameter("searchWord");
      
      request.setAttribute("pageNum", pageNum);
      request.setAttribute("searchField", searchField);
      request.setAttribute("searchWord", searchWord);
      
      super.service(request, response);
   }
   
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.getRequestDispatcher("/WEB-INF/view/admin/notice/board/reg.jsp").forward(request, response);
   }
   
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String title = request.getParameter("title");
      String content = request.getParameter("content");
      boolean pub = "true".equals(request.getParameter("pub"));
      String realPath = request.getServletContext().getRealPath("/upload");
      
      File path = new File(realPath);
      
      if(!path.exists()) {
         path.mkdir();
      }
      
      Collection<Part> fileParts = request.getParts();
      
      /*
       * DB 저장을 위한 fileParts 에 저장되어있는 파일들의 파일명들을 조립하기 위해 StringBuilder 활용.
       * 
       * ※ 일반적으로 파일의 메타데이터(파일명, 경로, 파일 크기 등)는 데이터베이스에 저장하고, 실제 파일은
       *   파일 시스템에 저장하는 방법을 활용.
       *   이는 파일 관리의 용이성과 대용량 파일을 DB 에서 직접 입출력함에 있어 성능 저하의 부담을 완화하기 위함.
       */
      StringBuilder joinFileNames = new StringBuilder();
      
      for(Part filePart : fileParts) {
         
         /*
          *       < getSize >
          * 
          * - 파일 업로드 시 해당 파일의 크기를 바이트 단위로 반환함으로써, 이를 통해 파일이 실제 정상적으로
          *   업로드되었는지 여부를 반환된 파일 크기를 측정하여 확인 가능.
          *   특정 파일이 업로드되지 않은 경우에도 filePart.getName() 메서드는 해당 파일의 식별자("file")를
          *   반환하므로 getSubmittedFileName() 메서드로 추출된 파일명의 존재 여부를 이후 다식 측정해야 하지만,
          *   getSize() 메서드를 통해 굳이 별도의 파일명 추출을 통한 측정을 할 필요가 없으므로 선제적인 유효성 검사가 가능.
          */
         if( !filePart.getName().equals("files") || filePart.getSize() == 0 ) continue;
         
         String fileName = filePart.getSubmittedFileName();
         
         String saveFilePath = realPath + File.separator + fileName;
         
         try (InputStream fileInputStream = filePart.getInputStream()) {
            Files.copy(fileInputStream, Paths.get(saveFilePath), StandardCopyOption.REPLACE_EXISTING);
         }
         
         joinFileNames.append(fileName).append(",");
      }
      
      /*
       * 모든 파일이 업로드되지 않은 경우 StringBuilder 가 비어있게 되고, 이로인해 joinFileNames.length() - 1 의
       * 결과가 -1 이 나와 deleteCharAt 에 대입되므로 이에 대한 방어코드 설정.
       */
      if( !joinFileNames.isEmpty() ) {
         joinFileNames.deleteCharAt(joinFileNames.length() - 1);      /* 마지막 파일명의 쉼표 삭제. */
      }
      
      /* ======================================================================================================= */
      
      Notice notice = new Notice();
      notice.setTitle(title);
      notice.setContent(content);
      notice.setPub(pub);
      notice.setWriter_id("psy");
      
      notice.setFiles(joinFileNames.toString());
      
      service.regNotice(notice);
   }
}