package it.unisa.modelcargallery.control;

import java.io.*;
import java.sql.SQLException;
import java.util.UUID;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.ProductDao;
import it.unisa.modelcargallery.dao.ProductDaoImpl;
import it.unisa.modelcargallery.model.ProductBean;

@WebServlet("/image")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, // max 5 MB per file
	maxRequestSize = 10 * 1024 * 1024, // max 10 MB per request
	fileSizeThreshold = 2* 1024 * 1024) // 2 MB after which the file will be temporarily stored on disk
public class ImageControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String UPLOAD_DIR = "img";

	private ProductDao productDao;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		if (ds == null) {
			throw new ServletException("DataSource non disponibile nel contesto applicativo.");
		}
		productDao = new ProductDaoImpl(ds);
		// Crea la cartella uploads
		String uploadPath = getServletContext().getRealPath(File.separator + UPLOAD_DIR);
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists())
			uploadDir.mkdir();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String action = request.getParameter("action");

	    if ("show".equalsIgnoreCase(action)) {

	        int productCode = Integer.parseInt(request.getParameter("code"));

	        try {
	            ProductBean bean = productDao.doRetrieveByKey(productCode);

	            String mimeType = bean.getMimeType();
	            String nomeImmagine = bean.getImmagine_copertina();

	            if (nomeImmagine == null || nomeImmagine.isEmpty()) {
	                response.sendError(HttpServletResponse.SC_NOT_FOUND);
	                return;
	            }

	            String imagePath = getServletContext().getRealPath(
	                    File.separator + UPLOAD_DIR + File.separator + nomeImmagine
	            );

	            response.setContentType(mimeType);

	            try (InputStream is = new FileInputStream(imagePath);
	                 OutputStream os = response.getOutputStream()) {

	                is.transferTo(os);
	            }

	        } catch (SQLException e) {
	            throw new ServletException("Errore nel recupero immagine", e);
	        }
	    }
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if ("upload".equalsIgnoreCase(action)) {
			int productCode = Integer.parseInt(request.getParameter("productCode"));
			Part part = request.getPart("image");
			if (part != null) {
				String originalFileName = part.getSubmittedFileName();
				if (originalFileName != null && !originalFileName.isEmpty() && part.getSize() > 0) {
					String mimeType = part.getContentType();
					String uniqueFileName = buildUniqueFileName(part);
					String uploadImmagine_copertina = getServletContext().getRealPath(File.separator + UPLOAD_DIR + File.separator + uniqueFileName);
					ProductBean bean = new ProductBean();
					bean.setCode(productCode);
					bean.setMimeType(mimeType);
					bean.setImmagine_copertina(uniqueFileName);
					try {
						part.write(uploadImmagine_copertina);
						productDao.doUpdateImage(bean);
						System.out.println(uploadImmagine_copertina);
					} catch (SQLException e) {
						System.err.println("Error:" + e.getMessage());
					}
				}
			}
		}
		response.sendRedirect(request.getContextPath() + "/admin/welcome" );
	}

	private String buildUniqueFileName(Part part) {
		String originalName = part.getSubmittedFileName();
		String extension;
		if (originalName.contains(".")) {
		    extension = originalName.substring(originalName.lastIndexOf("."));
		} else {
		    extension = "";
		}
		return UUID.randomUUID() + extension;
	}

	
}
