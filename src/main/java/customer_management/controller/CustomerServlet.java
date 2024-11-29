package customer_management.controller;

import customer_management.model.Customer;
import customer_management.service.CustomerService;
import customer_management.service.CustomerServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {
	private final CustomerService customerService = new CustomerServiceImpl();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String action = request.getParameter("action");

		if (action == null) {
			action = "";
		}
		switch (action) {
			case "create":
				createCustomer(request, response);
				break;
			case "edit":
				updateCustomer(request, response);
				break;
			case "delete":
				deleteCustomer(request, response);
				break;
			default:
				break;
		}
	}

	private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Customer customer = customerService.findById(id);
		RequestDispatcher dispatcher;
		if (customer == null) {
			dispatcher = request.getRequestDispatcher("error-404.jsp");
		} else {
			customerService.remove(id);
			try {
				response.sendRedirect("/customers");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		Customer customer = customerService.findById(id);
		RequestDispatcher dispatcher;
		if (customer == null) {
			dispatcher = request.getRequestDispatcher("error-404.jsp");
		}
		else {
			customer.setName(name);
			customer.setEmail(email);
			customer.setAddress(address);
			customerService.update(id, customer);
			request.setAttribute("customer", customer);
			request.setAttribute("message", "Customer information was updated");
			dispatcher = request.getRequestDispatcher("customer/edit.jsp");
			try {
				dispatcher.forward(request, response);
			} catch (ServletException | IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void createCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		int id = (int) (Math.random() * 10000);

		Customer customer = new Customer(id, name, email, address);
		customerService.save(customer);
		RequestDispatcher dispatcher = request.getRequestDispatcher("customer/create.jsp");
		request.setAttribute("message", "New customer was created");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String action = request.getParameter("action");
		if (action == null) {
			action = "";
		}
		switch (action) {
			case "create":
				showCreateForm(request, response);
				break;
			case "edit":
				showEditForm(request, response);
				break;
			case "delete":
				showDeleteForm(request, response);
				break;
			case "view":
				viewCustomer(request, response);
				break;
			default:
				listCustomers(request, response);
				break;
		}
	}

	private void viewCustomer(HttpServletRequest request, HttpServletResponse response) {
		int id = Integer.parseInt(request.getParameter("id"));
		Customer customer = customerService.findById(id);
		RequestDispatcher dispatcher;
		if (customer == null) {
			dispatcher = request.getRequestDispatcher("error-404.jsp");
		} else {
			request.setAttribute("customer", customer);
			dispatcher = request.getRequestDispatcher("customer/view.jsp");
		}
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void showDeleteForm(HttpServletRequest request, HttpServletResponse response) {
		int id = Integer.parseInt(request.getParameter("id"));
		Customer customer = customerService.findById(id);
		RequestDispatcher dispatcher;
		if (customer == null) {
			dispatcher = request.getRequestDispatcher("error-404.jsp");
		} else {
			request.setAttribute("customer", customer);
			dispatcher = request.getRequestDispatcher("customer/delete.jsp");
		}
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response) {
		int id = Integer.parseInt(request.getParameter("id"));
		Customer customer = customerService.findById(id);
		RequestDispatcher dispatcher;
		if (customer == null) {
			dispatcher = request.getRequestDispatcher("error-404.jsp");
		} else {
			request.setAttribute("customer", customer);
			dispatcher = request.getRequestDispatcher("customer/edit.jsp");
		}
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void showCreateForm(HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher dispatcher = request.getRequestDispatcher("customer/create.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void listCustomers(HttpServletRequest request, HttpServletResponse response) {
		List<Customer> customers = customerService.findAll();
		request.setAttribute("customers", customers);

		RequestDispatcher dispatcher = request.getRequestDispatcher("customer/list.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
