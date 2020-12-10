package edu.uark.registerapp.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.EmployeeSignIn;
import edu.uark.registerapp.models.api.Product;
import edu.uark.registerapp.commands.employees.ActiveEmployeeExistQuery;
import edu.uark.registerapp.commands.employees.EmployeeSignInCommand;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.products.ProductsQuery;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/searchResult")
public class SearchResultRouteController extends BaseRouteController {
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView showResult(
		String searchText, 
		HttpServletRequest request
	) {
        List<Product> products = this.productsQuery.execute();
        List<Product> results = new ArrayList<Product>();
        for (Product product : products) {
            if (product.getLookupCode().contains(searchText)) {
                results.add(product);
            }
        }
        if (results.size() == 0) {
            return new ModelAndView(ViewNames.SEARCH_RESULT.getViewName())
                .addObject("noResult", true).addObject("searchText", searchText);
        } else {
            return new ModelAndView(ViewNames.SEARCH_RESULT.getViewName())
                .addObject("noResult", false).addObject("searchText", searchText).addObject("products", results);
        }
	}

	// Properties
    @Autowired
    private ProductsQuery productsQuery;
}

