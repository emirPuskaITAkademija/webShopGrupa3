package shop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shop.model.Product;
//ServletContext application

public class WebShopServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //SKLADIŠTE PODATAKA -> podataka koje svi dijele
        ServletContext application = getServletContext();
        //ovo je način da dohvatim neki fajl unutar Web Pages direktorijuma
        String path = application.getRealPath("products.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            List<Product> products = new ArrayList<>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ";");
                int id = Integer.parseInt(tokenizer.nextToken());
                String productName = tokenizer.nextToken();
                double price = Double.parseDouble(tokenizer.nextToken());
                Product product = new Product(id, productName, price);
                products.add(product);
            }
            application.setAttribute(Constants.PRODUCTS, products);
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletContext application = getServletContext();
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Products</title>");
            out.println("</head>");
            out.println("<body>");
            List<Product> products = (List<Product>) application.getAttribute(Constants.PRODUCTS);
            if (products != null && !products.isEmpty()) {
                out.println("<h2>Dostupni proizvodi</h2>");
                out.println("<table border='1'>");
                out.println("<tr bgcolor='lightgray'><th>Naziv</th><th>Cijena</th><th>Korpa</th></tr>");
                for (Product product : products) {
                    out.println("<tr>");
                    out.println("<td>" + product.getProductName() + "</td>");
                    out.println("<td>" + product.getPrice() + "</td>");
                    out.println("<td></td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<h2>Trenutno nismo u mogućnost da prodajemo artikle na webu</h2>");
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
